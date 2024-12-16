package fsk.glcc.gevents.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class EmailConfirmationToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String token;
	private LocalDateTime expireAt;
	@OneToOne
	private Utilisateur utilisateur;
	
	public EmailConfirmationToken() {
		utilisateur = new Utilisateur();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public LocalDateTime getExpireAt() {
		return expireAt;
	}
	public void setExpireAt(LocalDateTime expireAt) {
		this.expireAt = expireAt;
	}
	public Utilisateur getUtilisateur() {
		return utilisateur;
	}
	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}
	public boolean isExpired() {
		return expireAt.isBefore(LocalDateTime.now());
	}
	
	
}
