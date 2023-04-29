package com.indegene.vem.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.indegene.vem.dto.CommonDTO;
import com.indegene.vem.service.HomeService;

@Controller
public class HomeController {
	
	@Autowired
	@Qualifier("service1")
	private HomeService service1;
	
	@Autowired
	@Qualifier("service2")
	private HomeService service2;
	
	private static final String GET_LABELS_FROM_IMAGES = "/get-labels";
	
	@PostMapping(value = GET_LABELS_FROM_IMAGES)
	public ResponseEntity<List<CommonDTO>> getLabels(@RequestBody MultipartFile file)
	{
		List<CommonDTO> response = new ArrayList<>();
		try {
			File inputFile = new File("E:\\WorkSpaces\\Personal_ws\\LabelDetection\\src\\main\\java\\com\\indegene\\vem\\media\\" + file.getOriginalFilename());
			file.transferTo(inputFile);
			response = service2.getLabels("E:\\WorkSpaces\\Personal_ws\\LabelDetection\\src\\main\\java\\com\\indegene\\vem\\media\\" + file.getOriginalFilename());
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
		return new ResponseEntity<List<CommonDTO>>(response, HttpStatus.OK);
	}

}
