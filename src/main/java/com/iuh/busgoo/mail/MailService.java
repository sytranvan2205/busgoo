package com.iuh.busgoo.mail;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.iuh.busgoo.entity.Account;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class MailService {
	Logger logger = LoggerFactory.getLogger(MailService.class);

	@Autowired
	private JavaMailSender emailSender;

	public void sendEmail(Mail mail) throws MessagingException {
		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper( message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());
		helper.setTo(mail.getTo());
		String content = "Dear registrant"+ ",\n\n"
				+ "You have requested to verify your account with BUSGOO. Below is your verification token:\n\n"
				+ "Verification Token: "+mail.getModel().get("token").toString()+ "\n\n"
				+ "If you did not request this token, please disregard this email." +"\n\n"
				+ "Best regards,\n"
				+ "BUSGOO";
		helper.setText(content);
		helper.setSubject(mail.getSubject());
		helper.setFrom(mail.getFrom());

		emailSender.send(message);
		logger.info("Success send mail");

	}

}
