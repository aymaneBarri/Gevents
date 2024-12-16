package fsk.glcc.gevents.service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import fsk.glcc.gevents.model.EmailConfirmationToken;
import fsk.glcc.gevents.model.ParticipantEvenement;
import fsk.glcc.gevents.model.Utilisateur;
import fsk.glcc.gevents.repository.EmailConfirmationTokenRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailConfirmationTokenService {

//	@Value("2800")
//	private int tokenValidityInSeconds; 
//	private static final BytesKeyGenerator DEFAULT_TOKEN_GENERATOR = KeyGenerators.secureRandom(12);
//	@Autowired
//	private EmailConfirmationTokenRepository repo;
//	
//	public EmailConfirmationToken createToken() {
//		String tokenValue = new String(Base64.encodeBase64URLSafeString(DEFAULT_TOKEN_GENERATOR.generateKey()));
//		EmailConfirmationToken token = new EmailConfirmationToken();
//		token.setToken(tokenValue);
//		token.setExpireAt(LocalDateTime.now().plusSeconds(tokenValidityInSeconds));
//		this.saveToken(token);
//		return token;
//	}
//	
//	public void saveToken(EmailConfirmationToken token) {
//		repo.save(token);
//	}
//	
//	public EmailConfirmationToken findByToken(String token) {
//		return repo.findByToken(token);
//	}
//	
//	public void removeToken(EmailConfirmationToken token) {
//		repo.delete(token);
//	}

	private JavaMailSender emailSender;
	
	public EmailConfirmationTokenService(JavaMailSender sender) {
		emailSender = sender;
	}
	
	public void sendConfirmationEmail(EmailConfirmationToken token) throws MessagingException {
		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);
		
		mimeMessageHelper.setTo(token.getUtilisateur().getEmail());
		mimeMessageHelper.setSubject("Confirmez votre adresse email pour continuer d'utiliser Gevents");
		mimeMessageHelper.setFrom("aymanebarri@gmail.com");
		mimeMessageHelper.setText("<html>"+
				"<body>"+
				"<h2>" + token.getUtilisateur().getNom() + ",</h2>" +
				"<br/> Voici le lien pour confirmer votre adresse email, cliquez dessous pour commencer à utiliser Gevents" +
				"<br/>"+ generateConfirmationLink(token.getToken()) + "" +
				"<br/>Aymane Barri" +
				"</body>" +
				"</html>"
				, true);
		emailSender.send(message);
	}
	
	private String generateConfirmationLink(String token) {
		return "<a href=http://localhost:8080/confirm-email?token=" + token + ">Confirmer Email</a>";
	}
	
	public void sendParticipationEMail(ParticipantEvenement pe) throws MessagingException {
		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);
		
		mimeMessageHelper.setTo(pe.getParticipant().getEmail());
		mimeMessageHelper.setSubject("Confirmation de votre participation à l'évènement " + pe.getEvenement().getTitre());
		mimeMessageHelper.setFrom("aymanebarri@gmail.com");
		mimeMessageHelper.setText("<html>"+
				"<body>"+
				"<h2>" + pe.getParticipant().getNom() + ",</h2>" +
				"<br/> Nous vous confirmons votre participation à l'évènement " + pe.getEvenement().getTitre() + " qui se déroulera le " + pe.getEvenement().getDate() + " à " + pe.getEvenement().getLieu() +
				"<br/>Aymane Barri" +
				"</body>" +
				"</html>"
				, true);
		emailSender.send(message);
	}
}
