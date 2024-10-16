package com.rpa.sapsf.offer.approval.dto;

import java.util.List;

public class Zone3FieldDefinitionDTO {
	private String fieldId;
	private String fieldLabel;
	private String source;
	private String typeOfField;
	private String parentPickListOrObject;
	private String pickListOrObjectId;
	private String objectType;
	private String required;
	private String custom;
	private String anonymize;
	private String mobile;
	private String customToken;
	private String reportable;
	private String helpText;
	private String notes;
	private List<Zone3FieldDefinitionTranslaterDTO> zone3FieldDefinitionTranslatersList;

	public String getFieldId() {
		return fieldId;
	}

	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}

	public String getFieldLabel() {
		return fieldLabel;
	}

	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTypeOfField() {
		return typeOfField;
	}

	public void setTypeOfField(String typeOfField) {
		this.typeOfField = typeOfField;
	}

	public String getParentPickListOrObject() {
		return parentPickListOrObject;
	}

	public void setParentPickListOrObject(String parentPickListOrObject) {
		this.parentPickListOrObject = parentPickListOrObject;
	}

	public String getPickListOrObjectId() {
		return pickListOrObjectId;
	}

	public void setPickListOrObjectId(String pickListOrObjectId) {
		this.pickListOrObjectId = pickListOrObjectId;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getRequired() {
		return required;
	}

	public void setRequired(String required) {
		this.required = required;
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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCustomToken() {
		return customToken;
	}

	public void setCustomToken(String customToken) {
		this.customToken = customToken;
	}

	public String getReportable() {
		return reportable;
	}

	public void setReportable(String reportable) {
		this.reportable = reportable;
	}

	public String getHelpText() {
		return helpText;
	}

	public void setHelpText(String helpText) {
		this.helpText = helpText;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public List<Zone3FieldDefinitionTranslaterDTO> getZone3TranslatersList() {
		return zone3FieldDefinitionTranslatersList;
	}

	public void setZone3TranslatersList(List<Zone3FieldDefinitionTranslaterDTO> zone3TranslatersList) {
		this.zone3FieldDefinitionTranslatersList = zone3TranslatersList;
	}
}
