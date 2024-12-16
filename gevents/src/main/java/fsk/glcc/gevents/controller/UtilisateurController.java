package fsk.glcc.gevents.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import fsk.glcc.gevents.model.Evenement;
import fsk.glcc.gevents.model.UserPrincipal;
import fsk.glcc.gevents.model.Utilisateur;
import fsk.glcc.gevents.repository.UtilisateurRepository;
import fsk.glcc.gevents.service.UtilisateurService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

@Controller
public class UtilisateurController {

	@Autowired
	private UtilisateurRestController controller;
	@Autowired
	private UtilisateurRepository repo;
	@Autowired
	private UtilisateurService service;
	
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("utilisateur", new Utilisateur());
		return "register";
	}

	@PostMapping("/register")
	public String addUtilisateur(Model model, @Valid @ModelAttribute Utilisateur utilisateur, BindingResult bindingResult) {
		Utilisateur user = repo.findByEmail(utilisateur.getEmail());
		System.out.println("user: " + user);
		if(user != null) {
			bindingResult.addError(new FieldError("utilisateur", "email", "Adresse email déjà utilisée"));
		}
		
		try {
			controller.addUtilisateur(utilisateur);
			model.addAttribute("utilisateur", new Utilisateur());
		} catch (Exception e) {
//			bindingResult.addError(new FieldError("utilisateur", "nom", e.getMessage()));
		}
		
		if (bindingResult.hasErrors()) {
			return "register";
		}
		
		return "redirect:";
	}
	
	@GetMapping("/about")
	public String about(Model model) {
		return "about";
	}
	
	@GetMapping("/utilisateurs")
	public String getUtilisateurs(Model model) {
		model.addAttribute("pageTitle", "Utilisateurs");
		model.addAttribute("evenements", controller.getUtilisateurs());
		return "utilisateurs";
	}
	
	@GetMapping("/utilisateurs/{id}")
	public String getUtilisateurById(@PathVariable Long id, Model model){
		Utilisateur utilisateur = controller.getUtilisateurById(id);
		model.addAttribute("pageTitle", "Utilisateurs - " + utilisateur.getEmail());
		model.addAttribute("evenement", utilisateur);
		return "utilisateurs_details";
	}
	
	@PutMapping("/utilisateurs")
	public String updateUtilisateur(@RequestBody Utilisateur utilisateur) {
		controller.updateUtilisateur(utilisateur);
		return "utilisateurs";
	}
	
	@GetMapping("/confirm-email")
	public String confirmEmail(@RequestParam("token") String token) {
		try {
			if(service.verifyUser(token)) {
				return "redirect:/login";
			} else {
				return "tokenExpired";
			}
		} catch (Exception e) {
			return "tokenExpired";
		}
	}
	
	@GetMapping("/send-confirmation")
	public String envoyerConfirmation(@AuthenticationPrincipal UserPrincipal user) {
		try {
			service.sendRegistrationConfirmationEmail(user.getUtilisateur());
			return "redirect:/login";
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "verifier_email";
		}
	}
}
