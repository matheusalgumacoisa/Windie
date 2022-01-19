package com.tc.windie.apiPaypalPayouts;

import java.util.List;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import util.Debug;

@Service
public class ApiPayouts {
	
	  private final RestTemplate restTemplate = new RestTemplate(); 
	  private static ApiPayouts instance;
	  public static String AccessToken;// = "A21AAJsI74np5RNCaThT8f3jQxGek2R_yVbmgRRwwhI-qr8NMda8OBDjzfJxQgjVHVX4RmhM1R-1L9Tc7Bla4pK1ApNAgsOSg";
	  
	  public static ApiPayouts getInstance() {
		  if(instance == null) instance = new ApiPayouts();
		  return instance;
	  }
	  
	  public ApiPayouts(){
;
	  }
	  
	  public boolean enviarPagamentos(List<PayoutItem> pagamentos,int batch_id) { //retorna true se nenhum erro for encontrado no envio e o pagamento se encontra como pendente no paypal
		  String url = "https://api-m.sandbox.paypal.com/v1/payments/payouts";
		  String json = PayoutJson.getJson(pagamentos, batch_id);
		  HttpHeaders headers = new HttpHeaders();
		  headers.setContentType(MediaType.APPLICATION_JSON);
		  Debug.logDetalhe("json request: "+json );
		  headers.setBearerAuth(getToken());
		
		  HttpEntity<String> entity = new HttpEntity<String>(json,headers);	  
		  ResponseEntity<String> response = this.restTemplate.exchange(url ,HttpMethod.POST,entity,String.class);
		  
		  Debug.logDetalhe("pagamento resposta: "+response.getBody());
		  JSONObject jsonObj = new JSONObject(response.getBody());
		  JSONObject batch_header = jsonObj.getJSONObject("batch_header");
		  if(batch_header.getString("batch_status").equals("PENDING")) {
			  return true;
		  } else {
			  return false;
		  }
	  }
	  
	  public String getToken() {
		  String url = "https://api-m.sandbox.paypal.com/v1/oauth2/token";
		  
		  HttpHeaders headers = new HttpHeaders();
		  //headers.setContentType(MediaType.APPLICATION_JSON);
		  
		  headers.setBasicAuth("AAWzyl4ZYQJwf5VIhq2bPL8eZL6FrfC0pY51SSFAhYn30T4zWhCZgMFgT4AzkOWWYFSbL9GXgnWn2hzcDD", "EN2vkHr9UyICXBeQ5mbg7H9I8JnnO3dbnMXCxEOUzKhyg_zuV1N28HGcExI20dvKUFL0HZgPqfjT1X0HH");
		
		  HttpEntity<String> entity = new HttpEntity<String>("grant_type=client_credentials",headers);	  
		  ResponseEntity<String> response = this.restTemplate.exchange(url ,HttpMethod.POST,entity,String.class);
		  
		  Debug.logDetalhe("get paypal token: "+response.getBody());
		  JSONObject jsonObj = new JSONObject(response.getBody());
		  ApiPayouts.AccessToken = jsonObj.getString("access_token");
		  return AccessToken;
	  }

}
