import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from './auth.service';

// Interceptor que agrega automáticamente el JWT a todas las peticiones HTTP.
// De esta forma, no es necesario agregar el header Authorization en cada servicio.
export const authInterceptor: HttpInterceptorFn = (req, next) => {

  // Obtenemos el AuthService para acceder al token almacenado.
  const authService = inject(AuthService);

  // Recuperamos el JWT desde LocalStorage.
  const token = authService.getToken();

  // Si existe un token, clonamos la petición agregando el header Authorization.
  // Las peticiones HTTP son inmutables, por eso debemos crear una copia.
  if (token) {

    const cloned = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });

    // Continuamos la cadena enviando la petición modificada.
    return next(cloned);
  }

  // Si no hay token, enviamos la petición original sin modificaciones.
  return next(req);
};