package com.example.image_service.rest;

import lombok.Data;

@Data
public class ImageRequest {

	private String imageBase64;
	
	private int targetWidth;
	
	private int targetHeight;
	
	private String serviceName;
}
