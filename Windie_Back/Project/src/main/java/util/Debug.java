package util;

public class Debug {
	
	public static final int debugLevel = 4; //debug a ser exibido 0: nenhum debug, 1: erros e validações, 2: requests, 3: inputs e outputs em Json, 4: tudo
	
	
	private static void log(String mensagem) {
		System.out.println(mensagem);
	}
	
	public static void log(String mensagem,int debugLevel) {
		if(debugLevel<=Debug.debugLevel) {
			Debug.log(mensagem);
		}
	}
	
	public static void logErro(String mensagem) {
		
		Debug.log(mensagem, 1);
	}
	
	public static void logFalhaNaValidacao(String mensagem) {
		
		Debug.log(mensagem, 1);
	}
	
	
	public static void logRequest(String mensagem) {
		
		Debug.log(mensagem, 2);
	}
	
	
	public static void logInput(String mensagem) {
		
		Debug.log(mensagem, 3);
	}
	
	
	public static void logOutput(String mensagem) {
		
		Debug.log(mensagem, 3);
	}
	
	public static void logDetalhe(String mensagem) {
		
		Debug.log(mensagem, 4);
	}
	
	
	


}
