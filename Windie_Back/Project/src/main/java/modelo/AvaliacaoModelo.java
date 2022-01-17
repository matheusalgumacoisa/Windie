package modelo;

public class AvaliacaoModelo {

	private int avaliacao_id;
	private int nota;
	private int jogo_id;
	private int usuario_id;
	
	public AvaliacaoModelo(int avaliacao_id, int nota, int jogo_id, int usuario_id) {
		super();
		this.avaliacao_id = avaliacao_id;
		this.nota = nota;
		this.jogo_id = jogo_id;
		this.usuario_id = usuario_id;
	}
	
	public AvaliacaoModelo(int nota, int jogo_id, int usuario_id) {
		super();
		this.nota = nota;
		this.jogo_id = jogo_id;
		this.usuario_id = usuario_id;
	}
	
	public int getAvaliacao_id() {
		return avaliacao_id;
	}
	public void setAvaliacao_id(int avaliacao_id) {
		this.avaliacao_id = avaliacao_id;
	}
	public int getNota() {
		return nota;
	}
	public void setNota(int nota) {
		this.nota = nota;
	}
	public int getJogo_id() {
		return jogo_id;
	}
	public void setJogo_id(int jogo_id) {
		this.jogo_id = jogo_id;
	}
	public int getUsuario_id() {
		return usuario_id;
	}
	public void setUsuario_id(int usuario_id) {
		this.usuario_id = usuario_id;
	}

}
