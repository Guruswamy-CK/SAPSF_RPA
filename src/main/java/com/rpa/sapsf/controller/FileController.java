package com.rpa.sapsf.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rpa.sapsf.business.FileBusinessLogic;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class FileController {

	private FileBusinessLogic businessLogic = new FileBusinessLogic();

	@PostMapping("/file-convert")
	public void fileConvert(@RequestParam("file") MultipartFile file,
			@RequestParam("directionSelected") String directionSelected,
			@RequestParam("moduleSelected") String moduleSelected, @RequestParam("modelSelected") String modelSelected,
			HttpServletResponse response) {
		try {
			String fileName = this.businessLogic.directToFileConvertion(file, directionSelected, moduleSelected,
					modelSelected);
			if (StringUtils.isNotEmpty(fileName)) {
				File storedFile = new File(fileName);
				byte[] isr = Files.readAllBytes(storedFile.toPath());
				ByteArrayOutputStream out = new ByteArrayOutputStream(isr.length);
				out.write(isr, 0, isr.length);
				response.setContentType("application/octet-stream");
				// Use 'inline' for preview and 'attachement' for download in browser.
				response.addHeader("Content-Disposition", "inline; filename=" + fileName);
				OutputStream os;
				os = response.getOutputStream();
				out.writeTo(os);
				os.flush();
				os.close();
				storedFile.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PostMapping("/wbp-file-generate")
	public void fileWbpGenerate(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
		try {
			String fileName = this.businessLogic.directToWbpFileGeneration(file);
			if (StringUtils.isNotEmpty(fileName)) {
				File storedFile = new File(fileName);
				byte[] isr = Files.readAllBytes(storedFile.toPath());
				ByteArrayOutputStream out = new ByteArrayOutputStream(isr.length);
				out.write(isr, 0, isr.length);
				response.setContentType("application/octet-stream");
				// Use 'inline' for preview and 'attachement' for download in browser.
				response.addHeader("Content-Disposition", "inline; filename=" + fileName);
				OutputStream os;
				os = response.getOutputStream();
				out.writeTo(os);
				os.flush();
				os.close();
				storedFile.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PostMapping("/job-file-generate")
	public void fileJobGenerate(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
		try {
			String fileName = this.businessLogic.directToJobFileGeneration(file);
			if (StringUtils.isNotEmpty(fileName)) {
				File storedFile = new File(fileName);
				byte[] isr = Files.readAllBytes(storedFile.toPath());
				ByteArrayOutputStream out = new ByteArrayOutputStream(isr.length);
				out.write(isr, 0, isr.length);
				response.setContentType("application/octet-stream");
				// Use 'inline' for preview and 'attachement' for download in browser.
				response.addHeader("Content-Disposition", "inline; filename=" + fileName);
				OutputStream os;
				os = response.getOutputStream();
				out.writeTo(os);
				os.flush();
				os.close();
				storedFile.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
