package com.rpa.sapsf.candidate.application;

import java.util.List;

import com.rpa.sapsf.dto.TranslaterDTO;

public class Zone3FieldDefinitionDTO {
	private String fieldId;
	private String fieldLabel;
	private String fieldType;
	private String mimeType;
	private String parentPickListOrObject;
	private String pickListOrObjectId;
	private String required;
	private String custom;
	private String isPublic;
	private String anonymize;
	private String forwardIntact;
	private String sensitive;
	private String reportable;
	private String fieldDescription;
	private String recruiterHelpText;
	private String candidateHelpText;
	private String notes;
	private List<TranslaterDTO> zone3FieldDefinitionTranslatersList;
	private List<TranslaterDTO> zone3FieldDefinitionTranslatersDescList;

	public List<TranslaterDTO> getZone3FieldDefinitionTranslatersDescList() {
		return zone3FieldDefinitionTranslatersDescList;
	}

	public void setZone3FieldDefinitionTranslatersDescList(List<TranslaterDTO> zone3FieldDefinitionTranslatersDescList) {
		this.zone3FieldDefinitionTranslatersDescList = zone3FieldDefinitionTranslatersDescList;
	}

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

	public String getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(String isPublic) {
		this.isPublic = isPublic;
	}

	public String getAnonymize() {
		return anonymize;
	}

	public void setAnonymize(String anonymize) {
		this.anonymize = anonymize;
	}

	public String getForwardIntact() {
		return forwardIntact;
	}

	public void setForwardIntact(String forwardIntact) {
		this.forwardIntact = forwardIntact;
	}

	public String getSensitive() {
		return sensitive;
	}

	public void setSensitive(String sensitive) {
		this.sensitive = sensitive;
	}

	public String getReportable() {
		return reportable;
	}

	public void setReportable(String reportable) {
		this.reportable = reportable;
	}

	public String getFieldDescription() {
		return fieldDescription;
	}

	public void setFieldDescription(String fieldDescription) {
		this.fieldDescription = fieldDescription;
	}

	public String getRecruiterHelpText() {
		return recruiterHelpText;
	}

	public void setRecruiterHelpText(String recruiterHelpText) {
		this.recruiterHelpText = recruiterHelpText;
	}

	public String getCandidateHelpText() {
		return candidateHelpText;
	}

	public void setCandidateHelpText(String candidateHelpText) {
		this.candidateHelpText = candidateHelpText;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public List<TranslaterDTO> getZone3FieldDefinitionTranslatersList() {
		return zone3FieldDefinitionTranslatersList;
	}

	public void setZone3FieldDefinitionTranslatersList(List<TranslaterDTO> zone3FieldDefinitionTranslatersList) {
		this.zone3FieldDefinitionTranslatersList = zone3FieldDefinitionTranslatersList;
	}
}
