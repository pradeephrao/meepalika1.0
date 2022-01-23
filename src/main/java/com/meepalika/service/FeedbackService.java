package com.meepalika.service;

import java.util.List;

import javax.validation.Valid;

import com.meepalika.entity.Feedback;
import com.meepalika.response.ApiResponse;

public interface FeedbackService {

	ApiResponse saveFeedback(@Valid Feedback feedback);

	Feedback getFeedbackById(Integer id);

	List<Feedback> getFeedbackByUserId(int id);

}
