package com.rpa.sapsf.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileGeneratorService {

	String generateWbpWorkbook(MultipartFile file);
	String generateJobWorkbook(MultipartFile file);
}
