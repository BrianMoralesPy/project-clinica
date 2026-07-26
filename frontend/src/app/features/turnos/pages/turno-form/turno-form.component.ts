import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { Router } from '@angular/router';
import { TurnoService, PacienteService, MedicoService } from '../../../../shared/services/domain.service';
import { Paciente, Medico } from '../../../../shared/models/api.models';

@Component({
  selector: 'app-turno-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatSelectModule, MatDatepickerModule, MatNativeDateModule],
  template: `
    <div class="container">
      <h2>Nuevo Turno</h2>

      <form [formGroup]="form" (ngSubmit)="onSubmit()" class="form-grid">
        <mat-form-field appearance="outline">
          <mat-label>Paciente</mat-label>
          <mat-select formControlName="pacienteId">
            @for (p of pacientes(); track p.id) {
              <mat-option [value]="p.id">{{ p.nombre }} {{ p.apellido }} ({{ p.dni }})</mat-option>
            }
          </mat-select>
        </mat-form-field>

        <mat-form-field appearance="outline">
          <mat-label>Médico</mat-label>
          <mat-select formControlName="medicoId">
            @for (m of medicos(); track m.id) {
              <mat-option [value]="m.id">{{ m.nombre }} {{ m.apellido }} - {{ m.especialidad.nombre }}</mat-option>
            }
          </mat-select>
        </mat-form-field>

        <mat-form-field appearance="outline">
          <mat-label>Fecha y Hora</mat-label>
          <input matInput [matDatepicker]="picker" formControlName="fecha">
          <mat-datepicker-toggle matIconSuffix [for]="picker"></mat-datepicker-toggle>
          <mat-datepicker #picker></mat-datepicker>
        </mat-form-field>

        <mat-form-field appearance="outline">
          <mat-label>Hora</mat-label>
          <input matInput type="time" formControlName="hora">
        </mat-form-field>

        <mat-form-field appearance="outline">
          <mat-label>Duración (minutos)</mat-label>
          <input matInput type="number" formControlName="duracionMinutos">
        </mat-form-field>

        <mat-form-field appearance="outline" class="full-width">
          <mat-label>Motivo</mat-label>
          <textarea matInput formControlName="motivo" rows="3"></textarea>
        </mat-form-field>

        <div class="actions">
          <button mat-button type="button" routerLink="/turnos">Cancelar</button>
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
export class TurnoFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private turnoService = inject(TurnoService);
  private pacienteService = inject(PacienteService);
  private medicoService = inject(MedicoService);
  private router = inject(Router);

  form: FormGroup = this.fb.group({
    pacienteId: [null, Validators.required],
    medicoId: [null, Validators.required],
    fecha: [null, Validators.required],
    hora: ['09:00', Validators.required],
    duracionMinutos: [30],
    motivo: ['']
  });

  pacientes = signal<Paciente[]>([]);
  medicos = signal<Medico[]>([]);
  isLoading = signal(false);

  ngOnInit() {
    this.pacienteService.findAll({ size: 100 }).subscribe(data => this.pacientes.set(data.content));
    this.medicoService.findAll({ size: 100 }).subscribe(data => this.medicos.set(data.content));
  }

  onSubmit() {
    if (this.form.invalid) return;
    this.isLoading.set(true);

    const { fecha, hora, ...rest } = this.form.value;
    const [hours, minutes] = hora.split(':');
    const fechaHora = new Date(fecha);
    fechaHora.setHours(parseInt(hours), parseInt(minutes), 0, 0);

    const data = {
      ...rest,
      fechaHora: fechaHora.toISOString()
    };

    this.turnoService.create(data).subscribe({
      next: () => this.router.navigate(['/turnos']),
      error: () => this.isLoading.set(false)
    });
  }
}
