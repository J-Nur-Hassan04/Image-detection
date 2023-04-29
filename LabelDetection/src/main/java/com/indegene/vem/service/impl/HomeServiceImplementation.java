package com.indegene.vem.service.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.indegene.vem.dto.CommonDTO;
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
	public List<CommonDTO> getLabels(String imageSource) {
		List<CommonDTO> labelDtos = new ArrayList<>();
		try {
			String sourceImage = imageSource;
			Region region = Region.US_EAST_1;
			RekognitionClient rekClint = RekognitionClient.builder()
					.region(region)
					.credentialsProvider(ProfileCredentialsProvider.create())
					.build();
			labelDtos = detectImageLabels(rekClint, sourceImage);
			rekClint.close();
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
		}
		return labelDtos;
	}
	
	 public static List<CommonDTO> detectImageLabels(RekognitionClient rekClient, String sourceImage) {

		 List<CommonDTO> labelDtos = new ArrayList<>();
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
	                CommonDTO dto = new CommonDTO();
	                dto.setLabel(label.name());
	                dto.setConfidence(label.confidence().toString());
	                labelDtos.add(dto);
	            }

	        } catch (RekognitionException | FileNotFoundException e) {
	            System.out.println(e.getMessage());
	        }
	        return labelDtos;
	    }

}
