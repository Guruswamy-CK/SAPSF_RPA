package com.rpa.sapsf.job.requisition.dto;

public class FieldPermissionDTO {
	private String fieldId;
	private String permissionType;

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
}
