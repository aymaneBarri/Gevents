package fsk.glcc.gevents.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ParticipantEvenementKey implements Serializable {

	@Column(name = "participant_id")
	Long participantId;

    @Column(name = "evenement_id")
    Long evenementId;

	public Long getParticipantId() {
		return participantId;
	}

	public void setParticipantId(Long participantId) {
		this.participantId = participantId;
	}

	public Long getEvenementId() {
		return evenementId;
	}

	public void setEvenementId(Long evenementId) {
		this.evenementId = evenementId;
	}

    
}
