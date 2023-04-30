package com.indegene.vem.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.indegene.vem.dto.LabelsDTO;
import com.indegene.vem.service.HomeService;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsRequest;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsResponse;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.rekognition.model.Label;
import software.amazon.awssdk.services.rekognition.model.RekognitionException;

@Service("service1")
public class HomeServiceImplementation implements HomeService {
	

	@Override
	public Map<String, Object> getLabels(String imageSource) {
		Map<String, Object> imageAnalyzation = new HashMap<>();
		try {
			List<LabelsDTO> labelDtos = new ArrayList<>();
			String sourceImage = imageSource;
			Region region = Region.US_EAST_1;
			RekognitionClient rekClint = RekognitionClient.builder()
					.region(region)
					.credentialsProvider(ProfileCredentialsProvider.create())
					.build();
			labelDtos = detectImageLabels(rekClint, sourceImage);
			rekClint.close();
			imageAnalyzation.put("labels", labelDtos);
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
		}
		return imageAnalyzation;
	}
	
	 public static List<LabelsDTO> detectImageLabels(RekognitionClient rekClient, String sourceImage) {

		 List<LabelsDTO> labelDtos = new ArrayList<>();
	        try {
	            InputStream sourceStream = new FileInputStream(sourceImage);
	            SdkBytes sourceBytes = SdkBytes.fromInputStream(sourceStream);

	            // Create an Image object for the source image.
	            Image souImage = Image.builder()
	                .bytes(sourceBytes)
	                .build();

	            DetectLabelsRequest detectLabelsRequest = DetectLabelsRequest.builder()
	                .image(souImage)
	                .maxLabels(10)
	                .build();

	            DetectLabelsResponse labelsResponse = rekClient.detectLabels(detectLabelsRequest);
	            List<Label> labels = labelsResponse.labels();
	            for (Label label: labels) {
	                LabelsDTO dto = new LabelsDTO();
	                dto.setLabel(label.name());
	                dto.setConfidence(label.confidence());
	                labelDtos.add(dto);
	            }

	        } catch (RekognitionException | FileNotFoundException e) {
	            System.out.println(e.getMessage());
	        }
	        return labelDtos;
	    }

	@Override
	public List<Map<String, Object>> separateImagesInPdf(File file) {
		return null;
		
	}

}
