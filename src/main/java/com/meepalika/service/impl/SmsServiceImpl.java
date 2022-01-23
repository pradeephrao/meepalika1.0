package com.meepalika.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.meepalika.exception.ApplicationException;
import com.meepalika.localization.Translator;
import com.meepalika.response.ApiResponse;
import com.meepalika.response.ApiResponse.CODES;
import com.meepalika.service.SmsService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class SmsServiceImpl extends BaseServiceImpl implements SmsService {

	private static final Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);

	@Value("${twilio.account_sid}")
	private String account_sid;
	@Value("${twilio.auth_token}")
	private String auth_token;
	@Value("${twilio.from_phone}")
	private String from_phone;
	@Value("${app.google.playstore}")
	private String playstoreLink;

	@Override
	public void sendSms(String to_phone, String content) {
		logger.debug("Sending sms...");
		ApiResponse apiResponse = null;
		try {
			Twilio.init(account_sid, auth_token);
			StringBuilder messageContent = new StringBuilder();
			messageContent.append(Translator.toLocale("prefix_text")).append("\n\n").append(content).append("\n\n").append(Translator.toLocale("download_app")).append("\n").append(playstoreLink);
			Message.creator(new com.twilio.type.PhoneNumber(to_phone), new PhoneNumber(from_phone), messageContent.toString()).create();
		} catch (Exception e) {
			apiResponse = new ApiResponse(CODES.FAILURE, Translator.toLocale("sms_failed"));
			throw new ApplicationException(Translator.toLocale("sms_failed"), apiResponse);
		}
	}

}
