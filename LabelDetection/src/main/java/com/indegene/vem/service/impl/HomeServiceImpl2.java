package com.indegene.vem.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;

import com.indegene.vem.dto.LabelsDTO;
import com.indegene.vem.dto.TextDTO;
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
import software.amazon.awssdk.services.rekognition.model.DetectTextRequest;
import software.amazon.awssdk.services.rekognition.model.DetectTextResponse;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.rekognition.model.Label;
import software.amazon.awssdk.services.rekognition.model.RekognitionException;
import software.amazon.awssdk.services.rekognition.model.TextDetection;

@Service("service2")
public class HomeServiceImpl2 implements HomeService {
	private static final String ACCESS_KEY = "*";
	private static final String SECRET_KEY = "*";

	@Override
	public Map<String, Object> getLabels(String imageSource) {
		Map<String, Object> imageAnalyzation = new HashMap<>();
		try {
			List<LabelsDTO> labelDtos = new ArrayList<>();
			List<TextDTO> textDtos = new ArrayList<>();
			Region region = Region.US_EAST_1;
			AwsCredentials credentials = AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY);
			AwsCredentialsProvider staticCredentials = StaticCredentialsProvider.create(credentials);
			RekognitionClient rekClint = RekognitionClient.builder().region(region)
					.credentialsProvider(staticCredentials).build();
			labelDtos = detectImageLabels(rekClint, imageSource);
			textDtos = detectTextLabels(rekClint, imageSource);
			rekClint.close();
			imageAnalyzation.put("labels", labelDtos);
			imageAnalyzation.put("texts", textDtos);

		} catch (Exception exception) {
			System.out.println(exception.getMessage());
		}

		return imageAnalyzation;
	}

	private List<LabelsDTO> detectImageLabels(RekognitionClient rekClient, String sourceImage) {

		List<LabelsDTO> labelDtos = new ArrayList<>();
		try {
			InputStream sourceStream = new FileInputStream(sourceImage);
			SdkBytes sourceBytes = SdkBytes.fromInputStream(sourceStream);
			Image souImage = Image.builder().bytes(sourceBytes).build();

			DetectLabelsRequest detectLabelsRequest = DetectLabelsRequest.builder().image(souImage).maxLabels(10)
					.build();

			DetectLabelsResponse labelsResponse = rekClient.detectLabels(detectLabelsRequest);
			List<Label> labels = labelsResponse.labels();
			labelDtos = labels.stream().map(label -> new LabelsDTO(label.name(), label.confidence()))
					.collect(Collectors.toList());
		} catch (RekognitionException | FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return labelDtos;
	}

	private List<TextDTO> detectTextLabels(RekognitionClient rekClient, String sourceImage) {

		List<TextDTO> textDtos = new ArrayList<>();
		try {
			InputStream sourceStream = new FileInputStream(sourceImage);
			SdkBytes sourceBytes = SdkBytes.fromInputStream(sourceStream);
			Image souImage = Image.builder().bytes(sourceBytes).build();

			DetectTextRequest textRequest = DetectTextRequest.builder().image(souImage).build();

			DetectTextResponse textResponse = rekClient.detectText(textRequest);
			List<TextDetection> textCollections = textResponse.textDetections();
			textDtos = textCollections.stream().filter(textCollection -> textCollection.parentId() == null)
					.map(textCollection -> new TextDTO(textCollection.id(), textCollection.detectedText(),
							textCollection.confidence()))
					.collect(Collectors.toList());
		} catch (RekognitionException | FileNotFoundException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
		return textDtos;
	}

	@Override
	public void separateImagesInPdf(File file) {
		try {
			PDDocument document = PDDocument.load(file);
			PDFRenderer renderer = new PDFRenderer(document);
			int pageNumber = 0;
			for (int i = 0; i < document.getNumberOfPages(); i++) {
				pageNumber++;
				BufferedImage image = renderer.renderImage(i);
				String fileName = "page" + pageNumber + ".png";
				ImageIO.write(image, "png", new File("E:\\WorkSpaces\\Personal_ws\\LabelDetection\\src\\main\\java\\com\\indegene\\vem\\media\\" + fileName));
			}

			document.close();

		} catch (Exception exception) {
			System.out.println(exception.getMessage());
		}

	}

}
