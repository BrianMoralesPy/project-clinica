import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { DashboardService } from '../../../shared/services/domain.service';
import { DashboardData } from '../../../shared/models/api.models';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatIconModule],
  template: `
    <div class="container">
      <h2>Dashboard</h2>

      <div class="stats-grid">
        <mat-card class="stat-card">
          <mat-card-content>
            <mat-icon class="stat-icon patients">people</mat-icon>
            <div class="stat-value">{{ data()?.totalPacientes || 0 }}</div>
            <div class="stat-label">Pacientes</div>
          </mat-card-content>
        </mat-card>

        <mat-card class="stat-card">
          <mat-card-content>
            <mat-icon class="stat-icon medicos">medical_services</mat-icon>
            <div class="stat-value">{{ data()?.totalMedicos || 0 }}</div>
            <div class="stat-label">Médicos</div>
          </mat-card-content>
        </mat-card>

        <mat-card class="stat-card">
          <mat-card-content>
            <mat-icon class="stat-icon especialidades">science</mat-icon>
            <div class="stat-value">{{ data()?.totalEspecialidades || 0 }}</div>
            <div class="stat-label">Especialidades</div>
          </mat-card-content>
        </mat-card>

        <mat-card class="stat-card">
          <mat-card-content>
            <mat-icon class="stat-icon turnos">event</mat-icon>
            <div class="stat-value">{{ data()?.turnosPendientes || 0 }}</div>
            <div class="stat-label">Turnos Pendientes</div>
          </mat-card-content>
        </mat-card>

        <mat-card class="stat-card">
          <mat-card-content>
            <mat-icon class="stat-icon completados">check_circle</mat-icon>
            <div class="stat-value">{{ data()?.turnosCompletados || 0 }}</div>
            <div class="stat-label">Turnos Completados</div>
          </mat-card-content>
        </mat-card>
      </div>
    </div>
  `,
  styles: [`
    .container { padding: 24px; }
    h2 { margin-bottom: 24px; color: #333; }
    .stats-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 24px; }
    .stat-card { text-align: center; padding: 24px; }
    .stat-icon { font-size: 48px; width: 48px; height: 48px; margin-bottom: 16px; }
    .stat-icon.patients { color: #3f51b5; }
    .stat-icon.medicos { color: #4caf50; }
    .stat-icon.especialidades { color: #ff9800; }
    .stat-icon.turnos { color: #2196f3; }
    .stat-icon.completados { color: #8bc34a; }
    .stat-value { font-size: 36px; font-weight: bold; color: #333; }
    .stat-label { font-size: 14px; color: #666; margin-top: 8px; }
  `]
})
export class DashboardComponent implements OnInit {
  private dashboardService = inject(DashboardService);

  data = signal<DashboardData | null>(null);

  ngOnInit() {
    this.dashboardService.getDashboard().subscribe(data => this.data.set(data));
  }
}
