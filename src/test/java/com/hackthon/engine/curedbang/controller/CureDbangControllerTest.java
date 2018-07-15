package com.hackthon.engine.curedbang.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackthon.engine.curedbang.configuration.ExceptionTranslater;
import com.hackthon.engine.curedbang.model.FileInput;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CureDbangControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@InjectMocks
	private CureDbangController cureDbangController;

	@Before
	public void before() {
		mockMvc = MockMvcBuilders.standaloneSetup(cureDbangController).setControllerAdvice(new ExceptionTranslater())
				.build();
	}

	@Test
	public void testBase64Success() throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		FileInput fileInput = new FileInput();
		fileInput.setContent("content");
		fileInput.setDescription("description");
		fileInput.setName("name");

		String stringValue = mapper.writeValueAsString(fileInput);

		mockMvc.perform(
				post("/file/base64")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.content(stringValue)
				)
		.andExpect(status().isOk());
	}
	
	@Test
	public void testBase64Failure() throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		FileInput fileInput = new FileInput();
		
		String stringValue = mapper.writeValueAsString(fileInput);

		mockMvc.perform(
				post("/file/base64")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.content(stringValue)
				)
		.andExpect(status().isBadRequest());
	}
}
