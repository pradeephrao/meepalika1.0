package com.meepalika.service.helper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.meepalika.dto.EmailContentDto;
import com.meepalika.localization.Translator;

@Component
public class EmailServiceHelper {

	
	public EmailContentDto prepareEmailContentForBackgroundProcess(List<String> errorMessages, String subjectTitle) {
		EmailContentDto emailContentDto = null;
		if(errorMessages != null && !errorMessages.isEmpty()) {
			String subject = Translator.toLocale(subjectTitle) ;
		
			StringBuilder builder = new StringBuilder();
			builder.append(Translator.toLocale("account_admin_account_create_body") + System.lineSeparator() + System.lineSeparator());
			int index = 0;
			for(String message : errorMessages) {
				builder.append((++index) + ". " + message).append(System.lineSeparator());
			}
			emailContentDto = new EmailContentDto();
			emailContentDto.setSubject(subject);
			emailContentDto.setBody(builder.toString());
			
		}
		return emailContentDto;
	}
	
	public EmailContentDto prepareEmailContentForBackgroundProcess(String successMessage,  String operation) {
		EmailContentDto emailContentDto = null;
		if(successMessage != null && !successMessage.isEmpty()) {
			String subject = Translator.setPlaceHoldersWithLocale("background_operation_success_title", operation);
			StringBuilder builder = new StringBuilder();
			builder.append(Translator.toLocale("account_admin_account_create_body") + System.lineSeparator() + System.lineSeparator());
			builder.append(successMessage).append(System.lineSeparator());
			emailContentDto = new EmailContentDto();
			emailContentDto.setSubject(subject);
			emailContentDto.setBody(builder.toString());
		}
		return emailContentDto;
	}
	
	
}
