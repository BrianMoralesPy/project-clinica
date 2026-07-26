import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { RouterLink } from '@angular/router';
import { MedicoService, EspecialidadService } from '../../../../shared/services/domain.service';
import { Medico, Especialidad } from '../../../../shared/models/api.models';

@Component({
  selector: 'app-medico-list',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatPaginatorModule, MatButtonModule, MatIconModule, MatSelectModule, MatFormFieldModule, RouterLink],
  template: `
    <div class="container">
      <div class="header">
        <h2>Gestión de Médicos</h2>
        <a mat-raised-button color="primary" routerLink="nuevo">
          <mat-icon>add</mat-icon> Nuevo Médico
        </a>
      </div>

      <div class="filters">
        <mat-form-field appearance="outline">
          <mat-label>Especialidad</mat-label>
          <mat-select [value]="especialidadFilter()" (selectionChange)="onEspecialidadChange($event)">
            <mat-option value="">Todas</mat-option>
            @for (esp of especialidades(); track esp.id) {
              <mat-option [value]="esp.id">{{ esp.nombre }}</mat-option>
            }
          </mat-select>
        </mat-form-field>
      </div>

      <table mat-table [dataSource]="medicos()">
        <ng-container matColumnDef="nombre">
          <th mat-header-cell *matHeaderCellDef>Nombre</th>
          <td mat-cell *matCellDef="let m">{{ m.nombre }} {{ m.apellido }}</td>
        </ng-container>

        <ng-container matColumnDef="dni">
          <th mat-header-cell *matHeaderCellDef>DNI</th>
          <td mat-cell *matCellDef="let m">{{ m.dni }}</td>
        </ng-container>

        <ng-container matColumnDef="matricula">
          <th mat-header-cell *matHeaderCellDef>Matrícula</th>
          <td mat-cell *matCellDef="let m">{{ m.matricula }}</td>
        </ng-container>

        <ng-container matColumnDef="especialidad">
          <th mat-header-cell *matHeaderCellDef>Especialidad</th>
          <td mat-cell *matCellDef="let m">{{ m.especialidad.nombre }}</td>
        </ng-container>

        <ng-container matColumnDef="telefono">
          <th mat-header-cell *matHeaderCellDef>Teléfono</th>
          <td mat-cell *matCellDef="let m">{{ m.telefono || '-' }}</td>
        </ng-container>

        <ng-container matColumnDef="estado">
          <th mat-header-cell *matHeaderCellDef>Estado</th>
          <td mat-cell *matCellDef="let m">
            <span [class]="'badge badge-' + m.estado.toLowerCase()">{{ m.estado }}</span>
          </td>
        </ng-container>

        <ng-container matColumnDef="acciones">
          <th mat-header-cell *matHeaderCellDef>Acciones</th>
          <td mat-cell *matCellDef="let m">
            <button mat-icon-button [routerLink]="[m.id]"><mat-icon>edit</mat-icon></button>
            <button mat-icon-button color="warn" (click)="onDelete(m.id)"><mat-icon>delete</mat-icon></button>
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
    .filters mat-form-field { width: 300px; }
    table { width: 100%; }
    .badge { padding: 4px 8px; border-radius: 4px; font-size: 12px; font-weight: 500; }
    .badge-activo { background: #c8e6c9; color: #2e7d32; }
    .badge-inactivo { background: #ffcdd2; color: #c62828; }
  `]
})
export class MedicoListComponent implements OnInit {
  private medicoService = inject(MedicoService);
  private especialidadService = inject(EspecialidadService);

  medicos = signal<Medico[]>([]);
  especialidades = signal<Especialidad[]>([]);
  totalElements = signal(0);
  pageSize = signal(10);
  currentPage = signal(0);
  especialidadFilter = signal<number | ''>('');

  displayedColumns = ['nombre', 'dni', 'matricula', 'especialidad', 'telefono', 'estado', 'acciones'];

  ngOnInit() {
    this.especialidadService.findAll().subscribe(data => this.especialidades.set(data));
    this.loadMedicos();
  }

  loadMedicos() {
    this.medicoService.findAll({
      especialidadId: this.especialidadFilter() || undefined,
      page: this.currentPage(),
      size: this.pageSize()
    }).subscribe(data => {
      this.medicos.set(data.content);
      this.totalElements.set(data.totalElements);
    });
  }

  onEspecialidadChange(event: any) {
    this.especialidadFilter.set(event.value);
    this.currentPage.set(0);
    this.loadMedicos();
  }

  onPage(event: PageEvent) {
    this.currentPage.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.loadMedicos();
  }

  onDelete(id: number) {
    if (confirm('¿Está seguro de eliminar este médico?')) {
      this.medicoService.delete(id).subscribe(() => this.loadMedicos());
    }
  }
}
