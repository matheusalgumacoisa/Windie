import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AprovaListaComponent } from '../Telas/Aprovar-Jogos/aprova-lista/aprova-lista.component';
import { AprovarDetalhesComponent } from '../Telas/Aprovar-Jogos/aprovar-detalhes/aprovar-detalhes.component';
import { ListarBibliotecaComponent } from '../Telas/Biblioteca/listar-biblioteca/listar-biblioteca.component';
import { DetalhesComponent } from '../Telas/Exibir-Catalogo/catalogo-detalhes/detalhes.component';
import { CatalogoListaComponent } from '../Telas/Exibir-Catalogo/catalogo-lista/catalogo-lista.component';
import { CadastrarEditarComponent } from '../Telas/Manter-Jogos/cadastrar-editar/cadastrar-editar.component';
import { PainelComponent } from '../Telas/Manter-Jogos/painel/painel.component';
import { AutenticarUsuarioComponent } from '../Telas/Manter-Usuarios/autenticar-usuario/autenticar-usuario.component';
import { CadastrarDesenvolvedorComponent } from '../Telas/Manter-Usuarios/cadastrar-desenvolvedor/cadastrar-desenvolvedor.component';
import { CadastrarUsuarioComponent } from '../Telas/Manter-Usuarios/cadastrar-usuario/cadastrar-usuario.component';
import { EditarUsuarioComponent } from '../Telas/Manter-Usuarios/editar-usuario/editar-usuario.component';
import { AuthGuardService } from './auth-guard.service';


const routes: Routes = [
  {path:'usuario/cadastrar', component: CadastrarUsuarioComponent, canActivate :[AuthGuardService]},
  {path:'login', component: AutenticarUsuarioComponent, canActivate :[AuthGuardService]},
  {path:'usuario/editar', component: EditarUsuarioComponent, canActivate :[AuthGuardService]},
  {path:'usuario/cadastrar-desenvolvedor', component: CadastrarDesenvolvedorComponent, canActivate :[AuthGuardService]},
  {path:'jogos/publicar', component: CadastrarEditarComponent, canActivate :[AuthGuardService]},
  {path:'painel', component: PainelComponent, canActivate :[AuthGuardService]},
  {path:'detalhes', component: DetalhesComponent},
  {path:'greenLight', component: AprovaListaComponent},
  {path:'biblioteca', component: ListarBibliotecaComponent},
  {path:'greenLight/detalhes', component: AprovarDetalhesComponent},
  {path:'', component: CatalogoListaComponent}
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
