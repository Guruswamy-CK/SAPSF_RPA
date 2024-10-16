package com.rpa.sapsf.candidate.profile.dto;

import java.util.List;

import com.rpa.sapsf.dto.TranslaterDTO;

public class Zone8SearchDisplayOptionSubCategoryDTO {
	private String fieldRefId;
	private String backgroundElementRef;
	private String selectByDefault;
	private String gridOrder;
	private String tagsNeeded;
	private List<TranslaterDTO> translatersList;

	public List<TranslaterDTO> getTranslatersList() {
		return translatersList;
	}

	public void setTranslatersList(List<TranslaterDTO> translatersList) {
		this.translatersList = translatersList;
	}
	
	public String getFieldRefId() {
		return fieldRefId;
	}
	public void setFieldRefId(String fieldRefId) {
		this.fieldRefId = fieldRefId;
	}
	public String getBackgroundElementRef() {
		return backgroundElementRef;
	}
	public void setBackgroundElementRef(String backgroundElementRef) {
		this.backgroundElementRef = backgroundElementRef;
	}
	public String getSelectByDefault() {
		return selectByDefault;
	}
	public void setSelectByDefault(String selectByDefault) {
		this.selectByDefault = selectByDefault;
	}
	public String getGridOrder() {
		return gridOrder;
	}
	public void setGridOrder(String gridOrder) {
		this.gridOrder = gridOrder;
	}
	public String getTagsNeeded() {
		return tagsNeeded;
	}
	public void setTagsNeeded(String tagsNeeded) {
		this.tagsNeeded = tagsNeeded;
	}
}
