export class Debug{
    private static  debugLevel : number = 4; //debug a ser exibido 0: nenhum debug, 1: erros e validações, 2: requests, 3: inputs e outputs em Json, 4: tudo

    private static logBrowser(mensagem : string) {
		console.log(mensagem);
	}
	
	public static log(mensagem: string,debugLevel: number) {
		if(debugLevel<=Debug.debugLevel) {
			Debug.logBrowser(mensagem);
		}
	}
	
	public static  logErro(mensagem: string) {
		
		Debug.log(mensagem, 1);
	}
	
	public static logFalhaNaValidacao(mensagem: string) {
		
		Debug.log(mensagem, 1);
	}
	
	
	public static  logRequest(mensagem: string) {
		
		Debug.log(mensagem, 2);
	}
	
	
	public static  logInput(mensagem: string) {
		
		Debug.log(mensagem, 3);
	}
	
	
	public static logOutput(mensagem: string) {
		
		Debug.log(mensagem, 3);
	}
	
	public static logDetalhe(mensagem: string) {
		
		Debug.log(mensagem, 4);
	}
}