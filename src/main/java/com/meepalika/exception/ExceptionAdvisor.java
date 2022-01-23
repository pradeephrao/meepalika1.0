package com.meepalika.exception;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.meepalika.localization.Translator;
import com.meepalika.response.ApiResponse;
import com.meepalika.response.ApiResponse.CODES;
import com.meepalika.service.impl.AccountServiceImpl;

@ControllerAdvice
public class ExceptionAdvisor extends ResponseEntityExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

	@Value("${spring.servlet.multipart.max-file-size}")
	private String maxFileSize;

	@ExceptionHandler(ApplicationException.class)
	public ResponseEntity<?> excpetionHandler(ApplicationException ex, WebRequest request) {
		logger.error(ex.getMessage());
		ex.printStackTrace();
		return new ResponseEntity<>(ex.apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> ResourceNotFoundExceptionHandler(ResourceNotFoundException ex, WebRequest request) {
		logger.error(ex.getMessage());
		return new ResponseEntity<>(ex.apiResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<?> AccessDeniedExceptionHandler(AccessDeniedException ex, WebRequest request) {
		logger.error(ex.getMessage());
		return new ResponseEntity<>(ex.apiResponse, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(JwtTokenInvalidException.class)
	public ResponseEntity<?> JwtTokenInvalidExceptionHandler(JwtTokenInvalidException ex, WebRequest request) {
		ex.printStackTrace();
		logger.error(ex.getMessage());
		SecurityContextHolder.clearContext();
		return new ResponseEntity<>(ex.apiResponse, HttpStatus.UNAUTHORIZED);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
																  HttpHeaders headers, HttpStatus status, WebRequest request) {
		logger.info("Checking validations");
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDate.now());
		body.put("status", status.value());

		List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(x -> x.getDefaultMessage())
				.collect(Collectors.toList());

		body.put("errors", errors);

		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> excpetionHandler(Exception ex, WebRequest request) {
		logger.error("error" + ex.getMessage());
		ex.printStackTrace();
		ApiResponse apiResponse = new ApiResponse(CODES.FAILURE, Translator.toLocale("exception_raised_message"));
		return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
	public ResponseEntity<?> AuthenticationCredentialsNotFoundExceptionHandler(
			AuthenticationCredentialsNotFoundException ex, WebRequest request) {
		logger.error("error" + ex.getMessage());
		ex.printStackTrace();
		ApiResponse apiResponse = new ApiResponse(CODES.FAILURE, ex.getMessage());
		return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<?> MaxUploadSizeExceededException(MaxUploadSizeExceededException ex, WebRequest request) {
		logger.error("error" + ex.getMessage());
		ex.printStackTrace();
		String message = Translator.setPlaceHoldersWithLocale("exception_raised_file_exceed_size", this.maxFileSize);
		ApiResponse apiResponse = new ApiResponse(CODES.FAILURE, message);
		return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
