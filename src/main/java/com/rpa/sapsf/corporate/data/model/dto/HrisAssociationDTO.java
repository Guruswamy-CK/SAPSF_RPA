package com.rpa.sapsf.corporate.data.model.dto;

public class HrisAssociationDTO {
	private String id;
	private String multiplicity;
	private String required;
	private String destinationEntity;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMultiplicity() {
		return multiplicity;
	}

	public void setMultiplicity(String multiplicity) {
		this.multiplicity = multiplicity;
	}

	public String getRequired() {
		return required;
	}

	public void setRequired(String required) {
		this.required = required;
	}

	public String getDestinationEntity() {
		return destinationEntity;
	}

	public void setDestinationEntity(String destinationEntity) {
		this.destinationEntity = destinationEntity;
	}
}
