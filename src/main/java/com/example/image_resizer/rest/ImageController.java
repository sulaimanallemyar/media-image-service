package com.example.image_resizer.rest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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
    	
    	String imageUrl = imageService.uploadImage(imageRequest);
    	JSONObject jsonResult = new JSONObject();
    	
    	jsonResult.put("status", true);
    	jsonResult.put("status_code", 200);
    	jsonResult.put("message", "SUCCESS");
    	jsonResult.put("data", imageUrl);
    	
    	return ResponseEntity.ok(jsonResult.toString());
    }
}
