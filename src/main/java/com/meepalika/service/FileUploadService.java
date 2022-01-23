package com.meepalika.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.meepalika.entity.UserFileUploadEntity;
import com.meepalika.response.ApiResponse;

public interface FileUploadService {


	public UserFileUploadEntity getUserFileById(long id);

	public List<UserFileUploadEntity> getAllUserFileByUserIdAndSlotId(long userId, int slotId);

	public ApiResponse deleteuserFileById(long id);

	public ApiResponse deleteuserFileByUserIdAndSlotId(long userId, int slotId);

	public String updateUserWithProfile(long id, MultipartFile file);

	public String updateAccountLogo(long id, MultipartFile file);


}
