import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CatalogoListaComponent } from './Exibir-Catalogo/catalogo-lista/catalogo-lista.component';
import { CadastrarUsuarioComponent } from './Manter-Usuarios/cadastrar-usuario/cadastrar-usuario.component';
import { AutenticarUsuarioComponent } from './Manter-Usuarios/autenticar-usuario/autenticar-usuario.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { EditarUsuarioComponent } from './Manter-Usuarios/editar-usuario/editar-usuario.component';
import { CadastrarDesenvolvedorComponent } from './Manter-Usuarios/cadastrar-desenvolvedor/cadastrar-desenvolvedor.component';
import { AuthGuardService } from './auth-guard.service';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { TooltipModule } from 'ngx-bootstrap/tooltip';
import { ModalModule } from 'ngx-bootstrap/modal';
import { CadastrarComponent } from './Manter-Jogos/cadastrar/cadastrar.component';
import { JwPaginationModule } from 'jw-angular-pagination';
import { TimesPipePipe } from './Exibir-Catalogo/times-pipe.pipe';
import { DetalhesComponent } from './Exibir-Catalogo/catalogo-detalhes/detalhes/detalhes.component';
import { PainelComponent } from './Manter-Jogos/painel/painel.component';

@NgModule({
  declarations: [
    AppComponent,
    CatalogoListaComponent,
    CadastrarUsuarioComponent,
    AutenticarUsuarioComponent,
    EditarUsuarioComponent,
    CadastrarDesenvolvedorComponent,
    CadastrarComponent,
    TimesPipePipe,
    DetalhesComponent,
    PainelComponent
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
