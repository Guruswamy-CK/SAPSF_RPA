package com.rpa.sapsf.candidate.application;

import java.util.List;

import com.rpa.sapsf.dto.TranslaterDTO;

public class Zone7SummaryDisplayOptionDTO {
	private String categoryId;
	private String categoryName;
	private String label;
	private String fieldRefId;
	private String selectByDefault;
	private String gridOrder;
	private List<TranslaterDTO> translatersList;
	private List<Zone7SummaryDisplayOptionSubCategoryDTO> Zone7SummaryDisplayOptionSubCategoryList;

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

	public List<Zone7SummaryDisplayOptionSubCategoryDTO> getZone7SummaryDisplayOptionSubCategoryList() {
		return Zone7SummaryDisplayOptionSubCategoryList;
	}

	public void setZone7SummaryDisplayOptionSubCategoryList(
			List<Zone7SummaryDisplayOptionSubCategoryDTO> zone7SummaryDisplayOptionSubCategoryList) {
		Zone7SummaryDisplayOptionSubCategoryList = zone7SummaryDisplayOptionSubCategoryList;
	}
}
