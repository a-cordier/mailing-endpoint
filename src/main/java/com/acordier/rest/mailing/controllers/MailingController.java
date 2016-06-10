package com.acordier.rest.mailing.controllers;

import java.io.IOException;

import javax.mail.MessagingException;

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
			@RequestPart("file") MultipartFile[] files) {
		logger.info(message);

			try {
				mailingService.sendMail(deserialize(message), files);
			} catch (JsonParseException e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("bad json string format: " + e.getLocalizedMessage());
			} catch (JsonMappingException e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("unable to deserialize json string: " + e.getLocalizedMessage());
			} catch (MessagingException e) {
				return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("unable to send message: " + e.getLocalizedMessage());
			} catch (IOException e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("attachment error: " + e.getLocalizedMessage());
			}
			return ResponseEntity.status(HttpStatus.CREATED).body("mail sent");

	}

	@RequestMapping(method = RequestMethod.POST, consumes = { "application/json" })
	public ResponseEntity<String> sendSimpleMail(@RequestBody MailMessage message) {
			try {
				mailingService.sendMail(message);
			} catch (MessagingException e) {
				return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("unable to send message: " + e.getLocalizedMessage());
			}
			return ResponseEntity.status(HttpStatus.CREATED).body("mail sent");
	}
	
	private MailMessage deserialize(String jsonString) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper objMApper = new ObjectMapper();
		return objMApper.readValue(jsonString, MailMessage.class);
	}
}
