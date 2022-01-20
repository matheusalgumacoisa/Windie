package com.tc.windie.controle;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import com.tc.windie.apiPagSeguro.ApiCheckout;
import dao.PagamentoAssinaturaDAO;
import dao.VW_usuarioAssinaturaDAO;
import util.Config;
import util.CustomException;

@Component
public class ManterAssinatura {
	
	private static ManterAssinatura instance;
	
	public static ManterAssinatura getInstance() {
		if(instance==null) instance = new ManterAssinatura();
		return instance;
	}
	
	public String generateCheckout(int usuario_id) throws SAXException, IOException, ParserConfigurationException, SQLException, CustomException { // faz um checkout e retorna o código, cria um pagamento pendendte no banco
		
		
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
		
	}
	
	
	@Scheduled(fixedDelay = 600000) //de 10 em 10 minutos
	public void processarPagamentos() {
		
		try {
			List<Integer> pagamentos_em_aberto;
			pagamentos_em_aberto = PagamentoAssinaturaDAO.getInstance().getPagamentosEmAberto();
			LocalDate data_atual = java.time.LocalDate.now();
			Date initial_date =  Date.valueOf(data_atual.minusDays(27));//por quanto tempo são pesquisados os pagamentos no caso nos ultimos 27 dias com o limite que não pode passar de 30
			
			for (Integer pagamento : pagamentos_em_aberto) {
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
		
		
	}

}
