package fsk.glcc.gevents.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fsk.glcc.gevents.model.Evenement;
import fsk.glcc.gevents.model.ParticipantEvenement;
import fsk.glcc.gevents.model.ParticipantEvenementKey;
import fsk.glcc.gevents.service.EvenementService;
import fsk.glcc.gevents.service.ParticipantEvenementService;

@RestController
@RequestMapping("/api")
public class EvenementRestController {

	@Autowired
	private EvenementService service;
	@Autowired
	private ParticipantEvenementService peService;
	
	@GetMapping("/evenements")
	public List<Evenement> getEvenements(){
		System.out.println("getEvenements");
		return service.getEvenements();
	}
	
	@GetMapping("/evenements/expired")
	public List<Evenement> getEvenementsExpires(){
		System.out.println("getEvenementsExpires");
		return service.getEvenementsExpires();
	}
	
	@GetMapping("/evenements/non-expired")
	public List<Evenement> getEvenementsNonExpires(){
		System.out.println("getEvenementsNonExpires");
		return service.getEvenementsNonExpires();
	}
	
	@GetMapping("/evenements/organized-by/{id}")
	public List<Evenement> getEvenementsOrganisesParMoi(@PathVariable Long id){
		System.out.println("getEvenementsNonExpires");
		return service.getEvenementsOrganisesParMoi(id);
	}
	
	@GetMapping("/evenements/{id}")
	public Evenement getEvenementById(@PathVariable Long id){
		System.out.println("getEvenementById " + id);
		return service.getEvenementById(id);
	}
	
	@PostMapping("/evenements")
	public void addEvenement(@RequestBody Evenement evenement) {
		System.out.println("addEvenement " + evenement);
		service.addEvenement(evenement);
	}
	
	@PutMapping("/evenements")
	public void updateEvenement(@RequestBody Evenement evenement) {
		System.out.println("updateEvenement " + evenement);
		service.updateEvenement(evenement);
	}
	
	@DeleteMapping("/evenements/{id}")
	public void deleteEvenement(@PathVariable Long id) {
		System.out.println("deleteEvenement " + id);
		service.deleteEvenement(id);
	}
	
	@GetMapping("/evenements/participer/{id}")
	public void participerEvenement(@RequestBody ParticipantEvenement pe, ParticipantEvenementKey key) {
		System.out.println("participerEvenement " + pe);
		if(peService.getParticipationsById(key) == null) {
			peService.addParticipantEvenement(pe);
		}
	}
	
	@GetMapping("/evenements/rate/{id}")
	public void noterEvenement(@RequestBody ParticipantEvenement pe) {
		System.out.println("noterEvenement " + pe);
		peService.updateParticipantEvenement(pe);
	}
	
	@GetMapping("/evenements/kick")
	public void kickFromEvenement(@RequestParam(name = "eid") Long eid, @RequestParam(name = "pid") Long pid) {
		ParticipantEvenementKey key = new ParticipantEvenementKey();
		key.setEvenementId(eid);
		key.setParticipantId(pid);
		if(peService.getParticipationsById(key) != null) {
			peService.deleteParticipation(key);
		}
	}
}
