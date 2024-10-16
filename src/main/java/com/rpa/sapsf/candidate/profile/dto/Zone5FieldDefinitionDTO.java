package com.rpa.sapsf.candidate.profile.dto;

import java.util.List;

public class Zone5FieldDefinitionDTO {
	private String fieldId;
	private String fieldLabel;
	private String fieldType;
	private String mimeType;
	private String parentPickListIdOrObject;
	private String pickListOrObject;
	private String objectType;
	private String required;
	private String custom;
	private String anonymize;
	private String sensitive;
	private String fieldDescription;
	private String employeeTypeId;
	private String employeeProfileFieldId;
	private String availableForCandidateSearhResult;
	private String displayOrFilter;
	private String reportable;
	private String notesOrComments;
	private List<Zone5FieldDefinitionTranslaterDTO> zone5FieldDefinitionTranslatersList;

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

	public String getFieldDescription() {
		return fieldDescription;
	}

	public void setFieldDescription(String fieldDescription) {
		this.fieldDescription = fieldDescription;
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

	public String getAvailableForCandidateSearhResult() {
		return availableForCandidateSearhResult;
	}

	public void setAvailableForCandidateSearhResult(String availableForCandidateSearhResult) {
		this.availableForCandidateSearhResult = availableForCandidateSearhResult;
	}

	public String getDisplayOrFilter() {
		return displayOrFilter;
	}

	public void setDisplayOrFilter(String displayOrFilter) {
		this.displayOrFilter = displayOrFilter;
	}

	public String getReportable() {
		return reportable;
	}

	public void setReportable(String reportable) {
		this.reportable = reportable;
	}

	public String getNotesOrComments() {
		return notesOrComments;
	}

	public void setNotesOrComments(String notesOrComments) {
		this.notesOrComments = notesOrComments;
	}

	public List<Zone5FieldDefinitionTranslaterDTO> getZone5FieldDefinitionTranslatersList() {
		return zone5FieldDefinitionTranslatersList;
	}

	public void setZone5FieldDefinitionTranslatersList(
			List<Zone5FieldDefinitionTranslaterDTO> zone5FieldDefinitionTranslatersList) {
		this.zone5FieldDefinitionTranslatersList = zone5FieldDefinitionTranslatersList;
	}
}
