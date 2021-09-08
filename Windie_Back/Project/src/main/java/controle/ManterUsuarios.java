package controle;

import java.sql.SQLException;

import modelo.DesenvolvedorDAO;
import modelo.DesenvolvedorModelo;
import modelo.UsuarioDAO;
import modelo.UsuarioDesenvolvedorModelo;
import modelo.UsuarioModelo;
import util.CustomException;

public class ManterUsuarios {

	private static ManterUsuarios instance;
	
	public ManterUsuarios() {
		
	}
	
	public void CadastrarUsuario(String email,String senha,String apelido) throws SQLException, CustomException {
		
		if(UsuarioDAO.getInstance().seEmailExiste(email)) {
			throw new CustomException("O e-mail informado ja está cadastrado");
		}
		if(UsuarioDAO.getInstance().seApelidoExiste(apelido)) {
			throw new CustomException("O apelido já existe");
		}
			
		senha = String.valueOf(senha.hashCode());
		
		UsuarioModelo usuario = new UsuarioModelo(null,email,senha,apelido);
		
		UsuarioDAO.getInstance().InserirUsuario(usuario);
		
	}
	
	public void CadastrarDesenvolvedor(String nome_desenvolvedor, String agencia, String conta, String userMail) throws SQLException, CustomException {
		
		if(DesenvolvedorDAO.getInstance().seNomeExiste(nome_desenvolvedor)) {
			throw new CustomException("O nome já existe");
		}
		
		DesenvolvedorModelo desenvolvedor = new DesenvolvedorModelo(nome_desenvolvedor,Integer.parseInt(agencia),Integer.parseInt(conta),UsuarioDAO.getInstance().idByEmail(userMail));
		
		DesenvolvedorDAO.getInstance().InserirDesenvolvedor(desenvolvedor);
	}
	
	public void AtualizarUsuario(String email, String apelido, String nome_desenvolvedor, String conta, String agencia) throws SQLException, CustomException {
		if(UsuarioDAO.getInstance().seApelidoExiste(apelido)&&!UsuarioDAO.getInstance().getUsuarioByEmail(email).getApelido().equals(apelido)) {
			throw new CustomException("O apelido já existe");
		}
		if(DesenvolvedorDAO.getInstance().seNomeExiste(nome_desenvolvedor)&&!DesenvolvedorDAO.getInstance().getByUser(UsuarioDAO.getInstance().getUsuarioByEmail(email).getUsuario_id()).getNome_de_desenvolvedor().equals(nome_desenvolvedor)) {
			throw new CustomException("O nome de desenvolvedor já existe");
		}	
		UsuarioDAO.getInstance().AtualizarUsuario(email, apelido);
		if(getPapel(email).equals("D")) {
			DesenvolvedorDAO.getInstance().atualizarDesenvolvedor(nome_desenvolvedor, conta,agencia,UsuarioDAO.getInstance().idByEmail(email));
		}
	}
	
	public boolean AutenticaUsuario(String email,String senha) throws SQLException, CustomException {
		
		if(!UsuarioDAO.getInstance().seEmailExiste(email)) {
			throw new CustomException("Usuário não cadastrado");
		}
		UsuarioModelo usuario = UsuarioDAO.getInstance().getUsuarioByEmail(email);
		if(usuario != null && usuario.getSenha().equals(String.valueOf(senha.hashCode()))) {
			return true;
		}else {
			throw new CustomException("Senha incorreta");
		}
	}
	
	public String apelidoByEmail(String email) throws SQLException {

		String apelido = UsuarioDAO.getInstance().apelidoByEmail(email);
		System.out.println(apelido);
		return apelido;
	}
	

	public String getPapel(String email) throws SQLException {
		
		if(DesenvolvedorDAO.getInstance().seUsuarioDesenvolvedor(UsuarioDAO.getInstance().idByEmail(email))) {
			
			return "D";
		}else {
			
			return "U";
		}
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

	public static ManterUsuarios getInstance() {
		
		if(instance ==null) {
			
			instance = new ManterUsuarios();
			return instance;
			
		}else {
			return instance;
		}
	}

}
