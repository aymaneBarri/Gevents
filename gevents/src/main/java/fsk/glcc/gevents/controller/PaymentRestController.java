package fsk.glcc.gevents.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fsk.glcc.gevents.model.Evenement;
import fsk.glcc.gevents.model.Paiement;
import fsk.glcc.gevents.model.UserPrincipal;
import fsk.glcc.gevents.service.PaypalService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/api/paypal")
public class PaymentRestController {

	@Autowired
    private PaypalService payPalService;
    
//    public PaymentRestController(PaypalService payPalService) {
//    	this.payPalService = payPalService;
//    }

    @GetMapping("/create-order")
    public ResponseEntity<Map<String, String>> createOrder(@PathParam("eid") Long eid, @PathParam("amount") double amount, @PathParam("currency") String currency) {
//        try {
//            String orderId = payPalService.createOrder(amount, currency);
//            return ResponseEntity.ok(orderId);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//        }
    	try {
//            double amount = Double.parseDouble(payload.get("amount").toString());
//            String currency = payload.get("currency").toString();
            String orderId = payPalService.createOrder(eid,amount, currency);

            // Retrieve the approval link from PayPal
            String approvalLink = "https://www.sandbox.paypal.com/checkoutnow?token=" + orderId;
            Map<String, String> response = new HashMap<>();
            response.put("orderId", orderId);
            response.put("approvalLink", approvalLink);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", e.getMessage()));
        }
    }
    
    @PostMapping("/capture-payment")
    public ResponseEntity<String> capturePayment(@RequestParam String orderId) {
        try {
            String captureDetails = payPalService.capturePayment(orderId);
            return ResponseEntity.ok(captureDetails);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
//    	try {
//            String captureDetails = payPalService.capturePayment(orderId);
//            model.addAttribute("orderId", orderId);
//            return "payment-success"; // Redirect to the success page
//        } catch (Exception e) {
//            return "payment-failure"; // Redirect to the failure page
//        }
    }
    
    @PostMapping("/paiements")
	public void addPaiement(@RequestBody Paiement paiement) {
		payPalService.addPaiement(paiement);
	}
}