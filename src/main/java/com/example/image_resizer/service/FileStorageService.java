package com.example.image_resizer.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.image_resizer.utils.MinioConnector;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileStorageService {

	@Autowired
	public FileStorageService() {
	}

	public String uploadToMinio(String base64Data, String extension, String contentType, String serviceName) {

		MinioClient minioClient = MinioConnector.connect();
		Random rand = new Random();
		int randomInt = rand.nextInt(10000);
		long currentTime = System.currentTimeMillis();
		String objectName = randomInt + "" + currentTime + "." + extension;
		String bucketName = serviceName;

		// create a new bucket if not exists
		createBucketIfNotExists(minioClient, bucketName);

		// Decode the Base64 string
		byte[] imageBytes = Base64.getDecoder().decode(base64Data);

		// Upload the image
		try (InputStream is = new ByteArrayInputStream(imageBytes)) {
			minioClient.putObject(
					PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(is, imageBytes.length, -1) 
							.contentType(contentType) // Set the content type
							.build());
		} catch (Exception e) {
			log.error("Error occured while uploading image - root cause: {}", e.getMessage());
		}

		return objectName;
	}

	private void createBucketIfNotExists(MinioClient minioClient, String bucketName) {

		try {
			// Check if the bucket already exists
			boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
			if (!isExist) {
				// Create the bucket if it does not exist
				minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
			}
		} catch (MinioException e) {
			log.error("MinioException occured while creating bucket - root cause: {}", e.getMessage());
		} catch (InvalidKeyException e) {
			log.error("InvalidKeyException occured while creating bucket - root cause: {}", e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			log.error("NoSuchAlgorithmException occured while creating bucket - root cause: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			log.error("IllegalArgumentException occured while creating bucket - root cause: {}", e.getMessage());
		} catch (IOException e) {
			log.error("IOException occured while creating bucket - root cause: {}", e.getMessage());
		}
	}
}
