package modelo;

public class JogoModelo {
	
	 private int jogo_id ;
	 private String titulo ;
	 private String descricao ;
	 private byte[] arquivos ;
	 private String caminho_executavel ;
	 private String detalhes ;
	 private String tags ;
	 private String visibilidade ;
	 private byte[] imagem_capa ;
	 private int genero ;
	 private int desenvolvedor_id;
	 


	public JogoModelo(int jogo_id, String titulo, String descricao, String caminho_executavel,
			String detalhes, String tags, String visibilidade, byte[] imagem_capa, int genero) {
		super();
		this.jogo_id = jogo_id;
		this.titulo = titulo;
		this.descricao = descricao;
		this.caminho_executavel = caminho_executavel;
		this.detalhes = detalhes;
		this.tags = tags;
		this.visibilidade = visibilidade;
		this.imagem_capa = imagem_capa;
		this.genero = genero;
	}
	
	public JogoModelo(int jogo_id, String titulo, String descricao, String caminho_executavel,
			String detalhes, String tags, String visibilidade, byte[] imagem_capa, int genero, int desenvolvedor_id) {
		super();
		this.jogo_id = jogo_id;
		this.titulo = titulo;
		this.descricao = descricao;
		this.caminho_executavel = caminho_executavel;
		this.detalhes = detalhes;
		this.tags = tags;
		this.visibilidade = visibilidade;
		this.imagem_capa = imagem_capa;
		this.genero = genero;
		this.desenvolvedor_id = desenvolvedor_id;
	}
	
	public int getJogo_id() {
		return jogo_id;
	}
	public void setJogo_id(int jogo_id) {
		this.jogo_id = jogo_id;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public byte[] getArquivos() {
		return arquivos;
	}
	public void setArquivos(byte[] arquivos) {
		this.arquivos = arquivos;
	}
	public String getCaminho_executavel() {
		return caminho_executavel;
	}
	public void setCaminho_executavel(String caminho_executavel) {
		this.caminho_executavel = caminho_executavel;
	}
	public String getDetalhes() {
		return detalhes;
	}
	public void setDetalhes(String detalhes) {
		this.detalhes = detalhes;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getVisibilidade() {
		return visibilidade;
	}
	public void setVisibilidade(String visibilidade) {
		this.visibilidade = visibilidade;
	}
	public byte[] getImagem_capa() {
		return imagem_capa;
	}
	public void setImagem_capa(byte[] imagem_capa) {
		this.imagem_capa = imagem_capa;
	}
	public int getGenero() {
		return genero;
	}
	public void setGenero(int genero) {
		this.genero = genero;
	}

	public int getDesenvolvedor_id() {
		return desenvolvedor_id;
	}

	public void setDesenvolvedor_id(int desenvolvedor_id) {
		this.desenvolvedor_id = desenvolvedor_id;
	}
}
