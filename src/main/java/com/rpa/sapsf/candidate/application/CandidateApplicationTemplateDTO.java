package com.rpa.sapsf.candidate.application;

import java.util.List;

import com.rpa.sapsf.dto.EnumDTO;

public class CandidateApplicationTemplateDTO {
	private Zone2DTO zone2;
	private Zone3DTO zone3;
	private Zone4DTO zone4;
	private Zone5DTO zone5;
	private Zone6DTO zone6;
	private Zone7DTO zone7;
	private List<EnumDTO> enumValuesList;

	public Zone2DTO getZone2() {
		return zone2;
	}

	public void setZone2(Zone2DTO zone2) {
		this.zone2 = zone2;
	}

	public Zone3DTO getZone3() {
		return zone3;
	}

	public void setZone3(Zone3DTO zone3) {
		this.zone3 = zone3;
	}

	public Zone4DTO getZone4() {
		return zone4;
	}

	public void setZone4(Zone4DTO zone4) {
		this.zone4 = zone4;
	}

	public Zone5DTO getZone5() {
		return zone5;
	}

	public void setZone5(Zone5DTO zone5) {
		this.zone5 = zone5;
	}

	public Zone6DTO getZone6() {
		return zone6;
	}

	public void setZone6(Zone6DTO zone6) {
		this.zone6 = zone6;
	}

	public Zone7DTO getZone7() {
		return zone7;
	}

	public void setZone7(Zone7DTO zone7) {
		this.zone7 = zone7;
	}
	
	public List<EnumDTO> getEnumValuesList() {
		return enumValuesList;
	}

	public void setEnumValuesList(List<EnumDTO> enumValuesList) {
		this.enumValuesList = enumValuesList;
	}
}
