package com.meepalika.service.impl;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.meepalika.dao.AccountDAO;

import com.meepalika.dao.UserDAO;
import com.meepalika.dao.UserFileUploadDAO;
import com.meepalika.entity.Account;
import com.meepalika.entity.User;
import com.meepalika.entity.UserFileUploadEntity;
import com.meepalika.exception.ApplicationException;
import com.meepalika.exception.ResourceNotFoundException;
import com.meepalika.localization.Translator;
import com.meepalika.model.UserMedia;
import com.meepalika.response.ApiResponse;
import com.meepalika.response.ApiResponse.CODES;
import com.meepalika.service.FileUploadService;
import com.meepalika.utils.MultiPartFileUtils;
import com.meepalika.utils.Utils;

@Service
public class FileUploadServiceImpl extends BaseServiceImpl implements FileUploadService {

	private static final Logger logger = LoggerFactory.getLogger(FileUploadServiceImpl.class);


	@Autowired
	UserFileUploadDAO userFileUploadDao;

	@Autowired
	UserDAO userDao;

	@Autowired
	AccountDAO accountDAO;

	@Value("${root.storage.path}")
	private String rootStoragePath;

	public ApiResponse insertUserFileUploads(UserMedia userMedia, MultipartFile[] files) {
		logger.info("File Upload service to saveAll files info to DB");
		ApiResponse apiResponse = null;
		List<UserFileUploadEntity> userFileUploadsList = new ArrayList<UserFileUploadEntity>();
		Set<String> fileNames = null;
		try {
			if (files.length > 0) {
				User userObj = populateUserAuditInfo(Long.parseLong(MDC.get("loggedInUser")));
				Arrays.asList(files).stream().forEach(file -> {
					UserFileUploadEntity fileUpload = new UserFileUploadEntity();
					fileUpload.setOriginal_filename(file.getOriginalFilename());
					fileUpload.setFile_type(MultiPartFileUtils.getExtensionOfFile(file));
					fileUpload.setMedia_type(userMedia.getMediaType());
					fileUpload.setFile_size(MultiPartFileUtils.bytesToMegaBytes(file.getSize()));
					User user = new User();
					user.setId(userMedia.getUserId());
					fileUpload.setUser(user);
					if (uploadFiles(file, fileUpload)) {
						userFileUploadsList.add(fileUpload);
					}
				});
				fileNames = userFileUploadDao.saveAll(userFileUploadsList).stream().filter(Objects::nonNull)
						.map(item -> item.getOriginal_filename().trim() + "." + item.getFile_type().trim())
						.collect(Collectors.toSet());
			} else {
				String message = Translator.toLocale("files_not_present");
				apiResponse = generateApiResponse(CODES.FAILURE, message);
				throw new ApplicationException(message, apiResponse);
			}

		} catch (DataAccessException ex) {
			deleteFiles(userMedia.getMediaType(), fileNames);
			String message = Translator.toLocale("exception_raised_save_files");
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ApplicationException(message, ex, apiResponse);
		}
		apiResponse = generateApiResponse(CODES.SUCCESS, Translator.toLocale("files_uploaded_successfully"));
		return apiResponse;

	}

	@Override
	public UserFileUploadEntity getUserFileById(long id) {
		logger.info("media service to getUserFileByIdgetUserFileById");
		UserFileUploadEntity userFileUploadEntity = null;
		Optional<UserFileUploadEntity> userFileUploadEntityOpt = null;
		ApiResponse apiResponse = null;
		String storagePath = null;
		try {
			userFileUploadEntityOpt = userFileUploadDao.findById(id);
			userFileUploadEntity = userFileUploadEntityOpt.get();
			if (userFileUploadEntity != null) {
				storagePath = MultiPartFileUtils
						.getNormalizedPath(Utils.getTextIncludingLastSpecialCharachter(rootStoragePath, "/")
								+ File.separator + userFileUploadEntity.getMedia_type() + File.separator
								+ userFileUploadEntity.getOriginal_filename() + "."
								+ userFileUploadEntity.getFile_type());
				userFileUploadEntity.setStorage_path(storagePath);
			}
		} catch (DataAccessException ex) {
			String message = Translator.toLocale("exception_raised_fetch_media_info");
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ApplicationException(Translator.toLocale("exception_raised_fetch_media_info"), ex, apiResponse);
		}

		if (userFileUploadEntity == null) {
			String message = Translator.toLocale("media_info_failed_id");
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ResourceNotFoundException(message, apiResponse);
		}
		return userFileUploadEntity;
	}

	@Override
	public List<UserFileUploadEntity> getAllUserFileByUserIdAndSlotId(long userId, int slotId) {
		logger.info("media service to get all media by userId and slotId");
		ApiResponse apiResponse = null;
		List<UserFileUploadEntity> userFileUploadEntityList = new ArrayList<>();
		try {
			//userFileUploadEntityList = userFileUploadDao.findByUserIdAndDoctorAppointmentId(userId, slotId);
			userFileUploadEntityList.forEach(userFileUploadEntity -> {
				String storagePath = MultiPartFileUtils
						.getNormalizedPath(Utils.getTextIncludingLastSpecialCharachter(rootStoragePath, "/")
								+ File.separator + userFileUploadEntity.getMedia_type() + File.separator
								+ userFileUploadEntity.getOriginal_filename() + "."
								+ userFileUploadEntity.getFile_type());
				userFileUploadEntity.setStorage_path(storagePath);
			});
		} catch (DataAccessException ex) {
			String message = Translator.toLocale("exception_raised_fetchAll_media_info");
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ApplicationException(message, ex, apiResponse);
		}

		if (userFileUploadEntityList == null || userFileUploadEntityList.isEmpty()) {
			String message = Translator.toLocale("media_info_fetchAll_failed");
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ResourceNotFoundException(message, apiResponse);
		}
		return userFileUploadEntityList;
	}

	@Override
	@Transactional
	public ApiResponse deleteuserFileById(long id) {
		logger.info("fileUpload service to delete UserFile by Id", id);
		ApiResponse apiResponse = null;
		UserFileUploadEntity userFileUploadEntity = null;
		Optional<UserFileUploadEntity> userFileUploadEntityOpt = null;
		Set<String> fileNames = new HashSet<>();
		try {
			if (id != 0) {
				userFileUploadEntityOpt = userFileUploadDao.findById(id);
				userFileUploadEntity = userFileUploadEntityOpt.get();
				fileNames.add(userFileUploadEntity.getOriginal_filename() + "." + userFileUploadEntity.getFile_type());
				deleteFiles(userFileUploadEntity.getMedia_type(), fileNames);
				userFileUploadDao.deleteById(id);
			}
		} catch (DataAccessException ex) {
			String message = Translator.toLocale("exception_raised_delete_media_info");
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ApplicationException(Translator.toLocale("exception_raised_delete_media_info"), ex, apiResponse);
		}
		if (userFileUploadEntity == null) {
			String message = Translator.toLocale("media_info_failed_id");
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ResourceNotFoundException(message, apiResponse);
		}
		apiResponse = generateApiResponse(CODES.SUCCESS, Translator.toLocale("media_info_deleted_successfully"));
		return apiResponse;
	}

	@Override
	@Transactional
	public ApiResponse deleteuserFileByUserIdAndSlotId(long userId, int slotId) {
		logger.info("fileUpload service to delete UserFile by Id", userId, slotId);
		ApiResponse apiResponse = null;
		Set<String> filePaths = new HashSet<>();
		List<UserFileUploadEntity> userFileUploadEntityList = null;
		try {
			if (userId != 0 && slotId != 0) {
			}
		} catch (DataAccessException ex) {
			String message = Translator.toLocale("exception_raised_delete_media_info");
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ApplicationException(Translator.toLocale("exception_raised_delete_media_info"), ex, apiResponse);
		}
		if (userFileUploadEntityList == null || userFileUploadEntityList.isEmpty()) {
			String message = Translator.toLocale("media_info_fetchAll_failed");
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ResourceNotFoundException(message, apiResponse);
		}
		apiResponse = generateApiResponse(CODES.SUCCESS, Translator.toLocale("media_info_deleted_successfully"));
		return apiResponse;
	}

	@Override
	public String updateUserWithProfile(long id, MultipartFile file) {
		logger.info("File upload service to replace media");
		ApiResponse apiResponse = null;
		Set<String> fileNames = new HashSet<>();
		User userDataFromDb = null;
		Optional<User> userDataFromDbOpt = null;
		String uploadedPath = null;
		try {
			userDataFromDbOpt = userDao.findById(id);
			userDataFromDb = userDataFromDbOpt.get();
			if (userDataFromDb != null) {
				if (!Utils.isEmpty(userDataFromDb.getUser_media_storage_path())) {
					// delete the old media file from local folder
					fileNames.add(userDataFromDb.getUser_media_storage_path());
					deleteFiles(fileNames);
				}
				// update media entity with new file data
				uploadedPath = uploadFiles(file, "USER_PROFILE");

			}
		} catch (DataAccessException ex) {
			String message = Translator.toLocale("exception_raised_update_user");
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ApplicationException(message, ex, apiResponse);
		}
		if (userDataFromDb == null) {
			String message = Translator.toLocale("user_fetch_failed_id") + " " + id;
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ResourceNotFoundException(message, apiResponse);
		}

		return uploadedPath;
	}

	@Override
	public String updateAccountLogo(long id, MultipartFile file) {
		logger.info("File upload service to replace account logo");
		ApiResponse apiResponse = null;
		Set<String> fileNames = new HashSet<>();
		Account accountDataFromDb = null;
		Optional<Account> accountDataFromDbOpt = null;
		String uploadedPath = null;
		try {
			if (id != 0) {
				accountDataFromDbOpt = accountDAO.findById(id);
				accountDataFromDb = accountDataFromDbOpt.get();
				if (accountDataFromDb != null) {
					if (!Utils.isEmpty(accountDataFromDb.getLogoStoragePath())) {
						// delete the old media file from local folder
						fileNames.add(accountDataFromDb.getLogoStoragePath());
						deleteFiles(fileNames);
					}
				}
			}

			// update media entity with new file data
			uploadedPath = uploadFiles(file, "ACCOUNT_LOGO");
		} catch (DataAccessException ex) {
			String message = Translator.toLocale("exception_raised_update_accountlogo");
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ApplicationException(message, ex, apiResponse);
		}
		return uploadedPath;
	}


}
