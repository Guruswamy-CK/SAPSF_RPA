package com.rpa.sapsf.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.rpa.sapsf.dto.UserDTO;
import com.rpa.sapsf.service.UserService;

public class UserServiceImpl implements UserService {

	@Override
	public UserDTO userRegistration(UserDTO user) {
		File userTable = new File("src\\main\\resources\\templates\\User_Management\\User_Table.xlsx");
		try {
			XSSFWorkbook workBook;
			FileInputStream userTableStream = new FileInputStream(userTable);
			workBook = new XSSFWorkbook(userTableStream);
			XSSFSheet usersSheet = workBook.getSheet("Users");
			usersSheet.createRow(usersSheet.getLastRowNum() + 1);
			usersSheet.getRow(usersSheet.getLastRowNum()).createCell(0);
			usersSheet.getRow(usersSheet.getLastRowNum()).getCell(0).setCellValue(user.getUserName());
			usersSheet.getRow(usersSheet.getLastRowNum()).createCell(1);
			usersSheet.getRow(usersSheet.getLastRowNum()).getCell(1).setCellValue(user.getEmailId());
			usersSheet.getRow(usersSheet.getLastRowNum()).createCell(2);
			usersSheet.getRow(usersSheet.getLastRowNum()).getCell(2).setCellValue(user.getPassword());
			usersSheet.getRow(usersSheet.getLastRowNum()).createCell(4);
			usersSheet.getRow(usersSheet.getLastRowNum()).getCell(4)
					.setCellValue(new SimpleDateFormat("dd-MMM-yyyy").format(new Date()).toString());
			usersSheet.getRow(usersSheet.getLastRowNum()).createCell(5);
			usersSheet.getRow(usersSheet.getLastRowNum()).getCell(5)
					.setCellValue(new SimpleDateFormat("dd-MMM-yyyy").format(new Date()).toString());

			userTableStream.close();
			// Crating output stream and writing the updated workbook
			FileOutputStream os = new FileOutputStream(userTable);
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
		return user;
	}

	@Override
	public UserDTO userLogin(UserDTO user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserDTO userForgotPassword(UserDTO user) {
		// TODO Auto-generated method stub
		return null;
	}
}
