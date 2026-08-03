// ===========================
// Respuestas del Backend
// ===========================

// Respuesta que devuelve el backend luego de un login o un registro exitoso.
export interface AuthResponse {

  // JWT utilizado para autenticar las siguientes peticiones.
  token: string;

  // Tipo de autenticación (normalmente "Bearer").
  tipo: string;

  // Tiempo de validez del token (generalmente expresado en segundos).
  expiresIn: number;

  // Información del usuario autenticado.
  usuario: Usuario;
}

// Modelo que representa un usuario dentro de la aplicación.
export interface Usuario {

  // Identificador único del usuario.
  id: number;

  // Nombre de usuario utilizado para iniciar sesión.
  username: string;

  // Correo electrónico.
  email: string;

  // Nombre real del usuario.
  nombre: string;

  // Apellido del usuario.
  apellido: string;

  // Lista de roles asignados.
  // Ejemplo: ["ROLE_ADMIN"] o ["ROLE_MEDICO"].
  roles: string[];

  // Fecha de creación de la cuenta.
  createdAt: string;
}

// ===========================
// Requests hacia el Backend
// ===========================

// Datos necesarios para iniciar sesión.
export interface LoginRequest {

  // Puede ser username o email según la lógica implementada.
  identifier: string;

  // Contraseña del usuario.
  password: string;
}

// Datos necesarios para registrar un nuevo usuario.
export interface RegistroRequest {

  // Nombre de usuario.
  username: string;

  // Correo electrónico.
  email: string;

  // Contraseña.
  password: string;

  // Nombre.
  nombre: string;

  // Apellido.
  apellido: string;
}