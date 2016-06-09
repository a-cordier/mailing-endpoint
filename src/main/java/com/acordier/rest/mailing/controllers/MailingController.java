package com.acordier.rest.mailing.controllers;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.acordier.rest.mailing.model.MailMessage;
import com.acordier.rest.mailing.services.MailingService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class MailingController {

	@Autowired
	private MailingService mailingService;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public ResponseEntity<String> sendMailWithAttachment(@RequestPart("message") String message,
			@RequestPart("file") MultipartFile file) {
		logger.info(message);
		try {
			
			mailingService.sendMail(deserialize(message), file);
			return ResponseEntity.status(HttpStatus.CREATED).body("mail sent");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
		}
	}

	@RequestMapping(method = RequestMethod.POST, consumes = { "application/json" })
	public ResponseEntity<String> sendSimpleMail(@RequestBody MailMessage message) {
		try {
			mailingService.sendMail(message);
			return ResponseEntity.status(HttpStatus.CREATED).body("mail sent");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
		}
	}
	
	private MailMessage deserialize(String jsonString) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper objMApper = new ObjectMapper();
		return objMApper.readValue(jsonString, MailMessage.class);
	}
}