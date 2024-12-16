package fsk.glcc.gevents.controller;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class EmailRestController {

	private final JavaMailSender mailSender;

	public EmailRestController(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	@GetMapping("/send-email")
	public String sendEmail() {
		try {
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setFrom("aymanebarri@gmail.com");
		message.setTo("jaxafek521@nausard.com");
		message.setSubject("Evenement demain");
		message.setText("Rappel de l'evenement de demain");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2024);
		cal.set(Calendar.MONTH, Calendar.NOVEMBER);
		cal.set(Calendar.DAY_OF_MONTH, 29);
		cal.set(Calendar.HOUR, 21);
		cal.set(Calendar.MINUTE, 39);
		message.setSentDate(cal.getTime());
		mailSender.send(message);
		
		return "success";
		} catch (Exception e) {
			return e.getMessage();
		}
	}

}
