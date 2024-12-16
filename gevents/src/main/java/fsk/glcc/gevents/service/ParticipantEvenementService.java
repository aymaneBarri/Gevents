package fsk.glcc.gevents.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fsk.glcc.gevents.model.Evenement;
import fsk.glcc.gevents.model.ParticipantEvenement;
import fsk.glcc.gevents.model.ParticipantEvenementKey;
import fsk.glcc.gevents.repository.EvenementRepository;
import fsk.glcc.gevents.repository.ParticipantEvenementRepository;
import jakarta.mail.MessagingException;

@Service
public class ParticipantEvenementService {

	@Autowired
	private ParticipantEvenementRepository repo;
	@Autowired
	private EmailConfirmationTokenService emailService;

	public ParticipantEvenement getParticipationsById(ParticipantEvenementKey id){
		return repo.findById(id).orElse(null);
	}
	
	public void addParticipantEvenement(ParticipantEvenement pe) {
		repo.save(pe);
		try {
			emailService.sendParticipationEMail(pe);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void updateParticipantEvenement(ParticipantEvenement pe) {
		repo.save(pe);
	}

	public void deleteParticipation(ParticipantEvenementKey id) {
		ParticipantEvenement pe = repo.findById(id).orElse(null);
		repo.delete(pe);
	}
	
	public int countParticipationsByEvenementId(Long id) {
		return repo.countByEvenementId(id);
	}
}
