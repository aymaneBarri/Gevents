package fsk.glcc.gevents.model;

import java.util.Set;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Utilisateur {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotEmpty(message = "Le nom est obligatoir")
	private String nom;
	@Column(unique = true, nullable = false)
	@NotEmpty(message = "L'email est obligatoir")
	@Email
	private String email;
//	@NotEmpty(message="Le mot de passe est obligatoir")
	@Length(min=6, message="Le mot de passe doit être compris entre 6 et 32 caractères")
	private String motDePasse;
	@Enumerated(EnumType.STRING)
	private Role role;
	private boolean accountVerified;
//	@ManyToMany
//    Set<Evenement> evenements;
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "participant")
    @JsonManagedReference
	Set<ParticipantEvenement> participantEvenement;
	
	public Utilisateur() {
		this.accountVerified = false;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMotDePasse() {
		return motDePasse;
	}
	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public boolean isAccountVerified() {
		return accountVerified;
	}
	public void setAccountVerified(boolean accountVerified) {
		this.accountVerified = accountVerified;
	}
	public Set<ParticipantEvenement> getParticipantEvenement() {
		return participantEvenement;
	}
	public void setParticipantEvenement(Set<ParticipantEvenement> participantEvenement) {
		this.participantEvenement = participantEvenement;
	}
	//	public Set<Evenement> getEvenements() {
//		return evenements;
//	}
//	public void setEvenements(Set<Evenement> evenements) {
//		this.evenements = evenements;
//	}
	
	@Override
	public String toString() {
		return "Utilisateur [id=" + id + ", nom=" + nom + ", email=" + email + ", motDePasse=" + motDePasse + ", role="
				+ role + ", accountVerified=" + accountVerified + ", participantEvenement=" + participantEvenement
				+ "]";
	}
	
}
