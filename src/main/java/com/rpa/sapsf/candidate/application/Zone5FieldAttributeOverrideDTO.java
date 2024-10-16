package com.rpa.sapsf.candidate.application;

import java.util.List;

public class Zone5FieldAttributeOverrideDTO {
	private String country;
	private String attribute;
	private String applicant;
	private List<FieldOverrideDTO> fieldOverridesList;

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getApplicant() {
		return applicant;
	}

	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}

	public List<FieldOverrideDTO> getFieldOverridesList() {
		return fieldOverridesList;
	}

	public void setFieldOverridesList(List<FieldOverrideDTO> fieldOverridesList) {
		this.fieldOverridesList = fieldOverridesList;
	}
}
