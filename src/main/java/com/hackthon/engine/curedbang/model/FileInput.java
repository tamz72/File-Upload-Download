package com.hackthon.engine.curedbang.model;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author Tanmay Dey
 *
 */
@Getter
@Setter
@ToString
public class FileInput {

	@NotBlank(message = "name")
	private String name;

	@NotBlank(message = "description")
	private String description;

	@NotBlank(message = "content")
	private String content;

}
