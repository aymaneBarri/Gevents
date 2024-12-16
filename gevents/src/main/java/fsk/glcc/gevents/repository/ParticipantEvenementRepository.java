package fsk.glcc.gevents.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fsk.glcc.gevents.model.ParticipantEvenement;
import fsk.glcc.gevents.model.ParticipantEvenementKey;

@Repository
public interface ParticipantEvenementRepository extends JpaRepository<ParticipantEvenement, ParticipantEvenementKey>{

//	 @Query("SELECT COUNT(*) FROM ParticipantEvenement pe WHERE pe.id.evenementId = )
	 int countByEvenementId(Long id);
}
