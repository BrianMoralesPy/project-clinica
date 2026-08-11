package com.clinica.module.paciente.dto;

import java.time.LocalDate;
/* 
DTO utilizado para enviar la información
de un paciente al frontend
*/
public record PacienteResponse(
    Long id,
    String nombre,
    String apellido,
    String email,
    String dni,
    LocalDate fechaNacimiento,
    String sexo,
    String telefono,
    String direccion,
    String grupoSanguineo,
    String alergias,
    String estado,
    Boolean perfilCompleto
) {}