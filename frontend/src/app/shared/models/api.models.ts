export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

export interface Paciente {
  id: number;
  nombre: string;
  apellido: string;
  dni: string;
  fechaNacimiento: string;
  sexo: string;
  telefono?: string;
  email?: string;
  direccion?: string;
  grupoSanguineo?: string;
  alergias?: string;
  estado: string;
  createdAt: string;
}

export interface CrearPacienteRequest {
  nombre: string;
  apellido: string;
  dni: string;
  fechaNacimiento: string;
  sexo: string;
  telefono?: string;
  email?: string;
  direccion?: string;
  grupoSanguineo?: string;
  alergias?: string;
}

export interface Medico {
  id: number;
  nombre: string;
  apellido: string;
  dni: string;
  matricula: string;
  especialidad: { id: number; nombre: string };
  telefono?: string;
  email?: string;
  estado: string;
  createdAt: string;
}

export interface CrearMedicoRequest {
  nombre: string;
  apellido: string;
  dni: string;
  matricula: string;
  especialidadId: number;
  telefono?: string;
  email?: string;
}

export interface Especialidad {
  id: number;
  nombre: string;
  descripcion?: string;
}

export interface CrearEspecialidadRequest {
  nombre: string;
  descripcion?: string;
}

export interface Turno {
  id: number;
  paciente: { id: number; nombre: string; apellido: string };
  medico: { id: number; nombre: string; apellido: string; especialidad: string };
  fechaHora: string;
  duracionMinutos: number;
  motivo?: string;
  observaciones?: string;
  estado: string;
  createdAt: string;
}

export interface CrearTurnoRequest {
  pacienteId: number;
  medicoId: number;
  fechaHora: string;
  duracionMinutos?: number;
  motivo?: string;
}

export interface Usuario {
  id: number;
  username: string;
  email: string;
  nombre: string;
  apellido: string;
  roles: string[];
  estado: string;
  createdAt: string;
}

export interface Rol {
  id: number;
  nombre: string;
  descripcion?: string;
}

export interface DashboardData {
  totalPacientes: number;
  totalMedicos: number;
  totalEspecialidades: number;
  turnosHoy: number;
  turnosPendientes: number;
  turnosCompletados: number;
}
