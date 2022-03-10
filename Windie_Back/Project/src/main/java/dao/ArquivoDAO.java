package dao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;

import util.Debug;

public class ArquivoDAO {
	public static final String dbPath = "C:/WindieServerFiles";//caminho no disco onde os arquivos são armazenados no servidor
	private static ArquivoDAO instance;
	
	public static ArquivoDAO getInstance() {
		if(instance==null) instance = new ArquivoDAO();
		
		return instance;
	}
	
	public void criarArquivo(String nome_arquivo, byte[] arquivo) throws IOException {
		Files.write(Paths.get(dbPath+"/"+nome_arquivo), arquivo);
	}
	
	public byte[] getArquivo(String path) throws IOException {
		Debug.logDetalhe("get arquivo path:"+path);
		return Files.readAllBytes(Paths.get(path));
	}
	
	public long getArquivoTamanhoBytes(String path) throws IOException{
		Debug.logDetalhe("get arquivo tamanho, path:"+path);
		File f = new File(path);
		if(!f.exists()) return 0;
		return f.length();		
	}
	
	public String getArquivoModData(String path) throws IOException{
		Debug.logDetalhe("get arquivo data de modificação, path:"+path);
		File f = new File(path);
		if(!f.exists()) return "";
		
		 BasicFileAttributes attr;
		 SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		 // read file's attribute as a bulk operation
	      attr = Files.readAttributes(Paths.get(path), BasicFileAttributes.class);
	      return sdf.format(attr.lastModifiedTime().toMillis()); 	
	}

}
