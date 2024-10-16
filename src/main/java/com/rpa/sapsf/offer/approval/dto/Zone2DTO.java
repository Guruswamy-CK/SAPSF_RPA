package com.rpa.sapsf.offer.approval.dto;

import java.util.List;

public class Zone2DTO {
	private String templateName;
	private String templateDescription;
	private List<Zone2TemplateNameTranslaterDTO> zone2TemplateNameTranslatersList;
	private List<Zone2TemplateDescriptionTranslaterDTO> zone2TemplateDescriptionTranslatersList;
	private String lastModifiedDate;

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

	public List<Zone2TemplateNameTranslaterDTO> getZone2TemplateNameTranslatersList() {
		return zone2TemplateNameTranslatersList;
	}

	public void setZone2TemplateNameTranslatersList(
			List<Zone2TemplateNameTranslaterDTO> zone2TemplateNameTranslatersList) {
		this.zone2TemplateNameTranslatersList = zone2TemplateNameTranslatersList;
	}

	public List<Zone2TemplateDescriptionTranslaterDTO> getZone2TemplateDescriptionTranslatersList() {
		return zone2TemplateDescriptionTranslatersList;
	}

	public void setZone2TemplateDescriptionTranslatersList(
			List<Zone2TemplateDescriptionTranslaterDTO> zone2TemplateDescriptionTranslatersList) {
		this.zone2TemplateDescriptionTranslatersList = zone2TemplateDescriptionTranslatersList;
	}

	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
}
