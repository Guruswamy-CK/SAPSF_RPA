package com.rpa.sapsf.offer.approval.dto;

import java.util.List;

public class Zone5DTO {
	private String offerApproversEditable;
	private String offerApproversReOrder;
	private List<Zone5OfferApproverDTO> zone5OfferApproversList;

	public String getOfferApproversEditable() {
		return offerApproversEditable;
	}

	public void setOfferApproversEditable(String offerApproversEditable) {
		this.offerApproversEditable = offerApproversEditable;
	}

	public String getOfferApproversReOrder() {
		return offerApproversReOrder;
	}

	public void setOfferApproversReOrder(String offerApproversReOrder) {
		this.offerApproversReOrder = offerApproversReOrder;
	}

	public List<Zone5OfferApproverDTO> getZone5OfferApproversList() {
		return zone5OfferApproversList;
	}

	public void setZone5OfferApproversList(List<Zone5OfferApproverDTO> zone5OfferApproversList) {
		this.zone5OfferApproversList = zone5OfferApproversList;
	}
}
