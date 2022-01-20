package com.tc.windie.apiPagSeguro;

import util.Config;

public class PsCobrancaXML {

	public static String description = "Assinatura Windie";
	public static String currency = "BRL";
	public static String notificacao_url = "";
	public static String soft_descriptor = "Windie Software Assinatura";
	public static String url_redirect = "http://localhost:4200";
	
	public static String getBodyXML(int reference) { //reference é o identificador da transação
		String json = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
				+ "<checkout>\r\n"
				+ "   <currency>"+currency+"</currency>\r\n"
				+ "   <reference>"+reference+"</reference>\r\n"
				+ "   <items>\r\n"
				+ "        <item>\r\n"
				+ "            <amount>"+Config.ValorDaAssinatura+"</amount>\r\n"
				+ "            <description>"+soft_descriptor+"</description>\r\n"
				+ "            <id>1</id>\r\n"
				+ "            <quantity>1</quantity>\r\n"
				+ "            <weight>0</weight>\r\n"
				+ "        </item>\r\n"
				+ "   </items>\r\n"
				+ "   <shippingAddressRequired>false</shippingAddressRequired>\r\n"
				+ "	  <redirectURL>"+url_redirect+"</redirectURL>"
				+ "</checkout>";
		
		return json;
	}
	
}
