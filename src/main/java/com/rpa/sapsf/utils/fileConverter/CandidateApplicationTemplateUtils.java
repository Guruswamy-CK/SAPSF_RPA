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
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Row;
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

import com.rpa.sapsf.candidate.application.*;
import com.rpa.sapsf.dto.EnumDTO;
import com.rpa.sapsf.dto.EnumLabelTranslaterDTO;
import com.rpa.sapsf.dto.TranslaterDTO;

import io.micrometer.common.util.StringUtils;

public class CandidateApplicationTemplateUtils {
	static boolean isReadPermissionReady = false;
	static boolean isWritePermissionReady = false;
	static boolean isExternalViewPermissionReady = false;
	static boolean isTruePermissionReady = false;
	static boolean isFalsePermissionReady = false;

	public static CandidateApplicationTemplateDTO extractExcelData(MultipartFile file) {
		CandidateApplicationTemplateDTO candidateApplicationExcelData = new CandidateApplicationTemplateDTO();

		// Zone 2 objects
		Zone2DTO zone2Data = new Zone2DTO();
		List<TranslaterDTO> zone2TemplateNameTranslatersList = new ArrayList<TranslaterDTO>();
		List<TranslaterDTO> zone2TemplateDescriptionTranslatersList = new ArrayList<TranslaterDTO>();
		List<String> translateTemplateNamesList = new ArrayList<String>();
		List<String> translateTemplateDesrciptionsList = new ArrayList<String>();

		// Zone 3 objects
		Zone3DTO zone3Data = new Zone3DTO();
		List<Zone3FieldDefinitionDTO> zone3FieldDefinitionsList = new ArrayList<Zone3FieldDefinitionDTO>();

		// Zone 4 objects
		Zone4DTO zone4Data = new Zone4DTO();
		List<Zone4FieldPermissionDTO> zone4FieldPermissionsList = new ArrayList<Zone4FieldPermissionDTO>();

		// Zone 5 objects
		Zone5DTO zone5Data = new Zone5DTO();
		List<Zone5FieldAttributeOverrideDTO> zone5FieldAttributeOverridesList = new ArrayList<Zone5FieldAttributeOverrideDTO>();

		// Zone 6 objects
		Zone6DTO zone6Data = new Zone6DTO();
		List<Zone6ButtonPermissionDTO> zone6ButtonPermissionsList = new ArrayList<Zone6ButtonPermissionDTO>();

		// Zone 7 objects
		Zone7DTO zone7Data = new Zone7DTO();
		List<Zone7SummaryDisplayOptionDTO> zone7SummaryDisplayOptionsList = new ArrayList<Zone7SummaryDisplayOptionDTO>();

		// Common Objects
		List<String> translateLangsList = new ArrayList<String>();
		List<String> translateDescLangsList = new ArrayList<String>();
		List<String> translateLangsSummaryList = new ArrayList<String>();
		List<EnumDTO> enumValuesList = new ArrayList<EnumDTO>();
		List<String> roleNamesList = new ArrayList<String>();
		List<String> buttonRoleNamesList = new ArrayList<String>();
		List<String> countriesOverrideList = new ArrayList<String>();
		List<String> attributesOverrideList = new ArrayList<String>();
		List<String> applicantsOverrideList = new ArrayList<String>();

		try {
			XSSFWorkbook workBook;
			ZipSecureFile.setMinInflateRatio(0);
			workBook = new XSSFWorkbook(file.getInputStream());
			XSSFSheet sheetTemplate = workBook.getSheet("Application_Fields&Buttons");
			XSSFSheet sheetOverride = workBook.getSheet("Application_Override");
			XSSFSheet sheetSummary = workBook.getSheet("Summary_Display_Option");
			XSSFRow row;
			int rowNum = 0;
			int rowNumSummery = 0;
			int rowNumOverride = 0;
			int rowNumApplicationButtonsStart = 0;
			int translatersStartColNum = 0;
			int translatersDescStartColNum = 0;
			int fieldPermissionsStartColNum = 0;
			int rowNumOverrideSheetStart = 0;
			int rowNumTemplateSheetStart = 0;
			int rowNumSummarySheetStart = 0;
			Iterator<Row> rowsTemplate = null;
			Iterator<Row> rowsOverride = null;
			Iterator<Row> rowsSummery = null;
			if (null != sheetTemplate)
				rowsTemplate = sheetTemplate.rowIterator();
			if (null != sheetOverride)
				rowsOverride = sheetOverride.rowIterator();
			if (null != sheetSummary)
				rowsSummery = sheetSummary.rowIterator();
			CommonTemplateUtils.extractEnumData("Application_Enum_Values", workBook, enumValuesList);

			/* Override read starts here */
			while (null != rowsOverride && rowsOverride.hasNext()) {
				rowNumOverride++;
				row = (XSSFRow) rowsOverride.next();
				if (row.getCell(3) != null
						&& "Template Name".equalsIgnoreCase(CommonTemplateUtils.readCellValue(row.getCell(3)))) {
					rowNumOverrideSheetStart = rowNumOverride;
				}
				if (rowNumOverrideSheetStart != 0) {
					if (rowNumOverride == (rowNumOverrideSheetStart + 2)) {
						int colNum = 5;
						while (null != row.getCell(colNum)
								&& StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colNum)))) {
							String country = CommonTemplateUtils.readCellValue(row.getCell(colNum));
							countriesOverrideList.add(country.substring(0, country.lastIndexOf("|")).trim());
							colNum++;
						}
					} else if (rowNumOverride == (rowNumOverrideSheetStart + 3)) {
						int colNum = 5;
						for (int i = 0; i < countriesOverrideList.size(); i++) {
							applicantsOverrideList.add(CommonTemplateUtils.readCellValue(row.getCell(colNum + i)));
						}
					} else if (rowNumOverride == (rowNumOverrideSheetStart + 4)) {
						int colNum = 5;
						for (int i = 0; i < countriesOverrideList.size(); i++) {
							attributesOverrideList.add(CommonTemplateUtils.readCellValue(row.getCell(colNum + i)));
						}

						/* Zone 5 starts here */
						if (countriesOverrideList.size() > 0) {
							for (int i = 0; i < countriesOverrideList.size(); i++) {
								Zone5FieldAttributeOverrideDTO zone5FieldAttributeOverride = new Zone5FieldAttributeOverrideDTO();
								zone5FieldAttributeOverride.setCountry(countriesOverrideList.get(i));
								if (i < applicantsOverrideList.size()) {
									zone5FieldAttributeOverride.setApplicant(applicantsOverrideList.get(i));
								}
								if (i < attributesOverrideList.size()) {
									zone5FieldAttributeOverride.setAttribute(attributesOverrideList.get(i));
								}
								zone5FieldAttributeOverride.setFieldOverridesList(new ArrayList<FieldOverrideDTO>());

								if (StringUtils.isNotEmpty(zone5FieldAttributeOverride.getApplicant())
										&& StringUtils.isNotEmpty(zone5FieldAttributeOverride.getCountry())
										&& StringUtils.isNotEmpty(zone5FieldAttributeOverride.getAttribute()))
									zone5FieldAttributeOverridesList.add(zone5FieldAttributeOverride);
							}
						}
						/* Zone 5 ends here */
					} else if (rowNumOverride > (rowNumOverrideSheetStart + 4)) {
						if (null != row.getCell(3)
								&& StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(3)))) {
							/* Zone 5 starts here */
							int colNumOverride = 5;
							int indexOverrideZone5 = 0;
							while (null != row.getCell(colNumOverride)) {
								if (StringUtils
										.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colNumOverride)))
										&& indexOverrideZone5 < zone5FieldAttributeOverridesList.size()) {
									FieldOverrideDTO fieldOverride = new FieldOverrideDTO();
									fieldOverride.setFieldId(CommonTemplateUtils.readCellValue(row.getCell(3)));
									fieldOverride.setOverrideValue(
											CommonTemplateUtils.readCellValue(row.getCell(colNumOverride)));
									zone5FieldAttributeOverridesList.get(indexOverrideZone5).getFieldOverridesList()
											.add(fieldOverride);
								}
								colNumOverride++;
								indexOverrideZone5++;
							}
							/* Zone 5 ends here */
						}
					}
				}
			}
			/* Override read ends here */

			/* Summary read starts here */
			while (null != rowsSummery && rowsSummery.hasNext()) {
				rowNumSummery++;
				row = (XSSFRow) rowsSummery.next();
				if (row.getCell(3) != null
						&& "Template Name".equalsIgnoreCase(CommonTemplateUtils.readCellValue(row.getCell(3)))) {
					rowNumSummarySheetStart = rowNumSummery;
				}
				if (rowNumSummarySheetStart != 0) {
					/* Zone 7 starts here */
					if (rowNumSummery == (rowNumSummarySheetStart + 3)) {
						int colNum = 14;
						while (null != row.getCell(colNum)
								&& StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colNum)))) {
							translateLangsSummaryList
									.add(CommonTemplateUtils.readCellValue(row.getCell(colNum)).substring(0, 5));
							colNum++;
						}
					} else if (rowNumSummery > (rowNumSummarySheetStart + 3)) {
						if (null != row.getCell(3)
								&& StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(3)))) {
							Zone7SummaryDisplayOptionDTO zone7SummaryDisplayOption = new Zone7SummaryDisplayOptionDTO();
							zone7SummaryDisplayOption.setCategoryId(CommonTemplateUtils.readCellValue(row.getCell(3)));
							zone7SummaryDisplayOption
									.setCategoryName(CommonTemplateUtils.readCellValue(row.getCell(4)));
							zone7SummaryDisplayOption.setLabel(CommonTemplateUtils.readCellValue(row.getCell(5)));
							zone7SummaryDisplayOption.setFieldRefId(CommonTemplateUtils.readCellValue(row.getCell(6)));
							zone7SummaryDisplayOption
									.setSelectByDefault(CommonTemplateUtils.readCellValue(row.getCell(7)));
							zone7SummaryDisplayOption.setGridOrder(CommonTemplateUtils.readCellValue(row.getCell(8)));
							int colNum = 14;
							int index = 0;
							List<TranslaterDTO> translatersList = new ArrayList<TranslaterDTO>();
							while (null != row.getCell(colNum)) {
								if (StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colNum)))
										&& index < translateLangsSummaryList.size()) {
									TranslaterDTO translaterData = new TranslaterDTO();
									translaterData
											.setTranslateLabel(CommonTemplateUtils.readCellValue(row.getCell(colNum)));
									translaterData.setTranslateLang(translateLangsSummaryList.get(index));
									translatersList.add(translaterData);
								}
								colNum++;
								index++;
							}
							zone7SummaryDisplayOption.setTranslatersList(translatersList);
							zone7SummaryDisplayOption.setZone7SummaryDisplayOptionSubCategoryList(
									new ArrayList<Zone7SummaryDisplayOptionSubCategoryDTO>());

							Zone7SummaryDisplayOptionSubCategoryDTO zone7SummaryDisplayOptionSubCategory = new Zone7SummaryDisplayOptionSubCategoryDTO();
							zone7SummaryDisplayOptionSubCategory
									.setFieldRefId(zone7SummaryDisplayOption.getFieldRefId());
							zone7SummaryDisplayOptionSubCategory
									.setSelectByDefault(zone7SummaryDisplayOption.getSelectByDefault());
							zone7SummaryDisplayOptionSubCategory.setGridOrder(zone7SummaryDisplayOption.getGridOrder());
							zone7SummaryDisplayOptionSubCategory.setTranslatersList(translatersList);
							zone7SummaryDisplayOption.getZone7SummaryDisplayOptionSubCategoryList()
									.add(zone7SummaryDisplayOptionSubCategory);

							zone7SummaryDisplayOptionsList.add(zone7SummaryDisplayOption);
						} else if (null != row.getCell(6)
								&& StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(6)))) {
							Zone7SummaryDisplayOptionDTO zone7SummaryDisplayOption = zone7SummaryDisplayOptionsList
									.get(zone7SummaryDisplayOptionsList.size() - 1);
							Zone7SummaryDisplayOptionSubCategoryDTO zone7SummaryDisplayOptionSubCategory = new Zone7SummaryDisplayOptionSubCategoryDTO();
							zone7SummaryDisplayOptionSubCategory
									.setFieldRefId(CommonTemplateUtils.readCellValue(row.getCell(6)));
							zone7SummaryDisplayOptionSubCategory
									.setSelectByDefault(CommonTemplateUtils.readCellValue(row.getCell(7)));
							zone7SummaryDisplayOptionSubCategory
									.setGridOrder(CommonTemplateUtils.readCellValue(row.getCell(8)));
							int colNum = 14;
							int index = 0;
							List<TranslaterDTO> translatersList = new ArrayList<TranslaterDTO>();
							while (null != row.getCell(colNum)) {
								if (StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colNum)))
										&& index < translateLangsSummaryList.size()) {
									TranslaterDTO translaterData = new TranslaterDTO();
									translaterData
											.setTranslateLabel(CommonTemplateUtils.readCellValue(row.getCell(colNum)));
									translaterData.setTranslateLang(translateLangsSummaryList.get(index));
									translatersList.add(translaterData);
								}
								colNum++;
								index++;
							}
							zone7SummaryDisplayOptionSubCategory.setTranslatersList(translatersList);
							zone7SummaryDisplayOption.getZone7SummaryDisplayOptionSubCategoryList()
									.add(zone7SummaryDisplayOptionSubCategory);
						}
						/* Zone 7 ends here */
					}
				}
			}
			/* Summary read ends here */

			/* Template read starts here */
			while (null != rowsTemplate && rowsTemplate.hasNext()) {
				rowNum++;
				row = (XSSFRow) rowsTemplate.next();
				if (row.getCell(4) != null
						&& "Application Buttons".equalsIgnoreCase(CommonTemplateUtils.readCellValue(row.getCell(4)))) {
					rowNumApplicationButtonsStart = rowNum;
				}
				if (rowNumApplicationButtonsStart == 0) {
					if (row.getCell(4) != null
							&& "Template Name".equalsIgnoreCase(CommonTemplateUtils.readCellValue(row.getCell(4)))) {
						rowNumTemplateSheetStart = rowNum;
					}
					if (rowNumTemplateSheetStart != 0) {
						/* Zone 2 starts here */
						if (rowNum == rowNumTemplateSheetStart) {
							zone2Data.setTemplateName(CommonTemplateUtils.readCellValue(row.getCell(5)));
							for (int translatersStartColNumTemp = 21; translatersStartColNum == 0; translatersStartColNumTemp++) {
								if (null != row.getCell(translatersStartColNumTemp)
										&& StringUtils.isNotEmpty(CommonTemplateUtils
												.readCellValue(row.getCell(translatersStartColNumTemp)))
										&& "Template Name and Field Label Translations"
												.equalsIgnoreCase(CommonTemplateUtils
														.readCellValue(row.getCell(translatersStartColNumTemp))
														.trim())) {
									translatersStartColNum = translatersStartColNumTemp;
								}
							}
							int colNum = translatersStartColNum + 1;
							while (null != row.getCell(colNum)
									&& StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colNum)))) {
								translateTemplateNamesList.add(CommonTemplateUtils.readCellValue(row.getCell(colNum)));
								colNum++;
							}

							for (int translatersDescStartColNumTemp = translatersStartColNum; translatersDescStartColNum == 0; translatersDescStartColNumTemp++) {
								if (null != row.getCell(translatersDescStartColNumTemp)
										&& StringUtils.isNotEmpty(CommonTemplateUtils
												.readCellValue(row.getCell(translatersDescStartColNumTemp)))
										&& "Field Description Translations".equalsIgnoreCase(CommonTemplateUtils
												.readCellValue(row.getCell(translatersDescStartColNumTemp)).trim())) {
									translatersDescStartColNum = translatersDescStartColNumTemp;
								}
							}

						} else if (rowNum == (rowNumTemplateSheetStart + 1)) {
							zone2Data.setTemplateDescription(CommonTemplateUtils.readCellValue(row.getCell(5)));
							zone2Data.setIsMultiStage(CommonTemplateUtils.readCellValue(row.getCell(8)));
							int colNum = translatersStartColNum + 1;
							while (null != row.getCell(colNum)
									&& StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colNum)))) {
								translateTemplateDesrciptionsList
										.add(CommonTemplateUtils.readCellValue(row.getCell(colNum)));
								colNum++;
							}
						} else if (rowNum == (rowNumTemplateSheetStart + 2)) {
							for (int fieldPermissionsStartColNumTemp = 21; fieldPermissionsStartColNum == 0; fieldPermissionsStartColNumTemp++) {
								if (null != row.getCell(fieldPermissionsStartColNumTemp)
										&& StringUtils.isNotEmpty(CommonTemplateUtils
												.readCellValue(row.getCell(fieldPermissionsStartColNumTemp)))
										&& "Field Permissions".equalsIgnoreCase(CommonTemplateUtils
												.readCellValue(row.getCell(fieldPermissionsStartColNumTemp)).trim())) {
									fieldPermissionsStartColNum = fieldPermissionsStartColNumTemp;
								}
							}
						} else if (rowNum == (rowNumTemplateSheetStart + 3)) {
							int roleColNum = fieldPermissionsStartColNum + 1;
							while (null != row.getCell(roleColNum) && StringUtils
									.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(roleColNum)))) {
								roleNamesList.add(CommonTemplateUtils.readCellValue(row.getCell(roleColNum)));
								roleColNum++;
							}
							int colNum = translatersStartColNum + 1;
							while (null != row.getCell(colNum)
									&& StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colNum)))) {
								translateLangsList
										.add(CommonTemplateUtils.readCellValue(row.getCell(colNum)).substring(0, 5));
								colNum++;
							}
							int colNumDesc = translatersDescStartColNum + 1;
							while (null != row.getCell(colNumDesc) && StringUtils
									.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colNumDesc)))) {
								translateDescLangsList.add(
										CommonTemplateUtils.readCellValue(row.getCell(colNumDesc)).substring(0, 5));
								colNumDesc++;
							}
							/* Zone 4 starts here */
							if (roleNamesList.size() > 0) {
								for (int i = 0; i < roleNamesList.size(); i++) {
									Zone4FieldPermissionDTO zone4FieldPermission = new Zone4FieldPermissionDTO();
									zone4FieldPermission.setRoleName(roleNamesList.get(i));
									zone4FieldPermission.setFieldPermissionsList(new ArrayList<FieldPermissionDTO>());
									if (StringUtils.isNotEmpty(zone4FieldPermission.getRoleName()))
										zone4FieldPermissionsList.add(zone4FieldPermission);
								}
							}
							/* Zone 4 ends here */
						}
						/* Zone 2 ends here */
						else if (rowNum > (rowNumTemplateSheetStart + 3)) {
							/* Zone 3 starts here */
							if (null != row.getCell(4)
									&& StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(4)))) {
								Zone3FieldDefinitionDTO zone3FieldDefinition = new Zone3FieldDefinitionDTO();
								zone3FieldDefinition.setFieldId(CommonTemplateUtils.readCellValue(row.getCell(4)));
								zone3FieldDefinition.setFieldLabel(CommonTemplateUtils.readCellValue(row.getCell(5)));
								zone3FieldDefinition.setFieldType(CommonTemplateUtils.readCellValue(row.getCell(6)));
								zone3FieldDefinition.setMimeType(CommonTemplateUtils.readCellValue(row.getCell(7)));
								zone3FieldDefinition
										.setParentPickListOrObject(CommonTemplateUtils.readCellValue(row.getCell(8)));
								zone3FieldDefinition
										.setPickListOrObjectId(CommonTemplateUtils.readCellValue(row.getCell(9)));
								zone3FieldDefinition.setRequired(CommonTemplateUtils.readCellValue(row.getCell(10)));
								zone3FieldDefinition.setCustom(CommonTemplateUtils.readCellValue(row.getCell(11)));
								zone3FieldDefinition.setIsPublic(CommonTemplateUtils.readCellValue(row.getCell(12)));
								zone3FieldDefinition.setAnonymize(CommonTemplateUtils.readCellValue(row.getCell(13)));
								zone3FieldDefinition
										.setForwardIntact(CommonTemplateUtils.readCellValue(row.getCell(14)));
								zone3FieldDefinition.setSensitive(CommonTemplateUtils.readCellValue(row.getCell(15)));
								zone3FieldDefinition.setReportable(CommonTemplateUtils.readCellValue(row.getCell(16)));
								zone3FieldDefinition
										.setFieldDescription(CommonTemplateUtils.readCellValue(row.getCell(17)));
								zone3FieldDefinition
										.setRecruiterHelpText(CommonTemplateUtils.readCellValue(row.getCell(18)));
								zone3FieldDefinition
										.setCandidateHelpText(CommonTemplateUtils.readCellValue(row.getCell(19)));
								zone3FieldDefinition.setNotes(CommonTemplateUtils.readCellValue(row.getCell(20)));
								int colPermissionNum = fieldPermissionsStartColNum + 1;
								int indexPermissionZone3 = 0;
								while (null != row.getCell(colPermissionNum)) {
									if (StringUtils.isNotEmpty(
											CommonTemplateUtils.readCellValue(row.getCell(colPermissionNum)))
											&& indexPermissionZone3 < zone4FieldPermissionsList.size()) {
										FieldPermissionDTO fieldPermission = new FieldPermissionDTO();
										fieldPermission.setFieldId(zone3FieldDefinition.getFieldId());
										fieldPermission.setPermissionType(
												CommonTemplateUtils.readCellValue(row.getCell(colPermissionNum)));
										zone4FieldPermissionsList.get(indexPermissionZone3).getFieldPermissionsList()
												.add(fieldPermission);
									}
									colPermissionNum++;
									indexPermissionZone3++;
								}
								List<TranslaterDTO> zone3FieldDefinitionTranslatersList = new ArrayList<TranslaterDTO>();
								int colTransNum = translatersStartColNum + 1;
								int indexTransZone3 = 0;
								while (null != row.getCell(colTransNum)) {
									if (StringUtils
											.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colTransNum)))
											&& indexTransZone3 < translateLangsList.size()) {
										TranslaterDTO zone3FieldDefinitionTranslater = new TranslaterDTO();
										zone3FieldDefinitionTranslater.setTranslateLabel(
												CommonTemplateUtils.readCellValue(row.getCell(colTransNum)));
										zone3FieldDefinitionTranslater
												.setTranslateLang(translateLangsList.get(indexTransZone3));
										zone3FieldDefinitionTranslatersList.add(zone3FieldDefinitionTranslater);
									}
									colTransNum++;
									indexTransZone3++;
								}
								zone3FieldDefinition
										.setZone3FieldDefinitionTranslatersList(zone3FieldDefinitionTranslatersList);

								List<TranslaterDTO> zone3FieldDefinitionTranslatersDescList = new ArrayList<TranslaterDTO>();
								int colTransDescNum = translatersDescStartColNum + 1;
								int indexTransDescZone3 = 0;
								while (null != row.getCell(colTransDescNum)) {
									if (StringUtils
											.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colTransDescNum)))
											&& indexTransDescZone3 < translateDescLangsList.size()) {
										TranslaterDTO zone3FieldDefinitionTranslaterDesc = new TranslaterDTO();
										zone3FieldDefinitionTranslaterDesc.setTranslateLabel(
												CommonTemplateUtils.readCellValue(row.getCell(colTransDescNum)));
										zone3FieldDefinitionTranslaterDesc
												.setTranslateLang(translateDescLangsList.get(indexTransDescZone3));
										zone3FieldDefinitionTranslatersDescList.add(zone3FieldDefinitionTranslaterDesc);
									}
									colTransDescNum++;
									indexTransDescZone3++;
								}
								zone3FieldDefinition.setZone3FieldDefinitionTranslatersDescList(
										zone3FieldDefinitionTranslatersDescList);
								zone3FieldDefinitionsList.add(zone3FieldDefinition);
							}
							/* Zone 3 ends here */
						}
					}
				} else {
					/* Zone 6 starts here */
					if (rowNum == rowNumApplicationButtonsStart) {
						int roleColNum = 5;
						while (null != row.getCell(roleColNum)
								&& StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(roleColNum)))) {
							buttonRoleNamesList.add(CommonTemplateUtils.readCellValue(row.getCell(roleColNum)));
							roleColNum++;
						}
					} else if (rowNum > rowNumApplicationButtonsStart) {
						if (null != row.getCell(4)
								&& StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(4)))) {
							Zone6ButtonPermissionDTO zone6ButtonPermission = new Zone6ButtonPermissionDTO();
							zone6ButtonPermission.setButtonId(CommonTemplateUtils.readCellValue(row.getCell(4)));
							zone6ButtonPermission.setPermittedRolesList(new ArrayList<String>());
							int colPermissionNum = 5;
							int index = 0;
							while (null != row.getCell(colPermissionNum)) {
								if (StringUtils
										.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colPermissionNum)))
										&& index < buttonRoleNamesList.size()) {
									if ("Y".equalsIgnoreCase(CommonTemplateUtils
											.readCellValue(row.getCell(colPermissionNum)).substring(0, 1))) {
										zone6ButtonPermission.getPermittedRolesList()
												.add(buttonRoleNamesList.get(index));
									}
								}
								colPermissionNum++;
								index++;
							}
							if (zone6ButtonPermission.getPermittedRolesList().size() > 0)
								zone6ButtonPermissionsList.add(zone6ButtonPermission);
						}
					}
					/* Zone 6 ends here */
				}
			}
			/* Zone 2 starts here */
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy hh:mm a");
			Date date = new Date();
			zone2Data.setLastModifiedDate(formatter.format(date).toUpperCase());
			for (int i = 0; i < translateLangsList.size(); i++) {
				TranslaterDTO zone2TemplateNameTranslaterData = new TranslaterDTO();
				TranslaterDTO zone2TemplateDescriptionTranslaterData = new TranslaterDTO();
				if (i < translateTemplateNamesList.size()
						&& StringUtils.isNotEmpty(translateTemplateNamesList.get(i))) {
					zone2TemplateNameTranslaterData.setTranslateLabel(translateTemplateNamesList.get(i));
					zone2TemplateNameTranslaterData.setTranslateLang(translateLangsList.get(i));
					zone2TemplateNameTranslatersList.add(zone2TemplateNameTranslaterData);
				}
				if (i < translateTemplateDesrciptionsList.size()
						&& StringUtils.isNotEmpty(translateTemplateDesrciptionsList.get(i))) {
					zone2TemplateDescriptionTranslaterData.setTranslateLabel(translateTemplateDesrciptionsList.get(i));
					zone2TemplateDescriptionTranslaterData.setTranslateLang(translateLangsList.get(i));
					zone2TemplateDescriptionTranslatersList.add(zone2TemplateDescriptionTranslaterData);
				}
			}
			zone2Data.setZone2TemplateNameTranslatersList(zone2TemplateNameTranslatersList);
			zone2Data.setZone2TemplateDescriptionTranslatersList(zone2TemplateDescriptionTranslatersList);
			/* Zone 2 ends here */

			/* Template read ends here */
			candidateApplicationExcelData.setZone2(zone2Data);
			zone3Data.setZone3FieldDefinitionsList(zone3FieldDefinitionsList);
			candidateApplicationExcelData.setZone3(zone3Data);
			zone4Data.setZone4FieldPermissionsList(zone4FieldPermissionsList);
			candidateApplicationExcelData.setZone4(zone4Data);
			zone5Data.setZone5FieldAttributeOverridesList(zone5FieldAttributeOverridesList);
			candidateApplicationExcelData.setZone5(zone5Data);
			zone6Data.setZone6ButtonPermissionsList(zone6ButtonPermissionsList);
			candidateApplicationExcelData.setZone6(zone6Data);
			zone7Data.setZone7SummaryDisplayOptionsList(zone7SummaryDisplayOptionsList);
			candidateApplicationExcelData.setZone7(zone7Data);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return candidateApplicationExcelData;
	}

	public static String buildXmlFile(CandidateApplicationTemplateDTO candidateApplicationTemplateExcelData,
			String fileName) {
		StringBuilder xmlStringBuilder = new StringBuilder();
		xmlStringBuilder.append("<candidate-data-model spell-check=\"true\">\n");
		// Zone 2 starts here
		StringBuilder xmlStrZone2 = new StringBuilder();
		buildXmlStrZone2(xmlStrZone2, candidateApplicationTemplateExcelData.getZone2());
		xmlStringBuilder.append(xmlStrZone2);
		// Zone 2 ends here

		// Zone 3 starts here
		StringBuilder xmlStrZone3 = new StringBuilder();
		buildXmlStrZone3(xmlStrZone3, candidateApplicationTemplateExcelData.getZone3(),
				candidateApplicationTemplateExcelData.getEnumValuesList());
		xmlStringBuilder.append(xmlStrZone3);
		// Zone 3 ends here

		// Zone 4 starts here
		StringBuilder xmlStrZone4 = new StringBuilder();
		buildXmlStrZone4(xmlStrZone4, candidateApplicationTemplateExcelData.getZone4(),
				candidateApplicationTemplateExcelData.getZone2());
		xmlStringBuilder.append(xmlStrZone4);
		// Zone 4 ends here

		// Zone 5 starts here
		StringBuilder xmlStrZone5 = new StringBuilder();
		buildXmlStrZone5(xmlStrZone5, candidateApplicationTemplateExcelData.getZone5());
		xmlStringBuilder.append(xmlStrZone5);
		// Zone 5 ends here

		// Zone 6 starts here
		StringBuilder xmlStrZone6 = new StringBuilder();
		buildXmlStrZone6(xmlStrZone6, candidateApplicationTemplateExcelData.getZone6());
		xmlStringBuilder.append(xmlStrZone6);
		// Zone 6 ends here

		// Zone 7 starts here
		StringBuilder xmlStrZone7 = new StringBuilder();
		buildXmlStrZone7(xmlStrZone7, candidateApplicationTemplateExcelData.getZone7());
		xmlStringBuilder.append(xmlStrZone7);
		// Zone 7 ends here

		xmlStringBuilder.append("</candidate-data-model>");
		Document doc = CommonTemplateUtils.convertStringToDocument(xmlStringBuilder.toString());
		return CommonTemplateUtils.convertDocumentToXml(doc, fileName,
				"-//SuccessFactors, Inc.//DTD Candidate Data Model//EN", "candidate-data-model.dtd");
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
				xmlStrZone2.append("<![CDATA[" + translater.getTranslateLabel() + "]]>");
				xmlStrZone2.append("</template-name>\n");
			});
			if (StringUtils.isNotEmpty(zone2dto.getTemplateDescription())) {
				xmlStrZone2.append("<template-desc>");
				xmlStrZone2.append("<![CDATA[" + zone2dto.getTemplateDescription() + "]]>");
				xmlStrZone2.append("</template-desc>\n");
			}
			zone2dto.getZone2TemplateDescriptionTranslatersList().forEach(translater -> {
				xmlStrZone2.append("<template-desc lang=\"" + translater.getTranslateLang() + "\">");
				xmlStrZone2.append("<![CDATA[" + translater.getTranslateLabel() + "]]>");
				xmlStrZone2.append("</template-desc>\n");
			});
			xmlStrZone2
					.append("<template-lastmodified>" + zone2dto.getLastModifiedDate() + "</template-lastmodified>\n");
		}
	}

	private static void buildXmlStrZone3(StringBuilder xmlStrZone3, Zone3DTO zone3dto, List<EnumDTO> enumValuesList) {
		if (null != zone3dto) {
			zone3dto.getZone3FieldDefinitionsList().forEach(fieldDefinition -> {
				xmlStrZone3.append("<field-definition");
				xmlStrZone3.append(" id=\"" + fieldDefinition.getFieldId() + "\"");
				xmlStrZone3.append(" type=\"" + fieldDefinition.getFieldType() + "\"");
				xmlStrZone3.append(" required=\"" + (StringUtils.isEmpty(fieldDefinition.getRequired()) ? ""
						: (("Y").equalsIgnoreCase(fieldDefinition.getRequired().substring(0, 1)) ? true : false))
						+ "\"");
				xmlStrZone3.append(" custom=\""
						+ (StringUtils.isEmpty(fieldDefinition.getCustom()) ? ""
								: (("Y").equalsIgnoreCase(fieldDefinition.getCustom().substring(0, 1)) ? true : false))
						+ "\"");
				xmlStrZone3.append(" public=\"" + (StringUtils.isEmpty(fieldDefinition.getIsPublic()) ? ""
						: (("Y").equalsIgnoreCase(fieldDefinition.getIsPublic().substring(0, 1)) ? true : false))
						+ "\"");
				xmlStrZone3.append(" readOnly=\"false\"");
				xmlStrZone3
						.append(" anonymize=\""
								+ (StringUtils
										.isEmpty(fieldDefinition.getAnonymize())
												? ""
												: (("Y").equalsIgnoreCase(
														fieldDefinition.getAnonymize().substring(0, 1)) ? true : false))
								+ "\"");
				xmlStrZone3.append(" forward-intact=\"" + (StringUtils.isEmpty(fieldDefinition.getForwardIntact()) ? ""
						: (("Y").equalsIgnoreCase(fieldDefinition.getForwardIntact().substring(0, 1)) ? true : false))
						+ "\"");
				CommonTemplateUtils.appendStringBuilderOptionalAttribute(xmlStrZone3, "sensitive",
						fieldDefinition.getSensitive(), false);
				xmlStrZone3.append(">\n");
				if (StringUtils.isNotEmpty(fieldDefinition.getFieldLabel())) {
					xmlStrZone3.append("<field-label");
					CommonTemplateUtils.appendStringBuilderOptionalAttribute(xmlStrZone3, "mime-type",
							fieldDefinition.getMimeType(), true);
					xmlStrZone3.append("><![CDATA[" + fieldDefinition.getFieldLabel() + "]]></field-label>\n");
				}
				fieldDefinition.getZone3FieldDefinitionTranslatersList().forEach(translater -> {
					xmlStrZone3.append("<field-label lang=\"" + translater.getTranslateLang() + "\"");
					CommonTemplateUtils.appendStringBuilderOptionalAttribute(xmlStrZone3, "mime-type",
							fieldDefinition.getMimeType(), true);
					xmlStrZone3.append("><![CDATA[" + translater.getTranslateLabel() + "]]>");
					xmlStrZone3.append("</field-label>\n");
				});

				if (StringUtils.isNotEmpty(fieldDefinition.getFieldDescription())) {
					xmlStrZone3.append("<field-description><![CDATA[" + fieldDefinition.getFieldDescription()
							+ "]]></field-description>\n");
				}
				fieldDefinition.getZone3FieldDefinitionTranslatersDescList().forEach(translater -> {
					xmlStrZone3.append("<field-description lang=\"" + translater.getTranslateLang() + "\"");
					xmlStrZone3.append("><![CDATA[" + translater.getTranslateLabel() + "]]>");
					xmlStrZone3.append("</field-description>\n");
				});

				if ("picklist".equalsIgnoreCase(fieldDefinition.getFieldType())) {
					if (StringUtils.isNotEmpty(fieldDefinition.getPickListOrObjectId())
							&& StringUtils.isNotEmpty(fieldDefinition.getParentPickListOrObject())) {
						xmlStrZone3
								.append("<picklist-id parent-field-id=\"" + fieldDefinition.getParentPickListOrObject()
										+ "\">" + fieldDefinition.getPickListOrObjectId() + "</picklist-id>\n");
					} else if (StringUtils.isNotEmpty(fieldDefinition.getPickListOrObjectId())) {
						xmlStrZone3
								.append("<picklist-id>" + fieldDefinition.getPickListOrObjectId() + "</picklist-id>\n");
					}
				} else if ("enum".equalsIgnoreCase(fieldDefinition.getFieldType())) {
					enumValuesList.forEach(enumObj -> {
						if (fieldDefinition.getFieldId().equalsIgnoreCase(enumObj.getEnumFieldId())) {
							xmlStrZone3.append("<enum-value value=\"" + enumObj.getEnumValue() + "\">\n"
									+ "<enum-label><![CDATA[" + enumObj.getEnumLabel() + "]]></enum-label>\n");
							enumObj.getEnumLabelTranslatersList().forEach(translater -> {
								xmlStrZone3.append("<enum-label lang=\"" + translater.getTranslateLang()
										+ "\"><![CDATA[" + translater.getEnumLabel() + "]]></enum-label>\n");
							});
							xmlStrZone3.append("</enum-value>\n");
						}
					});
				}
				xmlStrZone3.append("</field-definition>\n");
			});
		}
	}

	private static void buildXmlStrZone4(StringBuilder xmlStrZone4, Zone4DTO zone4dto, Zone2DTO zone2dto) {
		if (null != zone4dto) {
			String typeWrite = "write";
			String typeRead = "read";
			String typeExternalView = "external-view";
			zone4dto.getZone4FieldPermissionsList().forEach(permissionObj -> {
				isReadPermissionReady = false;
				isWritePermissionReady = false;
				isExternalViewPermissionReady = false;
				StringBuilder commonBuilder = new StringBuilder();
				commonBuilder.append("<role-name><![CDATA[" + permissionObj.getRoleName() + "]]></role-name>\n");

				StringBuilder readPerm = new StringBuilder();
				readPerm.append("<field-permission type=\"" + typeRead + "\">\n");
				readPerm.append("<description><![CDATA[Read permissions to " + permissionObj.getRoleName()
						+ " for following fileds]]></description>\n");
				readPerm.append(commonBuilder);

				StringBuilder writePerm = new StringBuilder();
				writePerm.append("<field-permission type=\"" + typeWrite + "\">\n");
				writePerm.append("<description><![CDATA[Write permissions to " + permissionObj.getRoleName()
						+ " for following fileds]]></description>\n");
				writePerm.append(commonBuilder);

				StringBuilder externalView = new StringBuilder();
				externalView.append("<field-permission type=\"" + typeExternalView + "\">\n");
				externalView.append("<description><![CDATA[External-View permissions to " + permissionObj.getRoleName()
						+ " for following fileds]]></description>\n");
				externalView.append(commonBuilder);

				permissionObj.getFieldPermissionsList().forEach(fieldPermission -> {
					if ("N".equalsIgnoreCase(zone2dto.getIsMultiStage().substring(0, 1))
							|| ("Y".equalsIgnoreCase(zone2dto.getIsMultiStage().substring(0, 1))
									&& ("statusId".equalsIgnoreCase(fieldPermission.getFieldId())
											|| "resume".equalsIgnoreCase(fieldPermission.getFieldId())
											|| "comments".equalsIgnoreCase(fieldPermission.getFieldId())))) {
						if (typeRead.equalsIgnoreCase(fieldPermission.getPermissionType())) {
							isReadPermissionReady = true;
							readPerm.append("<field refid=\"" + fieldPermission.getFieldId() + "\"/>\n");
						} else if (typeWrite.equalsIgnoreCase(fieldPermission.getPermissionType())) {
							isWritePermissionReady = true;
							writePerm.append("<field refid=\"" + fieldPermission.getFieldId() + "\"/>\n");
						} else if (typeExternalView.equalsIgnoreCase(fieldPermission.getPermissionType())
								&& !"comment".equalsIgnoreCase(fieldPermission.getFieldId())) {
							isExternalViewPermissionReady = true;
							externalView.append("<field refid=\"" + fieldPermission.getFieldId() + "\"/>\n");
						}
					}
				});
				readPerm.append("</field-permission>\n");
				writePerm.append("</field-permission>\n");
				externalView.append("</field-permission>\n");
				if (isReadPermissionReady)
					xmlStrZone4.append(readPerm);
				if (isWritePermissionReady)
					xmlStrZone4.append(writePerm);
				if (isExternalViewPermissionReady) {
					xmlStrZone4.append(externalView);
				}
			});
		}
	}

	private static void buildXmlStrZone5(StringBuilder xmlStrZone5, Zone5DTO zone5dto) {
		if (null != zone5dto) {
			zone5dto.getZone5FieldAttributeOverridesList().forEach(fieldAttributeOverride -> {
				isTruePermissionReady = false;
				isFalsePermissionReady = false;

				StringBuilder commonBuilder = new StringBuilder();
				commonBuilder.append("<field-attr-override>\n");
				commonBuilder.append("<override>\n");
				commonBuilder.append("<description><![CDATA[For " + fieldAttributeOverride.getAttribute().trim()
						+ " types of applicants of "
						+ ("*".equalsIgnoreCase(fieldAttributeOverride.getCountry().trim()) ? "ALL"
								: fieldAttributeOverride.getCountry().trim())
						+ "]]></description>\n");
				commonBuilder
						.append("<country><![CDATA[" + fieldAttributeOverride.getCountry().trim() + "]]></country>\n");

				StringBuilder trueBuilder = new StringBuilder();
				trueBuilder.append(commonBuilder);
				trueBuilder.append("<field-attr attribute=\"" + fieldAttributeOverride.getAttribute().trim()
						+ "\" value=\"true\" applicant=\"" + fieldAttributeOverride.getApplicant().toString()
						+ "\"/>\n");
				trueBuilder.append("</override>\n");

				StringBuilder flaseBuilder = new StringBuilder();
				flaseBuilder.append(commonBuilder);
				flaseBuilder.append("<field-attr attribute=\"" + fieldAttributeOverride.getAttribute().trim()
						+ "\" value=\"false\" applicant=\"" + fieldAttributeOverride.getApplicant().trim() + "\"/>\n");
				flaseBuilder.append("</override>\n");
				fieldAttributeOverride.getFieldOverridesList().forEach(fieldOverride -> {
					if ("Y".equalsIgnoreCase(fieldOverride.getOverrideValue().substring(0, 1))) {
						isTruePermissionReady = true;
						trueBuilder.append("<field refid=\"" + fieldOverride.getFieldId() + "\"/>\n");
					} else if ("N".equalsIgnoreCase(fieldOverride.getOverrideValue().substring(0, 1))) {
						isFalsePermissionReady = true;
						flaseBuilder.append("<field refid=\"" + fieldOverride.getFieldId() + "\"/>\n");
					}
				});
				trueBuilder.append("</field-attr-override>\n");
				flaseBuilder.append("</field-attr-override>\n");
				if (isTruePermissionReady)
					xmlStrZone5.append(trueBuilder);
				if (isFalsePermissionReady)
					xmlStrZone5.append(flaseBuilder);
			});
		}
	}

	private static void buildXmlStrZone6(StringBuilder xmlStrZone6, Zone6DTO zone6dto) {
		if (null != zone6dto) {
			zone6dto.getZone6ButtonPermissionsList().forEach(permissionObj -> {
				xmlStrZone6.append("<button-permission>\n");
				xmlStrZone6.append("<description><![CDATA[Following roles will have permission for "
						+ permissionObj.getButtonId() + "]]></description>\n");
				permissionObj.getPermittedRolesList().forEach(roleName -> {
					xmlStrZone6.append("<role-name><![CDATA[" + roleName + "]]></role-name>\n");
				});
				xmlStrZone6.append("<button-id><![CDATA[" + permissionObj.getButtonId() + "]]></button-id>\n");
				xmlStrZone6.append("</button-permission>\n");
			});
		}
	}

	private static void buildXmlStrZone7(StringBuilder xmlStrZone7, Zone7DTO zone7dto) {
		if (null != zone7dto && zone7dto.getZone7SummaryDisplayOptionsList().size() > 0) {
			xmlStrZone7.append("<candidate-summary-display-options-config>\n");
			zone7dto.getZone7SummaryDisplayOptionsList().forEach(category -> {
				xmlStrZone7.append("<category id=\"" + category.getCategoryId() + "\" name=\""
						+ category.getCategoryName() + "\">\n");
				xmlStrZone7.append("<label><![CDATA[" + category.getLabel() + "]]></label>\n");
				category.getTranslatersList().forEach(translater -> {
					xmlStrZone7.append("<label xml:lang=\"" + translater.getTranslateLang() + "\"><![CDATA["
							+ translater.getTranslateLabel() + "]]></label>\n");
				});
				category.getZone7SummaryDisplayOptionSubCategoryList().forEach(subCategory -> {
					xmlStrZone7
							.append("<column field-ref=\"" + subCategory.getFieldRefId() + "\" gridOrder=\""
									+ subCategory.getGridOrder() + "\" select-by-default=\""
									+ (StringUtils.isEmpty(subCategory.getSelectByDefault()) ? ""
											: (("Y").equalsIgnoreCase(subCategory.getSelectByDefault().substring(0, 1))
													? true
													: false))
									+ "\"/>\n");
				});
				xmlStrZone7.append("</category>\n");
			});
			xmlStrZone7.append("</candidate-summary-display-options-config>\n");
		}
	}

	public static CandidateApplicationTemplateDTO extractXmlData(MultipartFile file) {
		CandidateApplicationTemplateDTO candidateApplicationXmlData = new CandidateApplicationTemplateDTO();

		// Zone 2 objects
		Zone2DTO zone2Data = new Zone2DTO();
		List<TranslaterDTO> zone2TemplateNameTranslatersList = new ArrayList<TranslaterDTO>();
		List<TranslaterDTO> zone2TemplateDescriptionTranslatersList = new ArrayList<TranslaterDTO>();

		// Zone 3 objects
		Zone3DTO zone3Data = new Zone3DTO();
		List<Zone3FieldDefinitionDTO> zone3FieldDefinitionsList = new ArrayList<Zone3FieldDefinitionDTO>();

		// Zone 4 objects
		Zone4DTO zone4Data = new Zone4DTO();
		List<Zone4FieldPermissionDTO> zone4FieldPermissionsList = new ArrayList<Zone4FieldPermissionDTO>();

		// Zone 5 objects
		Zone5DTO zone5Data = new Zone5DTO();
		List<Zone5FieldAttributeOverrideDTO> zone5FieldAttributeOverridesList = new ArrayList<Zone5FieldAttributeOverrideDTO>();

		// Zone 6 objects
		Zone6DTO zone6Data = new Zone6DTO();
		List<Zone6ButtonPermissionDTO> zone6ButtonPermissionsList = new ArrayList<Zone6ButtonPermissionDTO>();

		// Zone 7 objects
		Zone7DTO zone7Data = new Zone7DTO();
		List<Zone7SummaryDisplayOptionDTO> zone7SummaryDisplayOptionsList = new ArrayList<Zone7SummaryDisplayOptionDTO>();

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
			NodeList n_templateNamesList = document.getElementsByTagName("template-name");
			for (int i = 0; i < n_templateNamesList.getLength(); i++) {
				Node node = n_templateNamesList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					if (StringUtils.isEmpty(element.getAttribute("lang"))) {
						zone2Data.setTemplateName(element.getTextContent());
					} else {
						TranslaterDTO tNameTranslater = new TranslaterDTO();
						tNameTranslater.setTranslateLang(element.getAttribute("lang"));
						tNameTranslater.setTranslateLabel(element.getTextContent());
						zone2TemplateNameTranslatersList.add(tNameTranslater);
					}
				}
			}
			zone2Data.setZone2TemplateNameTranslatersList(zone2TemplateNameTranslatersList);

			NodeList n_templateDescriptionsList = document.getElementsByTagName("template-desc");
			for (int i = 0; i < n_templateDescriptionsList.getLength(); i++) {
				Node node = n_templateDescriptionsList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					if (StringUtils.isEmpty(element.getAttribute("lang"))) {
						zone2Data.setTemplateDescription(element.getTextContent());
					} else {
						TranslaterDTO tNameTranslater = new TranslaterDTO();
						tNameTranslater.setTranslateLang(element.getAttribute("lang"));
						tNameTranslater.setTranslateLabel(element.getTextContent());
						zone2TemplateDescriptionTranslatersList.add(tNameTranslater);
					}
				}
			}
			zone2Data.setZone2TemplateDescriptionTranslatersList(zone2TemplateDescriptionTranslatersList);
			/* Zone2 ends here */

			/* Zone3 starts here */
			NodeList n_fieldDefinitionsList = document.getElementsByTagName("field-definition");
			for (int i = 0; i < n_fieldDefinitionsList.getLength(); i++) {
				Node node = n_fieldDefinitionsList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Zone3FieldDefinitionDTO zone3FieldDefinition = new Zone3FieldDefinitionDTO();
					Element element = (Element) node;
					zone3FieldDefinition.setFieldId(element.getAttribute("id"));
					zone3FieldDefinition.setFieldType(element.getAttribute("type"));
					zone3FieldDefinition.setRequired(element.getAttribute("required"));
					zone3FieldDefinition.setCustom(element.getAttribute("custom"));
					zone3FieldDefinition.setAnonymize(element.getAttribute("anonymize"));
					zone3FieldDefinition.setForwardIntact(element.getAttribute("forward-intact"));
					zone3FieldDefinition.setIsPublic(element.getAttribute("public"));
					zone3FieldDefinition.setSensitive(element.getAttribute("sensitive"));
					NodeList n_fieldLabelsList = element.getElementsByTagName("field-label");
					List<TranslaterDTO> zone3FieldDefinitionTranslatersList = new ArrayList<TranslaterDTO>();
					for (int j = 0; j < n_fieldLabelsList.getLength(); j++) {
						Node nodeLabel = n_fieldLabelsList.item(j);
						if (nodeLabel.getNodeType() == Node.ELEMENT_NODE) {
							Element elementLabel = (Element) nodeLabel;
							if (StringUtils.isEmpty(elementLabel.getAttribute("lang"))) {
								zone3FieldDefinition.setMimeType(elementLabel.getAttribute("mime-type"));
								zone3FieldDefinition.setFieldLabel(elementLabel.getTextContent());
							} else {
								TranslaterDTO labelTranslater = new TranslaterDTO();
								labelTranslater.setTranslateLang(elementLabel.getAttribute("lang"));
								zone3FieldDefinition.setMimeType(elementLabel.getAttribute("mime-type"));
								labelTranslater.setTranslateLabel(elementLabel.getTextContent());
								zone3FieldDefinitionTranslatersList.add(labelTranslater);
							}
						}
					}
					NodeList n_fieldDescsList = element.getElementsByTagName("field-description");
					List<TranslaterDTO> zone3FieldDefinitionTranslatersDescList = new ArrayList<TranslaterDTO>();
					for (int j = 0; j < n_fieldDescsList.getLength(); j++) {
						Node nodeDesc = n_fieldDescsList.item(j);
						if (nodeDesc.getNodeType() == Node.ELEMENT_NODE) {
							Element elementDesc = (Element) nodeDesc;
							if (StringUtils.isEmpty(elementDesc.getAttribute("lang"))) {
								zone3FieldDefinition.setFieldDescription(elementDesc.getTextContent());
							} else {
								TranslaterDTO descTranslater = new TranslaterDTO();
								descTranslater.setTranslateLang(elementDesc.getAttribute("lang"));
								descTranslater.setTranslateLabel(elementDesc.getTextContent());
								zone3FieldDefinitionTranslatersDescList.add(descTranslater);
							}
						}
					}
					if ("object".equalsIgnoreCase(zone3FieldDefinition.getFieldType())) {
						NodeList n_fieldObjectsList = element.getElementsByTagName("field-criteria");
						for (int k = 0; k < n_fieldObjectsList.getLength(); k++) {
							Node nodeFieldObject = n_fieldObjectsList.item(k);
							if (nodeFieldObject.getNodeType() == Node.ELEMENT_NODE) {
								Element elementFieldObject = (Element) nodeFieldObject;
								zone3FieldDefinition
										.setParentPickListOrObject(elementFieldObject.getAttribute("sourceFieldName"));
								zone3FieldDefinition.setPickListOrObjectId(
										elementFieldObject.getAttribute("destinationFieldValue"));
							}
						}
					} else if ("enum".equalsIgnoreCase(zone3FieldDefinition.getFieldType())) {
						NodeList n_fieldEnumsList = element.getElementsByTagName("enum-value");
						for (int k = 0; k < n_fieldEnumsList.getLength(); k++) {
							Node nodeFieldEnum = n_fieldEnumsList.item(k);
							if (nodeFieldEnum.getNodeType() == Node.ELEMENT_NODE) {
								EnumDTO enumObj = new EnumDTO();
								Element elementFieldEnum = (Element) nodeFieldEnum;
								enumObj.setEnumFieldId(zone3FieldDefinition.getFieldId());
								enumObj.setEnumFieldLabel(zone3FieldDefinition.getFieldLabel());
								enumObj.setEnumValue(elementFieldEnum.getAttribute("value"));

								NodeList n_enumLabelsList = elementFieldEnum.getElementsByTagName("enum-label");
								List<EnumLabelTranslaterDTO> enumTranslatersList = new ArrayList<EnumLabelTranslaterDTO>();
								for (int j = 0; j < n_enumLabelsList.getLength(); j++) {
									Node nodeLabel = n_enumLabelsList.item(j);
									if (nodeLabel.getNodeType() == Node.ELEMENT_NODE) {
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
					} else if ("picklist".equalsIgnoreCase(zone3FieldDefinition.getFieldType())) {
						NodeList n_fieldPickListsList = element.getElementsByTagName("picklist-id");
						for (int k = 0; k < n_fieldPickListsList.getLength(); k++) {
							Node nodeFieldPickList = n_fieldPickListsList.item(k);
							if (nodeFieldPickList.getNodeType() == Node.ELEMENT_NODE) {
								Element elementFieldPickList = (Element) nodeFieldPickList;
								zone3FieldDefinition.setParentPickListOrObject(
										elementFieldPickList.getAttribute("parent-field-id"));
								zone3FieldDefinition.setPickListOrObjectId(elementFieldPickList.getTextContent());
							}
						}
					}
					zone3FieldDefinition.setZone3FieldDefinitionTranslatersList(zone3FieldDefinitionTranslatersList);
					zone3FieldDefinition
							.setZone3FieldDefinitionTranslatersDescList(zone3FieldDefinitionTranslatersDescList);
					zone3FieldDefinitionsList.add(zone3FieldDefinition);
				}
			}
			zone3Data.setZone3FieldDefinitionsList(zone3FieldDefinitionsList);
			/* Zone3 ends here */

			/* Zone4 starts here */
			NodeList n_fieldPermissionsList = document.getElementsByTagName("field-permission");
			for (int i = 0; i < n_fieldPermissionsList.getLength(); i++) {
				Node node = n_fieldPermissionsList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					List<FieldPermissionDTO> fieldPermissionsList = new ArrayList<FieldPermissionDTO>();
					NodeList n_fieldsList = element.getElementsByTagName("field");
					for (int j = 0; j < n_fieldsList.getLength(); j++) {
						Node nodeField = n_fieldsList.item(j);
						if (nodeField.getNodeType() == Node.ELEMENT_NODE) {
							Element elementField = (Element) nodeField;
							FieldPermissionDTO fieldPermission = new FieldPermissionDTO();
							fieldPermission.setFieldId(elementField.getAttribute("refid"));
							fieldPermission.setPermissionType(element.getAttribute("type"));
							fieldPermissionsList.add(fieldPermission);
						}
					}
					NodeList n_rolesList = element.getElementsByTagName("role-name");
					for (int j = 0; j < n_rolesList.getLength(); j++) {
						Node nodeRole = n_rolesList.item(j);
						if (nodeRole.getNodeType() == Node.ELEMENT_NODE) {
							Element elementRole = (Element) nodeRole;
							Zone4FieldPermissionDTO zone4FieldPermission = new Zone4FieldPermissionDTO();
							zone4FieldPermission.setRoleName(elementRole.getTextContent());
							zone4FieldPermission.setFieldPermissionsList(fieldPermissionsList);
							zone4FieldPermissionsList.add(zone4FieldPermission);
						}
					}
				}
			}
			zone4Data.setZone4FieldPermissionsList(zone4FieldPermissionsList);
			/* Zone4 ends here */

			/* Zone5 starts here */
			NodeList n_fieldAttributeOverridesList = document.getElementsByTagName("field-attr-override");
			for (int i = 0; i < n_fieldAttributeOverridesList.getLength(); i++) {
				Node node = n_fieldAttributeOverridesList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					List<FieldOverrideDTO> fieldOverridesList = new ArrayList<>();
					NodeList n_fieldsList = element.getElementsByTagName("field");
					for (int j = 0; j < n_fieldsList.getLength(); j++) {
						Node nodeField = n_fieldsList.item(j);
						if (nodeField.getNodeType() == Node.ELEMENT_NODE) {
							Element elementField = (Element) nodeField;
							FieldOverrideDTO fieldOverride = new FieldOverrideDTO();
							fieldOverride.setFieldId(elementField.getAttribute("refid"));
							fieldOverridesList.add(fieldOverride);
						}
					}
					NodeList n_overridesList = element.getElementsByTagName("override");
					for (int j = 0; j < n_overridesList.getLength(); j++) {
						Node nodeOverride = n_overridesList.item(j);
						if (nodeOverride.getNodeType() == Node.ELEMENT_NODE) {
							Element elementOverride = (Element) nodeOverride;
							String applicant = null;
							String attribute = null;
							NodeList n_fieldAttributesList = elementOverride.getElementsByTagName("field-attr");
							for (int k = 0; k < n_fieldAttributesList.getLength(); k++) {
								Node nodeFieldAttribute = n_fieldAttributesList.item(k);
								if (nodeFieldAttribute.getNodeType() == Node.ELEMENT_NODE) {
									Element elementFieldAttribute = (Element) nodeFieldAttribute;
									applicant = elementFieldAttribute.getAttribute("applicant");
									attribute = elementFieldAttribute.getAttribute("attribute");
									fieldOverridesList.forEach(fieldOverride -> {
										fieldOverride.setOverrideValue(elementFieldAttribute.getAttribute("value"));
									});
								}
							}
							NodeList n_countriessList = elementOverride.getElementsByTagName("country");
							for (int k = 0; k < n_countriessList.getLength(); k++) {
								Node nodeCountry = n_countriessList.item(k);
								if (nodeCountry.getNodeType() == Node.ELEMENT_NODE) {
									Element elementCountry = (Element) nodeCountry;
									Zone5FieldAttributeOverrideDTO zone5FieldAttributeOverride = new Zone5FieldAttributeOverrideDTO();
									zone5FieldAttributeOverride.setCountry(elementCountry.getTextContent());
									zone5FieldAttributeOverride.setApplicant(applicant);
									zone5FieldAttributeOverride.setAttribute(attribute);
									zone5FieldAttributeOverride.setFieldOverridesList(fieldOverridesList);
									zone5FieldAttributeOverridesList.add(zone5FieldAttributeOverride);
								}
							}
						}
					}
				}
			}
			zone5Data.setZone5FieldAttributeOverridesList(zone5FieldAttributeOverridesList);
			/* Zone5 ends here */

			/* Zone6 starts here */
			NodeList n_buttonPermissionsList = document.getElementsByTagName("button-permission");
			for (int i = 0; i < n_buttonPermissionsList.getLength(); i++) {
				Node node = n_buttonPermissionsList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					List<String> permittedRolesList = new ArrayList<String>();
					NodeList n_rolesList = element.getElementsByTagName("role-name");
					for (int j = 0; j < n_rolesList.getLength(); j++) {
						Node nodeRole = n_rolesList.item(j);
						if (nodeRole.getNodeType() == Node.ELEMENT_NODE) {
							Element elementRole = (Element) nodeRole;
							permittedRolesList.add(elementRole.getTextContent());
						}
					}
					NodeList n_buttonsList = element.getElementsByTagName("button-id");
					for (int j = 0; j < n_buttonsList.getLength(); j++) {
						Node nodeButton = n_buttonsList.item(j);
						if (nodeButton.getNodeType() == Node.ELEMENT_NODE) {
							Element elementButton = (Element) nodeButton;
							Zone6ButtonPermissionDTO buttonPermissionDTO = new Zone6ButtonPermissionDTO();
							buttonPermissionDTO.setButtonId(elementButton.getTextContent());
							buttonPermissionDTO.setPermittedRolesList(permittedRolesList);
							zone6ButtonPermissionsList.add(buttonPermissionDTO);
						}
					}
				}
			}
			zone6Data.setZone6ButtonPermissionsList(zone6ButtonPermissionsList);
			/* Zone6 ends here */

			/* Zone7 starts here */
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
							Zone7SummaryDisplayOptionDTO zone7SummaryDisplayOption = new Zone7SummaryDisplayOptionDTO();
							zone7SummaryDisplayOption.setCategoryId(elementCategory.getAttribute("id"));
							zone7SummaryDisplayOption.setCategoryName(elementCategory.getAttribute("name"));
							NodeList n_LabelsList = elementCategory.getElementsByTagName("label");
							List<TranslaterDTO> translatersList = new ArrayList<TranslaterDTO>();
							for (int k = 0; k < n_LabelsList.getLength(); k++) {
								Node nodeLabel = n_LabelsList.item(k);
								if (nodeLabel.getNodeType() == Node.ELEMENT_NODE) {
									Element elementLabel = (Element) nodeLabel;
									if (StringUtils.isEmpty(elementLabel.getAttribute("xml:lang"))) {
										zone7SummaryDisplayOption.setLabel(elementLabel.getTextContent());
									} else {
										TranslaterDTO labelTranslater = new TranslaterDTO();
										labelTranslater.setTranslateLang(elementLabel.getAttribute("xml:lang"));
										labelTranslater.setTranslateLabel(elementLabel.getTextContent());
										translatersList.add(labelTranslater);
									}
								}
							}
							NodeList n_subCategoriesList = elementCategory.getElementsByTagName("column");
							List<Zone7SummaryDisplayOptionSubCategoryDTO> zone7SummaryDisplayOptionSubCategoryList = new ArrayList<Zone7SummaryDisplayOptionSubCategoryDTO>();
							for (int k = 0; k < n_subCategoriesList.getLength(); k++) {
								Node nodeSubCategory = n_subCategoriesList.item(k);
								if (nodeSubCategory.getNodeType() == Node.ELEMENT_NODE) {
									Element elementSubCategory = (Element) nodeSubCategory;
									if (k == 0) {
										zone7SummaryDisplayOption
												.setFieldRefId(elementSubCategory.getAttribute("field-ref"));
										zone7SummaryDisplayOption.setSelectByDefault(
												elementSubCategory.getAttribute("select-by-default"));
										zone7SummaryDisplayOption
												.setGridOrder(elementSubCategory.getAttribute("gridOrder"));
									} else {
										Zone7SummaryDisplayOptionSubCategoryDTO zone7SummaryDisplayOptionSubCategory = new Zone7SummaryDisplayOptionSubCategoryDTO();
										zone7SummaryDisplayOptionSubCategory
												.setFieldRefId(elementSubCategory.getAttribute("field-ref"));
										zone7SummaryDisplayOptionSubCategory.setSelectByDefault(
												elementSubCategory.getAttribute("select-by-default"));
										zone7SummaryDisplayOptionSubCategory
												.setGridOrder(elementSubCategory.getAttribute("gridOrder"));
										zone7SummaryDisplayOptionSubCategoryList
												.add(zone7SummaryDisplayOptionSubCategory);
									}
								}
							}
							zone7SummaryDisplayOption.setTranslatersList(translatersList);
							zone7SummaryDisplayOption.setZone7SummaryDisplayOptionSubCategoryList(
									zone7SummaryDisplayOptionSubCategoryList);
							zone7SummaryDisplayOptionsList.add(zone7SummaryDisplayOption);
						}
					}
				}
			}
			zone7Data.setZone7SummaryDisplayOptionsList(zone7SummaryDisplayOptionsList);
			/* Zone7 ends here */

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

		candidateApplicationXmlData.setZone2(zone2Data);
		candidateApplicationXmlData.setZone3(zone3Data);
		candidateApplicationXmlData.setZone4(zone4Data);
		candidateApplicationXmlData.setZone5(zone5Data);
		candidateApplicationXmlData.setZone6(zone6Data);
		candidateApplicationXmlData.setZone7(zone7Data);
		candidateApplicationXmlData.setEnumValuesList(enumValuesList);

		return candidateApplicationXmlData;
	}

	public static String buildExcelFile(CandidateApplicationTemplateDTO candidateApplicationTemplateXmlData,
			String fileName) {
		try {
			Resource resource = new ClassPathResource("classpath:Candidate_Application_RPA_Template.xlsx");
			InputStream inputStream = resource.getInputStream();
			//File sourceFile = new File("Candidate_Application_RPA_Template.xlsx");
			File newFile = new File(fileName + ".xlsx");
			FileUtils.copyInputStreamToFile(inputStream, newFile);
			//FileUtils.copyFile(sourceFile, destinationFile);
			XSSFWorkbook workBook;
			File destinationFile = new File(fileName + ".xlsx");
			FileInputStream destinationFileStream = new FileInputStream(destinationFile);
			workBook = new XSSFWorkbook(destinationFileStream);
			XSSFSheet appFieldsButtonsSheet = workBook.getSheet("Application_Fields&Buttons");
			XSSFSheet appOverrideSheet = workBook.getSheet("Application_Override");
			XSSFSheet summaryDisplayOptionSheet = workBook.getSheet("Summary_Display_Option");
			Set<String> translaterLangsSet = new HashSet<String>();
			Set<String> translaterDescLangsSet = new HashSet<String>();
			Set<String> translaterSSLangsSet = new HashSet<String>();
			Set<String> permRolesSet = new HashSet<String>();
			List<String> roleNamesList = new ArrayList<String>();
			Set<String> permissionsGroupSet = new HashSet<String>();
			List<String> permissionsGroupList = new ArrayList<String>();
			List<String> buttonIdsList = new ArrayList<String>();
			List<String> countriesList = new ArrayList<String>();
			List<String> applicantsList = new ArrayList<String>();
			List<String> attributesList = new ArrayList<String>();
			List<FieldLabelDTO> fieldsDefaultList = new ArrayList<>();

			HashMap<String, String> validationsLangsMap = CommonTemplateUtils.getValidationsLangsMap(workBook,
					"CDM_Validations");
			List<String> validationsLangsList = new ArrayList<String>();
			validationsLangsMap.forEach((key, value) -> {
				if (value.contains("_")) {
					validationsLangsList.add(value);
				}
			});
			List<String> validationsFieldTypesList = CommonTemplateUtils.getValidationsFieldTypesList(workBook,
					"CDM_Validations", 2);
			List<String> validationsOperatorsPermList = CommonTemplateUtils.getValidationsFieldTypesList(workBook,
					"CDM_Validations", 1);
			List<String> validationsCountriesList = CommonTemplateUtils.getValidationsFieldTypesList(workBook,
					"CDM_Validations", 3);

			CommonTemplateUtils.buildWorkBookEnumSheet(candidateApplicationTemplateXmlData.getZone2().getTemplateName(),
					candidateApplicationTemplateXmlData.getZone2().getTemplateDescription(),
					candidateApplicationTemplateXmlData.getEnumValuesList(), "Application_Enum_Values", workBook,
					validationsLangsMap, validationsLangsList);
			if (null != appFieldsButtonsSheet) {
				appFieldsButtonsSheet.getRow(2).getCell(8).setCellValue("Yes");
				candidateApplicationTemplateXmlData.getZone4().getZone4FieldPermissionsList()
						.forEach(fieldPermissionObj -> {
							if (roleNamesList.indexOf(fieldPermissionObj.getRoleName()) == -1
									&& validationsOperatorsPermList.contains(fieldPermissionObj.getRoleName()))
								roleNamesList.add(fieldPermissionObj.getRoleName());
						});
				candidateApplicationTemplateXmlData.getZone6().getZone6ButtonPermissionsList()
						.forEach(permissionObj -> {
							buttonIdsList.add(permissionObj.getButtonId());
							permissionObj.getPermittedRolesList().forEach(roleId -> {
								if (validationsOperatorsPermList.contains(roleId)) {
									permRolesSet.add(roleId);
								}
							});
						});
				permRolesSet.forEach(role -> {
					if (roleNamesList.indexOf(role) == -1)
						roleNamesList.add(role);
				});
				permRolesSet.addAll(roleNamesList);
				for (int i = 0; i < roleNamesList.size(); i++) {
					CommonTemplateUtils.createColumnFieldPermission(workBook, appFieldsButtonsSheet, 24 + i, 25 + i, 1,
							roleNamesList, i, validationsOperatorsPermList);
				}
				candidateApplicationTemplateXmlData.getZone2().getZone2TemplateNameTranslatersList()
						.forEach(translaterObj -> {
							translaterLangsSet.add(translaterObj.getTranslateLang());
						});
				candidateApplicationTemplateXmlData.getZone2().getZone2TemplateDescriptionTranslatersList()
						.forEach(translaterObj -> {
							translaterLangsSet.add(translaterObj.getTranslateLang());
						});
				candidateApplicationTemplateXmlData.getZone3().getZone3FieldDefinitionsList()
						.forEach(fieldDefinitionObj -> {
							fieldDefinitionObj.getZone3FieldDefinitionTranslatersList().forEach(translaterObj -> {
								translaterLangsSet.add(translaterObj.getTranslateLang());
							});
							fieldDefinitionObj.getZone3FieldDefinitionTranslatersDescList().forEach(translaterObj -> {
								translaterDescLangsSet.add(translaterObj.getTranslateLang());
							});
							FieldLabelDTO fieldLabelDTO = new FieldLabelDTO();
							fieldLabelDTO.setFieldId(fieldDefinitionObj.getFieldId());
							fieldLabelDTO.setLabel(fieldDefinitionObj.getFieldLabel());
							fieldsDefaultList.add(fieldLabelDTO);
						});
				List<String> translaterLangsList = new ArrayList<>(translaterLangsSet);
				int roleNamesListSize = roleNamesList.size();
				for (int i = 0; i < translaterLangsList.size(); i++) {
					CommonTemplateUtils.createColumnTransLang(workBook, appFieldsButtonsSheet,
							(29 + roleNamesListSize + i), (30 + roleNamesListSize + i), 4, validationsLangsMap,
							translaterLangsList, i);
					CommonTemplateUtils.addDataValidation255CharsAbove(workBook, appFieldsButtonsSheet, 4, 4,
							(29 + roleNamesListSize + i), (29 + roleNamesListSize + i),
							validationsLangsList.toArray(new String[0]), "CDM_Validations", "!$A$2:$A$47");
				}

				List<String> translaterDescLangsList = new ArrayList<>(translaterDescLangsSet);
				int transLangsListSize = translaterLangsList.size();
				for (int i = 0; i < translaterDescLangsList.size(); i++) {
					CommonTemplateUtils.createColumnTransLang(workBook, appFieldsButtonsSheet,
							(34 + roleNamesListSize + transLangsListSize + i),
							(35 + roleNamesListSize + transLangsListSize + i), 4, validationsLangsMap,
							translaterDescLangsList, i);
					CommonTemplateUtils.addDataValidation255CharsAbove(workBook, appFieldsButtonsSheet, 4, 4,
							(34 + roleNamesListSize + transLangsListSize + i),
							(34 + roleNamesListSize + transLangsListSize + i),
							validationsLangsList.toArray(new String[0]), "CDM_Validations", "!$A$2:$A$47");
				}

				int startRowIndex = 5;
				for (int i = 0; i < candidateApplicationTemplateXmlData.getZone3().getZone3FieldDefinitionsList()
						.size(); i++) {
					CommonTemplateUtils.createRow(workBook, appFieldsButtonsSheet, startRowIndex + i,
							(startRowIndex + 1) + i);
					fillRowDataFieldDefenition(appFieldsButtonsSheet, appFieldsButtonsSheet.getRow(startRowIndex + i),
							candidateApplicationTemplateXmlData.getZone3().getZone3FieldDefinitionsList().get(i),
							translaterLangsList, validationsFieldTypesList, workBook, roleNamesList,
							candidateApplicationTemplateXmlData.getZone4().getZone4FieldPermissionsList(),
							translaterDescLangsList);
				}
				List<String> permRolesList = new ArrayList<>(permRolesSet);
				startRowIndex = startRowIndex
						+ candidateApplicationTemplateXmlData.getZone3().getZone3FieldDefinitionsList().size() + 5;
				for (int i = 0; i < permRolesList.size(); i++) {
					CommonTemplateUtils.createColumnButtonPermission(workBook, appFieldsButtonsSheet, (5 + i), (6 + i),
							startRowIndex, appFieldsButtonsSheet.getLastRowNum());
					appFieldsButtonsSheet.getRow(startRowIndex).getCell(5 + i).setCellValue(permRolesList.get(i));
					CommonTemplateUtils.addDataValidation(appFieldsButtonsSheet, startRowIndex, startRowIndex, (5 + i),
							(5 + i), validationsOperatorsPermList.toArray(new String[0]));
				}
				startRowIndex = startRowIndex + 1;
				List<String> buttonsDefaultList = new ArrayList<>();
				buttonsDefaultList.add("Save");
				buttonsDefaultList.add("Cancel");
				buttonsDefaultList.add("Forward");
				buttonsDefaultList.add("ForwardAsApplicant");
				buttonsDefaultList.add("EDIT_CANDIDATE_SNAPSHOT");
				for (int i = 0; i < candidateApplicationTemplateXmlData.getZone6().getZone6ButtonPermissionsList()
						.size(); i++) {
					Zone6ButtonPermissionDTO zone6ButtonPermission = candidateApplicationTemplateXmlData.getZone6()
							.getZone6ButtonPermissionsList().get(i);
					if (buttonsDefaultList.indexOf(zone6ButtonPermission.getButtonId()) != -1) {
						fillRowDataButtonPermission(appFieldsButtonsSheet,
								appFieldsButtonsSheet.getRow(startRowIndex
										+ buttonsDefaultList.indexOf(zone6ButtonPermission.getButtonId())),
								zone6ButtonPermission, permRolesList);
					} else {
						CommonTemplateUtils.createRow(workBook, appFieldsButtonsSheet,
								startRowIndex + buttonsDefaultList.size(),
								(startRowIndex + 1) + buttonsDefaultList.size());
						buttonsDefaultList.add(zone6ButtonPermission.getButtonId());
						fillRowDataButtonPermission(appFieldsButtonsSheet,
								appFieldsButtonsSheet.getRow(startRowIndex
										+ buttonsDefaultList.indexOf(zone6ButtonPermission.getButtonId())),
								zone6ButtonPermission, permRolesList);
					}
				}

				appFieldsButtonsSheet.getRow(1).getCell(5)
						.setCellValue(candidateApplicationTemplateXmlData.getZone2().getTemplateName());
				candidateApplicationTemplateXmlData.getZone2().getZone2TemplateNameTranslatersList()
						.forEach(translaterObj -> {
							appFieldsButtonsSheet.getRow(1)
									.getCell(29 + roleNamesListSize
											+ translaterLangsList.indexOf(translaterObj.getTranslateLang()))
									.setCellValue(translaterObj.getTranslateLabel());
						});
				appFieldsButtonsSheet.getRow(2).getCell(5)
						.setCellValue(candidateApplicationTemplateXmlData.getZone2().getTemplateDescription());
				candidateApplicationTemplateXmlData.getZone2().getZone2TemplateDescriptionTranslatersList()
						.forEach(translaterObj -> {
							appFieldsButtonsSheet.getRow(2)
									.getCell(29 + roleNamesListSize
											+ translaterLangsList.indexOf(translaterObj.getTranslateLang()))
									.setCellValue(translaterObj.getTranslateLabel());
						});
			}
			if (null != appOverrideSheet) {
				appOverrideSheet.getRow(1).getCell(4)
						.setCellValue(candidateApplicationTemplateXmlData.getZone2().getTemplateName());
				appOverrideSheet.getRow(2).getCell(4)
						.setCellValue(candidateApplicationTemplateXmlData.getZone2().getTemplateDescription());
				candidateApplicationTemplateXmlData.getZone5().getZone5FieldAttributeOverridesList()
						.forEach(fieldAttributeOverride -> {
							if (permissionsGroupSet.add(fieldAttributeOverride.getCountry()
									+ fieldAttributeOverride.getApplicant() + fieldAttributeOverride.getAttribute())) {
								permissionsGroupList
										.add(fieldAttributeOverride.getCountry() + fieldAttributeOverride.getApplicant()
												+ fieldAttributeOverride.getAttribute());
								countriesList.add(fieldAttributeOverride.getCountry());
								applicantsList.add(fieldAttributeOverride.getApplicant());
								attributesList.add(fieldAttributeOverride.getAttribute());
							}
						});
				for (int i = 0; i < permissionsGroupSet.size(); i++) {
					CommonTemplateUtils.createColumnApplicationOverride(workBook, appOverrideSheet, 5 + i, 6 + i, 1,
							applicantsList, countriesList, attributesList, validationsCountriesList, i);
					CommonTemplateUtils.addDataValidation255CharsAbove(workBook, appOverrideSheet, 3, 3, (5 + i),
							(5 + i), validationsCountriesList.toArray(new String[0]), "CDM_Validations",
							"!$D$2:$D$225");
				}
				int startRowIndex = 6;
				for (int i = 0; i < fieldsDefaultList.size(); i++) {
					CommonTemplateUtils.createRow(workBook, appOverrideSheet, startRowIndex + i,
							(startRowIndex + 1) + i);
					fillRowDataAppOverride(appOverrideSheet, appOverrideSheet.getRow(startRowIndex + i),
							fieldsDefaultList.get(i), permissionsGroupList, workBook,
							candidateApplicationTemplateXmlData.getZone5().getZone5FieldAttributeOverridesList());
				}
			}
			if (null != summaryDisplayOptionSheet) {
				summaryDisplayOptionSheet.getRow(0).getCell(4)
						.setCellValue(candidateApplicationTemplateXmlData.getZone2().getTemplateName());
				summaryDisplayOptionSheet.getRow(1).getCell(4)
						.setCellValue(candidateApplicationTemplateXmlData.getZone2().getTemplateDescription());
				candidateApplicationTemplateXmlData.getZone7().getZone7SummaryDisplayOptionsList()
						.forEach(summaryDisplayOption -> {
							summaryDisplayOption.getTranslatersList().forEach(translaterObj -> {
								translaterSSLangsSet.add(translaterObj.getTranslateLang());
							});
						});
				List<String> translaterSSLangsList = new ArrayList<>(translaterSSLangsSet);
				for (int i = 0; i < translaterSSLangsList.size(); i++) {
					CommonTemplateUtils.createColumnTransLang(workBook, summaryDisplayOptionSheet, (14 + i), (15 + i),
							3, validationsLangsMap, translaterSSLangsList, i);
					CommonTemplateUtils.addDataValidation255CharsAbove(workBook, summaryDisplayOptionSheet, 3, 3,
							(14 + i), (14 + i), validationsLangsList.toArray(new String[0]), "CDM_Validations",
							"!$A$2:$A$47");
				}
				int startRowIndex = 4;
				for (int i = 0; i < candidateApplicationTemplateXmlData.getZone7().getZone7SummaryDisplayOptionsList()
						.size(); i++) {
					if (i > 0) {
						startRowIndex = startRowIndex
								+ candidateApplicationTemplateXmlData.getZone7().getZone7SummaryDisplayOptionsList()
										.get(i - 1).getZone7SummaryDisplayOptionSubCategoryList().size();
					}
					CommonTemplateUtils.createRow(workBook, summaryDisplayOptionSheet, startRowIndex + i,
							(startRowIndex + 1) + i);
					fillRowDataSummaryDisplayOption(summaryDisplayOptionSheet,
							summaryDisplayOptionSheet.getRow(startRowIndex + i),
							candidateApplicationTemplateXmlData.getZone7().getZone7SummaryDisplayOptionsList().get(i),
							translaterSSLangsList, workBook);
				}
			}
			destinationFileStream.close();
			// Crating output stream and writing the updated workbook
			FileOutputStream os = new FileOutputStream(destinationFile);
			workBook.write(os);
			// Close the workbook and output stream
			workBook.close();
			os.close();
		} catch (

		FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return fileName + ".xlsx";
	}

	private static void fillRowDataFieldDefenition(XSSFSheet workSheet, XSSFRow row,
			Zone3FieldDefinitionDTO zone3FieldDefinitionDTO, List<String> translaterLangsList,
			List<String> validationsFieldTypesList, XSSFWorkbook workBook, List<String> roleNamesList,
			List<Zone4FieldPermissionDTO> zone4FieldPermissionsList, List<String> translaterDescLangsList) {
		row.getCell(4).setCellValue(zone3FieldDefinitionDTO.getFieldId());
		row.getCell(5).setCellValue(zone3FieldDefinitionDTO.getFieldLabel());
		row.getCell(6).setCellValue(zone3FieldDefinitionDTO.getFieldType());
		CommonTemplateUtils.addDataValidation255CharsAbove(workBook, workSheet, row.getRowNum(), row.getRowNum(), 6, 6,
				validationsFieldTypesList.toArray(new String[0]), "CDM_Validations", "!$C$2:$C$13");
		row.getCell(7).setCellValue(zone3FieldDefinitionDTO.getMimeType());
		CommonTemplateUtils.addDataValidation(workSheet, row.getRowNum(), row.getRowNum(), 7, 7,
				new String[] { "text-plain", "text-html" });
		row.getCell(8).setCellValue(zone3FieldDefinitionDTO.getParentPickListOrObject());
		row.getCell(9).setCellValue(zone3FieldDefinitionDTO.getPickListOrObjectId());
		row.getCell(10).setCellValue(CommonTemplateUtils.convertToYesOrNo(zone3FieldDefinitionDTO.getRequired()));
		row.getCell(11).setCellValue(CommonTemplateUtils.convertToYesOrNo(zone3FieldDefinitionDTO.getCustom()));
		row.getCell(12).setCellValue(CommonTemplateUtils.convertToYesOrNo(zone3FieldDefinitionDTO.getIsPublic()));
		row.getCell(13).setCellValue(CommonTemplateUtils.convertToYesOrNo(zone3FieldDefinitionDTO.getAnonymize()));
		row.getCell(14).setCellValue(CommonTemplateUtils.convertToYesOrNo(zone3FieldDefinitionDTO.getForwardIntact()));
		row.getCell(15).setCellValue(CommonTemplateUtils.convertToYesOrNo(zone3FieldDefinitionDTO.getSensitive()));
		row.getCell(16).setCellValue(CommonTemplateUtils.convertToYesOrNo(zone3FieldDefinitionDTO.getReportable()));
		row.getCell(17).setCellValue(zone3FieldDefinitionDTO.getFieldDescription());
		zone4FieldPermissionsList.forEach(zone4FieldPermissionObj -> {
			zone4FieldPermissionObj.getFieldPermissionsList().forEach(fieldPermissionObj -> {
				if (roleNamesList.contains(zone4FieldPermissionObj.getRoleName())) {
					if (fieldPermissionObj.getFieldId().equalsIgnoreCase(zone3FieldDefinitionDTO.getFieldId())) {
						row.getCell(24 + roleNamesList.indexOf(zone4FieldPermissionObj.getRoleName()))
								.setCellValue(fieldPermissionObj.getPermissionType());
						CommonTemplateUtils.addDataValidation(workSheet, row.getRowNum(), row.getRowNum(),
								24 + roleNamesList.indexOf(zone4FieldPermissionObj.getRoleName()),
								24 + roleNamesList.indexOf(zone4FieldPermissionObj.getRoleName()),
								new String[] { "read", "write", "external-view" });
						if (!(fieldPermissionObj.getFieldId().equalsIgnoreCase("statusId")
								|| fieldPermissionObj.getFieldId().equalsIgnoreCase("comments")
								|| fieldPermissionObj.getFieldId().equalsIgnoreCase("resume"))) {
							workSheet.getRow(2).getCell(8).setCellValue("No");
						}
					}
				}
			});
		});

		int roleNamesListSize = roleNamesList.size();
		zone3FieldDefinitionDTO.getZone3FieldDefinitionTranslatersList().forEach(translaterObj -> {
			row.getCell(29 + roleNamesListSize + translaterLangsList.indexOf(translaterObj.getTranslateLang()))
					.setCellValue(translaterObj.getTranslateLabel());
			workSheet.setColumnWidth(
					29 + roleNamesListSize + translaterLangsList.indexOf(translaterObj.getTranslateLang()), 8000);
		});
		int transLangsListSize = translaterLangsList.size();
		zone3FieldDefinitionDTO.getZone3FieldDefinitionTranslatersDescList().forEach(translaterObj -> {
			row.getCell(34 + roleNamesListSize + transLangsListSize
					+ translaterDescLangsList.indexOf(translaterObj.getTranslateLang()))
					.setCellValue(translaterObj.getTranslateLabel());
			workSheet.setColumnWidth(34 + roleNamesListSize + transLangsListSize
					+ translaterDescLangsList.indexOf(translaterObj.getTranslateLang()), 8000);
		});
	}

	private static void fillRowDataButtonPermission(XSSFSheet workSheet, XSSFRow row,
			Zone6ButtonPermissionDTO zone6ButtonPermissionDTO, List<String> permRolesList) {
		row.getCell(4).setCellValue(zone6ButtonPermissionDTO.getButtonId());
		zone6ButtonPermissionDTO.getPermittedRolesList().forEach(permittedRole -> {
			if (permRolesList.contains(permittedRole)) {
				row.getCell(5 + permRolesList.indexOf(permittedRole)).setCellValue("Yes");
				CommonTemplateUtils.addDataValidation(workSheet, row.getRowNum(), row.getRowNum(),
						5 + permRolesList.indexOf(permittedRole), 5 + permRolesList.indexOf(permittedRole),
						new String[] { "Yes", "No" });
			}
		});
	}

	private static void fillRowDataSummaryDisplayOption(XSSFSheet workSheet, XSSFRow row,
			Zone7SummaryDisplayOptionDTO zone7SummaryDisplayOptionDTO, List<String> translaterSSLangsList,
			XSSFWorkbook workBook) {
		row.getCell(3).setCellValue(zone7SummaryDisplayOptionDTO.getCategoryId());
		row.getCell(4).setCellValue(zone7SummaryDisplayOptionDTO.getCategoryName());
		row.getCell(5).setCellValue(zone7SummaryDisplayOptionDTO.getLabel());
		row.getCell(6).setCellValue(zone7SummaryDisplayOptionDTO.getFieldRefId());
		row.getCell(7)
				.setCellValue(CommonTemplateUtils.convertToYesOrNo(zone7SummaryDisplayOptionDTO.getSelectByDefault()));
		CommonTemplateUtils.addDataValidation(workSheet, row.getRowNum(), row.getRowNum(), 7, 7,
				new String[] { "Y", "N" });
		row.getCell(8).setCellValue(zone7SummaryDisplayOptionDTO.getGridOrder());
		zone7SummaryDisplayOptionDTO.getTranslatersList().forEach(translaterObj -> {
			row.getCell(14 + translaterSSLangsList.indexOf(translaterObj.getTranslateLang()))
					.setCellValue(translaterObj.getTranslateLabel());
			workSheet.setColumnWidth(14 + translaterSSLangsList.indexOf(translaterObj.getTranslateLang()), 8000);
		});
		for (int i = 0; i < zone7SummaryDisplayOptionDTO.getZone7SummaryDisplayOptionSubCategoryList().size(); i++) {
			CommonTemplateUtils.createRow(workBook, workSheet, (row.getRowNum() + 1 + i), (row.getRowNum() + 2 + i));
			fillRowDataSummaryDisplayOptionSubCategory(workSheet, workSheet.getRow(row.getRowNum() + 1 + i),
					zone7SummaryDisplayOptionDTO.getZone7SummaryDisplayOptionSubCategoryList().get(i),
					translaterSSLangsList);
		}
	}

	private static void fillRowDataSummaryDisplayOptionSubCategory(XSSFSheet workSheet, XSSFRow row,
			Zone7SummaryDisplayOptionSubCategoryDTO zone7SummaryDisplayOptionSubCategoryDTO,
			List<String> translaterSSLangsList) {
		row.getCell(6).setCellValue(zone7SummaryDisplayOptionSubCategoryDTO.getFieldRefId());
		row.getCell(7).setCellValue(
				CommonTemplateUtils.convertToYesOrNo(zone7SummaryDisplayOptionSubCategoryDTO.getSelectByDefault()));
		CommonTemplateUtils.addDataValidation(workSheet, row.getRowNum(), row.getRowNum(), 7, 7,
				new String[] { "Y", "N" });
		row.getCell(8).setCellValue(zone7SummaryDisplayOptionSubCategoryDTO.getGridOrder());
	}

	private static void fillRowDataAppOverride(XSSFSheet workSheet, XSSFRow row, FieldLabelDTO fieldLabelDTO,
			List<String> permissionsGroupList, XSSFWorkbook workBook,
			List<Zone5FieldAttributeOverrideDTO> zone5FieldAttributeOverridesList) {
		row.getCell(3).setCellValue(fieldLabelDTO.getFieldId());
		row.getCell(4).setCellValue(fieldLabelDTO.getLabel());
		zone5FieldAttributeOverridesList.forEach(zone5FieldAttributeOverrideObj -> {
			zone5FieldAttributeOverrideObj.getFieldOverridesList().forEach(fieldOverrideObj -> {
				if (fieldOverrideObj.getFieldId().equalsIgnoreCase(fieldLabelDTO.getFieldId())) {
					row.getCell(5 + permissionsGroupList.indexOf(
							zone5FieldAttributeOverrideObj.getCountry() + zone5FieldAttributeOverrideObj.getApplicant()
									+ zone5FieldAttributeOverrideObj.getAttribute()))
							.setCellValue(
									CommonTemplateUtils.convertToYesOrNoLabel(fieldOverrideObj.getOverrideValue()));
					CommonTemplateUtils.addDataValidation(workSheet, row.getRowNum(), row.getRowNum(),
							5 + permissionsGroupList.indexOf(zone5FieldAttributeOverrideObj.getCountry()
									+ zone5FieldAttributeOverrideObj.getApplicant()
									+ zone5FieldAttributeOverrideObj.getAttribute()),
							5 + permissionsGroupList.indexOf(zone5FieldAttributeOverrideObj.getCountry()
									+ zone5FieldAttributeOverrideObj.getApplicant()
									+ zone5FieldAttributeOverrideObj.getAttribute()),
							new String[] { "Yes", "No" });
				}
			});
		});
	}
}
