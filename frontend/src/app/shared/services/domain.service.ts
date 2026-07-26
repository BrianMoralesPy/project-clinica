import { Injectable, inject } from '@angular/core';
import { ApiService } from './api.service';
import { Observable } from 'rxjs';
import {
  PageResponse, Paciente, CrearPacienteRequest,
  Medico, CrearMedicoRequest,
  Especialidad, CrearEspecialidadRequest,
  Turno, CrearTurnoRequest,
  Usuario, Rol,
  DashboardData
} from '../models/api.models';

@Injectable({ providedIn: 'root' })
export class PacienteService {
  private api = inject(ApiService);

  findAll(params: { search?: string; estado?: string; page?: number; size?: number } = {}): Observable<PageResponse<Paciente>> {
    return this.api.get<PageResponse<Paciente>>('/pacientes', params);
  }

  findById(id: number): Observable<Paciente> {
    return this.api.get<Paciente>(`/pacientes/${id}`);
  }

  create(data: CrearPacienteRequest): Observable<Paciente> {
    return this.api.post<Paciente>('/pacientes', data);
  }

  update(id: number, data: CrearPacienteRequest): Observable<Paciente> {
    return this.api.put<Paciente>(`/pacientes/${id}`, data);
  }

  delete(id: number): Observable<void> {
    return this.api.delete<void>(`/pacientes/${id}`);
  }

  changeStatus(id: number, estado: string): Observable<Paciente> {
    return this.api.patch<Paciente>(`/pacientes/${id}/estado`, { estado });
  }
}

@Injectable({ providedIn: 'root' })
export class MedicoService {
  private api = inject(ApiService);

  findAll(params: { especialidadId?: number; estado?: string; page?: number; size?: number } = {}): Observable<PageResponse<Medico>> {
    return this.api.get<PageResponse<Medico>>('/medicos', params);
  }

  findById(id: number): Observable<Medico> {
    return this.api.get<Medico>(`/medicos/${id}`);
  }

  create(data: CrearMedicoRequest): Observable<Medico> {
    return this.api.post<Medico>('/medicos', data);
  }

  update(id: number, data: CrearMedicoRequest): Observable<Medico> {
    return this.api.put<Medico>(`/medicos/${id}`, data);
  }

  delete(id: number): Observable<void> {
    return this.api.delete<void>(`/medicos/${id}`);
  }
}

@Injectable({ providedIn: 'root' })
export class EspecialidadService {
  private api = inject(ApiService);

  findAll(): Observable<Especialidad[]> {
    return this.api.get<Especialidad[]>('/especialidades');
  }

  create(data: CrearEspecialidadRequest): Observable<Especialidad> {
    return this.api.post<Especialidad>('/especialidades', data);
  }

  update(id: number, data: CrearEspecialidadRequest): Observable<Especialidad> {
    return this.api.put<Especialidad>(`/especialidades/${id}`, data);
  }

  delete(id: number): Observable<void> {
    return this.api.delete<void>(`/especialidades/${id}`);
  }
}

@Injectable({ providedIn: 'root' })
export class TurnoService {
  private api = inject(ApiService);

  findAll(params: { medicoId?: number; pacienteId?: number; estado?: string; page?: number; size?: number } = {}): Observable<PageResponse<Turno>> {
    return this.api.get<PageResponse<Turno>>('/turnos', params);
  }

  findById(id: number): Observable<Turno> {
    return this.api.get<Turno>(`/turnos/${id}`);
  }

  create(data: CrearTurnoRequest): Observable<Turno> {
    return this.api.post<Turno>('/turnos', data);
  }

  cancel(id: number): Observable<Turno> {
    return this.api.patch<Turno>(`/turnos/${id}/cancelar`, {});
  }

  complete(id: number, observaciones?: string): Observable<Turno> {
    return this.api.patch<Turno>(`/turnos/${id}/completar`, { observaciones });
  }
}

@Injectable({ providedIn: 'root' })
export class UsuarioService {
  private api = inject(ApiService);

  findAll(params: { search?: string; estado?: string; page?: number; size?: number } = {}): Observable<PageResponse<Usuario>> {
    return this.api.get<PageResponse<Usuario>>('/usuarios', params);
  }

  findById(id: number): Observable<Usuario> {
    return this.api.get<Usuario>(`/usuarios/${id}`);
  }
}

@Injectable({ providedIn: 'root' })
export class RolService {
  private api = inject(ApiService);

  findAll(): Observable<Rol[]> {
    return this.api.get<Rol[]>('/roles');
  }
}

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private api = inject(ApiService);

  getDashboard(): Observable<DashboardData> {
    return this.api.get<DashboardData>('/dashboard');
  }
}
