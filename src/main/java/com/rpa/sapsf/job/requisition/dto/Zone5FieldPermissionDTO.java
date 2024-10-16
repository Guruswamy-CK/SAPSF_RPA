package com.rpa.sapsf.job.requisition.dto;

import java.util.List;

public class Zone5FieldPermissionDTO {
	private String roleName;
	private String status;
	private String description;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<FieldPermissionDTO> getFieldPermissionsList() {
		return fieldPermissionsList;
	}

	public void setFieldPermissionsList(List<FieldPermissionDTO> fieldPermissionsList) {
		this.fieldPermissionsList = fieldPermissionsList;
	}
}
