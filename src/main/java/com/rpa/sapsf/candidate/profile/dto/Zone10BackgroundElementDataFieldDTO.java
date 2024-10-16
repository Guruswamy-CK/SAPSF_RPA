package com.rpa.sapsf.candidate.profile.dto;

import java.util.List;

public class Zone10BackgroundElementDataFieldDTO {
	private String fieldId;
	private String fieldlabel;
	private String fieldType;
	private String mimeType;
	private String parentPickListIdOrObject;
	private String pickListObject;
	private String required;
	private String maxLength;
	private String custom;
	private String anonymize;
	private String sensitive;
	private String employeeTypeId;
	private String employeeProfileFieldId;
	private List<Zone10BackgroundElementDataFieldTranslaterDTO> zone10BackgroundElementDataFieldTranslatersList;

	public String getFieldId() {
		return fieldId;
	}

	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}

	public String getFieldlabel() {
		return fieldlabel;
	}

	public void setFieldlabel(String fieldlabel) {
		this.fieldlabel = fieldlabel;
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

	public String getParentPickListIdOrObject() {
		return parentPickListIdOrObject;
	}

	public void setParentPickListIdOrObject(String parentPickListIdOrObject) {
		this.parentPickListIdOrObject = parentPickListIdOrObject;
	}
	
	public String getPickListObject() {
		return pickListObject;
	}

	public void setPickListObject(String pickListObject) {
		this.pickListObject = pickListObject;
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

	public String getCustom() {
		return custom;
	}

	public void setCustom(String custom) {
		this.custom = custom;
	}

	public String getAnonymize() {
		return anonymize;
	}

	public void setAnonymize(String anonymize) {
		this.anonymize = anonymize;
	}

	public String getSensitive() {
		return sensitive;
	}

	public void setSensitive(String sensitive) {
		this.sensitive = sensitive;
	}

	public String getEmployeeTypeId() {
		return employeeTypeId;
	}

	public void setEmployeeTypeId(String employeeTypeId) {
		this.employeeTypeId = employeeTypeId;
	}

	public String getEmployeeProfileFieldId() {
		return employeeProfileFieldId;
	}

	public void setEmployeeProfileFieldId(String employeeProfileFieldId) {
		this.employeeProfileFieldId = employeeProfileFieldId;
	}

	public List<Zone10BackgroundElementDataFieldTranslaterDTO> getZone10BackgroundElementDataFieldTranslatersList() {
		return zone10BackgroundElementDataFieldTranslatersList;
	}

	public void setZone10BackgroundElementDataFieldTranslatersList(
			List<Zone10BackgroundElementDataFieldTranslaterDTO> zone10BackgroundElementDataFieldTranslatersList) {
		this.zone10BackgroundElementDataFieldTranslatersList = zone10BackgroundElementDataFieldTranslatersList;
	}
}
