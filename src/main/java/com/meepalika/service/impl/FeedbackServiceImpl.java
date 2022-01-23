package com.meepalika.service.impl;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.meepalika.dao.FeedbackDAO;
import com.meepalika.entity.Feedback;
import com.meepalika.exception.ApplicationException;
import com.meepalika.exception.ResourceNotFoundException;
import com.meepalika.localization.Translator;
import com.meepalika.response.ApiResponse;
import com.meepalika.response.ApiResponse.CODES;
import com.meepalika.service.FeedbackService;

@Service
public class FeedbackServiceImpl extends BaseServiceImpl implements FeedbackService {
	private static final Logger logger = LoggerFactory.getLogger(FeedbackServiceImpl.class);

	@Autowired
	private FeedbackDAO feedbackDAO;

	@Override
	public ApiResponse saveFeedback(@Valid Feedback feedback) {
		logger.info("Executing saveFeedback() in user service");
		ApiResponse apiResponse = null;
		try {
			feedbackDAO.save(feedback);
		} catch (DataAccessException e) {
			String message = Translator.toLocale("exception_raised_create_feedback");
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ApplicationException(message, e, apiResponse);
		}
		apiResponse = generateApiResponse(CODES.SUCCESS, Translator.toLocale("feedback_created_successfully"));
		return apiResponse;
	}

	@Override
	public Feedback getFeedbackById(Integer id) {
		logger.info("Feedback service to get feedback by id");
		Feedback feedback = null;
		ApiResponse apiResponse = null;
		try {
			feedback = feedbackDAO.findById(id);
		} catch (DataAccessException ex) {
			apiResponse = generateApiResponse(CODES.FAILURE, Translator.toLocale("exception_raised_fetch_feedback"));
			throw new ApplicationException(Translator.toLocale("exception_raised_fetch_feedback"), ex, apiResponse);
		}
		if (feedback == null) {
			String message = Translator.toLocale("no_feedback_found") + " " + id;
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ResourceNotFoundException(message, apiResponse);
		}
		return feedback;
	}

	@Override
	public List<Feedback> getFeedbackByUserId(int id) {
		logger.info("Feedback service to get feedback by userid");
		List<Feedback> feedbackList = null;
		ApiResponse apiResponse = null;
		try {
			feedbackList = feedbackDAO.findAllByUserIdOrderByIdDesc(id);
		} catch (DataAccessException ex) {
			apiResponse = generateApiResponse(CODES.FAILURE, Translator.toLocale("exception_raised_fetch_feedback"));
			throw new ApplicationException(Translator.toLocale("exception_raised_fetch_feedback"), ex, apiResponse);
		}
		if (feedbackList.isEmpty()) {
			String message = Translator.toLocale("no_feedback_found") + " " + id;
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ResourceNotFoundException(message, apiResponse);
		}
		return feedbackList;
	}

}
