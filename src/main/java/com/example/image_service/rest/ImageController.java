package com.example.image_service.rest;

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

import com.example.image_service.rest.errors.BadRequestAlertException;
import com.example.image_service.service.ImageService;
import com.example.image_service.utils.Configuration;
import com.example.image_service.utils.Utils;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping
@Slf4j
public class ImageController {

	@Autowired
	private ImageService imageService;

	@PostMapping("/v1/upload")
	public ResponseEntity<String> upload(@RequestBody ImageRequest imageRequest) {
		log.info("REST request to upload image - serviceName: {}, targetWidth: {}, targetHeight: {}",
				imageRequest.getServiceName(), imageRequest.getTargetWidth(), imageRequest.getTargetHeight());

		JSONObject response = new JSONObject();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		String imageBase64 = imageRequest.getImageBase64();
		String serviceName = imageRequest.getServiceName();
		int targetWidth = imageRequest.getTargetWidth();
		int targetHeight = imageRequest.getTargetHeight();

		if (imageBase64 == null) {

			log.error("Image cannot be null");
			throw new BadRequestAlertException("Image cannot be null");
		} else if (imageBase64.trim().length() == 0) {
			log.error("Image cannot be empty");
			throw new BadRequestAlertException("Image cannot be empty");
		} else if (Utils.isBase64ImageInvalid(imageBase64)) {
			log.error("Image is invalid");
			throw new BadRequestAlertException("Image is invalid");
		}

		if (serviceName == null) {

			log.error("ServiceName cannot be null");
			throw new BadRequestAlertException("ServiceName cannot be null");
		} else if (serviceName.trim().length() == 0) {
			log.error("ServiceName cannot be empty");
			throw new BadRequestAlertException("ServiceName cannot be empty");
		}

		if (targetWidth < 0) {

			log.error("TargetWidth cannot be negative number");
			throw new BadRequestAlertException("TargetWidth cannot be negative number");
		}
		
		if (targetHeight < 0) {

			log.error("TargetHeight cannot be negative number");
			throw new BadRequestAlertException("TargetHeight cannot be negative number");
		}

		String uploadedImageName = imageService.uploadImage(imageRequest);
		String imageUrl = Configuration.MINIO_ENDPOINT.concat("/").concat(serviceName).concat("/")
				.concat(uploadedImageName);

		response.put("status", true);
		response.put("status_code", 200);
		response.put("message", "SUCCESS");
		response.put("data", imageUrl);

		log.info("Rest response to upload image: {}", response.toString());
		return new ResponseEntity<>(response.toString(), httpHeaders, HttpStatus.OK);
	}
}
