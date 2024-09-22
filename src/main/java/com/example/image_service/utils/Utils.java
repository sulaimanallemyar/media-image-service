package com.example.image_service.utils;

import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.binary.Base64;

public class Utils {

	public static boolean isBase64ImageInvalid(String image) {
		String encodedImg = image;
		String partSeparator = ",";

		if (image.contains(partSeparator)) {
			encodedImg = image.split(partSeparator)[1];
		}

		try {
			Base64.decodeBase64(encodedImg.getBytes(StandardCharsets.UTF_8));
			return false;
		} catch (Exception e) {
			return true;
		}
	}
}
