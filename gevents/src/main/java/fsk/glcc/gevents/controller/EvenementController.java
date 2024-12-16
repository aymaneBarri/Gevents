package fsk.glcc.gevents.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fsk.glcc.gevents.model.Evenement;
import fsk.glcc.gevents.model.ParticipantEvenement;
import fsk.glcc.gevents.model.ParticipantEvenementKey;
import fsk.glcc.gevents.model.UserPrincipal;
import fsk.glcc.gevents.model.Utilisateur;
import fsk.glcc.gevents.service.ParticipantEvenementService;
import jakarta.validation.Valid;

@Controller
//@PreAuthorize("hasRole('ORGANISATEUR')")
public class EvenementController {
//	@GetMapping
//	public String sayHello() {
//		return "hello";
//	}
	
	@Autowired
	private EvenementRestController controller;
	@Autowired ParticipantEvenementService peService;
	
	@GetMapping("/evenements")
	@PreAuthorize("hasAuthority('evenement:read')")
	public String getEvenements(Model model, @AuthenticationPrincipal UserPrincipal utilisateur) {
		if(utilisateur.getUtilisateur().isAccountVerified()) {
			model.addAttribute("pageTitle", "Evenements");

			List<Double> noteEvenementsOrganises = new ArrayList<>(), 
					noteEvenements = new ArrayList<>(), 
					noteEvenementsExpires = new ArrayList<>();
			List<Boolean> participantEvenementsOrganises = new ArrayList<>(),
					participantEvenements = new ArrayList<>(), 
					participantEvenementsExpires = new ArrayList<>();
			
			List<Evenement> evenementsOrganises = controller.getEvenementsOrganisesParMoi(utilisateur.getUtilisateur().getId());
			for(Evenement e : evenementsOrganises) {
				boolean participant = false;
				double note = 0;
				if (e.getParticipants().isEmpty()) {
					noteEvenementsOrganises.add(0.0);
//					break;
				}
				for(ParticipantEvenement pe : e.getParticipants()) {
					if(pe.getParticipant().getId() == utilisateur.getUtilisateur().getId())
						participant = true;
					note += pe.getRating();
				}
				participantEvenementsOrganises.add(participant);
				noteEvenementsOrganises.add(note/e.getParticipants().size());
			}
			
			List<Evenement> evenements = controller.getEvenementsNonExpires();
			for(Evenement e : evenements) {
				System.out.println("e: "+e);
				boolean participant = false;
				double note = 0;
				if (e.getParticipants().isEmpty()) {
					noteEvenements.add(0.0);
//					break;
				}
				for(ParticipantEvenement pe : e.getParticipants()) {
					System.out.println("pe: "+pe);
					if(pe.getParticipant().getId() == utilisateur.getUtilisateur().getId())
						participant = true;
					note += pe.getRating();
				}
				System.out.println("p: "+participant);
				participantEvenements.add(participant);
				noteEvenements.add(note/e.getParticipants().size());
			}
			
			List<Evenement> evenementsExpires = controller.getEvenementsExpires();
			for(Evenement e : evenementsExpires) {
				boolean participant = false;
				double note = 0;
				if (e.getParticipants().isEmpty()) {
					noteEvenementsExpires.add(0.0);
//					break;
				}
				for(ParticipantEvenement pe : e.getParticipants()) {
					if(pe.getParticipant().getId() == utilisateur.getUtilisateur().getId())
						participant = true;
					note += pe.getRating();
				}
				participantEvenementsExpires.add(participant);
				noteEvenementsExpires.add(note/e.getParticipants().size());
			}
			
			model.addAttribute("evenementsOrganises", evenementsOrganises);
			model.addAttribute("noteEvenementsOrganises", noteEvenementsOrganises);
			model.addAttribute("participantEvenementsOrganises", participantEvenementsOrganises);
			
			model.addAttribute("evenements", evenements);
			model.addAttribute("noteEvenements", noteEvenements);
			model.addAttribute("participantEvenements", participantEvenements);
			
			model.addAttribute("evenementsExpires", evenementsExpires);
			model.addAttribute("noteEvenementsExpires", noteEvenementsExpires);
			model.addAttribute("participantEvenementsExpires", participantEvenementsExpires);
			return "evenements";
		}
		return "verifier_email";
		
	}
	
	@GetMapping("/evenements/{id}")
	public String getEvenementById(@PathVariable Long id, Model model, @AuthenticationPrincipal UserPrincipal utilisateur){
		if(utilisateur.getUtilisateur().isAccountVerified()) {
			Evenement evenement = controller.getEvenementById(id);
			model.addAttribute("pageTitle", "Events - " + evenement.getTitre());
			model.addAttribute("evenement", evenement);
			double rating = 0;
			for(ParticipantEvenement pe : evenement.getParticipants()) {
				rating += pe.getRating();
			}
			model.addAttribute("rating", Math.round(rating/evenement.getParticipants().size()));
			
			boolean isParticipating = false;
			for (ParticipantEvenement pe : evenement.getParticipants()) {
				if (pe.getParticipant().getId() == utilisateur.getUtilisateur().getId()) {
					isParticipating = true;
				}
			}
			model.addAttribute("isParticipating", isParticipating);
			
			return "evenement_details";
		}
		return "verifier_email";
	}
	
	@GetMapping("/evenements/create")
	@PreAuthorize("hasAuthority('evenement:create')")
	public String addEvenement(Model model, @AuthenticationPrincipal UserPrincipal utilisateur) {
		if(utilisateur.getUtilisateur().isAccountVerified()) {
			model.addAttribute("pageTitle", "Evenements");
			model.addAttribute("evenement", new Evenement());
			model.addAttribute("create_success", false);
			return "evenement_create";
		}
		return "verifier_email";
	}
	
	@PostMapping("/evenements/{id}")
	@PreAuthorize("hasAuthority('evenement:update')")
	public String updateEvenement(Model model, @Valid @ModelAttribute Evenement evenement, BindingResult bindingResult, @AuthenticationPrincipal UserPrincipal utilisateur) {		
		if(utilisateur.getUtilisateur().isAccountVerified()) {
			try {
				controller.updateEvenement(evenement);
				model.addAttribute("evenement", new Evenement());
			} catch (Exception e) {
				bindingResult.addError(new FieldError("evenement", "titre", e.getMessage()));
			}
			
			if (bindingResult.hasErrors()) {
				return "evenements/" + evenement.getId();
			}
			
			return "redirect:/evenements";
		}
		return "verifier_email";
	}
	
	@PostMapping("/evenements")
	@PreAuthorize("hasAuthority('evenement:create')")
	public String addEvenement(Model model, @Valid @ModelAttribute Evenement evenement, @AuthenticationPrincipal UserPrincipal utilisateur, BindingResult bindingResult) {
		System.out.println("==========================================/n "+utilisateur.getUtilisateur().toString());
		if(utilisateur.getUtilisateur().isAccountVerified()) {
			try {
				evenement.setOrganisateur(utilisateur.getUtilisateur());
				controller.addEvenement(evenement);
				model.addAttribute("evenement", new Evenement());
				model.addAttribute("create_success", true);
			} catch (Exception e) {
				bindingResult.addError(new FieldError("evenement", "titre", e.getMessage()));
			}
			
			if (bindingResult.hasErrors()) {
				return "evenements/create";
			}
			
			return "redirect:/evenements";
		}
		return "verifier_email";
	}
		
	@GetMapping("/evenements/delete/{id}")
	@PreAuthorize("hasAuthority('evenement:delete')")
	public String deleteEvenement(Model model, @PathVariable Long id, @AuthenticationPrincipal UserPrincipal utilisateur) {
		if(utilisateur.getUtilisateur().isAccountVerified()) {
			controller.deleteEvenement(id);
			model.addAttribute("cancel_success", true);
			return "redirect:/evenements";
		}
		return "verifier_email";
	}
	
	@GetMapping("/evenements/participer/{id}")
	@PreAuthorize("hasRole('PARTICIPANT')")
	public String participerEvenement(Model model, @PathVariable Long id, @AuthenticationPrincipal UserPrincipal utilisateur) {
		
		if(utilisateur.getUtilisateur().isAccountVerified()) {
			Evenement evenement = controller.getEvenementById(id);
			if(peService.countParticipationsByEvenementId(id) < evenement.getCapacite()) {
		//		System.out.println("perticiperEvenement1: "+evenement);
				Utilisateur participant = utilisateur.getUtilisateur();
				ParticipantEvenement pe = new ParticipantEvenement();
				pe.setEvenement(evenement);
				pe.setParticipant(participant);
				ParticipantEvenementKey key = new ParticipantEvenementKey();
				key.setEvenementId(evenement.getId());
				key.setParticipantId(participant.getId());
				pe.setId(key);
		//		evenement.getParticipants().add(pe);
				System.out.println("perticiperEvenement2: "+evenement);
				controller.participerEvenement(pe, key);
				model.addAttribute("participate_success", true);
				System.out.println("perticiperEvenement3: "+evenement);
				return "redirect:/evenements";
			} else {
				return "participation-error";
			}
		}
		return "verifier_email";
	}
	
	@GetMapping("/evenements/rate/{id}")
	@PreAuthorize("hasRole('PARTICIPANT')")
	public String noterEvenement(Model model, @PathVariable Long id, @RequestParam("rating") String ratingString, @AuthenticationPrincipal UserPrincipal utilisateur) {
		
		if(utilisateur.getUtilisateur().isAccountVerified()) {
			Evenement evenement = controller.getEvenementById(id);
			try {
				int rating = Integer.parseInt(ratingString);
				if(rating >= 0 && rating <=5) {
			//		System.out.println("perticiperEvenement1: "+evenement);
					Utilisateur participant = utilisateur.getUtilisateur();
					ParticipantEvenement pe = new ParticipantEvenement();
					pe.setEvenement(evenement);
					pe.setParticipant(participant);
					pe.setRating(rating);
					ParticipantEvenementKey key = new ParticipantEvenementKey();
					key.setEvenementId(evenement.getId());
					key.setParticipantId(participant.getId());
					pe.setId(key);
			//		evenement.getParticipants().add(pe);
					controller.noterEvenement(pe);
					return "redirect:/evenements";
				} else {
					return "participation-error";
				}
			} catch (Exception e) {
				return "participation-error";
			}
			
		}
		return "verifier_email";
	}
	
	@GetMapping("/evenements/kick")
	@PreAuthorize("hasAuthority('evenement:update')")
	public String kickFromEvenement(Model model, @RequestParam(name = "eid") Long eid, @RequestParam(name = "pid") Long pid, @AuthenticationPrincipal UserPrincipal utilisateur) {
		
		if(utilisateur.getUtilisateur().isAccountVerified()) {
			controller.kickFromEvenement(eid, pid);
			model.addAttribute("kick_success", true);
			return "redirect:/evenements/"+eid;
		}
		return "verifier_email";
	}
}

