package com.acordier.rest.mailing.services;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.acordier.rest.mailing.model.MailMessage;

@Service
public class MailingService {
	
	@Autowired
	private JavaMailSender javaMailSender;
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public void sendMail(MailMessage message) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setFrom(message.getFrom());
			mimeMessageHelper.setTo(message.getTo());
			mimeMessageHelper.setSubject(message.getSubject());
			mimeMessageHelper.setText(message.getContent());
			javaMailSender.send(mimeMessage);
		} catch (MessagingException e) {
			logger.error("error sending message: " + e.getMessage());
			throw e;
		}

	}
	
	public void sendMail(MailMessage message, MultipartFile file) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setFrom(message.getFrom());
			mimeMessageHelper.setTo(message.getTo());
			mimeMessageHelper.setSubject(message.getSubject());
			mimeMessageHelper.setText(message.getContent());
			mimeMessageHelper.addAttachment(file.getOriginalFilename(), file);
			javaMailSender.send(mimeMessage);
		} catch (MessagingException e) {
			logger.error("error sending message: " + e.getMessage());
			throw e;
		}

	}
}
