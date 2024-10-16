package com.rpa.sapsf.corporate.data.model.dto;

import java.util.List;

public class Zone1DTO {
	private String templateName;
	private String templateDescription;
	private String lastModifiedDate;
	private List<HrisElementDTO> hrisElementsLocationGroupList;

	public List<HrisElementDTO> getHrisElementsLocationGroupList() {
		return hrisElementsLocationGroupList;
	}

	public void setHrisElementsLocationGroupList(List<HrisElementDTO> hrisElementsLocationGroupList) {
		this.hrisElementsLocationGroupList = hrisElementsLocationGroupList;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplateDescription() {
		return templateDescription;
	}

	public void setTemplateDescription(String templateDescription) {
		this.templateDescription = templateDescription;
	}

	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
}
