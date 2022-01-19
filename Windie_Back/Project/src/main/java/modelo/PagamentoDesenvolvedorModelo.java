package modelo;

import java.sql.Date;

public class PagamentoDesenvolvedorModelo {

	private int pagamento_desenvolvedor_id;
	private Date data;
	private float valor;
	private Date mes_referente;
	private int desenvolvedor_id;
	
	public int getPagamento_desenvolvedor_id() {
		return pagamento_desenvolvedor_id;
	}
	public void setPagamento_desenvolvedor_id(int pagamento_desenvolvedor_id) {
		this.pagamento_desenvolvedor_id = pagamento_desenvolvedor_id;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public float getValor() {
		return valor;
	}
	public void setValor(float valor) {
		this.valor = valor;
	}
	public Date getMes_referente() {
		return mes_referente;
	}
	public void setMes_referente(Date mes_referente) {
		this.mes_referente = mes_referente;
	}
	public int getDesenvolvedor_id() {
		return desenvolvedor_id;
	}
	public void setDesenvolvedor_id(int desenvolvedor_id) {
		this.desenvolvedor_id = desenvolvedor_id;
	}
}
