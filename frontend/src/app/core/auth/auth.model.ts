export interface AuthResponse {
  token: string;
  tipo: string;
  expiresIn: number;
  usuario: Usuario;
}

export interface Usuario {
  id: number;
  username: string;
  email: string;
  nombre: string;
  apellido: string;
  roles: string[];
  createdAt: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegistroRequest {
  username: string;
  email: string;
  password: string;
  nombre: string;
  apellido: string;
}
