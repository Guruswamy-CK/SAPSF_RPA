package com.rpa.sapsf.candidate.profile.dto;

import java.util.List;

public class Zone4OperatorInstructionHeaderDTO {
	private String fieldLabel;
	private String fieldType;
	private String mimeType;
	private List<Zone4OperatorInstructionHeaderTranslaterDTO> zone4OperatorInstructionHeaderTranslatersList;

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

	public List<Zone4OperatorInstructionHeaderTranslaterDTO> getZone4OperatorInstructionHeaderTranslatersList() {
		return zone4OperatorInstructionHeaderTranslatersList;
	}

	public void setZone4OperatorInstructionHeaderTranslatersList(
			List<Zone4OperatorInstructionHeaderTranslaterDTO> zone4OperatorInstructionHeaderTranslatersList) {
		this.zone4OperatorInstructionHeaderTranslatersList = zone4OperatorInstructionHeaderTranslatersList;
	}

}
