package com.rpa.sapsf.dto;

public class TranslaterDTO {
	private String translateLabelName;
	private String translateLang;
	
	public String getTranslateLabel() {
		return translateLabelName;
	}
	public void setTranslateLabel(String translateLabelName) {
		this.translateLabelName = translateLabelName;
	}
	public String getTranslateLang() {
		return translateLang;
	}
	public void setTranslateLang(String translateLang) {
		this.translateLang = translateLang;
	}
}
