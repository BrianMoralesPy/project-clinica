import { Component, DestroyRef, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';

import { finalize } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

import { AuthService } from '../../../../core/auth/auth.service';

@Component({
  selector: 'app-login-page',
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
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.css'
})
export class LoginPageComponent {

  // ==========================
  // Inyección de dependencias
  // ==========================

  // Constructor de formularios reactivos.
  private readonly fb = inject(FormBuilder);

  // Servicio encargado de toda la lógica de autenticación.
  private readonly authService = inject(AuthService);

  // Permite navegar entre las rutas de la aplicación.
  private readonly router = inject(Router);

  // Referencia al ciclo de vida del componente.
  // Se utiliza para cancelar automáticamente las suscripciones
  // cuando el componente es destruido.
  private readonly destroyRef = inject(DestroyRef);

  // ==========================
  // Estado reactivo de la UI
  // ==========================

  // Controla si la contraseña se muestra o permanece oculta.
  readonly hidePassword = signal(true);

  // Indica si hay una petición en curso.
  // Se utiliza para deshabilitar el formulario y mostrar un spinner.
  readonly isLoading = signal(false);

  // Mensaje de error mostrado al usuario.
  readonly errorMessage = signal('');

  // ==========================
  // Formulario reactivo
  // ==========================

  // Formulario tipado para el inicio de sesión.
  readonly loginForm = this.fb.nonNullable.group({

    // Puede ser username o email.
    identifier: ['', Validators.required],

    // Contraseña del usuario.
    password: ['', Validators.required]
  });

  constructor() {

    // Cada vez que el usuario modifica un campo,
    // eliminamos el mensaje de error anterior.
    // takeUntilDestroyed() cancela automáticamente la suscripción
    // cuando el componente deja de existir.
    this.loginForm.valueChanges
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => this.errorMessage.set(''));
  }

  // Se ejecuta cuando el usuario envía el formulario.
  onSubmit(): void {

    // Si el formulario es inválido,
    // mostramos todas las validaciones y detenemos el proceso.
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    // Activamos el estado de carga.
    this.isLoading.set(true);

    // Obtenemos los valores del formulario.
    const credentials = this.loginForm.getRawValue();

    // Enviamos las credenciales al AuthService.
    this.authService.login(credentials)
      .pipe(

        // finalize() se ejecuta tanto si la petición fue exitosa
        // como si ocurrió un error.
        // Es el lugar ideal para desactivar el estado de carga.
        finalize(() => this.isLoading.set(false))
      )
      .subscribe({

        // Login exitoso.
        next: () => {
          this.router.navigate(['/dashboard']);
        },

        // Error durante el login.
        error: (err: HttpErrorResponse) => {
          this.errorMessage.set(
            err.error?.message ??
            'Usuario o contraseña incorrectos.'
          );
        }
      });
  }
}