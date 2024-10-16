package com.rpa.sapsf.utils.fileConverter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;

import com.rpa.sapsf.corporate.data.model.dto.*;
import com.rpa.sapsf.dto.TranslaterDTO;

import io.micrometer.common.util.StringUtils;

public class CorporateDataModelTemplateUtils {
	public static CorporateDataModelTemplateDTO extractExcelData(MultipartFile file) {
		CorporateDataModelTemplateDTO corporateDataModelTemplateExcelData = new CorporateDataModelTemplateDTO();
		// Zone 1 objects
		Zone1DTO zone1Data = new Zone1DTO();
		List<HrisElementDTO> hrisElementsLocationGroupList = new ArrayList<HrisElementDTO>();
		// Zone 2 objects
		Zone2DTO zone2Data = new Zone2DTO();
		List<HrisElementDTO> hrisElementsLocationList = new ArrayList<HrisElementDTO>();
		// Zone 3 objects
		Zone3DTO zone3Data = new Zone3DTO();
		List<HrisElementDTO> hrisElementsGeoZoneList = new ArrayList<HrisElementDTO>();
		// Zone 4 objects
		Zone4DTO zone4Data = new Zone4DTO();
		List<HrisElementDTO> hrisElementsPayRangeList = new ArrayList<HrisElementDTO>();
		// Zone 5 objects
		Zone5DTO zone5Data = new Zone5DTO();
		List<HrisElementDTO> hrisElementsPayGradeList = new ArrayList<HrisElementDTO>();
		// Zone 6 objects
		Zone6DTO zone6Data = new Zone6DTO();
		List<HrisElementDTO> hrisElementsPayComponentList = new ArrayList<HrisElementDTO>();
		// Zone 7 objects
		Zone7DTO zone7Data = new Zone7DTO();
		List<HrisElementDTO> hrisElementsPayComponentGroupList = new ArrayList<HrisElementDTO>();
		List<HrisElementDTO> hrisElementsFrequencyList = new ArrayList<HrisElementDTO>();
		// Zone 8 objects
		Zone8DTO zone8Data = new Zone8DTO();
		List<HrisElementDTO> hrisElementsCorporateAddressList = new ArrayList<HrisElementDTO>();
		List<HrisElementDTO> hrisElementsDynamicRoleList = new ArrayList<HrisElementDTO>();
		List<HrisElementDTO> hrisElementsDynamicRoleAssignmentList = new ArrayList<HrisElementDTO>();
		List<HrisElementDTO> hrisElementsWfConfigList = new ArrayList<HrisElementDTO>();
		List<HrisElementDTO> hrisElementsWfConfigStepList = new ArrayList<HrisElementDTO>();
		List<HrisElementDTO> hrisElementsWfStepApproverList = new ArrayList<HrisElementDTO>();
		List<HrisElementDTO> hrisElementsEventReasonList = new ArrayList<HrisElementDTO>();
		List<HrisElementDTO> hrisElementsWfConfigContributorList = new ArrayList<HrisElementDTO>();
		List<HrisElementDTO> hrisElementsWfConfigCCList = new ArrayList<HrisElementDTO>();

		try {
			XSSFWorkbook workBook;
			workBook = new XSSFWorkbook(file.getInputStream());

			extractSheetData(hrisElementsLocationGroupList, workBook, "locationGroup", 18, 58, 4);
			extractSheetData(hrisElementsLocationList, workBook, "location", 20, 60, 4);
			extractSheetData(hrisElementsGeoZoneList, workBook, "geozone", 21, 61, 4);
			extractSheetData(hrisElementsPayRangeList, workBook, "payRange", 20, 60, 4);
			extractSheetData(hrisElementsPayGradeList, workBook, "payGrade", 19, 59, 4);
			extractSheetData(hrisElementsPayComponentList, workBook, "payComponent", 19, 59, 4);
			extractSheetData(hrisElementsPayComponentGroupList, workBook, "payComponentGroup", 20, 60, 4);
			extractSheetData(hrisElementsFrequencyList, workBook, "frequency", 18, 58, 4);
			extractSheetData(hrisElementsCorporateAddressList, workBook, "corporateaddress", 20, 60, 5);
			extractSheetData(hrisElementsDynamicRoleAssignmentList, workBook, "dynamicRoleAssignment", 16, 56, 4);
			extractSheetData(hrisElementsDynamicRoleList, workBook, "dynamicRole", 16, 56, 4);
			extractSheetData(hrisElementsEventReasonList, workBook, "eventreason", 21, 61, 4);
			extractSheetData(hrisElementsWfConfigCCList, workBook, "wfConfigCC", 21, 61, 4);
			extractSheetData(hrisElementsWfConfigContributorList, workBook, "wfConfigContributor", 21, 61, 4);
			extractSheetData(hrisElementsWfConfigList, workBook, "wfConfig", 21, 61, 4);
			extractSheetData(hrisElementsWfConfigStepList, workBook, "wfConfigStep", 21, 61, 4);
			extractSheetData(hrisElementsWfStepApproverList, workBook, "wfStepApprover", 21, 61, 4);

			zone1Data.setHrisElementsLocationGroupList(hrisElementsLocationGroupList);
			zone2Data.setHrisElementsLocationList(hrisElementsLocationList);
			zone3Data.setHrisElementsGeoZoneList(hrisElementsGeoZoneList);
			zone4Data.setHrisElementsPayRangeList(hrisElementsPayRangeList);
			zone5Data.setHrisElementsPayGradeList(hrisElementsPayGradeList);
			zone6Data.setHrisElementsPayComponentList(hrisElementsPayComponentList);
			zone7Data.setHrisElementsPayComponentGroupList(hrisElementsPayComponentGroupList);
			zone7Data.setHrisElementsFrequencyList(hrisElementsFrequencyList);
			zone8Data.setHrisElementsCorporateAddressList(hrisElementsCorporateAddressList);
			zone8Data.setHrisElementsDynamicRoleAssignmentList(hrisElementsDynamicRoleAssignmentList);
			zone8Data.setHrisElementsDynamicRoleList(hrisElementsDynamicRoleList);
			zone8Data.setHrisElementsEventReasonList(hrisElementsEventReasonList);
			zone8Data.setHrisElementsWfConfigCCList(hrisElementsWfConfigCCList);
			zone8Data.setHrisElementsWfConfigContributorList(hrisElementsWfConfigContributorList);
			zone8Data.setHrisElementsWfConfigList(hrisElementsWfConfigList);
			zone8Data.setHrisElementsWfConfigStepList(hrisElementsWfConfigStepList);
			zone8Data.setHrisElementsWfStepApproverList(hrisElementsWfStepApproverList);

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy hh:mm a");
			Date date = new Date();
			zone1Data.setLastModifiedDate(formatter.format(date).toUpperCase());
			corporateDataModelTemplateExcelData.setZone1(zone1Data);
			corporateDataModelTemplateExcelData.setZone2(zone2Data);
			corporateDataModelTemplateExcelData.setZone3(zone3Data);
			corporateDataModelTemplateExcelData.setZone4(zone4Data);
			corporateDataModelTemplateExcelData.setZone5(zone5Data);
			corporateDataModelTemplateExcelData.setZone6(zone6Data);
			corporateDataModelTemplateExcelData.setZone7(zone7Data);
			corporateDataModelTemplateExcelData.setZone8(zone8Data);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return corporateDataModelTemplateExcelData;
	}

	private static void extractSheetData(List<HrisElementDTO> hrisElementsList, XSSFWorkbook workBook, String sheetName,
			int transStartIndex, int transEndIndex, int hrisElementFieldIndex) {
		XSSFSheet sheetHrisElement = workBook.getSheet(sheetName);
		XSSFRow row;
		int rowNum = 0;
		List<String> translateLangsList = new ArrayList<String>();
		Iterator<Row> rowsSheet = null;
		if (null != sheetHrisElement)
			rowsSheet = sheetHrisElement.rowIterator();
		/* Sheet read starts here */
		while (null != rowsSheet && rowsSheet.hasNext()) {
			rowNum++;
			row = (XSSFRow) rowsSheet.next();
			if (rowNum == 4) {
				for (int colNum = transStartIndex; colNum < transEndIndex + 1; colNum++) {
					translateLangsList.add(CommonTemplateUtils.readCellValue(row.getCell(colNum)));
				}
			} else if (rowNum > 4) {
				if (null != row.getCell(hrisElementFieldIndex) && StringUtils
						.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(hrisElementFieldIndex)))) {
					if ("hris-element"
							.equalsIgnoreCase(CommonTemplateUtils.readCellValue(row.getCell(hrisElementFieldIndex)))) {
						HrisElementDTO hrisElement = new HrisElementDTO();
						hrisElement.setHrisElementId(
								CommonTemplateUtils.readCellValue(row.getCell(hrisElementFieldIndex + 1)));
						hrisElement.setHrisElementLabel(
								CommonTemplateUtils.readCellValue(row.getCell(hrisElementFieldIndex + 2)));
						hrisElement.setHrisFieldsList(new ArrayList<HrisFieldDTO>());
						hrisElement.setHrisAssociationList(new ArrayList<HrisAssociationDTO>());
						hrisElement.setHrisSearchFieldsList(new ArrayList<HrisSearchFieldDTO>());
						hrisElementsList.add(hrisElement);
					} else if ("hris-field"
							.equalsIgnoreCase(CommonTemplateUtils.readCellValue(row.getCell(hrisElementFieldIndex)))
							&& hrisElementsList.size() > 0) {
						HrisElementDTO hrisElement = hrisElementsList.get(hrisElementsList.size() - 1);

						HrisFieldDTO hrisField = new HrisFieldDTO();
						hrisField.setId(CommonTemplateUtils.readCellValue(row.getCell(hrisElementFieldIndex + 1)));
						hrisField.setLabelName(
								CommonTemplateUtils.readCellValue(row.getCell(hrisElementFieldIndex + 2)));
						hrisField.setMaxLength(
								CommonTemplateUtils.readCellValue(row.getCell(hrisElementFieldIndex + 4)));
						hrisField
								.setRequired(CommonTemplateUtils.readCellValue(row.getCell(hrisElementFieldIndex + 7)));
						hrisField.setVisibility(
								CommonTemplateUtils.readCellValue(row.getCell(hrisElementFieldIndex + 5)));
						int index = 0;
						List<TranslaterDTO> translatersList = new ArrayList<TranslaterDTO>();
						for (int colNum = transStartIndex; colNum < transEndIndex + 1; colNum++) {
							if (null != row.getCell(colNum)
									&& StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colNum)))
									&& index < translateLangsList.size()) {
								TranslaterDTO translaterData = new TranslaterDTO();
								translaterData
										.setTranslateLabel(CommonTemplateUtils.readCellValue(row.getCell(colNum)));
								translaterData.setTranslateLang(translateLangsList.get(index));
								translatersList.add(translaterData);
							}
							index++;
						}
						hrisField.setLabelTranslatersList(translatersList);
						hrisElement.getHrisFieldsList().add(hrisField);

						HrisAssociationDTO hrisAssociation = new HrisAssociationDTO();
						hrisAssociation.setId(CommonTemplateUtils.readCellValue(row.getCell(transEndIndex + 1)));
						hrisAssociation
								.setMultiplicity(CommonTemplateUtils.readCellValue(row.getCell(transEndIndex + 2)));
						hrisAssociation.setDestinationEntity(
								CommonTemplateUtils.readCellValue(row.getCell(transEndIndex + 3)));
						hrisAssociation.setRequired(CommonTemplateUtils.readCellValue(row.getCell(transEndIndex + 4)));
						if (StringUtils.isNotEmpty(hrisAssociation.getId())
								&& StringUtils.isNotEmpty(hrisAssociation.getMultiplicity())
								&& StringUtils.isNotEmpty(hrisAssociation.getDestinationEntity()))
							hrisElement.getHrisAssociationList().add(hrisAssociation);

						HrisSearchFieldDTO hrisSearchField = new HrisSearchFieldDTO();
						hrisSearchField.setCriteria(CommonTemplateUtils.readCellValue(row.getCell(transEndIndex + 5)));
						hrisSearchField.setId(CommonTemplateUtils.readCellValue(row.getCell(transEndIndex + 6)));
						hrisSearchField.setName(CommonTemplateUtils.readCellValue(row.getCell(transEndIndex + 7)));
						if (StringUtils.isNotEmpty(hrisSearchField.getId()))
							hrisElement.getHrisSearchFieldsList().add(hrisSearchField);
					}
				}
			}
		}
		/* Sheet read ends here */
	}

	public static String buildXmlFile(CorporateDataModelTemplateDTO corporateDataModelTemplateExcelData,
			String fileName) {

		StringBuilder xmlStringBuilder = new StringBuilder();
		xmlStringBuilder.append("<corporate-data-model>\n");
		xmlStringBuilder.append("<description>Success Factors HRIS Data Model</description>\n");
		xmlStringBuilder.append("<template-lastmodified>"
				+ corporateDataModelTemplateExcelData.getZone1().getLastModifiedDate() + "</template-lastmodified>\n");

		// Zone 1 starts here
		buildXmlHrisElementSeries(xmlStringBuilder,
				corporateDataModelTemplateExcelData.getZone1().getHrisElementsLocationGroupList());
		// Zone 1 ends here

		// Zone 2 starts here
		buildXmlHrisElementSeries(xmlStringBuilder,
				corporateDataModelTemplateExcelData.getZone2().getHrisElementsLocationList());
		// Zone 2 ends here

		// Zone 3 starts here
		buildXmlHrisElementSeries(xmlStringBuilder,
				corporateDataModelTemplateExcelData.getZone3().getHrisElementsGeoZoneList());
		// Zone 3 ends here

		// Zone 4 starts here
		buildXmlHrisElementSeries(xmlStringBuilder,
				corporateDataModelTemplateExcelData.getZone4().getHrisElementsPayRangeList());
		// Zone 4 ends here

		// Zone 5 starts here
		buildXmlHrisElementSeries(xmlStringBuilder,
				corporateDataModelTemplateExcelData.getZone5().getHrisElementsPayGradeList());
		// Zone 5 ends here

		// Zone 6 starts here
		buildXmlHrisElementSeries(xmlStringBuilder,
				corporateDataModelTemplateExcelData.getZone6().getHrisElementsPayComponentList());
		// Zone 6 ends here

		// Zone 7 starts here
		buildXmlHrisElementSeries(xmlStringBuilder,
				corporateDataModelTemplateExcelData.getZone7().getHrisElementsPayComponentGroupList());
		buildXmlHrisElementSeries(xmlStringBuilder,
				corporateDataModelTemplateExcelData.getZone7().getHrisElementsFrequencyList());
		// Zone 7 ends here

		// Zone 8 starts here
		buildXmlHrisElementSeries(xmlStringBuilder,
				corporateDataModelTemplateExcelData.getZone8().getHrisElementsCorporateAddressList());
		buildXmlHrisElementSeries(xmlStringBuilder,
				corporateDataModelTemplateExcelData.getZone8().getHrisElementsDynamicRoleList());
		buildXmlHrisElementSeries(xmlStringBuilder,
				corporateDataModelTemplateExcelData.getZone8().getHrisElementsDynamicRoleAssignmentList());
		buildXmlHrisElementSeries(xmlStringBuilder,
				corporateDataModelTemplateExcelData.getZone8().getHrisElementsWfConfigList());
		buildXmlHrisElementSeries(xmlStringBuilder,
				corporateDataModelTemplateExcelData.getZone8().getHrisElementsWfConfigStepList());
		buildXmlHrisElementSeries(xmlStringBuilder,
				corporateDataModelTemplateExcelData.getZone8().getHrisElementsWfStepApproverList());
		buildXmlHrisElementSeries(xmlStringBuilder,
				corporateDataModelTemplateExcelData.getZone8().getHrisElementsEventReasonList());
		buildXmlHrisElementSeries(xmlStringBuilder,
				corporateDataModelTemplateExcelData.getZone8().getHrisElementsWfConfigContributorList());
		buildXmlHrisElementSeries(xmlStringBuilder,
				corporateDataModelTemplateExcelData.getZone8().getHrisElementsWfConfigCCList());
		// Zone 8 ends here

		xmlStringBuilder.append("</corporate-data-model>");
		Document doc = CommonTemplateUtils.convertStringToDocument(xmlStringBuilder.toString());
		return CommonTemplateUtils.convertDocumentToXml(doc, fileName,
				"-//SuccessFactors, Inc.//Corporate Data Model Template//EN", "corporate-data-model.dtd");
	}

	private static void buildXmlHrisElementSeries(StringBuilder xmlStringBuilder,
			List<HrisElementDTO> hrisElementsList) {
		hrisElementsList.forEach(hrisElement -> {
			xmlStringBuilder.append("<hris-element id=\"" + hrisElement.getHrisElementId() + "\">\n");
			xmlStringBuilder.append("<label>" + hrisElement.getHrisElementLabel() + "</label>\n");
			hrisElement.getHrisFieldsList().forEach(fieldObj -> {
				xmlStringBuilder.append("<hris-field max-length=\"" + fieldObj.getMaxLength() + "\" id=\""
						+ fieldObj.getId() + "\" visibility=\"" + fieldObj.getVisibility() + "\" required=\""
						+ (StringUtils.isEmpty(fieldObj.getRequired()) ? ""
								: (("Y").equalsIgnoreCase(fieldObj.getRequired().substring(0, 1)) ? true : false))
						+ "\">\n");
				xmlStringBuilder.append("<label>" + fieldObj.getLabelName() + "</label>\n");
				fieldObj.getLabelTranslatersList().forEach(translaterObj -> {
					xmlStringBuilder.append("<label xml:lang=\"" + translaterObj.getTranslateLang() + "\">"
							+ translaterObj.getTranslateLabel() + "</label>\n");
				});
				xmlStringBuilder.append("</hris-field>\n");
			});
			if (hrisElement.getHrisAssociationList().size() > 0)
				xmlStringBuilder.append("<hris-associations>\n");
			hrisElement.getHrisAssociationList().forEach(associationObj -> {
				xmlStringBuilder.append("<association id=\"" + associationObj.getId() + "\" multiplicity=\""
						+ associationObj.getMultiplicity() + "\" destination-entity=\""
						+ associationObj.getDestinationEntity() + "\" required=\""
						+ (StringUtils.isEmpty(associationObj.getRequired()) ? ""
								: (("Y").equalsIgnoreCase(associationObj.getRequired().substring(0, 1)) ? true : false))
						+ "\"/>\n");
			});
			if (hrisElement.getHrisAssociationList().size() > 0)
				xmlStringBuilder.append("</hris-associations>\n");
			if (hrisElement.getHrisSearchFieldsList().size() > 0)
				xmlStringBuilder.append("<search-criteria>\n");
			hrisElement.getHrisSearchFieldsList().forEach(searchFieldObj -> {
				xmlStringBuilder.append("<search-field id=\"" + searchFieldObj.getId() + "\"/>\n");
			});
			if (hrisElement.getHrisSearchFieldsList().size() > 0)
				xmlStringBuilder.append("</search-criteria>\n");
			xmlStringBuilder.append("</hris-element>\n");
		});
	}

	public static CorporateDataModelTemplateDTO extractXmlData(MultipartFile file) {
		// TODO Auto-generated method stub
		return null;
	}

	public static String buildExcelFile(CorporateDataModelTemplateDTO corporateDataModelTemplateXmlData,
			String fileName) {
		// TODO Auto-generated method stub
		return null;
	}
}
