package com.rpa.sapsf.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rpa.sapsf.candidate.application.CandidateApplicationTemplateDTO;
import com.rpa.sapsf.candidate.profile.dto.CandidateProfileTemplateDTO;
import com.rpa.sapsf.corporate.data.model.dto.CorporateDataModelTemplateDTO;
import com.rpa.sapsf.offer.approval.dto.OfferApprovalTemplateDTO;
import com.rpa.sapsf.job.requisition.dto.JobRequisitionTemplateDTO;
import com.rpa.sapsf.service.FileConverterService;
import com.rpa.sapsf.utils.fileConverter.CandidateApplicationTemplateUtils;
import com.rpa.sapsf.utils.fileConverter.CandidateProfileTemplateUtils;
import com.rpa.sapsf.utils.fileConverter.CommonTemplateUtils;
import com.rpa.sapsf.utils.fileConverter.CorporateDataModelTemplateUtils;
import com.rpa.sapsf.utils.fileConverter.JobRequisitionTemplateUtils;
import com.rpa.sapsf.utils.fileConverter.OfferApprovalTemplateUtils;

@Service
public class FileConverterServiceImpl implements FileConverterService {

	@Override
	public String excelToXmlFileConvertOfferApprovalTemplate(MultipartFile file) {
		OfferApprovalTemplateDTO offerApprovalTemplateExcelData = OfferApprovalTemplateUtils.extractExcelData(file);
		return OfferApprovalTemplateUtils.buildXmlFile(offerApprovalTemplateExcelData, CommonTemplateUtils.readOriginalFileName(file));
	}

	@Override
	public String xmlToExcelFileConvertOfferApprovalTemplate(MultipartFile file) {
		OfferApprovalTemplateDTO offerApprovalTemplateXmlData = OfferApprovalTemplateUtils.extractXmlData(file);
		return OfferApprovalTemplateUtils.buildExcelFile(offerApprovalTemplateXmlData, CommonTemplateUtils.readOriginalFileName(file));
	}

	@Override
	public String excelToXmlFileConvertCandidateProfileTemplate(MultipartFile file) {
		CandidateProfileTemplateDTO candidateProfileTemplateExcelData = CandidateProfileTemplateUtils.extractExcelData(file);
		return CandidateProfileTemplateUtils.buildXmlFile(candidateProfileTemplateExcelData, CommonTemplateUtils.readOriginalFileName(file));
	}
	
	@Override
	public String xmlToExcelFileConvertCandidateProfileTemplate(MultipartFile file) {
		CandidateProfileTemplateDTO candidateProfileTemplateXmlData = CandidateProfileTemplateUtils.extractXmlData(file);
		return CandidateProfileTemplateUtils.buildExcelFile(candidateProfileTemplateXmlData, CommonTemplateUtils.readOriginalFileName(file));
	}

	@Override
	public String excelToXmlFileConvertJobRequisitionTemplate(MultipartFile file) {
		JobRequisitionTemplateDTO jobRequisitionTemplateExcelData = JobRequisitionTemplateUtils.extractExcelData(file);
		return JobRequisitionTemplateUtils.buildXmlFile(jobRequisitionTemplateExcelData, CommonTemplateUtils.readOriginalFileName(file));
	}

	@Override
	public String xmlToExcelFileConvertJobRequisitionTemplate(MultipartFile file) {
		JobRequisitionTemplateDTO jobRequisitionTemplateXmlData = JobRequisitionTemplateUtils.extractXmlData(file);
		return JobRequisitionTemplateUtils.buildExcelFile(jobRequisitionTemplateXmlData, CommonTemplateUtils.readOriginalFileName(file));
	}

	@Override
	public String excelToXmlFileConvertCandidateApplicationTemplate(MultipartFile file) {
		CandidateApplicationTemplateDTO candidateApplicationTemplateExcelData = CandidateApplicationTemplateUtils.extractExcelData(file);
		return CandidateApplicationTemplateUtils.buildXmlFile(candidateApplicationTemplateExcelData, CommonTemplateUtils.readOriginalFileName(file));
	}

	@Override
	public String xmlToExcelFileConvertCandidateApplicationTemplate(MultipartFile file) {
		CandidateApplicationTemplateDTO candidateApplicationTemplateXmlData = CandidateApplicationTemplateUtils.extractXmlData(file);
		return CandidateApplicationTemplateUtils.buildExcelFile(candidateApplicationTemplateXmlData, CommonTemplateUtils.readOriginalFileName(file));
	}

	@Override
	public String excelToXmlFileConvertCorporateDataModelTemplate(MultipartFile file) {
		CorporateDataModelTemplateDTO corporateDataModelTemplateExcelData = CorporateDataModelTemplateUtils.extractExcelData(file);
		return CorporateDataModelTemplateUtils.buildXmlFile(corporateDataModelTemplateExcelData, CommonTemplateUtils.readOriginalFileName(file));
	}

	@Override
	public String xmlToExcelFileConvertCorporateDataModelTemplate(MultipartFile file) {
		CorporateDataModelTemplateDTO corporateDataModelTemplateXmlData = CorporateDataModelTemplateUtils.extractXmlData(file);
		return CorporateDataModelTemplateUtils.buildExcelFile(corporateDataModelTemplateXmlData, CommonTemplateUtils.readOriginalFileName(file));
	}
}
