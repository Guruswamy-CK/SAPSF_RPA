package com.rpa.sapsf.candidate.profile.dto;

public class FieldPermissionDTO {
	private String permissionType;
	private String fieldId;
	private String fieldType;
	private String beFieldId;

	public String getBeFieldId() {
		return beFieldId;
	}

	public void setBeFieldId(String beFieldId) {
		this.beFieldId = beFieldId;
	}

	public String getPermissionType() {
		return permissionType;
	}

	public void setPermissionType(String permissionType) {
		this.permissionType = permissionType;
	}

	public String getFieldId() {
		return fieldId;
	}

	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
}
