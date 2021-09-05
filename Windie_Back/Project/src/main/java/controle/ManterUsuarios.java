package controle;

import java.sql.SQLException;

import modelo.DesenvolvedorDAO;
import modelo.DesenvolvedorModelo;
import modelo.UsuarioDAO;
import modelo.UsuarioDesenvolvedorModelo;
import modelo.UsuarioModelo;

public class ManterUsuarios {

	private static ManterUsuarios instance;
	
	public ManterUsuarios() {
		
	}
	
	public void CadastrarUsuario(String email,String senha,String apelido) throws SQLException {
		
		senha = String.valueOf(senha.hashCode());
		
		UsuarioModelo usuario = new UsuarioModelo(null,email,senha,apelido);
		
		UsuarioDAO.getInstance().InserirUsuario(usuario);
		
	}
	
	public boolean AutenticaUsuario(String email,String senha) throws SQLException {
		UsuarioModelo usuario = UsuarioDAO.getInstance().getUsuarioByEmail(email);
		if(usuario != null && usuario.getSenha().equals(String.valueOf(senha.hashCode()))) {
			return true;
		}else {
			return false;
		}
	}
	
	public String apelidoByEmail(String email) throws SQLException {

		String apelido = UsuarioDAO.getInstance().apelidoByEmail(email);
		System.out.println(apelido);
		return apelido;
	}
	
	public static ManterUsuarios getInstance() {
		
		if(instance ==null) {
			
			instance = new ManterUsuarios();
			return instance;
			
		}else {
			return instance;
		}
	}

	public String getPapel(String email) throws SQLException {
		
		if(DesenvolvedorDAO.getInstance().seUsuarioDesenvolvedor(UsuarioDAO.getInstance().idByEmail(email))) {
			
			return "D";
		}else {
			
			return "U";
		}
	}
	
	public void AtualizarUsuario(String email, String apelido, String nome_desenvolvedor, String conta, String agencia) throws SQLException {
		// TODO Auto-generated method stub
		//suarioModelo usuario = new UsuarioModelo(email);
		UsuarioDAO.getInstance().AtualizarUsuario(email, apelido);
		if(getPapel(email).equals("D")) {
			DesenvolvedorDAO.getInstance().atualizarDesenvolvedor(nome_desenvolvedor, conta,agencia,UsuarioDAO.getInstance().idByEmail(email));
		}
	}

	public void CadastrarDesenvolvedor(String nome_desenvolvedor, String agencia, String conta, String userMail) throws SQLException {
		
		DesenvolvedorModelo desenvolvedor = new DesenvolvedorModelo(nome_desenvolvedor,Integer.parseInt(agencia),Integer.parseInt(conta),UsuarioDAO.getInstance().idByEmail(userMail));
		DesenvolvedorDAO.getInstance().InserirDesenvolvedor(desenvolvedor);
	}

	public String getAssinatura(String userMail) throws SQLException {
		 
		if(UsuarioDAO.getInstance().seAssinante(UsuarioDAO.getInstance().idByEmail(userMail))){
			
			return "valid";
		}else {
			return "invalid";
		}
	}

	public UsuarioDesenvolvedorModelo getUsuarioForm(String userMail) throws SQLException {
		UsuarioDesenvolvedorModelo modelo = new UsuarioDesenvolvedorModelo();
		
		modelo.setApelido(apelidoByEmail(userMail));
		
		if(getPapel(userMail).equals("D")) {
			DesenvolvedorModelo desenvolvedor = DesenvolvedorDAO.getInstance().getByUser(UsuarioDAO.getInstance().idByEmail(userMail));
			
			modelo.setAgencia(desenvolvedor.getAgencia_bancaria().toString());
			modelo.setConta(desenvolvedor.getConta_bancaria().toString());
			modelo.setNome_desenvolvedor(desenvolvedor.getNome_de_desenvolvedor());
		}else {
			
			modelo.setAgencia("");
			modelo.setConta("");
			modelo.setNome_desenvolvedor("");
			
		}
		
		return modelo;
	}


}
