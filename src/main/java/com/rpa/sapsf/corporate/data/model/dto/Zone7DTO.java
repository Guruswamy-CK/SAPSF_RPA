package com.rpa.sapsf.corporate.data.model.dto;

import java.util.List;

public class Zone7DTO {
	private List<HrisElementDTO> hrisElementsPayComponentGroupList;
	private List<HrisElementDTO> hrisElementsFrequencyList;

	public List<HrisElementDTO> getHrisElementsPayComponentGroupList() {
		return hrisElementsPayComponentGroupList;
	}

	public void setHrisElementsPayComponentGroupList(List<HrisElementDTO> hrisElementsPayComponentGroupList) {
		this.hrisElementsPayComponentGroupList = hrisElementsPayComponentGroupList;
	}

	public List<HrisElementDTO> getHrisElementsFrequencyList() {
		return hrisElementsFrequencyList;
	}

	public void setHrisElementsFrequencyList(List<HrisElementDTO> hrisElementsFrequencyList) {
		this.hrisElementsFrequencyList = hrisElementsFrequencyList;
	}
}
