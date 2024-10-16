package com.rpa.sapsf.candidate.application;

import java.util.List;

public class Zone6ButtonPermissionDTO {
	private String buttonId;
	private List<String> permittedRolesList;

	public String getButtonId() {
		return buttonId;
	}

	public void setButtonId(String buttonId) {
		this.buttonId = buttonId;
	}

	public List<String> getPermittedRolesList() {
		return permittedRolesList;
	}

	public void setPermittedRolesList(List<String> permittedRolesList) {
		this.permittedRolesList = permittedRolesList;
	}
}
