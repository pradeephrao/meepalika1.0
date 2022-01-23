package com.meepalika.localization;

import java.text.MessageFormat;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

@Component
public class Translator {

	private static ResourceBundleMessageSource messageSource;

	@Autowired
	Translator(ResourceBundleMessageSource messageSource) {
		Translator.messageSource = messageSource;
	}

	public static String toLocale(String msgCode) {
		Locale locale = LocaleContextHolder.getLocale();
		return messageSource.getMessage(msgCode, null, locale);
	}

	public static String setPlaceHoldersWithLocale(String msgCode, Object args) {
		return MessageFormat.format(toLocale(msgCode), args);
	}
	
	public static String setPlaceHoldersWithLocale(String msgCode, Object[] args) {
		return MessageFormat.format(toLocale(msgCode), args);
	}


	public static String setPlaceHoldersWithLocale(String msgCode, Object arg1, Object arg2) {
		return MessageFormat.format(toLocale(msgCode), arg1, arg2);
	}
}
