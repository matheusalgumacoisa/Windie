package modelo;

public class DesenvolvedorModelo {
	
	Integer desenvolvedor_id;
	String nome_de_desenvolvedor;
	//Integer agencia_bancaria;
	//Integer conta_bancaria;
	String email_paypal;
	Integer usuario_id;
	
	public DesenvolvedorModelo(String nome_desenvolvedor, String email_paypal, int id_usuario) {
		this.nome_de_desenvolvedor = nome_desenvolvedor;
		this.email_paypal = email_paypal;
		this.usuario_id = id_usuario;
	}
	
	public DesenvolvedorModelo(int desenvolvedor_id,String nome_desenvolvedor, String email_paypal, int id_usuario) {
		this.nome_de_desenvolvedor = nome_desenvolvedor;
		this.email_paypal = email_paypal;
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
	
	public Integer getUsuario_id() {
		return usuario_id;
	}
	public void setUsuario_id(Integer usuario_id) {
		this.usuario_id = usuario_id;
	}

	public String getEmail_paypal() {
		return email_paypal;
	}

	public void setEmail_paypal(String email_paypal) {
		this.email_paypal = email_paypal;
	}

}
