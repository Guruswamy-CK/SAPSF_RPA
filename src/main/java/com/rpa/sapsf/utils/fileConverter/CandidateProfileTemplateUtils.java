package com.rpa.sapsf.utils.fileConverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.rpa.sapsf.candidate.profile.dto.*;
import com.rpa.sapsf.dto.EnumDTO;
import com.rpa.sapsf.dto.EnumLabelTranslaterDTO;
import com.rpa.sapsf.dto.TranslaterDTO;

import io.micrometer.common.util.StringUtils;

public class CandidateProfileTemplateUtils {
	static int suffix_bg_alphanumeric = 1;
	static int suffix_bg_decimal_boolean = 1;
	static int suffix_bg_integer = 1;
	static int suffix_bg_date = 1;
	static boolean isReadPermissionReady = false;
	static boolean isWritePermissionReady = false;

	public static CandidateProfileTemplateDTO extractExcelData(MultipartFile file) {
		CandidateProfileTemplateDTO candidateProfileTemplateExcelData = new CandidateProfileTemplateDTO();

		// Zone 2 objects
		Zone2DTO zone2Data = new Zone2DTO();
		List<Zone2TemplateNameTranslaterDTO> zone2TemplateNameTranslatersList = new ArrayList<Zone2TemplateNameTranslaterDTO>();
		List<Zone2TemplateDescriptionTranslaterDTO> zone2TemplateDescriptionTranslatersList = new ArrayList<Zone2TemplateDescriptionTranslaterDTO>();
		List<String> translateTemplateNamesList = new ArrayList<String>();
		List<String> translateTemplateDesrciptionsList = new ArrayList<String>();

		// Zone 3 objects
		Zone3DTO zone3Data = new Zone3DTO();
		List<Zone3InstructionHeaderDTO> zone3InstructionHeadersList = new ArrayList<Zone3InstructionHeaderDTO>();

		// Zone 4 objects
		Zone4DTO zone4Data = new Zone4DTO();
		List<Zone4OperatorInstructionHeaderDTO> zone4OperatorInstructionHeadersList = new ArrayList<Zone4OperatorInstructionHeaderDTO>();

		// Zone 5 objects
		Zone5DTO zone5Data = new Zone5DTO();
		List<Zone5FieldDefinitionDTO> zone5FieldDefinitionsList = new ArrayList<Zone5FieldDefinitionDTO>();

		// Zone 6 objects
		Zone6DTO zone6Data = new Zone6DTO();
		List<Zone6InstructionFooterDTO> zone6InstructionFootersList = new ArrayList<Zone6InstructionFooterDTO>();

		// Zone 7 objects
		Zone7DTO zone7Data = new Zone7DTO();
		List<Zone7OperatorInstructionFooterDTO> zone7OperatorInstructionFootersList = new ArrayList<Zone7OperatorInstructionFooterDTO>();

		// Zone 8 objects
		Zone8DTO zone8Data = new Zone8DTO();
		List<Zone8SearchDisplayOptionDTO> zone8SearchDisplayOptionsList = new ArrayList<Zone8SearchDisplayOptionDTO>();

		// Zone 9 objects
		Zone9DTO zone9Data = new Zone9DTO();
		List<Zone9SummaryDisplayOptionDTO> zone9SummaryDisplayOptionsList = new ArrayList<Zone9SummaryDisplayOptionDTO>();

		// Zone 10 objects
		Zone10DTO zone10Data = new Zone10DTO();
		List<Zone10BackgroundElementDTO> zone10BackgroundElementsList = new ArrayList<Zone10BackgroundElementDTO>();
		boolean isBackgroundElementStarted = false;

		// Zone 11 objects
		Zone11DTO zone11Data = new Zone11DTO();
		List<Zone11SmMappingDTO> zone11SmMappingsList = new ArrayList<Zone11SmMappingDTO>();

		// Zone 12 objects
		Zone12DTO zone12Data = new Zone12DTO();
		List<Zone12FieldPermissionDTO> zone12FieldPermissionsList = new ArrayList<Zone12FieldPermissionDTO>();

		// Common Objects
		List<String> translateLangsList = new ArrayList<String>();
		List<String> translateLangsSearchSummaryList = new ArrayList<String>();
		List<EnumDTO> enumValuesList = new ArrayList<EnumDTO>();
		List<String> roleNamesList = new ArrayList<String>();
		List<String> sourcesList = new ArrayList<String>();
		List<String> countriesList = new ArrayList<String>();

		try {
			XSSFWorkbook workBook;
			workBook = new XSSFWorkbook(file.getInputStream());
			XSSFSheet sheetTemplate = workBook.getSheet("Candidate_Profile");
			XSSFSheet sheetSearch_Summery = workBook.getSheet("Search_N_Summary_Display_Option");
			XSSFRow row;
			int rowNum = 0;
			int rowNumSs = 0;
			int translatersStartColNum = 0;
			int fieldPermissionsStartColNum = 0;
			int rowNumSearch_SummerySheetStart = 0;
			int rowNumCandidateProfileSheetStart = 0;
			int rowNumSummaryStart = 0;
			Iterator<Row> rowsTemplate = null;
			Iterator<Row> rowsSearch_Summery = null;
			if (null != sheetTemplate)
				rowsTemplate = sheetTemplate.rowIterator();
			if (null != sheetSearch_Summery)
				rowsSearch_Summery = sheetSearch_Summery.rowIterator();
			CommonTemplateUtils.extractEnumData("Candidate_Profile_Enum_Values", workBook, enumValuesList);
			/* Search & Summary read starts here */
			while (null != rowsSearch_Summery && rowsSearch_Summery.hasNext()) {
				rowNumSs++;
				row = (XSSFRow) rowsSearch_Summery.next();
				if (row.getCell(3) != null) {
					if (null != CommonTemplateUtils.readCellValue(row.getCell(3))
							&& "Summary Display Options Configuration"
									.equalsIgnoreCase(CommonTemplateUtils.readCellValue(row.getCell(3)).trim()))
						rowNumSummaryStart = rowNumSs;
				}
				if (rowNumSummaryStart == 0) {
					if (row.getCell(3) != null
							&& "Template Name".equalsIgnoreCase(CommonTemplateUtils.readCellValue(row.getCell(3)))) {
						rowNumSearch_SummerySheetStart = rowNumSs;
					}
					if (rowNumSearch_SummerySheetStart != 0) {
						if (rowNumSs == (rowNumSearch_SummerySheetStart + 3)) {
							int colNum = 14;
							while (null != row.getCell(colNum)
									&& StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colNum)))) {
								translateLangsSearchSummaryList
										.add(CommonTemplateUtils.readCellValue(row.getCell(colNum)).substring(0, 5));
								colNum++;
							}
						} else if (rowNumSs > (rowNumSearch_SummerySheetStart + 3)) {
							/* Zone 8 starts here */
							if (null != row.getCell(3)
									&& StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(3)))) {
								Zone8SearchDisplayOptionDTO zone8SearchDisplayOption = new Zone8SearchDisplayOptionDTO();
								zone8SearchDisplayOption
										.setCategoryId(CommonTemplateUtils.readCellValue(row.getCell(3)));
								zone8SearchDisplayOption
										.setCategoryName(CommonTemplateUtils.readCellValue(row.getCell(4)));
								zone8SearchDisplayOption.setLabel(CommonTemplateUtils.readCellValue(row.getCell(5)));
								zone8SearchDisplayOption
										.setFieldRefId(CommonTemplateUtils.readCellValue(row.getCell(6)));
								zone8SearchDisplayOption
										.setBackgroundElementRef(CommonTemplateUtils.readCellValue(row.getCell(7)));
								zone8SearchDisplayOption
										.setSelectByDefault(CommonTemplateUtils.readCellValue(row.getCell(8)));
								zone8SearchDisplayOption
										.setGridOrder(CommonTemplateUtils.readCellValue(row.getCell(9)));
								zone8SearchDisplayOption
										.setTagsNeeded(CommonTemplateUtils.readCellValue(row.getCell(10)));
								int colNum = 14;
								int index = 0;
								List<TranslaterDTO> translatersList = new ArrayList<TranslaterDTO>();
								while (null != row.getCell(colNum)) {
									if (StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colNum)))
											&& index < translateLangsSearchSummaryList.size()) {
										TranslaterDTO translaterData = new TranslaterDTO();
										translaterData.setTranslateLabel(
												CommonTemplateUtils.readCellValue(row.getCell(colNum)));
										translaterData.setTranslateLang(translateLangsSearchSummaryList.get(index));
										translatersList.add(translaterData);
									}
									colNum++;
									index++;
								}
								zone8SearchDisplayOption.setTranslatersList(translatersList);
								zone8SearchDisplayOption.setZone8SearchDisplayOptionSubCategoryList(
										new ArrayList<Zone8SearchDisplayOptionSubCategoryDTO>());

								Zone8SearchDisplayOptionSubCategoryDTO zone8SearchDisplayOptionSubCategory = new Zone8SearchDisplayOptionSubCategoryDTO();
								zone8SearchDisplayOptionSubCategory
										.setFieldRefId(zone8SearchDisplayOption.getFieldRefId());
								zone8SearchDisplayOptionSubCategory
										.setBackgroundElementRef(zone8SearchDisplayOption.getBackgroundElementRef());
								zone8SearchDisplayOptionSubCategory
										.setSelectByDefault(zone8SearchDisplayOption.getSelectByDefault());
								zone8SearchDisplayOptionSubCategory
										.setGridOrder(zone8SearchDisplayOption.getGridOrder());
								zone8SearchDisplayOptionSubCategory
										.setTagsNeeded(zone8SearchDisplayOption.getTagsNeeded());
								zone8SearchDisplayOptionSubCategory.setTranslatersList(translatersList);
								zone8SearchDisplayOption.getZone8SearchDisplayOptionSubCategoryList()
										.add(zone8SearchDisplayOptionSubCategory);

								zone8SearchDisplayOptionsList.add(zone8SearchDisplayOption);
							} else if (null != row.getCell(6)
									&& StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(6)))) {
								Zone8SearchDisplayOptionDTO zone8SearchDisplayOption = zone8SearchDisplayOptionsList
										.get(zone8SearchDisplayOptionsList.size() - 1);
								Zone8SearchDisplayOptionSubCategoryDTO zone8SearchDisplayOptionSubCategory = new Zone8SearchDisplayOptionSubCategoryDTO();
								zone8SearchDisplayOptionSubCategory
										.setFieldRefId(CommonTemplateUtils.readCellValue(row.getCell(6)));
								zone8SearchDisplayOptionSubCategory
										.setBackgroundElementRef(CommonTemplateUtils.readCellValue(row.getCell(7)));
								zone8SearchDisplayOptionSubCategory
										.setSelectByDefault(CommonTemplateUtils.readCellValue(row.getCell(8)));
								zone8SearchDisplayOptionSubCategory
										.setGridOrder(CommonTemplateUtils.readCellValue(row.getCell(9)));
								zone8SearchDisplayOptionSubCategory
										.setTagsNeeded(CommonTemplateUtils.readCellValue(row.getCell(10)));
								int colNum = 14;
								int index = 0;
								List<TranslaterDTO> translatersList = new ArrayList<TranslaterDTO>();
								while (null != row.getCell(colNum)) {
									if (StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colNum)))
											&& index < translateLangsSearchSummaryList.size()) {
										TranslaterDTO translaterData = new TranslaterDTO();
										translaterData.setTranslateLabel(
												CommonTemplateUtils.readCellValue(row.getCell(colNum)));
										translaterData.setTranslateLang(translateLangsSearchSummaryList.get(index));
										translatersList.add(translaterData);
									}
									colNum++;
									index++;
								}
								zone8SearchDisplayOptionSubCategory.setTranslatersList(translatersList);
								zone8SearchDisplayOption.getZone8SearchDisplayOptionSubCategoryList()
										.add(zone8SearchDisplayOptionSubCategory);
							}
							/* Zone 8 ends here */
						}
					}
				} else {
					/* Zone 9 starts here */
					if (rowNumSs > rowNumSummaryStart + 1) {
						if (null != row.getCell(3)
								&& StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(3)))) {
							Zone9SummaryDisplayOptionDTO zone9SummaryDisplayOption = new Zone9SummaryDisplayOptionDTO();
							zone9SummaryDisplayOption.setCategoryId(CommonTemplateUtils.readCellValue(row.getCell(3)));
							zone9SummaryDisplayOption
									.setCategoryName(CommonTemplateUtils.readCellValue(row.getCell(4)));
							zone9SummaryDisplayOption.setLabel(CommonTemplateUtils.readCellValue(row.getCell(5)));
							zone9SummaryDisplayOption.setFieldRefId(CommonTemplateUtils.readCellValue(row.getCell(6)));
							zone9SummaryDisplayOption
									.setSelectByDefault(CommonTemplateUtils.readCellValue(row.getCell(7)));
							zone9SummaryDisplayOption.setGridOrder(CommonTemplateUtils.readCellValue(row.getCell(8)));
							int colNum = 14;
							int index = 0;
							List<TranslaterDTO> translatersList = new ArrayList<TranslaterDTO>();
							while (null != row.getCell(colNum)) {
								if (StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colNum)))
										&& index < translateLangsSearchSummaryList.size()) {
									TranslaterDTO translaterData = new TranslaterDTO();
									translaterData
											.setTranslateLabel(CommonTemplateUtils.readCellValue(row.getCell(colNum)));
									translaterData.setTranslateLang(translateLangsSearchSummaryList.get(index));
									translatersList.add(translaterData);
								}
								colNum++;
								index++;
							}
							zone9SummaryDisplayOption.setTranslatersList(translatersList);
							zone9SummaryDisplayOption.setZone9SummaryDisplayOptionSubCategoryList(
									new ArrayList<Zone9SummaryDisplayOptionSubCategoryDTO>());

							Zone9SummaryDisplayOptionSubCategoryDTO zone9SummaryDisplayOptionSubCategory = new Zone9SummaryDisplayOptionSubCategoryDTO();
							zone9SummaryDisplayOptionSubCategory
									.setFieldRefId(zone9SummaryDisplayOption.getFieldRefId());
							zone9SummaryDisplayOptionSubCategory
									.setSelectByDefault(zone9SummaryDisplayOption.getSelectByDefault());
							zone9SummaryDisplayOptionSubCategory.setGridOrder(zone9SummaryDisplayOption.getGridOrder());
							zone9SummaryDisplayOptionSubCategory.setTranslatersList(translatersList);
							zone9SummaryDisplayOption.getZone9SummaryDisplayOptionSubCategoryList()
									.add(zone9SummaryDisplayOptionSubCategory);

							zone9SummaryDisplayOptionsList.add(zone9SummaryDisplayOption);
						} else if (null != row.getCell(6)
								&& StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(6)))) {
							Zone9SummaryDisplayOptionDTO zone9SummaryDisplayOption = zone9SummaryDisplayOptionsList
									.get(zone9SummaryDisplayOptionsList.size() - 1);
							Zone9SummaryDisplayOptionSubCategoryDTO zone9SummaryDisplayOptionSubCategory = new Zone9SummaryDisplayOptionSubCategoryDTO();
							zone9SummaryDisplayOptionSubCategory
									.setFieldRefId(CommonTemplateUtils.readCellValue(row.getCell(6)));
							zone9SummaryDisplayOptionSubCategory
									.setSelectByDefault(CommonTemplateUtils.readCellValue(row.getCell(7)));
							zone9SummaryDisplayOptionSubCategory
									.setGridOrder(CommonTemplateUtils.readCellValue(row.getCell(8)));
							int colNum = 14;
							int index = 0;
							List<TranslaterDTO> translatersList = new ArrayList<TranslaterDTO>();
							while (null != row.getCell(colNum)) {
								if (StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colNum)))
										&& index < translateLangsSearchSummaryList.size()) {
									TranslaterDTO translaterData = new TranslaterDTO();
									translaterData
											.setTranslateLabel(CommonTemplateUtils.readCellValue(row.getCell(colNum)));
									translaterData.setTranslateLang(translateLangsSearchSummaryList.get(index));
									translatersList.add(translaterData);
								}
								colNum++;
								index++;
							}
							zone9SummaryDisplayOptionSubCategory.setTranslatersList(translatersList);
							zone9SummaryDisplayOption.getZone9SummaryDisplayOptionSubCategoryList()
									.add(zone9SummaryDisplayOptionSubCategory);
						}
						/* Zone 9 ends here */
					}
				}
			}
			/* Search & Summary read ends here */

			/* Template read starts here */
			while (null != rowsTemplate && rowsTemplate.hasNext()) {
				rowNum++;
				row = (XSSFRow) rowsTemplate.next();
				if (row.getCell(3) != null
						&& "Template Name".equalsIgnoreCase(CommonTemplateUtils.readCellValue(row.getCell(3)))) {
					rowNumCandidateProfileSheetStart = rowNum;
				}
				if (rowNumCandidateProfileSheetStart != 0) {
					/* Zone 2 starts here */
					if (rowNum == rowNumCandidateProfileSheetStart) {
						zone2Data.setTemplateName(CommonTemplateUtils.readCellValue(row.getCell(4)));

						for (int translatersStartColNumTemp = 5; translatersStartColNum == 0; translatersStartColNumTemp++) {
							if (null != row.getCell(translatersStartColNumTemp)
									&& StringUtils.isNotEmpty(
											CommonTemplateUtils.readCellValue(row.getCell(translatersStartColNumTemp)))
									&& "Template Name and Field Label Translations".equalsIgnoreCase(CommonTemplateUtils
											.readCellValue(row.getCell(translatersStartColNumTemp)).trim())) {
								translatersStartColNum = translatersStartColNumTemp;
							}
						}
						int colNum = translatersStartColNum + 1;
						while (null != row.getCell(colNum)
								&& StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colNum)))) {
							translateTemplateNamesList.add(CommonTemplateUtils.readCellValue(row.getCell(colNum)));
							colNum++;
						}
					} else if (rowNum == (rowNumCandidateProfileSheetStart + 1)) {
						zone2Data.setTemplateDescription(CommonTemplateUtils.readCellValue(row.getCell(4)));

						for (int fieldPermissionsStartColNumTemp = 5; fieldPermissionsStartColNum == 0; fieldPermissionsStartColNumTemp++) {
							if (null != row.getCell(fieldPermissionsStartColNumTemp)
									&& StringUtils.isNotEmpty(CommonTemplateUtils
											.readCellValue(row.getCell(fieldPermissionsStartColNumTemp)))
									&& "Field Permissions".equalsIgnoreCase(CommonTemplateUtils
											.readCellValue(row.getCell(fieldPermissionsStartColNumTemp)).trim())) {
								fieldPermissionsStartColNum = fieldPermissionsStartColNumTemp;
							}
						}
						int roleColNum = fieldPermissionsStartColNum + 1;
						while (null != row.getCell(roleColNum)
								&& StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(roleColNum)))) {
							roleNamesList.add(CommonTemplateUtils.readCellValue(row.getCell(roleColNum)));
							roleColNum++;
						}
						int colNum = translatersStartColNum + 1;
						while (null != row.getCell(colNum)
								&& StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colNum)))) {
							translateTemplateDesrciptionsList
									.add(CommonTemplateUtils.readCellValue(row.getCell(colNum)));
							colNum++;
						}
					} else if (rowNum == (rowNumCandidateProfileSheetStart + 2)) {
						int sourceColNum = fieldPermissionsStartColNum + 1;
						for (int i = 0; i < roleNamesList.size(); i++) {
							sourcesList.add(CommonTemplateUtils.readCellValue(row.getCell(sourceColNum + i)));
						}
					} else if (rowNum == (rowNumCandidateProfileSheetStart + 3)) {
						int countryColNum = fieldPermissionsStartColNum + 1;
						for (int i = 0; i < roleNamesList.size(); i++) {
							countriesList.add(CommonTemplateUtils.readCellValue(row.getCell(countryColNum + i)));
						}
						int colNum = translatersStartColNum + 1;
						while (null != row.getCell(colNum)
								&& StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colNum)))) {
							translateLangsList
									.add(CommonTemplateUtils.readCellValue(row.getCell(colNum)).substring(0, 5));
							colNum++;
						}
						/* Zone 12 starts here */
						if (roleNamesList.size() > 0) {
							for (int i = 0; i < roleNamesList.size(); i++) {
								Zone12FieldPermissionDTO zone12FieldPermission = new Zone12FieldPermissionDTO();
								zone12FieldPermission.setRoleName(roleNamesList.get(i));
								if (i < countriesList.size()) {
									zone12FieldPermission.setCountry(countriesList.get(i));
								}
								if (i < sourcesList.size()) {
									zone12FieldPermission.setSource(sourcesList.get(i));
								}
								zone12FieldPermission.setFieldPermissionsList(new ArrayList<FieldPermissionDTO>());

								if (StringUtils.isNotEmpty(zone12FieldPermission.getRoleName())
										&& StringUtils.isNotEmpty(zone12FieldPermission.getCountry())
										&& StringUtils.isNotEmpty(zone12FieldPermission.getSource()))
									zone12FieldPermissionsList.add(zone12FieldPermission);
							}
						}
						/* Zone 12 ends here */
					}
					/* Zone 2 ends here */
					else if (rowNum > (rowNumCandidateProfileSheetStart + 3)) {
						if (null != row.getCell(5)
								&& StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(5)))) {
							String fieldType = CommonTemplateUtils.readCellValue(row.getCell(5)).trim();
							if ("instruction-header".equalsIgnoreCase(fieldType)
									|| "instruction-header-internal-candidate".equalsIgnoreCase(fieldType)
									|| "instruction-header-external-candidate".equalsIgnoreCase(fieldType)) {
								composeZone3Data(row, zone3InstructionHeadersList, translateLangsList,
										translatersStartColNum);
							} else if ("operator-instruction-header".equalsIgnoreCase(fieldType)) {
								composeZone4Data(row, zone4OperatorInstructionHeadersList, translateLangsList,
										translatersStartColNum);
							} else if ("instruction-footer".equalsIgnoreCase(fieldType)
									|| "instruction-footer-internal-candidate".equalsIgnoreCase(fieldType)
									|| "instruction-footer-external-candidate".equalsIgnoreCase(fieldType)) {
								composeZone6Data(row, zone6InstructionFootersList, translateLangsList,
										translatersStartColNum);
							} else if ("operator-instruction-footer".equalsIgnoreCase(fieldType)) {
								composeZone7Data(row, zone7OperatorInstructionFootersList, translateLangsList,
										translatersStartColNum);
							} else if ("Background Element".equalsIgnoreCase(fieldType)) {
								isBackgroundElementStarted = true;
								zone10BackgroundElementsList.add(new Zone10BackgroundElementDTO());
								composeZone10Data(row, zone10BackgroundElementsList, translateLangsList,
										translatersStartColNum, fieldPermissionsStartColNum,
										zone12FieldPermissionsList);
							} else if (isBackgroundElementStarted) {
								composeZone10DataFieldData(row, zone10BackgroundElementsList, translateLangsList,
										translatersStartColNum, fieldPermissionsStartColNum,
										zone12FieldPermissionsList);
							} else {
								composeZone5Data(row, zone5FieldDefinitionsList, translateLangsList,
										translatersStartColNum, fieldPermissionsStartColNum,
										zone12FieldPermissionsList);
							}
						}
					}
				}
			}
			/* Zone 2 starts here */
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy hh:mm a");
			Date date = new Date();
			zone2Data.setLastModifiedDate(formatter.format(date).toUpperCase());
			for (int i = 0; i < translateLangsList.size(); i++) {
				Zone2TemplateNameTranslaterDTO zone2TemplateNameTranslaterData = new Zone2TemplateNameTranslaterDTO();
				Zone2TemplateDescriptionTranslaterDTO zone2TemplateDescriptionTranslaterData = new Zone2TemplateDescriptionTranslaterDTO();
				if (i < translateTemplateNamesList.size()
						&& StringUtils.isNotEmpty(translateTemplateNamesList.get(i))) {
					zone2TemplateNameTranslaterData.setTemplateName(translateTemplateNamesList.get(i));
					zone2TemplateNameTranslaterData.setTranslateLang(translateLangsList.get(i));
					zone2TemplateNameTranslatersList.add(zone2TemplateNameTranslaterData);
				}
				if (i < translateTemplateDesrciptionsList.size()
						&& StringUtils.isNotEmpty(translateTemplateDesrciptionsList.get(i))) {
					zone2TemplateDescriptionTranslaterData
							.setTemplateDescription(translateTemplateDesrciptionsList.get(i));
					zone2TemplateDescriptionTranslaterData.setTranslateLang(translateLangsList.get(i));
					zone2TemplateDescriptionTranslatersList.add(zone2TemplateDescriptionTranslaterData);
				}
			}
			zone2Data.setZone2TemplateNameTranslatersList(zone2TemplateNameTranslatersList);
			zone2Data.setZone2TemplateDescriptionTranslatersList(zone2TemplateDescriptionTranslatersList);

			/* Zone 2 ends here */

			/* Template read ends here */

			candidateProfileTemplateExcelData.setZone2(zone2Data);
			zone3Data.setZone3InstructionHeadersList(zone3InstructionHeadersList);
			candidateProfileTemplateExcelData.setZone3(zone3Data);
			zone4Data.setZone4OperatorInstructionHeadersList(zone4OperatorInstructionHeadersList);
			candidateProfileTemplateExcelData.setZone4(zone4Data);
			zone5Data.setZone5FieldDefinitionsList(zone5FieldDefinitionsList);
			candidateProfileTemplateExcelData.setZone5(zone5Data);
			zone6Data.setZone6InstructionFootersList(zone6InstructionFootersList);
			candidateProfileTemplateExcelData.setZone6(zone6Data);
			zone7Data.setZone7OperatorInstructionFootersList(zone7OperatorInstructionFootersList);
			candidateProfileTemplateExcelData.setZone7(zone7Data);
			zone8Data.setZone8SearchDisplayOptionsList(zone8SearchDisplayOptionsList);
			candidateProfileTemplateExcelData.setZone8(zone8Data);
			zone9Data.setZone9SummaryDisplayOptionsList(zone9SummaryDisplayOptionsList);
			candidateProfileTemplateExcelData.setZone9(zone9Data);
			zone10Data.setZone10BackgroundElementsList(zone10BackgroundElementsList);
			candidateProfileTemplateExcelData.setZone10(zone10Data);
			zone11Data.setZone11SmMappingsList(zone11SmMappingsList);
			candidateProfileTemplateExcelData.setZone11(zone11Data);
			zone12Data.setZone12FieldPermissionsList(zone12FieldPermissionsList);
			candidateProfileTemplateExcelData.setZone12(zone12Data);
			candidateProfileTemplateExcelData.setEnumValuesList(enumValuesList);

			/* Zone 11 starts here */
			zone5Data.getZone5FieldDefinitionsList().forEach(fieldDef -> {
				if (StringUtils.isNotEmpty(fieldDef.getFieldId())
						&& StringUtils.isNotEmpty(fieldDef.getEmployeeProfileFieldId())) {
					Zone11SmMappingDTO zone11SmMapping = new Zone11SmMappingDTO();
					zone11SmMapping.setFieldId(fieldDef.getFieldId());
					zone11SmMapping.setEmployeeProfileFieldId(fieldDef.getEmployeeProfileFieldId());
					zone11SmMappingsList.add(zone11SmMapping);
				}
			});
			zone10Data.getZone10BackgroundElementsList().forEach(backgroundElement -> {
				if (StringUtils.isNotEmpty(backgroundElement.getFieldId())
						&& StringUtils.isNotEmpty(backgroundElement.getEmployeeProfileFieldId())) {
					Zone11SmMappingDTO zone11SmMapping = new Zone11SmMappingDTO();
					zone11SmMapping.setFieldId(backgroundElement.getFieldId());
					zone11SmMapping.setEmployeeProfileFieldId(backgroundElement.getEmployeeProfileFieldId());
					zone11SmMappingsList.add(zone11SmMapping);
				}
				backgroundElement.getZone10BackgroundElementDataFieldsList().forEach(dataField -> {
					if (StringUtils.isNotEmpty(dataField.getFieldId())
							&& StringUtils.isNotEmpty(dataField.getEmployeeProfileFieldId())) {
						Zone11SmMappingDTO zone11SmMapping = new Zone11SmMappingDTO();
						zone11SmMapping.setFieldId(dataField.getFieldId());
						zone11SmMapping.setEmployeeProfileFieldId(dataField.getEmployeeProfileFieldId());
						zone11SmMappingsList.add(zone11SmMapping);
					}
				});
			});
			/* Zone 11 ends here */

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return candidateProfileTemplateExcelData;
	}

	private static void composeZone3Data(XSSFRow row, List<Zone3InstructionHeaderDTO> zone3InstructionHeadersList,
			List<String> translateLangsList, int translatersStartColNum) {
		Zone3InstructionHeaderDTO zone3InstructionHeader = new Zone3InstructionHeaderDTO();
		zone3InstructionHeader.setFieldLabel(CommonTemplateUtils.readCellValue(row.getCell(4)));
		zone3InstructionHeader.setFieldType(CommonTemplateUtils.readCellValue(row.getCell(5)));
		zone3InstructionHeader.setMimeType(CommonTemplateUtils.readCellValue(row.getCell(6)));
		List<Zone3InstructionHeaderTranslaterDTO> zone3InstructionHeaderTranslatersList = new ArrayList<Zone3InstructionHeaderTranslaterDTO>();

		int colNum = translatersStartColNum + 1;
		int indexZone3 = 0;
		while (null != row.getCell(colNum)) {
			if (StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colNum)))
					&& indexZone3 < translateLangsList.size()) {
				Zone3InstructionHeaderTranslaterDTO zone3InstructionHeaderTranslater = new Zone3InstructionHeaderTranslaterDTO();
				zone3InstructionHeaderTranslater.setLabelName(CommonTemplateUtils.readCellValue(row.getCell(colNum)));
				zone3InstructionHeaderTranslater.setTranslateLang(translateLangsList.get(indexZone3));
				zone3InstructionHeaderTranslatersList.add(zone3InstructionHeaderTranslater);
			}
			colNum++;
			indexZone3++;
		}
		zone3InstructionHeader.setZone3InstructionHeaderTranslatersList(zone3InstructionHeaderTranslatersList);
		zone3InstructionHeadersList.add(zone3InstructionHeader);
	}

	private static void composeZone4Data(XSSFRow row,
			List<Zone4OperatorInstructionHeaderDTO> zone4OperatorInstructionHeadersList,
			List<String> translateLangsList, int translatersStartColNum) {
		Zone4OperatorInstructionHeaderDTO zone4OperatorInstructionHeader = new Zone4OperatorInstructionHeaderDTO();
		zone4OperatorInstructionHeader.setFieldLabel(CommonTemplateUtils.readCellValue(row.getCell(4)));
		zone4OperatorInstructionHeader.setFieldType(CommonTemplateUtils.readCellValue(row.getCell(5)));
		zone4OperatorInstructionHeader.setMimeType(CommonTemplateUtils.readCellValue(row.getCell(6)));
		List<Zone4OperatorInstructionHeaderTranslaterDTO> zone4OperatorInstructionHeaderTranslatersList = new ArrayList<Zone4OperatorInstructionHeaderTranslaterDTO>();

		int colNum = translatersStartColNum + 1;
		int indexZone4 = 0;
		while (null != row.getCell(colNum)) {
			if (StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colNum)))
					&& indexZone4 < translateLangsList.size()) {
				Zone4OperatorInstructionHeaderTranslaterDTO zone4OperatorInstructionHeaderTranslater = new Zone4OperatorInstructionHeaderTranslaterDTO();
				zone4OperatorInstructionHeaderTranslater
						.setLabelName(CommonTemplateUtils.readCellValue(row.getCell(colNum)));
				zone4OperatorInstructionHeaderTranslater.setTranslateLang(translateLangsList.get(indexZone4));
				zone4OperatorInstructionHeaderTranslatersList.add(zone4OperatorInstructionHeaderTranslater);
			}
			colNum++;
			indexZone4++;
		}
		zone4OperatorInstructionHeader
				.setZone4OperatorInstructionHeaderTranslatersList(zone4OperatorInstructionHeaderTranslatersList);
		zone4OperatorInstructionHeadersList.add(zone4OperatorInstructionHeader);
	}

	private static void composeZone5Data(XSSFRow row, List<Zone5FieldDefinitionDTO> zone5FieldDefinitionsList,
			List<String> translateLangsList, int translatersStartColNum, int fieldPermissionsStartColNum,
			List<Zone12FieldPermissionDTO> zone12FieldPermissionsList) {
		Zone5FieldDefinitionDTO zone5FieldDefinition = new Zone5FieldDefinitionDTO();
		zone5FieldDefinition.setFieldId(CommonTemplateUtils.readCellValue(row.getCell(3)));
		zone5FieldDefinition.setFieldLabel(CommonTemplateUtils.readCellValue(row.getCell(4)));
		zone5FieldDefinition.setFieldType(CommonTemplateUtils.readCellValue(row.getCell(5)));
		zone5FieldDefinition.setMimeType(CommonTemplateUtils.readCellValue(row.getCell(6)));
		zone5FieldDefinition.setParentPickListIdOrObject(CommonTemplateUtils.readCellValue(row.getCell(7)));
		zone5FieldDefinition.setPickListOrObject(CommonTemplateUtils.readCellValue(row.getCell(8)));
		zone5FieldDefinition.setObjectType(CommonTemplateUtils.readCellValue(row.getCell(9)));
		zone5FieldDefinition.setRequired(CommonTemplateUtils.readCellValue(row.getCell(10)));
		zone5FieldDefinition.setCustom(CommonTemplateUtils.readCellValue(row.getCell(12)));
		zone5FieldDefinition.setAnonymize(CommonTemplateUtils.readCellValue(row.getCell(13)));
		zone5FieldDefinition.setSensitive(CommonTemplateUtils.readCellValue(row.getCell(14)));
		zone5FieldDefinition.setFieldDescription(CommonTemplateUtils.readCellValue(row.getCell(15)));
		zone5FieldDefinition.setEmployeeTypeId(CommonTemplateUtils.readCellValue(row.getCell(16)));
		zone5FieldDefinition.setEmployeeProfileFieldId(CommonTemplateUtils.readCellValue(row.getCell(17)));
		zone5FieldDefinition.setAvailableForCandidateSearhResult(CommonTemplateUtils.readCellValue(row.getCell(18)));
		zone5FieldDefinition.setDisplayOrFilter(CommonTemplateUtils.readCellValue(row.getCell(19)));
		zone5FieldDefinition.setReportable(CommonTemplateUtils.readCellValue(row.getCell(20)));
		zone5FieldDefinition.setNotesOrComments(CommonTemplateUtils.readCellValue(row.getCell(21)));

		int colPermissionNum = fieldPermissionsStartColNum + 1;
		int indexPermissionZone5 = 0;
		while (null != row.getCell(colPermissionNum)) {
			if (StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colPermissionNum)))
					&& indexPermissionZone5 < zone12FieldPermissionsList.size()) {
				FieldPermissionDTO fieldPermission = new FieldPermissionDTO();
				fieldPermission.setFieldId(zone5FieldDefinition.getFieldId());
				fieldPermission.setFieldType(zone5FieldDefinition.getFieldType());
				fieldPermission.setPermissionType(CommonTemplateUtils.readCellValue(row.getCell(colPermissionNum)));
				zone12FieldPermissionsList.get(indexPermissionZone5).getFieldPermissionsList().add(fieldPermission);
			}
			colPermissionNum++;
			indexPermissionZone5++;
		}

		List<Zone5FieldDefinitionTranslaterDTO> zone5FieldDefinitionTranslatersList = new ArrayList<Zone5FieldDefinitionTranslaterDTO>();
		int colTransNum = translatersStartColNum + 1;
		int indexTransZone5 = 0;
		while (null != row.getCell(colTransNum)) {
			if (StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colTransNum)))
					&& indexTransZone5 < translateLangsList.size()) {
				Zone5FieldDefinitionTranslaterDTO zone5FieldDefinitionTranslater = new Zone5FieldDefinitionTranslaterDTO();
				zone5FieldDefinitionTranslater
						.setLabelName(CommonTemplateUtils.readCellValue(row.getCell(colTransNum)));
				zone5FieldDefinitionTranslater.setTranslateLang(translateLangsList.get(indexTransZone5));
				zone5FieldDefinitionTranslatersList.add(zone5FieldDefinitionTranslater);
			}
			colTransNum++;
			indexTransZone5++;
		}
		zone5FieldDefinition.setZone5FieldDefinitionTranslatersList(zone5FieldDefinitionTranslatersList);
		zone5FieldDefinitionsList.add(zone5FieldDefinition);
	}

	private static void composeZone6Data(XSSFRow row, List<Zone6InstructionFooterDTO> zone6InstructionFootersList,
			List<String> translateLangsList, int translatersStartColNum) {
		Zone6InstructionFooterDTO zone6InstructionFooter = new Zone6InstructionFooterDTO();
		zone6InstructionFooter.setFieldLabel(CommonTemplateUtils.readCellValue(row.getCell(4)));
		zone6InstructionFooter.setFieldType(CommonTemplateUtils.readCellValue(row.getCell(5)));
		zone6InstructionFooter.setMimeType(CommonTemplateUtils.readCellValue(row.getCell(6)));
		List<Zone6InstructionFooterTranslaterDTO> zone6InstructionFooterTranslatersList = new ArrayList<Zone6InstructionFooterTranslaterDTO>();

		int colNum = translatersStartColNum + 1;
		int indexZone6 = 0;
		while (null != row.getCell(colNum)) {
			if (StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colNum)))
					&& indexZone6 < translateLangsList.size()) {
				Zone6InstructionFooterTranslaterDTO zone6InstructionFooterTranslater = new Zone6InstructionFooterTranslaterDTO();
				zone6InstructionFooterTranslater.setLabelName(CommonTemplateUtils.readCellValue(row.getCell(colNum)));
				zone6InstructionFooterTranslater.setTranslateLang(translateLangsList.get(indexZone6));
				zone6InstructionFooterTranslatersList.add(zone6InstructionFooterTranslater);
			}
			colNum++;
			indexZone6++;
		}
		zone6InstructionFooter.setZone6InstructionFooterTranslatersList(zone6InstructionFooterTranslatersList);
		zone6InstructionFootersList.add(zone6InstructionFooter);
	}

	private static void composeZone7Data(XSSFRow row,
			List<Zone7OperatorInstructionFooterDTO> zone7OperatorInstructionFootersList,
			List<String> translateLangsList, int translatersStartColNum) {
		Zone7OperatorInstructionFooterDTO zone7OperatorInstructionFooter = new Zone7OperatorInstructionFooterDTO();
		zone7OperatorInstructionFooter.setFieldLabel(CommonTemplateUtils.readCellValue(row.getCell(4)));
		zone7OperatorInstructionFooter.setFieldType(CommonTemplateUtils.readCellValue(row.getCell(5)));
		zone7OperatorInstructionFooter.setMimeType(CommonTemplateUtils.readCellValue(row.getCell(6)));
		List<Zone7OperatorInstructionFooterTranslaterDTO> zone7OperatorInstructionFooterTranslatersList = new ArrayList<Zone7OperatorInstructionFooterTranslaterDTO>();

		int colNum = translatersStartColNum + 1;
		int indexZone7 = 0;
		while (null != row.getCell(colNum)) {
			if (StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colNum)))
					&& indexZone7 < translateLangsList.size()) {
				Zone7OperatorInstructionFooterTranslaterDTO zone7OperatorInstructionFooterTranslater = new Zone7OperatorInstructionFooterTranslaterDTO();
				zone7OperatorInstructionFooterTranslater
						.setLabelName(CommonTemplateUtils.readCellValue(row.getCell(colNum)));
				zone7OperatorInstructionFooterTranslater.setTranslateLang(translateLangsList.get(indexZone7));
				zone7OperatorInstructionFooterTranslatersList.add(zone7OperatorInstructionFooterTranslater);
			}
			colNum++;
			indexZone7++;
		}
		zone7OperatorInstructionFooter
				.setZone7OperatorInstructionFooterTranslatersList(zone7OperatorInstructionFooterTranslatersList);
		zone7OperatorInstructionFootersList.add(zone7OperatorInstructionFooter);
	}

	private static void composeZone10Data(XSSFRow row, List<Zone10BackgroundElementDTO> zone10BackgroundElementsList,
			List<String> translateLangsList, int translatersStartColNum, int fieldPermissionsStartColNum,
			List<Zone12FieldPermissionDTO> zone12FieldPermissionsList) {
		Zone10BackgroundElementDTO zone10BackgroundElement = zone10BackgroundElementsList
				.get(zone10BackgroundElementsList.size() - 1);
		zone10BackgroundElement.setFieldId(CommonTemplateUtils.readCellValue(row.getCell(3)));
		zone10BackgroundElement.setFieldlabel(CommonTemplateUtils.readCellValue(row.getCell(4)));
		zone10BackgroundElement.setFieldType(CommonTemplateUtils.readCellValue(row.getCell(5)));
		zone10BackgroundElement.setMimeType(CommonTemplateUtils.readCellValue(row.getCell(6)));
		zone10BackgroundElement.setParentPickListIdOrObject(CommonTemplateUtils.readCellValue(row.getCell(7)));
		zone10BackgroundElement.setPickListObject(CommonTemplateUtils.readCellValue(row.getCell(8)));
		zone10BackgroundElement.setRequired(CommonTemplateUtils.readCellValue(row.getCell(10)));
		zone10BackgroundElement.setMaxLength(CommonTemplateUtils.readCellValue(row.getCell(11)));
		zone10BackgroundElement.setCustom(CommonTemplateUtils.readCellValue(row.getCell(12)));
		zone10BackgroundElement.setAnonymize(CommonTemplateUtils.readCellValue(row.getCell(13)));
		zone10BackgroundElement.setSensitive(CommonTemplateUtils.readCellValue(row.getCell(14)));
		zone10BackgroundElement.setEmployeeTypeId(CommonTemplateUtils.readCellValue(row.getCell(16)));
		zone10BackgroundElement.setEmployeeProfileFieldId(CommonTemplateUtils.readCellValue(row.getCell(17)));

		int colPermissionNum = fieldPermissionsStartColNum + 1;
		int indexPermissionZone10 = 0;
		while (null != row.getCell(colPermissionNum)) {
			if (StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colPermissionNum)))
					&& indexPermissionZone10 < zone12FieldPermissionsList.size()) {
				FieldPermissionDTO fieldPermission = new FieldPermissionDTO();
				fieldPermission.setFieldId(zone10BackgroundElement.getFieldId());
				fieldPermission.setFieldType(zone10BackgroundElement.getFieldType());
				fieldPermission.setPermissionType(CommonTemplateUtils.readCellValue(row.getCell(colPermissionNum)));
				zone12FieldPermissionsList.get(indexPermissionZone10).getFieldPermissionsList().add(fieldPermission);
			}
			colPermissionNum++;
			indexPermissionZone10++;
		}

		List<Zone10BackgroundElementTranslaterDTO> zone10BackgroundElementTranslatersList = new ArrayList<Zone10BackgroundElementTranslaterDTO>();
		int colTransNum = translatersStartColNum + 1;
		int indexTransZone10 = 0;
		while (null != row.getCell(colTransNum)) {
			if (StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colTransNum)))
					&& indexTransZone10 < translateLangsList.size()) {
				Zone10BackgroundElementTranslaterDTO zone10BackgroundElementTranslater = new Zone10BackgroundElementTranslaterDTO();
				zone10BackgroundElementTranslater
						.setLabelName(CommonTemplateUtils.readCellValue(row.getCell(colTransNum)));
				zone10BackgroundElementTranslater.setTranslateLang(translateLangsList.get(indexTransZone10));
				zone10BackgroundElementTranslatersList.add(zone10BackgroundElementTranslater);
			}
			colTransNum++;
			indexTransZone10++;
		}
		zone10BackgroundElement.setZone10BackgroundElementTranslatersList(zone10BackgroundElementTranslatersList);
		zone10BackgroundElement
				.setZone10BackgroundElementDataFieldsList(new ArrayList<Zone10BackgroundElementDataFieldDTO>());
	}

	private static void composeZone10DataFieldData(XSSFRow row,
			List<Zone10BackgroundElementDTO> zone10BackgroundElementsList, List<String> translateLangsList,
			int translatersStartColNum, int fieldPermissionsStartColNum,
			List<Zone12FieldPermissionDTO> zone12FieldPermissionsList) {

		Zone10BackgroundElementDTO zone10BackgroundElement = zone10BackgroundElementsList
				.get(zone10BackgroundElementsList.size() - 1);
		Zone10BackgroundElementDataFieldDTO zone10BackgroundElementDataField = new Zone10BackgroundElementDataFieldDTO();
		zone10BackgroundElementDataField.setFieldId(CommonTemplateUtils.readCellValue(row.getCell(3)));
		zone10BackgroundElementDataField.setFieldlabel(CommonTemplateUtils.readCellValue(row.getCell(4)));
		zone10BackgroundElementDataField.setFieldType(CommonTemplateUtils.readCellValue(row.getCell(5)));
		zone10BackgroundElementDataField.setMimeType(CommonTemplateUtils.readCellValue(row.getCell(6)));
		zone10BackgroundElementDataField.setParentPickListIdOrObject(CommonTemplateUtils.readCellValue(row.getCell(7)));
		zone10BackgroundElementDataField.setPickListObject(CommonTemplateUtils.readCellValue(row.getCell(8)));
		zone10BackgroundElementDataField.setRequired(CommonTemplateUtils.readCellValue(row.getCell(10)));
		zone10BackgroundElementDataField.setMaxLength(CommonTemplateUtils.readCellValue(row.getCell(11)));
		zone10BackgroundElementDataField.setCustom(CommonTemplateUtils.readCellValue(row.getCell(12)));
		zone10BackgroundElementDataField.setAnonymize(CommonTemplateUtils.readCellValue(row.getCell(13)));
		zone10BackgroundElementDataField.setSensitive(CommonTemplateUtils.readCellValue(row.getCell(14)));
		zone10BackgroundElementDataField.setEmployeeTypeId(CommonTemplateUtils.readCellValue(row.getCell(16)));
		zone10BackgroundElementDataField.setEmployeeProfileFieldId(CommonTemplateUtils.readCellValue(row.getCell(17)));

		int colPermissionNum = fieldPermissionsStartColNum + 1;
		int indexPermissionZone10 = 0;
		while (null != row.getCell(colPermissionNum)) {
			if (StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colPermissionNum)))
					&& indexPermissionZone10 < zone12FieldPermissionsList.size()) {
				FieldPermissionDTO fieldPermission = new FieldPermissionDTO();
				fieldPermission.setFieldId(zone10BackgroundElementDataField.getFieldId());
				fieldPermission.setFieldType(zone10BackgroundElementDataField.getFieldType());
				fieldPermission.setPermissionType(CommonTemplateUtils.readCellValue(row.getCell(colPermissionNum)));
				fieldPermission.setBeFieldId(zone10BackgroundElement.getFieldId());
				zone12FieldPermissionsList.get(indexPermissionZone10).getFieldPermissionsList().add(fieldPermission);
			}
			colPermissionNum++;
			indexPermissionZone10++;
		}

		List<Zone10BackgroundElementDataFieldTranslaterDTO> zone10BackgroundElementDataFieldTranslatersList = new ArrayList<Zone10BackgroundElementDataFieldTranslaterDTO>();
		int colTransNum = translatersStartColNum + 1;
		int indexTransZone10 = 0;
		while (null != row.getCell(colTransNum)) {
			if (StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colTransNum)))
					&& indexTransZone10 < translateLangsList.size()) {
				Zone10BackgroundElementDataFieldTranslaterDTO zone10BackgroundElementDataFieldTranslater = new Zone10BackgroundElementDataFieldTranslaterDTO();
				zone10BackgroundElementDataFieldTranslater
						.setLabelName(CommonTemplateUtils.readCellValue(row.getCell(colTransNum)));
				zone10BackgroundElementDataFieldTranslater.setTranslateLang(translateLangsList.get(indexTransZone10));
				zone10BackgroundElementDataFieldTranslatersList.add(zone10BackgroundElementDataFieldTranslater);
			}
			colTransNum++;
			indexTransZone10++;
		}
		zone10BackgroundElementDataField
				.setZone10BackgroundElementDataFieldTranslatersList(zone10BackgroundElementDataFieldTranslatersList);
		zone10BackgroundElement.getZone10BackgroundElementDataFieldsList().add(zone10BackgroundElementDataField);
	}

	public static String buildXmlFile(CandidateProfileTemplateDTO candidateProfileTemplateExcelData, String fileName) {
		StringBuilder xmlStringBuilder = new StringBuilder();
		xmlStringBuilder.append("<candidate-profile-data-model>\n");
		// Zone 2 starts here
		StringBuilder xmlStrZone2 = new StringBuilder();
		buildXmlStrZone2(xmlStrZone2, candidateProfileTemplateExcelData.getZone2());
		xmlStringBuilder.append(xmlStrZone2);
		// Zone 2 ends here

		// Zone 3 starts here
		StringBuilder xmlStrZone3 = new StringBuilder();
		buildXmlStrZone3(xmlStrZone3, candidateProfileTemplateExcelData.getZone3());
		xmlStringBuilder.append(xmlStrZone3);
		// Zone 3 ends here

		// Zone 4 starts here
		StringBuilder xmlStrZone4 = new StringBuilder();
		buildXmlStrZone4(xmlStrZone4, candidateProfileTemplateExcelData.getZone4());
		xmlStringBuilder.append(xmlStrZone4);
		// Zone 4 ends here

		// Zone 5 starts here
		StringBuilder xmlStrZone5 = new StringBuilder();
		buildXmlStrZone5(xmlStrZone5, candidateProfileTemplateExcelData.getZone5(),
				candidateProfileTemplateExcelData.getEnumValuesList());
		xmlStringBuilder.append(xmlStrZone5);
		// Zone 5 ends here

		// Zone 6 starts here
		StringBuilder xmlStrZone6 = new StringBuilder();
		buildXmlStrZone6(xmlStrZone6, candidateProfileTemplateExcelData.getZone6());
		xmlStringBuilder.append(xmlStrZone6);
		// Zone 6 ends here

		// Zone 7 starts here
		StringBuilder xmlStrZone7 = new StringBuilder();
		buildXmlStrZone7(xmlStrZone7, candidateProfileTemplateExcelData.getZone7());
		xmlStringBuilder.append(xmlStrZone7);
		// Zone 7 ends here

		// Zone 8 starts here
		StringBuilder xmlStrZone8 = new StringBuilder();
		buildXmlStrZone8(xmlStrZone8, candidateProfileTemplateExcelData.getZone8());
		xmlStringBuilder.append(xmlStrZone8);
		// Zone 8 ends here

		// Zone 9 starts here
		StringBuilder xmlStrZone9 = new StringBuilder();
		buildXmlStrZone9(xmlStrZone9, candidateProfileTemplateExcelData.getZone9());
		xmlStringBuilder.append(xmlStrZone9);
		// Zone 9 ends here

		// Zone 10 starts here
		StringBuilder xmlStrZone10 = new StringBuilder();
		buildXmlStrZone10(xmlStrZone10, candidateProfileTemplateExcelData.getZone10());
		xmlStringBuilder.append(xmlStrZone10);
		// Zone 10 ends here

		// Zone 11 starts here
		StringBuilder xmlStrZone11 = new StringBuilder();
		buildXmlStrZone11(xmlStrZone11, candidateProfileTemplateExcelData.getZone11());
		xmlStringBuilder.append(xmlStrZone11);
		// Zone 11 ends here

		// Zone 12 starts here
		StringBuilder xmlStrZone12 = new StringBuilder();
		buildXmlStrZone12(xmlStrZone12, candidateProfileTemplateExcelData.getZone12());
		xmlStringBuilder.append(xmlStrZone12);
		// Zone 12 ends here

		xmlStringBuilder.append("</candidate-profile-data-model>");
		Document doc = CommonTemplateUtils.convertStringToDocument(xmlStringBuilder.toString());
		return CommonTemplateUtils.convertDocumentToXml(doc, fileName,
				"-//SuccessFactors, Inc.//DTD Candidate Profile Data Model//EN", "candidate-profile-data-model.dtd");
	}

	private static void buildXmlStrZone2(StringBuilder xmlStrZone2, Zone2DTO zone2dto) {
		if (null != zone2dto) {
			if (StringUtils.isNotEmpty(zone2dto.getTemplateName())) {
				xmlStrZone2.append("<template-name>");
				xmlStrZone2.append("<![CDATA[" + zone2dto.getTemplateName() + "]]>");
				xmlStrZone2.append("</template-name>\n");
			}
			zone2dto.getZone2TemplateNameTranslatersList().forEach(translater -> {
				xmlStrZone2.append("<template-name lang=\"" + translater.getTranslateLang() + "\">");
				xmlStrZone2.append("<![CDATA[" + translater.getTemplateName() + "]]>");
				xmlStrZone2.append("</template-name>\n");
			});
			if (StringUtils.isNotEmpty(zone2dto.getTemplateDescription())) {
				xmlStrZone2.append("<template-desc>");
				xmlStrZone2.append("<![CDATA[" + zone2dto.getTemplateDescription() + "]]>");
				xmlStrZone2.append("</template-desc>\n");
			}
			zone2dto.getZone2TemplateDescriptionTranslatersList().forEach(translater -> {
				xmlStrZone2.append("<template-desc lang=\"" + translater.getTranslateLang() + "\">");
				xmlStrZone2.append("<![CDATA[" + translater.getTemplateDescription() + "]]>");
				xmlStrZone2.append("</template-desc>\n");
			});
			xmlStrZone2
					.append("<template-lastmodified>" + zone2dto.getLastModifiedDate() + "</template-lastmodified>\n");
		}
	}

	private static void buildXmlStrZone3(StringBuilder xmlStrZone3, Zone3DTO zone3dto) {
		if (null != zone3dto) {
			zone3dto.getZone3InstructionHeadersList().forEach(instructionHeader -> {
				xmlStrZone3.append("<" + instructionHeader.getFieldType() + ">\n");
				xmlStrZone3.append("<label");
				CommonTemplateUtils.appendStringBuilderOptionalAttribute(xmlStrZone3, "mime-type",
						instructionHeader.getMimeType(), true);
				xmlStrZone3.append("><![CDATA[" + instructionHeader.getFieldLabel() + "]]></label>\n");
				instructionHeader.getZone3InstructionHeaderTranslatersList().forEach(translator -> {
					xmlStrZone3.append("<label xml:lang=\"" + translator.getTranslateLang() + "\"");
					CommonTemplateUtils.appendStringBuilderOptionalAttribute(xmlStrZone3, "mime-type",
							instructionHeader.getMimeType(), true);
					xmlStrZone3.append("><![CDATA[" + translator.getLabelName() + "]]></label>\n");
				});
				xmlStrZone3.append("</" + instructionHeader.getFieldType() + ">\n");
			});
		}
	}

	private static void buildXmlStrZone4(StringBuilder xmlStrZone4, Zone4DTO zone4dto) {
		if (null != zone4dto) {
			zone4dto.getZone4OperatorInstructionHeadersList().forEach(operatorInstructionHeader -> {
				xmlStrZone4.append("<operator-instruction-header>\n");
				xmlStrZone4.append("<label");
				CommonTemplateUtils.appendStringBuilderOptionalAttribute(xmlStrZone4, "mime-type",
						operatorInstructionHeader.getMimeType(), true);
				xmlStrZone4.append("><![CDATA[" + operatorInstructionHeader.getFieldLabel() + "]]></label>\n");
				operatorInstructionHeader.getZone4OperatorInstructionHeaderTranslatersList().forEach(translator -> {
					xmlStrZone4.append("<label xml:lang=\"" + translator.getTranslateLang() + "\"");
					CommonTemplateUtils.appendStringBuilderOptionalAttribute(xmlStrZone4, "mime-type",
							operatorInstructionHeader.getMimeType(), true);
					xmlStrZone4.append("><![CDATA[" + translator.getLabelName() + "]]></label>\n");
				});
				xmlStrZone4.append("</operator-instruction-header>\n");
			});
		}
	}

	private static void buildXmlStrZone5(StringBuilder xmlStrZone5, Zone5DTO zone5dto, List<EnumDTO> enumValuesList) {
		if (null != zone5dto) {
			zone5dto.getZone5FieldDefinitionsList().forEach(fieldDefinition -> {
				xmlStrZone5.append("<field-definition");
				xmlStrZone5.append(" id=\"" + fieldDefinition.getFieldId() + "\"");
				xmlStrZone5.append(" type=\"" + fieldDefinition.getFieldType() + "\"");
				xmlStrZone5.append(" required=\"" + (StringUtils.isEmpty(fieldDefinition.getRequired()) ? ""
						: (("Y").equalsIgnoreCase(fieldDefinition.getRequired().substring(0, 1)) ? true : false))
						+ "\"");
				xmlStrZone5.append(" custom=\""
						+ (StringUtils.isEmpty(fieldDefinition.getCustom()) ? ""
								: (("Y").equalsIgnoreCase(fieldDefinition.getCustom().substring(0, 1)) ? true : false))
						+ "\"");
				xmlStrZone5
						.append(" anonymize=\""
								+ (StringUtils
										.isEmpty(fieldDefinition.getAnonymize())
												? ""
												: (("Y").equalsIgnoreCase(
														fieldDefinition.getAnonymize().substring(0, 1)) ? true : false))
								+ "\"");
				CommonTemplateUtils.appendStringBuilderOptionalAttribute(xmlStrZone5, "sensitive",
						fieldDefinition.getSensitive(), false);
				if ("Object".equalsIgnoreCase(fieldDefinition.getFieldType())) {
					xmlStrZone5.append(" object-type=\"" + fieldDefinition.getObjectType() + "\"");
				}
				xmlStrZone5.append(">\n");
				if (StringUtils.isNotEmpty(fieldDefinition.getFieldLabel())) {
					xmlStrZone5.append("<field-label");
					CommonTemplateUtils.appendStringBuilderOptionalAttribute(xmlStrZone5, "mime-type",
							fieldDefinition.getMimeType(), true);
					xmlStrZone5.append("><![CDATA[" + fieldDefinition.getFieldLabel() + "]]></field-label>\n");
				}
				fieldDefinition.getZone5FieldDefinitionTranslatersList().forEach(translater -> {
					xmlStrZone5.append("<field-label lang=\"" + translater.getTranslateLang() + "\"");
					CommonTemplateUtils.appendStringBuilderOptionalAttribute(xmlStrZone5, "mime-type",
							fieldDefinition.getMimeType(), true);
					xmlStrZone5.append("><![CDATA[" + translater.getLabelName() + "]]>");
					xmlStrZone5.append("</field-label>\n");
				});

				if (StringUtils.isNotEmpty(fieldDefinition.getFieldDescription())) {
					xmlStrZone5.append("<field-description><![CDATA[" + fieldDefinition.getFieldDescription()
							+ "]]></field-description>\n");
				}

				if ("Object".equalsIgnoreCase(fieldDefinition.getFieldType())
						&& StringUtils.isNotEmpty(fieldDefinition.getPickListOrObject())
						&& StringUtils.isNotEmpty(fieldDefinition.getParentPickListIdOrObject())) {
					xmlStrZone5.append("<field-criteria destinationFieldValue=\""
							+ fieldDefinition.getPickListOrObject() + "\" sourceFieldName=\""
							+ fieldDefinition.getParentPickListIdOrObject() + "\"/>\n");
				} else if ("picklist".equalsIgnoreCase(fieldDefinition.getFieldType())) {
					if (StringUtils.isNotEmpty(fieldDefinition.getPickListOrObject())
							&& StringUtils.isNotEmpty(fieldDefinition.getParentPickListIdOrObject())) {
						xmlStrZone5.append(
								"<picklist-id parent-field-id=\"" + fieldDefinition.getParentPickListIdOrObject()
										+ "\">" + fieldDefinition.getPickListOrObject() + "</picklist-id>\n");
					} else if (StringUtils.isNotEmpty(fieldDefinition.getPickListOrObject())) {
						xmlStrZone5
								.append("<picklist-id>" + fieldDefinition.getPickListOrObject() + "</picklist-id>\n");
					}
				} else if ("enum".equalsIgnoreCase(fieldDefinition.getFieldType())) {
					enumValuesList.forEach(enumObj -> {
						if (fieldDefinition.getFieldId().equalsIgnoreCase(enumObj.getEnumFieldId())) {
							xmlStrZone5.append("<enum-value value=\"" + enumObj.getEnumValue() + "\">\n"
									+ "<enum-label><![CDATA[" + enumObj.getEnumLabel() + "]]></enum-label>\n");
							enumObj.getEnumLabelTranslatersList().forEach(translater -> {
								xmlStrZone5.append("<enum-label lang=\"" + translater.getTranslateLang()
										+ "\"><![CDATA[" + translater.getEnumLabel() + "]]></enum-label>\n");
							});
							xmlStrZone5.append("</enum-value>\n");
						}
					});
				}
				xmlStrZone5.append("</field-definition>\n");
			});
		}
	}

	private static void buildXmlStrZone6(StringBuilder xmlStrZone6, Zone6DTO zone6dto) {
		if (null != zone6dto) {
			zone6dto.getZone6InstructionFootersList().forEach(instructionFooter -> {
				xmlStrZone6.append("<" + instructionFooter.getFieldType() + ">\n");
				xmlStrZone6.append("<label");
				CommonTemplateUtils.appendStringBuilderOptionalAttribute(xmlStrZone6, "mime-type",
						instructionFooter.getMimeType(), true);
				xmlStrZone6.append("><![CDATA[" + instructionFooter.getFieldLabel() + "]]></label>\n");
				instructionFooter.getZone6InstructionFooterTranslatersList().forEach(translator -> {
					xmlStrZone6.append("<label xml:lang=\"" + translator.getTranslateLang() + "\"");
					CommonTemplateUtils.appendStringBuilderOptionalAttribute(xmlStrZone6, "mime-type",
							instructionFooter.getMimeType(), true);
					xmlStrZone6.append("><![CDATA[" + translator.getLabelName() + "]]></label>\n");
				});
				xmlStrZone6.append("</" + instructionFooter.getFieldType() + ">\n");
			});
		}
	}

	private static void buildXmlStrZone7(StringBuilder xmlStrZone7, Zone7DTO zone7dto) {
		if (null != zone7dto) {
			zone7dto.getZone7OperatorInstructionFootersList().forEach(operatorInstructionFooter -> {
				xmlStrZone7.append("<operator-instruction-footer>\n");
				xmlStrZone7.append("<label");
				CommonTemplateUtils.appendStringBuilderOptionalAttribute(xmlStrZone7, "mime-type",
						operatorInstructionFooter.getMimeType(), true);
				xmlStrZone7.append("><![CDATA[" + operatorInstructionFooter.getFieldLabel() + "]]></label>\n");
				operatorInstructionFooter.getZone7OperatorInstructionFooterTranslatersList().forEach(translator -> {
					xmlStrZone7.append("<label xml:lang=\"" + translator.getTranslateLang() + "\"");
					CommonTemplateUtils.appendStringBuilderOptionalAttribute(xmlStrZone7, "mime-type",
							operatorInstructionFooter.getMimeType(), true);
					xmlStrZone7.append("><![CDATA[" + translator.getLabelName() + "]]></label>\n");
				});
				xmlStrZone7.append("</operator-instruction-footer>\n");
			});
		}
	}

	private static void buildXmlStrZone8(StringBuilder xmlStrZone8, Zone8DTO zone8dto) {
		if (null != zone8dto && zone8dto.getZone8SearchDisplayOptionsList().size() > 0) {
			xmlStrZone8.append("<search-display-options-config>\n");
			zone8dto.getZone8SearchDisplayOptionsList().forEach(category -> {
				xmlStrZone8.append("<category id=\"" + category.getCategoryId() + "\" name=\""
						+ category.getCategoryName() + "\">\n");
				xmlStrZone8.append("<label><![CDATA[" + category.getLabel() + "]]></label>\n");
				category.getTranslatersList().forEach(translater -> {
					xmlStrZone8.append("<label xml:lang=\"" + translater.getTranslateLang() + "\"><![CDATA["
							+ translater.getTranslateLabel() + "]]></label>\n");
				});
				category.getZone8SearchDisplayOptionSubCategoryList().forEach(subCategory -> {
					xmlStrZone8
							.append("<column bge-ref=\"" + subCategory.getBackgroundElementRef() + "\" field-ref=\""
									+ subCategory.getFieldRefId() + "\" gridOrder=\"" + subCategory.getGridOrder()
									+ "\" select-by-default=\""
									+ (StringUtils.isEmpty(subCategory.getSelectByDefault()) ? ""
											: (("Y").equalsIgnoreCase(subCategory.getSelectByDefault().substring(0, 1))
													? true
													: false))
									+ "\"/>\n");
				});
				xmlStrZone8.append("</category>\n");
				if ("Y".equalsIgnoreCase(category.getTagsNeeded())) {
					xmlStrZone8.append("<category id=\"tag\" name=\"tag\">\n");
					xmlStrZone8.append("<label><![CDATA[Tags]]></label>\n");
					xmlStrZone8.append("<column gridOrder=\"1999\" name=\"tags\" select-by-default=\"true\">\n");
					xmlStrZone8.append("<label><![CDATA[tags]]></label>\n");
					xmlStrZone8.append("</column>\n");
					xmlStrZone8.append("</category>\n");
				}
			});
			xmlStrZone8.append("</search-display-options-config>\n");
		}
	}

	private static void buildXmlStrZone9(StringBuilder xmlStrZone9, Zone9DTO zone9dto) {
		if (null != zone9dto && zone9dto.getZone9SummaryDisplayOptionsList().size() > 0) {
			xmlStrZone9.append("<candidate-summary-display-options-config>\n");
			zone9dto.getZone9SummaryDisplayOptionsList().forEach(category -> {
				xmlStrZone9.append("<category id=\"" + category.getCategoryId() + "\" name=\""
						+ category.getCategoryName() + "\">\n");
				xmlStrZone9.append("<label><![CDATA[" + category.getLabel() + "]]></label>\n");
				category.getTranslatersList().forEach(translater -> {
					xmlStrZone9.append("<label xml:lang=\"" + translater.getTranslateLang() + "\"><![CDATA["
							+ translater.getTranslateLabel() + "]]></label>\n");
				});
				category.getZone9SummaryDisplayOptionSubCategoryList().forEach(subCategory -> {
					xmlStrZone9
							.append("<column field-ref=\"" + subCategory.getFieldRefId() + "\" gridOrder=\""
									+ subCategory.getGridOrder() + "\" select-by-default=\""
									+ (StringUtils.isEmpty(subCategory.getSelectByDefault()) ? ""
											: (("Y").equalsIgnoreCase(subCategory.getSelectByDefault().substring(0, 1))
													? true
													: false))
									+ "\"/>\n");
				});
				xmlStrZone9.append("</category>\n");
			});
			xmlStrZone9.append("</candidate-summary-display-options-config>\n");
		}
	}

	private static void buildXmlStrZone10(StringBuilder xmlStrZone10, Zone10DTO zone10dto) {
		if (null != zone10dto) {
			zone10dto.getZone10BackgroundElementsList().forEach(backgroundElement -> {
				xmlStrZone10
						.append("<background-element id=\"" + backgroundElement.getFieldId() + "\" type-id=\""
								+ backgroundElement.getEmployeeTypeId() + "\" required=\""
								+ (StringUtils.isEmpty(backgroundElement.getRequired()) ? ""
										: (("Y").equalsIgnoreCase(backgroundElement.getRequired().substring(0, 1))
												? true
												: false))
								+ "\">\n");
				xmlStrZone10.append("<label><![CDATA[" + backgroundElement.getFieldlabel() + "]]></label>\n");
				backgroundElement.getZone10BackgroundElementTranslatersList().forEach(translator -> {
					xmlStrZone10.append("<label xml:lang=\"" + translator.getTranslateLang() + "\"");
					xmlStrZone10.append("><![CDATA[" + translator.getLabelName() + "]]></label>\n");
				});
				backgroundElement.getZone10BackgroundElementDataFieldsList().forEach(dataFieldObj -> {
					String fieldNameConverted = "";
					if ("bg-alphanumeric".equalsIgnoreCase(dataFieldObj.getFieldType())) {
						fieldNameConverted = "vfld" + suffix_bg_alphanumeric;
						suffix_bg_alphanumeric++;
					} else if ("bg-decimal / boolean".equalsIgnoreCase(dataFieldObj.getFieldType())) {
						fieldNameConverted = "ffld" + suffix_bg_decimal_boolean;
						suffix_bg_decimal_boolean++;
					} else if ("bg-date".equalsIgnoreCase(dataFieldObj.getFieldType())) {
						fieldNameConverted = "dfld" + suffix_bg_date;
						suffix_bg_date++;
					} else if ("bg-integer".equalsIgnoreCase(dataFieldObj.getFieldType())) {
						fieldNameConverted = "ifld" + suffix_bg_integer;
						suffix_bg_integer++;
					} else if ("bg-startDate".equalsIgnoreCase(dataFieldObj.getFieldType())
							|| "bg-endDate".equalsIgnoreCase(dataFieldObj.getFieldType())) {
						fieldNameConverted = dataFieldObj.getFieldType().replace("bg-", "");
					} else {
						fieldNameConverted = dataFieldObj.getFieldType();
					}
					xmlStrZone10.append("<data-field anonymize=\""
							+ (StringUtils.isEmpty(dataFieldObj.getAnonymize()) ? ""
									: (("Y").equalsIgnoreCase(dataFieldObj.getAnonymize().substring(0, 1)) ? true
											: false))
							+ "\" field-name=\"" + fieldNameConverted + "\" id=\"" + dataFieldObj.getFieldId()
							+ "\" max-length=\"" + dataFieldObj.getMaxLength() + "\" required=\""
							+ (StringUtils.isEmpty(dataFieldObj.getRequired()) ? ""
									: (("Y").equalsIgnoreCase(dataFieldObj.getRequired().substring(0, 1)) ? true
											: false))
							+ "\">\n");
					xmlStrZone10.append("<label><![CDATA[" + dataFieldObj.getFieldlabel() + "]]></label>\n");
					dataFieldObj.getZone10BackgroundElementDataFieldTranslatersList().forEach(translator -> {
						xmlStrZone10.append("<label xml:lang=\"" + translator.getTranslateLang() + "\"");
						xmlStrZone10.append("><![CDATA[" + translator.getLabelName() + "]]></label>\n");
					});
					if (StringUtils.isNotEmpty(dataFieldObj.getPickListObject())
							&& StringUtils.isNotEmpty(dataFieldObj.getParentPickListIdOrObject())) {
						xmlStrZone10
								.append("<picklist-id parent-field-id=\"" + dataFieldObj.getParentPickListIdOrObject()
										+ "\">" + dataFieldObj.getPickListObject() + "</picklist-id>\n");
					} else if (StringUtils.isNotEmpty(dataFieldObj.getPickListObject())) {
						xmlStrZone10.append("<picklist-id>" + dataFieldObj.getPickListObject() + "</picklist-id>\n");
					}
					xmlStrZone10.append("</data-field>\n");
				});
				xmlStrZone10.append("</background-element>\n");
				suffix_bg_alphanumeric = 1;
				suffix_bg_decimal_boolean = 1;
				suffix_bg_integer = 1;
				suffix_bg_date = 1;
			});
		}
	}

	private static void buildXmlStrZone11(StringBuilder xmlStrZone11, Zone11DTO zone11dto) {
		if (null != zone11dto) {
			zone11dto.getZone11SmMappingsList().forEach(zone11SmMapping -> {
				xmlStrZone11.append("<sm-mapping field-id=\"" + zone11SmMapping.getFieldId() + "\" map-to=\""
						+ zone11SmMapping.getEmployeeProfileFieldId() + "\"/>\n");
			});
		}
	}

	private static void buildXmlStrZone12(StringBuilder xmlStrZone12, Zone12DTO zone12dto) {
		if (null != zone12dto) {
			StringBuilder writePerm = new StringBuilder();
			StringBuilder readPerm = new StringBuilder();
			String typeWrite = "write";
			String typeRead = "read";

			zone12dto.getZone12FieldPermissionsList().forEach(permissionObj -> {
				isReadPermissionReady = false;
				isWritePermissionReady = false;
				StringBuilder writePermTemp = new StringBuilder();
				StringBuilder readPermTemp = new StringBuilder();
				writePermTemp.append("<field-permission type=\"" + typeWrite + "\">\n");
				if ("candidate".equalsIgnoreCase(permissionObj.getRoleName())) {
					writePermTemp.append("<role-name><![CDATA[" + permissionObj.getRoleName() + "]]></role-name>\n");
				} else {
					writePermTemp.append("<role-name group-name=\"" + permissionObj.getRoleName() + "\"><![CDATA["
							+ permissionObj.getRoleName() + "]]></role-name>\n");
				}
				writePermTemp.append("<country><![CDATA[" + permissionObj.getCountry() + "]]></country>\n");
				writePermTemp.append("<source><![CDATA[" + permissionObj.getSource() + "]]></source>\n");
				permissionObj.getFieldPermissionsList().forEach(fieldPermission -> {
					if ("W".equalsIgnoreCase(fieldPermission.getPermissionType())) {
						isWritePermissionReady = true;
						if ("Background Element".equalsIgnoreCase(fieldPermission.getFieldType())) {
							writePermTemp.append("<bg-element data-field-id=\"*\" refid=\""
									+ fieldPermission.getFieldId() + "\"/>\n");
						} else if (fieldPermission.getFieldType().startsWith("bg-")) {
							writePermTemp.append("<bg-element data-field-id=\"" + fieldPermission.getFieldId()
									+ "\" refid=\"" + fieldPermission.getBeFieldId() + "\"/>\n");
						} else {
							writePermTemp.append("<field refid=\"" + fieldPermission.getFieldId() + "\"/>\n");
						}
					}
				});
				writePermTemp.append("</field-permission>\n");

				readPermTemp.append("<field-permission type=\"" + typeRead + "\">\n");
				if ("candidate".equalsIgnoreCase(permissionObj.getRoleName())) {
					readPermTemp.append("<role-name><![CDATA[" + permissionObj.getRoleName() + "]]></role-name>\n");
				} else {
					readPermTemp.append("<role-name group-name=\"" + permissionObj.getRoleName() + "\"><![CDATA["
							+ permissionObj.getRoleName() + "]]></role-name>\n");
				}
				readPermTemp.append("<country><![CDATA[" + permissionObj.getCountry() + "]]></country>\n");
				readPermTemp.append("<source><![CDATA[" + permissionObj.getSource() + "]]></source>\n");
				permissionObj.getFieldPermissionsList().forEach(fieldPermission -> {
					if ("R".equalsIgnoreCase(fieldPermission.getPermissionType())) {
						isReadPermissionReady = true;
						if ("Background Element".equalsIgnoreCase(fieldPermission.getFieldType())) {
							readPermTemp.append("<bg-element data-field-id=\"*\" refid=\""
									+ fieldPermission.getFieldId() + "\"/>\n");
						} else if (fieldPermission.getFieldType().startsWith("bg-")) {
							readPermTemp.append("<bg-element data-field-id=\"" + fieldPermission.getFieldId()
									+ "\" refid=\"" + fieldPermission.getBeFieldId() + "\"/>\n");
						} else {
							readPermTemp.append("<field refid=\"" + fieldPermission.getFieldId() + "\"/>\n");
						}
					}
				});
				readPermTemp.append("</field-permission>\n");
				if (isReadPermissionReady)
					readPerm.append(readPermTemp);
				if (isWritePermissionReady)
					writePerm.append(writePermTemp);
			});

			xmlStrZone12.append(readPerm);
			xmlStrZone12.append(writePerm);
		}
	}

	public static CandidateProfileTemplateDTO extractXmlData(MultipartFile file) {
		CandidateProfileTemplateDTO candidateProfileTemplateXmlData = new CandidateProfileTemplateDTO();

		// Zone 2 objects
		Zone2DTO zone2Data = new Zone2DTO();
		List<Zone2TemplateNameTranslaterDTO> zone2TemplateNameTranslatersList = new ArrayList<Zone2TemplateNameTranslaterDTO>();
		List<Zone2TemplateDescriptionTranslaterDTO> zone2TemplateDescriptionTranslatersList = new ArrayList<Zone2TemplateDescriptionTranslaterDTO>();

		// Zone 3 objects
		Zone3DTO zone3Data = new Zone3DTO();
		List<Zone3InstructionHeaderDTO> zone3InstructionHeadersList = new ArrayList<Zone3InstructionHeaderDTO>();

		// Zone 4 objects
		Zone4DTO zone4Data = new Zone4DTO();
		List<Zone4OperatorInstructionHeaderDTO> zone4OperatorInstructionHeadersList = new ArrayList<Zone4OperatorInstructionHeaderDTO>();

		// Zone 5 objects
		Zone5DTO zone5Data = new Zone5DTO();
		List<Zone5FieldDefinitionDTO> zone5FieldDefinitionsList = new ArrayList<Zone5FieldDefinitionDTO>();

		// Zone 6 objects
		Zone6DTO zone6Data = new Zone6DTO();
		List<Zone6InstructionFooterDTO> zone6InstructionFootersList = new ArrayList<Zone6InstructionFooterDTO>();

		// Zone 7 objects
		Zone7DTO zone7Data = new Zone7DTO();
		List<Zone7OperatorInstructionFooterDTO> zone7OperatorInstructionFootersList = new ArrayList<Zone7OperatorInstructionFooterDTO>();

		// Zone 8 objects
		Zone8DTO zone8Data = new Zone8DTO();
		List<Zone8SearchDisplayOptionDTO> zone8SearchDisplayOptionsList = new ArrayList<Zone8SearchDisplayOptionDTO>();

		// Zone 9 objects
		Zone9DTO zone9Data = new Zone9DTO();
		List<Zone9SummaryDisplayOptionDTO> zone9SummaryDisplayOptionsList = new ArrayList<Zone9SummaryDisplayOptionDTO>();

		// Zone 10 objects
		Zone10DTO zone10Data = new Zone10DTO();
		List<Zone10BackgroundElementDTO> zone10BackgroundElementsList = new ArrayList<Zone10BackgroundElementDTO>();

		// Zone 11 objects
		Zone11DTO zone11Data = new Zone11DTO();
		List<Zone11SmMappingDTO> zone11SmMappingsList = new ArrayList<Zone11SmMappingDTO>();

		// Zone 12 objects
		Zone12DTO zone12Data = new Zone12DTO();
		List<Zone12FieldPermissionDTO> zone12FieldPermissionsList = new ArrayList<Zone12FieldPermissionDTO>();

		// Common Objects
		List<EnumDTO> enumValuesList = new ArrayList<EnumDTO>();

		try {
			DocumentBuilderFactory dBfactory = DocumentBuilderFactory.newInstance();
			dBfactory.setValidating(false);
			dBfactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			DocumentBuilder builder = dBfactory.newDocumentBuilder();
			Document document = builder.parse(file.getInputStream());
			document.getDocumentElement().normalize();
			/* Zone2 starts here */
			NodeList n_TemplateNamesList = document.getElementsByTagName("template-name");
			for (int i = 0; i < n_TemplateNamesList.getLength(); i++) {
				Node node = n_TemplateNamesList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					if (StringUtils.isEmpty(element.getAttribute("lang"))) {
						zone2Data.setTemplateName(element.getTextContent());
					} else {
						Zone2TemplateNameTranslaterDTO tNameTranslater = new Zone2TemplateNameTranslaterDTO();
						tNameTranslater.setTranslateLang(element.getAttribute("lang"));
						tNameTranslater.setTemplateName(element.getTextContent());
						zone2TemplateNameTranslatersList.add(tNameTranslater);
					}
				}
			}
			zone2Data.setZone2TemplateNameTranslatersList(zone2TemplateNameTranslatersList);

			NodeList n_TemplateDescriptionsList = document.getElementsByTagName("template-desc");
			for (int i = 0; i < n_TemplateDescriptionsList.getLength(); i++) {
				Node node = n_TemplateDescriptionsList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					if (StringUtils.isEmpty(element.getAttribute("lang"))) {
						zone2Data.setTemplateDescription(element.getTextContent());
					} else {
						Zone2TemplateDescriptionTranslaterDTO tNameTranslater = new Zone2TemplateDescriptionTranslaterDTO();
						tNameTranslater.setTranslateLang(element.getAttribute("lang"));
						tNameTranslater.setTemplateDescription(element.getTextContent());
						zone2TemplateDescriptionTranslatersList.add(tNameTranslater);
					}
				}
			}
			zone2Data.setZone2TemplateDescriptionTranslatersList(zone2TemplateDescriptionTranslatersList);
			/* Zone2 ends here */

			/* Zone3 starts here */
			NodeList n_instructionHeadersList = document.getElementsByTagName("instruction-header");
			for (int i = 0; i < n_instructionHeadersList.getLength(); i++) {
				Node node = n_instructionHeadersList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Zone3InstructionHeaderDTO zone3InstructionHeader = new Zone3InstructionHeaderDTO();
					zone3InstructionHeader.setFieldType("instruction-header");
					Element element = (Element) node;
					NodeList n_LabelsList = element.getElementsByTagName("label");
					List<Zone3InstructionHeaderTranslaterDTO> zone3InstructionHeaderTranslatersList = new ArrayList<Zone3InstructionHeaderTranslaterDTO>();
					for (int j = 0; j < n_LabelsList.getLength(); j++) {
						Node nodeLabel = n_LabelsList.item(j);
						if (nodeLabel.getNodeType() == Node.ELEMENT_NODE) {
							Element elementLabel = (Element) nodeLabel;
							if (StringUtils.isEmpty(elementLabel.getAttribute("xml:lang"))) {
								zone3InstructionHeader.setFieldLabel(elementLabel.getTextContent());
								zone3InstructionHeader.setMimeType(elementLabel.getAttribute("mime-type"));
							} else {
								Zone3InstructionHeaderTranslaterDTO labelTranslater = new Zone3InstructionHeaderTranslaterDTO();
								labelTranslater.setTranslateLang(elementLabel.getAttribute("xml:lang"));
								zone3InstructionHeader.setMimeType(elementLabel.getAttribute("mime-type"));
								labelTranslater.setLabelName(elementLabel.getTextContent());
								zone3InstructionHeaderTranslatersList.add(labelTranslater);
							}
						}
					}
					zone3InstructionHeader
							.setZone3InstructionHeaderTranslatersList(zone3InstructionHeaderTranslatersList);
					zone3InstructionHeadersList.add(zone3InstructionHeader);
				}
			}

			NodeList n_instructionHeaderInternalCandidatesList = document
					.getElementsByTagName("instruction-header-internal-candidate");
			for (int i = 0; i < n_instructionHeaderInternalCandidatesList.getLength(); i++) {
				Node node = n_instructionHeaderInternalCandidatesList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Zone3InstructionHeaderDTO zone3InstructionHeader = new Zone3InstructionHeaderDTO();
					zone3InstructionHeader.setFieldType("instruction-header-internal-candidate");
					Element element = (Element) node;
					NodeList n_LabelsList = element.getElementsByTagName("label");
					List<Zone3InstructionHeaderTranslaterDTO> zone3InstructionHeaderTranslatersList = new ArrayList<Zone3InstructionHeaderTranslaterDTO>();
					for (int j = 0; j < n_LabelsList.getLength(); j++) {
						Node nodeLabel = n_LabelsList.item(j);
						if (nodeLabel.getNodeType() == Node.ELEMENT_NODE) {
							Element elementLabel = (Element) nodeLabel;
							if (StringUtils.isEmpty(elementLabel.getAttribute("xml:lang"))) {
								zone3InstructionHeader.setFieldLabel(elementLabel.getTextContent());
								zone3InstructionHeader.setMimeType(elementLabel.getAttribute("mime-type"));
							} else {
								Zone3InstructionHeaderTranslaterDTO labelTranslater = new Zone3InstructionHeaderTranslaterDTO();
								labelTranslater.setTranslateLang(elementLabel.getAttribute("xml:lang"));
								zone3InstructionHeader.setMimeType(elementLabel.getAttribute("mime-type"));
								labelTranslater.setLabelName(elementLabel.getTextContent());
								zone3InstructionHeaderTranslatersList.add(labelTranslater);
							}
						}
					}
					zone3InstructionHeader
							.setZone3InstructionHeaderTranslatersList(zone3InstructionHeaderTranslatersList);
					zone3InstructionHeadersList.add(zone3InstructionHeader);
				}
			}

			NodeList n_instructionHeaderExternalCandidatesList = document
					.getElementsByTagName("instruction-header-external-candidate");
			for (int i = 0; i < n_instructionHeaderExternalCandidatesList.getLength(); i++) {
				Node node = n_instructionHeaderExternalCandidatesList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Zone3InstructionHeaderDTO zone3InstructionHeader = new Zone3InstructionHeaderDTO();
					zone3InstructionHeader.setFieldType("instruction-header-external-candidate");
					Element element = (Element) node;
					NodeList n_LabelsList = element.getElementsByTagName("label");
					List<Zone3InstructionHeaderTranslaterDTO> zone3InstructionHeaderTranslatersList = new ArrayList<Zone3InstructionHeaderTranslaterDTO>();
					for (int j = 0; j < n_LabelsList.getLength(); j++) {
						Node nodeLabel = n_LabelsList.item(j);
						if (nodeLabel.getNodeType() == Node.ELEMENT_NODE) {
							Element elementLabel = (Element) nodeLabel;
							if (StringUtils.isEmpty(elementLabel.getAttribute("xml:lang"))) {
								zone3InstructionHeader.setFieldLabel(elementLabel.getTextContent());
								zone3InstructionHeader.setMimeType(elementLabel.getAttribute("mime-type"));
							} else {
								Zone3InstructionHeaderTranslaterDTO labelTranslater = new Zone3InstructionHeaderTranslaterDTO();
								labelTranslater.setTranslateLang(elementLabel.getAttribute("xml:lang"));
								zone3InstructionHeader.setMimeType(elementLabel.getAttribute("mime-type"));
								labelTranslater.setLabelName(elementLabel.getTextContent());
								zone3InstructionHeaderTranslatersList.add(labelTranslater);
							}
						}
					}
					zone3InstructionHeader
							.setZone3InstructionHeaderTranslatersList(zone3InstructionHeaderTranslatersList);
					zone3InstructionHeadersList.add(zone3InstructionHeader);
				}
			}
			zone3Data.setZone3InstructionHeadersList(zone3InstructionHeadersList);
			/* Zone3 ends here */

			/* Zone4 starts here */
			NodeList n_operatorInstructionHeadersList = document.getElementsByTagName("operator-instruction-header");
			for (int i = 0; i < n_operatorInstructionHeadersList.getLength(); i++) {
				Node node = n_operatorInstructionHeadersList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Zone4OperatorInstructionHeaderDTO zone4OperatorInstructionHeader = new Zone4OperatorInstructionHeaderDTO();
					zone4OperatorInstructionHeader.setFieldType("operator-instruction-header");
					Element element = (Element) node;
					NodeList n_LabelsList = element.getElementsByTagName("label");
					List<Zone4OperatorInstructionHeaderTranslaterDTO> zone4OperatorInstructionHeaderTranslatersList = new ArrayList<Zone4OperatorInstructionHeaderTranslaterDTO>();
					for (int j = 0; j < n_LabelsList.getLength(); j++) {
						Node nodeLabel = n_LabelsList.item(j);
						if (nodeLabel.getNodeType() == Node.ELEMENT_NODE) {
							Element elementLabel = (Element) nodeLabel;
							if (StringUtils.isEmpty(elementLabel.getAttribute("xml:lang"))) {
								zone4OperatorInstructionHeader.setFieldLabel(elementLabel.getTextContent());
								zone4OperatorInstructionHeader.setMimeType(elementLabel.getAttribute("mime-type"));
							} else {
								Zone4OperatorInstructionHeaderTranslaterDTO labelTranslater = new Zone4OperatorInstructionHeaderTranslaterDTO();
								labelTranslater.setTranslateLang(elementLabel.getAttribute("xml:lang"));
								zone4OperatorInstructionHeader.setMimeType(elementLabel.getAttribute("mime-type"));
								labelTranslater.setLabelName(elementLabel.getTextContent());
								zone4OperatorInstructionHeaderTranslatersList.add(labelTranslater);
							}
						}
					}
					zone4OperatorInstructionHeader.setZone4OperatorInstructionHeaderTranslatersList(
							zone4OperatorInstructionHeaderTranslatersList);
					zone4OperatorInstructionHeadersList.add(zone4OperatorInstructionHeader);
				}
			}
			zone4Data.setZone4OperatorInstructionHeadersList(zone4OperatorInstructionHeadersList);
			/* Zone4 ends here */

			/* Zone5 starts here */
			NodeList n_FieldDefinitionsList = document.getElementsByTagName("field-definition");
			for (int i = 0; i < n_FieldDefinitionsList.getLength(); i++) {
				Node node = n_FieldDefinitionsList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Zone5FieldDefinitionDTO zone5FieldDefinition = new Zone5FieldDefinitionDTO();
					Element element = (Element) node;
					zone5FieldDefinition.setFieldId(element.getAttribute("id"));
					zone5FieldDefinition.setFieldType(element.getAttribute("type"));
					zone5FieldDefinition.setRequired(element.getAttribute("required"));
					zone5FieldDefinition.setCustom(element.getAttribute("custom"));
					zone5FieldDefinition.setAnonymize(element.getAttribute("anonymize"));
					zone5FieldDefinition.setObjectType(element.getAttribute("object-type"));
					zone5FieldDefinition.setSensitive(element.getAttribute("sensitive"));
					if (null != element.getElementsByTagName("field-description").item(0)) {
						zone5FieldDefinition.setFieldDescription(
								element.getElementsByTagName("field-description").item(0).getTextContent());
					}
					NodeList n_FieldLabelsList = element.getElementsByTagName("field-label");
					List<Zone5FieldDefinitionTranslaterDTO> zone5FieldDefinitionTranslatersList = new ArrayList<Zone5FieldDefinitionTranslaterDTO>();
					for (int j = 0; j < n_FieldLabelsList.getLength(); j++) {
						Node nodeLabel = n_FieldLabelsList.item(j);
						if (nodeLabel.getNodeType() == Node.ELEMENT_NODE) {
							Element elementLabel = (Element) nodeLabel;
							if (StringUtils.isEmpty(elementLabel.getAttribute("lang"))) {
								zone5FieldDefinition.setMimeType(elementLabel.getAttribute("mime-type"));
								zone5FieldDefinition.setFieldLabel(elementLabel.getTextContent());
							} else {
								Zone5FieldDefinitionTranslaterDTO labelTranslater = new Zone5FieldDefinitionTranslaterDTO();
								labelTranslater.setTranslateLang(elementLabel.getAttribute("lang"));
								zone5FieldDefinition.setMimeType(elementLabel.getAttribute("mime-type"));
								labelTranslater.setLabelName(elementLabel.getTextContent());
								zone5FieldDefinitionTranslatersList.add(labelTranslater);
							}
						}
					}
					if ("object".equalsIgnoreCase(zone5FieldDefinition.getFieldType())) {
						NodeList n_FieldObjectsList = element.getElementsByTagName("field-criteria");
						for (int k = 0; k < n_FieldObjectsList.getLength(); k++) {
							Node nodeFieldObject = n_FieldObjectsList.item(k);
							if (nodeFieldObject.getNodeType() == Node.ELEMENT_NODE) {
								Element elementFieldObject = (Element) nodeFieldObject;
								zone5FieldDefinition.setParentPickListIdOrObject(
										elementFieldObject.getAttribute("sourceFieldName"));
								zone5FieldDefinition
										.setPickListOrObject(elementFieldObject.getAttribute("destinationFieldValue"));
							}
						}
					} else if ("enum".equalsIgnoreCase(zone5FieldDefinition.getFieldType())) {
						NodeList n_FieldEnumsList = element.getElementsByTagName("enum-value");
						for (int k = 0; k < n_FieldEnumsList.getLength(); k++) {
							Node nodeFieldEnum = n_FieldEnumsList.item(k);
							if (nodeFieldEnum.getNodeType() == Node.ELEMENT_NODE) {
								EnumDTO enumObj = new EnumDTO();
								Element elementFieldEnum = (Element) nodeFieldEnum;
								enumObj.setEnumFieldId(zone5FieldDefinition.getFieldId());
								enumObj.setEnumFieldLabel(zone5FieldDefinition.getFieldLabel());
								enumObj.setEnumValue(elementFieldEnum.getAttribute("value"));

								NodeList n_EnumLabelsList = elementFieldEnum.getElementsByTagName("enum-label");
								List<EnumLabelTranslaterDTO> enumTranslatersList = new ArrayList<EnumLabelTranslaterDTO>();
								for (int j = 0; j < n_EnumLabelsList.getLength(); j++) {
									Node nodeLabel = n_EnumLabelsList.item(j);
									if (node.getNodeType() == Node.ELEMENT_NODE) {
										Element elementLabel = (Element) nodeLabel;
										if (StringUtils.isEmpty(elementLabel.getAttribute("lang"))) {
											enumObj.setEnumLabel(elementLabel.getTextContent());
										} else {
											EnumLabelTranslaterDTO labelTranslater = new EnumLabelTranslaterDTO();
											labelTranslater.setTranslateLang(elementLabel.getAttribute("lang"));
											labelTranslater.setEnumLabel(elementLabel.getTextContent());
											enumTranslatersList.add(labelTranslater);
										}
									}
								}
								enumObj.setEnumLabelTranslatersList(enumTranslatersList);
								enumValuesList.add(enumObj);
							}
						}
					} else if ("picklist".equalsIgnoreCase(zone5FieldDefinition.getFieldType())) {
						NodeList n_FieldPickListsList = element.getElementsByTagName("picklist-id");
						for (int k = 0; k < n_FieldPickListsList.getLength(); k++) {
							Node nodeFieldPickList = n_FieldPickListsList.item(k);
							if (nodeFieldPickList.getNodeType() == Node.ELEMENT_NODE) {
								Element elementFieldPickList = (Element) nodeFieldPickList;
								zone5FieldDefinition.setParentPickListIdOrObject(
										elementFieldPickList.getAttribute("parent-field-id"));
								zone5FieldDefinition.setPickListOrObject(elementFieldPickList.getTextContent());
							}
						}
					}
					zone5FieldDefinition.setZone5FieldDefinitionTranslatersList(zone5FieldDefinitionTranslatersList);
					zone5FieldDefinitionsList.add(zone5FieldDefinition);
				}
			}
			zone5Data.setZone5FieldDefinitionsList(zone5FieldDefinitionsList);
			/* Zone5 ends here */

			/* Zone6 starts here */
			NodeList n_instructionFootersList = document.getElementsByTagName("instruction-footer");
			for (int i = 0; i < n_instructionFootersList.getLength(); i++) {
				Node node = n_instructionFootersList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Zone6InstructionFooterDTO zone6InstructionFooter = new Zone6InstructionFooterDTO();
					zone6InstructionFooter.setFieldType("instruction-footer");
					Element element = (Element) node;
					NodeList n_LabelsList = element.getElementsByTagName("label");
					List<Zone6InstructionFooterTranslaterDTO> zone6InstructionFooterTranslatersList = new ArrayList<Zone6InstructionFooterTranslaterDTO>();
					for (int j = 0; j < n_LabelsList.getLength(); j++) {
						Node nodeLabel = n_LabelsList.item(j);
						if (nodeLabel.getNodeType() == Node.ELEMENT_NODE) {
							Element elementLabel = (Element) nodeLabel;
							if (StringUtils.isEmpty(elementLabel.getAttribute("xml:lang"))) {
								zone6InstructionFooter.setFieldLabel(elementLabel.getTextContent());
								zone6InstructionFooter.setMimeType(elementLabel.getAttribute("mime-type"));
							} else {
								Zone6InstructionFooterTranslaterDTO labelTranslater = new Zone6InstructionFooterTranslaterDTO();
								labelTranslater.setTranslateLang(elementLabel.getAttribute("xml:lang"));
								zone6InstructionFooter.setMimeType(elementLabel.getAttribute("mime-type"));
								labelTranslater.setLabelName(elementLabel.getTextContent());
								zone6InstructionFooterTranslatersList.add(labelTranslater);
							}
						}
					}
					zone6InstructionFooter
							.setZone6InstructionFooterTranslatersList(zone6InstructionFooterTranslatersList);
					zone6InstructionFootersList.add(zone6InstructionFooter);
				}
			}

			NodeList n_instructionFooterInternalCandidatesList = document
					.getElementsByTagName("instruction-footer-internal-candidate");
			for (int i = 0; i < n_instructionFooterInternalCandidatesList.getLength(); i++) {
				Node node = n_instructionFooterInternalCandidatesList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Zone6InstructionFooterDTO zone6InstructionFooter = new Zone6InstructionFooterDTO();
					zone6InstructionFooter.setFieldType("instruction-footer-internal-candidate");
					Element element = (Element) node;
					NodeList n_LabelsList = element.getElementsByTagName("label");
					List<Zone6InstructionFooterTranslaterDTO> zone6InstructionFooterTranslatersList = new ArrayList<Zone6InstructionFooterTranslaterDTO>();
					for (int j = 0; j < n_LabelsList.getLength(); j++) {
						Node nodeLabel = n_LabelsList.item(j);
						if (nodeLabel.getNodeType() == Node.ELEMENT_NODE) {
							Element elementLabel = (Element) nodeLabel;
							if (StringUtils.isEmpty(elementLabel.getAttribute("xml:lang"))) {
								zone6InstructionFooter.setFieldLabel(elementLabel.getTextContent());
								zone6InstructionFooter.setMimeType(elementLabel.getAttribute("mime-type"));
							} else {
								Zone6InstructionFooterTranslaterDTO labelTranslater = new Zone6InstructionFooterTranslaterDTO();
								labelTranslater.setTranslateLang(elementLabel.getAttribute("xml:lang"));
								zone6InstructionFooter.setMimeType(elementLabel.getAttribute("mime-type"));
								labelTranslater.setLabelName(elementLabel.getTextContent());
								zone6InstructionFooterTranslatersList.add(labelTranslater);
							}
						}
					}
					zone6InstructionFooter
							.setZone6InstructionFooterTranslatersList(zone6InstructionFooterTranslatersList);
					zone6InstructionFootersList.add(zone6InstructionFooter);
				}
			}

			NodeList n_instructionFooterExternalCandidatesList = document
					.getElementsByTagName("instruction-footer-external-candidate");
			for (int i = 0; i < n_instructionFooterExternalCandidatesList.getLength(); i++) {
				Node node = n_instructionFooterExternalCandidatesList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Zone6InstructionFooterDTO zone6InstructionFooter = new Zone6InstructionFooterDTO();
					zone6InstructionFooter.setFieldType("instruction-footer-external-candidate");
					Element element = (Element) node;
					NodeList n_LabelsList = element.getElementsByTagName("label");
					List<Zone6InstructionFooterTranslaterDTO> zone6InstructionFooterTranslatersList = new ArrayList<Zone6InstructionFooterTranslaterDTO>();
					for (int j = 0; j < n_LabelsList.getLength(); j++) {
						Node nodeLabel = n_LabelsList.item(j);
						if (nodeLabel.getNodeType() == Node.ELEMENT_NODE) {
							Element elementLabel = (Element) nodeLabel;
							if (StringUtils.isEmpty(elementLabel.getAttribute("xml:lang"))) {
								zone6InstructionFooter.setFieldLabel(elementLabel.getTextContent());
								zone6InstructionFooter.setMimeType(elementLabel.getAttribute("mime-type"));
							} else {
								Zone6InstructionFooterTranslaterDTO labelTranslater = new Zone6InstructionFooterTranslaterDTO();
								labelTranslater.setTranslateLang(elementLabel.getAttribute("xml:lang"));
								zone6InstructionFooter.setMimeType(elementLabel.getAttribute("mime-type"));
								labelTranslater.setLabelName(elementLabel.getTextContent());
								zone6InstructionFooterTranslatersList.add(labelTranslater);
							}
						}
					}
					zone6InstructionFooter
							.setZone6InstructionFooterTranslatersList(zone6InstructionFooterTranslatersList);
					zone6InstructionFootersList.add(zone6InstructionFooter);
				}
			}
			zone6Data.setZone6InstructionFootersList(zone6InstructionFootersList);
			/* Zone6 ends here */

			/* Zone7 starts here */
			NodeList n_operatorInstructionFootrsList = document.getElementsByTagName("operator-instruction-footer");
			for (int i = 0; i < n_operatorInstructionFootrsList.getLength(); i++) {
				Node node = n_operatorInstructionFootrsList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Zone7OperatorInstructionFooterDTO zone7OperatorInstructionFooter = new Zone7OperatorInstructionFooterDTO();
					zone7OperatorInstructionFooter.setFieldType("operator-instruction-footer");
					Element element = (Element) node;
					NodeList n_LabelsList = element.getElementsByTagName("label");
					List<Zone7OperatorInstructionFooterTranslaterDTO> zone7OperatorInstructionFooterTranslatersList = new ArrayList<Zone7OperatorInstructionFooterTranslaterDTO>();
					for (int j = 0; j < n_LabelsList.getLength(); j++) {
						Node nodeLabel = n_LabelsList.item(j);
						if (nodeLabel.getNodeType() == Node.ELEMENT_NODE) {
							Element elementLabel = (Element) nodeLabel;
							if (StringUtils.isEmpty(elementLabel.getAttribute("xml:lang"))) {
								zone7OperatorInstructionFooter.setFieldLabel(elementLabel.getTextContent());
								zone7OperatorInstructionFooter.setMimeType(elementLabel.getAttribute("mime-type"));
							} else {
								Zone7OperatorInstructionFooterTranslaterDTO labelTranslater = new Zone7OperatorInstructionFooterTranslaterDTO();
								labelTranslater.setTranslateLang(elementLabel.getAttribute("xml:lang"));
								zone7OperatorInstructionFooter.setMimeType(elementLabel.getAttribute("mime-type"));
								labelTranslater.setLabelName(elementLabel.getTextContent());
								zone7OperatorInstructionFooterTranslatersList.add(labelTranslater);
							}
						}
					}
					zone7OperatorInstructionFooter.setZone7OperatorInstructionFooterTranslatersList(
							zone7OperatorInstructionFooterTranslatersList);
					zone7OperatorInstructionFootersList.add(zone7OperatorInstructionFooter);
				}
			}
			zone7Data.setZone7OperatorInstructionFootersList(zone7OperatorInstructionFootersList);
			/* Zone7 ends here */

			/* Zone8 starts here */
			NodeList n_searchDisplayOptionsConfigsList = document.getElementsByTagName("search-display-options-config");
			for (int i = 0; i < n_searchDisplayOptionsConfigsList.getLength(); i++) {
				Node node = n_searchDisplayOptionsConfigsList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					NodeList n_categoriesList = element.getElementsByTagName("category");
					for (int j = 0; j < n_categoriesList.getLength(); j++) {
						Node nodeCategory = n_categoriesList.item(j);
						if (nodeCategory.getNodeType() == Node.ELEMENT_NODE) {
							Element elementCategory = (Element) nodeCategory;
							if ("tag".equalsIgnoreCase(elementCategory.getAttribute("id"))
									&& "tag".equalsIgnoreCase(elementCategory.getAttribute("name"))
									&& zone8SearchDisplayOptionsList.size() > 0) {
								zone8SearchDisplayOptionsList.get(zone8SearchDisplayOptionsList.size() - 1)
										.setTagsNeeded("Y");
							} else {
								Zone8SearchDisplayOptionDTO zone8SearchDisplayOption = new Zone8SearchDisplayOptionDTO();
								zone8SearchDisplayOption.setCategoryId(elementCategory.getAttribute("id"));
								zone8SearchDisplayOption.setCategoryName(elementCategory.getAttribute("name"));
								NodeList n_LabelsList = elementCategory.getElementsByTagName("label");
								List<TranslaterDTO> translatersList = new ArrayList<TranslaterDTO>();
								for (int k = 0; k < n_LabelsList.getLength(); k++) {
									Node nodeLabel = n_LabelsList.item(k);
									if (nodeLabel.getNodeType() == Node.ELEMENT_NODE) {
										Element elementLabel = (Element) nodeLabel;
										if (StringUtils.isEmpty(elementLabel.getAttribute("xml:lang"))) {
											zone8SearchDisplayOption.setLabel(elementLabel.getTextContent());
										} else {
											TranslaterDTO labelTranslater = new TranslaterDTO();
											labelTranslater.setTranslateLang(elementLabel.getAttribute("xml:lang"));
											labelTranslater.setTranslateLabel(elementLabel.getTextContent());
											translatersList.add(labelTranslater);
										}
									}
								}
								NodeList n_subCategoriesList = elementCategory.getElementsByTagName("column");
								List<Zone8SearchDisplayOptionSubCategoryDTO> zone8SearchDisplayOptionSubCategoryList = new ArrayList<Zone8SearchDisplayOptionSubCategoryDTO>();
								for (int k = 0; k < n_subCategoriesList.getLength(); k++) {
									Node nodeSubCategory = n_subCategoriesList.item(k);
									if (nodeSubCategory.getNodeType() == Node.ELEMENT_NODE) {
										Element elementSubCategory = (Element) nodeSubCategory;
										if (k == 0) {
											zone8SearchDisplayOption
													.setFieldRefId(elementSubCategory.getAttribute("field-ref"));
											zone8SearchDisplayOption.setBackgroundElementRef(
													elementSubCategory.getAttribute("bge-ref"));
											zone8SearchDisplayOption.setSelectByDefault(
													elementSubCategory.getAttribute("select-by-default"));
											zone8SearchDisplayOption
													.setGridOrder(elementSubCategory.getAttribute("gridOrder"));
										} else {
											Zone8SearchDisplayOptionSubCategoryDTO zone8SearchDisplayOptionSubCategory = new Zone8SearchDisplayOptionSubCategoryDTO();
											zone8SearchDisplayOptionSubCategory
													.setFieldRefId(elementSubCategory.getAttribute("field-ref"));
											zone8SearchDisplayOptionSubCategory.setBackgroundElementRef(
													elementSubCategory.getAttribute("bge-ref"));
											zone8SearchDisplayOptionSubCategory.setSelectByDefault(
													elementSubCategory.getAttribute("select-by-default"));
											zone8SearchDisplayOptionSubCategory
													.setGridOrder(elementSubCategory.getAttribute("gridOrder"));
											zone8SearchDisplayOptionSubCategoryList
													.add(zone8SearchDisplayOptionSubCategory);
										}
									}
								}
								zone8SearchDisplayOption.setTranslatersList(translatersList);
								zone8SearchDisplayOption.setZone8SearchDisplayOptionSubCategoryList(
										zone8SearchDisplayOptionSubCategoryList);
								zone8SearchDisplayOptionsList.add(zone8SearchDisplayOption);
							}
						}
					}
				}
			}
			zone8SearchDisplayOptionsList.forEach(searchDisplayOption -> {
				if (StringUtils.isEmpty(searchDisplayOption.getTagsNeeded())) {
					searchDisplayOption.setTagsNeeded("N");
				}
			});
			zone8Data.setZone8SearchDisplayOptionsList(zone8SearchDisplayOptionsList);
			/* Zone8 ends here */

			/* Zone9 starts here */
			NodeList n_candidateSummaryDisplayOptionsConfigsList = document
					.getElementsByTagName("candidate-summary-display-options-config");
			for (int i = 0; i < n_candidateSummaryDisplayOptionsConfigsList.getLength(); i++) {
				Node node = n_candidateSummaryDisplayOptionsConfigsList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					NodeList n_categoriesList = element.getElementsByTagName("category");
					for (int j = 0; j < n_categoriesList.getLength(); j++) {
						Node nodeCategory = n_categoriesList.item(j);
						if (nodeCategory.getNodeType() == Node.ELEMENT_NODE) {
							Element elementCategory = (Element) nodeCategory;
							Zone9SummaryDisplayOptionDTO zone9SummaryDisplayOption = new Zone9SummaryDisplayOptionDTO();
							zone9SummaryDisplayOption.setCategoryId(elementCategory.getAttribute("id"));
							zone9SummaryDisplayOption.setCategoryName(elementCategory.getAttribute("name"));
							NodeList n_LabelsList = elementCategory.getElementsByTagName("label");
							List<TranslaterDTO> translatersList = new ArrayList<TranslaterDTO>();
							for (int k = 0; k < n_LabelsList.getLength(); k++) {
								Node nodeLabel = n_LabelsList.item(k);
								if (nodeLabel.getNodeType() == Node.ELEMENT_NODE) {
									Element elementLabel = (Element) nodeLabel;
									if (StringUtils.isEmpty(elementLabel.getAttribute("xml:lang"))) {
										zone9SummaryDisplayOption.setLabel(elementLabel.getTextContent());
									} else {
										TranslaterDTO labelTranslater = new TranslaterDTO();
										labelTranslater.setTranslateLang(elementLabel.getAttribute("xml:lang"));
										labelTranslater.setTranslateLabel(elementLabel.getTextContent());
										translatersList.add(labelTranslater);
									}
								}
							}
							NodeList n_subCategoriesList = elementCategory.getElementsByTagName("column");
							List<Zone9SummaryDisplayOptionSubCategoryDTO> zone9SummaryDisplayOptionSubCategoryList = new ArrayList<Zone9SummaryDisplayOptionSubCategoryDTO>();
							for (int k = 0; k < n_subCategoriesList.getLength(); k++) {
								Node nodeSubCategory = n_subCategoriesList.item(k);
								if (nodeSubCategory.getNodeType() == Node.ELEMENT_NODE) {
									Element elementSubCategory = (Element) nodeSubCategory;
									if (k == 0) {
										zone9SummaryDisplayOption
												.setFieldRefId(elementSubCategory.getAttribute("field-ref"));
										zone9SummaryDisplayOption.setSelectByDefault(
												elementSubCategory.getAttribute("select-by-default"));
										zone9SummaryDisplayOption
												.setGridOrder(elementSubCategory.getAttribute("gridOrder"));
									} else {
										Zone9SummaryDisplayOptionSubCategoryDTO zone9SummaryDisplayOptionSubCategory = new Zone9SummaryDisplayOptionSubCategoryDTO();
										zone9SummaryDisplayOptionSubCategory
												.setFieldRefId(elementSubCategory.getAttribute("field-ref"));
										zone9SummaryDisplayOptionSubCategory.setSelectByDefault(
												elementSubCategory.getAttribute("select-by-default"));
										zone9SummaryDisplayOptionSubCategory
												.setGridOrder(elementSubCategory.getAttribute("gridOrder"));
										zone9SummaryDisplayOptionSubCategoryList
												.add(zone9SummaryDisplayOptionSubCategory);
									}
								}
							}
							zone9SummaryDisplayOption.setTranslatersList(translatersList);
							zone9SummaryDisplayOption.setZone9SummaryDisplayOptionSubCategoryList(
									zone9SummaryDisplayOptionSubCategoryList);
							zone9SummaryDisplayOptionsList.add(zone9SummaryDisplayOption);
						}
					}
				}
			}
			zone9Data.setZone9SummaryDisplayOptionsList(zone9SummaryDisplayOptionsList);
			/* Zone9 ends here */

			/* Zone10 starts here */
			NodeList n_backgroundElementsList = document.getElementsByTagName("background-element");
			for (int i = 0; i < n_backgroundElementsList.getLength(); i++) {
				Node node = n_backgroundElementsList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					Zone10BackgroundElementDTO zone10BackgroundElement = new Zone10BackgroundElementDTO();
					zone10BackgroundElement.setFieldId(element.getAttribute("id"));
					zone10BackgroundElement.setRequired(element.getAttribute("required"));
					zone10BackgroundElement.setEmployeeTypeId(element.getAttribute("type-id"));
					zone10BackgroundElement.setFieldType("Background Element");
					NodeList n_LabelsList = element.getElementsByTagName("label");
					List<Zone10BackgroundElementTranslaterDTO> translatersList = new ArrayList<Zone10BackgroundElementTranslaterDTO>();
					for (int j = 0; j < n_LabelsList.getLength(); j++) {
						Node nodeLabel = n_LabelsList.item(j);
						if (nodeLabel.getNodeType() == Node.ELEMENT_NODE
								&& nodeLabel.getParentNode().toString().contains("background-element")) {
							Element elementLabel = (Element) nodeLabel;
							if (StringUtils.isEmpty(elementLabel.getAttribute("xml:lang"))) {
								zone10BackgroundElement.setFieldlabel(elementLabel.getTextContent());
							} else {
								Zone10BackgroundElementTranslaterDTO labelTranslater = new Zone10BackgroundElementTranslaterDTO();
								labelTranslater.setTranslateLang(elementLabel.getAttribute("xml:lang"));
								labelTranslater.setLabelName(elementLabel.getTextContent());
								translatersList.add(labelTranslater);
							}
						}
					}
					NodeList n_dataFieldsList = element.getElementsByTagName("data-field");
					List<Zone10BackgroundElementDataFieldDTO> zone10BackgroundElementDataFieldsList = new ArrayList<Zone10BackgroundElementDataFieldDTO>();
					for (int j = 0; j < n_dataFieldsList.getLength(); j++) {
						Node nodeDataField = n_dataFieldsList.item(j);
						if (nodeDataField.getNodeType() == Node.ELEMENT_NODE) {
							Element elementDataField = (Element) nodeDataField;
							Zone10BackgroundElementDataFieldDTO zone10BackgroundElementDataField = new Zone10BackgroundElementDataFieldDTO();
							zone10BackgroundElementDataField.setFieldId(elementDataField.getAttribute("id"));
							String fieldName = elementDataField.getAttribute("field-name");
							if (StringUtils.isNotEmpty(fieldName)) {
								if (fieldName.contains("vfld")) {
									zone10BackgroundElementDataField.setFieldType("bg-alphanumberic");
								} else if (fieldName.contains("ffld")) {
									zone10BackgroundElementDataField.setFieldType("bg-decimal / boolean");
								} else if (fieldName.contains("dfld")) {
									zone10BackgroundElementDataField.setFieldType("bg-date");
								} else if (fieldName.contains("ifld")) {
									zone10BackgroundElementDataField.setFieldType("bg-integer");
								} else {
									zone10BackgroundElementDataField
											.setFieldType(elementDataField.getAttribute("field-name"));
								}
							}
							zone10BackgroundElementDataField.setAnonymize(elementDataField.getAttribute("anonymize"));
							zone10BackgroundElementDataField.setRequired(elementDataField.getAttribute("required"));
							zone10BackgroundElementDataField.setMaxLength(elementDataField.getAttribute("max-length"));
							Node nodePickList = elementDataField.getElementsByTagName("picklist-id").item(0);
							if (null != nodePickList) {
								Element elementPickList = (Element) nodePickList;
								zone10BackgroundElementDataField.setPickListObject(elementPickList.getTextContent());
								zone10BackgroundElementDataField
										.setParentPickListIdOrObject(elementPickList.getAttribute("parent-field-id"));
							}
							NodeList n_dataFieldLabelsList = elementDataField.getElementsByTagName("label");
							List<Zone10BackgroundElementDataFieldTranslaterDTO> dataFieldTranslatersList = new ArrayList<Zone10BackgroundElementDataFieldTranslaterDTO>();
							for (int k = 0; k < n_dataFieldLabelsList.getLength(); k++) {
								Node nodeLabel = n_dataFieldLabelsList.item(k);
								if (nodeLabel.getNodeType() == Node.ELEMENT_NODE) {
									Element elementLabel = (Element) nodeLabel;
									if (StringUtils.isEmpty(elementLabel.getAttribute("xml:lang"))) {
										zone10BackgroundElementDataField.setFieldlabel(elementLabel.getTextContent());
									} else {
										Zone10BackgroundElementDataFieldTranslaterDTO labelTranslater = new Zone10BackgroundElementDataFieldTranslaterDTO();
										labelTranslater.setTranslateLang(elementLabel.getAttribute("xml:lang"));
										labelTranslater.setLabelName(elementLabel.getTextContent());
										dataFieldTranslatersList.add(labelTranslater);
									}
								}
							}
							zone10BackgroundElementDataField
									.setZone10BackgroundElementDataFieldTranslatersList(dataFieldTranslatersList);
							zone10BackgroundElementDataFieldsList.add(zone10BackgroundElementDataField);
						}
					}
					zone10BackgroundElement.setZone10BackgroundElementTranslatersList(translatersList);
					zone10BackgroundElement
							.setZone10BackgroundElementDataFieldsList(zone10BackgroundElementDataFieldsList);
					zone10BackgroundElementsList.add(zone10BackgroundElement);
				}
			}
			zone10Data.setZone10BackgroundElementsList(zone10BackgroundElementsList);
			/* Zone10 ends here */

			/* Zone11 starts here */
			NodeList n_smMappingsList = document.getElementsByTagName("sm-mapping");
			for (int i = 0; i < n_smMappingsList.getLength(); i++) {
				Node node = n_smMappingsList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					Zone11SmMappingDTO zone11SmMapping = new Zone11SmMappingDTO();
					zone11SmMapping.setFieldId(element.getAttribute("field-id"));
					zone11SmMapping.setEmployeeProfileFieldId(element.getAttribute("map-to"));
					zone11SmMappingsList.add(zone11SmMapping);
				}
			}
			zone11Data.setZone11SmMappingsList(zone11SmMappingsList);
			/* Zone11 ends here */

			/* Zone12 starts here */
			NodeList n_fieldPermissionsList = document.getElementsByTagName("field-permission");
			for (int i = 0; i < n_fieldPermissionsList.getLength(); i++) {
				Node node = n_fieldPermissionsList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					Zone12FieldPermissionDTO zone12FieldPermission = new Zone12FieldPermissionDTO();
					if (null != element.getElementsByTagName("country").item(0))
						zone12FieldPermission
								.setCountry(element.getElementsByTagName("country").item(0).getTextContent());
					if (null != element.getElementsByTagName("role-name").item(0))
						zone12FieldPermission
								.setRoleName(element.getElementsByTagName("role-name").item(0).getTextContent());
					if (null != element.getElementsByTagName("source").item(0))
						zone12FieldPermission
								.setSource(element.getElementsByTagName("source").item(0).getTextContent());
					List<FieldPermissionDTO> fieldPermissionsList = new ArrayList<FieldPermissionDTO>();
					NodeList n_fieldsList = element.getElementsByTagName("field");
					for (int j = 0; j < n_fieldsList.getLength(); j++) {
						Node nodeField = n_fieldsList.item(j);
						if (nodeField.getNodeType() == Node.ELEMENT_NODE) {
							Element elementField = (Element) nodeField;
							FieldPermissionDTO fieldPermission = new FieldPermissionDTO();
							fieldPermission.setFieldId(elementField.getAttribute("refid"));
							fieldPermission
									.setPermissionType(element.getAttribute("type").substring(0, 1).toUpperCase());
							fieldPermissionsList.add(fieldPermission);
						}
					}
					NodeList n_bgElementsList = element.getElementsByTagName("bg-element");
					for (int j = 0; j < n_bgElementsList.getLength(); j++) {
						Node nodeBgElement = n_bgElementsList.item(j);
						if (nodeBgElement.getNodeType() == Node.ELEMENT_NODE) {
							Element elementBgElement = (Element) nodeBgElement;
							FieldPermissionDTO fieldPermission = new FieldPermissionDTO();
							if ("*".equalsIgnoreCase(elementBgElement.getAttribute("data-field-id"))) {
								fieldPermission.setFieldId(elementBgElement.getAttribute("refid"));
							} else {
								fieldPermission.setFieldId(elementBgElement.getAttribute("data-field-id"));
							}
							fieldPermission.setBeFieldId(elementBgElement.getAttribute("refid"));
							fieldPermission
									.setPermissionType(element.getAttribute("type").substring(0, 1).toUpperCase());
							fieldPermissionsList.add(fieldPermission);
						}
					}
					zone12FieldPermission.setFieldPermissionsList(fieldPermissionsList);
					zone12FieldPermissionsList.add(zone12FieldPermission);
				}
			}
			zone12Data.setZone12FieldPermissionsList(zone12FieldPermissionsList);
			/* Zone12 ends here */

		} catch (DOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		zone11Data.getZone11SmMappingsList().forEach(smMappingObj -> {
			zone5Data.getZone5FieldDefinitionsList().forEach(fieldDef -> {
				if (smMappingObj.getFieldId().equalsIgnoreCase(fieldDef.getFieldId())) {
					fieldDef.setEmployeeProfileFieldId(smMappingObj.getEmployeeProfileFieldId());
				}
			});
		});
		zone11Data.getZone11SmMappingsList().forEach(smMappingObj -> {
			zone10Data.getZone10BackgroundElementsList().forEach(BEElement -> {
				if (smMappingObj.getFieldId().equalsIgnoreCase(BEElement.getFieldId())) {
					BEElement.setEmployeeProfileFieldId(smMappingObj.getEmployeeProfileFieldId());
				}
			});
		});
		candidateProfileTemplateXmlData.setZone2(zone2Data);
		candidateProfileTemplateXmlData.setZone3(zone3Data);
		candidateProfileTemplateXmlData.setZone4(zone4Data);
		candidateProfileTemplateXmlData.setZone5(zone5Data);
		candidateProfileTemplateXmlData.setZone6(zone6Data);
		candidateProfileTemplateXmlData.setZone7(zone7Data);
		candidateProfileTemplateXmlData.setZone8(zone8Data);
		candidateProfileTemplateXmlData.setZone9(zone9Data);
		candidateProfileTemplateXmlData.setZone10(zone10Data);
		candidateProfileTemplateXmlData.setZone11(zone11Data);
		candidateProfileTemplateXmlData.setZone12(zone12Data);
		candidateProfileTemplateXmlData.setEnumValuesList(enumValuesList);
		return candidateProfileTemplateXmlData;
	}

	public static String buildExcelFile(CandidateProfileTemplateDTO candidateProfileTemplateXmlData, String fileName) {
		try {
			Resource resource = new ClassPathResource("classpath:Candidate_Profile_RPA_Template.xlsx");
			InputStream inputStream = resource.getInputStream();
			//File sourceFile = new File("Candidate_Profile_RPA_Template.xlsx");
			File newFile = new File(fileName + ".xlsx");
			FileUtils.copyInputStreamToFile(inputStream, newFile);
			//FileUtils.copyFile(sourceFile, destinationFile);
			XSSFWorkbook workBook;
			File destinationFile = new File(fileName + ".xlsx");
			FileInputStream destinationFileStream = new FileInputStream(destinationFile);
			workBook = new XSSFWorkbook(destinationFileStream);
			XSSFSheet candidateProfileSheet = workBook.getSheet("Candidate_Profile");
			XSSFSheet sheetSearch_Summery = workBook.getSheet("Search_N_Summary_Display_Option");
			Set<String> translaterLangsSet = new HashSet<String>();
			Set<String> translaterSSLangsSet = new HashSet<String>();
			List<String> roleNamesList = new ArrayList<String>();
			List<String> sourcesList = new ArrayList<String>();
			List<String> countriesList = new ArrayList<String>();
			Set<String> permissionsGroupSet = new HashSet<String>();
			List<String> permissionsGroupList = new ArrayList<String>();

			HashMap<String, String> validationsLangsMap = CommonTemplateUtils.getValidationsLangsMap(workBook,
					"Validations");
			List<String> validationsLangsList = new ArrayList<String>();
			validationsLangsMap.forEach((key, value) -> {
				if (value.contains("_")) {
					validationsLangsList.add(value);
				}
			});
			List<String> validationsFieldTypesList = CommonTemplateUtils.getValidationsFieldTypesList(workBook,
					"Validations", 1);
			CommonTemplateUtils.buildWorkBookEnumSheet(candidateProfileTemplateXmlData.getZone2().getTemplateName(),
					candidateProfileTemplateXmlData.getZone2().getTemplateDescription(),
					candidateProfileTemplateXmlData.getEnumValuesList(), "Candidate_Profile_Enum_Values", workBook,
					validationsLangsMap, validationsLangsList);

			if (null != sheetSearch_Summery) {
				sheetSearch_Summery.getRow(0).getCell(4)
						.setCellValue(candidateProfileTemplateXmlData.getZone2().getTemplateName());
				sheetSearch_Summery.getRow(1).getCell(4)
						.setCellValue(candidateProfileTemplateXmlData.getZone2().getTemplateDescription());
				candidateProfileTemplateXmlData.getZone8().getZone8SearchDisplayOptionsList()
						.forEach(searchDisplayOption -> {
							searchDisplayOption.getTranslatersList().forEach(translaterObj -> {
								translaterSSLangsSet.add(translaterObj.getTranslateLang());
							});
						});
				candidateProfileTemplateXmlData.getZone9().getZone9SummaryDisplayOptionsList()
						.forEach(summaryDisplayOption -> {
							summaryDisplayOption.getTranslatersList().forEach(translaterObj -> {
								translaterSSLangsSet.add(translaterObj.getTranslateLang());
							});
						});
				List<String> translaterSSLangsList = new ArrayList<>(translaterSSLangsSet);
				for (int i = 0; i < translaterSSLangsList.size(); i++) {
					CommonTemplateUtils.createColumnTransLang(workBook, sheetSearch_Summery, (14 + i), (15 + i), 3,
							validationsLangsMap, translaterSSLangsList, i);
					CommonTemplateUtils.addDataValidation255CharsAbove(workBook, sheetSearch_Summery, 3, 3, (14 + i),
							(14 + i), validationsLangsList.toArray(new String[0]), "Validations", "!$A$2:$A$47");
				}

				int startRowIndex = 4;
				for (int i = 0; i < candidateProfileTemplateXmlData.getZone8().getZone8SearchDisplayOptionsList()
						.size(); i++) {
					if (i > 0) {
						startRowIndex = startRowIndex
								+ candidateProfileTemplateXmlData.getZone8().getZone8SearchDisplayOptionsList()
										.get(i - 1).getZone8SearchDisplayOptionSubCategoryList().size();
					}
					CommonTemplateUtils.createRow(workBook, sheetSearch_Summery, startRowIndex + i,
							(startRowIndex + 1) + i);
					fillRowDataSearchDisplayOption(sheetSearch_Summery, sheetSearch_Summery.getRow(startRowIndex + i),
							candidateProfileTemplateXmlData.getZone8().getZone8SearchDisplayOptionsList().get(i),
							translaterSSLangsList, workBook);
				}
				startRowIndex = startRowIndex + candidateProfileTemplateXmlData.getZone8()
						.getZone8SearchDisplayOptionsList()
						.get(candidateProfileTemplateXmlData.getZone8().getZone8SearchDisplayOptionsList().size() - 1)
						.getZone8SearchDisplayOptionSubCategoryList().size() + 7;
				for (int i = 0; i < candidateProfileTemplateXmlData.getZone9().getZone9SummaryDisplayOptionsList()
						.size(); i++) {
					if (i > 0) {
						startRowIndex = startRowIndex
								+ candidateProfileTemplateXmlData.getZone9().getZone9SummaryDisplayOptionsList()
										.get(i - 1).getZone9SummaryDisplayOptionSubCategoryList().size();
					}
					CommonTemplateUtils.createRow(workBook, sheetSearch_Summery, startRowIndex + i,
							(startRowIndex + 1) + i);
					fillRowDataSummaryDisplayOption(sheetSearch_Summery, sheetSearch_Summery.getRow(startRowIndex + i),
							candidateProfileTemplateXmlData.getZone9().getZone9SummaryDisplayOptionsList().get(i),
							translaterSSLangsList, workBook);
				}
			}

			if (null != candidateProfileSheet) {
				candidateProfileTemplateXmlData.getZone12().getZone12FieldPermissionsList()
						.forEach(fieldPermissionObj -> {
							if (permissionsGroupSet.add(fieldPermissionObj.getCountry()
									+ fieldPermissionObj.getRoleName() + fieldPermissionObj.getSource())) {
								permissionsGroupList.add(fieldPermissionObj.getCountry()
										+ fieldPermissionObj.getRoleName() + fieldPermissionObj.getSource());
								countriesList.add(fieldPermissionObj.getCountry());
								roleNamesList.add(fieldPermissionObj.getRoleName());
								sourcesList.add(fieldPermissionObj.getSource());
							}
						});
				for (int i = 0; i < permissionsGroupSet.size(); i++) {
					CommonTemplateUtils.createColumnFieldPermission(workBook, candidateProfileSheet, 24 + i, 25 + i, 1,
							roleNamesList, countriesList, sourcesList, i);
				}

				candidateProfileTemplateXmlData.getZone2().getZone2TemplateNameTranslatersList()
						.forEach(translaterObj -> {
							translaterLangsSet.add(translaterObj.getTranslateLang());
						});
				candidateProfileTemplateXmlData.getZone2().getZone2TemplateDescriptionTranslatersList()
						.forEach(translaterObj -> {
							translaterLangsSet.add(translaterObj.getTranslateLang());
						});
				candidateProfileTemplateXmlData.getZone3().getZone3InstructionHeadersList()
						.forEach(instructionHeaderObj -> {
							instructionHeaderObj.getZone3InstructionHeaderTranslatersList().forEach(translaterObj -> {
								translaterLangsSet.add(translaterObj.getTranslateLang());
							});
						});
				candidateProfileTemplateXmlData.getZone4().getZone4OperatorInstructionHeadersList()
						.forEach(operatorInstructionHeaderObj -> {
							operatorInstructionHeaderObj.getZone4OperatorInstructionHeaderTranslatersList()
									.forEach(translaterObj -> {
										translaterLangsSet.add(translaterObj.getTranslateLang());
									});
						});
				candidateProfileTemplateXmlData.getZone5().getZone5FieldDefinitionsList().forEach(fieldDefObj -> {
					fieldDefObj.getZone5FieldDefinitionTranslatersList().forEach(translaterObj -> {
						translaterLangsSet.add(translaterObj.getTranslateLang());
					});
				});
				candidateProfileTemplateXmlData.getZone6().getZone6InstructionFootersList()
						.forEach(instructionFooterObj -> {
							instructionFooterObj.getZone6InstructionFooterTranslatersList().forEach(translaterObj -> {
								translaterLangsSet.add(translaterObj.getTranslateLang());
							});
						});
				candidateProfileTemplateXmlData.getZone7().getZone7OperatorInstructionFootersList()
						.forEach(operatorInstructionFooterObj -> {
							operatorInstructionFooterObj.getZone7OperatorInstructionFooterTranslatersList()
									.forEach(translaterObj -> {
										translaterLangsSet.add(translaterObj.getTranslateLang());
									});
						});
				candidateProfileTemplateXmlData.getZone10().getZone10BackgroundElementsList().forEach(beElementObj -> {
					beElementObj.getZone10BackgroundElementTranslatersList().forEach(translaterObj -> {
						translaterLangsSet.add(translaterObj.getTranslateLang());
					});
					beElementObj.getZone10BackgroundElementDataFieldsList().forEach(dataFieldObj -> {
						dataFieldObj.getZone10BackgroundElementDataFieldTranslatersList().forEach(translaterObj -> {
							translaterLangsSet.add(translaterObj.getTranslateLang());
						});
					});
				});

				List<String> translaterLangsList = new ArrayList<>(translaterLangsSet);
				int roleNamesListSize = roleNamesList.size();
				for (int i = 0; i < translaterLangsList.size(); i++) {
					CommonTemplateUtils.createColumnTransLang(workBook, candidateProfileSheet,
							(28 + roleNamesListSize + i), (29 + roleNamesListSize + i), 4, validationsLangsMap,
							translaterLangsList, i);
					CommonTemplateUtils.addDataValidation255CharsAbove(workBook, candidateProfileSheet, 4, 4,
							(28 + roleNamesListSize + i), (28 + roleNamesListSize + i),
							validationsLangsList.toArray(new String[0]), "Validations", "!$A$2:$A$47");
				}

				int startRowIndex = 5;
				for (int i = 0; i < candidateProfileTemplateXmlData.getZone3().getZone3InstructionHeadersList()
						.size(); i++) {
					CommonTemplateUtils.createRow(workBook, candidateProfileSheet, startRowIndex + i,
							(startRowIndex + 1) + i);
					fillRowDataInstructionHeader(candidateProfileSheet, candidateProfileSheet.getRow(startRowIndex + i),
							candidateProfileTemplateXmlData.getZone3().getZone3InstructionHeadersList().get(i),
							translaterLangsList, validationsFieldTypesList, workBook, roleNamesList);
				}
				startRowIndex = startRowIndex
						+ candidateProfileTemplateXmlData.getZone3().getZone3InstructionHeadersList().size();
				for (int i = 0; i < candidateProfileTemplateXmlData.getZone4().getZone4OperatorInstructionHeadersList()
						.size(); i++) {
					CommonTemplateUtils.createRow(workBook, candidateProfileSheet, startRowIndex + i,
							(startRowIndex + 1) + i);
					fillRowDataOperatorInstructionHeader(candidateProfileSheet,
							candidateProfileSheet.getRow(startRowIndex + i),
							candidateProfileTemplateXmlData.getZone4().getZone4OperatorInstructionHeadersList().get(i),
							translaterLangsList, validationsFieldTypesList, workBook, roleNamesList);
				}
				startRowIndex = startRowIndex
						+ candidateProfileTemplateXmlData.getZone4().getZone4OperatorInstructionHeadersList().size();
				for (int i = 0; i < candidateProfileTemplateXmlData.getZone6().getZone6InstructionFootersList()
						.size(); i++) {
					CommonTemplateUtils.createRow(workBook, candidateProfileSheet, startRowIndex + i,
							(startRowIndex + 1) + i);
					fillRowDataInstructionFooter(candidateProfileSheet, candidateProfileSheet.getRow(startRowIndex + i),
							candidateProfileTemplateXmlData.getZone6().getZone6InstructionFootersList().get(i),
							translaterLangsList, validationsFieldTypesList, workBook, roleNamesList);
				}
				startRowIndex = startRowIndex
						+ candidateProfileTemplateXmlData.getZone6().getZone6InstructionFootersList().size();
				for (int i = 0; i < candidateProfileTemplateXmlData.getZone7().getZone7OperatorInstructionFootersList()
						.size(); i++) {
					CommonTemplateUtils.createRow(workBook, candidateProfileSheet, startRowIndex + i,
							(startRowIndex + 1) + i);
					fillRowDataOperatorInstructionFooter(candidateProfileSheet,
							candidateProfileSheet.getRow(startRowIndex + i),
							candidateProfileTemplateXmlData.getZone7().getZone7OperatorInstructionFootersList().get(i),
							translaterLangsList, validationsFieldTypesList, workBook, roleNamesList);
				}
				startRowIndex = startRowIndex
						+ candidateProfileTemplateXmlData.getZone7().getZone7OperatorInstructionFootersList().size()
						+ 3;
				for (int i = 0; i < candidateProfileTemplateXmlData.getZone5().getZone5FieldDefinitionsList()
						.size(); i++) {

					CommonTemplateUtils.createRow(workBook, candidateProfileSheet, startRowIndex + i,
							(startRowIndex + 1) + i);
					fillRowDataFieldDefenition(candidateProfileSheet, candidateProfileSheet.getRow(startRowIndex + i),
							candidateProfileTemplateXmlData.getZone5().getZone5FieldDefinitionsList().get(i),
							translaterLangsList, validationsFieldTypesList, workBook, roleNamesList,
							candidateProfileTemplateXmlData.getZone12().getZone12FieldPermissionsList(),
							permissionsGroupList);
				}
				startRowIndex = startRowIndex
						+ candidateProfileTemplateXmlData.getZone5().getZone5FieldDefinitionsList().size() + 3;
				for (int i = 0; i < candidateProfileTemplateXmlData.getZone10().getZone10BackgroundElementsList()
						.size(); i++) {
					if (i > 0) {
						startRowIndex = startRowIndex
								+ candidateProfileTemplateXmlData.getZone10().getZone10BackgroundElementsList()
										.get(i - 1).getZone10BackgroundElementDataFieldsList().size();
					}
					CommonTemplateUtils.createRow(workBook, candidateProfileSheet, startRowIndex + i,
							(startRowIndex + 1) + i);
					fillRowDataBackgroundElement(candidateProfileSheet, candidateProfileSheet.getRow(startRowIndex + i),
							candidateProfileTemplateXmlData.getZone10().getZone10BackgroundElementsList().get(i),
							translaterLangsList, validationsFieldTypesList, workBook, roleNamesList,
							candidateProfileTemplateXmlData.getZone12().getZone12FieldPermissionsList(),
							permissionsGroupList);
				}
				candidateProfileSheet.getRow(1).getCell(4)
						.setCellValue(candidateProfileTemplateXmlData.getZone2().getTemplateName());
				candidateProfileTemplateXmlData.getZone2().getZone2TemplateNameTranslatersList()
						.forEach(translaterObj -> {
							candidateProfileSheet.getRow(1)
									.getCell(28 + roleNamesListSize
											+ translaterLangsList.indexOf(translaterObj.getTranslateLang()))
									.setCellValue(translaterObj.getTemplateName());
						});
				candidateProfileSheet.getRow(2).getCell(4)
						.setCellValue(candidateProfileTemplateXmlData.getZone2().getTemplateDescription());
				candidateProfileTemplateXmlData.getZone2().getZone2TemplateDescriptionTranslatersList()
						.forEach(translaterObj -> {
							candidateProfileSheet.getRow(2)
									.getCell(28 + roleNamesListSize
											+ translaterLangsList.indexOf(translaterObj.getTranslateLang()))
									.setCellValue(translaterObj.getTemplateDescription());
						});
			}
			destinationFileStream.close();
			// Crating output stream and writing the updated workbook
			FileOutputStream os = new FileOutputStream(destinationFile);
			workBook.write(os);
			// Close the workbook and output stream
			workBook.close();
			os.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileName + ".xlsx";
	}

	private static void fillRowDataInstructionHeader(XSSFSheet workSheet, XSSFRow row,
			Zone3InstructionHeaderDTO zone3InstructionHeaderDTO, List<String> translaterLangsList,
			List<String> validationsFieldTypesList, Workbook workBook, List<String> roleNamesList) {
		row.getCell(4).setCellValue(zone3InstructionHeaderDTO.getFieldLabel());
		row.getCell(5).setCellValue(zone3InstructionHeaderDTO.getFieldType());
		CommonTemplateUtils.addDataValidation255CharsAbove(workBook, workSheet, row.getRowNum(), row.getRowNum(), 5, 5,
				validationsFieldTypesList.toArray(new String[0]), "Validations", "!$B$2:$B$31");
		row.getCell(6).setCellValue(zone3InstructionHeaderDTO.getMimeType());
		CommonTemplateUtils.addDataValidation(workSheet, row.getRowNum(), row.getRowNum(), 6, 6,
				new String[] { "text-plain", "text-html" });
		int roleNamesListSize = roleNamesList.size();
		zone3InstructionHeaderDTO.getZone3InstructionHeaderTranslatersList().forEach(translaterObj -> {
			row.getCell(28 + roleNamesListSize + translaterLangsList.indexOf(translaterObj.getTranslateLang()))
					.setCellValue(translaterObj.getLabelName());
			workSheet.autoSizeColumn(
					28 + roleNamesListSize + translaterLangsList.indexOf(translaterObj.getTranslateLang()));
		});
	}

	private static void fillRowDataOperatorInstructionHeader(XSSFSheet workSheet, XSSFRow row,
			Zone4OperatorInstructionHeaderDTO zone4OperatorInstructionHeaderDTO, List<String> translaterLangsList,
			List<String> validationsFieldTypesList, XSSFWorkbook workBook, List<String> roleNamesList) {
		row.getCell(4).setCellValue(zone4OperatorInstructionHeaderDTO.getFieldLabel());
		row.getCell(5).setCellValue(zone4OperatorInstructionHeaderDTO.getFieldType());
		CommonTemplateUtils.addDataValidation255CharsAbove(workBook, workSheet, row.getRowNum(), row.getRowNum(), 5, 5,
				validationsFieldTypesList.toArray(new String[0]), "Validations", "!$B$2:$B$31");
		row.getCell(6).setCellValue(zone4OperatorInstructionHeaderDTO.getMimeType());
		CommonTemplateUtils.addDataValidation(workSheet, row.getRowNum(), row.getRowNum(), 6, 6,
				new String[] { "text-plain", "text-html" });
		int roleNamesListSize = roleNamesList.size();
		zone4OperatorInstructionHeaderDTO.getZone4OperatorInstructionHeaderTranslatersList().forEach(translaterObj -> {
			row.getCell(28 + roleNamesListSize + translaterLangsList.indexOf(translaterObj.getTranslateLang()))
					.setCellValue(translaterObj.getLabelName());
			workSheet.autoSizeColumn(
					28 + roleNamesListSize + translaterLangsList.indexOf(translaterObj.getTranslateLang()));
		});
	}

	private static void fillRowDataInstructionFooter(XSSFSheet workSheet, XSSFRow row,
			Zone6InstructionFooterDTO zone6InstructionFooterDTO, List<String> translaterLangsList,
			List<String> validationsFieldTypesList, Workbook workBook, List<String> roleNamesList) {
		row.getCell(4).setCellValue(zone6InstructionFooterDTO.getFieldLabel());
		row.getCell(5).setCellValue(zone6InstructionFooterDTO.getFieldType());
		CommonTemplateUtils.addDataValidation255CharsAbove(workBook, workSheet, row.getRowNum(), row.getRowNum(), 5, 5,
				validationsFieldTypesList.toArray(new String[0]), "Validations", "!$B$2:$B$31");
		row.getCell(6).setCellValue(zone6InstructionFooterDTO.getMimeType());
		CommonTemplateUtils.addDataValidation(workSheet, row.getRowNum(), row.getRowNum(), 6, 6,
				new String[] { "text-plain", "text-html" });
		int roleNamesListSize = roleNamesList.size();
		zone6InstructionFooterDTO.getZone6InstructionFooterTranslatersList().forEach(translaterObj -> {
			row.getCell(28 + roleNamesListSize + translaterLangsList.indexOf(translaterObj.getTranslateLang()))
					.setCellValue(translaterObj.getLabelName());
			workSheet.autoSizeColumn(
					28 + roleNamesListSize + translaterLangsList.indexOf(translaterObj.getTranslateLang()));
		});
	}

	private static void fillRowDataOperatorInstructionFooter(XSSFSheet workSheet, XSSFRow row,
			Zone7OperatorInstructionFooterDTO zone7OperatorInstructionFooterDTO, List<String> translaterLangsList,
			List<String> validationsFieldTypesList, XSSFWorkbook workBook, List<String> roleNamesList) {
		row.getCell(4).setCellValue(zone7OperatorInstructionFooterDTO.getFieldLabel());
		row.getCell(5).setCellValue(zone7OperatorInstructionFooterDTO.getFieldType());
		CommonTemplateUtils.addDataValidation255CharsAbove(workBook, workSheet, row.getRowNum(), row.getRowNum(), 5, 5,
				validationsFieldTypesList.toArray(new String[0]), "Validations", "!$B$2:$B$31");
		row.getCell(6).setCellValue(zone7OperatorInstructionFooterDTO.getMimeType());
		CommonTemplateUtils.addDataValidation(workSheet, row.getRowNum(), row.getRowNum(), 6, 6,
				new String[] { "text-plain", "text-html" });
		int roleNamesListSize = roleNamesList.size();
		zone7OperatorInstructionFooterDTO.getZone7OperatorInstructionFooterTranslatersList().forEach(translaterObj -> {
			row.getCell(28 + roleNamesListSize + translaterLangsList.indexOf(translaterObj.getTranslateLang()))
					.setCellValue(translaterObj.getLabelName());
			workSheet.autoSizeColumn(
					28 + roleNamesListSize + translaterLangsList.indexOf(translaterObj.getTranslateLang()));
		});
	}

	private static void fillRowDataFieldDefenition(XSSFSheet workSheet, XSSFRow row,
			Zone5FieldDefinitionDTO zone5FieldDefinitionDTO, List<String> translaterLangsList,
			List<String> validationsFieldTypesList, XSSFWorkbook workBook, List<String> roleNamesList,
			List<Zone12FieldPermissionDTO> zone12FieldPermissionsList, List<String> permissionsGroupList) {
		row.getCell(3).setCellValue(zone5FieldDefinitionDTO.getFieldId());
		row.getCell(4).setCellValue(zone5FieldDefinitionDTO.getFieldLabel());
		row.getCell(5).setCellValue(zone5FieldDefinitionDTO.getFieldType());
		CommonTemplateUtils.addDataValidation255CharsAbove(workBook, workSheet, row.getRowNum(), row.getRowNum(), 5, 5,
				validationsFieldTypesList.toArray(new String[0]), "Validations", "!$B$2:$B$31");
		row.getCell(6).setCellValue(zone5FieldDefinitionDTO.getMimeType());
		CommonTemplateUtils.addDataValidation(workSheet, row.getRowNum(), row.getRowNum(), 6, 6,
				new String[] { "text-plain", "text-html" });
		row.getCell(7).setCellValue(zone5FieldDefinitionDTO.getParentPickListIdOrObject());
		row.getCell(8).setCellValue(zone5FieldDefinitionDTO.getPickListOrObject());
		row.getCell(9).setCellValue(zone5FieldDefinitionDTO.getObjectType());
		row.getCell(10).setCellValue(CommonTemplateUtils.convertToYesOrNo(zone5FieldDefinitionDTO.getRequired()));
		row.getCell(12).setCellValue(CommonTemplateUtils.convertToYesOrNo(zone5FieldDefinitionDTO.getCustom()));
		row.getCell(13).setCellValue(CommonTemplateUtils.convertToYesOrNo(zone5FieldDefinitionDTO.getAnonymize()));
		row.getCell(14).setCellValue(CommonTemplateUtils.convertToYesOrNo(zone5FieldDefinitionDTO.getSensitive()));
		row.getCell(15).setCellValue(zone5FieldDefinitionDTO.getFieldDescription());
		row.getCell(16).setCellValue(zone5FieldDefinitionDTO.getEmployeeTypeId());
		row.getCell(17).setCellValue(zone5FieldDefinitionDTO.getEmployeeProfileFieldId());
		row.getCell(18).setCellValue(zone5FieldDefinitionDTO.getAvailableForCandidateSearhResult());
		row.getCell(19).setCellValue(zone5FieldDefinitionDTO.getDisplayOrFilter());
		row.getCell(20).setCellValue(zone5FieldDefinitionDTO.getReportable());
		row.getCell(21).setCellValue(zone5FieldDefinitionDTO.getNotesOrComments());

		zone12FieldPermissionsList.forEach(zone12FieldPermissionObj -> {
			zone12FieldPermissionObj.getFieldPermissionsList().forEach(fieldPermissionObj -> {
				if (fieldPermissionObj.getFieldId().equalsIgnoreCase(zone5FieldDefinitionDTO.getFieldId())) {
					row.getCell(24 + permissionsGroupList.indexOf(zone12FieldPermissionObj.getCountry()
							+ zone12FieldPermissionObj.getRoleName() + zone12FieldPermissionObj.getSource()))
							.setCellValue(fieldPermissionObj.getPermissionType());
				}
			});
		});
		int roleNamesListSize = roleNamesList.size();
		zone5FieldDefinitionDTO.getZone5FieldDefinitionTranslatersList().forEach(translaterObj -> {
			row.getCell(28 + roleNamesListSize + translaterLangsList.indexOf(translaterObj.getTranslateLang()))
					.setCellValue(translaterObj.getLabelName());
			workSheet.autoSizeColumn(
					28 + roleNamesListSize + translaterLangsList.indexOf(translaterObj.getTranslateLang()));
		});
	}

	private static void fillRowDataBackgroundElement(XSSFSheet workSheet, XSSFRow row,
			Zone10BackgroundElementDTO zone10BackgroundElementDTO, List<String> translaterLangsList,
			List<String> validationsFieldTypesList, XSSFWorkbook workBook, List<String> roleNamesList,
			List<Zone12FieldPermissionDTO> zone12FieldPermissionsList, List<String> permissionsGroupList) {

		row.getCell(3).setCellValue(zone10BackgroundElementDTO.getFieldId());
		row.getCell(4).setCellValue(zone10BackgroundElementDTO.getFieldlabel());
		row.getCell(5).setCellValue(zone10BackgroundElementDTO.getFieldType());
		CommonTemplateUtils.addDataValidation255CharsAbove(workBook, workSheet, row.getRowNum(), row.getRowNum(), 5, 5,
				validationsFieldTypesList.toArray(new String[0]), "Validations", "!$B$2:$B$31");
		row.getCell(6).setCellValue(zone10BackgroundElementDTO.getMimeType());
		CommonTemplateUtils.addDataValidation(workSheet, row.getRowNum(), row.getRowNum(), 6, 6,
				new String[] { "text-plain", "text-html" });
		row.getCell(7).setCellValue(zone10BackgroundElementDTO.getParentPickListIdOrObject());
		row.getCell(8).setCellValue(zone10BackgroundElementDTO.getPickListObject());
		row.getCell(10).setCellValue(CommonTemplateUtils.convertToYesOrNo(zone10BackgroundElementDTO.getRequired()));
		row.getCell(11).setCellValue(zone10BackgroundElementDTO.getMaxLength());
		row.getCell(12).setCellValue(CommonTemplateUtils.convertToYesOrNo(zone10BackgroundElementDTO.getCustom()));
		row.getCell(13).setCellValue(CommonTemplateUtils.convertToYesOrNo(zone10BackgroundElementDTO.getAnonymize()));
		row.getCell(14).setCellValue(CommonTemplateUtils.convertToYesOrNo(zone10BackgroundElementDTO.getSensitive()));
		row.getCell(16).setCellValue(zone10BackgroundElementDTO.getEmployeeTypeId());
		row.getCell(17).setCellValue(zone10BackgroundElementDTO.getEmployeeProfileFieldId());
		int roleNamesListSize = roleNamesList.size();
		zone10BackgroundElementDTO.getZone10BackgroundElementTranslatersList().forEach(translaterObj -> {
			row.getCell(28 + roleNamesListSize + translaterLangsList.indexOf(translaterObj.getTranslateLang()))
					.setCellValue(translaterObj.getLabelName());
			workSheet.autoSizeColumn(
					28 + roleNamesListSize + translaterLangsList.indexOf(translaterObj.getTranslateLang()));
		});

		zone12FieldPermissionsList.forEach(zone12FieldPermissionObj -> {
			zone12FieldPermissionObj.getFieldPermissionsList().forEach(fieldPermissionObj -> {
				if (StringUtils.isNotEmpty(fieldPermissionObj.getBeFieldId())
						&& fieldPermissionObj.getFieldId().equalsIgnoreCase(zone10BackgroundElementDTO.getFieldId())) {
					row.getCell(24 + permissionsGroupList.indexOf(zone12FieldPermissionObj.getCountry()
							+ zone12FieldPermissionObj.getRoleName() + zone12FieldPermissionObj.getSource()))
							.setCellValue(fieldPermissionObj.getPermissionType());
				}
			});
		});

		for (int i = 0; i < zone10BackgroundElementDTO.getZone10BackgroundElementDataFieldsList().size(); i++) {
			CommonTemplateUtils.createRow(workBook, workSheet, (row.getRowNum() + 1 + i), (row.getRowNum() + 2 + i));
			fillRowDataBackgroundElementField(workSheet, workSheet.getRow(row.getRowNum() + 1 + i),
					zone10BackgroundElementDTO.getZone10BackgroundElementDataFieldsList().get(i), translaterLangsList,
					validationsFieldTypesList, workBook, roleNamesList, zone12FieldPermissionsList,
					permissionsGroupList);
		}
	}

	private static void fillRowDataBackgroundElementField(XSSFSheet workSheet, XSSFRow row,
			Zone10BackgroundElementDataFieldDTO zone10BackgroundElementDataFieldDTO, List<String> translaterLangsList,
			List<String> validationsFieldTypesList, XSSFWorkbook workBook, List<String> roleNamesList,
			List<Zone12FieldPermissionDTO> zone12FieldPermissionsList, List<String> permissionsGroupList) {
		row.getCell(3).setCellValue(zone10BackgroundElementDataFieldDTO.getFieldId());
		row.getCell(4).setCellValue(zone10BackgroundElementDataFieldDTO.getFieldlabel());
		row.getCell(5).setCellValue(zone10BackgroundElementDataFieldDTO.getFieldType());
		CommonTemplateUtils.addDataValidation255CharsAbove(workBook, workSheet, row.getRowNum(), row.getRowNum(), 5, 5,
				validationsFieldTypesList.toArray(new String[0]), "Validations", "!$B$2:$B$31");
		row.getCell(6).setCellValue(zone10BackgroundElementDataFieldDTO.getMimeType());
		CommonTemplateUtils.addDataValidation(workSheet, row.getRowNum(), row.getRowNum(), 6, 6,
				new String[] { "text-plain", "text-html" });
		row.getCell(7).setCellValue(zone10BackgroundElementDataFieldDTO.getParentPickListIdOrObject());
		row.getCell(8).setCellValue(zone10BackgroundElementDataFieldDTO.getPickListObject());
		row.getCell(10)
				.setCellValue(CommonTemplateUtils.convertToYesOrNo(zone10BackgroundElementDataFieldDTO.getRequired()));
		row.getCell(11).setCellValue(zone10BackgroundElementDataFieldDTO.getMaxLength());
		row.getCell(12)
				.setCellValue(CommonTemplateUtils.convertToYesOrNo(zone10BackgroundElementDataFieldDTO.getCustom()));
		row.getCell(13)
				.setCellValue(CommonTemplateUtils.convertToYesOrNo(zone10BackgroundElementDataFieldDTO.getAnonymize()));
		row.getCell(14)
				.setCellValue(CommonTemplateUtils.convertToYesOrNo(zone10BackgroundElementDataFieldDTO.getSensitive()));
		row.getCell(16).setCellValue(zone10BackgroundElementDataFieldDTO.getEmployeeTypeId());
		row.getCell(17).setCellValue(zone10BackgroundElementDataFieldDTO.getEmployeeProfileFieldId());
		int roleNamesListSize = roleNamesList.size();
		zone10BackgroundElementDataFieldDTO.getZone10BackgroundElementDataFieldTranslatersList()
				.forEach(translaterObj -> {
					row.getCell(28 + roleNamesListSize + translaterLangsList.indexOf(translaterObj.getTranslateLang()))
							.setCellValue(translaterObj.getLabelName());
					workSheet.autoSizeColumn(
							28 + roleNamesListSize + translaterLangsList.indexOf(translaterObj.getTranslateLang()));
				});

		zone12FieldPermissionsList.forEach(zone12FieldPermissionObj -> {
			zone12FieldPermissionObj.getFieldPermissionsList().forEach(fieldPermissionObj -> {
				if (StringUtils.isNotEmpty(fieldPermissionObj.getBeFieldId()) && fieldPermissionObj.getFieldId()
						.equalsIgnoreCase(zone10BackgroundElementDataFieldDTO.getFieldId())) {
					row.getCell(24 + permissionsGroupList.indexOf(zone12FieldPermissionObj.getCountry()
							+ zone12FieldPermissionObj.getRoleName() + zone12FieldPermissionObj.getSource()))
							.setCellValue(fieldPermissionObj.getPermissionType());
				}
			});
		});

	}

	private static void fillRowDataSearchDisplayOption(XSSFSheet workSheet, XSSFRow row,
			Zone8SearchDisplayOptionDTO zone8SearchDisplayOptionDTO, List<String> translaterSSLangsList,
			XSSFWorkbook workBook) {
		row.getCell(3).setCellValue(zone8SearchDisplayOptionDTO.getCategoryId());
		row.getCell(4).setCellValue(zone8SearchDisplayOptionDTO.getCategoryName());
		row.getCell(5).setCellValue(zone8SearchDisplayOptionDTO.getLabel());
		row.getCell(6).setCellValue(zone8SearchDisplayOptionDTO.getFieldRefId());
		row.getCell(7).setCellValue(zone8SearchDisplayOptionDTO.getBackgroundElementRef());
		row.getCell(8)
				.setCellValue(CommonTemplateUtils.convertToYesOrNo(zone8SearchDisplayOptionDTO.getSelectByDefault()));
		CommonTemplateUtils.addDataValidation(workSheet, row.getRowNum(), row.getRowNum(), 8, 8,
				new String[] { "Y", "N" });
		row.getCell(9).setCellValue(zone8SearchDisplayOptionDTO.getGridOrder());
		row.getCell(10).setCellValue(zone8SearchDisplayOptionDTO.getTagsNeeded());
		zone8SearchDisplayOptionDTO.getTranslatersList().forEach(translaterObj -> {
			row.getCell(14 + translaterSSLangsList.indexOf(translaterObj.getTranslateLang()))
					.setCellValue(translaterObj.getTranslateLabel());
			workSheet.autoSizeColumn(14 + translaterSSLangsList.indexOf(translaterObj.getTranslateLang()));
		});
		for (int i = 0; i < zone8SearchDisplayOptionDTO.getZone8SearchDisplayOptionSubCategoryList().size(); i++) {
			CommonTemplateUtils.createRow(workBook, workSheet, (row.getRowNum() + 1 + i), (row.getRowNum() + 2 + i));
			fillRowDataSearchDisplayOptionSubCategory(workSheet, workSheet.getRow(row.getRowNum() + 1 + i),
					zone8SearchDisplayOptionDTO.getZone8SearchDisplayOptionSubCategoryList().get(i),
					translaterSSLangsList);
		}
	}

	private static void fillRowDataSearchDisplayOptionSubCategory(XSSFSheet workSheet, XSSFRow row,
			Zone8SearchDisplayOptionSubCategoryDTO zone8SearchDisplayOptionSubCategoryDTO,
			List<String> translaterSSLangsList) {
		row.getCell(6).setCellValue(zone8SearchDisplayOptionSubCategoryDTO.getFieldRefId());
		row.getCell(7).setCellValue(zone8SearchDisplayOptionSubCategoryDTO.getBackgroundElementRef());
		row.getCell(8).setCellValue(
				CommonTemplateUtils.convertToYesOrNo(zone8SearchDisplayOptionSubCategoryDTO.getSelectByDefault()));
		CommonTemplateUtils.addDataValidation(workSheet, row.getRowNum(), row.getRowNum(), 8, 8,
				new String[] { "Y", "N" });
		row.getCell(9).setCellValue(zone8SearchDisplayOptionSubCategoryDTO.getGridOrder());
		row.getCell(10).setCellValue(zone8SearchDisplayOptionSubCategoryDTO.getTagsNeeded());
	}

	private static void fillRowDataSummaryDisplayOption(XSSFSheet workSheet, XSSFRow row,
			Zone9SummaryDisplayOptionDTO zone9SummaryDisplayOptionDTO, List<String> translaterSSLangsList,
			XSSFWorkbook workBook) {
		row.getCell(3).setCellValue(zone9SummaryDisplayOptionDTO.getCategoryId());
		row.getCell(4).setCellValue(zone9SummaryDisplayOptionDTO.getCategoryName());
		row.getCell(5).setCellValue(zone9SummaryDisplayOptionDTO.getLabel());
		row.getCell(6).setCellValue(zone9SummaryDisplayOptionDTO.getFieldRefId());
		row.getCell(7)
				.setCellValue(CommonTemplateUtils.convertToYesOrNo(zone9SummaryDisplayOptionDTO.getSelectByDefault()));
		CommonTemplateUtils.addDataValidation(workSheet, row.getRowNum(), row.getRowNum(), 7, 7,
				new String[] { "Y", "N" });
		row.getCell(8).setCellValue(zone9SummaryDisplayOptionDTO.getGridOrder());
		zone9SummaryDisplayOptionDTO.getTranslatersList().forEach(translaterObj -> {
			row.getCell(14 + translaterSSLangsList.indexOf(translaterObj.getTranslateLang()))
					.setCellValue(translaterObj.getTranslateLabel());
			workSheet.autoSizeColumn(14 + translaterSSLangsList.indexOf(translaterObj.getTranslateLang()));
		});
		for (int i = 0; i < zone9SummaryDisplayOptionDTO.getZone9SummaryDisplayOptionSubCategoryList().size(); i++) {
			CommonTemplateUtils.createRow(workBook, workSheet, (row.getRowNum() + 1 + i), (row.getRowNum() + 2 + i));
			fillRowDataSummaryDisplayOptionSubCategory(workSheet, workSheet.getRow(row.getRowNum() + 1 + i),
					zone9SummaryDisplayOptionDTO.getZone9SummaryDisplayOptionSubCategoryList().get(i),
					translaterSSLangsList);
		}
	}

	private static void fillRowDataSummaryDisplayOptionSubCategory(XSSFSheet workSheet, XSSFRow row,
			Zone9SummaryDisplayOptionSubCategoryDTO zone9SummaryDisplayOptionSubCategoryDTO,
			List<String> translaterSSLangsList) {
		row.getCell(6).setCellValue(zone9SummaryDisplayOptionSubCategoryDTO.getFieldRefId());
		row.getCell(7).setCellValue(
				CommonTemplateUtils.convertToYesOrNo(zone9SummaryDisplayOptionSubCategoryDTO.getSelectByDefault()));
		CommonTemplateUtils.addDataValidation(workSheet, row.getRowNum(), row.getRowNum(), 7, 7,
				new String[] { "Y", "N" });
		row.getCell(8).setCellValue(zone9SummaryDisplayOptionSubCategoryDTO.getGridOrder());
	}
}
