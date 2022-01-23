package com.meepalika.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import com.meepalika.entity.User;
import com.meepalika.entity.UserFileUploadEntity;
import com.meepalika.exception.ApplicationException;
import com.meepalika.exception.ResourceNotFoundException;
import com.meepalika.localization.Translator;
import com.meepalika.response.ApiResponse;
import com.meepalika.response.ApiResponse.CODES;
import com.meepalika.utils.MultiPartFileUtils;

public class BaseServiceImpl {
	@Value("${root.storage.path}")
	private String rootStoragePath;

	public ApiResponse generateApiResponse(CODES code, String message) {
		ApiResponse apiResponse = new ApiResponse(code, message);
		return apiResponse;
	}

	public ApiResponse generateApiResponse(CODES code, String message, long id) {
		ApiResponse apiResponse = new ApiResponse(code, message, id);
		return apiResponse;
	}

/*	public ApiResponse generateApiResponse(CODES code, String message, Object data) {
		ApiResponse apiResponse = new ApiResponse(code, message, data);
		return apiResponse;
	}*/

	// upload files to the media table with detailed info on the uploaded file.
	public Boolean uploadFiles(MultipartFile file, UserFileUploadEntity userFileUpload) {
		String newFileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + "_"
				+ userFileUpload.getOriginal_filename();
		Path destinationFolder = Paths.get(rootStoragePath + File.separator + userFileUpload.getMedia_type());
		Path fileToBeCopied = Paths
				.get(rootStoragePath + File.separator + userFileUpload.getMedia_type() + File.separator + newFileName);
		createDirectories(destinationFolder.toString());
		try {
			Files.copy(file.getInputStream(), fileToBeCopied);
			userFileUpload.setOriginal_filename(MultiPartFileUtils.removeExtention(newFileName));
			return true;
		} catch (Exception ex) {
			String message = Translator.toLocale("exception_raised_during_fileupload");
			ApiResponse apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ApplicationException(message, ex, apiResponse);
		}
	}

	/*
	 * upload files which returns the uploaded path. Returned value will have full
	 * path including filename Mainly inserted to tables which have only column
	 * about media like user table
	 */

	public String uploadFiles(MultipartFile file, String mediaType) {
		String newFileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + "_"
				+ file.getOriginalFilename();
		Path destinationFolder = Paths.get(rootStoragePath + File.separator + mediaType);
		Path fileToBeCopied = Paths.get(rootStoragePath + File.separator + mediaType + File.separator + newFileName);
		createDirectories(destinationFolder.toString());
		try {
			Files.copy(file.getInputStream(), fileToBeCopied);
			return MultiPartFileUtils.getNormalizedPath(mediaType + File.separator + newFileName);
		} catch (Exception ex) {
			String message = Translator.toLocale("exception_raised_during_fileupload");
			ApiResponse apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ApplicationException(message, ex, apiResponse);
		}
	}

	// method to create directory if doesn't exist
	public void createDirectories(String directoryPath) {
		try {
			Files.createDirectories(Paths.get(directoryPath));
		} catch (IOException ex) {
			String message = Translator.toLocale("exception_raised_during_directory_creation");
			ApiResponse apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ApplicationException(message, ex, apiResponse);
		}
	}

	/*
	 * delete multiple files under same folder Eg : below method deletes multiple
	 * userReports, here mediaType is userReports which is a folder
	 */
	public void deleteFiles(String mediaType, Set<String> fileNames) {
		if (!fileNames.isEmpty()) {
			String destinationFolder = rootStoragePath + File.separator + mediaType;
			fileNames.forEach(filename -> {
				try {
					Files.delete(Paths.get(destinationFolder + File.separator + filename));
				} catch (IOException ex) {
					String message = Translator.toLocale("exception_raised_during_files_deletion");
					ApiResponse apiResponse = generateApiResponse(CODES.FAILURE, message);
					throw new ApplicationException(message, ex, apiResponse);
				}
			});
		} else {
			String message = Translator.toLocale("files_not_present");
			ApiResponse apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ResourceNotFoundException(message, apiResponse);
		}

	}

	// to delete multiple files
	public void deleteFiles(Set<String> filePaths) {
		// file path includes complete path including filename
		if (!filePaths.isEmpty()) {
			filePaths.forEach(filePath -> {
				try {
					Files.delete(Paths.get(rootStoragePath + File.separator + filePath));
				} catch (IOException ex) {
					String message = Translator.toLocale("exception_raised_during_files_deletion");
					ApiResponse apiResponse = generateApiResponse(CODES.FAILURE, message);
					throw new ApplicationException(message, ex, apiResponse);
				}
			});
		} else {
			String message = Translator.toLocale("files_not_present");
			ApiResponse apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ResourceNotFoundException(message, apiResponse);
		}
	}

	public static User populateUserAuditInfo(Long id) {
		User user = new User();
		user.setId(id);
		return user;
	}



}
