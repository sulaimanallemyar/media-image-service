package com.example.image_service.service;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.image_service.rest.ImageRequest;

@Service
public class ImageServiceImpl implements ImageService {
	
	@Autowired
	private FileStorageService fileStorageService;

	@Override
	public String uploadImage(ImageRequest imageRequest) {

		String serviceName = imageRequest.getServiceName();
		String fileBase64 = imageRequest.getImageBase64();
		String[] base64String = fileBase64.split(",");
		String attachment = base64String[1];
		String contentType = null;
		int targetWidth = imageRequest.getTargetWidth();
		int targetHeight = imageRequest.getTargetHeight();
		BufferedImage originalImage = null;
		BufferedImage resizedImage = null;
		
		String extension;
		switch (base64String[0]) {
		case "data:image/jpeg;base64":
			extension = "jpeg";
			contentType = "image/jpeg";
			break;
		case "data:image/png;base64":
			extension = "png";
			contentType = "image/png";
			break;
		case "data:image/jpg;base64":
			extension = "jpg";
			contentType = "image/jpg";
			break;
		default:
			extension = null;
			break;
		}

		byte[] imageBytes = Base64.getDecoder().decode(attachment);
		
		try {
			originalImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int originalWidth = originalImage.getWidth();
		int originalHeight = originalImage.getHeight();
		
		// need to resize image
		if((targetWidth != 0 && originalWidth != targetWidth) || (targetHeight != 0 && originalHeight != targetHeight)) {
			
			if(targetWidth == 0) {
				targetWidth = originalWidth;
			}
			
			if(targetHeight == 0) {
				targetHeight = originalHeight;
			}
			
			// Create a resized image
			resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = resizedImage.createGraphics();
			g.drawImage(originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH), 0, 0, null);
			g.dispose();
		}else {
			resizedImage = originalImage;
		}

		// Convert BufferedImage back to Base64 string
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		try {
			ImageIO.write(resizedImage, extension , baos);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		byte[] resizedImageBytes = baos.toByteArray();
		String resizedImageBase64 = Base64.getEncoder().encodeToString(resizedImageBytes);
		String fileUrl = fileStorageService.uploadToMinio(resizedImageBase64, extension, contentType, serviceName);

		return fileUrl;
	}
}
