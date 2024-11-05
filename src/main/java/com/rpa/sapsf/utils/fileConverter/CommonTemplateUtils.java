package com.rpa.sapsf.utils.fileConverter;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.xml.sax.InputSource;

import com.rpa.sapsf.dto.EnumDTO;
import com.rpa.sapsf.dto.EnumLabelTranslaterDTO;

import io.micrometer.common.util.StringUtils;

public class CommonTemplateUtils {

	public static String readCellValue(XSSFCell cell) {
		if (null != cell) {
			switch (cell.getCellType()) {
			case STRING:
				return cell.getStringCellValue();
			case NUMERIC:
				return Integer.toString((int) cell.getNumericCellValue());
			case BOOLEAN:
				return String.valueOf(cell.getBooleanCellValue());
			default:
				break;
			}
		}
		return null;
	}

	public static void appendStringBuilderOptionalAttribute(StringBuilder xmlString, String attribute,
			String optionalValue, boolean isString) {
		if (StringUtils.isNotEmpty(optionalValue)) {
			if (isString) {
				xmlString.append(" " + attribute + "=\"" + optionalValue + "\"");
			} else {
				xmlString.append(" " + attribute + "=\""
						+ (("Y").equalsIgnoreCase(optionalValue.substring(0, 1)) ? true : false) + "\"");
			}
		}
	}

	public static Document convertStringToDocument(String xmlString) {
		xmlString = xmlString.trim().replaceAll("\n", "");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String convertDocumentToXml(Document doc, String fileName, String templateName, String dtdFileName) {
		doc.setXmlStandalone(true);
		TransformerFactory tranFactory = TransformerFactory.newInstance();
		try {
			Transformer transformer = tranFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			DOMImplementation domImpl = doc.getImplementation();
			DocumentType doctype = domImpl.createDocumentType("doctype", templateName, dtdFileName);
			transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
			Source docContent = new DOMSource(doc);
			Result destination = new StreamResult(new File(fileName + ".xml"));
			transformer.transform(docContent, destination);
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileName + ".xml";
	}

	public static String readOriginalFileName(MultipartFile file) {
		String fileName = file.getOriginalFilename();
		if (StringUtils.isNotEmpty(fileName) && fileName.contains(".xlsx")) {
			fileName = fileName.replace(".xlsx", "");
		} else if (StringUtils.isNotEmpty(fileName) && fileName.contains(".xml")) {
			fileName = fileName.replace(".xml", "");
		}
		return fileName;
	}

	public static void extractEnumData(String sheetName, XSSFWorkbook workBook, List<EnumDTO> enumValuesList) {
		XSSFSheet sheetEnum = workBook.getSheet(sheetName);
		XSSFRow row;
		int rowNumEnum = 0;
		int rowNumEnumSheetStart = 0;
		List<String> translateLangsEnumList = new ArrayList<String>();
		Iterator<Row> rowsEnum = null;
		if (null != sheetEnum)
			rowsEnum = sheetEnum.rowIterator();
		/* Enum read starts here */
		while (null != rowsEnum && rowsEnum.hasNext()) {
			rowNumEnum++;
			row = (XSSFRow) rowsEnum.next();
			if (row.getCell(3) != null
					&& "Template Name".equalsIgnoreCase(CommonTemplateUtils.readCellValue(row.getCell(3)))) {
				rowNumEnumSheetStart = rowNumEnum;
			}
			if (rowNumEnumSheetStart != 0) {
				if (rowNumEnum == rowNumEnumSheetStart + 2) {
					int colNum = 9;
					while (null != row.getCell(colNum)
							&& StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colNum)))) {
						translateLangsEnumList
								.add(CommonTemplateUtils.readCellValue(row.getCell(colNum)).substring(0, 5));
						colNum++;
					}
				} else if (rowNumEnum > (rowNumEnumSheetStart + 2)) {
					if (null != row.getCell(3)
							&& StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(3)))) {
						EnumDTO enumObj = new EnumDTO();
						enumObj.setEnumFieldId(CommonTemplateUtils.readCellValue(row.getCell(3)));
						enumObj.setEnumFieldLabel(CommonTemplateUtils.readCellValue(row.getCell(4)));
						enumObj.setEnumValue(CommonTemplateUtils.readCellValue(row.getCell(5)));
						enumObj.setEnumLabel(CommonTemplateUtils.readCellValue(row.getCell(6)));
						int colNum = 9;
						int indexEnum = 0;
						List<EnumLabelTranslaterDTO> enumLabelTranslatersList = new ArrayList<EnumLabelTranslaterDTO>();
						while (null != row.getCell(colNum)) {
							if (StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(colNum)))
									&& indexEnum < translateLangsEnumList.size()) {
								EnumLabelTranslaterDTO enumLabelTranslaterData = new EnumLabelTranslaterDTO();
								enumLabelTranslaterData
										.setEnumLabel(CommonTemplateUtils.readCellValue(row.getCell(colNum)));
								enumLabelTranslaterData.setTranslateLang(translateLangsEnumList.get(indexEnum));
								enumLabelTranslatersList.add(enumLabelTranslaterData);
							}
							colNum++;
							indexEnum++;
						}
						enumObj.setEnumLabelTranslatersList(enumLabelTranslatersList);
						enumValuesList.add(enumObj);
					}

				}
			}
		}
		/* Enum read ends here */
	}

	public static HashMap<String, String> getValidationsLangsMap(XSSFWorkbook workBook, String sheetName) {
		XSSFSheet validationsSheet = workBook.getSheet(sheetName);
		Iterator<Row> rowsValidationsSheet = null;
		XSSFRow row;
		int rowNum = 0;
		LinkedHashMap<String, String> validationsLangsMap = new LinkedHashMap<String, String>();
		if (null != validationsSheet) {
			rowsValidationsSheet = validationsSheet.rowIterator();
			while (null != rowsValidationsSheet && rowsValidationsSheet.hasNext()) {
				rowNum++;
				row = (XSSFRow) rowsValidationsSheet.next();
				if (null != row.getCell(0)
						&& StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(0)))) {
					validationsLangsMap.put(CommonTemplateUtils.readCellValue(row.getCell(0)).substring(0, 5),
							CommonTemplateUtils.readCellValue(row.getCell(0)));
				}
			}
		}
		return validationsLangsMap;
	}

	public static void createRow(XSSFWorkbook workbook, XSSFSheet worksheet, int sourceRowNum, int destinationRowNum) {
		worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1, true, false);
		// Get the source / new row
		XSSFRow sourceRow = worksheet.getRow(sourceRowNum);
		XSSFRow newRow = worksheet.createRow(destinationRowNum);
		// Loop through source columns to add to new row
		for (int i = 0; null != sourceRow && i < sourceRow.getLastCellNum(); i++) {
			// Grab a copy of the old/new cell
			XSSFCell oldCell = sourceRow.getCell(i);
			XSSFCell newCell = newRow.createCell(i);
			// If the old cell is null jump to next cell
			if (oldCell == null) {
				newCell = null;
				continue;
			}
			// Copy style from old cell and apply to new cell
			/*
			 * XSSFCellStyle newCellStyle = workbook.createCellStyle();
			 * newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
			 */
			newCell.setCellStyle(oldCell.getCellStyle());
			// If there is a cell comment, copy
			if (oldCell.getCellComment() != null) {
				newCell.setCellComment(oldCell.getCellComment());
			}
			// If there is a cell hyperlink, copy
			if (oldCell.getHyperlink() != null) {
				newCell.setHyperlink(oldCell.getHyperlink());
			}

			// If there is a cell hyperlink, copy
			if (oldCell.getHyperlink() != null) {
				newCell.setHyperlink(oldCell.getHyperlink());
			}
			// Set the cell data type
			newCell.setCellType(oldCell.getCellType());
			newCell.setCellValue(oldCell.getRawValue());
			// Set the cell data value
			switch (oldCell.getCellType()) {
			case BLANK:
				newCell.setCellValue(oldCell.getStringCellValue());
				break;
			case BOOLEAN:
				newCell.setCellValue(oldCell.getBooleanCellValue());
				break;
			case ERROR:
				newCell.setCellErrorValue(oldCell.getErrorCellValue());
				break;
			case FORMULA:
				newCell.setCellFormula(oldCell.getCellFormula());
				break;
			case NUMERIC:
				newCell.setCellValue(oldCell.getNumericCellValue());
				break;
			case STRING:
				newCell.setCellValue(oldCell.getRichStringCellValue());
				break;
			}

		}

		// If there are are any merged regions in the source row, copy to new row
		for (int i = 0; i < worksheet.getNumMergedRegions(); i++) {
			CellRangeAddress cellRangeAddress = worksheet.getMergedRegion(i);
			if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum()) {
				CellRangeAddress newCellRangeAddress = new CellRangeAddress(newRow.getRowNum(),
						(newRow.getRowNum() + (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow())),
						cellRangeAddress.getFirstColumn(), cellRangeAddress.getLastColumn());
				worksheet.addMergedRegion(newCellRangeAddress);
			}
		}
	}

	public static void createColumnTransLang(XSSFWorkbook workbook, XSSFSheet worksheet, int sourceColNum,
			int destinationColNum, int rowNumTransStart, HashMap<String, String> validationsLangsMap,
			List<String> translaterLangsList, int index) {
		worksheet.shiftColumns(destinationColNum, worksheet.getRow(3).getLastCellNum(), 1);
		for (Row currentRow : worksheet) {
			XSSFCell oldCell = (XSSFCell) currentRow.getCell(sourceColNum);
			XSSFCell newCell = (XSSFCell) currentRow.createCell(destinationColNum);
			// If the old cell is null jump to next cell
			if (oldCell == null) {
				newCell = null;
				continue;
			}
			// Copy style from old cell and apply to new cell
			XSSFCellStyle newCellStyle = workbook.createCellStyle();
			newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
			newCell.setCellStyle(newCellStyle);
			// If there is a cell comment, copy
			if (oldCell.getCellComment() != null) {
				newCell.setCellComment(oldCell.getCellComment());
			}
			// If there is a cell hyperlink, copy
			if (oldCell.getHyperlink() != null) {
				newCell.setHyperlink(oldCell.getHyperlink());
			}
			// Set the cell data type
			newCell.setCellType(oldCell.getCellType());
			newCell.setCellValue(oldCell.getRawValue());
			// Set the cell data value
			switch (oldCell.getCellType()) {
			case BLANK:
				newCell.setCellValue(oldCell.getStringCellValue());
				break;
			case BOOLEAN:
				newCell.setCellValue(oldCell.getBooleanCellValue());
				break;
			case ERROR:
				newCell.setCellErrorValue(oldCell.getErrorCellValue());
				break;
			case FORMULA:
				newCell.setCellFormula(oldCell.getCellFormula());
				break;
			case NUMERIC:
				newCell.setCellValue(oldCell.getNumericCellValue());
				break;
			case STRING:
				newCell.setCellValue(oldCell.getRichStringCellValue());
				break;
			}
			if (currentRow.getRowNum() == rowNumTransStart)
				oldCell.setCellValue(validationsLangsMap.get(translaterLangsList.get(index)));
		}
	}

	public static void buildWorkBookEnumSheet(String templateName, String templateDescription,
			List<EnumDTO> enumValuesList, String sheetName, XSSFWorkbook workBook,
			HashMap<String, String> validationsLangsMap, List<String> validationsLangsList) {
		XSSFSheet enumSheet = workBook.getSheet(sheetName);
		Set<String> translaterLangsSet = new HashSet<String>();
		XSSFRow row;
		int rowNum = 0;
		int rowNumSheetStart = 0;
		Iterator<Row> rowsEnumSheet = null;
		if (null != enumSheet) {
			rowsEnumSheet = enumSheet.rowIterator();
			while (null != rowsEnumSheet && rowsEnumSheet.hasNext() && rowNumSheetStart == 0) {
				rowNum++;
				row = (XSSFRow) rowsEnumSheet.next();
				if (row.getCell(3) != null
						&& "Template Name".equalsIgnoreCase(CommonTemplateUtils.readCellValue(row.getCell(3)))) {
					rowNumSheetStart = rowNum;
				}
			}
			enumSheet.getRow(rowNumSheetStart).getCell(4).setCellValue(templateName);
			enumSheet.getRow(rowNumSheetStart + 1).getCell(4).setCellValue(templateDescription);
			enumValuesList.forEach(enumObj -> {
				enumObj.getEnumLabelTranslatersList().forEach(translaterObj -> {
					translaterLangsSet.add(translaterObj.getTranslateLang());
				});
			});
			List<String> translaterLangsList = new ArrayList<>(translaterLangsSet);
			for (int i = 0; i < translaterLangsList.size(); i++) {
				createColumnTransLang(workBook, enumSheet, 9 + i, 10 + i, rowNumSheetStart + 2, validationsLangsMap,
						translaterLangsList, i);

				addDataValidation255CharsAbove(workBook, enumSheet, rowNumSheetStart + 2, rowNumSheetStart + 2, 10 + i,
						10 + i, validationsLangsList.toArray(new String[0]), "Validations", "!$A$2:$A$47");
			}

			for (int i = 0; i < enumValuesList.size(); i++) {
				if (i < enumValuesList.size() - 1)
					CommonTemplateUtils.createRow(workBook, enumSheet, 4 + i, 5 + i);
				fillRowDataEnum(enumSheet, enumSheet.getRow(4 + i), enumValuesList.get(i), translaterLangsList);
			}
		}
	}

	private static void fillRowDataEnum(XSSFSheet enumSheet, XSSFRow row, EnumDTO enumDTO,
			List<String> translaterLangsList) {
		row.getCell(3).setCellValue(enumDTO.getEnumFieldId());
		row.getCell(4).setCellValue(enumDTO.getEnumFieldLabel());
		row.getCell(5).setCellValue(enumDTO.getEnumValue());
		row.getCell(6).setCellValue(enumDTO.getEnumLabel());
		enumDTO.getEnumLabelTranslatersList().forEach(translaterObj -> {
			row.getCell(9 + translaterLangsList.indexOf(translaterObj.getTranslateLang()))
					.setCellValue(translaterObj.getEnumLabel());
			enumSheet.setColumnWidth(9 + translaterLangsList.indexOf(translaterObj.getTranslateLang()), 8000);
		});
	}

	public static void addDataValidation(XSSFSheet workSheet, int firstRow, int lastRow, int firstCol, int lastCol,
			String[] listOfValues) {
		DataValidation dataValidation = null;
		DataValidationConstraint constraint = null;
		DataValidationHelper validationHelper = null;
		validationHelper = new XSSFDataValidationHelper(workSheet);
		CellRangeAddressList addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
		constraint = validationHelper.createExplicitListConstraint(listOfValues);
		dataValidation = validationHelper.createValidation(constraint, addressList);
		dataValidation.setSuppressDropDownArrow(true);
		workSheet.addValidationData(dataValidation);
	}

	public static void addDataValidation255CharsAbove(Workbook workBook, XSSFSheet workSheet, int firstRow, int lastRow,
			int firstCol, int lastCol, String[] listOfValues, String sheetNameHidden, String cellRange) {
		/*
		 * XSSFSheet hiddenSheet = (XSSFSheet) workBook.getSheet(sheetNameHidden);
		 * String formulae = sheetNameHidden.concat(cellRange); if (null == hiddenSheet)
		 * { addHiddenSheet(workBook, listOfValues, sheetNameHidden); formulae =
		 * sheetNameHidden.concat("!$A$2:$A"); } Name namedCell = workBook.createName();
		 * if (null == workBook.getName(sheetNameHidden))
		 * namedCell.setNameName(sheetNameHidden); namedCell.setRefersToFormula(formulae
		 * + listOfValues.length);
		 * 
		 * XSSFDataValidationHelper dataValidationHelper = new
		 * XSSFDataValidationHelper(workSheet); XSSFDataValidationConstraint
		 * dataValidationonstraint = (XSSFDataValidationConstraint) dataValidationHelper
		 * .createFormulaListConstraint(sheetNameHidden); CellRangeAddressList
		 * addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
		 * XSSFDataValidation dataValidation = (XSSFDataValidation) dataValidationHelper
		 * .createValidation(dataValidationonstraint, addressList);
		 * workSheet.addValidationData(dataValidation);
		 */
	}

	private static void addHiddenSheet(Workbook workBook, String[] values, String hiddenSheetName) {
		XSSFSheet hiddenSheet = (XSSFSheet) workBook.createSheet(hiddenSheetName);
		for (int i = 0, length = values.length; i < length; i++) {
			String name = values[i];
			Row row = ((XSSFSheet) hiddenSheet).createRow(i);
			Cell cell = row.createCell(0);
			cell.setCellValue(name);
		}
		workBook.setSheetHidden(1, true);
	}

	public static String convertToYesOrNo(String input) {
		return (StringUtils.isEmpty(input) ? input : ("true".equalsIgnoreCase(input) ? "Y" : "N"));
	}

	public static String convertToYesOrNoLabel(String input) {
		return (StringUtils.isEmpty(input) ? input : ("true".equalsIgnoreCase(input) ? "Yes" : "No"));
	}

	public static void createColumnFieldPermission(XSSFWorkbook workbook, XSSFSheet worksheet, int sourceColNum,
			int destinationColNum, int rowNumSheetStart, List<String> roleNamesList, List<String> countriesList,
			List<String> sourcesList, int index) {
		worksheet.shiftColumns(destinationColNum, worksheet.getRow(3).getLastCellNum(), 1);
		for (Row currentRow : worksheet) {
			XSSFCell oldCell = (XSSFCell) currentRow.getCell(sourceColNum);
			XSSFCell newCell = (XSSFCell) currentRow.createCell(destinationColNum);
			// If the old cell is null jump to next cell
			if (oldCell == null) {
				newCell = null;
				continue;
			}
			// Copy style from old cell and apply to new cell
			XSSFCellStyle newCellStyle = workbook.createCellStyle();
			newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
			newCell.setCellStyle(newCellStyle);
			// If there is a cell comment, copy
			if (oldCell.getCellComment() != null) {
				newCell.setCellComment(oldCell.getCellComment());
			}
			// If there is a cell hyperlink, copy
			if (oldCell.getHyperlink() != null) {
				newCell.setHyperlink(oldCell.getHyperlink());
			}
			// Set the cell data type
			newCell.setCellType(oldCell.getCellType());
			newCell.setCellValue(oldCell.getRawValue());
			// Set the cell data value
			switch (oldCell.getCellType()) {
			case BLANK:
				newCell.setCellValue(oldCell.getStringCellValue());
				break;
			case BOOLEAN:
				newCell.setCellValue(oldCell.getBooleanCellValue());
				break;
			case ERROR:
				newCell.setCellErrorValue(oldCell.getErrorCellValue());
				break;
			case FORMULA:
				newCell.setCellFormula(oldCell.getCellFormula());
				break;
			case NUMERIC:
				newCell.setCellValue(oldCell.getNumericCellValue());
				break;
			case STRING:
				newCell.setCellValue(oldCell.getRichStringCellValue());
				break;
			}
			if (currentRow.getRowNum() == (rowNumSheetStart + 1))
				oldCell.setCellValue(roleNamesList.get(index));
			if (currentRow.getRowNum() == (rowNumSheetStart + 2)) {
				oldCell.setCellValue(sourcesList.get(index));
				addDataValidation(worksheet, rowNumSheetStart + 2, rowNumSheetStart + 2, sourceColNum, sourceColNum,
						new String[] { "Internal", "External", "*" });
			}
			if (currentRow.getRowNum() == (rowNumSheetStart + 3))
				oldCell.setCellValue(countriesList.get(index));
		}
	}

	public static void createColumnFieldPermission(XSSFWorkbook workbook, XSSFSheet worksheet, int sourceColNum,
			int destinationColNum, int rowNumSheetStart, List<String> roleNamesList, List<String> statusesList,
			int index, List<String> validationsJrdmRolesList) {
		worksheet.shiftColumns(destinationColNum, worksheet.getRow(3).getLastCellNum(), 1);
		for (Row currentRow : worksheet) {
			XSSFCell oldCell = (XSSFCell) currentRow.getCell(sourceColNum);
			XSSFCell newCell = (XSSFCell) currentRow.createCell(destinationColNum);
			// If the old cell is null jump to next cell
			if (oldCell == null) {
				newCell = null;
				continue;
			}
			// Copy style from old cell and apply to new cell
			XSSFCellStyle newCellStyle = workbook.createCellStyle();
			newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
			newCell.setCellStyle(newCellStyle);
			// If there is a cell comment, copy
			if (oldCell.getCellComment() != null) {
				newCell.setCellComment(oldCell.getCellComment());
			}
			// If there is a cell hyperlink, copy
			if (oldCell.getHyperlink() != null) {
				newCell.setHyperlink(oldCell.getHyperlink());
			}
			// Set the cell data type
			newCell.setCellType(oldCell.getCellType());
			newCell.setCellValue(oldCell.getRawValue());
			// Set the cell data value
			switch (oldCell.getCellType()) {
			case BLANK:
				newCell.setCellValue(oldCell.getStringCellValue());
				break;
			case BOOLEAN:
				newCell.setCellValue(oldCell.getBooleanCellValue());
				break;
			case ERROR:
				newCell.setCellErrorValue(oldCell.getErrorCellValue());
				break;
			case FORMULA:
				newCell.setCellFormula(oldCell.getCellFormula());
				break;
			case NUMERIC:
				newCell.setCellValue(oldCell.getNumericCellValue());
				break;
			case STRING:
				newCell.setCellValue(oldCell.getRichStringCellValue());
				break;
			}
			if (currentRow.getRowNum() == (rowNumSheetStart + 2)) {
				oldCell.setCellValue(statusesList.get(index));
				addDataValidation(worksheet, currentRow.getRowNum(), currentRow.getRowNum(), sourceColNum, sourceColNum,
						new String[] { "Pre-Approved", "Approved", "Closed" });
			}
			if (currentRow.getRowNum() == (rowNumSheetStart + 3)) {
				oldCell.setCellValue(roleNamesList.get(index));
				addDataValidation(worksheet, currentRow.getRowNum(), currentRow.getRowNum(), sourceColNum, sourceColNum,
						validationsJrdmRolesList.toArray(new String[0]));
			}
		}
	}

	public static void createColumnFieldPermission(XSSFWorkbook workbook, XSSFSheet worksheet, int sourceColNum,
			int destinationColNum, int rowNumSheetStart, List<String> roleNamesList, int index,
			List<String> validationsOperatorsPermList) {
		worksheet.shiftColumns(destinationColNum, worksheet.getRow(3).getLastCellNum(), 1);
		for (Row currentRow : worksheet) {
			XSSFCell oldCell = (XSSFCell) currentRow.getCell(sourceColNum);
			XSSFCell newCell = (XSSFCell) currentRow.createCell(destinationColNum);
			// If the old cell is null jump to next cell
			if (oldCell == null) {
				newCell = null;
				continue;
			}
			// Copy style from old cell and apply to new cell
			XSSFCellStyle newCellStyle = workbook.createCellStyle();
			newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
			newCell.setCellStyle(newCellStyle);
			// If there is a cell comment, copy
			if (oldCell.getCellComment() != null) {
				newCell.setCellComment(oldCell.getCellComment());
			}
			// If there is a cell hyperlink, copy
			if (oldCell.getHyperlink() != null) {
				newCell.setHyperlink(oldCell.getHyperlink());
			}
			// Set the cell data type
			newCell.setCellType(oldCell.getCellType());
			newCell.setCellValue(oldCell.getRawValue());
			// Set the cell data value
			switch (oldCell.getCellType()) {
			case BLANK:
				newCell.setCellValue(oldCell.getStringCellValue());
				break;
			case BOOLEAN:
				newCell.setCellValue(oldCell.getBooleanCellValue());
				break;
			case ERROR:
				newCell.setCellErrorValue(oldCell.getErrorCellValue());
				break;
			case FORMULA:
				newCell.setCellFormula(oldCell.getCellFormula());
				break;
			case NUMERIC:
				newCell.setCellValue(oldCell.getNumericCellValue());
				break;
			case STRING:
				newCell.setCellValue(oldCell.getRichStringCellValue());
				break;
			}
			if (currentRow.getRowNum() == (rowNumSheetStart + 3)) {
				oldCell.setCellValue(roleNamesList.get(index));
				addDataValidation(worksheet, currentRow.getRowNum(), currentRow.getRowNum(), sourceColNum, sourceColNum,
						validationsOperatorsPermList.toArray(new String[0]));
			}
		}
	}

	public static void createColumnFieldPermissionMultiStage(XSSFWorkbook workbook, XSSFSheet worksheet,
			int sourceColNum, int destinationColNum, int rowNumSheetStart, List<String> roleNamesList,
			List<String> statusesList, int index, List<String> validationsFeatureRolesList) {
		worksheet.shiftColumns(destinationColNum, worksheet.getRow(3).getLastCellNum(), 1);
		for (Row currentRow : worksheet) {
			XSSFCell oldCell = (XSSFCell) currentRow.getCell(sourceColNum);
			XSSFCell newCell = (XSSFCell) currentRow.createCell(destinationColNum);
			// If the old cell is null jump to next cell
			if (oldCell == null) {
				newCell = null;
				continue;
			}
			// Copy style from old cell and apply to new cell
			XSSFCellStyle newCellStyle = workbook.createCellStyle();
			newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
			newCell.setCellStyle(newCellStyle);
			// If there is a cell comment, copy
			if (oldCell.getCellComment() != null) {
				newCell.setCellComment(oldCell.getCellComment());
			}
			// If there is a cell hyperlink, copy
			if (oldCell.getHyperlink() != null) {
				newCell.setHyperlink(oldCell.getHyperlink());
			}
			// Set the cell data type
			newCell.setCellType(oldCell.getCellType());
			newCell.setCellValue(oldCell.getRawValue());
			// Set the cell data value
			switch (oldCell.getCellType()) {
			case BLANK:
				newCell.setCellValue(oldCell.getStringCellValue());
				break;
			case BOOLEAN:
				newCell.setCellValue(oldCell.getBooleanCellValue());
				break;
			case ERROR:
				newCell.setCellErrorValue(oldCell.getErrorCellValue());
				break;
			case FORMULA:
				newCell.setCellFormula(oldCell.getCellFormula());
				break;
			case NUMERIC:
				newCell.setCellValue(oldCell.getNumericCellValue());
				break;
			case STRING:
				newCell.setCellValue(oldCell.getRichStringCellValue());
				break;
			}
			if (currentRow.getRowNum() == (rowNumSheetStart + 2)) {
				oldCell.setCellValue(statusesList.get(index));
			}
			if (currentRow.getRowNum() == (rowNumSheetStart + 4)) {
				oldCell.setCellValue(roleNamesList.get(index));
				addDataValidation(worksheet, currentRow.getRowNum(), currentRow.getRowNum(), sourceColNum, sourceColNum,
						validationsFeatureRolesList.toArray(new String[0]));
			}
		}
	}

	public static List<String> getValidationsFieldTypesList(XSSFWorkbook workBook, String sheetName, int cellNum) {
		XSSFSheet validationsSheet = workBook.getSheet(sheetName);
		Iterator<Row> rowsValidationsSheet = null;
		XSSFRow row;
		int rowNum = 0;
		List<String> validationsFieldTypesList = new ArrayList<String>();
		if (null != validationsSheet) {
			rowsValidationsSheet = validationsSheet.rowIterator();
			while (null != rowsValidationsSheet && rowsValidationsSheet.hasNext()) {
				rowNum++;
				row = (XSSFRow) rowsValidationsSheet.next();
				if (rowNum > 1 && null != row.getCell(cellNum)
						&& StringUtils.isNotEmpty(CommonTemplateUtils.readCellValue(row.getCell(cellNum)))) {
					validationsFieldTypesList.add(CommonTemplateUtils.readCellValue(row.getCell(cellNum)));
				}
			}
		}
		return validationsFieldTypesList;
	}

	public static void createColumnButtonPermission(XSSFWorkbook workBook, XSSFSheet worksheet, int sourceColNum,
			int destinationColNum, int rowNumSheetStart, int rowNumSheetEnd) {
		if (destinationColNum > worksheet.getRow(3).getLastCellNum())
			worksheet.shiftColumns(destinationColNum, worksheet.getRow(3).getLastCellNum(), 1);
		for (Row currentRow : worksheet) {
			if (currentRow.getRowNum() >= rowNumSheetStart && currentRow.getRowNum() <= rowNumSheetEnd) {
				XSSFCell oldCell = (XSSFCell) currentRow.getCell(sourceColNum);
				XSSFCell newCell = (XSSFCell) currentRow.createCell(destinationColNum);
				// If the old cell is null jump to next cell
				if (oldCell == null) {
					newCell = null;
					continue;
				}
				// Copy style from old cell and apply to new cell
				XSSFCellStyle newCellStyle = workBook.createCellStyle();
				newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
				newCell.setCellStyle(newCellStyle);
				// If there is a cell comment, copy
				if (oldCell.getCellComment() != null) {
					newCell.setCellComment(oldCell.getCellComment());
				}
				// If there is a cell hyperlink, copy
				if (oldCell.getHyperlink() != null) {
					newCell.setHyperlink(oldCell.getHyperlink());
				}
				// Set the cell data type
				newCell.setCellType(oldCell.getCellType());
				newCell.setCellValue(oldCell.getRawValue());
				// Set the cell data value
				switch (oldCell.getCellType()) {
				case BLANK:
					newCell.setCellValue(oldCell.getStringCellValue());
					break;
				case BOOLEAN:
					newCell.setCellValue(oldCell.getBooleanCellValue());
					break;
				case ERROR:
					newCell.setCellErrorValue(oldCell.getErrorCellValue());
					break;
				case FORMULA:
					newCell.setCellFormula(oldCell.getCellFormula());
					break;
				case NUMERIC:
					newCell.setCellValue(oldCell.getNumericCellValue());
					break;
				case STRING:
					newCell.setCellValue(oldCell.getRichStringCellValue());
					break;
				}
			}
		}
	}

	public static void createColumnApplicationOverride(XSSFWorkbook workbook, XSSFSheet worksheet, int sourceColNum,
			int destinationColNum, int rowNumSheetStart, List<String> applicantsList, List<String> countriesList,
			List<String> attributesList, List<String> validationsCountriesList, int index) {
		worksheet.shiftColumns(destinationColNum, worksheet.getRow(3).getLastCellNum(), 1);
		for (Row currentRow : worksheet) {
			XSSFCell oldCell = (XSSFCell) currentRow.getCell(sourceColNum);
			XSSFCell newCell = (XSSFCell) currentRow.createCell(destinationColNum);
			// If the old cell is null jump to next cell
			if (oldCell == null) {
				newCell = null;
				continue;
			}
			// Copy style from old cell and apply to new cell
			XSSFCellStyle newCellStyle = workbook.createCellStyle();
			newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
			newCell.setCellStyle(newCellStyle);
			// If there is a cell comment, copy
			if (oldCell.getCellComment() != null) {
				newCell.setCellComment(oldCell.getCellComment());
			}
			// If there is a cell hyperlink, copy
			if (oldCell.getHyperlink() != null) {
				newCell.setHyperlink(oldCell.getHyperlink());
			}
			// Set the cell data type
			newCell.setCellType(oldCell.getCellType());
			newCell.setCellValue(oldCell.getRawValue());
			// Set the cell data value
			switch (oldCell.getCellType()) {
			case BLANK:
				newCell.setCellValue(oldCell.getStringCellValue());
				break;
			case BOOLEAN:
				newCell.setCellValue(oldCell.getBooleanCellValue());
				break;
			case ERROR:
				newCell.setCellErrorValue(oldCell.getErrorCellValue());
				break;
			case FORMULA:
				newCell.setCellFormula(oldCell.getCellFormula());
				break;
			case NUMERIC:
				newCell.setCellValue(oldCell.getNumericCellValue());
				break;
			case STRING:
				newCell.setCellValue(oldCell.getRichStringCellValue());
				break;
			}
			if (currentRow.getRowNum() == (rowNumSheetStart + 2)) {
				validationsCountriesList.forEach(country -> {
					if (country.contains(countriesList.get(index) + " |")) {
						oldCell.setCellValue(country);
					}
				});
				if (StringUtils.isEmpty(oldCell.getStringCellValue())) {
					oldCell.setCellValue(countriesList.get(index) + " |");
				}
			}
			if (currentRow.getRowNum() == (rowNumSheetStart + 3)) {
				oldCell.setCellValue(applicantsList.get(index));
				addDataValidation(worksheet, currentRow.getRowNum(), currentRow.getRowNum(), sourceColNum, sourceColNum,
						new String[] { "internal", "external", "both" });
			}
			if (currentRow.getRowNum() == (rowNumSheetStart + 4)) {
				oldCell.setCellValue(attributesList.get(index));
				addDataValidation(worksheet, currentRow.getRowNum(), currentRow.getRowNum(), sourceColNum, sourceColNum,
						new String[] { "public", "required" });
			}
		}
	}
}
