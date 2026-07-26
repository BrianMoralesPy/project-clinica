import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { AuthService } from '../../../core/auth/auth.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [MatToolbarModule, MatButtonModule, MatIconModule, MatMenuModule],
  template: `
    <mat-toolbar color="primary" class="header">
      <span>Sistema de Gestión Clínica</span>
      <span class="spacer"></span>

      <button mat-button [matMenuTriggerFor]="userMenu">
        <mat-icon>account_circle</mat-icon>
        {{ authService.currentUser()?.nombre }}
      </button>

      <mat-menu #userMenu="matMenu">
        <button mat-menu-item (click)="logout()">
          <mat-icon>logout</mat-icon>
          <span>Cerrar Sesión</span>
        </button>
      </mat-menu>
    </mat-toolbar>
  `,
  styles: [`
    .header {
      position: fixed;
      top: 0;
      left: 250px;
      right: 0;
      z-index: 1000;
    }
    .spacer {
      flex: 1;
    }
  `]
})
export class HeaderComponent {
  constructor(
    public authService: AuthService,
    private router: Router
  ) {}

  logout(): void {
    this.authService.logout();
  }
}
