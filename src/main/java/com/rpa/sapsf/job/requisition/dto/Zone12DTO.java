package com.rpa.sapsf.job.requisition.dto;

import java.util.List;

public class Zone12DTO {
	private String applicationStatusSetName;
	private List<Zone12FieldPermissionDTO> zone12FieldPermissionsList;
	private List<Zone12FeaturePermissionDTO> zone12FeaturePermissionsList;

	public String getApplicationStatusSetName() {
		return applicationStatusSetName;
	}

	public void setApplicationStatusSetName(String applicationStatusSetName) {
		this.applicationStatusSetName = applicationStatusSetName;
	}

	public List<Zone12FieldPermissionDTO> getZone12FieldPermissionsList() {
		return zone12FieldPermissionsList;
	}

	public void setZone12FieldPermissionsList(List<Zone12FieldPermissionDTO> zone12FieldPermissionsList) {
		this.zone12FieldPermissionsList = zone12FieldPermissionsList;
	}

	public List<Zone12FeaturePermissionDTO> getZone12FeaturePermissionsList() {
		return zone12FeaturePermissionsList;
	}

	public void setZone12FeaturePermissionsList(List<Zone12FeaturePermissionDTO> zone12FeaturePermissionsList) {
		this.zone12FeaturePermissionsList = zone12FeaturePermissionsList;
	}
}
