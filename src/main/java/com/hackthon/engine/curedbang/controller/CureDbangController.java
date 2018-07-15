package com.hackthon.engine.curedbang.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.validation.Valid;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hackthon.engine.curedbang.model.FileInput;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * @author Tanmay Dey
 *
 */
@RestController
@RequestMapping("/file/")
public class CureDbangController {

	private static final String DRIVE_PATH = "D:/NewTest/";

	/**
	 * Simple file upload with multipart file, Here its being saved to the local
	 * drive, you can also save it to database or any server.
	 * 
	 * @param file
	 * @return
	 */
	@ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Business Validation Failed"),
			@ApiResponse(code = 500, message = "Internal Server Error")})
	@PostMapping("simple")
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(new File(DRIVE_PATH + file.getOriginalFilename())));
				stream.write(bytes);
				stream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>(HttpStatus.OK.getReasonPhrase(), HttpStatus.OK);
	}

	/**
	 * Simple file download with multipart file, Here its being fetched from the local
	 * drive, you can also fetch it from database or any server.
	 * 
	 * @param file
	 * @return
	 */
	@ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Business Validation Failed"),
			@ApiResponse(code = 500, message = "Internal Server Error")})
	@GetMapping("simple")
	public ResponseEntity<InputStreamResource> downloadFile(@RequestParam("fileName") String fileName)
			throws FileNotFoundException {

		File file = new File(DRIVE_PATH + fileName);
		if (file.canRead()) {
			InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

			HttpHeaders headers = new HttpHeaders();
			headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
			headers.add("Pragma", "no-cache");
			headers.add("Expires", "0");
			headers.add("Content-Disposition", "attachment; filename=" + fileName + "");

			return ResponseEntity.ok().headers(headers)
					.contentLength(file.length()).contentType(MediaType.parseMediaType("application/octet-stream"))
					.body(resource);
		}
		return new ResponseEntity<InputStreamResource>(HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * If the other data like description, tile, etc along with the file are need to be send
	 * we can use the below way. Here we need to convert the file to base64 format and send its content
	 * to the FileInput -> content.
	 * 
	 * @param file is the FileInput object
	 * @return
	 * @throws IOException
	 */
	@ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Business Validation Failed"),
			@ApiResponse(code = 500, message = "Internal Server Error")})
	@PostMapping("base64")
	public ResponseEntity<String> uploadFileBase64(@Valid @RequestBody FileInput file) throws IOException {

		// Save the file data in db or any server. You can send some id or something to let the user download the file later
		HttpHeaders header = new HttpHeaders();
		header.add("FILE_ID", "abcd-xyz");
		
		return new ResponseEntity<String>(HttpStatus.OK.getReasonPhrase(), header, HttpStatus.OK);
	}

	/**
	 * Base 64 get file content
	 * 
	 * @param fileId
	 * @return
	 * @throws IOException
	 */
	@ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Business Validation Failed"),
			@ApiResponse(code = 500, message = "Internal Server Error")})
	@GetMapping("base64")
	public ResponseEntity<String> downloadFileBase64(@RequestHeader("FILE_ID") String fileId) throws IOException {

		// Fetch the file data from db or any server. The same can be fetched from the get service.
		
		return new ResponseEntity<String>(HttpStatus.OK.getReasonPhrase(), HttpStatus.OK);
	}

	
}
