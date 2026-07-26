package com.clinica.module.paciente.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CrearPacienteRequest(
    @NotBlank @Size(max = 100) String nombre,
    @NotBlank @Size(max = 100) String apellido,
    @NotBlank @Size(max = 20) String dni,
    @NotNull LocalDate fechaNacimiento,
    @NotBlank String sexo,
    @Size(max = 30) String telefono,
    @Size(max = 100) String email,
    @Size(max = 200) String direccion,
    String grupoSanguineo,
    String alergias
) {}

public record ActualizarPacienteRequest(
    @NotBlank @Size(max = 100) String nombre,
    @NotBlank @Size(max = 100) String apellido,
    @NotNull LocalDate fechaNacimiento,
    @NotBlank String sexo,
    @Size(max = 30) String telefono,
    @Size(max = 100) String email,
    @Size(max = 200) String direccion,
    String grupoSanguineo,
    String alergias
) {}

public record PacienteResponse(
    Long id,
    String nombre,
    String apellido,
    String dni,
    LocalDate fechaNacimiento,
    String sexo,
    String telefono,
    String email,
    String direccion,
    String grupoSanguineo,
    String alergias,
    String estado,
    java.time.Instant createdAt
) {}
