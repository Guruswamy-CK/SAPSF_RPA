package com.rpa.sapsf.candidate.profile.dto;

import java.util.List;

public class Zone12FieldPermissionDTO {
	private String roleName;
	private String source;
	private String country;
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

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
}
