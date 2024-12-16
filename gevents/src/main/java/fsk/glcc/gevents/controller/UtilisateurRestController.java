package fsk.glcc.gevents.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fsk.glcc.gevents.model.Utilisateur;
import fsk.glcc.gevents.service.UtilisateurService;

@RestController
@RequestMapping("/api")
public class UtilisateurRestController {

	@Autowired
	private UtilisateurService service;
	
	@GetMapping("/utilisateurs")
	public List<Utilisateur> getUtilisateurs(){
		System.out.println("getUtilisateurs");
		return service.getUtilisateurs();
	}
	
	@GetMapping("/utilisateurs/{id}")
	public Utilisateur getUtilisateurById(@PathVariable Long id){
		System.out.println("getUtilisateurById " + id);
		return service.getUtilisateurById(id);
	}
	
	@PostMapping("/utilisateurs")
	public void addUtilisateur(@RequestBody Utilisateur utilisateur) {
		System.out.println("addUtilisateur " + utilisateur);
		service.addUtilisateur(utilisateur);
	}
	
	@PutMapping("/utilisateurs")
	public void updateUtilisateur(@RequestBody Utilisateur utilisateur) {
		System.out.println("updateUtilisateur " + utilisateur);
		service.updateUtilisateur(utilisateur);
	}
	
	@DeleteMapping("/utilisateurs/{id}")
	public void deleteUtilisateur(@PathVariable Long id) {
		System.out.println("deleteUtilisateur " + id);
		service.deleteUtilisateur(id);
	}

}
