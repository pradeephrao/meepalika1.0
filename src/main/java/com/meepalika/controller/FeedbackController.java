package com.meepalika.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.meepalika.entity.Feedback;
import com.meepalika.response.ApiResponse;
import com.meepalika.service.FeedbackService;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

	private static final Logger logger = LoggerFactory.getLogger(FeedbackController.class);

	@Autowired
	private FeedbackService feedbackService;

	@PostMapping("create")
	public ResponseEntity<ApiResponse> saveFeedback(@RequestBody @Valid Feedback feedback) {
		logger.info("Executing saveFeedback() in feedback controller");
		return new ResponseEntity<ApiResponse>(feedbackService.saveFeedback(feedback), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Feedback> getFeedbackById(@PathVariable int id) {
		logger.info("Executing getFeedbackById controller");
		Feedback feedback = null;
		feedback = feedbackService.getFeedbackById(id);
		return new ResponseEntity<Feedback>(feedback, HttpStatus.OK);
	}

	@GetMapping("/allFeedbacks/{id}")
	public ResponseEntity<List<Feedback>> getFeedbackByUserId(@PathVariable int id) {
		logger.info("Executing getFeedbackByUserId controller");
		List<Feedback> feedbackList = null;
		feedbackList = feedbackService.getFeedbackByUserId(id);
		return new ResponseEntity<List<Feedback>>(feedbackList, HttpStatus.OK);
	}

}
