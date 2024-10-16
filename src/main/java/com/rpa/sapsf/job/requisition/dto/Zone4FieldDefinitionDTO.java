package com.rpa.sapsf.job.requisition.dto;

import java.util.List;

import com.rpa.sapsf.dto.TranslaterDTO;

public class Zone4FieldDefinitionDTO {
	private String fieldId;
	private String fieldLabel;
	private String fieldType;
	private String mimeType;
	private String groupName;
	private String parentPickListIdOrObject;
	private String pickListOrObject;
	private String objectType;
	private String required;
	private String custom;
	private String multiSelect;
	private String mobileField;
	private String jobDescriptionToken;
	private String offerLetterToken;
	private String screenReader;
	private String filterField;
	private String reportable;
	private String candidateSearchField;
	private String notesOrComments;
	private List<TranslaterDTO> zone4FieldDefinitionTranslatersList;

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

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getParentPickListIdOrObject() {
		return parentPickListIdOrObject;
	}

	public void setParentPickListIdOrObject(String parentPickListIdOrObject) {
		this.parentPickListIdOrObject = parentPickListIdOrObject;
	}

	public String getPickListOrObject() {
		return pickListOrObject;
	}

	public void setPickListOrObject(String pickListOrObject) {
		this.pickListOrObject = pickListOrObject;
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

	public String getMultiSelect() {
		return multiSelect;
	}

	public void setMultiSelect(String multiSelect) {
		this.multiSelect = multiSelect;
	}

	public String getMobileField() {
		return mobileField;
	}

	public void setMobileField(String mobileField) {
		this.mobileField = mobileField;
	}

	public String getJobDescriptionToken() {
		return jobDescriptionToken;
	}

	public void setJobDescriptionToken(String jobDescriptionToken) {
		this.jobDescriptionToken = jobDescriptionToken;
	}

	public String getOfferLetterToken() {
		return offerLetterToken;
	}

	public void setOfferLetterToken(String offerLetterToken) {
		this.offerLetterToken = offerLetterToken;
	}

	public String getScreenReader() {
		return screenReader;
	}

	public void setScreenReader(String screenReader) {
		this.screenReader = screenReader;
	}

	public String getFilterField() {
		return filterField;
	}

	public void setFilterField(String filterField) {
		this.filterField = filterField;
	}

	public String getReportable() {
		return reportable;
	}

	public void setReportable(String reportable) {
		this.reportable = reportable;
	}

	public String getCandidateSearchField() {
		return candidateSearchField;
	}

	public void setCandidateSearchField(String candidateSearchField) {
		this.candidateSearchField = candidateSearchField;
	}

	public String getNotesOrComments() {
		return notesOrComments;
	}

	public void setNotesOrComments(String notesOrComments) {
		this.notesOrComments = notesOrComments;
	}

	public List<TranslaterDTO> getZone4FieldDefinitionTranslatersList() {
		return zone4FieldDefinitionTranslatersList;
	}

	public void setZone4FieldDefinitionTranslatersList(List<TranslaterDTO> zone4FieldDefinitionTranslatersList) {
		this.zone4FieldDefinitionTranslatersList = zone4FieldDefinitionTranslatersList;
	}
}
