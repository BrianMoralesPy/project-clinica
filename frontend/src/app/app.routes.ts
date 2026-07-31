import { Routes } from '@angular/router';
import { authGuard } from './core/auth/auth.guard';
import { LayoutComponent } from './layout/layout.component';

export const routes: Routes = [
  {
    path: 'bienvenida',
    loadComponent: () => import('./features/auth/pages/bienvenida-page/bienvenida-page.component')
      .then(m => m.BienvenidaComponent)
  },
  {
    path: 'login',
    loadComponent: () => import('./features/auth/pages/login-page/login-page.component')
      .then(m => m.LoginPageComponent)
  },
  {
    path: 'register',
    loadComponent: () => import('./features/auth/pages/register-page/register-page.component')
      .then(m => m.RegisterPageComponent)
  },
  {
    path: '',
    redirectTo: 'bienvenida',
    pathMatch: 'full'
  },
  {
    path: '',
    component: LayoutComponent,
    //canActivate: [authGuard],
    children: [
      {
        path: 'dashboard',
        loadComponent: () => import('./features/dashboard/pages/dashboard.component')
          .then(m => m.DashboardComponent)
      },
      {
        path: 'pacientes',
        loadComponent: () => import('./features/pacientes/pages/paciente-list/paciente-list.component')
          .then(m => m.PacienteListComponent)
      },
      {
        path: 'pacientes/nuevo',
        loadComponent: () => import('./features/pacientes/pages/paciente-form/paciente-form.component')
          .then(m => m.PacienteFormComponent)
      },
      {
        path: 'pacientes/:id',
        loadComponent: () => import('./features/pacientes/pages/paciente-form/paciente-form.component')
          .then(m => m.PacienteFormComponent)
      },
      {
        path: 'medicos',
        loadComponent: () => import('./features/medicos/pages/medico-list/medico-list.component')
          .then(m => m.MedicoListComponent)
      },
      {
        path: 'medicos/nuevo',
        loadComponent: () => import('./features/medicos/pages/medico-form/medico-form.component')
          .then(m => m.MedicoFormComponent)
      },
      {
        path: 'medicos/:id',
        loadComponent: () => import('./features/medicos/pages/medico-form/medico-form.component')
          .then(m => m.MedicoFormComponent)
      },
      {
        path: 'especialidades',
        loadComponent: () => import('./features/especialidades/pages/especialidad-list/especialidad-list.component')
          .then(m => m.EspecialidadListComponent)
      },
      {
        path: 'turnos',
        loadComponent: () => import('./features/turnos/pages/turno-list/turno-list.component')
          .then(m => m.TurnoListComponent)
      },
      {
        path: 'turnos/nuevo',
        loadComponent: () => import('./features/turnos/pages/turno-form/turno-form.component')
          .then(m => m.TurnoFormComponent)
      },
      {
        path: 'usuarios',
        loadComponent: () => import('./features/usuarios/pages/usuario-list/usuario-list.component')
          .then(m => m.UsuarioListComponent)
      },
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      }
    ]
  },
  {
    path: '**',
    redirectTo: 'dashboard'
  }
];
