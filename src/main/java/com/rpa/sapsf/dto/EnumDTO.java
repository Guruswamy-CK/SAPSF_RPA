package com.rpa.sapsf.dto;

import java.util.List;

public class EnumDTO {
	private String enumFieldId;
	private String enumFieldLabel;
	private String enumValue;
	private String enumLabel;
	private List<EnumLabelTranslaterDTO> enumLabelTranslatersList;

	public String getEnumFieldId() {
		return enumFieldId;
	}

	public void setEnumFieldId(String enumFieldId) {
		this.enumFieldId = enumFieldId;
	}

	public String getEnumFieldLabel() {
		return enumFieldLabel;
	}

	public void setEnumFieldLabel(String enumFieldLabel) {
		this.enumFieldLabel = enumFieldLabel;
	}

	public String getEnumValue() {
		return enumValue;
	}

	public void setEnumValue(String enumValue) {
		this.enumValue = enumValue;
	}

	public String getEnumLabel() {
		return enumLabel;
	}

	public void setEnumLabel(String enumLabel) {
		this.enumLabel = enumLabel;
	}

	public List<EnumLabelTranslaterDTO> getEnumLabelTranslatersList() {
		return enumLabelTranslatersList;
	}

	public void setEnumLabelTranslatersList(List<EnumLabelTranslaterDTO> enumLabelTranslatersList) {
		this.enumLabelTranslatersList = enumLabelTranslatersList;
	}
}
