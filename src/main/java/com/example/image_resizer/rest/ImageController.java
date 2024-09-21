package com.example.image_resizer.rest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.image_resizer.service.ImageService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class ImageController {

	@Autowired
	private ImageService imageService;

	@PostMapping("/v1/upload")
    public ResponseEntity<String> upload(@RequestBody ImageRequest imageRequest) {
    	log.info("REST request to upload image");
    	
		JSONObject response = new JSONObject();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    	
    	String imageUrl = imageService.uploadImage(imageRequest);
    	
    	response.put("status", true);
    	response.put("status_code", 200);
    	response.put("message", "SUCCESS");
    	response.put("data", imageUrl);
    	
    	return new ResponseEntity<>(response.toString(), httpHeaders, HttpStatus.OK);
    }
}
