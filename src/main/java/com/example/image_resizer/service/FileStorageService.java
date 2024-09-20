package com.example.image_resizer.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.image_resizer.utils.MinioConnector;

import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;

@Service
public class FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    @Autowired
    public FileStorageService() {}
    
    public String uploadToMinio(String imageBase64, String extension, String contentType, String serviceName) {
    	
		MinioClient minioClient = MinioConnector.connect();
		Random rand = new Random();
		int randomInt = rand.nextInt(10000);
		long currentTime = System.currentTimeMillis();
		String objectName = randomInt + "" + currentTime + "." + extension;

		String bucketName = serviceName;
		InputStream inputStream = new ByteArrayInputStream(imageBase64.getBytes());

		PutObjectArgs uArgs = PutObjectArgs.builder().bucket(bucketName).object(objectName)
				.stream(inputStream, -1, 1024 * 1024 * 5).contentType(contentType).build();

		try {
			ObjectWriteResponse resp = minioClient.putObject(uArgs);
		} catch (InvalidKeyException e) {
			
			e.printStackTrace();
		} catch (ErrorResponseException e) {
			
			e.printStackTrace();
		} catch (InsufficientDataException e) {
			
			e.printStackTrace();
		} catch (InternalException e) {
			e.printStackTrace();
		} catch (InvalidResponseException e) {
			
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			
			e.printStackTrace();
		} catch (ServerException e) {
			e.printStackTrace();
		} catch (XmlParserException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return objectName;
    }
}
