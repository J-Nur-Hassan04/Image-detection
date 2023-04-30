package com.indegene.vem.controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.indegene.vem.common.CommonApiResponse;
import com.indegene.vem.service.HomeService;

@Controller
@CrossOrigin("http://127.0.0.1:5500")
public class HomeController {
	
	@Autowired
	@Qualifier("service1")
	private HomeService service1;
	
	@Autowired
	@Qualifier("service2")
	private HomeService service2;
	
	private static final String GET_LABELS_FROM_IMAGES = "/get-labels";

	@PostMapping(value = GET_LABELS_FROM_IMAGES, produces = "application/json")
	public ResponseEntity<CommonApiResponse> getLabels(@RequestBody MultipartFile file)
	{
		CommonApiResponse response = new CommonApiResponse();
		try {
			File inputFile = new File("E:\\WorkSpaces\\Personal_ws\\LabelDetection\\src\\main\\java\\com\\indegene\\vem\\media\\" + file.getOriginalFilename());
			file.transferTo(inputFile);
			if(!file.getContentType().equals("application/pdf"))
			{
				response.setResBody(service2.getLabels("E:\\WorkSpaces\\Personal_ws\\LabelDetection\\src\\main\\java\\com\\indegene\\vem\\media\\" + file.getOriginalFilename()));
			}
			else
			{
				service2.separateImagesInPdf(inputFile);
			}
			response.setSuccess();
			if(inputFile.delete())
			{
				System.out.println("deleted");
			}
			else
			{
				System.out.println("Not deleted");
			}
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
		}
		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
	}

}
