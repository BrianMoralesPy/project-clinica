import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { EspecialidadService } from '../../../../shared/services/domain.service';
import { Especialidad } from '../../../../shared/models/api.models';
import { EspecialidadFormDialogComponent } from './especialidad-form-dialog.component';

@Component({
  selector: 'app-especialidad-list',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatButtonModule, MatIconModule, MatDialogModule],
  template: `
    <div class="container">
      <div class="header">
        <h2>Gestión de Especialidades</h2>
        <button mat-raised-button color="primary" (click)="onCreate()">
          <mat-icon>add</mat-icon> Nueva Especialidad
        </button>
      </div>

      <table mat-table [dataSource]="especialidades()">
        <ng-container matColumnDef="nombre">
          <th mat-header-cell *matHeaderCellDef>Nombre</th>
          <td mat-cell *matCellDef="let e">{{ e.nombre }}</td>
        </ng-container>

        <ng-container matColumnDef="descripcion">
          <th mat-header-cell *matHeaderCellDef>Descripción</th>
          <td mat-cell *matCellDef="let e">{{ e.descripcion || '-' }}</td>
        </ng-container>

        <ng-container matColumnDef="acciones">
          <th mat-header-cell *matHeaderCellDef>Acciones</th>
          <td mat-cell *matCellDef="let e">
            <button mat-icon-button (click)="onEdit(e)"><mat-icon>edit</mat-icon></button>
            <button mat-icon-button color="warn" (click)="onDelete(e.id)"><mat-icon>delete</mat-icon></button>
          </td>
        </ng-container>

        <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
        <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
      </table>
    </div>
  `,
  styles: [`
    .container { padding: 24px; }
    .header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; }
    table { width: 100%; }
  `]
})
export class EspecialidadListComponent implements OnInit {
  private especialidadService = inject(EspecialidadService);
  private dialog = inject(MatDialog);

  especialidades = signal<Especialidad[]>([]);
  displayedColumns = ['nombre', 'descripcion', 'acciones'];

  ngOnInit() {
    this.loadEspecialidades();
  }

  loadEspecialidades() {
    this.especialidadService.findAll().subscribe(data => this.especialidades.set(data));
  }

  onCreate() {
    const dialogRef = this.dialog.open(EspecialidadFormDialogComponent, { data: {} });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.especialidadService.create(result).subscribe(() => this.loadEspecialidades());
      }
    });
  }

  onEdit(esp: Especialidad) {
    const dialogRef = this.dialog.open(EspecialidadFormDialogComponent, { data: esp });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.especialidadService.update(esp.id, result).subscribe(() => this.loadEspecialidades());
      }
    });
  }

  onDelete(id: number) {
    if (confirm('¿Está seguro de eliminar esta especialidad?')) {
      this.especialidadService.delete(id).subscribe(() => this.loadEspecialidades());
    }
  }
}
