package com.rpa.sapsf.candidate.application;

import java.util.List;

public class Zone4FieldPermissionDTO {
	private String roleName;
	private List<FieldPermissionDTO> fieldPermissionsList;

	public List<FieldPermissionDTO> getFieldPermissionsList() {
		return fieldPermissionsList;
	}

	public void setFieldPermissionsList(List<FieldPermissionDTO> fieldPermissionsList) {
		this.fieldPermissionsList = fieldPermissionsList;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
