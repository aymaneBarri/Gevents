package fsk.glcc.gevents.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Paiement {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private double montant;
	private String orderId;
//	private String methode;
//	private String statut;
	@ManyToOne
	private Utilisateur participant;
	@ManyToOne
	private Evenement evenement;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public double getMontant() {
		return montant;
	}
	public void setMontant(double montant) {
		this.montant = montant;
	}
//	public String getMethode() {
//		return methode;
//	}
//	public void setMethode(String methode) {
//		this.methode = methode;
//	}
//	public String getStatut() {
//		return statut;
//	}
//	public void setStatut(String statut) {
//		this.statut = statut;
//	}
	public Utilisateur getParticipant() {
		return participant;
	}
	public void setParticipant(Utilisateur participant) {
		this.participant = participant;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Evenement getEvenement() {
		return evenement;
	}
	public void setEvenement(Evenement evenement) {
		this.evenement = evenement;
	}
	
	
}
