package fsk.glcc.gevents.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import fsk.glcc.gevents.model.Evenement;
import fsk.glcc.gevents.repository.EvenementRepository;

@Service
public class EvenementService {

	@Autowired
	private EvenementRepository repo;
	
	public List<Evenement> getEvenements(){
		return repo.findAll(Sort.by(Sort.Direction.ASC, "date"));
	}
	
	public List<Evenement> getEvenementsExpires(){
		return repo.findEvenementsExpires(Sort.by(Sort.Direction.ASC, "date"));
	}
	
	public List<Evenement> getEvenementsNonExpires(){
		return repo.findEvenementsNonExpires(Sort.by(Sort.Direction.ASC, "date"));
	}
	
	public List<Evenement> getEvenementsOrganisesParMoi(Long organisateurId){
		return repo.findEvenementsOrganisesParMoi(organisateurId, Sort.by(Sort.Direction.ASC, "date"));
	}
	
	public Evenement getEvenementById(Long id) {
		return repo.findById(id).orElse(null);
	}
	
	public void addEvenement(Evenement evenement) {
		repo.save(evenement);
	}
	
	public void updateEvenement(Evenement evenement) {
		repo.save(evenement);
	}
	
	public void deleteEvenement(Long id) {
		repo.deleteById(id);
	}
}
