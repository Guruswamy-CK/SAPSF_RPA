package com.rpa.sapsf.business;

import com.rpa.sapsf.service.FileConverterService;
import com.rpa.sapsf.service.FileGeneratorService;
import com.rpa.sapsf.service.impl.FileConverterServiceImpl;
import com.rpa.sapsf.service.impl.FileGeneratorServiceImpl;

import org.springframework.web.multipart.MultipartFile;

public class FileBusinessLogic {

	private FileConverterService fileConverterService = new FileConverterServiceImpl();
	private FileGeneratorService fileGeneratorService = new FileGeneratorServiceImpl();

	public String directToFileConvertion(MultipartFile file, String directionSelected, String moduleSelected,
			String modelSelected) {
		if ("excelToXml".equalsIgnoreCase(directionSelected)) {
			return this.excelToXmlFileConvert(file, modelSelected);
		} else if ("xmlToExcel".equalsIgnoreCase(directionSelected)) {
			return this.xmlToExcelFileConvert(file, modelSelected);
		}
		return null;
	}

	private String excelToXmlFileConvert(MultipartFile file, String modelSelected) {
		switch (modelSelected) {
		case "offerApprovalTemplate":
			return this.fileConverterService.excelToXmlFileConvertOfferApprovalTemplate(file);
		case "candidateProfileTemplate":
			return this.fileConverterService.excelToXmlFileConvertCandidateProfileTemplate(file);
		case "jobRequisitionTemplate":
			return this.fileConverterService.excelToXmlFileConvertJobRequisitionTemplate(file);
		case "applicationTemplate":
			return this.fileConverterService.excelToXmlFileConvertCandidateApplicationTemplate(file);
		case "corporateDataModelTemplate":
			return this.fileConverterService.excelToXmlFileConvertCorporateDataModelTemplate(file);
		}
		return null;
	}

	private String xmlToExcelFileConvert(MultipartFile file, String modelSelected) {
		switch (modelSelected) {
		case "offerApprovalTemplate":
			return this.fileConverterService.xmlToExcelFileConvertOfferApprovalTemplate(file);
		case "candidateProfileTemplate":
			return this.fileConverterService.xmlToExcelFileConvertCandidateProfileTemplate(file);
		case "jobRequisitionTemplate":
			return this.fileConverterService.xmlToExcelFileConvertJobRequisitionTemplate(file);
		case "applicationTemplate":
			return this.fileConverterService.xmlToExcelFileConvertCandidateApplicationTemplate(file);
		case "corporateDataModelTemplate":
			return this.fileConverterService.xmlToExcelFileConvertCorporateDataModelTemplate(file);
		}
		return null;
	}

	public String directToWbpFileGeneration(MultipartFile file) {
		return this.fileGeneratorService.generateWbpWorkbook(file);
	}
	
	public String directToJobFileGeneration(MultipartFile file) {
		return this.fileGeneratorService.generateJobWorkbook(file);
	}
}
