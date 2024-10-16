package com.rpa.sapsf.utils.fileConverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.rpa.sapsf.dto.EnumDTO;
import com.rpa.sapsf.dto.EnumLabelTranslaterDTO;
import com.rpa.sapsf.dto.TranslaterDTO;

import com.rpa.sapsf.job.requisition.dto.*;

import io.micrometer.common.util.StringUtils;

public class JobRequisitionTemplateUtils {
	static boolean isReadPermissionReady = false;
	static boolean isWritePermissionReady = false;

	public static JobRequisitionTemplateDTO extractExcelData(MultipartFile file) {
		JobRequisitionTemplateDTO jobRequisitionTemplateExcelData = new JobRequisitionTemplateDTO();

		// Zone 2 objects
		Zone2DTO zone2Data = new Zone2DTO();
		List<TranslaterDTO> zone2TemplateNameTranslatersList = new ArrayList<TranslaterDTO>();
		List<TranslaterDTO> zone2TemplateDescriptionTranslatersList = new ArrayList<TranslaterDTO>();
		List<String> translateTemplateNamesList = new ArrayList<String>();
		List<String> translateTemplateDesrciptionsList = new ArrayList<String>();

		// Zone 3 objects
		Zone3DTO zone3Data = new Zone3DTO();
		List<String> supportedLanguagesList = new ArrayList<String>();

		// Zone 4 objects
		Zone4DTO zone4Data = new Zone4DTO();
		List<Zone4FieldDefinitionDTO> zone4FieldDefinitionsList = new ArrayList<Zone4FieldDefinitionDTO>();

		// Zone 5 objects
		Zone5DTO zone5Data = new Zone5DTO();
		List<Zone5FieldPermissionDTO> zone5FieldPermissionsList = new ArrayList<Zone5FieldPermissionDTO>();

		// Zone 6 objects
		Zone6DTO zone6Data = new Zone6DTO();
		List<Zone6ButtonEmailPermissionDTO> zone6ButtonEmailPermissionsList = new ArrayList<Zone6ButtonEmailPermissionDTO>();

		// Zone 7 objects
		Zone7DTO zone7Data = new Zone7DTO();

		// Zone 8 objects
		Zone8DTO zone8Data = new Zone8DTO();
		List<String> listingFieldsList = new ArrayList<String>();

		// Zone 9 objects
		Zone9DTO zone9Data = new Zone9DTO();

		// Zone 10 objects
		Zone10DTO zone10Data = new Zone10DTO();
		List<String> mobileFieldsList = new ArrayList<String>();

		// Zone 11 objects
		Zone11DTO zone11Data = new Zone11DTO();
		List<String> offerLetterFieldsList = new ArrayList<String>();

		// Zone 12 objects
		Zone12DTO zone12Data = new Zone12DTO();
		List<Zone12FieldPermissionDTO> zone12FieldPermissionsList = new ArrayList<Zone12FieldPermissionDTO>();
		List<Zone12FeaturePermissionDTO> zone12FeaturePermissionsList = new ArrayList<Zone12FeaturePermissionDTO>();

		// Zone 13 objects
		Zone13DTO zone13Data = new Zone13DTO();

		// Zone 14 objects
		Zone14DTO zone14Data = new Zone14DTO();
		List<Zone14ButtonEmailPermissionDTO> zone14ButtonEmailPermissionsList = new ArrayList<Zone14ButtonEmailPermissionDTO>();

		// Common Objects
		List<String> translateLangsList = new ArrayList<String>();
		List<String> roleNamesList = new ArrayList<String>();
		List<String> statusesList = new ArrayList<String>();
		List<String> buttonEmailRoleNamesList = new ArrayList<String>();
		List<String> multiStageStatusesList = new ArrayList<String>();
		List<String> multiStageRoleNamesList = new ArrayList<String>();
		List<String> featurePermissionTypesList = new ArrayList<String>();
		List<String> featurePermissionRoleNamesList = new ArrayList<String>();
		List<EnumDTO> enumValuesList = new ArrayList<EnumDTO>();

		try {
			XSSFWorkbook workBook;
			workBook = new XSSFWorkbook(file.getInputStream());
			XSSFSheet sheetReqFieldsPermAndTranslations = workBook.getSheet("Req_Fields_Perm_N_Translations");
			XSSFSheet sheetFPAndLanguages = workBook.getSheet("Feature_Permission_N_Languages");
			XSSFSheet sheetMultiStage = workBook.getSheet("Multi-Stage");
			XSSFRow row;
			int rowRFPNT = 0;
			int rowNumFPAL = 0;
			int rowNumMultiStage = 0;
			int translatersStartColNum = 0;
			int fieldPermissionsStartColNum = 0;
			int rownNumButtonAEmailPermissionsStart = 0;
			int rownNumSupportedLanguagesStart = 0;
			int rowNumMultiStageSheetStart = 0;
			int rowNumFPAndLanguagesSheetStart = 0;
			int rowNumReqFieldsPermAndTranslationsSheetStart = 0;
			Iterator<Row> rowsReqFieldsPermAndTranslations = null;
			Iterator<Row> rowsFPAndLanguages = null;
			Iterator<Row> rowsMultiStage = null;
			if (null != sheetReqFieldsPermAndTranslations)
				rowsReqFieldsPermAndTranslations = sheetReqFieldsPermAndTranslations.rowIterator();
			if (null != sheetFPAndLanguages)
				rowsFPAndLanguages = sheetFPAndLanguages.rowIterator();
			if (null != sheetMultiStage)
				rowsMultiStage = sheetMultiStage.rowIterator();
			CommonTemplateUtils.extractEnumData("Req_Enum_Values", workBook, enumValuesList);

			/* MultiStage starts here */
			while (null != rowsMultiStage && rowsMultiStage.hasNext()) {
				rowNumMultiStage++;
				row = (XSSFRow) rowsMultiStage.next();
				if (row.getCell(3) != null
						&& "Template Name".equalsIgnoreCase(CommonTemplateUtils.readCellValue(row.getCell(3)))) {
					rowNumMultiStageSheetStart = rowNumMultiStage;
				}
				if (rowNumMultiStageSheetStart != 0) {
					if (rowNumMultiStage == (rowNumMultiStageSheetStart + 2)) {
						int statusColNum = 5;
						while (null != row.getCell(statusColNum) && StringUtils
								.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(statusColNum)))) {
							multiStageStatusesList.add(CommonTemplateUtils.readCellValue(row.getCell(statusColNum)));
							statusColNum++;
						}
					} else if (rowNumMultiStage == (rowNumMultiStageSheetStart + 4)) {
						int roleColNum = 5;
						while (null != row.getCell(roleColNum)
								&& StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(roleColNum)))) {
							multiStageRoleNamesList.add(CommonTemplateUtils.readCellValue(row.getCell(roleColNum)));
							roleColNum++;
						}
						if (multiStageStatusesList.size() > 0) {
							for (int i = 0; i < multiStageRoleNamesList.size(); i++) {
								Zone12FieldPermissionDTO zone12FieldPermission = new Zone12FieldPermissionDTO();
								zone12FieldPermission.setStatus(multiStageStatusesList.get(i));
								if (i < multiStageRoleNamesList.size()) {
									zone12FieldPermission.setRoleName(multiStageRoleNamesList.get(i));
								}
								zone12FieldPermission.setFieldPermissionsList(new ArrayList<FieldPermissionDTO>());
								if (StringUtils.isNotEmpty(zone12FieldPermission.getRoleName())
										&& StringUtils.isNotEmpty(zone12FieldPermission.getStatus()))
									zone12FieldPermissionsList.add(zone12FieldPermission);
							}
						}
					} else if (rowNumMultiStage > (rowNumMultiStageSheetStart + 4)) {
						if (null != row.getCell(3)
								&& StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(3)))) {
							int colPermissionNum = 5;
							int index = 0;
							while (null != row.getCell(colPermissionNum)) {
								if (StringUtils
										.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colPermissionNum)))
										&& index < zone12FieldPermissionsList.size()) {
									FieldPermissionDTO fieldPermission = new FieldPermissionDTO();
									fieldPermission.setFieldId(CommonTemplateUtils.readCellValue(row.getCell(3)));
									fieldPermission.setPermissionType(CommonTemplateUtils
											.readCellValue(row.getCell(colPermissionNum)).substring(0, 1));
									zone12FieldPermissionsList.get(index).getFieldPermissionsList()
											.add(fieldPermission);
								}
								colPermissionNum++;
								index++;
							}
						}
					}
				}
			}
			/* MultiStage ends here */

			/* Feature Permissions and Supported Languages starts here */
			while (null != rowsFPAndLanguages && rowsFPAndLanguages.hasNext()) {
				rowNumFPAL++;
				row = (XSSFRow) rowsFPAndLanguages.next();
				if (row.getCell(8) != null
						&& "SUPPORTED LANGUAGES".equalsIgnoreCase(CommonTemplateUtils.readCellValue(row.getCell(8)))) {
					rownNumSupportedLanguagesStart = rowNumFPAL;
				}
				if (rownNumSupportedLanguagesStart == 0) {
					if (row.getCell(3) != null
							&& "Template Name".equalsIgnoreCase(CommonTemplateUtils.readCellValue(row.getCell(3)))) {
						rowNumFPAndLanguagesSheetStart = rowNumFPAL;
					}
					if (rowNumFPAndLanguagesSheetStart != 0) {
						if (rowNumFPAL == (rowNumFPAndLanguagesSheetStart + 4)) {
							int typeColNum = 5;
							while (null != row.getCell(typeColNum) && StringUtils
									.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(typeColNum)))) {
								featurePermissionTypesList
										.add(CommonTemplateUtils.readCellValue(row.getCell(typeColNum)).substring(
												CommonTemplateUtils.readCellValue(row.getCell(typeColNum))
														.lastIndexOf("(") + 1,
												CommonTemplateUtils.readCellValue(row.getCell(typeColNum))
														.lastIndexOf(")")));
								typeColNum++;
							}
						} else if (rowNumFPAL == (rowNumFPAndLanguagesSheetStart + 6)) {
							int roleColNum = 5;
							while (null != row.getCell(roleColNum) && StringUtils
									.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(roleColNum)))) {
								featurePermissionRoleNamesList
										.add(CommonTemplateUtils.readCellValue(row.getCell(roleColNum)));
								roleColNum++;
							}
						} else if (rowNumFPAL > (rowNumFPAndLanguagesSheetStart + 6)) {
							if (null != row.getCell(4)
									&& StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(4)))) {
								int colPermissionNum = 5;
								int index = 0;
								Map<String, List<String>> permissionRoleMap = new HashMap<String, List<String>>();
								Zone12FeaturePermissionDTO zone12FeaturePermission = new Zone12FeaturePermissionDTO();
								zone12FeaturePermission.setStatus(CommonTemplateUtils.readCellValue(row.getCell(4)));
								while (null != row.getCell(colPermissionNum)) {
									if (StringUtils.isNotEmpty(
											CommonTemplateUtils.readCellValue(row.getCell(colPermissionNum)))
											&& index < featurePermissionTypesList.size()) {
										if ("Y".equalsIgnoreCase(CommonTemplateUtils
												.readCellValue(row.getCell(colPermissionNum)).substring(0, 1))) {
											if (permissionRoleMap.containsKey(featurePermissionTypesList.get(index))) {
												List<String> roleNamesTemp = permissionRoleMap
														.get(featurePermissionTypesList.get(index));
												roleNamesTemp.add(featurePermissionRoleNamesList.get(index));
											} else {
												List<String> roleNames = new ArrayList<String>();
												roleNames.add(featurePermissionRoleNamesList.get(index));
												permissionRoleMap.put(featurePermissionTypesList.get(index), roleNames);
											}
										}
									}
									colPermissionNum++;
									index++;
								}
								zone12FeaturePermission.setPermissionRoleMap(permissionRoleMap);
								zone12FeaturePermissionsList.add(zone12FeaturePermission);
							}
						}
					}
				} else {
					if ((rowNumFPAL > rownNumSupportedLanguagesStart + 1)
							&& (null != row.getCell(4)
									&& StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(4))))
							&& (null != row.getCell(5)
									&& StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(5))))) {
						if ("Y".equalsIgnoreCase(CommonTemplateUtils.readCellValue(row.getCell(5)).substring(0, 1))) {
							supportedLanguagesList.add(CommonTemplateUtils.readCellValue(row.getCell(4)));
						}
					}
				}
			}
			/* Feature Permissions and Supported Languages ends here */

			/* Template read starts here */
			while (null != rowsReqFieldsPermAndTranslations && rowsReqFieldsPermAndTranslations.hasNext()) {
				rowRFPNT++;
				row = (XSSFRow) rowsReqFieldsPermAndTranslations.next();
				if (row.getCell(6) != null && "BUTTON AND E-MAIL PERMISSIONS"
						.equalsIgnoreCase(CommonTemplateUtils.readCellValue(row.getCell(6)))) {
					rownNumButtonAEmailPermissionsStart = rowRFPNT;
				}
				if (rownNumButtonAEmailPermissionsStart == 0) {
					if (row.getCell(6) != null
							&& "Template Name".equalsIgnoreCase(CommonTemplateUtils.readCellValue(row.getCell(6)))) {
						rowNumReqFieldsPermAndTranslationsSheetStart = rowRFPNT;
					}
					if (rowNumReqFieldsPermAndTranslationsSheetStart != 0) {
						/* Zone 2, 5, 7, 9 & 13 starts here */
						if (rowRFPNT == rowNumReqFieldsPermAndTranslationsSheetStart) {
							zone2Data.setTemplateName(CommonTemplateUtils.readCellValue(row.getCell(7)));
							zone9Data.setScaleId(CommonTemplateUtils.readCellValue(row.getCell(10)));
							zone12Data.setApplicationStatusSetName(CommonTemplateUtils.readCellValue(row.getCell(13)));
							zone7Data.setJobReqSectionName(CommonTemplateUtils.readCellValue(row.getCell(16)));
							zone2Data.setMinPostingDays(CommonTemplateUtils.readCellValue(row.getCell(19)));
							if (StringUtils.isEmpty(zone2Data.getMinPostingDays()))
								zone2Data.setMinPostingDays("0");
							for (int translatersStartColNumTemp = 8; translatersStartColNum == 0; translatersStartColNumTemp++) {
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
						} else if (rowRFPNT == (rowNumReqFieldsPermAndTranslationsSheetStart + 1)) {
							zone2Data.setTemplateDescription(CommonTemplateUtils.readCellValue(row.getCell(7)));
							zone9Data.setReverseScale(CommonTemplateUtils.readCellValue(row.getCell(10)));
							zone13Data.setApplicationTemplateName(CommonTemplateUtils.readCellValue(row.getCell(13)));
							zone7Data.setSignatureSectionName(CommonTemplateUtils.readCellValue(row.getCell(16)));
							zone2Data.setMinIntervalLeadDays(CommonTemplateUtils.readCellValue(row.getCell(19)));
							if (StringUtils.isEmpty(zone2Data.getMinIntervalLeadDays()))
								zone2Data.setMinIntervalLeadDays("0");
							int colNum = translatersStartColNum + 1;
							while (null != row.getCell(colNum)
									&& StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colNum)))) {
								translateTemplateDesrciptionsList
										.add(CommonTemplateUtils.readCellValue(row.getCell(colNum)));
								colNum++;
							}
						} else if (rowRFPNT == (rowNumReqFieldsPermAndTranslationsSheetStart + 2)) {
							for (int fieldPermissionsStartColNumTemp = 8; fieldPermissionsStartColNum == 0; fieldPermissionsStartColNumTemp++) {
								if (null != row.getCell(fieldPermissionsStartColNumTemp)
										&& StringUtils.isNotEmpty(CommonTemplateUtils
												.readCellValue(row.getCell(fieldPermissionsStartColNumTemp)))
										&& "Field Permissions".equalsIgnoreCase(CommonTemplateUtils
												.readCellValue(row.getCell(fieldPermissionsStartColNumTemp)).trim())) {
									fieldPermissionsStartColNum = fieldPermissionsStartColNumTemp;
								}
							}

							int statusColNum = fieldPermissionsStartColNum + 1;
							while (null != row.getCell(statusColNum) && StringUtils
									.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(statusColNum)))) {
								statusesList.add(CommonTemplateUtils.readCellValue(row.getCell(statusColNum)));
								statusColNum++;
							}
						} else if (rowRFPNT == (rowNumReqFieldsPermAndTranslationsSheetStart + 3)) {
							int colNum = translatersStartColNum + 1;
							while (null != row.getCell(colNum)
									&& StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colNum)))) {
								translateLangsList
										.add(CommonTemplateUtils.readCellValue(row.getCell(colNum)).substring(0, 5));
								colNum++;
							}

							int roleColNum = fieldPermissionsStartColNum + 1;
							while (null != row.getCell(roleColNum) && StringUtils
									.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(roleColNum)))) {
								roleNamesList.add(CommonTemplateUtils.readCellValue(row.getCell(roleColNum)));
								roleColNum++;
							}

							/* Zone 5 starts here */
							if (statusesList.size() > 0) {
								for (int i = 0; i < roleNamesList.size(); i++) {
									Zone5FieldPermissionDTO zone5FieldPermission = new Zone5FieldPermissionDTO();
									zone5FieldPermission.setStatus(statusesList.get(i));
									if (i < roleNamesList.size()) {
										zone5FieldPermission.setRoleName(roleNamesList.get(i));
									}
									zone5FieldPermission.setFieldPermissionsList(new ArrayList<FieldPermissionDTO>());
									if (StringUtils.isNotEmpty(zone5FieldPermission.getRoleName())
											&& StringUtils.isNotEmpty(zone5FieldPermission.getStatus()))
										zone5FieldPermissionsList.add(zone5FieldPermission);
								}
							}
							/* Zone 5 ends here */

						}
						/* Zone 2, 5, 7, 9 & 13 ends here */
						else if (rowRFPNT > (rowNumReqFieldsPermAndTranslationsSheetStart + 3)) {
							/* Zone 4 & 5 starts here */
							if (null != row.getCell(6)
									&& StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(6)))) {
								Zone4FieldDefinitionDTO zone4FieldDefinition = new Zone4FieldDefinitionDTO();
								zone4FieldDefinition.setFieldId(CommonTemplateUtils.readCellValue(row.getCell(6)));
								zone4FieldDefinition.setFieldLabel(CommonTemplateUtils.readCellValue(row.getCell(7)));
								zone4FieldDefinition.setMimeType(CommonTemplateUtils.readCellValue(row.getCell(8)));
								zone4FieldDefinition.setFieldType(CommonTemplateUtils.readCellValue(row.getCell(9)));
								zone4FieldDefinition.setGroupName(CommonTemplateUtils.readCellValue(row.getCell(10)));
								zone4FieldDefinition.setParentPickListIdOrObject(
										CommonTemplateUtils.readCellValue(row.getCell(11)));
								zone4FieldDefinition
										.setPickListOrObject(CommonTemplateUtils.readCellValue(row.getCell(12)));
								zone4FieldDefinition.setObjectType(CommonTemplateUtils.readCellValue(row.getCell(13)));
								zone4FieldDefinition.setRequired(CommonTemplateUtils.readCellValue(row.getCell(14)));
								zone4FieldDefinition.setCustom(CommonTemplateUtils.readCellValue(row.getCell(15)));
								zone4FieldDefinition.setMultiSelect(CommonTemplateUtils.readCellValue(row.getCell(16)));
								zone4FieldDefinition.setMobileField(CommonTemplateUtils.readCellValue(row.getCell(17)));
								zone4FieldDefinition
										.setJobDescriptionToken(CommonTemplateUtils.readCellValue(row.getCell(18)));
								zone4FieldDefinition
										.setOfferLetterToken(CommonTemplateUtils.readCellValue(row.getCell(19)));
								zone4FieldDefinition
										.setScreenReader(CommonTemplateUtils.readCellValue(row.getCell(20)));
								zone4FieldDefinition.setFilterField(CommonTemplateUtils.readCellValue(row.getCell(21)));
								zone4FieldDefinition.setReportable(CommonTemplateUtils.readCellValue(row.getCell(22)));
								zone4FieldDefinition
										.setCandidateSearchField(CommonTemplateUtils.readCellValue(row.getCell(23)));
								zone4FieldDefinition
										.setNotesOrComments(CommonTemplateUtils.readCellValue(row.getCell(24)));

								int colPermissionNum = fieldPermissionsStartColNum + 1;
								int indexPermissionZone4 = 0;
								while (null != row.getCell(colPermissionNum)) {
									if (StringUtils.isNotEmpty(
											CommonTemplateUtils.readCellValue(row.getCell(colPermissionNum)))
											&& indexPermissionZone4 < zone5FieldPermissionsList.size()) {
										FieldPermissionDTO fieldPermission = new FieldPermissionDTO();
										fieldPermission.setFieldId(zone4FieldDefinition.getFieldId());
										fieldPermission.setPermissionType(CommonTemplateUtils
												.readCellValue(row.getCell(colPermissionNum)).substring(0, 1));
										zone5FieldPermissionsList.get(indexPermissionZone4).getFieldPermissionsList()
												.add(fieldPermission);
									}
									colPermissionNum++;
									indexPermissionZone4++;
								}

								List<TranslaterDTO> zone4FieldDefinitionTranslatersList = new ArrayList<TranslaterDTO>();
								int colTransNum = translatersStartColNum + 1;
								int indexTransZone4 = 0;
								while (null != row.getCell(colTransNum)) {
									if (StringUtils
											.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colTransNum)))
											&& indexTransZone4 < translateLangsList.size()) {
										TranslaterDTO zone4FieldDefinitionTranslater = new TranslaterDTO();
										zone4FieldDefinitionTranslater.setTranslateLabel(
												CommonTemplateUtils.readCellValue(row.getCell(colTransNum)));
										zone4FieldDefinitionTranslater
												.setTranslateLang(translateLangsList.get(indexTransZone4));
										zone4FieldDefinitionTranslatersList.add(zone4FieldDefinitionTranslater);
									}
									colTransNum++;
									indexTransZone4++;
								}
								zone4FieldDefinition
										.setZone4FieldDefinitionTranslatersList(zone4FieldDefinitionTranslatersList);
								zone4FieldDefinitionsList.add(zone4FieldDefinition);

							}
						}
					}
					/* Zone 4 & 5 ends here */
				} else {
					/* Zone 6 starts here */
					if (rowRFPNT == rownNumButtonAEmailPermissionsStart + 2) {
						int roleColNum = 8;
						while (null != row.getCell(roleColNum)
								&& StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(roleColNum)))) {
							buttonEmailRoleNamesList.add(CommonTemplateUtils.readCellValue(row.getCell(roleColNum)));
							roleColNum++;
						}
					} else if (rowRFPNT > rownNumButtonAEmailPermissionsStart + 2) {
						if (null != row.getCell(6)
								&& StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(6)))) {
							Zone6ButtonEmailPermissionDTO zone6ButtonEmailPermission = new Zone6ButtonEmailPermissionDTO();
							zone6ButtonEmailPermission
									.setButtonEmailId(CommonTemplateUtils.readCellValue(row.getCell(6)));
							zone6ButtonEmailPermission.setPermittedRolesList(new ArrayList<String>());
							int colPermissionNum = 8;
							int index = 0;
							while (null != row.getCell(colPermissionNum)) {
								if (StringUtils
										.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colPermissionNum)))
										&& index < buttonEmailRoleNamesList.size()) {
									if ("Y".equalsIgnoreCase(CommonTemplateUtils
											.readCellValue(row.getCell(colPermissionNum)).substring(0, 1))) {
										zone6ButtonEmailPermission.getPermittedRolesList()
												.add(buttonEmailRoleNamesList.get(index));
									}
								}
								colPermissionNum++;
								index++;
							}
							if (zone6ButtonEmailPermission.getPermittedRolesList().size() > 0)
								zone6ButtonEmailPermissionsList.add(zone6ButtonEmailPermission);
						}
					}
					/* Zone 6 ends here */
				}
			}

			/* Zone 2 start here */
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

			jobRequisitionTemplateExcelData.setZone2(zone2Data);
			zone3Data.setSupportedLanguages(supportedLanguagesList);
			jobRequisitionTemplateExcelData.setZone3(zone3Data);
			zone4Data.setZone4FieldDefinitionsList(zone4FieldDefinitionsList);
			jobRequisitionTemplateExcelData.setZone4(zone4Data);
			zone5Data.setZone5FieldPermissionsList(zone5FieldPermissionsList);
			jobRequisitionTemplateExcelData.setZone5(zone5Data);
			zone6Data.setZone6ButtonEmailPermissionsList(zone6ButtonEmailPermissionsList);
			jobRequisitionTemplateExcelData.setZone6(zone6Data);
			jobRequisitionTemplateExcelData.setZone7(zone7Data);
			/* Zone 8, 10 & 11 starts here */
			zone4Data.getZone4FieldDefinitionsList().forEach(zone3FieldDefinition -> {
				if (StringUtils.isNotEmpty(zone3FieldDefinition.getMobileField())) {
					if ("Y".equalsIgnoreCase(zone3FieldDefinition.getMobileField().substring(0, 1))) {
						mobileFieldsList.add(zone3FieldDefinition.getFieldId());
					}
				}
				if (StringUtils.isNotEmpty(zone3FieldDefinition.getJobDescriptionToken())) {
					if ("Y".equalsIgnoreCase(zone3FieldDefinition.getJobDescriptionToken().substring(0, 1))) {
						listingFieldsList.add(zone3FieldDefinition.getFieldId());
					}
				}
				if (StringUtils.isNotEmpty(zone3FieldDefinition.getOfferLetterToken())) {
					if ("Y".equalsIgnoreCase(zone3FieldDefinition.getOfferLetterToken().substring(0, 1))) {
						offerLetterFieldsList.add(zone3FieldDefinition.getFieldId());
					}
				}
			});
			/* Zone 8, 10 & 11 ends here */
			zone8Data.setListingFields(listingFieldsList);
			jobRequisitionTemplateExcelData.setZone8(zone8Data);
			jobRequisitionTemplateExcelData.setZone9(zone9Data);
			zone10Data.setMobileFieldsList(mobileFieldsList);
			jobRequisitionTemplateExcelData.setZone10(zone10Data);
			zone11Data.setOfferLetterFieldsList(offerLetterFieldsList);
			jobRequisitionTemplateExcelData.setZone11(zone11Data);
			zone12Data.setZone12FieldPermissionsList(zone12FieldPermissionsList);
			zone12Data.setZone12FeaturePermissionsList(zone12FeaturePermissionsList);
			jobRequisitionTemplateExcelData.setZone12(zone12Data);
			jobRequisitionTemplateExcelData.setZone13(zone13Data);
			/* Zone 14 starts here */
			zone6Data.getZone6ButtonEmailPermissionsList().forEach(permissionObj -> {
				if (permissionObj.getButtonEmailId().contains("(candidateEmail)")) {
					Zone14ButtonEmailPermissionDTO zone14ButtonEmailPermission = new Zone14ButtonEmailPermissionDTO();
					zone14ButtonEmailPermission.setButtonEmailId(permissionObj.getButtonEmailId());
					zone14ButtonEmailPermission.setPermittedRolesList(permissionObj.getPermittedRolesList());
					zone14ButtonEmailPermissionsList.add(zone14ButtonEmailPermission);
				}
			});
			/* Zone 14 ends here */
			zone14Data.setZone14ButtonEmailPermissionsList(zone14ButtonEmailPermissionsList);
			jobRequisitionTemplateExcelData.setZone14(zone14Data);
			jobRequisitionTemplateExcelData.setEnumValuesList(enumValuesList);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return jobRequisitionTemplateExcelData;
	}

	public static String buildXmlFile(JobRequisitionTemplateDTO jobRequisitionTemplateExcelData, String fileName) {

		StringBuilder xmlStringBuilder = new StringBuilder();
		xmlStringBuilder.append("<job-req-template type=\"jobReq\" spell-check=\"false\" min-posting-days=\""
				+ jobRequisitionTemplateExcelData.getZone2().getMinPostingDays() + "\" min-internal-lead-days=\""
				+ jobRequisitionTemplateExcelData.getZone2().getMinIntervalLeadDays() + "\">\n");

		// Zone 2 starts here
		StringBuilder xmlStrZone2 = new StringBuilder();
		buildXmlStrZone2(xmlStrZone2, jobRequisitionTemplateExcelData.getZone2());
		xmlStringBuilder.append(xmlStrZone2);
		// Zone 2 ends here

		// Zone 3 starts here
		StringBuilder xmlStrZone3 = new StringBuilder();
		buildXmlStrZone3(xmlStrZone3, jobRequisitionTemplateExcelData.getZone3());
		xmlStringBuilder.append(xmlStrZone3);
		// Zone 3 ends here

		// Zone 4 starts here
		StringBuilder xmlStrZone4 = new StringBuilder();
		buildXmlStrZone4(xmlStrZone4, jobRequisitionTemplateExcelData.getZone4(),
				jobRequisitionTemplateExcelData.getEnumValuesList());
		xmlStringBuilder.append(xmlStrZone4);
		// Zone 4 ends here

		// Zone 5 starts here
		StringBuilder xmlStrZone5 = new StringBuilder();
		buildXmlStrZone5(xmlStrZone5, jobRequisitionTemplateExcelData.getZone5());
		xmlStringBuilder.append(xmlStrZone5);
		// Zone 5 ends here

		// Zone 6 starts here
		StringBuilder xmlStrZone6 = new StringBuilder();
		buildXmlStrZone6(xmlStrZone6, jobRequisitionTemplateExcelData.getZone6());
		xmlStringBuilder.append(xmlStrZone6);
		// Zone 6 ends here

		// Zone 7 starts here
		StringBuilder xmlStrZone7 = new StringBuilder();
		buildXmlStrZone7(xmlStrZone7, jobRequisitionTemplateExcelData.getZone7());
		xmlStringBuilder.append(xmlStrZone7);
		// Zone 7 ends here

		// Zone 8 starts here
		StringBuilder xmlStrZone8 = new StringBuilder();
		buildXmlStrZone8(xmlStrZone8, jobRequisitionTemplateExcelData.getZone8());
		xmlStringBuilder.append(xmlStrZone8);
		// Zone 8 ends here

		// Zone 9 starts here
		StringBuilder xmlStrZone9 = new StringBuilder();
		buildXmlStrZone9(xmlStrZone9, jobRequisitionTemplateExcelData.getZone9());
		xmlStringBuilder.append(xmlStrZone9);
		// Zone 9 ends here

		// Zone 10 starts here
		StringBuilder xmlStrZone10 = new StringBuilder();
		buildXmlStrZone10(xmlStrZone10, jobRequisitionTemplateExcelData.getZone10());
		xmlStringBuilder.append(xmlStrZone10);
		// Zone 10 ends here

		// Zone 11 starts here
		StringBuilder xmlStrZone11 = new StringBuilder();
		buildXmlStrZone11(xmlStrZone11, jobRequisitionTemplateExcelData.getZone11());
		xmlStringBuilder.append(xmlStrZone11);
		// Zone 11 ends here

		// Zone 12 starts here
		StringBuilder xmlStrZone12 = new StringBuilder();
		buildXmlStrZone12(xmlStrZone12, jobRequisitionTemplateExcelData.getZone12());
		xmlStringBuilder.append(xmlStrZone12);
		// Zone 12 ends here

		// Zone 12 starts here
		StringBuilder xmlStrZone13 = new StringBuilder();
		buildXmlStrZone13(xmlStrZone13, jobRequisitionTemplateExcelData.getZone13());
		xmlStringBuilder.append(xmlStrZone13);
		// Zone 12 ends here

		// Zone 12 starts here
		StringBuilder xmlStrZone14 = new StringBuilder();
		buildXmlStrZone14(xmlStrZone14, jobRequisitionTemplateExcelData.getZone14());
		xmlStringBuilder.append(xmlStrZone14);
		// Zone 12 ends here

		xmlStringBuilder.append("</job-req-template>");
		Document doc = CommonTemplateUtils.convertStringToDocument(xmlStringBuilder.toString());
		return CommonTemplateUtils.convertDocumentToXml(doc, fileName,
				"-//SuccessFactors, Inc.//DTD Job Requisition Template//EN", "job-req-template.dtd");
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

	private static void buildXmlStrZone3(StringBuilder xmlStrZone3, Zone3DTO zone3dto) {
		if (null != zone3dto && zone3dto.getSupportedLanguages().size() > 0) {
			xmlStrZone3.append("<supported-languages>\n");
			zone3dto.getSupportedLanguages().forEach(languageCode -> {
				xmlStrZone3.append("<lang><![CDATA[" + languageCode + "]]></lang>\n");
			});
			xmlStrZone3.append("</supported-languages>\n");
		}
	}

	private static void buildXmlStrZone4(StringBuilder xmlStrZone4, Zone4DTO zone4dto, List<EnumDTO> enumValuesList) {
		if (null != zone4dto) {
			zone4dto.getZone4FieldDefinitionsList().forEach(fieldDefinition -> {
				xmlStrZone4.append("<field-definition");
				xmlStrZone4.append(" id=\"" + fieldDefinition.getFieldId() + "\"");
				xmlStrZone4.append(" type=\"" + fieldDefinition.getFieldType() + "\"");
				if (StringUtils.isNotEmpty(fieldDefinition.getMultiSelect())
						&& "Y".equalsIgnoreCase(fieldDefinition.getMultiSelect().substring(0, 1))) {
					xmlStrZone4.append(" multiselect=\"true\"");
				}
				xmlStrZone4.append(" required=\"" + (StringUtils.isEmpty(fieldDefinition.getRequired()) ? ""
						: (("Y").equalsIgnoreCase(fieldDefinition.getRequired().substring(0, 1)) ? true : false))
						+ "\"");
				xmlStrZone4.append(" custom=\""
						+ (StringUtils.isEmpty(fieldDefinition.getCustom()) ? ""
								: (("Y").equalsIgnoreCase(fieldDefinition.getCustom().substring(0, 1)) ? true : false))
						+ "\"");
				if (("operator".equalsIgnoreCase(fieldDefinition.getFieldType())
						|| "operatorTeam".equalsIgnoreCase(fieldDefinition.getFieldType()))
						&& StringUtils.isNotEmpty(fieldDefinition.getGroupName())) {
					CommonTemplateUtils.appendStringBuilderOptionalAttribute(xmlStrZone4, "group-name",
							fieldDefinition.getGroupName(), true);
				}
				if ("Object".equalsIgnoreCase(fieldDefinition.getFieldType())) {
					xmlStrZone4.append(" object-type=\"" + fieldDefinition.getObjectType() + "\"");
				}
				xmlStrZone4.append(">\n");
				if (StringUtils.isNotEmpty(fieldDefinition.getFieldLabel())) {
					xmlStrZone4.append("<field-label");
					CommonTemplateUtils.appendStringBuilderOptionalAttribute(xmlStrZone4, "mime-type",
							fieldDefinition.getMimeType(), true);
					xmlStrZone4.append("><![CDATA[" + fieldDefinition.getFieldLabel() + "]]></field-label>\n");
				}
				fieldDefinition.getZone4FieldDefinitionTranslatersList().forEach(translater -> {
					xmlStrZone4.append("<field-label lang=\"" + translater.getTranslateLang() + "\"");
					CommonTemplateUtils.appendStringBuilderOptionalAttribute(xmlStrZone4, "mime-type",
							fieldDefinition.getMimeType(), true);
					xmlStrZone4.append("><![CDATA[" + translater.getTranslateLabel() + "]]>");
					xmlStrZone4.append("</field-label>\n");
				});

				if (StringUtils.isNotEmpty(fieldDefinition.getScreenReader())) {
					xmlStrZone4.append("<field-description><![CDATA[" + fieldDefinition.getScreenReader()
							+ "]]></field-description>\n");
				}

				if ("Object".equalsIgnoreCase(fieldDefinition.getFieldType())
						&& StringUtils.isNotEmpty(fieldDefinition.getPickListOrObject())
						&& StringUtils.isNotEmpty(fieldDefinition.getParentPickListIdOrObject())) {
					xmlStrZone4.append("<field-criteria destinationFieldValue=\""
							+ fieldDefinition.getPickListOrObject() + "\" sourceFieldName=\""
							+ fieldDefinition.getParentPickListIdOrObject() + "\"/>\n");
				} else if ("picklist".equalsIgnoreCase(fieldDefinition.getFieldType())) {
					if (StringUtils.isNotEmpty(fieldDefinition.getPickListOrObject())
							&& StringUtils.isNotEmpty(fieldDefinition.getParentPickListIdOrObject())) {
						xmlStrZone4.append(
								"<picklist-id parent-field-id=\"" + fieldDefinition.getParentPickListIdOrObject()
										+ "\">" + fieldDefinition.getPickListOrObject() + "</picklist-id>\n");
					} else if (StringUtils.isNotEmpty(fieldDefinition.getPickListOrObject())) {
						xmlStrZone4
								.append("<picklist-id>" + fieldDefinition.getPickListOrObject() + "</picklist-id>\n");
					}
				} else if ("enum".equalsIgnoreCase(fieldDefinition.getFieldType())) {
					enumValuesList.forEach(enumObj -> {
						if (fieldDefinition.getFieldId().equalsIgnoreCase(enumObj.getEnumFieldId())) {
							xmlStrZone4.append("<enum-value value=\"" + enumObj.getEnumValue() + "\">\n"
									+ "<enum-label><![CDATA[" + enumObj.getEnumLabel() + "]]></enum-label>\n");
							enumObj.getEnumLabelTranslatersList().forEach(translater -> {
								xmlStrZone4.append("<enum-label lang=\"" + translater.getTranslateLang()
										+ "\"><![CDATA[" + translater.getEnumLabel() + "]]></enum-label>\n");
							});
							xmlStrZone4.append("</enum-value>\n");
						}
					});
				}
				xmlStrZone4.append("</field-definition>\n");
			});
		}
	}

	private static void buildXmlStrZone5(StringBuilder xmlStrZone5, Zone5DTO zone5dto) {
		if (null != zone5dto) {
			String typeWrite = "write";
			String typeRead = "read";
			zone5dto.getZone5FieldPermissionsList().forEach(permissionObj -> {
				isReadPermissionReady = false;
				isWritePermissionReady = false;
				StringBuilder commonBuilder = new StringBuilder();
				commonBuilder.append("<role-name><![CDATA[" + permissionObj.getRoleName() + "]]></role-name>\n");
				commonBuilder.append("<status><![CDATA[" + permissionObj.getStatus() + "]]></status>\n");

				StringBuilder readPerm = new StringBuilder();
				readPerm.append("<field-permission type=\"" + typeRead + "\">\n");
				readPerm.append("<description><![CDATA[Read permission for " + permissionObj.getRoleName() + " in "
						+ permissionObj.getStatus() + " status]]></description>\n");
				readPerm.append(commonBuilder);

				StringBuilder writePerm = new StringBuilder();
				writePerm.append("<field-permission type=\"" + typeWrite + "\">\n");
				writePerm.append("<description><![CDATA[Write permission for " + permissionObj.getRoleName() + " in "
						+ permissionObj.getStatus() + " status]]></description>\n");
				writePerm.append(commonBuilder);

				permissionObj.getFieldPermissionsList().forEach(fieldPermission -> {
					if ("R".equalsIgnoreCase(fieldPermission.getPermissionType())) {
						isReadPermissionReady = true;
						readPerm.append("<field refid=\"" + fieldPermission.getFieldId() + "\"/>\n");
					} else if ("W".equalsIgnoreCase(fieldPermission.getPermissionType())) {
						isWritePermissionReady = true;
						writePerm.append("<field refid=\"" + fieldPermission.getFieldId() + "\"/>\n");
					}
				});
				readPerm.append("</field-permission>\n");
				writePerm.append("</field-permission>\n");
				if (isReadPermissionReady)
					xmlStrZone5.append(readPerm);
				if (isWritePermissionReady)
					xmlStrZone5.append(writePerm);
			});
		}
	}

	private static void buildXmlStrZone6(StringBuilder xmlStrZone6, Zone6DTO zone6dto) {
		if (null != zone6dto) {
			zone6dto.getZone6ButtonEmailPermissionsList().forEach(permissionObj -> {
				if (!permissionObj.getButtonEmailId().contains("(candidateEmail)")) {
					xmlStrZone6.append("<button-permission>\n");
					xmlStrZone6.append("<description><![CDATA[Following roles will have permission for " + permissionObj
							.getButtonEmailId().substring(permissionObj.getButtonEmailId().lastIndexOf("(") + 1,
									permissionObj.getButtonEmailId().lastIndexOf(")"))
							+ "]]></description>\n");
					permissionObj.getPermittedRolesList().forEach(roleName -> {
						xmlStrZone6.append("<role-name><![CDATA[" + roleName + "]]></role-name>\n");
					});
					xmlStrZone6.append("<button-id><![CDATA[" + permissionObj.getButtonEmailId().substring(
							permissionObj.getButtonEmailId().lastIndexOf("(") + 1,
							permissionObj.getButtonEmailId().lastIndexOf(")")) + "]]></button-id>\n");
					xmlStrZone6.append("</button-permission>\n");
				}
			});
		}
	}

	private static void buildXmlStrZone7(StringBuilder xmlStrZone7, Zone7DTO zone7dto) {
		if (null != zone7dto) {
			if (StringUtils.isNotEmpty(zone7dto.getJobReqSectionName()))
				xmlStrZone7.append("<job-req-section-name><![CDATA[" + zone7dto.getJobReqSectionName()
						+ "]]></job-req-section-name>\n");
			if (StringUtils.isNotEmpty(zone7dto.getSignatureSectionName()))
				xmlStrZone7.append("<signature-section-name><![CDATA[" + zone7dto.getSignatureSectionName()
						+ "]]></signature-section-name>\n");
		}
	}

	private static void buildXmlStrZone8(StringBuilder xmlStrZone8, Zone8DTO zone8dto) {
		if (null != zone8dto && zone8dto.getListingFields().size() > 0) {
			xmlStrZone8.append("<listing-fields>\n");
			zone8dto.getListingFields().forEach(listingField -> {
				xmlStrZone8.append("<field refid=\"" + listingField + "\"/>\n");
			});
			xmlStrZone8.append("</listing-fields>\n");
		}
	}

	private static void buildXmlStrZone9(StringBuilder xmlStrZone9, Zone9DTO zone9dto) {
		if (null != zone9dto) {
			xmlStrZone9.append("<assessment-scale reverse-scale=\""
					+ (StringUtils.isEmpty(zone9dto.getReverseScale()) ? ""
							: (("Y").equalsIgnoreCase(zone9dto.getReverseScale().substring(0, 1)) ? true : false))
					+ "\">\n");
			xmlStrZone9.append("<scale-id><![CDATA[" + zone9dto.getScaleId() + "]]></scale-id>\n");
			xmlStrZone9.append("</assessment-scale>\n");
		}
	}

	private static void buildXmlStrZone10(StringBuilder xmlStrZone10, Zone10DTO zone10dto) {
		if (null != zone10dto && zone10dto.getMobileFieldsList().size() > 0) {
			xmlStrZone10.append("<mobile-fields>\n");
			zone10dto.getMobileFieldsList().forEach(mobileField -> {
				xmlStrZone10.append("<field refid=\"" + mobileField + "\"/>\n");
			});
			xmlStrZone10.append("</mobile-fields>\n");
		}
	}

	private static void buildXmlStrZone11(StringBuilder xmlStrZone11, Zone11DTO zone11dto) {
		if (null != zone11dto && zone11dto.getOfferLetterFieldsList().size() > 0) {
			xmlStrZone11.append("<offer-letter-fields>\n");
			zone11dto.getOfferLetterFieldsList().forEach(offerLetterField -> {
				xmlStrZone11.append("<field refid=\"" + offerLetterField + "\"/>\n");
			});
			xmlStrZone11.append("</offer-letter-fields>\n");
		}
	}

	private static void buildXmlStrZone12(StringBuilder xmlStrZone12, Zone12DTO zone12dto) {
		if (null != zone12dto) {
			xmlStrZone12.append("<application-status-config>\n");
			if (StringUtils.isNotEmpty(zone12dto.getApplicationStatusSetName())) {
				xmlStrZone12
						.append("<application-status-set name=\"" + zone12dto.getApplicationStatusSetName() + "\"/>\n");
			}
			if (zone12dto.getZone12FieldPermissionsList().size() > 0) {
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
					writePermTemp.append(
							"<description><![CDATA[Write permission for " + permissionObj.getRoleName() + " role in "
									+ permissionObj.getStatus() + " status for following fields]]></description>\n");
					writePermTemp.append("<role-name><![CDATA[" + permissionObj.getRoleName() + "]]></role-name>\n");
					writePermTemp.append("<status><![CDATA[" + permissionObj.getStatus() + "]]></status>\n");
					permissionObj.getFieldPermissionsList().forEach(fieldPermission -> {
						if ("W".equalsIgnoreCase(fieldPermission.getPermissionType())) {
							isWritePermissionReady = true;
							writePermTemp
									.append("<field application-field-id=\"" + fieldPermission.getFieldId() + "\"/>\n");
						}
					});
					writePermTemp.append("</field-permission>\n");

					readPermTemp.append("<field-permission type=\"" + typeRead + "\">\n");
					readPermTemp.append(
							"<description><![CDATA[Read permission for " + permissionObj.getRoleName() + " role in "
									+ permissionObj.getStatus() + " status for following fields]]></description>\n");
					readPermTemp.append("<role-name><![CDATA[" + permissionObj.getRoleName() + "]]></role-name>\n");
					readPermTemp.append("<status><![CDATA[" + permissionObj.getStatus() + "]]></status>\n");
					permissionObj.getFieldPermissionsList().forEach(fieldPermission -> {
						if ("R".equalsIgnoreCase(fieldPermission.getPermissionType())) {
							isReadPermissionReady = true;
							readPermTemp
									.append("<field application-field-id=\"" + fieldPermission.getFieldId() + "\"/>\n");
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
			if (zone12dto.getZone12FeaturePermissionsList().size() > 0) {
				zone12dto.getZone12FeaturePermissionsList().forEach(permissionObj -> {
					permissionObj.getPermissionRoleMap().forEach((permissionType, roleNames) -> {
						xmlStrZone12.append("<feature-permission type=\"" + permissionType + "\">\n");
						xmlStrZone12.append("<description><![CDATA[Feature permission for " + permissionType
								+ "]]></description>\n");
						roleNames.forEach(roleName -> {
							xmlStrZone12.append("<role-name><![CDATA[" + roleName + "]]></role-name>\n");
						});
						xmlStrZone12.append("<status><![CDATA[" + permissionObj.getStatus() + "]]></status>\n");
						xmlStrZone12.append("</feature-permission>\n");
					});
				});
			}
			xmlStrZone12.append("</application-status-config>\n");
		}
	}

	private static void buildXmlStrZone13(StringBuilder xmlStrZone13, Zone13DTO zone13dto) {
		if (null != zone13dto && StringUtils.isNotEmpty(zone13dto.getApplicationTemplateName())) {
			xmlStrZone13
					.append("<application-template-name name=\"" + zone13dto.getApplicationTemplateName() + "\"/>\n");
		}
	}

	private static void buildXmlStrZone14(StringBuilder xmlStrZone14, Zone14DTO zone14dto) {
		if (null != zone14dto) {
			zone14dto.getZone14ButtonEmailPermissionsList().forEach(permissionObj -> {
				if (permissionObj.getButtonEmailId().contains("(candidateEmail)")) {
					xmlStrZone14.append("<candidate-email-permission type=\"" + permissionObj.getButtonEmailId()
							.substring(permissionObj.getButtonEmailId().lastIndexOf("(") + 1,
									permissionObj.getButtonEmailId().lastIndexOf(")"))
							+ "\">\n");
					xmlStrZone14.append(
							"<description><![CDATA[Following roles will have permission to email candidate]]></description>\n");
					permissionObj.getPermittedRolesList().forEach(roleName -> {
						xmlStrZone14.append("<role-name><![CDATA[" + roleName + "]]></role-name>\n");
					});
					xmlStrZone14.append("</candidate-email-permission>\n");
				}
			});
		}
	}

	public static JobRequisitionTemplateDTO extractXmlData(MultipartFile file) {
		JobRequisitionTemplateDTO jobRequisitionTemplateXmlData = new JobRequisitionTemplateDTO();

		// Zone 2 objects
		Zone2DTO zone2Data = new Zone2DTO();
		List<TranslaterDTO> zone2TemplateNameTranslatersList = new ArrayList<TranslaterDTO>();
		List<TranslaterDTO> zone2TemplateDescriptionTranslatersList = new ArrayList<TranslaterDTO>();

		// Zone 3 objects
		Zone3DTO zone3Data = new Zone3DTO();
		List<String> supportedLanguagesList = new ArrayList<String>();

		// Zone 4 objects
		Zone4DTO zone4Data = new Zone4DTO();
		List<Zone4FieldDefinitionDTO> zone4FieldDefinitionsList = new ArrayList<Zone4FieldDefinitionDTO>();

		// Zone 5 objects
		Zone5DTO zone5Data = new Zone5DTO();
		List<Zone5FieldPermissionDTO> zone5FieldPermissionsList = new ArrayList<Zone5FieldPermissionDTO>();

		// Zone 6 objects
		Zone6DTO zone6Data = new Zone6DTO();
		List<Zone6ButtonEmailPermissionDTO> zone6ButtonEmailPermissionsList = new ArrayList<Zone6ButtonEmailPermissionDTO>();

		// Zone 7 objects
		Zone7DTO zone7Data = new Zone7DTO();

		// Zone 8 objects
		Zone8DTO zone8Data = new Zone8DTO();
		List<String> listingFieldsList = new ArrayList<String>();

		// Zone 9 objects
		Zone9DTO zone9Data = new Zone9DTO();

		// Zone 10 objects
		Zone10DTO zone10Data = new Zone10DTO();
		List<String> mobileFieldsList = new ArrayList<String>();

		// Zone 11 objects
		Zone11DTO zone11Data = new Zone11DTO();
		List<String> offerLetterFieldsList = new ArrayList<String>();

		// Zone 12 objects
		Zone12DTO zone12Data = new Zone12DTO();
		List<Zone12FieldPermissionDTO> zone12FieldPermissionsList = new ArrayList<Zone12FieldPermissionDTO>();
		List<Zone12FeaturePermissionDTO> zone12FeaturePermissionsList = new ArrayList<Zone12FeaturePermissionDTO>();

		// Zone 13 objects
		Zone13DTO zone13Data = new Zone13DTO();

		// Zone 14 objects
		Zone14DTO zone14Data = new Zone14DTO();
		List<Zone14ButtonEmailPermissionDTO> zone14ButtonEmailPermissionsList = new ArrayList<Zone14ButtonEmailPermissionDTO>();

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
			if (null != document.getElementsByTagName("job-req-template").item(0)) {
				Element element = (Element) document.getElementsByTagName("job-req-template").item(0);
				zone2Data.setMinIntervalLeadDays(element.getAttribute("min-internal-lead-days"));
				zone2Data.setMinPostingDays(element.getAttribute("min-posting-days"));
			}
			NodeList n_TemplateNamesList = document.getElementsByTagName("template-name");
			for (int i = 0; i < n_TemplateNamesList.getLength(); i++) {
				Node node = n_TemplateNamesList.item(i);
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

			NodeList n_TemplateDescriptionsList = document.getElementsByTagName("template-desc");
			for (int i = 0; i < n_TemplateDescriptionsList.getLength(); i++) {
				Node node = n_TemplateDescriptionsList.item(i);
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
			NodeList n_supportedLangsList = document.getElementsByTagName("supported-languages");
			for (int i = 0; i < n_supportedLangsList.getLength(); i++) {
				Node node = n_supportedLangsList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					NodeList n_langsList = element.getElementsByTagName("lang");
					for (int j = 0; j < n_langsList.getLength(); j++) {
						Node nodeLang = n_langsList.item(j);
						if (nodeLang.getNodeType() == Node.ELEMENT_NODE) {
							Element elementLang = (Element) nodeLang;
							supportedLanguagesList.add(elementLang.getTextContent());
						}
					}
				}
			}
			zone3Data.setSupportedLanguages(supportedLanguagesList);
			/* Zone3 ends here */

			/* Zone4 starts here */
			NodeList n_fieldDefinitionsList = document.getElementsByTagName("field-definition");
			for (int i = 0; i < n_fieldDefinitionsList.getLength(); i++) {
				Node node = n_fieldDefinitionsList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Zone4FieldDefinitionDTO zone4FieldDefinition = new Zone4FieldDefinitionDTO();
					Element element = (Element) node;
					zone4FieldDefinition.setFieldId(element.getAttribute("id"));
					zone4FieldDefinition.setRequired(element.getAttribute("required"));
					zone4FieldDefinition.setCustom(element.getAttribute("custom"));
					zone4FieldDefinition.setFieldType(element.getAttribute("type"));
					zone4FieldDefinition.setGroupName(element.getAttribute("group-name"));
					zone4FieldDefinition.setObjectType(element.getAttribute("object-type"));
					if (null != element.getElementsByTagName("field-description").item(0)) {
						zone4FieldDefinition.setScreenReader(
								element.getElementsByTagName("field-description").item(0).getTextContent());
					}
					NodeList n_fieldLabelsList = element.getElementsByTagName("field-label");
					List<TranslaterDTO> zone4FieldDefinitionTranslatersList = new ArrayList<TranslaterDTO>();
					for (int j = 0; j < n_fieldLabelsList.getLength(); j++) {
						Node nodeLabel = n_fieldLabelsList.item(j);
						if (nodeLabel.getNodeType() == Node.ELEMENT_NODE) {
							Element elementLabel = (Element) nodeLabel;
							if (StringUtils.isEmpty(elementLabel.getAttribute("lang"))) {
								zone4FieldDefinition.setFieldLabel(elementLabel.getTextContent());
								zone4FieldDefinition.setMimeType(elementLabel.getAttribute("mime-type"));
							} else {
								TranslaterDTO labelTranslater = new TranslaterDTO();
								labelTranslater.setTranslateLang(elementLabel.getAttribute("lang"));
								zone4FieldDefinition.setMimeType(elementLabel.getAttribute("mime-type"));
								labelTranslater.setTranslateLabel(elementLabel.getTextContent());
								zone4FieldDefinitionTranslatersList.add(labelTranslater);
							}
						}
					}
					if ("object".equalsIgnoreCase(zone4FieldDefinition.getFieldType())) {
						NodeList n_fieldObjectsList = element.getElementsByTagName("field-criteria");
						for (int k = 0; k < n_fieldObjectsList.getLength(); k++) {
							Node nodeFieldObject = n_fieldObjectsList.item(k);
							if (nodeFieldObject.getNodeType() == Node.ELEMENT_NODE) {
								Element elementFieldObject = (Element) nodeFieldObject;
								zone4FieldDefinition.setParentPickListIdOrObject(
										elementFieldObject.getAttribute("sourceFieldName"));
								zone4FieldDefinition
										.setPickListOrObject(elementFieldObject.getAttribute("destinationFieldValue"));
							}
						}
					} else if ("enum".equalsIgnoreCase(zone4FieldDefinition.getFieldType())) {
						NodeList n_fieldEnumsList = element.getElementsByTagName("enum-value");
						for (int k = 0; k < n_fieldEnumsList.getLength(); k++) {
							Node nodeFieldEnum = n_fieldEnumsList.item(k);
							if (nodeFieldEnum.getNodeType() == Node.ELEMENT_NODE) {
								EnumDTO enumObj = new EnumDTO();
								Element elementFieldEnum = (Element) nodeFieldEnum;
								enumObj.setEnumFieldId(zone4FieldDefinition.getFieldId());
								enumObj.setEnumFieldLabel(zone4FieldDefinition.getFieldLabel());
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
					} else if ("picklist".equalsIgnoreCase(zone4FieldDefinition.getFieldType())) {
						NodeList n_fieldPickListsList = element.getElementsByTagName("picklist-id");
						for (int k = 0; k < n_fieldPickListsList.getLength(); k++) {
							Node nodeFieldPickList = n_fieldPickListsList.item(k);
							if (nodeFieldPickList.getNodeType() == Node.ELEMENT_NODE) {
								Element elementFieldPickList = (Element) nodeFieldPickList;
								zone4FieldDefinition.setParentPickListIdOrObject(
										elementFieldPickList.getAttribute("parent-field-id"));
								zone4FieldDefinition.setPickListOrObject(elementFieldPickList.getTextContent());
							}
						}
					}

					zone4FieldDefinition.setZone4FieldDefinitionTranslatersList(zone4FieldDefinitionTranslatersList);
					zone4FieldDefinitionsList.add(zone4FieldDefinition);
				}
			}
			zone4Data.setZone4FieldDefinitionsList(zone4FieldDefinitionsList);
			/* Zone4 ends here */

			/* Zone5 starts here */
			NodeList n_fieldPermissionsList = document.getElementsByTagName("field-permission");
			for (int i = 0; i < n_fieldPermissionsList.getLength(); i++) {
				Node node = n_fieldPermissionsList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE
						&& node.getParentNode().toString().contains("job-req-template")) {
					Element element = (Element) node;
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
					NodeList n_rolesList = element.getElementsByTagName("role-name");
					for (int j = 0; j < n_rolesList.getLength(); j++) {
						Node nodeRole = n_rolesList.item(j);
						if (nodeRole.getNodeType() == Node.ELEMENT_NODE) {
							Element elementRole = (Element) nodeRole;
							Zone5FieldPermissionDTO zone5FieldPermission = new Zone5FieldPermissionDTO();
							zone5FieldPermission.setRoleName(elementRole.getTextContent());
							if (null != element.getElementsByTagName("status").item(0)) {
								zone5FieldPermission
										.setStatus(element.getElementsByTagName("status").item(0).getTextContent());
							}
							zone5FieldPermission.setFieldPermissionsList(fieldPermissionsList);
							if (!"J".equalsIgnoreCase(zone5FieldPermission.getRoleName().trim())) {
								zone5FieldPermissionsList.add(zone5FieldPermission);
							}
						}
					}
				}
			}
			zone5Data.setZone5FieldPermissionsList(zone5FieldPermissionsList);
			/* Zone5 ends here */

			/* Zone6 starts here */
			NodeList n_buttonPermissionsList = document.getElementsByTagName("button-permission");
			for (int i = 0; i < n_buttonPermissionsList.getLength(); i++) {
				Node node = n_buttonPermissionsList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					NodeList n_rolesList = element.getElementsByTagName("role-name");
					List<String> permittedRolesList = new ArrayList<String>();
					for (int j = 0; j < n_rolesList.getLength(); j++) {
						Node nodeRole = n_rolesList.item(j);
						if (node.getNodeType() == Node.ELEMENT_NODE) {
							Element elementRole = (Element) nodeRole;
							permittedRolesList.add(elementRole.getTextContent());
						}
					}
					NodeList n_buttonsList = element.getElementsByTagName("button-id");
					for (int j = 0; j < n_buttonsList.getLength(); j++) {
						Node nodeButton = n_buttonsList.item(j);
						if (nodeButton.getNodeType() == Node.ELEMENT_NODE) {
							Element elementButton = (Element) nodeButton;
							Zone6ButtonEmailPermissionDTO buttonEmailPermissionDTO = new Zone6ButtonEmailPermissionDTO();
							buttonEmailPermissionDTO.setButtonEmailId(elementButton.getTextContent());
							buttonEmailPermissionDTO.setPermittedRolesList(permittedRolesList);
							zone6ButtonEmailPermissionsList.add(buttonEmailPermissionDTO);
						}
					}
				}
			}
			zone6Data.setZone6ButtonEmailPermissionsList(zone6ButtonEmailPermissionsList);
			/* Zone6 ends here */

			/* Zone7 starts here */
			if (null != document.getElementsByTagName("job-req-section-name").item(0)) {
				zone7Data.setJobReqSectionName(
						document.getElementsByTagName("job-req-section-name").item(0).getTextContent());
			}
			if (null != document.getElementsByTagName("signature-section-name").item(0)) {
				zone7Data.setSignatureSectionName(
						document.getElementsByTagName("signature-section-name").item(0).getTextContent());
			}
			/* Zone7 ends here */

			/* Zone8 starts here */
			NodeList n_listingFieldsList = document.getElementsByTagName("listing-fields");
			for (int i = 0; i < n_listingFieldsList.getLength(); i++) {
				Node node = n_listingFieldsList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					NodeList n_fieldsList = element.getElementsByTagName("field");
					for (int j = 0; j < n_fieldsList.getLength(); j++) {
						Node nodeField = n_fieldsList.item(j);
						if (nodeField.getNodeType() == Node.ELEMENT_NODE) {
							Element elementField = (Element) nodeField;
							listingFieldsList.add(elementField.getAttribute("refid"));
						}
					}
				}
			}
			zone8Data.setListingFields(listingFieldsList);
			/* Zone8 ends here */

			/* Zone9 starts here */
			if (null != document.getElementsByTagName("assessment-scale").item(0)) {
				Element element = (Element) document.getElementsByTagName("assessment-scale").item(0);
				zone9Data.setReverseScale(element.getAttribute("reverse-scale"));
				if (null != document.getElementsByTagName("scale-id").item(0)) {
					Element elementScaleId = (Element) document.getElementsByTagName("scale-id").item(0);
					zone9Data.setScaleId(elementScaleId.getTextContent());
				}
			}
			/* Zone9 ends here */

			/* Zone10 starts here */
			NodeList n_mobileFieldsList = document.getElementsByTagName("mobile-fields");
			for (int i = 0; i < n_mobileFieldsList.getLength(); i++) {
				Node node = n_mobileFieldsList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					NodeList n_fieldsList = element.getElementsByTagName("field");
					for (int j = 0; j < n_fieldsList.getLength(); j++) {
						Node nodeField = n_fieldsList.item(j);
						if (nodeField.getNodeType() == Node.ELEMENT_NODE) {
							Element elementField = (Element) nodeField;
							mobileFieldsList.add(elementField.getAttribute("refid"));
						}
					}
				}
			}
			zone10Data.setMobileFieldsList(mobileFieldsList);
			/* Zone10 ends here */

			/* Zone11 starts here */
			NodeList n_offerLetterFieldsList = document.getElementsByTagName("offer-letter-fields");
			for (int i = 0; i < n_offerLetterFieldsList.getLength(); i++) {
				Node node = n_offerLetterFieldsList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					NodeList n_fieldsList = element.getElementsByTagName("field");
					for (int j = 0; j < n_fieldsList.getLength(); j++) {
						Node nodeField = n_fieldsList.item(j);
						if (nodeField.getNodeType() == Node.ELEMENT_NODE) {
							Element elementField = (Element) nodeField;
							offerLetterFieldsList.add(elementField.getAttribute("refid"));
						}
					}
				}
			}
			zone11Data.setOfferLetterFieldsList(offerLetterFieldsList);
			/* Zone11 ends here */

			/* Zone12 starts here */
			if (null != document.getElementsByTagName("application-status-config").item(0)) {
				Element element = (Element) document.getElementsByTagName("application-status-config").item(0);
				if (null != element.getElementsByTagName("application-status-set").item(0)) {
					Element elementStatusSet = (Element) element.getElementsByTagName("application-status-set").item(0);
					zone12Data.setApplicationStatusSetName(elementStatusSet.getAttribute("name"));
				}
				NodeList n_fieldPermissionsSetConfigList = element.getElementsByTagName("field-permission");
				for (int i = 0; i < n_fieldPermissionsSetConfigList.getLength(); i++) {
					Node node = n_fieldPermissionsSetConfigList.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						Element elementFieldPerm = (Element) node;
						Zone12FieldPermissionDTO zone12FieldPermission = new Zone12FieldPermissionDTO();
						if (null != elementFieldPerm.getElementsByTagName("role-name").item(0))
							zone12FieldPermission.setRoleName(
									elementFieldPerm.getElementsByTagName("role-name").item(0).getTextContent());
						if (null != elementFieldPerm.getElementsByTagName("status").item(0))
							zone12FieldPermission.setStatus(
									elementFieldPerm.getElementsByTagName("status").item(0).getTextContent());
						List<FieldPermissionDTO> fieldPermissionsList = new ArrayList<FieldPermissionDTO>();
						NodeList n_fieldsList = elementFieldPerm.getElementsByTagName("field");
						for (int j = 0; j < n_fieldsList.getLength(); j++) {
							Node nodeField = n_fieldsList.item(j);
							if (nodeField.getNodeType() == Node.ELEMENT_NODE) {
								Element elementField = (Element) nodeField;
								FieldPermissionDTO fieldPermission = new FieldPermissionDTO();
								fieldPermission.setFieldId(elementField.getAttribute("application-field-id"));
								fieldPermission.setPermissionType(
										elementFieldPerm.getAttribute("type").substring(0, 1).toUpperCase());
								fieldPermissionsList.add(fieldPermission);
							}
						}
						zone12FieldPermission.setFieldPermissionsList(fieldPermissionsList);
						zone12FieldPermissionsList.add(zone12FieldPermission);
					}
				}

				NodeList n_featurePermissionsSetConfigList = element.getElementsByTagName("feature-permission");
				for (int i = 0; i < n_featurePermissionsSetConfigList.getLength(); i++) {
					Node node = n_featurePermissionsSetConfigList.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						Element elementFeaturePerm = (Element) node;
						Zone12FeaturePermissionDTO zone12FeaturePermission = new Zone12FeaturePermissionDTO();
						if (null != elementFeaturePerm.getElementsByTagName("status").item(0))
							zone12FeaturePermission.setStatus(
									elementFeaturePerm.getElementsByTagName("status").item(0).getTextContent());
						NodeList n_rolesList = elementFeaturePerm.getElementsByTagName("role-name");
						List<String> rolesList = new ArrayList<String>();
						for (int j = 0; j < n_rolesList.getLength(); j++) {
							Node nodeRole = n_rolesList.item(j);
							if (nodeRole.getNodeType() == Node.ELEMENT_NODE) {
								Element elementRole = (Element) nodeRole;
								rolesList.add(elementRole.getTextContent());
							}
						}
						Map<String, List<String>> permissionRoleMap = new HashMap<String, List<String>>();
						permissionRoleMap.put(elementFeaturePerm.getAttribute("type"), rolesList);
						zone12FeaturePermission.setPermissionRoleMap(permissionRoleMap);
						zone12FeaturePermissionsList.add(zone12FeaturePermission);
					}
				}
			}
			zone12Data.setZone12FieldPermissionsList(zone12FieldPermissionsList);
			zone12Data.setZone12FeaturePermissionsList(zone12FeaturePermissionsList);
			/* Zone12 ends here */

			/* Zone13 starts here */
			if (null != document.getElementsByTagName("application-template-name").item(0)) {
				Element element = (Element) document.getElementsByTagName("application-template-name").item(0);
				zone13Data.setApplicationTemplateName(element.getAttribute("name"));
			}
			/* Zone13 ends here */

			/* Zone14 starts here */
			NodeList n_emailPermissionsList = document.getElementsByTagName("candidate-email-permission");
			for (int i = 0; i < n_emailPermissionsList.getLength(); i++) {
				Node node = n_emailPermissionsList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					Zone14ButtonEmailPermissionDTO buttonEmailPermissionDTO = new Zone14ButtonEmailPermissionDTO();
					buttonEmailPermissionDTO.setButtonEmailId(element.getAttribute("type"));
					buttonEmailPermissionDTO.setPermittedRolesList(new ArrayList<String>());
					NodeList n_rolesList = element.getElementsByTagName("role-name");
					for (int j = 0; j < n_rolesList.getLength(); j++) {
						Node nodeRole = n_rolesList.item(j);
						if (node.getNodeType() == Node.ELEMENT_NODE) {
							Element elementRole = (Element) nodeRole;
							buttonEmailPermissionDTO.getPermittedRolesList().add(elementRole.getTextContent());
						}
					}
					zone14ButtonEmailPermissionsList.add(buttonEmailPermissionDTO);
				}
			}
			zone14Data.setZone14ButtonEmailPermissionsList(zone14ButtonEmailPermissionsList);
			/* Zone14 ends here */

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
		zone8Data.getListingFields().forEach(fieldObj -> {
			zone4Data.getZone4FieldDefinitionsList().forEach(fieldDef -> {
				if (fieldObj.equalsIgnoreCase(fieldDef.getFieldId())) {
					fieldDef.setJobDescriptionToken("true");
				}
			});
		});
		zone10Data.getMobileFieldsList().forEach(fieldObj -> {
			zone4Data.getZone4FieldDefinitionsList().forEach(fieldDef -> {
				if (fieldObj.equalsIgnoreCase(fieldDef.getFieldId())) {
					fieldDef.setMobileField("true");
				}
			});
		});
		zone11Data.getOfferLetterFieldsList().forEach(fieldObj -> {
			zone4Data.getZone4FieldDefinitionsList().forEach(fieldDef -> {
				if (fieldObj.equalsIgnoreCase(fieldDef.getFieldId())) {
					fieldDef.setOfferLetterToken("true");
				}
			});
		});
		jobRequisitionTemplateXmlData.setZone2(zone2Data);
		jobRequisitionTemplateXmlData.setZone3(zone3Data);
		jobRequisitionTemplateXmlData.setZone4(zone4Data);
		jobRequisitionTemplateXmlData.setZone5(zone5Data);
		jobRequisitionTemplateXmlData.setZone6(zone6Data);
		jobRequisitionTemplateXmlData.setZone7(zone7Data);
		jobRequisitionTemplateXmlData.setZone8(zone8Data);
		jobRequisitionTemplateXmlData.setZone9(zone9Data);
		jobRequisitionTemplateXmlData.setZone10(zone10Data);
		jobRequisitionTemplateXmlData.setZone11(zone11Data);
		jobRequisitionTemplateXmlData.setZone12(zone12Data);
		jobRequisitionTemplateXmlData.setZone13(zone13Data);
		jobRequisitionTemplateXmlData.setZone14(zone14Data);
		jobRequisitionTemplateXmlData.setEnumValuesList(enumValuesList);

		return jobRequisitionTemplateXmlData;
	}

	public static String buildExcelFile(JobRequisitionTemplateDTO jobRequisitionTemplateXmlData, String fileName) {
		File sourceFile = new File(
				"src\\main\\resources\\templates\\REC_Templates\\Job-Req_Approval_RPA_Template.xlsx");
		File destinationFile = new File("src\\main\\resources\\templates\\" + fileName + ".xlsx");
		try {
			FileUtils.copyFile(sourceFile, destinationFile);
			XSSFWorkbook workBook;
			FileInputStream destinationFileStream = new FileInputStream(destinationFile);
			workBook = new XSSFWorkbook(destinationFileStream);
			XSSFSheet regFieldsPermTransSheet = workBook.getSheet("Req_Fields_Perm_N_Translations");
			XSSFSheet featurePermLangsSheet = workBook.getSheet("Feature_Permission_N_Languages");
			XSSFSheet multiStageSheet = workBook.getSheet("Multi-Stage");
			Set<String> translaterLangsSet = new HashSet<String>();
			Set<String> jrdmRolesSet = new HashSet<String>();
			List<String> roleNamesList = new ArrayList<String>();
			List<String> statusesList = new ArrayList<String>();
			Set<String> permissionsGroupSet = new HashSet<String>();
			List<String> permissionsGroupList = new ArrayList<String>();
			List<String> multiStageFieldIdsList = new ArrayList<String>();
			List<String> featurePermissionIdsList = new ArrayList<String>();
			List<String> featuresList = new ArrayList<String>();

			HashMap<String, String> validationsLangsMap = CommonTemplateUtils.getValidationsLangsMap(workBook,
					"JRDM_Validations");
			List<String> validationsLangsList = new ArrayList<String>();
			validationsLangsMap.forEach((key, value) -> {
				if (value.contains("_")) {
					validationsLangsList.add(value);
				}
			});
			List<String> validationsFieldTypesList = CommonTemplateUtils.getValidationsFieldTypesList(workBook,
					"JRDM_Validations", 3);
			List<String> validationsJrdmRolesList = CommonTemplateUtils.getValidationsFieldTypesList(workBook,
					"JRDM_Validations", 1);
			List<String> validationsFeatureRolesList = CommonTemplateUtils.getValidationsFieldTypesList(workBook,
					"JRDM_Validations", 2);
			List<String> validationsFeaturesList = CommonTemplateUtils.getValidationsFieldTypesList(workBook,
					"JRDM_Validations", 4);

			CommonTemplateUtils.buildWorkBookEnumSheet(jobRequisitionTemplateXmlData.getZone2().getTemplateName(),
					jobRequisitionTemplateXmlData.getZone2().getTemplateDescription(),
					jobRequisitionTemplateXmlData.getEnumValuesList(), "Req_Enum_Values", workBook, validationsLangsMap,
					validationsLangsList);
			if (null != featurePermLangsSheet) {
				permissionsGroupList.clear();
				permissionsGroupSet.clear();
				statusesList.clear();
				roleNamesList.clear();
				featurePermLangsSheet.getRow(1).getCell(4)
						.setCellValue(jobRequisitionTemplateXmlData.getZone2().getTemplateName());
				featurePermLangsSheet.getRow(2).getCell(4)
						.setCellValue(jobRequisitionTemplateXmlData.getZone2().getTemplateDescription());
				jobRequisitionTemplateXmlData.getZone12().getZone12FeaturePermissionsList()
						.forEach(featurePermissionObj -> {
							if (featurePermissionIdsList.indexOf(featurePermissionObj.getStatus()) == -1)
								featurePermissionIdsList.add(featurePermissionObj.getStatus());
							featurePermissionObj.getPermissionRoleMap().forEach((feature, roles) -> {
								roles.forEach(role -> {
									if (permissionsGroupSet.add(feature + role)) {
										permissionsGroupList.add(feature + role);
										featuresList.add(feature);
										roleNamesList.add(role);
									}
								});
							});
						});
				int startRowIndex = 5;
				for (int i = 0; i < featuresList.size(); i++) {
					int index = i;
					int rowIndex = startRowIndex;
					CommonTemplateUtils.createColumnButtonPermission(workBook, featurePermLangsSheet, (5 + i), (6 + i),
							startRowIndex, startRowIndex + 4);
					validationsFeaturesList.forEach(feature -> {
						if (feature.contains("(" + featuresList.get(index) + ")")) {
							featurePermLangsSheet.getRow(rowIndex).getCell(5 + index).setCellValue(feature);
						}
					});
					CommonTemplateUtils.addDataValidation255CharsAbove(workBook, featurePermLangsSheet, startRowIndex,
							startRowIndex, 5 + i, 5 + i, validationsFeaturesList.toArray(new String[0]),
							"JRDM_Validations", "!$E$2:$E$12");
				}
				startRowIndex = 7;
				for (int i = 0; i < roleNamesList.size(); i++) {
					featurePermLangsSheet.getRow(startRowIndex).getCell(5 + i).setCellValue(roleNamesList.get(i));
					CommonTemplateUtils.addDataValidation(featurePermLangsSheet, startRowIndex, startRowIndex, 5 + i,
							5 + i, validationsFeatureRolesList.toArray(new String[0]));
				}
				startRowIndex = 8;
				for (int i = 0; i < featurePermissionIdsList.size(); i++) {
					CommonTemplateUtils.createRow(workBook, featurePermLangsSheet, startRowIndex + i,
							(startRowIndex + 1) + i);
					fillRowDataFeaturePermission(featurePermLangsSheet, featurePermLangsSheet.getRow(startRowIndex + i),
							featurePermissionIdsList.get(i), permissionsGroupList, workBook, roleNamesList,
							featuresList, jobRequisitionTemplateXmlData.getZone12().getZone12FeaturePermissionsList());
				}
				startRowIndex = startRowIndex + featurePermissionIdsList.size() + 7;
				for (int i = 0; i < jobRequisitionTemplateXmlData.getZone3().getSupportedLanguages().size(); i++) {
					if (validationsLangsList.indexOf(validationsLangsMap.get(
							jobRequisitionTemplateXmlData.getZone3().getSupportedLanguages().get(i).trim())) != -1) {
						fillRowDataSupportedLanguage(featurePermLangsSheet, featurePermLangsSheet
								.getRow(startRowIndex + validationsLangsList.indexOf(validationsLangsMap.get(
										jobRequisitionTemplateXmlData.getZone3().getSupportedLanguages().get(i)))));
					}
				}
			}
			if (null != multiStageSheet) {
				permissionsGroupList.clear();
				permissionsGroupSet.clear();
				statusesList.clear();
				roleNamesList.clear();
				multiStageSheet.getRow(1).getCell(4)
						.setCellValue(jobRequisitionTemplateXmlData.getZone2().getTemplateName());
				multiStageSheet.getRow(2).getCell(4)
						.setCellValue(jobRequisitionTemplateXmlData.getZone2().getTemplateDescription());
				jobRequisitionTemplateXmlData.getZone12().getZone12FieldPermissionsList()
						.forEach(fieldPermissionObj -> {
							fieldPermissionObj.getFieldPermissionsList().forEach(fieldObj -> {
								if (multiStageFieldIdsList.indexOf(fieldObj.getFieldId()) == -1)
									multiStageFieldIdsList.add(fieldObj.getFieldId());
							});
							if (permissionsGroupSet
									.add(fieldPermissionObj.getStatus() + fieldPermissionObj.getRoleName())) {
								permissionsGroupList
										.add(fieldPermissionObj.getStatus() + fieldPermissionObj.getRoleName());
								statusesList.add(fieldPermissionObj.getStatus());
								roleNamesList.add(fieldPermissionObj.getRoleName());
							}
						});
				for (int i = 0; i < permissionsGroupSet.size(); i++) {
					CommonTemplateUtils.createColumnFieldPermissionMultiStage(workBook, multiStageSheet, 5 + i, 6 + i,
							1, roleNamesList, statusesList, i, validationsFeatureRolesList);
				}
				int startRowIndex = 6;
				for (int i = 0; i < multiStageFieldIdsList.size(); i++) {
					CommonTemplateUtils.createRow(workBook, multiStageSheet, startRowIndex + i,
							(startRowIndex + 1) + i);
					fillRowDataMultiStage(multiStageSheet.getRow(startRowIndex + i), multiStageFieldIdsList.get(i),
							permissionsGroupList, workBook, roleNamesList, statusesList,
							jobRequisitionTemplateXmlData.getZone12().getZone12FieldPermissionsList());
				}
			}
			if (null != regFieldsPermTransSheet) {
				permissionsGroupList.clear();
				permissionsGroupSet.clear();
				statusesList.clear();
				roleNamesList.clear();
				List<String> preApprovedRoleNamesList = new ArrayList<String>();
				List<String> preApprovedStatusesList = new ArrayList<String>();
				List<String> approvedRoleNamesList = new ArrayList<String>();
				List<String> approvedStatusesList = new ArrayList<String>();
				List<String> closedRoleNamesList = new ArrayList<String>();
				List<String> closedStatusesList = new ArrayList<String>();

				jobRequisitionTemplateXmlData.getZone5().getZone5FieldPermissionsList().forEach(fieldPermissionObj -> {
					if (permissionsGroupSet.add(fieldPermissionObj.getStatus() + fieldPermissionObj.getRoleName())) {
						if ("Pre-Approved".equalsIgnoreCase(fieldPermissionObj.getStatus().trim())) {
							preApprovedStatusesList.add(fieldPermissionObj.getStatus());
							preApprovedRoleNamesList.add(fieldPermissionObj.getRoleName());
						} else if ("Approved".equalsIgnoreCase(fieldPermissionObj.getStatus().trim())) {
							approvedStatusesList.add(fieldPermissionObj.getStatus());
							approvedRoleNamesList.add(fieldPermissionObj.getRoleName());
						} else if ("Closed".equalsIgnoreCase(fieldPermissionObj.getStatus().trim())) {
							closedStatusesList.add(fieldPermissionObj.getStatus());
							closedRoleNamesList.add(fieldPermissionObj.getRoleName());
						}
					}
				});

				statusesList.addAll(preApprovedStatusesList);
				statusesList.addAll(approvedStatusesList);
				statusesList.addAll(closedStatusesList);
				roleNamesList.addAll(preApprovedRoleNamesList);
				roleNamesList.addAll(approvedRoleNamesList);
				roleNamesList.addAll(closedRoleNamesList);

				for (int i = 0; i < permissionsGroupSet.size(); i++) {
					permissionsGroupList.add(statusesList.get(i) + roleNamesList.get(i));
					CommonTemplateUtils.createColumnFieldPermission(workBook, regFieldsPermTransSheet, 27 + i, 28 + i,
							1, roleNamesList, statusesList, i, validationsJrdmRolesList);
				}
				jobRequisitionTemplateXmlData.getZone2().getZone2TemplateNameTranslatersList()
						.forEach(translaterObj -> {
							translaterLangsSet.add(translaterObj.getTranslateLang());
						});
				jobRequisitionTemplateXmlData.getZone2().getZone2TemplateDescriptionTranslatersList()
						.forEach(translaterObj -> {
							translaterLangsSet.add(translaterObj.getTranslateLang());
						});
				jobRequisitionTemplateXmlData.getZone4().getZone4FieldDefinitionsList().forEach(fieldDefinitionObj -> {
					fieldDefinitionObj.getZone4FieldDefinitionTranslatersList().forEach(translaterObj -> {
						translaterLangsSet.add(translaterObj.getTranslateLang());
					});
				});
				List<String> translaterLangsList = new ArrayList<>(translaterLangsSet);
				int roleNamesListSize = roleNamesList.size();
				for (int i = 0; i < translaterLangsList.size(); i++) {
					CommonTemplateUtils.createColumnTransLang(workBook, regFieldsPermTransSheet,
							(32 + roleNamesListSize + i), (33 + roleNamesListSize + i), 4, validationsLangsMap,
							translaterLangsList, i);
					CommonTemplateUtils.addDataValidation255CharsAbove(workBook, regFieldsPermTransSheet, 4, 4,
							(32 + roleNamesListSize + i), (33 + roleNamesListSize + i),
							validationsLangsList.toArray(new String[0]), "JRDM_Validations", "!$A$2:$A$47");
				}
				int startRowIndex = 5;
				for (int i = 0; i < jobRequisitionTemplateXmlData.getZone4().getZone4FieldDefinitionsList()
						.size(); i++) {
					CommonTemplateUtils.createRow(workBook, regFieldsPermTransSheet, startRowIndex + i,
							(startRowIndex + 1) + i);
					fillRowDataFieldDefenition(regFieldsPermTransSheet,
							regFieldsPermTransSheet.getRow(startRowIndex + i),
							jobRequisitionTemplateXmlData.getZone4().getZone4FieldDefinitionsList().get(i),
							translaterLangsList, validationsFieldTypesList, workBook, roleNamesList,
							jobRequisitionTemplateXmlData.getZone5().getZone5FieldPermissionsList(),
							permissionsGroupList);
				}

				jobRequisitionTemplateXmlData.getZone6().getZone6ButtonEmailPermissionsList().forEach(permissionObj -> {
					permissionObj.getPermittedRolesList().forEach(role -> {
						jrdmRolesSet.add(role);
					});
				});
				jobRequisitionTemplateXmlData.getZone14().getZone14ButtonEmailPermissionsList()
						.forEach(permissionObj -> {
							Zone6ButtonEmailPermissionDTO zone6ButtonEmailPermObj = new Zone6ButtonEmailPermissionDTO();
							zone6ButtonEmailPermObj.setButtonEmailId(permissionObj.getButtonEmailId());
							zone6ButtonEmailPermObj.setPermittedRolesList(permissionObj.getPermittedRolesList());
							jobRequisitionTemplateXmlData.getZone6().getZone6ButtonEmailPermissionsList()
									.add(zone6ButtonEmailPermObj);
							permissionObj.getPermittedRolesList().forEach(role -> {
								jrdmRolesSet.add(role);
							});
						});
				List<String> jrdmRolesList = new ArrayList<>(jrdmRolesSet);
				startRowIndex = startRowIndex
						+ jobRequisitionTemplateXmlData.getZone4().getZone4FieldDefinitionsList().size() + 7;
				for (int i = 0; i < jrdmRolesList.size(); i++) {
					CommonTemplateUtils.createColumnButtonPermission(workBook, regFieldsPermTransSheet, (8 + i),
							(9 + i), startRowIndex, regFieldsPermTransSheet.getLastRowNum());
					regFieldsPermTransSheet.getRow(startRowIndex).getCell(8 + i).setCellValue(jrdmRolesList.get(i));
					CommonTemplateUtils.addDataValidation(regFieldsPermTransSheet, startRowIndex, startRowIndex,
							(8 + i), (8 + i), validationsJrdmRolesList.toArray(new String[0]));
				}
				startRowIndex = startRowIndex + 1;
				List<String> buttonsEmailsDefaultList = new ArrayList<>();
				buttonsEmailsDefaultList.add("Close Requisition Button (closeReq)");
				buttonsEmailsDefaultList.add("Re-open Requisition Button (reopenReq)");
				buttonsEmailsDefaultList.add("Permission to E-Mail candidates (candidateEmail)");
				for (int i = 0; i < jobRequisitionTemplateXmlData.getZone6().getZone6ButtonEmailPermissionsList()
						.size(); i++) {
					Zone6ButtonEmailPermissionDTO zone6ButtonEmailPermission = jobRequisitionTemplateXmlData.getZone6()
							.getZone6ButtonEmailPermissionsList().get(i);
					int indexButtonEmailId = "closeReq"
							.equalsIgnoreCase(zone6ButtonEmailPermission.getButtonEmailId().trim()) ? 0
									: ("reopenReq".equalsIgnoreCase(
											zone6ButtonEmailPermission.getButtonEmailId().trim()) ? 1 : 2);
					fillRowDataButtonEmailPermission(regFieldsPermTransSheet,
							regFieldsPermTransSheet.getRow(startRowIndex + indexButtonEmailId),
							jobRequisitionTemplateXmlData.getZone6().getZone6ButtonEmailPermissionsList().get(i),
							workBook, jrdmRolesList);
				}
				regFieldsPermTransSheet.getRow(1).getCell(7)
						.setCellValue(jobRequisitionTemplateXmlData.getZone2().getTemplateName());
				jobRequisitionTemplateXmlData.getZone2().getZone2TemplateNameTranslatersList()
						.forEach(translaterObj -> {
							regFieldsPermTransSheet.getRow(1)
									.getCell(32 + roleNamesListSize
											+ translaterLangsList.indexOf(translaterObj.getTranslateLang()))
									.setCellValue(translaterObj.getTranslateLabel());
						});

				regFieldsPermTransSheet.getRow(1).getCell(10)
						.setCellValue(jobRequisitionTemplateXmlData.getZone9().getScaleId());
				regFieldsPermTransSheet.getRow(1).getCell(13)
						.setCellValue(jobRequisitionTemplateXmlData.getZone12().getApplicationStatusSetName());
				regFieldsPermTransSheet.getRow(1).getCell(16)
						.setCellValue(jobRequisitionTemplateXmlData.getZone7().getJobReqSectionName());
				regFieldsPermTransSheet.getRow(1).getCell(19)
						.setCellValue(jobRequisitionTemplateXmlData.getZone2().getMinPostingDays());

				regFieldsPermTransSheet.getRow(2).getCell(7)
						.setCellValue(jobRequisitionTemplateXmlData.getZone2().getTemplateDescription());
				jobRequisitionTemplateXmlData.getZone2().getZone2TemplateDescriptionTranslatersList()
						.forEach(translaterObj -> {
							regFieldsPermTransSheet.getRow(2)
									.getCell(32 + roleNamesListSize
											+ translaterLangsList.indexOf(translaterObj.getTranslateLang()))
									.setCellValue(translaterObj.getTranslateLabel());
						});
				regFieldsPermTransSheet.getRow(2).getCell(10).setCellValue(CommonTemplateUtils
						.convertToYesOrNoLabel(jobRequisitionTemplateXmlData.getZone9().getReverseScale()));
				regFieldsPermTransSheet.getRow(2).getCell(13)
						.setCellValue(jobRequisitionTemplateXmlData.getZone13().getApplicationTemplateName());
				regFieldsPermTransSheet.getRow(2).getCell(16)
						.setCellValue(jobRequisitionTemplateXmlData.getZone7().getSignatureSectionName());
				regFieldsPermTransSheet.getRow(2).getCell(19)
						.setCellValue(jobRequisitionTemplateXmlData.getZone2().getMinIntervalLeadDays());
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

	private static void fillRowDataSupportedLanguage(XSSFSheet workSheet, XSSFRow row) {
		row.getCell(5).setCellValue("Yes");
		CommonTemplateUtils.addDataValidation(workSheet, row.getRowNum(), row.getRowNum(), 5, 5,
				new String[] { "Yes", "No" });
	}

	private static void fillRowDataFeaturePermission(XSSFSheet workSheet, XSSFRow row, String featurePermissonId,
			List<String> permissionsGroupList, XSSFWorkbook workBook, List<String> roleNamesList,
			List<String> featuresList, List<Zone12FeaturePermissionDTO> zone12FeaturePermissionsList) {
		row.getCell(4).setCellValue(featurePermissonId);
		zone12FeaturePermissionsList.forEach(zone12FeaturePermissionObj -> {
			if (zone12FeaturePermissionObj.getStatus().equalsIgnoreCase(featurePermissonId)) {
				zone12FeaturePermissionObj.getPermissionRoleMap().forEach((feature, roles) -> {
					roles.forEach(role -> {
						row.getCell(5 + permissionsGroupList.indexOf(feature + role)).setCellValue("Yes");
						CommonTemplateUtils.addDataValidation(workSheet, row.getRowNum(), row.getRowNum(),
								5 + permissionsGroupList.indexOf(feature + role),
								5 + permissionsGroupList.indexOf(feature + role), new String[] { "Yes", "No" });
					});
				});
			}
		});
	}

	private static void fillRowDataMultiStage(XSSFRow row, String fieldId, List<String> permissionsGroupList,
			XSSFWorkbook workBook, List<String> roleNamesList, List<String> statusesList,
			List<Zone12FieldPermissionDTO> zone12FieldPermissionsList) {
		row.getCell(3).setCellValue(fieldId);
		zone12FieldPermissionsList.forEach(zone12FieldPermissionObj -> {
			zone12FieldPermissionObj.getFieldPermissionsList().forEach(fieldPermissionObj -> {
				if (fieldPermissionObj.getFieldId().equalsIgnoreCase(fieldId)) {
					row.getCell(5 + permissionsGroupList
							.indexOf(zone12FieldPermissionObj.getStatus() + zone12FieldPermissionObj.getRoleName()))
							.setCellValue(fieldPermissionObj.getPermissionType());
				}
			});
		});
	}

	private static void fillRowDataButtonEmailPermission(XSSFSheet workSheet, XSSFRow row,
			Zone6ButtonEmailPermissionDTO zone6ButtonEmailPermissionDTO, XSSFWorkbook workBook,
			List<String> jrdmRolesList) {
		zone6ButtonEmailPermissionDTO.getPermittedRolesList().forEach(permittedRole -> {
			row.getCell(8 + jrdmRolesList.indexOf(permittedRole)).setCellValue("Yes");
			CommonTemplateUtils.addDataValidation(workSheet, row.getRowNum(), row.getRowNum(),
					8 + jrdmRolesList.indexOf(permittedRole), 8 + jrdmRolesList.indexOf(permittedRole),
					new String[] { "Yes", "No" });
		});
	}

	private static void fillRowDataFieldDefenition(XSSFSheet workSheet, XSSFRow row,
			Zone4FieldDefinitionDTO zone4FieldDefinitionDTO, List<String> translaterLangsList,
			List<String> validationsFieldTypesList, XSSFWorkbook workBook, List<String> roleNamesList,
			List<Zone5FieldPermissionDTO> zone5FieldPermissionsList, List<String> permissionsGroupList) {
		row.getCell(6).setCellValue(zone4FieldDefinitionDTO.getFieldId());
		row.getCell(7).setCellValue(zone4FieldDefinitionDTO.getFieldLabel());
		row.getCell(8).setCellValue(zone4FieldDefinitionDTO.getMimeType());
		CommonTemplateUtils.addDataValidation(workSheet, row.getRowNum(), row.getRowNum(), 8, 8,
				new String[] { "text-plain", "text-html" });
		row.getCell(9).setCellValue(zone4FieldDefinitionDTO.getFieldType());
		CommonTemplateUtils.addDataValidation255CharsAbove(workBook, workSheet, row.getRowNum(), row.getRowNum(), 9, 9,
				validationsFieldTypesList.toArray(new String[0]), "JRDM_Validations", "!$D$2:$D$23");
		row.getCell(10).setCellValue(zone4FieldDefinitionDTO.getGroupName());
		row.getCell(11).setCellValue(zone4FieldDefinitionDTO.getParentPickListIdOrObject());
		row.getCell(12).setCellValue(zone4FieldDefinitionDTO.getPickListOrObject());
		row.getCell(13).setCellValue(zone4FieldDefinitionDTO.getObjectType());
		row.getCell(14).setCellValue(CommonTemplateUtils.convertToYesOrNo(zone4FieldDefinitionDTO.getRequired()));
		row.getCell(15).setCellValue(CommonTemplateUtils.convertToYesOrNo(zone4FieldDefinitionDTO.getCustom()));
		row.getCell(16).setCellValue(CommonTemplateUtils.convertToYesOrNo(zone4FieldDefinitionDTO.getMultiSelect()));
		row.getCell(17).setCellValue(CommonTemplateUtils.convertToYesOrNo(zone4FieldDefinitionDTO.getMobileField()));
		row.getCell(18)
				.setCellValue(CommonTemplateUtils.convertToYesOrNo(zone4FieldDefinitionDTO.getJobDescriptionToken()));
		row.getCell(19)
				.setCellValue(CommonTemplateUtils.convertToYesOrNo(zone4FieldDefinitionDTO.getOfferLetterToken()));
		row.getCell(20).setCellValue(zone4FieldDefinitionDTO.getScreenReader());
		row.getCell(21).setCellValue(zone4FieldDefinitionDTO.getFilterField());
		row.getCell(22).setCellValue(CommonTemplateUtils.convertToYesOrNo(zone4FieldDefinitionDTO.getReportable()));
		row.getCell(23).setCellValue(zone4FieldDefinitionDTO.getCandidateSearchField());
		row.getCell(24).setCellValue(zone4FieldDefinitionDTO.getNotesOrComments());

		zone5FieldPermissionsList.forEach(zone5FieldPermissionObj -> {
			zone5FieldPermissionObj.getFieldPermissionsList().forEach(fieldPermissionObj -> {
				if (fieldPermissionObj.getFieldId().equalsIgnoreCase(zone4FieldDefinitionDTO.getFieldId())) {
					row.getCell(27 + permissionsGroupList
							.indexOf(zone5FieldPermissionObj.getStatus() + zone5FieldPermissionObj.getRoleName()))
							.setCellValue(fieldPermissionObj.getPermissionType());
				}
			});
		});
		int roleNamesListSize = roleNamesList.size();
		zone4FieldDefinitionDTO.getZone4FieldDefinitionTranslatersList().forEach(translaterObj -> {
			row.getCell(32 + roleNamesListSize + translaterLangsList.indexOf(translaterObj.getTranslateLang()))
					.setCellValue(translaterObj.getTranslateLabel());
			workSheet.autoSizeColumn(
					32 + roleNamesListSize + translaterLangsList.indexOf(translaterObj.getTranslateLang()));
		});
	}

}
