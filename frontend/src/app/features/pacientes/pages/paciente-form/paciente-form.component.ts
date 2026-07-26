import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { Router, ActivatedRoute } from '@angular/router';
import { PacienteService } from '../../../../shared/services/domain.service';

@Component({
  selector: 'app-paciente-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatSelectModule, MatDatepickerModule, MatNativeDateModule],
  template: `
    <div class="container">
      <h2>{{ isEdit() ? 'Editar Paciente' : 'Nuevo Paciente' }}</h2>

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
          <mat-label>Fecha de Nacimiento</mat-label>
          <input matInput [matDatepicker]="picker" formControlName="fechaNacimiento">
          <mat-datepicker-toggle matIconSuffix [for]="picker"></mat-datepicker-toggle>
          <mat-datepicker #picker></mat-datepicker>
        </mat-form-field>

        <mat-form-field appearance="outline">
          <mat-label>Sexo</mat-label>
          <mat-select formControlName="sexo">
            <mat-option value="MASCULINO">Masculino</mat-option>
            <mat-option value="FEMENINO">Femenino</mat-option>
            <mat-option value="OTRO">Otro</mat-option>
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

        <mat-form-field appearance="outline">
          <mat-label>Dirección</mat-label>
          <input matInput formControlName="direccion">
        </mat-form-field>

        <mat-form-field appearance="outline">
          <mat-label>Grupo Sanguíneo</mat-label>
          <mat-select formControlName="grupoSanguineo">
            <mat-option value="A_POSITIVO">A+</mat-option>
            <mat-option value="A_NEGATIVO">A-</mat-option>
            <mat-option value="B_POSITIVO">B+</mat-option>
            <mat-option value="B_NEGATIVO">B-</mat-option>
            <mat-option value="AB_POSITIVO">AB+</mat-option>
            <mat-option value="AB_NEGATIVO">AB-</mat-option>
            <mat-option value="O_POSITIVO">O+</mat-option>
            <mat-option value="O_NEGATIVO">O-</mat-option>
          </mat-select>
        </mat-form-field>

        <mat-form-field appearance="outline" class="full-width">
          <mat-label>Alergias</mat-label>
          <textarea matInput formControlName="alergias" rows="3"></textarea>
        </mat-form-field>

        <div class="actions">
          <button mat-button type="button" routerLink="/pacientes">Cancelar</button>
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
    .full-width { grid-column: 1 / -1; }
    .actions { grid-column: 1 / -1; display: flex; justify-content: flex-end; gap: 8px; margin-top: 16px; }
  `]
})
export class PacienteFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private pacienteService = inject(PacienteService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  form: FormGroup = this.fb.group({
    nombre: ['', Validators.required],
    apellido: ['', Validators.required],
    dni: ['', Validators.required],
    fechaNacimiento: [null, Validators.required],
    sexo: ['', Validators.required],
    telefono: [''],
    email: [''],
    direccion: [''],
    grupoSanguineo: [''],
    alergias: ['']
  });

  isEdit = signal(false);
  isLoading = signal(false);
  private pacienteId = 0;

  ngOnInit() {
    this.pacienteId = Number(this.route.snapshot.paramMap.get('id'));
    if (this.pacienteId) {
      this.isEdit.set(true);
      this.pacienteService.findById(this.pacienteId).subscribe(p => {
        this.form.patchValue({
          nombre: p.nombre,
          apellido: p.apellido,
          dni: p.dni,
          fechaNacimiento: new Date(p.fechaNacimiento),
          sexo: p.sexo,
          telefono: p.telefono,
          email: p.email,
          direccion: p.direccion,
          grupoSanguineo: p.grupoSanguineo,
          alergias: p.alergias
        });
      });
    }
  }

  onSubmit() {
    if (this.form.invalid) return;
    this.isLoading.set(true);

    const data = {
      ...this.form.value,
      fechaNacimiento: this.form.value.fechaNacimiento?.toISOString().split('T')[0]
    };

    const request = this.isEdit()
      ? this.pacienteService.update(this.pacienteId, data)
      : this.pacienteService.create(data);

    request.subscribe({
      next: () => this.router.navigate(['/pacientes']),
      error: () => this.isLoading.set(false)
    });
  }
}
