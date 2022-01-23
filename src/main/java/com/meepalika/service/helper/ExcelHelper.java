package com.meepalika.service.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class ExcelHelper {
	
	private static final Logger logger = LoggerFactory.getLogger(ExcelHelper.class);
	
	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	static String DOCTOR_SCHEDULE = "doctor_schedule";
	
	public static boolean hasExcelFormat(MultipartFile file) {
	    if (!TYPE.equals(file.getContentType())) 
	      return false;
	    return true;
	}


	public static String checkForLocalTimeFormat(String localTime) {
		String[]  h = localTime.split(":");
   	 	if(h[0].length() == 1)
   	 		h[0] = "0" + h[0];
   	 	StringBuffer val = new StringBuffer();
   	 	for (int i = 1; i <= h.length; i++) {
   	 		val = val.append(h[i-1]);
   	 		if(i < h.length)
   			 val.append(":");
		}
   	 	return val.toString();
	}
	

}
