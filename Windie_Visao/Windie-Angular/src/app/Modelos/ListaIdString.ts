export class ListaIdString {
    constructor(
        public id: number,
        public valor: string,
    ) { }

    public static getValue(id:number,lista:ListaIdString[]):string{
        let retorno : string =''; 
        lista.forEach(element=>{
            if(element.id = id){
                retorno = element.valor;
            }
        });

        return retorno;
    }
  }