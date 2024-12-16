package fsk.glcc.gevents.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fsk.glcc.gevents.model.Paiement;

public interface PaiementRepository  extends JpaRepository<Paiement, Long> {

}
