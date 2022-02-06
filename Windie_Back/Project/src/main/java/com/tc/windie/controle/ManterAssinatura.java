package com.tc.windie.controle;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import com.tc.windie.apiStripe.ApiCheckout;

import dao.PagamentoAssinaturaDAO;
import dao.VW_usuarioAssinaturaDAO;
import util.Config;
import util.CustomException;
import util.Debug;

@Component
public class ManterAssinatura {
	
	private static ManterAssinatura instance;
	
	public static ManterAssinatura getInstance() {
		if(instance==null) instance = new ManterAssinatura();
		return instance;
	}
	
	/*public String generateCheckout(int usuario_id) throws SAXException, IOException, ParserConfigurationException, SQLException, CustomException { // faz um checkout e retorna o código, cria um pagamento pendendte no banco
		
		
		if(!VW_usuarioAssinaturaDAO.getInstance().seAssinante(usuario_id)) {
		
			LocalDate data_atual = java.time.LocalDate.now();
			data_atual = Date.valueOf(data_atual.getYear()+"-"+data_atual.getMonthValue()+"-01").toLocalDate();
			Date mes_referente = Date.valueOf(data_atual);
			
			int pagamento_id = PagamentoAssinaturaDAO.getInstance().inserirPagamento(Float.parseFloat(Config.ValorDaAssinatura), mes_referente, usuario_id);
		
			String code = ApiCheckout.getInstance().getCheckoutCod(pagamento_id);
			
			return code;
		}else {
			throw new CustomException("O usuário ja é um assinante");
		}
		
	}*/
	
	public String generateStripeCheckout(int usuario_id) throws SAXException, IOException, ParserConfigurationException, SQLException, CustomException { // faz um checkout e retorna o código, cria um pagamento pendendte no banco
		
		Debug.logDetalhe("Gerar checkout para: usuario_"+usuario_id);
		if(!VW_usuarioAssinaturaDAO.getInstance().seAssinante(usuario_id)) {
			List<Integer> pagamentos = PagamentoAssinaturaDAO.getInstance().getPagamentosEmAberto(usuario_id);
			
			for (Integer pagamento : pagamentos) { //pagamentos pendentes no banco para esse usuario
				
				Debug.logDetalhe("Há pagamento pendente no banco para: pg_"+pagamento);
				String checkout_key = PagamentoAssinaturaDAO.getInstance().getCheckoutKey(pagamento);
				String session_json = ApiCheckout.getInstance().getStripeCheckoutSession(checkout_key);
				
				if(session_json.isEmpty() || session_json == null) { //caso o pagamento não tenha sessão no stripe
					PagamentoAssinaturaDAO.getInstance().removerPagamento(pagamento);
					Debug.logDetalhe("Não há sessão aberta no stripe para: pg_"+pagamento);
				}else { //caso pagamento tenha sessão
					Debug.logDetalhe("Há sessão aberta no stripe para: pg_"+pagamento);
					JSONObject jsonOBJ = new JSONObject(session_json);
					
					if(jsonOBJ.getString("status").equals("complete")&& jsonOBJ.getString("payment_status").equals("unpaid")) {// pagamento pendente no stripe
						Debug.logDetalhe("Há pagamento em aberto no banco com sessão e  pagamento pendente no stripe para: pg_"+pagamento);
						throw new CustomException("Pagamento em processamento");
					}else if(jsonOBJ.getString("status").equals("complete")&& jsonOBJ.getString("payment_status").equals("paid")) {// pagamento realizado no stripe
						processarPagamento(pagamento);
						Debug.logDetalhe("Há pagamento  em aberto no banco e realizado no stripe para: pg_"+pagamento);
						throw new CustomException("O usuário ja é um assinante");
					} else if(!jsonOBJ.getString("status").equals("expired")){ //não está pendente nem pago nem expirado no stripe
						Debug.logDetalhe("Há pagamento  em aberto no banco que não está pendente nem pago nem expirado no stripe para: pg_"+pagamento);
						ApiCheckout.getInstance().expirarStripeSession(checkout_key);
					}
				}
				
				PagamentoAssinaturaDAO.getInstance().removerPagamento(pagamento);
				
			}

			String generate_session_json = ApiCheckout.getInstance().generateStripeCheckout();
			JSONObject jsonOBJ = new JSONObject(generate_session_json);
			String sessionKey = jsonOBJ.getString("id");
			LocalDate data_atual = java.time.LocalDate.now();
			data_atual = Date.valueOf(data_atual.getYear()+"-"+data_atual.getMonthValue()+"-01").toLocalDate();
			Date mes_referente = Date.valueOf(data_atual);
			PagamentoAssinaturaDAO.getInstance().inserirPagamento(Float.parseFloat(Config.ValorDaAssinatura), mes_referente, usuario_id,sessionKey);
			
			
			return jsonOBJ.getString("url");
			
		}else {
			throw new CustomException("O usuário ja é um assinante");
		}
		
	}
	
	public boolean sePagamentoPendente(int usuario_id) throws SQLException { //Há pagamento em aberto no banco com sessão e  pagamento pendente no stripe ?
		
		List<Integer> pagamentos = PagamentoAssinaturaDAO.getInstance().getPagamentosEmAberto(usuario_id);
		
		Debug.logDetalhe("Se pagamento pendente para: usuario_"+usuario_id);
		
		for (Integer pagamento : pagamentos) {
			String checkout_key = PagamentoAssinaturaDAO.getInstance().getCheckoutKey(pagamento);
			String session_json = ApiCheckout.getInstance().getStripeCheckoutSession(checkout_key);
			
			if(session_json.isEmpty() || session_json == null) {
				
				PagamentoAssinaturaDAO.getInstance().removerPagamento(pagamento);
				Debug.logDetalhe("Há pagamento em aberto no banco sem sessão e  pagamento pendente no stripe para: pg_"+pagamento);
				return false;
			}
			
			JSONObject jsonOBJ = new JSONObject(session_json);
			
			if(jsonOBJ.getString("status").equals("complete")&& jsonOBJ.getString("payment_status").equals("unpaid")) {// status complete significa que o cliente realizou o pagamento, payment_status paid significa que o pagamento foi processado
				Debug.logDetalhe("Há pagamento em aberto no banco com sessão e  pagamento pendente no stripe para: pg_"+pagamento);
				return true;
			}
			
			if(jsonOBJ.getString("status").equals("complete")&& jsonOBJ.getString("payment_status").equals("paid")) {// status complete significa que o cliente realizou o pagamento, payment_status paid significa que o pagamento foi processado
				Debug.logDetalhe("Há pagamento  em aberto no banco e realizado no stripe para: pg_"+pagamento);
				processarPagamento(pagamento);
				return true;
			}
			Debug.logDetalhe("Não há pagamento em aberto no banco com sessão e  pagamento pendente no stripe para: pg_"+pagamento);
			Debug.logDetalhe("Não há pagamento  em aberto no banco e realizado no stripe para: pg_"+pagamento);
		}
		
		return false;
		
	}
	
	
	public void processarPagamento(int pagamento_id) throws SQLException {
		LocalDate data_atual = java.time.LocalDate.now();	
		Date data_pagamento = Date.valueOf(data_atual);
		PagamentoAssinaturaDAO.getInstance().inserirDataPagamento(data_pagamento, pagamento_id);
		
	}
	
	
	/*@Scheduled(fixedDelay = 600000) //de 10 em 10 minutos
	public void processarPagamentos() {
		
		try {
			List<Integer> pagamentos_em_aberto;
			pagamentos_em_aberto = PagamentoAssinaturaDAO.getInstance().getPagamentosEmAberto();
			LocalDate data_atual = java.time.LocalDate.now();
			Date initial_date =  Date.valueOf(data_atual.minusDays(27));//por quanto tempo são pesquisados os pagamentos no caso nos ultimos 27 dias com o limite que não pode passar de 30
			

			
			for (Integer pagamento : pagamentos_em_aberto) {
				Debug.logDetalhe("verificando pagamento para pagamento: "+pagamento);
				try {
					if(ApiCheckout.getInstance().seTransacaoPaga(pagamento,initial_date))
					{
						Date data_pagamento = Date.valueOf(data_atual);
						
						PagamentoAssinaturaDAO.getInstance().inserirDataPagamento(data_pagamento, pagamento);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}*/
	
	@Scheduled(fixedDelay = 600000) //de 10 em 10 minutos
	public void processarPagamentos() {
		Debug.logDetalhe("processando pagamentos de assinatura");
		try {
			List<Integer> pagamentos_em_aberto;
			pagamentos_em_aberto = PagamentoAssinaturaDAO.getInstance().getPagamentosEmAberto();
			
			for (Integer pagamento : pagamentos_em_aberto) {
				String checkout_key = PagamentoAssinaturaDAO.getInstance().getCheckoutKey(pagamento);
				String session_json = ApiCheckout.getInstance().getStripeCheckoutSession(checkout_key);
				
				if(session_json.isEmpty() || session_json == null) { //caso o pagamento não tenha sessão no stripe
					Debug.logDetalhe("Pagamento  em aberto no banco e sem sessão no stripe para: pg_"+pagamento);
					PagamentoAssinaturaDAO.getInstance().removerPagamento(pagamento);
				}else {
					
					JSONObject jsonOBJ = new JSONObject(session_json);
					
					if(jsonOBJ.getString("status").equals("complete")&& jsonOBJ.getString("payment_status").equals("unpaid")) {// pagamento pendente no stripe
						Debug.logOutput("pagamento em processamento :"+session_json);
					}else if(jsonOBJ.getString("status").equals("complete")&& jsonOBJ.getString("payment_status").equals("paid")) {// pagamento realizado no stripe
						Debug.logDetalhe("Pagamento em aberto no banco e realizado no stripe para: pg_"+pagamento);
						processarPagamento(pagamento);
					}
					
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}

}
