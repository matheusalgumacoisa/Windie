package modelo;

public class UsuarioModelo {

	private Integer usuario_id;
	private String email;
	private String senha;
	private String apelido;
	
	
	public UsuarioModelo() {
		
	}
	
	public UsuarioModelo(Integer usuario_id, String  email, String  senha, String  apelido) {
		
		this.usuario_id = usuario_id;
		this.email = email;
		this.senha = senha;
		this.apelido = apelido;
	}
	
	public Integer getUsuario_id() {
		return usuario_id;
	}
	public void setUsuario_id(Integer usuario_id) {
		this.usuario_id = usuario_id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getApelido() {
		return apelido;
	}
	public void setApelido(String apelido) {
		this.apelido = apelido;
	}
	
}
