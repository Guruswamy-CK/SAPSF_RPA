package com.rpa.sapsf.job.requisition.dto;

import java.util.List;
import java.util.Map;

public class Zone12FeaturePermissionDTO {
	private String status;
	private Map<String, List<String>> permissionRoleMap;

	public Map<String, List<String>> getPermissionRoleMap() {
		return permissionRoleMap;
	}

	public void setPermissionRoleMap(Map<String, List<String>> permissionRoleMap) {
		this.permissionRoleMap = permissionRoleMap;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
