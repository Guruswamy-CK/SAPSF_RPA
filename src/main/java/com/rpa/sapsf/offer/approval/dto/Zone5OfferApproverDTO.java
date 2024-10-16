package com.rpa.sapsf.offer.approval.dto;

import java.util.List;

public class Zone5OfferApproverDTO {
	private String stepId;
	private String editable;
	private String editInvalidUser;
	private String label;
	private String defaultUserType;
	private String defaultUser;
	private List<Zone5OfferApproverTranslaterDTO> zone5OfferApproverTranslatersList;

	public String getStepId() {
		return stepId;
	}

	public void setStepId(String stepId) {
		this.stepId = stepId;
	}

	public String getEditable() {
		return editable;
	}

	public void setEditable(String editable) {
		this.editable = editable;
	}

	public String getEditInvalidUser() {
		return editInvalidUser;
	}

	public void setEditInvalidUser(String editInvalidUser) {
		this.editInvalidUser = editInvalidUser;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDefaultUserType() {
		return defaultUserType;
	}

	public void setDefaultUserType(String defaultUserType) {
		this.defaultUserType = defaultUserType;
	}

	public String getDefaultUser() {
		return defaultUser;
	}

	public void setDefaultUser(String defaultUser) {
		this.defaultUser = defaultUser;
	}

	public List<Zone5OfferApproverTranslaterDTO> getZone5OfferApproverTranslatersList() {
		return zone5OfferApproverTranslatersList;
	}

	public void setZone5OfferApproverTranslatersList(
			List<Zone5OfferApproverTranslaterDTO> zone5OfferApproverTranslatersList) {
		this.zone5OfferApproverTranslatersList = zone5OfferApproverTranslatersList;
	}
}
