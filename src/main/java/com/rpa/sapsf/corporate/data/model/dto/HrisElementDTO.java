package com.rpa.sapsf.corporate.data.model.dto;

import java.util.List;

public class HrisElementDTO {
	private String hrisElementId;
	private String hrisElementLabel;
	private List<HrisFieldDTO> hrisFieldsList;
	private List<HrisAssociationDTO> hrisAssociationList;
	private List<HrisSearchFieldDTO> hrisSearchFieldsList;

	public String getHrisElementId() {
		return hrisElementId;
	}

	public void setHrisElementId(String hrisElementId) {
		this.hrisElementId = hrisElementId;
	}

	public String getHrisElementLabel() {
		return hrisElementLabel;
	}

	public void setHrisElementLabel(String hrisElementLabel) {
		this.hrisElementLabel = hrisElementLabel;
	}

	public List<HrisFieldDTO> getHrisFieldsList() {
		return hrisFieldsList;
	}

	public void setHrisFieldsList(List<HrisFieldDTO> hrisFieldsList) {
		this.hrisFieldsList = hrisFieldsList;
	}

	public List<HrisAssociationDTO> getHrisAssociationList() {
		return hrisAssociationList;
	}

	public void setHrisAssociationList(List<HrisAssociationDTO> hrisAssociationList) {
		this.hrisAssociationList = hrisAssociationList;
	}

	public List<HrisSearchFieldDTO> getHrisSearchFieldsList() {
		return hrisSearchFieldsList;
	}

	public void setHrisSearchFieldsList(List<HrisSearchFieldDTO> hrisSearchFieldsList) {
		this.hrisSearchFieldsList = hrisSearchFieldsList;
	}
}
