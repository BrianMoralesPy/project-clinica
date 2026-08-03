import { Component, signal, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
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
  templateUrl: './register-page.component.html',
  styleUrl: './register-page.component.css'
})
export class RegisterPageComponent {

  // ==========================
  // Inyección de dependencias
  // ==========================

  // Constructor de formularios reactivos.
  private readonly fb = inject(FormBuilder);

  // Servicio encargado de toda la lógica de autenticación.
  private readonly authService = inject(AuthService);

  // Permite navegar entre las rutas de la aplicación.
  private readonly router = inject(Router);

  // ==========================
  // Estado reactivo de la UI
  // ==========================

  // Controla si la contraseña se muestra o permanece oculta.
  readonly hidePassword = signal(true);

  // Indica si hay una petición en curso.
  readonly isLoading = signal(false);

  // Mensaje de error mostrado al usuario.
  readonly errorMessage = signal('');

  // ==========================
  // Formulario reactivo
  // ==========================

  // Formulario tipado para registrar un nuevo usuario.
  readonly registerForm = this.fb.nonNullable.group({

    // Nombre del usuario.
    nombre: ['', [Validators.required]],

    // Apellido del usuario.
    apellido: ['', [Validators.required]],

    // Nombre de usuario.
    username: ['', [Validators.required, Validators.minLength(3)]],

    // Correo electrónico.
    email: ['', [Validators.required, Validators.email]],

    // Contraseña (mínimo 8 caracteres).
    password: ['', [Validators.required, Validators.minLength(8)]]
  });

  // Se ejecuta cuando el usuario envía el formulario.
  onSubmit(): void {

    // Si el formulario es inválido,
    // mostramos todas las validaciones y detenemos el proceso.
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }

    // Activamos el estado de carga y limpiamos errores anteriores.
    this.isLoading.set(true);
    this.errorMessage.set('');

    // Obtenemos los datos ingresados en el formulario.
    const userData = this.registerForm.getRawValue();

    // Enviamos los datos al backend para registrar al usuario.
    this.authService.register(userData).subscribe({

      // Registro exitoso.
      next: () => {

        // Una vez guardado el JWT,
        // obtenemos el usuario autenticado mediante /me.
        this.authService.me().subscribe({

          // Si todo salió correctamente,
          // redirigimos al dashboard.
          next: () => {
            this.isLoading.set(false);
            void this.router.navigate(['/dashboard']);
          },

          // Si el token no pudo validarse luego del registro,
          // cerramos la sesión y mostramos un mensaje.
          error: () => {
            this.authService.logout();

            this.errorMessage.set(
              'No se pudo iniciar la sesión luego del registro.'
            );

            this.isLoading.set(false);
          }
        });
      },

      // Error durante el registro.
      error: (err: any) => {
        this.errorMessage.set(
          err.error?.message ??
          'Ocurrió un error al procesar el registro. Intente nuevamente.'
        );

        this.isLoading.set(false);
      }
    });
  }
}