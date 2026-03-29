import { Routes } from '@angular/router';
import { HomePage } from './pages/home-page/home-page';
import { SalasGerenciaPage } from './pages/salas-gerencia-page/salas-gerencia-page';
import { CursoPage } from './pages/curso-page/curso-page';
import { TurmaPage } from './pages/turma-page/turma-page';
import { CertificadosPage } from './pages/certificados-page/certificados-page';
import { PerfilUsuarioPage } from './pages/perfil-usuario-page/perfil-usuario-page';

export const routes: Routes = [
  { path: '', component: HomePage }, 
  {path: 'salasgerencia', component: SalasGerenciaPage},
  {path: 'curso', component: CursoPage},
  { path: 'turma', component: TurmaPage }, 
  { path: 'certificados', component: CertificadosPage }, 
  { path: 'perfil', component: PerfilUsuarioPage }, 
];