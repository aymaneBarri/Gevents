package fsk.glcc.gevents.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

@Entity
public class ParticipantEvenement {

	@EmbeddedId
    ParticipantEvenementKey id;

    @ManyToOne
    @MapsId("participantId")
    @JoinColumn(name = "participant_id")
    @JsonBackReference
    Utilisateur participant;

    @ManyToOne
    @MapsId("evenementId")
    @JoinColumn(name = "evenement_id")
    @JsonBackReference
    Evenement evenement;

    int rating;

    public ParticipantEvenement() {
    	this.rating = 0;
    }
    
    public ParticipantEvenement(Utilisateur participant, Evenement evenement) {
    	this.participant = participant;
    	this.evenement = evenement;
    	this.rating = 0;
    }
    
	public ParticipantEvenementKey getId() {
		return id;
	}

	public void setId(ParticipantEvenementKey id) {
		this.id = id;
	}

	public Utilisateur getParticipant() {
		return participant;
	}

	public void setParticipant(Utilisateur participant) {
		this.participant = participant;
	}

	public Evenement getEvenement() {
		return evenement;
	}

	public void setEvenement(Evenement evenement) {
		this.evenement = evenement;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	@Override
	public String toString() {
		return "ParticipantEvenement [id=" + id + ", participant=" + participant.getId() + ", evenement=" + evenement.getId()
				+ ", rating=" + rating + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(evenement, id, participant, rating);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ParticipantEvenement))
			return false;
		ParticipantEvenement other = (ParticipantEvenement) obj;
		return Objects.equals(evenement, other.evenement) && Objects.equals(id, other.id)
				&& Objects.equals(participant, other.participant) && rating == other.rating;
	}
	
//	public int hashCode() {
//        return Objects.hash(this.id);
//    }
//
//    public boolean equals(Object o) {
//        if (o == this) { return true; }
//        if (o == null || !(o instanceof ParticipantEvenement) ) { return false; }
//        return Objects.equals(this.id, ((ParticipantEvenement) o).id);
//    }
	
	
}
