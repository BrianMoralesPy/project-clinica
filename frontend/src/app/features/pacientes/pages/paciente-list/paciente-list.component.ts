import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { RouterLink } from '@angular/router';
import { PacienteService } from '../../../../shared/services/domain.service';
import { Paciente } from '../../../../shared/models/api.models';

@Component({
  selector: 'app-paciente-list',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatPaginatorModule, MatButtonModule, MatIconModule, MatInputModule, MatFormFieldModule, MatSelectModule, RouterLink],
  template: `
    <div class="container">
      <div class="header">
        <h2>Gestión de Pacientes</h2>
        <a mat-raised-button color="primary" routerLink="nuevo">
          <mat-icon>add</mat-icon> Nuevo Paciente
        </a>
      </div>

      <div class="filters">
        <mat-form-field appearance="outline">
          <mat-label>Buscar</mat-label>
          <input matInput [value]="search()" (input)="onSearch($event)" placeholder="Nombre, apellido o DNI">
          <mat-icon matPrefix>search</mat-icon>
        </mat-form-field>

        <mat-form-field appearance="outline">
          <mat-label>Estado</mat-label>
          <mat-select [value]="estadoFilter()" (selectionChange)="onEstadoChange($event)">
            <mat-option value="">Todos</mat-option>
            <mat-option value="ACTIVO">Activo</mat-option>
            <mat-option value="INACTIVO">Inactivo</mat-option>
          </mat-select>
        </mat-form-field>
      </div>

      <table mat-table [dataSource]="pacientes()">
        <ng-container matColumnDef="nombre">
          <th mat-header-cell *matHeaderCellDef>Nombre</th>
          <td mat-cell *matCellDef="let p">{{ p.nombre }} {{ p.apellido }}</td>
        </ng-container>

        <ng-container matColumnDef="dni">
          <th mat-header-cell *matHeaderCellDef>DNI</th>
          <td mat-cell *matCellDef="let p">{{ p.dni }}</td>
        </ng-container>

        <ng-container matColumnDef="sexo">
          <th mat-header-cell *matHeaderCellDef>Sexo</th>
          <td mat-cell *matCellDef="let p">{{ p.sexo }}</td>
        </ng-container>

        <ng-container matColumnDef="telefono">
          <th mat-header-cell *matHeaderCellDef>Teléfono</th>
          <td mat-cell *matCellDef="let p">{{ p.telefono || '-' }}</td>
        </ng-container>

        <ng-container matColumnDef="estado">
          <th mat-header-cell *matHeaderCellDef>Estado</th>
          <td mat-cell *matCellDef="let p">
            <span [class]="'badge badge-' + p.estado.toLowerCase()">{{ p.estado }}</span>
          </td>
        </ng-container>

        <ng-container matColumnDef="acciones">
          <th mat-header-cell *matHeaderCellDef>Acciones</th>
          <td mat-cell *matCellDef="let p">
            <button mat-icon-button [routerLink]="[p.id]"><mat-icon>edit</mat-icon></button>
            <button mat-icon-button color="warn" (click)="onDelete(p.id)"><mat-icon>delete</mat-icon></button>
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
    .filters mat-form-field { flex: 1; }
    table { width: 100%; }
    .badge { padding: 4px 8px; border-radius: 4px; font-size: 12px; font-weight: 500; }
    .badge-activo { background: #c8e6c9; color: #2e7d32; }
    .badge-inactivo { background: #ffcdd2; color: #c62828; }
  `]
})
export class PacienteListComponent implements OnInit {
  private pacienteService = inject(PacienteService);

  pacientes = signal<Paciente[]>([]);
  totalElements = signal(0);
  pageSize = signal(10);
  currentPage = signal(0);
  search = signal('');
  estadoFilter = signal('');

  displayedColumns = ['nombre', 'dni', 'sexo', 'telefono', 'estado', 'acciones'];

  ngOnInit() {
    this.loadPacientes();
  }

  loadPacientes() {
    this.pacienteService.findAll({
      search: this.search(),
      estado: this.estadoFilter() || undefined,
      page: this.currentPage(),
      size: this.pageSize()
    }).subscribe(data => {
      this.pacientes.set(data.content);
      this.totalElements.set(data.totalElements);
    });
  }

  onSearch(event: Event) {
    const value = (event.target as HTMLInputElement).value;
    this.search.set(value);
    this.currentPage.set(0);
    this.loadPacientes();
  }

  onEstadoChange(event: any) {
    this.estadoFilter.set(event.value);
    this.currentPage.set(0);
    this.loadPacientes();
  }

  onPage(event: PageEvent) {
    this.currentPage.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.loadPacientes();
  }

  onDelete(id: number) {
    if (confirm('¿Está seguro de eliminar este paciente?')) {
      this.pacienteService.delete(id).subscribe(() => this.loadPacientes());
    }
  }
}
