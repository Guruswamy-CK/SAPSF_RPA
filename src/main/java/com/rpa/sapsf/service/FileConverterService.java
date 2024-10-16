package com.rpa.sapsf.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileConverterService {
	String excelToXmlFileConvertOfferApprovalTemplate(MultipartFile file);

	String xmlToExcelFileConvertOfferApprovalTemplate(MultipartFile file);

	String excelToXmlFileConvertCandidateProfileTemplate(MultipartFile file);

	String xmlToExcelFileConvertCandidateProfileTemplate(MultipartFile file);

	String excelToXmlFileConvertJobRequisitionTemplate(MultipartFile file);
	
	String xmlToExcelFileConvertJobRequisitionTemplate(MultipartFile file);

	String excelToXmlFileConvertCandidateApplicationTemplate(MultipartFile file);
	
	String xmlToExcelFileConvertCandidateApplicationTemplate(MultipartFile file);

	String excelToXmlFileConvertCorporateDataModelTemplate(MultipartFile file);

	String xmlToExcelFileConvertCorporateDataModelTemplate(MultipartFile file);
}
