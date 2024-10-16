package com.rpa.sapsf.corporate.data.model.dto;

import java.util.List;

import com.rpa.sapsf.dto.TranslaterDTO;

public class HrisFieldDTO {
	private String id;
	private String visibility;
	private String required;
	private String maxLength;
	private String labelName;
	private List<TranslaterDTO> labelTranslatersList;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public String getRequired() {
		return required;
	}

	public void setRequired(String required) {
		this.required = required;
	}

	public String getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(String maxLength) {
		this.maxLength = maxLength;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public List<TranslaterDTO> getLabelTranslatersList() {
		return labelTranslatersList;
	}

	public void setLabelTranslatersList(List<TranslaterDTO> labelTranslatersList) {
		this.labelTranslatersList = labelTranslatersList;
	}
}
