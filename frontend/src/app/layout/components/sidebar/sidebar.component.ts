import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, MatIconModule, MatListModule],
  template: `
    <div class="sidebar">
      <div class="sidebar-header">
        <mat-icon>local_hospital</mat-icon>
        <span>Clínica</span>
      </div>

      <mat-nav-list>
        <a mat-list-item routerLink="/dashboard" routerLinkActive="active">
          <mat-icon matListItemIcon>dashboard</mat-icon>
          <span matListItemTitle>Dashboard</span>
        </a>

        <a mat-list-item routerLink="/pacientes" routerLinkActive="active">
          <mat-icon matListItemIcon>people</mat-icon>
          <span matListItemTitle>Pacientes</span>
        </a>

        <a mat-list-item routerLink="/medicos" routerLinkActive="active">
          <mat-icon matListItemIcon>medical_services</mat-icon>
          <span matListItemTitle>Médicos</span>
        </a>

        <a mat-list-item routerLink="/especialidades" routerLinkActive="active">
          <mat-icon matListItemIcon>category</mat-icon>
          <span matListItemTitle>Especialidades</span>
        </a>

        <a mat-list-item routerLink="/turnos" routerLinkActive="active">
          <mat-icon matListItemIcon>calendar_today</mat-icon>
          <span matListItemTitle>Turnos</span>
        </a>

        <a mat-list-item routerLink="/usuarios" routerLinkActive="active">
          <mat-icon matListItemIcon>admin_panel_settings</mat-icon>
          <span matListItemTitle>Usuarios</span>
        </a>
      </mat-nav-list>
    </div>
  `,
  styles: [`
    .sidebar {
      width: 250px;
      height: 100vh;
      background-color: #3f51b5;
      color: white;
    }
    .sidebar-header {
      display: flex;
      align-items: center;
      gap: 8px;
      padding: 16px;
      font-size: 18px;
      font-weight: 500;
      border-bottom: 1px solid rgba(255,255,255,0.1);
    }
    mat-nav-list {
      padding-top: 8px;
    }
    a {
      color: rgba(255,255,255,0.8);
    }
    a:hover {
      background-color: rgba(255,255,255,0.1);
    }
    a.active {
      background-color: rgba(255,255,255,0.2);
      color: white;
    }
  `]
})
export class SidebarComponent {}
