package com.rpa.sapsf.job.requisition.dto;

import java.util.List;

public class Zone14ButtonEmailPermissionDTO {
	private String buttonEmailId;
	private List<String> permittedRolesList;

	public String getButtonEmailId() {
		return buttonEmailId;
	}

	public void setButtonEmailId(String buttonEmailId) {
		this.buttonEmailId = buttonEmailId;
	}

	public List<String> getPermittedRolesList() {
		return permittedRolesList;
	}

	public void setPermittedRolesList(List<String> permittedRolesList) {
		this.permittedRolesList = permittedRolesList;
	}
}
