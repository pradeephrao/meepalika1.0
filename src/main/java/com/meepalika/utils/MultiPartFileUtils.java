package com.meepalika.utils;

import java.nio.file.Paths;
import java.text.DecimalFormat;

import org.springframework.web.multipart.MultipartFile;

public class MultiPartFileUtils {

	public static String getExtensionOfFile(MultipartFile file) {
		String fileExtension = "";
		// Get file Name first
		String fileName = file.getOriginalFilename();

		// If fileName do not contain "." or starts with "." then it is not a valid file
		if (fileName.contains(".") && fileName.lastIndexOf(".") != 0) {
			fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
		}

		return fileExtension;
	}

	public static String bytesToMegaBytes(long bytes) {
		final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
		int digitGroups = (int) (Math.log10(bytes) / Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(bytes / Math.pow(1024, digitGroups)) + " " + units[digitGroups];

	}

	public static String removeExtention(String fileName) {
		if (fileName.indexOf(".") > 0) {
			return fileName.substring(0, fileName.lastIndexOf("."));
		}
		return fileName;
	}

	public static String getNormalizedPath(String text) {
		String path = Paths.get(text).normalize().toString();
		return path.replace("\\", "/");
	}
}
