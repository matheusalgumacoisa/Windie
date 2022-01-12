export class RestObject{ // objeto padrão de comunicação com o back end
    public sucesso : boolean = true;
    public erro :{cod:number,mensagem:string,stackTrace:string}|undefined;
    constructor( 
        public token : string | null,
        public body: any
    ){
        if(token != null){
            localStorage.setItem('token',token); // quando o back end retorna um novo token ele é logo armazenado no navegador
        }
    }

    public static assign(serverRetorno: any){
        return new RestObject( JSON.parse(JSON.stringify(serverRetorno)).token,JSON.parse(JSON.stringify(serverRetorno)).body);
    }
}  