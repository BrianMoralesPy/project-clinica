import { Component, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { AuthService } from '../../../../core/auth/auth.service';

@Component({
  selector: 'app-register-page',
  standalone: true,
  imports: [
    RouterLink,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule
  ],
  template: `
    <div class="register-container">
      <mat-card class="register-card">
        <mat-card-header>
          <mat-card-title>Registrarse</mat-card-title>
          <mat-card-subtitle>Sistema de Gestión Clínica</mat-card-subtitle>
        </mat-card-header>

        <mat-card-content>
          <form [formGroup]="registerForm" (ngSubmit)="onSubmit()">
            <mat-form-field appearance="outline" class="full-width">
              <mat-label>Nombre</mat-label>
              <input matInput formControlName="nombre" placeholder="Ingrese su nombre">
              @if (registerForm.get('nombre')?.hasError('required') && registerForm.get('nombre')?.touched) {
                <mat-error>El nombre es obligatorio</mat-error>
              }
            </mat-form-field>

            <mat-form-field appearance="outline" class="full-width">
              <mat-label>Apellido</mat-label>
              <input matInput formControlName="apellido" placeholder="Ingrese su apellido">
              @if (registerForm.get('apellido')?.hasError('required') && registerForm.get('apellido')?.touched) {
                <mat-error>El apellido es obligatorio</mat-error>
              }
            </mat-form-field>

            <mat-form-field appearance="outline" class="full-width">
              <mat-label>Username</mat-label>
              <input matInput formControlName="username" placeholder="Ingrese un username">
              @if (registerForm.get('username')?.hasError('required') && registerForm.get('username')?.touched) {
                <mat-error>El username es obligatorio</mat-error>
              }
              @if (registerForm.get('username')?.hasError('minlength') && registerForm.get('username')?.touched) {
                <mat-error>El username debe tener al menos 3 caracteres</mat-error>
              }
            </mat-form-field>

            <mat-form-field appearance="outline" class="full-width">
              <mat-label>Email</mat-label>
              <input matInput formControlName="email" type="email" placeholder="Ingrese su email">
              @if (registerForm.get('email')?.hasError('required') && registerForm.get('email')?.touched) {
                <mat-error>El email es obligatorio</mat-error>
              }
              @if (registerForm.get('email')?.hasError('email') && registerForm.get('email')?.touched) {
                <mat-error>El email debe ser válido</mat-error>
              }
            </mat-form-field>

            <mat-form-field appearance="outline" class="full-width">
              <mat-label>Contraseña</mat-label>
              <input matInput [type]="hidePassword() ? 'password' : 'text'" formControlName="password">
              <button mat-icon-button matSuffix (click)="hidePassword.set(!hidePassword())" type="button">
                <mat-icon>{{ hidePassword() ? 'visibility_off' : 'visibility' }}</mat-icon>
              </button>
              @if (registerForm.get('password')?.hasError('required') && registerForm.get('password')?.touched) {
                <mat-error>La contraseña es obligatoria</mat-error>
              }
              @if (registerForm.get('password')?.hasError('minlength') && registerForm.get('password')?.touched) {
                <mat-error>La contraseña debe tener al menos 8 caracteres</mat-error>
              }
            </mat-form-field>

            @if (errorMessage()) {
              <div class="error-message">{{ errorMessage() }}</div>
            }

            <button mat-raised-button color="primary" type="submit" class="full-width"
                    [disabled]="registerForm.invalid || isLoading()">
              {{ isLoading() ? 'Registrando...' : 'Registrarse' }}
            </button>
          </form>
        </mat-card-content>

        <mat-card-actions>
          <a routerLink="/login">¿Ya tiene cuenta? Iniciar sesión</a>
        </mat-card-actions>
      </mat-card>
    </div>
  `,
  styles: [`
    .register-container {
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 100vh;
      background-color: #f5f5f5;
    }
    .register-card {
      width: 100%;
      max-width: 400px;
    }
    .full-width {
      width: 100%;
    }
    mat-card-content {
      padding-top: 16px;
    }
    mat-card-actions {
      display: flex;
      justify-content: center;
      padding: 16px;
    }
    .error-message {
      color: #f44336;
      text-align: center;
      margin-bottom: 16px;
      font-size: 14px;
    }
  `]
})
export class RegisterPageComponent {
  registerForm: FormGroup;
  hidePassword = signal(true);
  isLoading = signal(false);
  errorMessage = signal('');

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      nombre: ['', Validators.required],
      apellido: ['', Validators.required],
      username: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]]
    });
  }

  onSubmit(): void {
    if (this.registerForm.invalid) return;

    this.isLoading.set(true);
    this.errorMessage.set('');

    this.authService.register(this.registerForm.value).subscribe({
      next: () => {
        this.router.navigate(['/dashboard']);
      },
      error: (err: any) => {
        this.errorMessage.set(err.error?.message || 'Error al registrarse');
        this.isLoading.set(false);
      }
    });
  }
}
