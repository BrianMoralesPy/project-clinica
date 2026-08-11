package com.clinica.module.paciente.dto;
import com.clinica.module.usuario.dto.ActualizarUsuarioRequest;


public record ActualizarPacienteYUsuarioRequest(
    ActualizarPacienteRequest pacienteRequest,
    ActualizarUsuarioRequest usuarioRequest
) {}