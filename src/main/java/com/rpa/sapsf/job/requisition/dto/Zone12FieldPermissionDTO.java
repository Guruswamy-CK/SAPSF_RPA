package com.rpa.sapsf.job.requisition.dto;

import java.util.List;

public class Zone12FieldPermissionDTO {
	private String roleName;
	private String status;
	private List<FieldPermissionDTO> fieldPermissionsList;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<FieldPermissionDTO> getFieldPermissionsList() {
		return fieldPermissionsList;
	}

	public void setFieldPermissionsList(List<FieldPermissionDTO> fieldPermissionsList) {
		this.fieldPermissionsList = fieldPermissionsList;
	}
}
