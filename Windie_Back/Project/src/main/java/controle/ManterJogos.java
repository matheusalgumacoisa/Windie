package controle;

import java.sql.SQLException;
import java.util.List;

import javax.security.sasl.AuthenticationException;

import dao.DesenvolvedorDAO;
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
	
	public void inserirJogo(JogoModelo jogo,List<byte[]> screenshots) throws CustomException, SQLException {
		
		if(jogo.getVisibilidade() == null || (!jogo.getVisibilidade().equals("PUBLICO")&&!jogo.getVisibilidade().equals("RASCUNHO"))) {
			throw new CustomException("Selecione como deseja salvar o jogo");
		}
		if(jogo.getTitulo()==null || jogo.getDescricao()==null ||jogo.getCaminho_executavel() == null || jogo.getDetalhes() == null || jogo.getTags() == null || jogo.getImagem_capa() ==null)
		{
			if(jogo.getImagem_capa() ==null) throw new CustomException("Faça upload de uma imagem no formato .pn ou .jpeg");
			throw new CustomException("Nem todos os campos foram preenchidos");
		}
		
		validarTamanhoDosCampos(jogo);
		
		if(JogoDAO.getInstance().seTituloExiste(jogo.getTitulo())) {
			throw new CustomException("O título inserido já existe");
		}
		
		jogo.setJogo_id(JogoDAO.getInstance().inserir(jogo)); //insere o jogo e pega o id gerado pela sequence do banco
		
		for (byte[] img_bytes : screenshots) {
			ScreenshotDAO.getInstance().inserirScreenshot(img_bytes, jogo.getJogo_id()); //insere uma screenshot
		}

	}
	
	public void atualizarJogo(JogoModelo jogo, List<byte[]> screenshots) throws Exception {
		
		if(jogo.getVisibilidade() == null || (!jogo.getVisibilidade().equals("PUBLICO")&&!jogo.getVisibilidade().equals("RASCUNHO"))) {
			throw new Exception();
		}
		JogoDAO.getInstance().atualizar(jogo); //atualiza o jogo e pega o id gerado pela sequence do banco
		ScreenshotDAO.getInstance().limparJogoScreenshots(jogo.getJogo_id()); // limpa as screenshots desse jogo
		for (byte[] img_bytes : screenshots) {
			ScreenshotDAO.getInstance().inserirScreenshot(img_bytes, jogo.getJogo_id()); //insere uma screenshot
		}	
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

}
