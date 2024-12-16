package fsk.glcc.gevents.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fsk.glcc.gevents.model.Evenement;
import fsk.glcc.gevents.model.Paiement;
import fsk.glcc.gevents.model.UserPrincipal;
import fsk.glcc.gevents.service.ParticipantEvenementService;
import jakarta.websocket.server.PathParam;

@Controller
@RequestMapping("/payment")
public class PaymentController {

	@Autowired
	private PaymentRestController paymentRestController;
	@Autowired EvenementController evenementController;
	@Autowired EvenementRestController evenementRestController;
	@Autowired ParticipantEvenementService peService;
	
	@GetMapping("/create-order")
	public String createOrder(@RequestParam Long eid, @RequestParam double amount, @RequestParam String currency, Model model) {
		Evenement evenement = evenementRestController.getEvenementById(eid);
		if(peService.countParticipationsByEvenementId(eid) < evenement.getCapacite()) {
			ResponseEntity<Map<String, String>> response = paymentRestController.createOrder(eid, amount, currency);
			if(response.getStatusCode() == HttpStatusCode.valueOf(200)) {
				model.addAttribute("amount", amount);
				model.addAttribute("currency", currency);
				model.addAttribute("orderId", response.getBody().get("orderId"));
				model.addAttribute("approvalLink", response.getBody().get("approvalLink"));
				model.addAttribute("eid", eid);
				return "confirm-payment";
			}
			return "payment-failure";
		} else {
			return "participation-error";
		}
	}
	
	@GetMapping("/capture")
	public String capturePayment(@RequestParam String token, @RequestParam Long eid, Model model, @AuthenticationPrincipal UserPrincipal user) {
		ResponseEntity<String> response = paymentRestController.capturePayment(token);
		if(response.getStatusCode() == HttpStatusCode.valueOf(200)) {
			evenementController.participerEvenement(model, eid, user);
			
			
			Evenement e = evenementRestController.getEvenementById(eid);
			Paiement paiement = new Paiement();
			paiement.setMontant(e.getPrix());
			paiement.setOrderId(token);
			paiement.setParticipant(user.getUtilisateur());
			paiement.setEvenement(e);
			paymentRestController.addPaiement(paiement);
			
			model.addAttribute("orderId", token);
			return "payment-success";
		}
		return "payment-failure";
	}
	
	@GetMapping("/cancel")
	public String cancelPayment(@RequestParam String token, Model model) {
		model.addAttribute("orderId", token);
		return "cancel-payment";
	}
	
	@PostMapping("/paiement")
	@PreAuthorize("hasAuthority('evenement:create')")
	public String addEvenement(Model model, @AuthenticationPrincipal UserPrincipal utilisateur, BindingResult bindingResult) {
		System.out.println("==========================================/n "+utilisateur.getUtilisateur().toString());
		if(utilisateur.getUtilisateur().isAccountVerified()) {
			try {
				
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
}
