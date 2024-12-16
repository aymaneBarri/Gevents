package fsk.glcc.gevents.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fsk.glcc.gevents.model.EmailConfirmationToken;

@Repository
public interface EmailConfirmationTokenRepository extends JpaRepository<EmailConfirmationToken, Long>{
	EmailConfirmationToken findByToken(String token);
	Long deleteByToken(String token);
}
