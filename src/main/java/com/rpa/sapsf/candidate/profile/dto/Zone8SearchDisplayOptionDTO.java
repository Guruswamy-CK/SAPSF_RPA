package com.rpa.sapsf.candidate.profile.dto;

import java.util.List;

import com.rpa.sapsf.dto.TranslaterDTO;

public class Zone8SearchDisplayOptionDTO {
	private String categoryId;
	private String categoryName;
	private String label;
	private String fieldRefId;
	private String backgroundElementRef;
	private String selectByDefault;
	private String gridOrder;
	private String tagsNeeded;
	private List<TranslaterDTO> translatersList;
	private List<Zone8SearchDisplayOptionSubCategoryDTO> zone8SearchDisplayOptionSubCategoryList;

	public List<Zone8SearchDisplayOptionSubCategoryDTO> getZone8SearchDisplayOptionSubCategoryList() {
		return zone8SearchDisplayOptionSubCategoryList;
	}

	public void setZone8SearchDisplayOptionSubCategoryList(
			List<Zone8SearchDisplayOptionSubCategoryDTO> zone8SearchDisplayOptionSubCategoryList) {
		this.zone8SearchDisplayOptionSubCategoryList = zone8SearchDisplayOptionSubCategoryList;
	}

	public List<TranslaterDTO> getTranslatersList() {
		return translatersList;
	}

	public void setTranslatersList(List<TranslaterDTO> translatersList) {
		this.translatersList = translatersList;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
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
