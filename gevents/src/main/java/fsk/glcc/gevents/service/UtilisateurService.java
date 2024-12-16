package fsk.glcc.gevents.service;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;

import fsk.glcc.gevents.model.EmailConfirmationToken;
import fsk.glcc.gevents.model.Utilisateur;
import fsk.glcc.gevents.repository.EmailConfirmationTokenRepository;
import fsk.glcc.gevents.repository.UtilisateurRepository;
import jakarta.mail.MessagingException;

@Service
public class UtilisateurService {

//	@Value("http://localhost:8080")
//	private String baseUrl;

	@Value("2800")
	private int tokenValidityInSeconds; 
	private static final BytesKeyGenerator DEFAULT_TOKEN_GENERATOR = KeyGenerators.secureRandom(12);
	@Autowired
	private UtilisateurRepository repo;
	private BCryptPasswordEncoder encoder;
	@Autowired
	private EmailConfirmationTokenService emailService;
	@Autowired
	private EmailConfirmationTokenRepository tokenRepo;
	
	public List<Utilisateur> getUtilisateurs(){
		return repo.findAll();
	}
	
	public Utilisateur getUtilisateurById(Long id) {
		return repo.findById(id).orElse(null);
	}
	
	public void addUtilisateur(Utilisateur utilisateur) {
		encoder = new BCryptPasswordEncoder(BCryptVersion.$2A, 12);
		utilisateur.setMotDePasse(encoder.encode(utilisateur.getMotDePasse()));
		repo.save(utilisateur);
		
		try {
			this.sendRegistrationConfirmationEmail(utilisateur);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void updateUtilisateur(Utilisateur utilisateur) {
		repo.save(utilisateur);
	}
	
	public void deleteUtilisateur(Long id) {
		repo.deleteById(id);
	}
	
	
	
	public void sendRegistrationConfirmationEmail(Utilisateur utilisateur) throws MessagingException {
//		EmailConfirmationToken token = service.createToken();
//		token.setUtilisateur(utilisateur);
//		tokenRepo.save(token);
//		AccountVerificationEmailContext emailContext = new AccountVerificationEmailContext();
//		emailContext.init(utilisateur);
//		emailContext.setToken(token.getToken());
//		emailContext.buildVerificationUrl(baseUrl, token.getToken());
//		
//		try {
//			emailService.sendMail(emailContext);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		String tokenValue = new String(Base64.encodeBase64URLSafeString(DEFAULT_TOKEN_GENERATOR.generateKey()));
		EmailConfirmationToken token = new EmailConfirmationToken();
		token.setToken(tokenValue);
		token.setExpireAt(LocalDateTime.now().plusSeconds(tokenValidityInSeconds));
		token.setUtilisateur(utilisateur);
		tokenRepo.save(token);
		
		emailService.sendConfirmationEmail(token);
	}
	
	public boolean verifyUser(final String tokenValue) {
		EmailConfirmationToken token = tokenRepo.findByToken(tokenValue);
		if(token == null || token.isExpired() || !token.getToken().equals(tokenValue)) {
			return false;
		}
		
		Utilisateur utilisateur = token.getUtilisateur();
		if (utilisateur == null) {
			return false;
		}
		utilisateur.setAccountVerified(true);
		repo.save(utilisateur);
		
		tokenRepo.delete(token);
		return true;
	}
}
