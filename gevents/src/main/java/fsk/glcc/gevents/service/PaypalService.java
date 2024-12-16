package fsk.glcc.gevents.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fsk.glcc.gevents.model.Evenement;
import fsk.glcc.gevents.model.Paiement;
import fsk.glcc.gevents.repository.PaiementRepository;

@Service
public class PaypalService {

    @Value("${paypal.clientId}")
    private String clientId;

    @Value("${paypal.secret}")
    private String clientSecret;

//    @Value("${paypal.mode}")
//    private String mode;
    
    @Autowired
    private PaiementRepository paiementRepo;

    private static final String BASE_URL = "https://api-m.sandbox.paypal.com"; // Use sandbox for testing
    private static final String AUTH_URL = BASE_URL + "/v1/oauth2/token";
    private static final String ORDER_URL = BASE_URL + "/v2/checkout/orders";

    public String createOrder(Long eid, double amount, String currency) throws Exception {
        String accessToken = getAccessToken();

        // Order request payload
        String payload = "{"
                + "\"intent\":\"CAPTURE\","
                + "\"application_context\":{"
                + "\"return_url\":\"http://localhost:8080/payment/capture?eid=" + eid + "\","
                + "\"cancel_url\":\"http://localhost:8080/payment/cancel\""
                + "},"
                + "\"purchase_units\":[{"
                + "\"amount\":{"
                + "\"currency_code\":\"" + currency + "\","
                + "\"value\":\"" + amount + "\""
                + "}}]"
                + "}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<String> request = new HttpEntity<>(payload, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(ORDER_URL, request, String.class);

        if (response.getStatusCode() == HttpStatus.CREATED) {
            JsonObject jsonResponse = JsonParser.parseString(response.getBody()).getAsJsonObject();
            return jsonResponse.get("id").getAsString(); // PayPal Order ID
        } else {
            throw new Exception("Failed to create order");
        }
    }

    private String getAccessToken() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientId, clientSecret);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> request = new HttpEntity<>("grant_type=client_credentials", headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(AUTH_URL, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            JsonObject jsonResponse = JsonParser.parseString(response.getBody()).getAsJsonObject();
            return jsonResponse.get("access_token").getAsString();
        } else {
            throw new Exception("Failed to retrieve access token");
        }
    }
    
    public String capturePayment(String orderId) throws Exception {
        String accessToken = getAccessToken(); // Get OAuth2 token

        String captureUrl = BASE_URL + "/v2/checkout/orders/" + orderId + "/capture";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(captureUrl, request, String.class);

        if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK) {
            JsonObject jsonResponse = JsonParser.parseString(response.getBody()).getAsJsonObject();
            return jsonResponse.toString(); // Return capture details
        } else {
            throw new Exception("Payment capture failed: " + response.getBody());
        }
    }
    
    public void addPaiement(Paiement paiement) {
		paiementRepo.save(paiement);
	}
}