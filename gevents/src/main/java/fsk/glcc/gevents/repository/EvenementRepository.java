package fsk.glcc.gevents.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fsk.glcc.gevents.model.Evenement;

@Repository
public interface EvenementRepository extends JpaRepository<Evenement, Long>{
	
	 @Query("SELECT e FROM Evenement e WHERE e.date < CURRENT_TIMESTAMP")
	 List<Evenement> findEvenementsExpires(Sort sort);
	 
	 @Query("SELECT e FROM Evenement e WHERE e.date >= CURRENT_TIMESTAMP")
	 List<Evenement> findEvenementsNonExpires(Sort sort);
	 
	 @Query("SELECT e FROM Evenement e WHERE e.date >= CURRENT_TIMESTAMP AND e.organisateur.id = :organisateurId")
	 List<Evenement> findEvenementsOrganisesParMoi(@Param("organisateurId") Long organisateurId, Sort sort);
}
