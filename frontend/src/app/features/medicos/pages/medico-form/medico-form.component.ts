import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { Router, ActivatedRoute } from '@angular/router';
import { MedicoService, EspecialidadService } from '../../../../shared/services/domain.service';
import { Especialidad } from '../../../../shared/models/api.models';

@Component({
  selector: 'app-medico-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatSelectModule],
  template: `
    <div class="container">
      <h2>{{ isEdit() ? 'Editar Médico' : 'Nuevo Médico' }}</h2>

      <form [formGroup]="form" (ngSubmit)="onSubmit()" class="form-grid">
        <mat-form-field appearance="outline">
          <mat-label>Nombre</mat-label>
          <input matInput formControlName="nombre">
        </mat-form-field>

        <mat-form-field appearance="outline">
          <mat-label>Apellido</mat-label>
          <input matInput formControlName="apellido">
        </mat-form-field>

        <mat-form-field appearance="outline">
          <mat-label>DNI</mat-label>
          <input matInput formControlName="dni">
        </mat-form-field>

        <mat-form-field appearance="outline">
          <mat-label>Matrícula</mat-label>
          <input matInput formControlName="matricula">
        </mat-form-field>

        <mat-form-field appearance="outline">
          <mat-label>Especialidad</mat-label>
          <mat-select formControlName="especialidadId">
            @for (esp of especialidades(); track esp.id) {
              <mat-option [value]="esp.id">{{ esp.nombre }}</mat-option>
            }
          </mat-select>
        </mat-form-field>

        <mat-form-field appearance="outline">
          <mat-label>Teléfono</mat-label>
          <input matInput formControlName="telefono">
        </mat-form-field>

        <mat-form-field appearance="outline">
          <mat-label>Email</mat-label>
          <input matInput formControlName="email" type="email">
        </mat-form-field>

        <div class="actions">
          <button mat-button type="button" routerLink="/medicos">Cancelar</button>
          <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid || isLoading()">
            {{ isLoading() ? 'Guardando...' : 'Guardar' }}
          </button>
        </div>
      </form>
    </div>
  `,
  styles: [`
    .container { padding: 24px; max-width: 800px; }
    .form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 0 16px; }
    .actions { grid-column: 1 / -1; display: flex; justify-content: flex-end; gap: 8px; margin-top: 16px; }
  `]
})
export class MedicoFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private medicoService = inject(MedicoService);
  private especialidadService = inject(EspecialidadService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  form: FormGroup = this.fb.group({
    nombre: ['', Validators.required],
    apellido: ['', Validators.required],
    dni: ['', Validators.required],
    matricula: ['', Validators.required],
    especialidadId: [null, Validators.required],
    telefono: [''],
    email: ['']
  });

  isEdit = signal(false);
  isLoading = signal(false);
  especialidades = signal<Especialidad[]>([]);
  private medicoId = 0;

  ngOnInit() {
    this.especialidadService.findAll().subscribe(data => this.especialidades.set(data));

    this.medicoId = Number(this.route.snapshot.paramMap.get('id'));
    if (this.medicoId) {
      this.isEdit.set(true);
      this.medicoService.findById(this.medicoId).subscribe(m => {
        this.form.patchValue({
          nombre: m.nombre,
          apellido: m.apellido,
          dni: m.dni,
          matricula: m.matricula,
          especialidadId: m.especialidad.id,
          telefono: m.telefono,
          email: m.email
        });
      });
    }
  }

  onSubmit() {
    if (this.form.invalid) return;
    this.isLoading.set(true);

    const request = this.isEdit()
      ? this.medicoService.update(this.medicoId, this.form.value)
      : this.medicoService.create(this.form.value);

    request.subscribe({
      next: () => this.router.navigate(['/medicos']),
      error: () => this.isLoading.set(false)
    });
  }
}
