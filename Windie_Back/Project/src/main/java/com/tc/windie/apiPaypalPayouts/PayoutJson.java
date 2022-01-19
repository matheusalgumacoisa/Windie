package com.tc.windie.apiPaypalPayouts;

import java.util.List;

public class PayoutJson {
	
	public static String email_subject = "Pagamento do Windie enviado";
	public static String email_message = "Seu pagamento do windie foi processado e se encontra na sua conta do PayPal";
	public static String currency ="BRL";


	
	public static String getJson(List<PayoutItem> pagamentos,int batch_id) { 
		 String json = "  { "
		+ "\"sender_batch_header\": {"
			+ "\"sender_batch_id\": \""+batch_id+"\","
		    + "\"recipient_type\": \"EMAIL\","
		    + "\"email_subject\": \""+email_subject+"!\","
		    + "\"email_message\": \""+email_message+"\""
		+ "},"
		+ "\"items\": [";
		 int count = 0;
		 for (PayoutItem payoutItem : pagamentos){
			
  json = json+ "{"
		    	+ "\"amount\": {"
		    		+ "\"value\": \""+payoutItem.value+"\","
		    		+ "\"currency\": \""+currency+"\"" 
		    	+ "},"
		    	+ "\"sender_item_id\": \""+payoutItem.send_id+"\","
		  		+ "\"recipient_wallet\": \"PAYPAL\","
		  		+ "\"receiver\": \""+payoutItem.receiver_email+"\""
		  	+ "}";//precisa de virgula para adicionar mais
  			if(count < pagamentos.size()-1)  json = json+ ",";
		  	count ++;	
		}

json = json + "]"
	+ "}";
		
		return json;
	
	}

}
