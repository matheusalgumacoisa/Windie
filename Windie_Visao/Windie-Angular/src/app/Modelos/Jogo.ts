import { DomSanitizer } from "@angular/platform-browser";

export class Jogo {

    public tags_arr: string[] = []; 
    public imagem_capa_sanitized: any;
    public genero_string!: string;
    public screenshots: any[] = [];

    constructor(
      public jogo_id: number,
      public titulo: string,
      public descricao: string,
      public caminho_executavel: string,
      public detalhes: string,
      public tags: string,
      public visibilidade: string,
      public imagem_capa: any,
      public genero: number
    ) { 
      //this.Init(null,null);
    }

    public static Init(genero_string: string | null, screenshots: any[] | null, jogo : Jogo,sanitizer: DomSanitizer){
      jogo.tags_arr = jogo.tags.split(',');
      jogo.imagem_capa_sanitized = sanitizer.bypassSecurityTrustUrl('data:image/jpeg;base64,' + jogo.imagem_capa); // configurando capa para exibição no navegador
      if(genero_string != null){
        jogo.genero_string = genero_string;
      }
      if(screenshots!=null){
        jogo.screenshots = screenshots;
      }
    }
        
  }