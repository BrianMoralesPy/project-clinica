import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';

// Guard que verifica si existe un usuario autenticado.
// Si no hay sesión iniciada, redirige al login.
export const authGuard: CanActivateFn = () => {

  // Obtenemos las dependencias mediante la API moderna de Angular.
  const authService = inject(AuthService);
  const router = inject(Router);

  // Si el usuario está autenticado, permitimos acceder a la ruta.
  if (authService.isAuthenticated()) {
    return true;
  }

  // Si no está autenticado, lo enviamos al login.
  router.navigate(['/login']);
  return false;
};

// Guard reutilizable para restringir el acceso según los roles del usuario.
// Recibe un arreglo con los roles permitidos.
export const roleGuard = (allowedRoles: string[]): CanActivateFn => {

  return () => {

    const authService = inject(AuthService);
    const router = inject(Router);

    // Primero verificamos que el usuario tenga una sesión iniciada.
    // Si no está autenticado, no tiene sentido comprobar los roles.
    if (!authService.isAuthenticated()) {
      router.navigate(['/login']);
      return false;
    }

    // Verificamos si el usuario posee al menos uno de los roles permitidos.
    // El método some() devuelve true cuando encuentra el primer rol válido.
    const hasAllowedRole = allowedRoles.some(role =>
      authService.hasRole(role)
    );

    // Si tiene un rol permitido, puede acceder a la ruta.
    if (hasAllowedRole) {
      return true;
    }

    // Si está autenticado pero no tiene permisos,
    // lo redirigimos al dashboard.
    router.navigate(['/dashboard']);
    return false;
  };
};