import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { CatalogoListaComponent } from './Telas/Exibir-Catalogo/catalogo-lista/catalogo-lista.component';
import { CadastrarUsuarioComponent } from './Telas/Manter-Usuarios/cadastrar-usuario/cadastrar-usuario.component';
import { AutenticarUsuarioComponent } from './Telas/Manter-Usuarios/autenticar-usuario/autenticar-usuario.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { EditarUsuarioComponent } from './Telas/Manter-Usuarios/editar-usuario/editar-usuario.component';
import { CadastrarDesenvolvedorComponent } from './Telas/Manter-Usuarios/cadastrar-desenvolvedor/cadastrar-desenvolvedor.component';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { TooltipModule } from 'ngx-bootstrap/tooltip';
import { ModalModule } from 'ngx-bootstrap/modal';
import { CadastrarEditarComponent } from './Telas/Manter-Jogos/cadastrar-editar/cadastrar-editar.component';
import { JwPaginationModule } from 'jw-angular-pagination';
import { TimesPipePipe } from './Telas/Exibir-Catalogo/times-pipe.pipe';
import { PainelComponent } from './Telas/Manter-Jogos/painel/painel.component';
import { DetalhesComponent } from './Telas/Exibir-Catalogo/catalogo-detalhes/detalhes.component';
import { AuthGuardService } from './Logica/auth-guard.service';
import { AppRoutingModule } from './Logica/app-routing.module';
import { AprovaListaComponent } from './Telas/Aprovar-Jogos/aprova-lista/aprova-lista.component';
import { AprovarDetalhesComponent } from './Telas/Aprovar-Jogos/aprovar-detalhes/aprovar-detalhes.component';
import { ListarBibliotecaComponent } from './Telas/Biblioteca/listar-biblioteca/listar-biblioteca.component';

@NgModule({
  declarations: [
    AppComponent,
    CatalogoListaComponent,
    CadastrarUsuarioComponent,
    AutenticarUsuarioComponent,
    EditarUsuarioComponent,
    CadastrarDesenvolvedorComponent,
    CadastrarEditarComponent,
    TimesPipePipe,
    DetalhesComponent,
    PainelComponent,
    AprovaListaComponent,
    AprovarDetalhesComponent,
    ListarBibliotecaComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    FormsModule,
    CommonModule,
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    BsDropdownModule.forRoot(),
    TooltipModule.forRoot(),
    ModalModule.forRoot(),
    JwPaginationModule
  ],
  providers: [
    AuthGuardService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
