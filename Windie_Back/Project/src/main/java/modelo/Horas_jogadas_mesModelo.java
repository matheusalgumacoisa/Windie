package modelo;

import java.util.Date;

public class Horas_jogadas_mesModelo {
	
	public float horas;
	public Date mes_referente;
	public int jogo_id;
	public int horas_jogadas_mes_id;
	
	public Horas_jogadas_mesModelo(float horas, Date mes_referente, int jogo_id, int horas_jogadas_mes_id) {
		super();
		this.horas = horas;
		this.mes_referente = mes_referente;
		this.jogo_id = jogo_id;
		this.horas_jogadas_mes_id = horas_jogadas_mes_id;
	}
	
	public float getHoras() {
		return horas;
	}
	public void setHoras(float horas) {
		this.horas = horas;
	}
	public Date getMes_referente() {
		return mes_referente;
	}
	public void setMes_referente(Date mes_referente) {
		this.mes_referente = mes_referente;
	}
	public int getJogo_id() {
		return jogo_id;
	}
	public void setJogo_id(int jogo_id) {
		this.jogo_id = jogo_id;
	}
	public int getHoras_jogadas_mes_id() {
		return horas_jogadas_mes_id;
	}
	public void setHoras_jogadas_mes_id(int horas_jogadas_mes_id) {
		this.horas_jogadas_mes_id = horas_jogadas_mes_id;
	}
	
	

}
