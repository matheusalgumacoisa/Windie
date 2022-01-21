package dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ArquivoDAO {
	public static final String dbPath = "F:/WindieServerFiles";//caminho no disco onde os arquivos são armazenados no servidor
	private static ArquivoDAO instance;
	
	public static ArquivoDAO getInstance() {
		if(instance==null) instance = new ArquivoDAO();
		
		return instance;
	}
	
	public void criarArquivo(String nome_arquivo, byte[] arquivo) throws IOException {
	 Files.write(Paths.get(dbPath+"/"+nome_arquivo+".zip"), arquivo);
	}

}
