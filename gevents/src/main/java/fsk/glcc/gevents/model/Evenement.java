package fsk.glcc.gevents.model;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;

@Entity
//@Getter @Setter @NoArgsConstructor @ToString
//@Data
public class Evenement {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotEmpty(message = "Le titre est obligatoir")
	private String titre;
	private String description;
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd MMM yyyy hh:mm")
//	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//	@NotEmpty(message="Séléctionnez une date")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm", shape = JsonFormat.Shape.STRING)
	private LocalDateTime date;
	@NotEmpty(message = "Le lieu est obligatoir")
	private String lieu;
	private int capacite;
	private double prix;
	@Enumerated(EnumType.STRING)
	private TypeEvenement type;
	@ManyToOne
	private Utilisateur organisateur;
//	@ManyToMany(mappedBy = "evenements")
//    Set<Utilisateur> participants;
	@OneToMany(mappedBy = "evenement")
    @JsonManagedReference
	Set<ParticipantEvenement> participants;
	
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getTitre() {
		return titre;
	}


	public void setTitre(String titre) {
		this.titre = titre;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public LocalDateTime getDate() {
		return date;
	}


	public void setDate(LocalDateTime date) {
		this.date = date;
	}


	public String getLieu() {
		return lieu;
	}


	public void setLieu(String lieu) {
		this.lieu = lieu;
	}


	public int getCapacite() {
		return capacite;
	}


	public void setCapacite(int capacite) {
		this.capacite = capacite;
	}

	public double getPrix() {
		return prix;
	}


	public void setPrix(double prix) {
		this.prix = prix;
	}
	
	public TypeEvenement getType() {
		return type;
	}


	public void setType(TypeEvenement type) {
		this.type = type;
	}


	public Utilisateur getOrganisateur() {
		return organisateur;
	}


	public void setOrganisateur(Utilisateur organisateur) {
		this.organisateur = organisateur;
	}


//	public Set<Utilisateur> getParticipants() {
//		return participants;
//	}
//
//
//	public void setParticipants(Set<Utilisateur> participants) {
//		this.participants = participants;
//	}


	public Set<ParticipantEvenement> getParticipants() {
		return participants;
	}


	public void setParticipants(Set<ParticipantEvenement> participants) {
		this.participants = participants;
	}


	@Override
	public String toString() {
		return "Evenement [id=" + id + ", titre=" + titre + ", description=" + description + ", date=" + date
				+ ", lieu=" + lieu + ", capacite=" + capacite + ", prix=" + prix + ", type=" + type + ", organisateur=" + organisateur
				+ ", participants=" + participants + "]";
	}
}
