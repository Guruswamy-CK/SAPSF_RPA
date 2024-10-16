package com.rpa.sapsf.candidate.application;

import java.util.List;

import com.rpa.sapsf.dto.TranslaterDTO;

public class Zone2DTO {
	private String templateName;
	private String templateDescription;
	private List<TranslaterDTO> zone2TemplateNameTranslatersList;
	private List<TranslaterDTO> zone2TemplateDescriptionTranslatersList;
	private String lastModifiedDate;
	private String isMultiStage;

	public String getIsMultiStage() {
		return isMultiStage;
	}

	public void setIsMultiStage(String isMultiStage) {
		this.isMultiStage = isMultiStage;
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

	public List<TranslaterDTO> getZone2TemplateNameTranslatersList() {
		return zone2TemplateNameTranslatersList;
	}

	public void setZone2TemplateNameTranslatersList(
			List<TranslaterDTO> zone2TemplateNameTranslatersList) {
		this.zone2TemplateNameTranslatersList = zone2TemplateNameTranslatersList;
	}

	public List<TranslaterDTO> getZone2TemplateDescriptionTranslatersList() {
		return zone2TemplateDescriptionTranslatersList;
	}

	public void setZone2TemplateDescriptionTranslatersList(
			List<TranslaterDTO> zone2TemplateDescriptionTranslatersList) {
		this.zone2TemplateDescriptionTranslatersList = zone2TemplateDescriptionTranslatersList;
	}

	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
}
