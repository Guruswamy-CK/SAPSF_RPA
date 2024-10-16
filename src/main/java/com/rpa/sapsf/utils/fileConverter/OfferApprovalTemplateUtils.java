package com.rpa.sapsf.utils.fileConverter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.rpa.sapsf.dto.EnumDTO;
import com.rpa.sapsf.dto.EnumLabelTranslaterDTO;
import com.rpa.sapsf.offer.approval.dto.*;

import io.micrometer.common.util.StringUtils;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class OfferApprovalTemplateUtils {

	public static OfferApprovalTemplateDTO extractExcelData(MultipartFile file) {
		OfferApprovalTemplateDTO offerApprovalTemplateExcelData = new OfferApprovalTemplateDTO();

		// Zone 2 objects
		Zone2DTO zone2Data = new Zone2DTO();
		List<Zone2TemplateNameTranslaterDTO> zone2TemplateNameTranslatersList = new ArrayList<Zone2TemplateNameTranslaterDTO>();
		List<Zone2TemplateDescriptionTranslaterDTO> zone2TemplateDescriptionTranslatersList = new ArrayList<Zone2TemplateDescriptionTranslaterDTO>();
		List<String> translateTemplateNamesList = new ArrayList<String>();
		List<String> translateTemplateDesrciptionsList = new ArrayList<String>();

		// Zone 3 objects
		Zone3DTO zone3Data = new Zone3DTO();
		List<Zone3FieldDefinitionDTO> zone3FieldDefinitionsList = new ArrayList<Zone3FieldDefinitionDTO>();

		// Zone 4 objects
		Zone4DTO zone4Data = new Zone4DTO();
		List<String> mobileFieldsList = new ArrayList<String>();

		// Zone 5 objects
		Zone5DTO zone5Data = new Zone5DTO();
		List<Zone5OfferApproverDTO> zone5OfferApproversList = new ArrayList<Zone5OfferApproverDTO>();

		// Common Objects
		List<String> translateLangsList = new ArrayList<String>();
		List<EnumDTO> enumValuesList = new ArrayList<EnumDTO>();

		try {
			XSSFWorkbook workBook;
			workBook = new XSSFWorkbook(file.getInputStream());
			XSSFSheet sheetTemplate = workBook.getSheet("Offer_Approval");
			XSSFRow row;
			int rowNum = 0;
			int rowNumApprovesStart = 0;
			int rowNumOfferApprovalSheetStart = 0;
			CommonTemplateUtils.extractEnumData("Enum_Values", workBook, enumValuesList);
			Iterator<Row> rowsTemplate = null;
			if (null != sheetTemplate)
				rowsTemplate = sheetTemplate.rowIterator();
			/* Template read starts here */
			while (null != rowsTemplate && rowsTemplate.hasNext()) {
				rowNum++;
				row = (XSSFRow) rowsTemplate.next();
				if (row.getCell(3) != null && "PRE-DEFINED OFFER APPROVAL WORKFLOW"
						.equalsIgnoreCase(CommonTemplateUtils.readCellValue(row.getCell(3)))) {
					rowNumApprovesStart = rowNum;
				}
				if (rowNumApprovesStart == 0) {
					if (row.getCell(3) != null
							&& "Template Name".equalsIgnoreCase(CommonTemplateUtils.readCellValue(row.getCell(3)))) {
						rowNumOfferApprovalSheetStart = rowNum;
					}
					if (rowNumOfferApprovalSheetStart != 0) {
						/* Zone 2 starts here */
						if (rowNum == rowNumOfferApprovalSheetStart) {
							zone2Data.setTemplateName(CommonTemplateUtils.readCellValue(row.getCell(4)));

							int colNum = 21;
							while (null != row.getCell(colNum)
									&& StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colNum)))) {
								translateTemplateNamesList.add(CommonTemplateUtils.readCellValue(row.getCell(colNum)));
								colNum++;
							}
						} else if (rowNum == (rowNumOfferApprovalSheetStart + 1)) {
							zone2Data.setTemplateDescription(CommonTemplateUtils.readCellValue(row.getCell(4)));
							int colNum = 21;
							while (null != row.getCell(colNum)
									&& StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colNum)))) {
								translateTemplateDesrciptionsList
										.add(CommonTemplateUtils.readCellValue(row.getCell(colNum)));
								colNum++;
							}
						} else if (rowNum == (rowNumOfferApprovalSheetStart + 2)) {
							int colNum = 21;
							while (null != row.getCell(colNum)
									&& StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colNum)))) {
								translateLangsList
										.add(CommonTemplateUtils.readCellValue(row.getCell(colNum)).substring(0, 5));
								colNum++;
							}
						}
						/* Zone 2 ends here */

						/* Zone 3 starts here */
						else if (rowNum > (rowNumOfferApprovalSheetStart + 2)) {
							if (null != row.getCell(3)
									&& StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(3)))) {
								Zone3FieldDefinitionDTO zone3FieldDefinition = new Zone3FieldDefinitionDTO();
								zone3FieldDefinition.setFieldId(CommonTemplateUtils.readCellValue(row.getCell(3)));
								zone3FieldDefinition.setFieldLabel(CommonTemplateUtils.readCellValue(row.getCell(4)));
								zone3FieldDefinition.setSource(CommonTemplateUtils.readCellValue(row.getCell(5)));
								zone3FieldDefinition.setTypeOfField(CommonTemplateUtils.readCellValue(row.getCell(6)));
								zone3FieldDefinition
										.setParentPickListOrObject(CommonTemplateUtils.readCellValue(row.getCell(7)));
								zone3FieldDefinition
										.setPickListOrObjectId(CommonTemplateUtils.readCellValue(row.getCell(8)));
								zone3FieldDefinition.setObjectType(CommonTemplateUtils.readCellValue(row.getCell(9)));
								zone3FieldDefinition.setRequired(CommonTemplateUtils.readCellValue(row.getCell(10)));
								zone3FieldDefinition.setCustom(CommonTemplateUtils.readCellValue(row.getCell(11)));
								zone3FieldDefinition.setAnonymize(CommonTemplateUtils.readCellValue(row.getCell(12)));
								zone3FieldDefinition.setMobile(CommonTemplateUtils.readCellValue(row.getCell(13)));
								zone3FieldDefinition.setCustomToken(CommonTemplateUtils.readCellValue(row.getCell(14)));
								zone3FieldDefinition.setReportable(CommonTemplateUtils.readCellValue(row.getCell(15)));
								zone3FieldDefinition.setHelpText(CommonTemplateUtils.readCellValue(row.getCell(16)));
								zone3FieldDefinition.setNotes(CommonTemplateUtils.readCellValue(row.getCell(17)));

								List<Zone3FieldDefinitionTranslaterDTO> zone3FieldDefinitionTranslatersList = new ArrayList<Zone3FieldDefinitionTranslaterDTO>();

								int colNum = 21;
								int indexZone3 = 0;
								while (null != row.getCell(colNum)) {
									if (StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colNum)))
											&& indexZone3 < translateLangsList.size()) {
										Zone3FieldDefinitionTranslaterDTO zone3FieldTranslatersData = new Zone3FieldDefinitionTranslaterDTO();
										zone3FieldTranslatersData.setFieldDefinitionName(
												CommonTemplateUtils.readCellValue(row.getCell(colNum)));
										zone3FieldTranslatersData.setTranslateLang(translateLangsList.get(indexZone3));
										zone3FieldDefinitionTranslatersList.add(zone3FieldTranslatersData);

									}
									colNum++;
									indexZone3++;
								}
								zone3FieldDefinition.setZone3TranslatersList(zone3FieldDefinitionTranslatersList);

								zone3FieldDefinitionsList.add(zone3FieldDefinition);

							}
						}
						zone3Data.setZone3FieldDefinitionsList(zone3FieldDefinitionsList);
						/* Zone 3 ends here */
					}
				} else {
					/* Zone 5 starts here */
					if (rowNum == rowNumApprovesStart || rowNum == rowNumApprovesStart + 3) {

					} else if (rowNum == rowNumApprovesStart + 1 && null != row.getCell(4)) {
						zone5Data.setOfferApproversReOrder(CommonTemplateUtils.readCellValue(row.getCell(4)));
					} else if (rowNum == rowNumApprovesStart + 2 && null != row.getCell(4)) {
						zone5Data.setOfferApproversEditable(CommonTemplateUtils.readCellValue(row.getCell(4)));
					} else {
						if (null != row.getCell(3)
								&& StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(3)))) {
							Zone5OfferApproverDTO zone5OfferApprover = new Zone5OfferApproverDTO();
							zone5OfferApprover.setStepId(CommonTemplateUtils.readCellValue(row.getCell(3)));
							zone5OfferApprover.setEditable(CommonTemplateUtils.readCellValue(row.getCell(4)));
							zone5OfferApprover.setEditInvalidUser(CommonTemplateUtils.readCellValue(row.getCell(5)));
							zone5OfferApprover.setLabel(CommonTemplateUtils.readCellValue(row.getCell(6)));
							zone5OfferApprover.setDefaultUserType(CommonTemplateUtils.readCellValue(row.getCell(7)));
							zone5OfferApprover.setDefaultUser(CommonTemplateUtils.readCellValue(row.getCell(8)));

							List<Zone5OfferApproverTranslaterDTO> zone5OfferApproverTranslatersList = new ArrayList<Zone5OfferApproverTranslaterDTO>();

							int colNum = 21;
							int indexZone5 = 0;
							while (null != row.getCell(colNum)) {
								if (StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colNum)))
										&& indexZone5 < translateLangsList.size()) {
									Zone5OfferApproverTranslaterDTO zone5ApproverTranslatersData = new Zone5OfferApproverTranslaterDTO();
									zone5ApproverTranslatersData.setOfferApproverName(
											CommonTemplateUtils.readCellValue(row.getCell(colNum)));
									zone5ApproverTranslatersData.setTranslateLang(translateLangsList.get(indexZone5));
									zone5OfferApproverTranslatersList.add(zone5ApproverTranslatersData);

								}
								colNum++;
								indexZone5++;
							}
							zone5OfferApprover.setZone5OfferApproverTranslatersList(zone5OfferApproverTranslatersList);
							zone5OfferApproversList.add(zone5OfferApprover);
						}
					}
					zone5Data.setZone5OfferApproversList(zone5OfferApproversList);
					/* Zone 5 ends here */
				}

			}
			/* Zone 2 start here */
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

			/* Zone 4 starts here */
			zone3Data.getZone3FieldDefinitionsList().forEach(zone3FieldDefinition -> {
				if ("Y".equalsIgnoreCase(zone3FieldDefinition.getMobile())) {
					mobileFieldsList.add(zone3FieldDefinition.getFieldId());
				}
			});
			zone4Data.setMobileFieldsList(mobileFieldsList);
			/* Zone 4 ends here */
			/* Template read ends here */

			offerApprovalTemplateExcelData.setZone2(zone2Data);
			offerApprovalTemplateExcelData.setZone3(zone3Data);
			offerApprovalTemplateExcelData.setZone4(zone4Data);
			offerApprovalTemplateExcelData.setZone5(zone5Data);
			offerApprovalTemplateExcelData.setEnumValuesList(enumValuesList);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return offerApprovalTemplateExcelData;

	}

	public static String buildXmlFile(OfferApprovalTemplateDTO offerApprovalTemplateExcelData, String fileName) {

		StringBuilder xmlStringBuilder = new StringBuilder();
		xmlStringBuilder.append("<offer-detail-template>\n");
		// Zone 2 starts here
		StringBuilder xmlStrZone2 = new StringBuilder();
		buildXmlStrZone2(xmlStrZone2, offerApprovalTemplateExcelData.getZone2());
		xmlStringBuilder.append(xmlStrZone2);
		// Zone 2 ends here

		// Zone 3 starts here
		StringBuilder xmlStrZone3 = new StringBuilder();
		buildXmlStrZone3(xmlStrZone3, offerApprovalTemplateExcelData.getZone3(),
				offerApprovalTemplateExcelData.getEnumValuesList());
		xmlStringBuilder.append(xmlStrZone3);
		// Zone 3 ends here

		// Zone 4 starts here
		StringBuilder xmlStrZone4 = new StringBuilder();
		buildXmlStrZone4(xmlStrZone4, offerApprovalTemplateExcelData.getZone4());
		xmlStringBuilder.append(xmlStrZone4);
		// Zone 4 ends here

		// Zone 5 starts here
		StringBuilder xmlStrZone5 = new StringBuilder();
		buildXmlStrZone5(xmlStrZone5, offerApprovalTemplateExcelData.getZone5());
		xmlStringBuilder.append(xmlStrZone5);
		// Zone 5 ends here

		xmlStringBuilder.append("</offer-detail-template>");
		Document doc = CommonTemplateUtils.convertStringToDocument(xmlStringBuilder.toString());
		return CommonTemplateUtils.convertDocumentToXml(doc, fileName,
				"-//SuccessFactors, Inc.//DTD Offer Detail Template//EN", "offer-detail-template.dtd");
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

	private static void buildXmlStrZone3(StringBuilder xmlStrZone3, Zone3DTO zone3dto, List<EnumDTO> enumValuesList) {
		if (null != zone3dto) {
			zone3dto.getZone3FieldDefinitionsList().forEach(fieldDefinition -> {
				xmlStrZone3.append("<field-definition");
				xmlStrZone3.append(" id=\"" + fieldDefinition.getFieldId() + "\"");
				xmlStrZone3.append(" type=\"" + fieldDefinition.getTypeOfField() + "\"");
				xmlStrZone3.append(" required=\"" + (StringUtils.isEmpty(fieldDefinition.getRequired()) ? ""
						: (("Y").equalsIgnoreCase(fieldDefinition.getRequired().substring(0, 1)) ? true : false))
						+ "\"");
				xmlStrZone3.append(" custom=\""
						+ (StringUtils.isEmpty(fieldDefinition.getCustom()) ? ""
								: (("Y").equalsIgnoreCase(fieldDefinition.getCustom().substring(0, 1)) ? true : false))
						+ "\"");
				CommonTemplateUtils.appendStringBuilderOptionalAttribute(xmlStrZone3, "template-type",
						fieldDefinition.getSource(), true);
				CommonTemplateUtils.appendStringBuilderOptionalAttribute(xmlStrZone3, "anonymize",
						fieldDefinition.getAnonymize(), false);
				if ("Object".equalsIgnoreCase(fieldDefinition.getTypeOfField())) {
					xmlStrZone3.append(" object-type=\"" + fieldDefinition.getObjectType() + "\"");
				}
				xmlStrZone3.append(">\n");
				if (StringUtils.isNotEmpty(fieldDefinition.getFieldLabel())) {
					xmlStrZone3.append("<field-label mime-type=\"text-plain\"><![CDATA["
							+ fieldDefinition.getFieldLabel() + "]]></field-label>\n");
				}

				fieldDefinition.getZone3TranslatersList().forEach(translater -> {
					xmlStrZone3.append(
							"<field-label lang=\"" + translater.getTranslateLang() + "\" mime-type=\"text-plain\">");
					xmlStrZone3.append("<![CDATA[" + translater.getFieldDefinitionName() + "]]>");
					xmlStrZone3.append("</field-label>\n");
				});
				if ("Object".equalsIgnoreCase(fieldDefinition.getTypeOfField())
						&& StringUtils.isNotEmpty(fieldDefinition.getPickListOrObjectId())
						&& StringUtils.isNotEmpty(fieldDefinition.getParentPickListOrObject())) {
					xmlStrZone3
							.append("<field-criteria destinationFieldValue=\"" + fieldDefinition.getPickListOrObjectId()
									+ "\" sourceFieldName=\"" + fieldDefinition.getParentPickListOrObject() + "\"/>\n");
				} else if ("picklist".equalsIgnoreCase(fieldDefinition.getTypeOfField())) {
					if (StringUtils.isNotEmpty(fieldDefinition.getPickListOrObjectId())
							&& StringUtils.isNotEmpty(fieldDefinition.getParentPickListOrObject())) {
						xmlStrZone3
								.append("<picklist-id parent-field-id=\"" + fieldDefinition.getParentPickListOrObject()
										+ "\">" + fieldDefinition.getPickListOrObjectId() + "</picklist-id>\n");
					} else if (StringUtils.isNotEmpty(fieldDefinition.getPickListOrObjectId())) {
						xmlStrZone3
								.append("<picklist-id>" + fieldDefinition.getPickListOrObjectId() + "</picklist-id>\n");
					}
				} else if ("enum".equalsIgnoreCase(fieldDefinition.getTypeOfField())) {
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

	private static void buildXmlStrZone4(StringBuilder xmlStrZone4, Zone4DTO zone4dto) {
		if (null != zone4dto && zone4dto.getMobileFieldsList().size() > 0) {
			xmlStrZone4.append("<mobile-fields>\n");
			zone4dto.getMobileFieldsList().forEach(mobileField -> {
				xmlStrZone4.append("<field refid=\"" + mobileField + "\"/>\n");
			});
			xmlStrZone4.append("</mobile-fields>\n");
		}
	}

	private static void buildXmlStrZone5(StringBuilder xmlStrZone5, Zone5DTO zone5dto) {
		if (null != zone5dto && StringUtils.isNotEmpty(zone5dto.getOfferApproversReOrder())) {
			xmlStrZone5.append("<offer-approvers");

			CommonTemplateUtils.appendStringBuilderOptionalAttribute(xmlStrZone5, "editable",
					zone5dto.getOfferApproversEditable(), false);

			xmlStrZone5.append(" reorder=\"" + (StringUtils.isEmpty(zone5dto.getOfferApproversReOrder()) ? ""
					: (("Y").equalsIgnoreCase(zone5dto.getOfferApproversReOrder().substring(0, 1)) ? true : false))
					+ "\">\n");
			zone5dto.getZone5OfferApproversList().forEach(offerApprover -> {
				xmlStrZone5.append("<offerApprovalStep editInvalidUser=\""
						+ (StringUtils.isEmpty(offerApprover.getEditInvalidUser()) ? ""
								: (("Y").equalsIgnoreCase(offerApprover.getEditInvalidUser().substring(0, 1)) ? true
										: false))
						+ "\" editable=\""
						+ (StringUtils.isEmpty(offerApprover.getEditable()) ? ""
								: (("Y").equalsIgnoreCase(offerApprover.getEditable().substring(0, 1)) ? true : false))
						+ "\" id=\"" + offerApprover.getStepId() + "\">\n");
				if (StringUtils.isNotEmpty(offerApprover.getLabel())) {
					xmlStrZone5.append("<field-label><![CDATA[" + offerApprover.getLabel() + "]]></field-label>\n");
				}
				offerApprover.getZone5OfferApproverTranslatersList().forEach(translater -> {
					xmlStrZone5.append("<field-label lang=\"" + translater.getTranslateLang() + "\"><![CDATA["
							+ translater.getOfferApproverName() + "]]></field-label>\n");
				});
				xmlStrZone5.append("<default-user type=\"" + offerApprover.getDefaultUserType() + "\"><![CDATA["
						+ offerApprover.getDefaultUser() + "]]></default-user>\n");
				xmlStrZone5.append("</offerApprovalStep>\n");
			});
			xmlStrZone5.append("</offer-approvers>\n");
		}
	}

	public static OfferApprovalTemplateDTO extractXmlData(MultipartFile file) {
		OfferApprovalTemplateDTO offerApprovalTemplateXmlData = new OfferApprovalTemplateDTO();
		// Zone 2 objects
		Zone2DTO zone2Data = new Zone2DTO();
		List<Zone2TemplateNameTranslaterDTO> zone2TemplateNameTranslatersList = new ArrayList<Zone2TemplateNameTranslaterDTO>();
		List<Zone2TemplateDescriptionTranslaterDTO> zone2TemplateDescriptionTranslatersList = new ArrayList<Zone2TemplateDescriptionTranslaterDTO>();
		// Zone 3 objects
		Zone3DTO zone3Data = new Zone3DTO();
		List<Zone3FieldDefinitionDTO> zone3FieldDefinitionsList = new ArrayList<Zone3FieldDefinitionDTO>();
		// Zone 4 objects
		Zone4DTO zone4Data = new Zone4DTO();
		List<String> mobileFieldsList = new ArrayList<String>();
		// Zone 5 objects
		Zone5DTO zone5Data = new Zone5DTO();
		List<Zone5OfferApproverDTO> zone5OfferApproversList = new ArrayList<Zone5OfferApproverDTO>();
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
						Zone2TemplateNameTranslaterDTO tNameTranslater = new Zone2TemplateNameTranslaterDTO();
						tNameTranslater.setTranslateLang(element.getAttribute("lang"));
						tNameTranslater.setTemplateName(element.getTextContent());
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
			NodeList n_fieldDefinitionsList = document.getElementsByTagName("field-definition");
			for (int i = 0; i < n_fieldDefinitionsList.getLength(); i++) {
				Node node = n_fieldDefinitionsList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Zone3FieldDefinitionDTO zone3FieldDefinition = new Zone3FieldDefinitionDTO();
					Element element = (Element) node;
					zone3FieldDefinition.setFieldId(element.getAttribute("id"));
					zone3FieldDefinition.setTypeOfField(element.getAttribute("type"));
					zone3FieldDefinition.setRequired(element.getAttribute("required"));
					zone3FieldDefinition.setCustom(element.getAttribute("custom"));
					zone3FieldDefinition.setSource(element.getAttribute("template-type"));
					zone3FieldDefinition.setAnonymize(element.getAttribute("anonymize"));
					zone3FieldDefinition.setObjectType(element.getAttribute("object-type"));

					NodeList n_fieldLabelsList = element.getElementsByTagName("field-label");
					List<Zone3FieldDefinitionTranslaterDTO> zone3FieldDefinitionTranslatersList = new ArrayList<Zone3FieldDefinitionTranslaterDTO>();
					for (int j = 0; j < n_fieldLabelsList.getLength(); j++) {
						Node nodeLabel = n_fieldLabelsList.item(j);
						if (nodeLabel.getNodeType() == Node.ELEMENT_NODE) {
							Element elementLabel = (Element) nodeLabel;
							if (StringUtils.isEmpty(elementLabel.getAttribute("lang"))) {
								zone3FieldDefinition.setFieldLabel(elementLabel.getTextContent());
							} else {
								Zone3FieldDefinitionTranslaterDTO labelTranslater = new Zone3FieldDefinitionTranslaterDTO();
								labelTranslater.setTranslateLang(elementLabel.getAttribute("lang"));
								labelTranslater.setFieldDefinitionName(elementLabel.getTextContent());
								zone3FieldDefinitionTranslatersList.add(labelTranslater);
							}
						}
					}
					if ("object".equalsIgnoreCase(zone3FieldDefinition.getTypeOfField())) {
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
					} else if ("enum".equalsIgnoreCase(zone3FieldDefinition.getTypeOfField())) {
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
					} else if ("picklist".equalsIgnoreCase(zone3FieldDefinition.getTypeOfField())) {
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

					zone3FieldDefinition.setZone3TranslatersList(zone3FieldDefinitionTranslatersList);
					zone3FieldDefinitionsList.add(zone3FieldDefinition);
				}
			}
			zone3Data.setZone3FieldDefinitionsList(zone3FieldDefinitionsList);
			/* Zone3 ends here */

			/* Zone4 starts here */
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
			zone4Data.setMobileFieldsList(mobileFieldsList);
			/* Zone4 ends here */

			/* Zone5 starts here */
			NodeList n_offerApproversList = document.getElementsByTagName("offer-approvers");
			for (int i = 0; i < n_offerApproversList.getLength(); i++) {
				Node node = n_offerApproversList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					zone5Data.setOfferApproversEditable(element.getAttribute("editable"));
					zone5Data.setOfferApproversReOrder(element.getAttribute("reorder"));
					NodeList n_offerApproverStepsList = element.getElementsByTagName("offerApprovalStep");
					for (int j = 0; j < n_offerApproverStepsList.getLength(); j++) {
						Node nodeStep = n_offerApproverStepsList.item(j);
						if (nodeStep.getNodeType() == Node.ELEMENT_NODE) {
							Zone5OfferApproverDTO offerApprover = new Zone5OfferApproverDTO();
							Element elementStep = (Element) nodeStep;
							offerApprover.setEditInvalidUser(elementStep.getAttribute("editInvalidUser"));
							offerApprover.setEditable(elementStep.getAttribute("editable"));
							offerApprover.setStepId(elementStep.getAttribute("id"));
							NodeList n_offerApproverLabelsList = elementStep.getElementsByTagName("field-label");
							List<Zone5OfferApproverTranslaterDTO> zone5OfferApproverTranslatersList = new ArrayList<Zone5OfferApproverTranslaterDTO>();
							for (int k = 0; k < n_offerApproverLabelsList.getLength(); k++) {
								Node nodeLabel = n_offerApproverLabelsList.item(k);
								if (nodeLabel.getNodeType() == Node.ELEMENT_NODE) {
									Element elementLabel = (Element) nodeLabel;
									if (StringUtils.isEmpty(elementLabel.getAttribute("lang"))) {
										offerApprover.setLabel(elementLabel.getTextContent());
									} else {
										Zone5OfferApproverTranslaterDTO labelTranslater = new Zone5OfferApproverTranslaterDTO();
										labelTranslater.setTranslateLang(elementLabel.getAttribute("lang"));
										labelTranslater.setOfferApproverName(elementLabel.getTextContent());
										zone5OfferApproverTranslatersList.add(labelTranslater);
									}
								}
							}
							NodeList n_offerApproverDefaultUsersList = elementStep.getElementsByTagName("default-user");
							for (int k = 0; k < n_offerApproverDefaultUsersList.getLength(); k++) {
								Node nodeDefaultUser = n_offerApproverDefaultUsersList.item(k);
								if (nodeDefaultUser.getNodeType() == Node.ELEMENT_NODE) {
									Element elementDefaultUser = (Element) nodeDefaultUser;
									offerApprover.setDefaultUser(elementDefaultUser.getAttribute("type"));
									offerApprover.setDefaultUserType(elementDefaultUser.getTextContent());
								}
							}
							offerApprover.setZone5OfferApproverTranslatersList(zone5OfferApproverTranslatersList);
							zone5OfferApproversList.add(offerApprover);
						}
					}
				}
			}
			zone5Data.setZone5OfferApproversList(zone5OfferApproversList);
			/* Zone5 ends here */

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
		zone4Data.getMobileFieldsList().forEach(mobileField -> {
			zone3Data.getZone3FieldDefinitionsList().forEach(fieldDef -> {
				if (mobileField.equalsIgnoreCase(fieldDef.getFieldId())) {
					fieldDef.setMobile("true");
				}
			});
		});
		offerApprovalTemplateXmlData.setZone2(zone2Data);
		offerApprovalTemplateXmlData.setZone3(zone3Data);
		offerApprovalTemplateXmlData.setZone4(zone4Data);
		offerApprovalTemplateXmlData.setZone5(zone5Data);
		offerApprovalTemplateXmlData.setEnumValuesList(enumValuesList);
		return offerApprovalTemplateXmlData;
	}

	public static String buildExcelFile(OfferApprovalTemplateDTO offerApprovalTemplateXmlData, String fileName) {
		File sourceFile = new File("src\\main\\resources\\templates\\REC_Templates\\Offer_Approval_RPA_Template.xlsx");
		File destinationFile = new File("src\\main\\resources\\templates\\" + fileName + ".xlsx");
		try {
			FileUtils.copyFile(sourceFile, destinationFile);
			XSSFWorkbook workBook;
			FileInputStream destinationFileStream = new FileInputStream(destinationFile);
			workBook = new XSSFWorkbook(destinationFileStream);
			XSSFSheet offerApprovalSheet = workBook.getSheet("Offer_Approval");
			XSSFRow row;
			int rowNum = 0;
			int rowNumApprovesStart = 0;
			int rowNumOfferApprovalSheetStart = 0;
			Set<String> translaterLangsSet = new HashSet<String>();
			Iterator<Row> rowsOfferApprovalSheet = null;
			HashMap<String, String> validationsLangsMap = CommonTemplateUtils.getValidationsLangsMap(workBook,
					"Validations");
			List<String> validationsLangsList = new ArrayList<String>();
			validationsLangsMap.forEach((key, value) -> {
				if (value.contains("_")) {
					validationsLangsList.add(value);
				}
			});
			CommonTemplateUtils.buildWorkBookEnumSheet(offerApprovalTemplateXmlData.getZone2().getTemplateName(),
					offerApprovalTemplateXmlData.getZone2().getTemplateDescription(),
					offerApprovalTemplateXmlData.getEnumValuesList(), "Enum_Values", workBook, validationsLangsMap,
					validationsLangsList);
			if (null != offerApprovalSheet) {
				offerApprovalTemplateXmlData.getZone2().getZone2TemplateNameTranslatersList().forEach(translaterObj -> {
					translaterLangsSet.add(translaterObj.getTranslateLang());
				});
				offerApprovalTemplateXmlData.getZone2().getZone2TemplateDescriptionTranslatersList()
						.forEach(translaterObj -> {
							translaterLangsSet.add(translaterObj.getTranslateLang());
						});
				offerApprovalTemplateXmlData.getZone3().getZone3FieldDefinitionsList().forEach(fieldDefObj -> {
					fieldDefObj.getZone3TranslatersList().forEach(translaterObj -> {
						translaterLangsSet.add(translaterObj.getTranslateLang());
					});
				});
				offerApprovalTemplateXmlData.getZone5().getZone5OfferApproversList().forEach(offerApproverObj -> {
					offerApproverObj.getZone5OfferApproverTranslatersList().forEach(translaterObj -> {
						translaterLangsSet.add(translaterObj.getTranslateLang());
					});
				});
				List<String> translaterLangsList = new ArrayList<>(translaterLangsSet);
				for (int i = 0; i < translaterLangsList.size(); i++) {
					CommonTemplateUtils.createColumnTransLang(workBook, offerApprovalSheet, 21 + i, 22 + i, 3,
							validationsLangsMap, translaterLangsList, i);

					CommonTemplateUtils.addDataValidation255CharsAbove(workBook, offerApprovalSheet, 3, 3, 22 + i,
							22 + i, validationsLangsList.toArray(new String[0]), "Validations", "!$A$2:$A$47");
				}
				for (int i = 0; i < offerApprovalTemplateXmlData.getZone3().getZone3FieldDefinitionsList()
						.size(); i++) {
					if (i < offerApprovalTemplateXmlData.getZone3().getZone3FieldDefinitionsList().size() - 1)
						CommonTemplateUtils.createRow(workBook, offerApprovalSheet, 4 + i, 5 + i);
					fillRowDataFieldDefenition(offerApprovalSheet, offerApprovalSheet.getRow(4 + i),
							offerApprovalTemplateXmlData.getZone3().getZone3FieldDefinitionsList().get(i),
							translaterLangsList);
				}
				rowsOfferApprovalSheet = offerApprovalSheet.rowIterator();
				while (null != rowsOfferApprovalSheet && rowsOfferApprovalSheet.hasNext()
						&& (rowNumApprovesStart == 0 || rowNumOfferApprovalSheetStart == 0)) {
					rowNum++;
					row = (XSSFRow) rowsOfferApprovalSheet.next();
					if (row.getCell(3) != null
							&& "Template Name".equalsIgnoreCase(CommonTemplateUtils.readCellValue(row.getCell(3)))) {
						rowNumOfferApprovalSheetStart = rowNum;
					}
					if (row.getCell(3) != null && "PRE-DEFINED OFFER APPROVAL WORKFLOW"
							.equalsIgnoreCase(CommonTemplateUtils.readCellValue(row.getCell(3)))) {
						rowNumApprovesStart = rowNum;
					}
				}
				offerApprovalSheet.getRow(rowNumOfferApprovalSheetStart - 1).getCell(4)
						.setCellValue(offerApprovalTemplateXmlData.getZone2().getTemplateName());
				offerApprovalTemplateXmlData.getZone2().getZone2TemplateNameTranslatersList().forEach(translaterObj -> {
					offerApprovalSheet.getRow(1)
							.getCell(21 + translaterLangsList.indexOf(translaterObj.getTranslateLang()))
							.setCellValue(translaterObj.getTemplateName());
				});
				offerApprovalSheet.getRow(rowNumOfferApprovalSheetStart).getCell(4)
						.setCellValue(offerApprovalTemplateXmlData.getZone2().getTemplateDescription());
				offerApprovalTemplateXmlData.getZone2().getZone2TemplateDescriptionTranslatersList()
						.forEach(translaterObj -> {
							offerApprovalSheet.getRow(2)
									.getCell(21 + translaterLangsList.indexOf(translaterObj.getTranslateLang()))
									.setCellValue(translaterObj.getTemplateDescription());
						});
				offerApprovalSheet.getRow(rowNumApprovesStart).getCell(4).setCellValue(CommonTemplateUtils
						.convertToYesOrNo(offerApprovalTemplateXmlData.getZone5().getOfferApproversReOrder()));
				CommonTemplateUtils.addDataValidation(offerApprovalSheet, rowNumApprovesStart, rowNumApprovesStart, 4,
						4, new String[] { "Y", "N" });
				offerApprovalSheet.getRow(rowNumApprovesStart + 1).getCell(4).setCellValue(CommonTemplateUtils
						.convertToYesOrNo(offerApprovalTemplateXmlData.getZone5().getOfferApproversEditable()));
				CommonTemplateUtils.addDataValidation(offerApprovalSheet, rowNumApprovesStart + 1,
						rowNumApprovesStart + 1, 4, 4, new String[] { "Y", "N" });
				for (int i = 0; i < offerApprovalTemplateXmlData.getZone5().getZone5OfferApproversList().size(); i++) {
					if (i < offerApprovalTemplateXmlData.getZone5().getZone5OfferApproversList().size() - 1)
						CommonTemplateUtils.createRow(workBook, offerApprovalSheet,
								offerApprovalSheet.getLastRowNum() - 1, offerApprovalSheet.getLastRowNum());
					fillRowDataOfferApprover(offerApprovalSheet,
							offerApprovalSheet.getRow((rowNumApprovesStart + 3) + i),
							offerApprovalTemplateXmlData.getZone5().getZone5OfferApproversList().get(i),
							translaterLangsList);
				}
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

	private static void fillRowDataFieldDefenition(XSSFSheet workSheet, XSSFRow row,
			Zone3FieldDefinitionDTO zone3FieldDefinitionDTO, List<String> translaterLangsList) {
		row.getCell(3).setCellValue(zone3FieldDefinitionDTO.getFieldId());
		row.getCell(4).setCellValue(zone3FieldDefinitionDTO.getFieldLabel());
		row.getCell(5).setCellValue(zone3FieldDefinitionDTO.getSource());
		CommonTemplateUtils.addDataValidation(workSheet, row.getRowNum(), row.getRowNum(), 5, 5,
				new String[] { "job-req", "job-application" });
		row.getCell(6).setCellValue(zone3FieldDefinitionDTO.getTypeOfField());
		CommonTemplateUtils.addDataValidation(workSheet, row.getRowNum(), row.getRowNum(), 6, 6,
				new String[] { "text", "textarea", "date", "percent", "bool", "enum", "picklist", "number", "object",
						"operator", "derived", "richText", "attachment", "multiattachment" });
		row.getCell(7).setCellValue(zone3FieldDefinitionDTO.getParentPickListOrObject());
		row.getCell(8).setCellValue(zone3FieldDefinitionDTO.getPickListOrObjectId());
		row.getCell(9).setCellValue(zone3FieldDefinitionDTO.getObjectType());
		row.getCell(10).setCellValue(CommonTemplateUtils.convertToYesOrNo(zone3FieldDefinitionDTO.getRequired()));
		row.getCell(11).setCellValue(CommonTemplateUtils.convertToYesOrNo(zone3FieldDefinitionDTO.getCustom()));
		row.getCell(12).setCellValue(CommonTemplateUtils.convertToYesOrNo(zone3FieldDefinitionDTO.getAnonymize()));
		row.getCell(13).setCellValue(CommonTemplateUtils.convertToYesOrNo(zone3FieldDefinitionDTO.getMobile()));
		row.getCell(14).setCellValue(CommonTemplateUtils.convertToYesOrNo(zone3FieldDefinitionDTO.getCustomToken()));
		row.getCell(15).setCellValue(CommonTemplateUtils.convertToYesOrNo(zone3FieldDefinitionDTO.getReportable()));
		row.getCell(16).setCellValue(zone3FieldDefinitionDTO.getHelpText());
		row.getCell(17).setCellValue(zone3FieldDefinitionDTO.getNotes());
		zone3FieldDefinitionDTO.getZone3TranslatersList().forEach(translaterObj -> {
			row.getCell(21 + translaterLangsList.indexOf(translaterObj.getTranslateLang()))
					.setCellValue(translaterObj.getFieldDefinitionName());
			workSheet.autoSizeColumn(21 + translaterLangsList.indexOf(translaterObj.getTranslateLang()));
		});
	}

	private static void fillRowDataOfferApprover(XSSFSheet workSheet, XSSFRow row,
			Zone5OfferApproverDTO zone5OfferApproverDTO, List<String> translaterLangsList) {
		row.getCell(3).setCellValue(zone5OfferApproverDTO.getStepId());
		row.getCell(4).setCellValue(CommonTemplateUtils.convertToYesOrNo(zone5OfferApproverDTO.getEditable()));
		CommonTemplateUtils.addDataValidation(workSheet, row.getRowNum(), row.getRowNum(), 4, 4,
				new String[] { "Y", "N" });
		row.getCell(5).setCellValue(CommonTemplateUtils.convertToYesOrNo(zone5OfferApproverDTO.getEditInvalidUser()));
		CommonTemplateUtils.addDataValidation(workSheet, row.getRowNum(), row.getRowNum(), 5, 5,
				new String[] { "Y", "N" });
		row.getCell(6).setCellValue(zone5OfferApproverDTO.getLabel());
		row.getCell(7).setCellValue(zone5OfferApproverDTO.getDefaultUserType());
		row.getCell(8).setCellValue(zone5OfferApproverDTO.getDefaultUser());
		zone5OfferApproverDTO.getZone5OfferApproverTranslatersList().forEach(translaterObj -> {
			row.getCell(21 + translaterLangsList.indexOf(translaterObj.getTranslateLang()))
					.setCellValue(translaterObj.getOfferApproverName());
			workSheet.autoSizeColumn(21 + translaterLangsList.indexOf(translaterObj.getTranslateLang()));
		});
	}
}
