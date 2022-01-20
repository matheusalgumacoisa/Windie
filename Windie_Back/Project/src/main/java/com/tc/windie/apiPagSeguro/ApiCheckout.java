package com.tc.windie.apiPagSeguro;

import java.io.IOException;
import java.sql.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.w3c.dom.Element;

import util.Debug;

public class ApiCheckout {
	
	private final RestTemplate restTemplate = new RestTemplate();
	private static ApiCheckout instance;
	private static final String emailPagSeguro = "matheusantoniors@hotmail.com";//email do pag seguro
	private static final String token = "28379D1E9032482596725EAA8BA87B3B"; //token gerado na conta de desenvolvedor do pag seguro
	
	public static ApiCheckout getInstance() {
		if(instance==null) instance = new ApiCheckout();
		return instance;
	}
	
	public String getCheckoutCod(int pagamento_cod) throws SAXException, IOException, ParserConfigurationException {				
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
	
	
	public boolean seTransacaoPaga(int reference, Date initialDate) throws ParserConfigurationException, SAXException, IOException {
		
		String url = "https://ws.sandbox.pagseguro.uol.com.br/v2/transactions?email="+emailPagSeguro+"&token="+token+"&refrence="+reference+"&initialDate="+initialDate+"T00:00";
		/*String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					+ "<transactions>"
					+ "<reference>"+reference+"</reference>"
					+ "</transactions>";*/
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_XML);
		Debug.logDetalhe("url request: "+url );
		
		HttpEntity<String> entity = new HttpEntity<String>(""/*xml*/,headers);	  
		ResponseEntity<String> response = this.restTemplate.exchange(url ,HttpMethod.GET,entity,String.class);
		
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
		
	}

}
