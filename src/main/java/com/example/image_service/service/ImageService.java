package com.example.image_service.service;

import com.example.image_service.rest.ImageRequest;

public interface ImageService {

	String uploadImage(ImageRequest imageRequest);
}
