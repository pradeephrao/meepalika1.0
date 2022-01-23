package com.meepalika.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.meepalika.controller.helper.UserContollerHelper;
import com.meepalika.entity.UserFileUploadEntity;
import com.meepalika.response.ApiResponse;
import com.meepalika.response.ApiResponse.CODES;
import com.meepalika.service.FileUploadService;

@RestController
@RequestMapping("/fileUpload")

public class FileUploadController {
	private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

	@Autowired
	FileUploadService fileUploadService;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private UserContollerHelper userControllerHelper;
	/*
	 * public ResponseEntity<ApiResponse> createDoctorSchedule(@RequestBody @Valid
	 * List<DoctorSchedule> doctorShedule) {
	 * logger.info("DoctorAppointment to be created"); ApiResponse apiResponse = new
	 * ApiResponse(CODES.FAILURE); apiResponse =
	 * fileUploadService.createDoctorShedule(doctorShedule); return new
	 * ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK); }
	 *
	 * public ResponseEntity<List<DoctorSchedule>> getAllSlots() {
	 * logger.info("DoctorSchedule service to be called"); List<DoctorSchedule>
	 * doctorSchedule = null; doctorSchedule =
	 * fileUploadService.getAllDoctorSchedule(); return new
	 * ResponseEntity<List<DoctorSchedule>>(doctorSchedule, HttpStatus.OK);
	 *
	 * }
	 */

	@PostMapping(value = "/userFileUpload", consumes = {MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<ApiResponse> userFileUploads(@RequestPart @Valid String userMedia,
													   @RequestParam("files") MultipartFile[] files) {
		logger.info("Executing fileUpload method in controller");
		ApiResponse apiResponse = new ApiResponse(CODES.FAILURE);
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
	}

	@GetMapping("/getuserFileById/{id}")
	public ResponseEntity<UserFileUploadEntity> getUserFileById(@PathVariable int id) {
		logger.info("fileUpload service to be called");
		UserFileUploadEntity userFileUploadEntity = null;
		userFileUploadEntity = fileUploadService.getUserFileById(id);
		return new ResponseEntity<UserFileUploadEntity>(userFileUploadEntity, HttpStatus.OK);
	}

	@GetMapping("/getAllUserFileByUserIdAndSlotId/{userId}/{slotId}")
	public ResponseEntity<List<UserFileUploadEntity>> getAllUserFileByUserIdAndSlotId(@PathVariable int userId,
																					  @PathVariable int slotId) {
		logger.info("fileUpload service to be called");
		List<UserFileUploadEntity> userFileUploadEntityList = null;
		userFileUploadEntityList = fileUploadService.getAllUserFileByUserIdAndSlotId(userId, slotId);
		return new ResponseEntity<List<UserFileUploadEntity>>(userFileUploadEntityList, HttpStatus.OK);

	}

	@DeleteMapping("/deleteuserFileById/{id}")
	public ResponseEntity<ApiResponse> deleteuserFileById(@PathVariable int id) {
		logger.info("fileUpload service to be called");
		ApiResponse apiResponse = null;
		apiResponse = fileUploadService.deleteuserFileById(id);
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
	}

	@DeleteMapping("/deleteuserFileByUserIdAndSlotId/{userId}/{slotId}")
	public ResponseEntity<ApiResponse> deleteuserFileByUserIdAndSlotId(@PathVariable int userId,
																	   @PathVariable int slotId) {
		logger.info("fileUpload service to be called");
		ApiResponse apiResponse = null;
		apiResponse = fileUploadService.deleteuserFileByUserIdAndSlotId(userId, slotId);
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
	}


}
