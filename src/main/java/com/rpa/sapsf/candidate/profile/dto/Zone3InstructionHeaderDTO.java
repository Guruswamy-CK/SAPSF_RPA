package com.rpa.sapsf.candidate.profile.dto;

import java.util.List;

public class Zone3InstructionHeaderDTO {
	private String fieldLabel;
	private String fieldType;
	private String mimeType;
	private List<Zone3InstructionHeaderTranslaterDTO> zone3InstructionHeaderTranslatersList;

	public String getFieldLabel() {
		return fieldLabel;
	}

	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public List<Zone3InstructionHeaderTranslaterDTO> getZone3InstructionHeaderTranslatersList() {
		return zone3InstructionHeaderTranslatersList;
	}

	public void setZone3InstructionHeaderTranslatersList(
			List<Zone3InstructionHeaderTranslaterDTO> zone3InstructionHeaderTranslatersList) {
		this.zone3InstructionHeaderTranslatersList = zone3InstructionHeaderTranslatersList;
	}
}
