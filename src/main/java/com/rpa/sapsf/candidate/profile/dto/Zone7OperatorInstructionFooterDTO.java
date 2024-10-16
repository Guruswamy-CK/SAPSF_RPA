package com.rpa.sapsf.candidate.profile.dto;

import java.util.List;

public class Zone7OperatorInstructionFooterDTO {
	private String fieldLabel;
	private String fieldType;
	private String mimeType;
	private List<Zone7OperatorInstructionFooterTranslaterDTO> zone7OperatorInstructionFooterTranslatersList;
	
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
	public List<Zone7OperatorInstructionFooterTranslaterDTO> getZone7OperatorInstructionFooterTranslatersList() {
		return zone7OperatorInstructionFooterTranslatersList;
	}
	public void setZone7OperatorInstructionFooterTranslatersList(
			List<Zone7OperatorInstructionFooterTranslaterDTO> zone7OperatorInstructionFooterTranslatersList) {
		this.zone7OperatorInstructionFooterTranslatersList = zone7OperatorInstructionFooterTranslatersList;
	}
}
