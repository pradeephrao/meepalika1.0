package com.meepalika.threads;

import java.util.Set;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.meepalika.constants.NumericConstants;
import com.meepalika.dto.EmailContentDto;
import com.meepalika.exception.ApplicationException;
import com.meepalika.localization.Translator;
import com.meepalika.response.ApiResponse;
import com.meepalika.response.ApiResponse.CODES;
import com.meepalika.service.UserService;
import com.meepalika.utils.Utils;

@Service
public class EmailService {

	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private UserService userService;

	@Async
	public void sendEmail(EmailContentDto emailContentDto) {
		try {
			if (!Utils.isEmpty(emailContentDto.getTo())) {
				MimeMessage message = javaMailSender.createMimeMessage();  
		        MimeMessageHelper helper = new MimeMessageHelper(message, true);  
		        helper.setTo(InternetAddress.parse(emailContentDto.getTo()));  
		        if(!Utils.isEmpty(emailContentDto.getCc())) {
			        helper.setCc(InternetAddress.parse(emailContentDto.getCc()));
		        }
                if(!Utils.isEmpty(emailContentDto.getBcc())) {
    		        helper.setBcc(InternetAddress.parse(emailContentDto.getBcc()));
		        }
		        helper.setSubject(emailContentDto.getSubject());  
		        helper.setText(emailContentDto.getBody());  
				javaMailSender.send(message);
			}
		} catch (Exception ex) {
			String message = Translator.toLocale("exception_raised_sending_email");
			ApiResponse apiResponse = new ApiResponse(CODES.FAILURE, message);
			throw new ApplicationException(Translator.toLocale("exception_raised_sending_email"), ex, apiResponse);
		}
	}

	@Async
	public void sendEmailToAdmins(String subject, String text) {
		logger.info("Executing sendEmailToAdmins()");
		Set<String> superAdmins = null;
		try {
			superAdmins = userService.fetchAllSuperAdminEmails(NumericConstants.SUPER_ADMIN_ID);
			String toEmail = !superAdmins.isEmpty() ? String.join(",", superAdmins) : null;
			logger.info("Admin emails " + toEmail);
			if (!Utils.isEmpty(toEmail)) {
				SimpleMailMessage msg = new SimpleMailMessage();
				msg.setTo(toEmail);
				msg.setSubject(subject);
				msg.setText(text);
				javaMailSender.send(msg);
			}
		} catch (Exception ex) {
			String message = Translator.toLocale("exception_raised_sending_email");
			ApiResponse apiResponse = new ApiResponse(CODES.FAILURE, message);
			throw new ApplicationException(message, ex, apiResponse);
		}
	}
	
	@Async
	public void sendEmailToAccountAdmins(String subject, String text, Integer accountId) {
		logger.info("Executing sendEmailToAdmins()");
		Set<String> superAdmins = null;
		try {
			superAdmins = userService.fetchAllAccountAdminEmails(accountId);
			String toEmail = !superAdmins.isEmpty() ? String.join(",", superAdmins) : null;
			logger.info("Admin emails " + toEmail);
			if (!Utils.isEmpty(toEmail)) {
				MimeMessage message = javaMailSender.createMimeMessage();  
		        MimeMessageHelper helper = new MimeMessageHelper(message, true);  
		        helper.setTo(InternetAddress.parse(toEmail));  
		        helper.setSubject(subject);  
		        helper.setText(text);  
				javaMailSender.send(message);
			}
		} catch (Exception ex) {
			String message = Translator.toLocale("exception_raised_sending_email");
			ApiResponse apiResponse = new ApiResponse(CODES.FAILURE, message);
			throw new ApplicationException(message, ex, apiResponse);
		}
	}
	
	

}
