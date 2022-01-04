package modelo;

public class DesenvolvedorModelo {
	
	Integer desenvolvedor_id;
	String nome_de_desenvolvedor;
	Integer agencia_bancaria;
	Integer conta_bancaria;
	Integer usuario_id;
	
	public DesenvolvedorModelo(String nome_desenvolvedor, int agencia, int conta, int id_usuario) {
		this.nome_de_desenvolvedor = nome_desenvolvedor;
		this.agencia_bancaria = agencia;
		this.conta_bancaria = conta;
		this.usuario_id = id_usuario;
	}
	
	public DesenvolvedorModelo(int desenvolvedor_id,String nome_desenvolvedor, int agencia, int conta, int id_usuario) {
		this.nome_de_desenvolvedor = nome_desenvolvedor;
		this.agencia_bancaria = agencia;
		this.conta_bancaria = conta;
		this.usuario_id = id_usuario;
		this.desenvolvedor_id = desenvolvedor_id;
	}
	
	public Integer getDesenvolvedor_id() {
		return desenvolvedor_id;
	}
	public void setDesenvolvedor_id(Integer desenvolvedor_id) {
		this.desenvolvedor_id = desenvolvedor_id;
	}
	public String getNome_de_desenvolvedor() {
		return nome_de_desenvolvedor;
	}
	public void setNome_de_desenvolvedor(String nome_de_desenvolvedor) {
		this.nome_de_desenvolvedor = nome_de_desenvolvedor;
	}
	public Integer getAgencia_bancaria() {
		return agencia_bancaria;
	}
	public void setAgencia_bancaria(Integer agencia_bancaria) {
		this.agencia_bancaria = agencia_bancaria;
	}
	public Integer getConta_bancaria() {
		return conta_bancaria;
	}
	public void setConta_bancaria(Integer conta_bancaria) {
		this.conta_bancaria = conta_bancaria;
	}
	public Integer getUsuario_id() {
		return usuario_id;
	}
	public void setUsuario_id(Integer usuario_id) {
		this.usuario_id = usuario_id;
	}

}
