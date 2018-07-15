package com.hackthon.engine.curedbang.configuration;

import java.util.HashSet;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.hackthon.engine.curedbang.model.Message;

/**
 * 
 * @author Tanmay Dey
 *
 */
@ControllerAdvice
public class ExceptionTranslater {

	/**
	 * This method will handle the binding failure(validation failure) of the input
	 * request
	 * 
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Message> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

		String description = ex.getMessage();

		if (!ObjectUtils.isEmpty(ex.getBindingResult())) {
			Set<String> fieldErrorSet = new HashSet<>();
			ex.getBindingResult().getAllErrors().forEach(v -> fieldErrorSet.add(v.getDefaultMessage()));
			description = "Invalid Fields - " + String.join(", ", fieldErrorSet);
		}
		Message message = new Message(HttpStatus.BAD_REQUEST.value(), description);

		return new ResponseEntity<Message>(message, HttpStatus.BAD_REQUEST);
	}

	/**
	 * This method will handle any exception at run time
	 * 
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Message> handleException(Exception ex) {

		Message message = new Message(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());

		return new ResponseEntity<Message>(message, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
