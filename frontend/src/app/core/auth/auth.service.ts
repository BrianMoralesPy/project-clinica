import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap, switchMap, EMPTY } from 'rxjs';
import { AuthResponse, LoginRequest, RegistroRequest, Usuario } from './auth.model';

@Injectable({ providedIn: 'root' })
export class AuthService {

  // URL base de todos los endpoints de autenticación
  private readonly API_URL = '/api/auth';

  // Clave utilizada para guardar el JWT en LocalStorage
  private readonly TOKEN_KEY = 'auth_token';

  // Signal privada que almacena el usuario autenticado.
  // Solo este servicio puede modificarla.
  private readonly _currentUser = signal<Usuario | null>(null);

  // Exponemos una versión de solo lectura para que otros componentes
  // puedan consultar el usuario, pero no modificarlo.
  readonly currentUser = this._currentUser.asReadonly();

  // Signal derivada.
  // Devuelve true cuando existe un usuario autenticado y false en caso contrario.
  readonly isAuthenticated = computed(() => !!this._currentUser());

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  // Método privado encargado de actualizar el usuario autenticado.
  // Centralizar esta lógica evita modificar la signal desde distintos lugares.
  private setCurrentUser(usuario: Usuario | null): void {
    this._currentUser.set(usuario);
  }

  // Inicia sesión.
  // 1. Envía las credenciales al backend.
  // 2. Guarda el JWT recibido.
  // 3. Obtiene el usuario autenticado mediante /me.
  login(request: LoginRequest): Observable<Usuario> {
    return this.http
      .post<AuthResponse>(`${this.API_URL}/login`, request)
      .pipe(
        tap(response => this.handleAuthResponse(response)),
        switchMap(() => this.me())
      );
  }

  // Registra un nuevo usuario.
  // El flujo es exactamente igual al login:
  // guardar el token y cargar el usuario autenticado.
  register(request: RegistroRequest): Observable<Usuario> {
    return this.http
      .post<AuthResponse>(`${this.API_URL}/register`, request)
      .pipe(
        tap(response => this.handleAuthResponse(response)),
        switchMap(() => this.me())
      );
  }

  // Obtiene el usuario autenticado a partir del JWT.
  // Si el token es válido, el backend devuelve los datos del usuario
  // y actualizamos la signal.
  me(): Observable<Usuario> {
    return this.http
      .get<Usuario>(`${this.API_URL}/me`)
      .pipe(
        tap(usuario => this.setCurrentUser(usuario))
      );
  }

  // Restaura la sesión al recargar la aplicación.
  // Si existe un token guardado, intenta recuperar el usuario mediante /me.
  restoreSession(): Observable<Usuario> {

    // Si no hay token, no tiene sentido consultar al backend.
    if (!this.getToken()) {
      return EMPTY;
    }

    return this.me();
  }

  // Cierra la sesión del usuario.
  // Elimina el JWT, limpia el usuario actual y redirige al login.
  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    this.setCurrentUser(null);
    this.router.navigate(['/login']);
  }

  // Devuelve el JWT almacenado en LocalStorage.
  // Es utilizado principalmente por el interceptor HTTP.
  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  // Verifica si el usuario autenticado posee un determinado rol.
  // Si no hay usuario autenticado devuelve false.
  hasRole(role: string): boolean {
    const user = this._currentUser();
    return user?.roles.includes(role) ?? false;
  }

  // Procesa la respuesta del login o del registro.
  // Actualmente solo guarda el JWT, pero mantener esta lógica en un método
  // separado facilita agregar más comportamiento en el futuro.
  private handleAuthResponse(response: AuthResponse): void {
    localStorage.setItem(this.TOKEN_KEY, response.token);
  }
}