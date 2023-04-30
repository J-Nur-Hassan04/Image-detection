package com.indegene.vem.dto;

public class TextDTO {

	private Integer id;
	private String detectedText;
	private Float confidence;

	public TextDTO() {
		super();
	}

	public TextDTO(Integer id, String detectedText, Float confidence) {
		super();
		this.id = id;
		this.detectedText = detectedText;
		this.confidence = confidence;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDetectedText() {
		return detectedText;
	}

	public void setDetectedText(String detectedText) {
		this.detectedText = detectedText;
	}

	public Float getConfidence() {
		return confidence;
	}

	public void setConfidence(Float confidence) {
		this.confidence = confidence;
	}

}
