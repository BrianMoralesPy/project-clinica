import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { UsuarioService } from '../../../../shared/services/domain.service';
import { Usuario } from '../../../../shared/models/api.models';

@Component({
  selector: 'app-usuario-list',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatPaginatorModule, MatButtonModule, MatIconModule, MatInputModule, MatFormFieldModule],
  template: `
    <div class="container">
      <div class="header">
        <h2>Gestión de Usuarios</h2>
      </div>

      <div class="filters">
        <mat-form-field appearance="outline">
          <mat-label>Buscar</mat-label>
          <input matInput [value]="search()" (input)="onSearch($event)" placeholder="Nombre, apellido o username">
          <mat-icon matPrefix>search</mat-icon>
        </mat-form-field>
      </div>

      <table mat-table [dataSource]="usuarios()">
        <ng-container matColumnDef="username">
          <th mat-header-cell *matHeaderCellDef>Username</th>
          <td mat-cell *matCellDef="let u">{{ u.username }}</td>
        </ng-container>

        <ng-container matColumnDef="nombre">
          <th mat-header-cell *matHeaderCellDef>Nombre</th>
          <td mat-cell *matCellDef="let u">{{ u.nombre }} {{ u.apellido }}</td>
        </ng-container>

        <ng-container matColumnDef="email">
          <th mat-header-cell *matHeaderCellDef>Email</th>
          <td mat-cell *matCellDef="let u">{{ u.email }}</td>
        </ng-container>

        <ng-container matColumnDef="roles">
          <th mat-header-cell *matHeaderCellDef>Roles</th>
          <td mat-cell *matCellDef="let u">
            @for (role of u.roles; track role) {
              <span class="badge">{{ role }}</span>
            }
          </td>
        </ng-container>

        <ng-container matColumnDef="estado">
          <th mat-header-cell *matHeaderCellDef>Estado</th>
          <td mat-cell *matCellDef="let u">
            <span [class]="'badge badge-' + u.estado.toLowerCase()">{{ u.estado }}</span>
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
    .badge { padding: 2px 8px; border-radius: 4px; font-size: 12px; font-weight: 500; margin-right: 4px; background: #e3f2fd; color: #1565c0; }
    .badge-activo { background: #c8e6c9; color: #2e7d32; }
    .badge-inactivo { background: #ffcdd2; color: #c62828; }
    .badge-bloqueado { background: #fff3e0; color: #e65100; }
  `]
})
export class UsuarioListComponent implements OnInit {
  private usuarioService = inject(UsuarioService);

  usuarios = signal<Usuario[]>([]);
  totalElements = signal(0);
  pageSize = signal(10);
  currentPage = signal(0);
  search = signal('');

  displayedColumns = ['username', 'nombre', 'email', 'roles', 'estado'];

  ngOnInit() {
    this.loadUsuarios();
  }

  loadUsuarios() {
    this.usuarioService.findAll({
      search: this.search(),
      page: this.currentPage(),
      size: this.pageSize()
    }).subscribe(data => {
      this.usuarios.set(data.content);
      this.totalElements.set(data.totalElements);
    });
  }

  onSearch(event: Event) {
    const value = (event.target as HTMLInputElement).value;
    this.search.set(value);
    this.currentPage.set(0);
    this.loadUsuarios();
  }

  onPage(event: PageEvent) {
    this.currentPage.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.loadUsuarios();
  }
}
