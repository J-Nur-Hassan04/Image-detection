package com.indegene.vem.service.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.indegene.vem.dto.CommonDTO;
import com.indegene.vem.service.HomeService;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsRequest;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsResponse;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.rekognition.model.Label;
import software.amazon.awssdk.services.rekognition.model.RekognitionException;

@Service("service2")
public class HomeServiceImpl2 implements HomeService {
	private static final String ACCESS_KEY = "****************************";
	private static final String SECRET_KEY = "************************************";

	@Override
	public List<CommonDTO> getLabels(String imageSource) {
		List<CommonDTO> labelDtos = new ArrayList<>();
		try {
			Region region = Region.US_EAST_1;
			AwsCredentials credentials = AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY);
			AwsCredentialsProvider staticCredentials = StaticCredentialsProvider.create(credentials);
			RekognitionClient rekClint = RekognitionClient.builder()
					.region(region)
					.credentialsProvider(staticCredentials)
					.build();
			labelDtos = detectImageLabels(rekClint, imageSource);
			rekClint.close();

		} catch (Exception exception) {
			System.out.println(exception.getMessage());
		}

		return labelDtos;
	}
	private static List<CommonDTO> detectImageLabels(RekognitionClient rekClient, String sourceImage) {

		 List<CommonDTO> labelDtos = new ArrayList<>();
	        try {
	            InputStream sourceStream = new FileInputStream(sourceImage);
	            SdkBytes sourceBytes = SdkBytes.fromInputStream(sourceStream);
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
	            try {
	            	sourceStream.close();
				} catch (Exception exception) {
					System.out.println(exception.getMessage());
				}

	        } catch (RekognitionException | FileNotFoundException e) {
	            System.out.println(e.getMessage());
	        }
	        return labelDtos;
	    }
	

}
