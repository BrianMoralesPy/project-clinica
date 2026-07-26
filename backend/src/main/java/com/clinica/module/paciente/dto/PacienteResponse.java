package com.clinica.module.paciente.dto;

import java.time.Instant;
import java.time.LocalDate;

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
    Instant createdAt
) {}