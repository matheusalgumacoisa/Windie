package com.tc.windie.apiStripe;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import util.Debug;



public class ApiCheckout {
	
	private final RestTemplate restTemplate = new RestTemplate();
	private static ApiCheckout instance;
	//private static final String emailPagSeguro = "matheusantoniors@hotmail.com";//email do pag seguro
	//private static final String token = "28379D1E9032482596725EAA8BA87B3B"; //token gerado na conta de desenvolvedor do pag seguro
	private static final String stripe_secret_key_test = "sk_test_51KPb0aHO9lbngdjOi1V6pmqWef84NFABMHsBwVwC6Ynx4BGuCT3h0BxcHybIx17PA2fJBxCxepHyosTPBLCyt9hN00BuHm1fmD"; //token gerado na conta de desenvolvedor do pag seguro
	
	public static ApiCheckout getInstance() {
		if(instance==null) instance = new ApiCheckout();
		return instance;
	}
	
	
	
	public void expirarStripeSession(String session_id) {
		String url = "https://api.stripe.com/v1/checkout/sessions/"+session_id+"/expire";
		  
		  HttpHeaders headers = new HttpHeaders();
		  headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		  headers.setBearerAuth(stripe_secret_key_test);
		  
		
		  HttpEntity<String> entity = new HttpEntity<>("",headers);	  
		 /* ResponseEntity<String> response =*/ this.restTemplate.exchange(url ,HttpMethod.POST,entity,String.class);
		  
		  Debug.logDetalhe("stripe session expire: "+session_id);
		  
	}

	public String  generateStripeCheckout() {
		  String url = "https://api.stripe.com/v1/checkout/sessions";
		  //String xml = PsCobrancaXML.getBodyXML(pagamento_cod);
		  
		  HttpHeaders headers = new HttpHeaders();
		  headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		  headers.setBearerAuth(stripe_secret_key_test);
		  
		  MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		  map.add("cancel_url","http://localhost:4200/pgff");
		  map.add("success_url","http://localhost:4200/pgss");
		  map.add("mode","payment");
		  map.add("line_items[0][description]","Assinatura Windie");
		  map.add("line_items[0][price]","price_1KPbDEHO9lbngdjO3TGJ19La");
		  map.add("line_items[0][quantity]", "1");
		  //map.add("id", "27");
		
		  HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map,headers);	  
		  ResponseEntity<String> response = this.restTemplate.exchange(url ,HttpMethod.POST,entity,String.class);
		  
		  Debug.logDetalhe("checkout resposta: "+response.getBody());
		  
		  
		  return response.getBody();
	}
	
	public String getStripeCheckoutSession(String session_id) {
		 String url = "https://api.stripe.com/v1/checkout/sessions/"+session_id;
		  
		  HttpHeaders headers = new HttpHeaders();
		  headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		  headers.setBearerAuth(stripe_secret_key_test);
		  
		  //map.add("id", "27");
		
		  HttpEntity<String> entity = new HttpEntity<>("",headers);	  
		  ResponseEntity<String> response = this.restTemplate.exchange(url ,HttpMethod.GET,entity,String.class);
		  
		  Debug.logDetalhe("checkout resposta: "+response.getBody());
		  
		  return response.getBody();
	}
	
	
	/*public String getCheckoutCod(int pagamento_cod) throws SAXException, IOException, ParserConfigurationException {
		  String url = "https://ws.sandbox.pagseguro.uol.com.br/v2/checkout?email="+emailPagSeguro+"&token="+token;
		  String xml = PsCobrancaXML.getBodyXML(pagamento_cod);
		  
		  HttpHeaders headers = new HttpHeaders();
		  headers.setContentType(MediaType.APPLICATION_XML);
		  Debug.logDetalhe("xml request: "+xml );
		  //headers.setBearerAuth(getToken());
		
		  HttpEntity<String> entity = new HttpEntity<String>(xml,headers);	  
		  ResponseEntity<String> response = this.restTemplate.exchange(url ,HttpMethod.POST,entity,String.class);
		  
		  Debug.logDetalhe("checkout resposta: "+response.getBody());
		  String xml_response = response.getBody();
		  
		  
		  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		  factory.setNamespaceAware(true);
		  DocumentBuilder builder = factory.newDocumentBuilder();
		  Document document =  builder.parse(new ByteArrayInputStream(xml_response.getBytes()));
		  
		  document.getDocumentElement().normalize();
		  Element rootElement = document.getDocumentElement();

		  String cod = rootElement.getElementsByTagName("code").item(0).getTextContent();
		  Debug.logDetalhe("checkout cod: "+cod);
		  
		  return cod;
	}
	*/
	
	/*public boolean seTransacaoPaga(int reference, Date initialDate) throws ParserConfigurationException, SAXException, IOException {
		
		String url = "https://ws.sandbox.pagseguro.uol.com.br/v2/transactions?email="+emailPagSeguro+"&token="+token+"&refrence="+reference+"&initialDate="+initialDate+"T00:00";
		/*String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					+ "<transactions>"
					+ "<reference>"+reference+"</reference>"
					+ "</transactions>";*/
		
		/*HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_XML);
		Debug.logDetalhe("url request: "+url );
		
		//HttpEntity<String> entity = new HttpEntity<String>(""/*xml*///,headers);	  
		/*ResponseEntity<String> response = this.restTemplate.exchange(url ,HttpMethod.GET,entity,String.class);
		
		Debug.logDetalhe("consulta resposta: "+response.getBody());
		String xml_response = response.getBody();
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document =  builder.parse(new ByteArrayInputStream(xml_response.getBytes()));
		  
		document.getDocumentElement().normalize();
		Element rootElement = document.getDocumentElement();

		
		
		String status = rootElement.getElementsByTagName("status").item(0).getTextContent();
		
		Debug.logDetalhe("status = "+status);
		
		if(status.equals("3")) {
			return true;
		}else {
			return false;
		}
		
	}*/

}
