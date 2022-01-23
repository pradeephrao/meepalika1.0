package com.meepalika.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Utils {
	public static final String LIKE = "%";

	public static boolean isEmpty(String str) {
		return str == null || "".equals(str.trim());
	}

	public static String trimWithNullCheck(String str) {
		if (str == null)
			return null;
		else {
			String res = str.trim();
			return "".equals(res) ? null : res;
		}
	}

	public static String checkNull(String str) {
		if (str == null) {
			return "";
		} else {
			return trimWithNullCheck(str);
		}
	}

	public static String getLikeParamValue(String param, boolean trailing) {
		StringBuilder sb = new StringBuilder("");
		if (!trailing) {
			sb.append(LIKE);
		}
		sb.append(param).append(LIKE);
		return sb.toString();
	}

	public static String getLikeParamsValue(Integer num, boolean trailing) {
		StringBuilder sb = new StringBuilder("");
		if (!trailing) {
			sb.append(LIKE);
		}
		sb.append(num).append(LIKE);
		return sb.toString();
	}

	public static float getRandomFloatNumber(int max, int min) {
		Random r = new Random();
		return (min + r.nextFloat() * (max - min));
	}

	public static String getAlphaNumericString(int n) {

		// chose a Character random from this String
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";

		// create StringBuffer size of AlphaNumericString
		StringBuilder sb = new StringBuilder(n);

		for (int i = 0; i < n; i++) {

			// generate a random number between
			// 0 to AlphaNumericString variable length
			int index = (int) (AlphaNumericString.length() * Math.random());

			// add Character one by one in end of sb
			sb.append(AlphaNumericString.charAt(index));
		}

		return sb.toString();
	}

	public static Date getDate(String dateInString) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		// String dateInString = "7-Jun-2013";
		Date date = null;
		try {

			date = formatter.parse(dateInString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date getStartOfYear(int year) {

		Calendar calendarStart = Calendar.getInstance();
		calendarStart.set(Calendar.YEAR, year);
		calendarStart.set(Calendar.MONTH, 0);
		calendarStart.set(Calendar.DAY_OF_MONTH, 1);
		Date startDate = calendarStart.getTime();

		return startDate;
	}

	public static Date getEndOfYear(int year) {

		Calendar calendarEnd = Calendar.getInstance();
		calendarEnd.set(Calendar.YEAR, year);
		calendarEnd.set(Calendar.MONTH, 11);
		calendarEnd.set(Calendar.DAY_OF_MONTH, 31);
		Date endDate = calendarEnd.getTime();
		return endDate;
	}

	public static java.sql.Timestamp getTimeStamp() {
		Instant instant = Instant.now();
		long timeStampMillis = instant.toEpochMilli();
		return new java.sql.Timestamp(timeStampMillis);

	}

	public static String getDateTime() {
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String now = sdf.format(date);
		return now;
	}

	public static Date getMinMaxTimeForDate(Date date, Boolean startOfDay) {
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		if (startOfDay) {
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
		} else {
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.HOUR_OF_DAY, 23);
		}

		try {
			String format = sdf.format(calendar.getTime());
			date = sdf.parse(format);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return date;
	}

	public static String getTextAfterLastSpecialCharachter(String text, String specialCharachter) {
		if (text.lastIndexOf(specialCharachter) != -1) {
			return text.substring(text.lastIndexOf(specialCharachter) + 1);
		}
		return text;
	}

	public static String getTextIncludingLastSpecialCharachter(String text, String specialCharachter) {
		if (text.lastIndexOf(specialCharachter) != -1) {
			return text.substring(text.lastIndexOf(specialCharachter));
		}
		return text;
	}

	public static <T> List<T> convertObjectToList(Object obj) {
		List<T> list = new ArrayList<>();
		if (obj.getClass().isArray()) {
			list = (List<T>) Arrays.asList((Object[]) obj);
		} else if (obj instanceof Collection) {
			list = new ArrayList<>((Collection<T>) obj);
		}
		return list;
	}

	public static String getInvoiceDate() {
		DateFormat sdf = new SimpleDateFormat("ddMMyyyy");
		Date date = new Date();
		String now = sdf.format(date);
		return now;
	}

	public static String formatDate(Date dateInput) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		String date = formatter.format(dateInput);
		return date;
	}
	
	public static String formatDBDate(Date dateInput) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String date = formatter.format(dateInput);
		return date;
	}
	
	public static String getDate() {
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String now = sdf.format(date);
		return now;
	}
	
	public static List<LocalDate> getAllDatesBetweenTwoDates(LocalDate startDate, LocalDate endDate){
		    
		    List<LocalDate> totalDates = new ArrayList<>();
		    while (!startDate.isAfter(endDate)) {
		        totalDates.add(startDate);
		        startDate = startDate.plusDays(1);
		    }
		    return totalDates;
	}
	
	public static Boolean checkIfDateIsAfterCurrentDate(Date date) {
		if(Utils.formatDBDate(date).compareTo(Utils.getDate()) >= 0){
			return true;
		}else {
			return false;
		}
	}
	
	public static Boolean checkIfStartDateIsBeforeEndDate(Date startDate, Date endDate) {
		if(Utils.formatDBDate(startDate).compareTo(Utils.formatDBDate(endDate)) <= 0){
			return true;
		}else {
			return false;
		}
	}
	
	public static Boolean checkIfTimeIsAfterCurrentTime(LocalTime time) {
		if(time.compareTo(LocalTime.now()) >= 0){
			return true;
		}else {
			return false;
		}
	}
	
	public static Boolean checkIfStartTimeIsBeforeEndTime(LocalTime startTime, LocalTime endTime) {
		if(startTime.compareTo(endTime) <= 0){
			return true;
		}else {
			return false;
		}
	}
	
}
