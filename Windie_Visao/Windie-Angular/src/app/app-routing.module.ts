import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuardService } from './auth-guard.service';
import { DetalhesComponent } from './Exibir-Catalogo/catalogo-detalhes/detalhes/detalhes.component';
import { CatalogoListaComponent } from './Exibir-Catalogo/catalogo-lista/catalogo-lista.component';
import { CadastrarComponent } from './Manter-Jogos/cadastrar/cadastrar.component';
import { PainelComponent } from './Manter-Jogos/painel/painel.component';
import { AutenticarUsuarioComponent } from './Manter-Usuarios/autenticar-usuario/autenticar-usuario.component';
import { CadastrarDesenvolvedorComponent } from './Manter-Usuarios/cadastrar-desenvolvedor/cadastrar-desenvolvedor.component';
import { CadastrarUsuarioComponent } from './Manter-Usuarios/cadastrar-usuario/cadastrar-usuario.component';
import { EditarUsuarioComponent } from './Manter-Usuarios/editar-usuario/editar-usuario.component';

const routes: Routes = [
  {path:'usuario/cadastrar', component: CadastrarUsuarioComponent, canActivate :[AuthGuardService]},
  {path:'login', component: AutenticarUsuarioComponent, canActivate :[AuthGuardService]},
  {path:'usuario/editar', component: EditarUsuarioComponent, canActivate :[AuthGuardService]},
  {path:'usuario/cadastrar-desenvolvedor', component: CadastrarDesenvolvedorComponent, canActivate :[AuthGuardService]},
  {path:'jogos/publicar', component: CadastrarComponent, canActivate :[AuthGuardService]},
  {path:'painel', component: PainelComponent, canActivate :[AuthGuardService]},
  {path:'detalhes', component: DetalhesComponent},
  {path:'', component: CatalogoListaComponent}
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
