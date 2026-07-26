import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { RouterLink } from '@angular/router';
import { TurnoService, MedicoService, PacienteService } from '../../../../shared/services/domain.service';
import { Turno, Medico, Paciente } from '../../../../shared/models/api.models';

@Component({
  selector: 'app-turno-list',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatPaginatorModule, MatButtonModule, MatIconModule, MatSelectModule, MatFormFieldModule, RouterLink],
  template: `
    <div class="container">
      <div class="header">
        <h2>Gestión de Turnos</h2>
        <a mat-raised-button color="primary" routerLink="nuevo">
          <mat-icon>add</mat-icon> Nuevo Turno
        </a>
      </div>

      <div class="filters">
        <mat-form-field appearance="outline">
          <mat-label>Estado</mat-label>
          <mat-select [value]="estadoFilter()" (selectionChange)="onEstadoChange($event)">
            <mat-option value="">Todos</mat-option>
            <mat-option value="PROGRAMADO">Programado</mat-option>
            <mat-option value="CONFIRMADO">Confirmado</mat-option>
            <mat-option value="EN_ATENCION">En Atención</mat-option>
            <mat-option value="COMPLETADO">Completado</mat-option>
            <mat-option value="CANCELADO">Cancelado</mat-option>
          </mat-select>
        </mat-form-field>
      </div>

      <table mat-table [dataSource]="turnos()">
        <ng-container matColumnDef="fecha">
          <th mat-header-cell *matHeaderCellDef>Fecha/Hora</th>
          <td mat-cell *matCellDef="let t">{{ formatDate(t.fechaHora) }}</td>
        </ng-container>

        <ng-container matColumnDef="paciente">
          <th mat-header-cell *matHeaderCellDef>Paciente</th>
          <td mat-cell *matCellDef="let t">{{ t.paciente.nombre }} {{ t.paciente.apellido }}</td>
        </ng-container>

        <ng-container matColumnDef="medico">
          <th mat-header-cell *matHeaderCellDef>Médico</th>
          <td mat-cell *matCellDef="let t">{{ t.medico.nombre }} {{ t.medico.apellido }}</td>
        </ng-container>

        <ng-container matColumnDef="especialidad">
          <th mat-header-cell *matHeaderCellDef>Especialidad</th>
          <td mat-cell *matCellDef="let t">{{ t.medico.especialidad }}</td>
        </ng-container>

        <ng-container matColumnDef="motivo">
          <th mat-header-cell *matHeaderCellDef>Motivo</th>
          <td mat-cell *matCellDef="let t">{{ t.motivo || '-' }}</td>
        </ng-container>

        <ng-container matColumnDef="estado">
          <th mat-header-cell *matHeaderCellDef>Estado</th>
          <td mat-cell *matCellDef="let t">
            <span [class]="'badge badge-' + t.estado.toLowerCase()">{{ t.estado }}</span>
          </td>
        </ng-container>

        <ng-container matColumnDef="acciones">
          <th mat-header-cell *matHeaderCellDef>Acciones</th>
          <td mat-cell *matCellDef="let t">
            @if (t.estado === 'PROGRAMADO' || t.estado === 'CONFIRMADO') {
              <button mat-icon-button color="warn" (click)="onCancel(t.id)"><mat-icon>cancel</mat-icon></button>
            }
          </td>
        </ng-container>

        <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
        <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
      </table>

      <mat-paginator
        [length]="totalElements()"
        [pageSize]="pageSize()"
        [pageSizeOptions]="[5, 10, 25]"
        (page)="onPage($event)"
        showFirstLastButtons>
      </mat-paginator>
    </div>
  `,
  styles: [`
    .container { padding: 24px; }
    .header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; }
    .filters { display: flex; gap: 16px; margin-bottom: 16px; }
    .filters mat-form-field { width: 200px; }
    table { width: 100%; }
    .badge { padding: 4px 8px; border-radius: 4px; font-size: 12px; font-weight: 500; }
    .badge-programado { background: #bbdefb; color: #1565c0; }
    .badge-confirmado { background: #c8e6c9; color: #2e7d32; }
    .badge-en_atencion { background: #fff9c4; color: #f57f17; }
    .badge-completado { background: #e0e0e0; color: #424242; }
    .badge-cancelado { background: #ffcdd2; color: #c62828; }
  `]
})
export class TurnoListComponent implements OnInit {
  private turnoService = inject(TurnoService);

  turnos = signal<Turno[]>([]);
  totalElements = signal(0);
  pageSize = signal(10);
  currentPage = signal(0);
  estadoFilter = signal('');

  displayedColumns = ['fecha', 'paciente', 'medico', 'especialidad', 'motivo', 'estado', 'acciones'];

  ngOnInit() {
    this.loadTurnos();
  }

  loadTurnos() {
    this.turnoService.findAll({
      estado: this.estadoFilter() || undefined,
      page: this.currentPage(),
      size: this.pageSize()
    }).subscribe(data => {
      this.turnos.set(data.content);
      this.totalElements.set(data.totalElements);
    });
  }

  formatDate(dateStr: string): string {
    const date = new Date(dateStr);
    return date.toLocaleDateString('es-AR', { day: '2-digit', month: '2-digit', year: 'numeric' }) + ' ' +
           date.toLocaleTimeString('es-AR', { hour: '2-digit', minute: '2-digit' });
  }

  onEstadoChange(event: any) {
    this.estadoFilter.set(event.value);
    this.currentPage.set(0);
    this.loadTurnos();
  }

  onPage(event: PageEvent) {
    this.currentPage.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.loadTurnos();
  }

  onCancel(id: number) {
    if (confirm('¿Está seguro de cancelar este turno?')) {
      this.turnoService.cancel(id).subscribe(() => this.loadTurnos());
    }
  }
}
