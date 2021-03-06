package com.tc.windie.controle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.security.sasl.AuthenticationException;

import dao.ArquivoDAO;
import dao.DesenvolvedorDAO;
import dao.FichaAprovacaoDAO;
import dao.GeneroDAO;
import dao.JogoDAO;
import dao.ScreenshotDAO;
import dao.UsuarioDAO;
import modelo.GeneroModelo;
import modelo.JogoModelo;
import util.CustomException;
import util.TokenManager;

public class ManterJogos {
	
	public static ManterJogos instance;
	
	public static ManterJogos getInstance () {
		if(instance == null) {
			instance = new ManterJogos();
		}
		
		return instance;
	}
	
	public int inserirJogo(JogoModelo jogo,List<byte[]> screenshots) throws CustomException, SQLException {
		
		if(jogo.getVisibilidade().isEmpty()  ||jogo.getVisibilidade() == null || (!jogo.getVisibilidade().equals("PUBLICO")&&!jogo.getVisibilidade().equals("RASCUNHO"))) {
			throw new CustomException("Selecione como deseja salvar o jogo");
		}
		if(jogo.getTitulo().isEmpty() || jogo.getDescricao().isEmpty() ||jogo.getCaminho_executavel().isEmpty()  || jogo.getDetalhes().isEmpty()  || jogo.getTags().isEmpty()  || jogo.getImagem_capa() == null )
		{
			if(jogo.getImagem_capa() == null) throw new CustomException("Faça upload de uma imagem no formato .pn ou .jpeg");
			throw new CustomException("Nem todos os campos foram preenchidos");
		}
		
		validarTamanhoDosCampos(jogo);
		if(jogo.getGenero() == 0) throw new CustomException("Selecione um gênero");
		
		if(JogoDAO.getInstance().seTituloExiste(jogo.getTitulo())) {
			throw new CustomException("O título inserido já existe");
		}
		
		if(jogo.getImagem_capa().length>4194304) {
			throw new CustomException("A imagem de capa não deve possuir mais que 4MB");
		}
		
		for (byte[] img_bytes : screenshots) {
			if(img_bytes.length>2097152) {
				throw new CustomException("As screenshots não devem possuir mais que 2MB cada");
			}
		}
		
		int jogo_id = JogoDAO.getInstance().inserir(jogo);
		jogo.setJogo_id(jogo_id); //insere o jogo e pega o id gerado pela sequence do banco
		
		for (byte[] img_bytes : screenshots) {
			ScreenshotDAO.getInstance().inserirScreenshot(img_bytes, jogo.getJogo_id()); //insere uma screenshot
		}
		
		if(jogo.getVisibilidade().equals("PUBLICO")) {
			FichaAprovacaoDAO.getInstance().inserir(jogo_id);
		}
		return jogo_id;

	}
	
	public void atualizarJogo(JogoModelo jogo, List<byte[]> screenshots) throws CustomException, SQLException{
		
		if(jogo.getVisibilidade().isEmpty()  ||jogo.getVisibilidade() == null || (!jogo.getVisibilidade().equals("PUBLICO")&&!jogo.getVisibilidade().equals("RASCUNHO"))) {
			throw new CustomException("Selecione como deseja salvar o jogo");
		}
		if(jogo.getJogo_id() ==0 || jogo.getTitulo().isEmpty() || jogo.getDescricao().isEmpty() ||jogo.getCaminho_executavel().isEmpty()  || jogo.getDetalhes().isEmpty()  || jogo.getTags().isEmpty()  || jogo.getImagem_capa() == null )
		{
			if(jogo.getImagem_capa() == null) throw new CustomException("Faça upload de uma imagem no formato .pn ou .jpeg");
			throw new CustomException("Nem todos os campos foram preenchidos");
		}
		
		validarTamanhoDosCampos(jogo);
		if(jogo.getGenero() == 0) throw new CustomException("Selecione um gênero");
		
		if(JogoDAO.getInstance().seTituloExisteEmOutroJogo(jogo.getTitulo(),jogo.getJogo_id())) {
			throw new CustomException("O título inserido já existe");
		}
		
		if(jogo.getImagem_capa().length>4194304) {
			throw new CustomException("A imagem de capa não deve possuir mais que 4MB");
		}
		
		for (byte[] img_bytes : screenshots) {
			if(img_bytes.length>2097152) {
				throw new CustomException("As screenshots não devem possuir mais que 2MB cada");
			}
		}
		
		JogoDAO.getInstance().atualizar(jogo); //atualiza o jogo e pega o id gerado pela sequence do banco
		ScreenshotDAO.getInstance().limparJogoScreenshots(jogo.getJogo_id()); // limpa as screenshots desse jogo
		if(!FichaAprovacaoDAO.getInstance().sePossuiFichaAprovacao(jogo.getJogo_id())) {
			FichaAprovacaoDAO.getInstance().inserir(jogo.getJogo_id());
		}
		for (byte[] img_bytes : screenshots) {
			ScreenshotDAO.getInstance().inserirScreenshot(img_bytes, jogo.getJogo_id()); //insere uma screenshot
		}	
	}
	
	public void excluirRascunho(int jogo_id,int desenvolvedor_id) throws SQLException, AuthenticationException, CustomException {
		
		if(!seDesenvolvedorDoJogo(jogo_id,desenvolvedor_id)){
			throw new AuthenticationException("Não é desenvolvedor desse jogo");
		}else
		
		if(!getJogo(jogo_id).getVisibilidade().equals("RASCUNHO")) {
			throw new CustomException("Não é possível excluir um jogo publicado");
		}else
		
		JogoDAO.getInstance().excluir(jogo_id);
	}
	
	public List<byte[]> getScreenshots(int jogo_id) throws SQLException{ //retorna uma lista de screenshots para um dado jogo
		
		return ScreenshotDAO.getInstance().getListaByJogo(jogo_id);
	}
	
	public List<JogoModelo> getJogosByDesenvolvedor(int desenvolvedor_id) throws SQLException{ //retorna uma lista de jogos para um dado desenvolvedor
		return JogoDAO.getInstance().getListaByDesenvolvedor(desenvolvedor_id);
	}
	
	public int getDevIdByToken(String token) throws SQLException, AuthenticationException {
		
		int user_id =  UsuarioDAO.getInstance().idByEmail( TokenManager.getInstance().getUser(token));
		int dev_id = DesenvolvedorDAO.getInstance().getByUser(user_id).getDesenvolvedor_id();
		
		return dev_id;
	}

	public List<GeneroModelo> getListaGeneros() throws SQLException{
		return GeneroDAO.getInstance().getLista();
	}

	public JogoModelo getJogo(int jogo_id) throws SQLException {
		
		JogoModelo jogo = JogoDAO.getInstance().getJogo(jogo_id);		
		return jogo;
	}
	
	public void validarTamanhoDosCampos(JogoModelo jogo) throws CustomException {
		if(jogo.getTitulo().length()>100) throw new CustomException("O título não deve possuir mais que 100 caracteres");
		if(jogo.getDescricao().length()>100) throw new CustomException("A descrição não deve possuir mais que 100 caracteres");
		if(jogo.getDetalhes().length()>5000) throw new CustomException("Os detalhes não devem possuir mais que 5000 caracteres");
		if(jogo.getTags().length()>200) throw new CustomException("As tags não devem possuir mais que 200 caracteres");

	}
	
	public boolean seDesenvolvedorDoJogo(int jogo_id, int desenvolvedor_id) throws SQLException {
		return JogoDAO.getInstance().seDesenvolvedorDoJogo(jogo_id,desenvolvedor_id);
	}

	public void salvarArquivos(int jogo_id, byte[] arquivo) throws SQLException, IOException {
		
		String arquivo_nome = "/arquivos_jogo_"+jogo_id+".zip";
		
		ArquivoDAO.getInstance().criarArquivo(arquivo_nome, arquivo);
		JogoDAO.getInstance().salvarCaminhoArquivo(jogo_id,ArquivoDAO.dbPath+arquivo_nome);

	}
	
	public String getFileInfo(int jogo_id) throws SQLException, IOException { //retorna um texto com as informações do arquivo
		JogoModelo jogo = ManterJogos.getInstance().getJogo(jogo_id);		String path = jogo.getArquivo_caminho();
		Long tamanho = ArquivoDAO.getInstance().getArquivoTamanhoBytes(path);
		String data_modificacao = ArquivoDAO.getInstance().getArquivoModData(path);
		if(tamanho > 0) {
			return "Arquivo atualizado pela última vez em "+data_modificacao+" com o tamanho de "+getTamanhoFormatado(tamanho);
		}else {
			return "";
		}
		
	}
	
	
	public String getTamanhoFormatado(Long tamanho_bytes) { //retorna uma string informando o tamanho do arquivo em uma escala compatível como KB ou GB
		Long tamanhoI = tamanho_bytes;

		if (tamanho_bytes>1073741824) { //GB
			tamanhoI = tamanho_bytes / 1073741824;
			return tamanhoI.toString() + "GBs";
			
		}else if(tamanho_bytes>1048576) { //MB
			tamanhoI = tamanho_bytes / 1048576;
			return tamanhoI.toString() + " MBs";
			
		}else if(tamanho_bytes>1024) { //KB
			tamanhoI = tamanho_bytes / 1024;
			return tamanhoI.toString() + " KBs";
			
		}else {
			return tamanhoI.toString() + " bytes";
		}
	}
	


}
