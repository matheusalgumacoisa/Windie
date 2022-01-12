package modelo;

public class VW_usuarioAssinaturaModelo {
	

	private int usuario_id;
	private String email;
	private String apelido;
	private String assinatura_status;
	
	public VW_usuarioAssinaturaModelo(int usuario_id, String email, String apelido, String assinatura_status) {
		super();
		this.usuario_id = usuario_id;
		this.email = email;
		this.apelido = apelido;
		this.assinatura_status = assinatura_status;
	}
	
	
	public int getUsuario_id() {
		return usuario_id;
	}
	public void setUsuario_id(int usuario_id) {
		this.usuario_id = usuario_id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getApelido() {
		return apelido;
	}
	public void setApelido(String apelido) {
		this.apelido = apelido;
	}
	public String getAssinatura_status() {
		return assinatura_status;
	}
	public void setAssinatura_status(String assinatura_status) {
		this.assinatura_status = assinatura_status;
	}

}
