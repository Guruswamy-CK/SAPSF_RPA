package com.rpa.sapsf.candidate.profile.dto;

import java.util.List;

import com.rpa.sapsf.dto.TranslaterDTO;

public class Zone9SummaryDisplayOptionDTO {
	private String categoryId;
	private String categoryName;
	private String label;
	private String fieldRefId;
	private String selectByDefault;
	private String gridOrder;
	private List<TranslaterDTO> translatersList;
	private List<Zone9SummaryDisplayOptionSubCategoryDTO> Zone9SummaryDisplayOptionSubCategoryList;

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public List<TranslaterDTO> getTranslatersList() {
		return translatersList;
	}

	public void setTranslatersList(List<TranslaterDTO> translatersList) {
		this.translatersList = translatersList;
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

	public List<Zone9SummaryDisplayOptionSubCategoryDTO> getZone9SummaryDisplayOptionSubCategoryList() {
		return Zone9SummaryDisplayOptionSubCategoryList;
	}

	public void setZone9SummaryDisplayOptionSubCategoryList(
			List<Zone9SummaryDisplayOptionSubCategoryDTO> zone9SummaryDisplayOptionSubCategoryList) {
		Zone9SummaryDisplayOptionSubCategoryList = zone9SummaryDisplayOptionSubCategoryList;
	}
}
