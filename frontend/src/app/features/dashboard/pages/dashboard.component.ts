import { Component } from '@angular/core';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  template: `
    <div class="dashboard">
      <h1>Dashboard</h1>
      <p>Bienvenido al Sistema de Gestión Clínica</p>
    </div>
  `,
  styles: [`
    .dashboard {
      padding: 16px;
    }
  `]
})
export class DashboardComponent {}
