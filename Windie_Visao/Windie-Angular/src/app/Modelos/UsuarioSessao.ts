export class UsuarioSessao{
    constructor( 
        public usuario_id : number,
        public email: string,
        public apelido: string,
        public nome_desenvolvedor: string,
        public desenvolvedor_id: number,
        public se_assinante: boolean
    ){

    }

}