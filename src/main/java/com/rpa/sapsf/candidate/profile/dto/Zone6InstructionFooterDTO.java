package com.rpa.sapsf.candidate.profile.dto;

import java.util.List;

public class Zone6InstructionFooterDTO {
	private String fieldLabel;
	private String fieldType;
	private String mimeType;
	private List<Zone6InstructionFooterTranslaterDTO> zone6InstructionFooterTranslatersList;

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

	public List<Zone6InstructionFooterTranslaterDTO> getZone6InstructionFooterTranslatersList() {
		return zone6InstructionFooterTranslatersList;
	}

	public void setZone6InstructionFooterTranslatersList(
			List<Zone6InstructionFooterTranslaterDTO> zone6InstructionFooterTranslatersList) {
		this.zone6InstructionFooterTranslatersList = zone6InstructionFooterTranslatersList;
	}
}
