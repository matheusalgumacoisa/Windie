package com.tc.windie.controle;

import java.sql.SQLException;

import dao.DesenvolvedorDAO;
import dao.UsuarioDAO;
import dao.VW_usuarioAssinaturaDAO;
import modelo.DesenvolvedorModelo;
import modelo.UsuarioDesenvolvedorModelo;
import modelo.UsuarioModelo;
import util.CustomException;

public class ManterUsuarios {

	private static ManterUsuarios instance;
	
	public static ManterUsuarios getInstance() {
		
		if(instance ==null) {
			
			instance = new ManterUsuarios();
			return instance;
			
		}else {
			return instance;
		}
	}
	
	public void mudarSenha(String senha_antiga,String senha,String email) throws SQLException, CustomException {
		if(autenticaUsuario(email, senha_antiga)) {
			String nova_senha = String.valueOf(senha.hashCode());
			UsuarioDAO.getInstance().atualizarSenha(email, nova_senha);
		}else {
			throw new CustomException("Senha antiga incorreta");
		}
	}
	
	
	public void cadastrarUsuario(String email,String senha,String apelido) throws SQLException, CustomException {
		
		validarDadosUsuarioCriacao(email, apelido);
			
		senha = String.valueOf(senha.hashCode());
		
		UsuarioModelo usuario = new UsuarioModelo(null,email,senha,apelido);
		
		UsuarioDAO.getInstance().inserirUsuario(usuario);
		
	}
	
	public void atualizarUsuario(String email, String apelido, String nome_desenvolvedor, String email_paypal) throws SQLException, CustomException {
		
		int usuario_id = GetIdByEmail(email);
		if(seEmailDesenvolvedor(email)) {
			int desenvolvedor_id = devByUser(usuario_id).getDesenvolvedor_id();
			if(UsuarioDAO.getInstance().seNomeDesenvolvedorExisteParaOutroDesenvolvedor(nome_desenvolvedor, desenvolvedor_id)) {
				throw new CustomException("O nome de desenvolvedor informado já existe");
			}
			DesenvolvedorDAO.getInstance().atualizarDesenvolvedor(nome_desenvolvedor, email_paypal,UsuarioDAO.getInstance().idByEmail(email));
		}
		if(UsuarioDAO.getInstance().seApelidoExisteParaOutroUsuario(apelido, usuario_id)) {
			throw new CustomException("O apelido informado já existe");
		}
		UsuarioDAO.getInstance().atualizarUsuario(email, apelido);
	}
	
	public void cadastrarDadosDesenvolvedor(String nome_desenvolvedor, String email_paypal, String userMail) throws SQLException, CustomException {
		
		validarDadosDesenvolvedor(nome_desenvolvedor);
		
		DesenvolvedorModelo desenvolvedor = new DesenvolvedorModelo(nome_desenvolvedor,email_paypal,UsuarioDAO.getInstance().idByEmail(userMail));
		
		DesenvolvedorDAO.getInstance().inserirDesenvolvedor(desenvolvedor);
	}
	

	
	public String getApelidoByEmail(String email) throws SQLException {

		String apelido = UsuarioDAO.getInstance().getByEmail(email).getApelido();
		return apelido;
	}
	
	public int GetIdByEmail(String email) throws SQLException {

		int id = UsuarioDAO.getInstance().idByEmail(email);
		return id;
	}
	

	public boolean seEmailDesenvolvedor(String email) throws SQLException {
		
		return DesenvolvedorDAO.getInstance().seUsuarioDesenvolvedor(UsuarioDAO.getInstance().idByEmail(email));

	}
	



	public boolean seEmailAssinantes(String userMail) throws SQLException {
		 
		return VW_usuarioAssinaturaDAO.getInstance().seAssinante(UsuarioDAO.getInstance().idByEmail(userMail));

	}

	public UsuarioDesenvolvedorModelo getUsuarioForm(String userMail) throws SQLException {
		UsuarioDesenvolvedorModelo modelo = new UsuarioDesenvolvedorModelo();
		
		modelo.setApelido(getApelidoByEmail(userMail));
		
		if(seEmailDesenvolvedor(userMail)) {
			DesenvolvedorModelo desenvolvedor = DesenvolvedorDAO.getInstance().getByUser(UsuarioDAO.getInstance().idByEmail(userMail));
			
			//modelo.setAgencia(desenvolvedor.getAgencia_bancaria().toString());
			//modelo.setConta(desenvolvedor.getConta_bancaria().toString());
			modelo.setEmail_paypal(desenvolvedor.getEmail_paypal());
			modelo.setNome_desenvolvedor(desenvolvedor.getNome_de_desenvolvedor());
		}else {
			
			//modelo.setAgencia("");
			//modelo.setConta("");
			modelo.setEmail_paypal("");
			modelo.setNome_desenvolvedor("");
			
		}
		
		return modelo;
	}



	public DesenvolvedorModelo devByUser(int user_id) throws SQLException {

		return DesenvolvedorDAO.getInstance().getByUser(user_id);
	}
	
	
	public boolean autenticaUsuario(String email,String senha) throws SQLException, CustomException { // retorna true se os dados de login informados estão corretos
		
		if(!UsuarioDAO.getInstance().seEmailExiste(email)) {
			throw new CustomException("Usuário não cadastrado");
		}
		UsuarioModelo usuario = UsuarioDAO.getInstance().getByEmail(email);
		if(usuario != null && usuario.getSenha().equals(String.valueOf(senha.hashCode()))) {
			return true;
		}else {
			throw new CustomException("Senha incorreta");
		}
	}
	
	public void validarDadosUsuarioCriacao(String email, String apelido) throws SQLException, CustomException{
		
		if(UsuarioDAO.getInstance().seEmailExiste(email)) {
			throw new CustomException("O e-mail informado ja está cadastrado");
		}
		if(UsuarioDAO.getInstance().seApelidoExiste(apelido)) {
			throw new CustomException("O apelido informado já existe");
		}
	}
	
	public void validarDadosDesenvolvedor(String nome_desenvolvedor) throws SQLException, CustomException {
		if(DesenvolvedorDAO.getInstance().seNomeExiste(nome_desenvolvedor)) {
			throw new CustomException("O nome de desenvolvedor já existe");
		}
	}

}
