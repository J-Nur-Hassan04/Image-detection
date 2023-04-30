package com.indegene.vem.dto;

public class LabelsDTO {
	private String label;
	private Float confidence;

	public LabelsDTO() {
	}

	public LabelsDTO(String label, Float confidence) {
		this.label = label;
		this.confidence = confidence;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Float getConfidence() {
		return confidence;
	}

	public void setConfidence(Float confidence) {
		this.confidence = confidence;
	}

}
