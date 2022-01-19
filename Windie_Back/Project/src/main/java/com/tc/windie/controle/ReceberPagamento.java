package com.tc.windie.controle;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.tc.windie.apiPaypalPayouts.ApiPayouts;
import com.tc.windie.apiPaypalPayouts.PayoutItem;

import dao.DesenvolvedorDAO;
import dao.HorasJogadasDAO;
import dao.JogoDAO;
import dao.PagamentoAssinaturaDAO;
import dao.PagamentoDesenvolvedorDAO;
import modelo.JogoModelo;
import modelo.PagamentoDesenvolvedorModelo;
import util.Config;
import util.CustomException;
import util.Debug;
import util.SoftwareEstado;

@Component
public class ReceberPagamento {

	private static ReceberPagamento instance;
	
	public static ReceberPagamento getInstance() {
		if(instance == null) instance = new ReceberPagamento();
		return instance;
	}
	
	public float getHoras(int desenvolvedor_id, Date mes) throws SQLException {
		List<JogoModelo> jogos = JogoDAO.getInstance().getListaByDesenvolvedor(desenvolvedor_id);
		float horas = 0;
		for (JogoModelo jogoModelo : jogos) {
			horas = horas + HorasJogadasDAO.getInstance().getHorasJogadasMesJogo(jogoModelo.getJogo_id(), mes).horas;
		}
		
		return horas;
	}
	
	public float getFaturado(int desenvolvedor_id,Date mes) throws SQLException {
		float horas_desenvolvedor_mes = getHoras(desenvolvedor_id, mes);
		float horas_geral_mes = HorasJogadasDAO.getInstance().getHorasJogadasMesTotal(mes);
		float despesas = 0 ;
		float faturado_geral_mes = PagamentoAssinaturaDAO.getInstance().getFaturamentoMes(mes) - despesas;
		
		if(faturado_geral_mes<0) {
			faturado_geral_mes = 0;
		}
		
		
		float faturado =  (faturado_geral_mes  * horas_desenvolvedor_mes)/ horas_geral_mes; 
		if(Float.isNaN(faturado)) {
			faturado = 0;
		}
		Debug.logDetalhe("faturado: ("+faturado_geral_mes+"*"+horas_desenvolvedor_mes+")/ "+ horas_geral_mes+" = "+faturado);
		return faturado;
						  					
	}
	
	public float getPagamento(int desenvolvedor_id,Date mes) throws SQLException, CustomException {
		return PagamentoDesenvolvedorDAO.getInstance().getValorPagoMes(desenvolvedor_id, mes);
	}
	
	private void ProcessarPagamento(int desenvolvedor_id) throws SQLException, CustomException {
		Debug.logDetalhe("Processando pagamento de: "+desenvolvedor_id);
		Date data_inicial = Date.valueOf("2021-01-01");//inicio da contabilização de pagamentos
		LocalDate data_perc = data_inicial.toLocalDate();
		LocalDate data_atual = java.time.LocalDate.now();
		List<PayoutItem> payouts = new ArrayList<PayoutItem>();
		List<PagamentoDesenvolvedorModelo> pagamentos_nao_confirmados = new ArrayList<>();
		
		data_atual = Date.valueOf(data_atual.getYear()+"-"+data_atual.getMonthValue()+"-01").toLocalDate();
		while(data_perc.isBefore(data_atual)) {
			if(!PagamentoDesenvolvedorDAO.getInstance().seRecebeuMes(desenvolvedor_id, Date.valueOf(data_perc))) {
				float valor = getFaturado(desenvolvedor_id, Date.valueOf(data_perc));
				if(Config.softwareEstado==SoftwareEstado.desenvolvimento) {
					PagamentoDesenvolvedorDAO.getInstance().inserirPagamento(valor, Date.valueOf(data_perc), desenvolvedor_id);
					ConfirmarPagamento(desenvolvedor_id, Date.valueOf(data_perc),0); //se a confirmação foi feita em desenvolvimento o batch_id = 0
					Debug.logDetalhe("Pagando mês: "+data_perc+" para o dev: "+desenvolvedor_id+" no valor de: "+valor);
				}
				if(Config.softwareEstado==SoftwareEstado.producao) {
					payouts.add(new PayoutItem(Float.toString(valor), data_perc.toString() + desenvolvedor_id, DesenvolvedorDAO.getInstance().getPaypal(desenvolvedor_id)));
					PagamentoDesenvolvedorDAO.getInstance().inserirPagamento(valor, Date.valueOf(data_perc), desenvolvedor_id);
					PagamentoDesenvolvedorModelo pagamento = new PagamentoDesenvolvedorModelo();
					pagamento.setData(Date.valueOf(data_perc));
					pagamento.setValor(valor);
					pagamento.setDesenvolvedor_id(desenvolvedor_id);
					pagamentos_nao_confirmados.add(pagamento);
					Debug.logDetalhe("Pagando mês: "+data_perc+" para o dev: "+desenvolvedor_id+" no valor de: "+valor);
				}

				
			}
			data_perc = data_perc.plusMonths(1);		
		}
		
		if(Config.softwareEstado==SoftwareEstado.producao) {
			int batch_id = PagamentoDesenvolvedorDAO.getInstance().getNewBatchID();
			if(ApiPayouts.getInstance().enviarPagamentos(payouts, batch_id)) {
				for (PagamentoDesenvolvedorModelo pagamento : pagamentos_nao_confirmados) {
					ConfirmarPagamento(pagamento.getDesenvolvedor_id(), pagamento.getMes_referente(),batch_id);
				}
			}
			
		}
	}
	
	@Scheduled(cron = "0 0 12 1 * ?")
	public void ProcessarPagamentos() throws SQLException, CustomException {
		Debug.logDetalhe("Processando pagamentos");
		List<Integer> devs = DesenvolvedorDAO.getInstance().getListIds();
		
		for (Integer dev : devs) {
			ProcessarPagamento(dev);
		}
	}
	
	public void ConfirmarPagamento(int desenvolvedor_id,Date mes,int batch_id) throws SQLException { //chamado quando há retorno da api confirmando o pagamento, então é escrito na data de pagamento a data de hoje
		LocalDate data_atual = java.time.LocalDate.now();
		
		PagamentoDesenvolvedorDAO.getInstance().inserirDataDePagamento(desenvolvedor_id, mes, Date.valueOf(data_atual));
		PagamentoDesenvolvedorDAO.getInstance().inserirBatchId(desenvolvedor_id, mes,batch_id);
	}
}
