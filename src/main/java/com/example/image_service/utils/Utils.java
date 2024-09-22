package com.example.image_service.utils;

import java.util.Base64;

public class Utils {

	public static boolean isBase64ImageInvalid(String image) {
		String encodedImg = image;
		String partSeparator = ",";

		if (image.contains(partSeparator)) {
			encodedImg = image.split(partSeparator)[1];
		}

		try {
			
			Base64.getDecoder().decode(encodedImg);
			return false;
		} catch (Exception e) {
			return true;
		}
	}
}
