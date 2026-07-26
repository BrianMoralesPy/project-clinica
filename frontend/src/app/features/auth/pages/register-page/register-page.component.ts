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
  // 1. Inyección de dependencias moderna (Angular 14+)
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  // 2. Signals para el estado reactivo de la UI
  hidePassword = signal(true);
  isLoading = signal(false);
  errorMessage = signal('');

  // 3. Formulario fuertemente tipado con NonNullableFormBuilder
  registerForm = this.fb.nonNullable.group({
    nombre: ['', [Validators.required]],
    apellido: ['', [Validators.required]],
    username: ['', [Validators.required, Validators.minLength(3)]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(8)]]
  });

  onSubmit(): void {
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched(); // Muestra todos los errores visuales si intentan enviar vacío
      return;
    }

    this.isLoading.set(true);
    this.errorMessage.set('');

    // Extraemos los valores de forma segura (sin nulls)
    const userData = this.registerForm.getRawValue();

    this.authService.register(userData).subscribe({
      next: () => {
        this.router.navigate(['/dashboard']);
      },
      error: (err: any) => {
        this.errorMessage.set(err.error?.message || 'Ocurrió un error al procesar el registro. Intente nuevamente.');
        this.isLoading.set(false);
      }
    });
  }
}
