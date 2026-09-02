package com.clinica.module.paciente.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.clinica.module.paciente.interfaces.I_DatosPacientesRequest;
import java.time.LocalDate;
/**
 * DTO utilizado para registrar un nuevo paciente.
 *
 * Contiene los datos mínimos necesarios para crear
 * un paciente dentro del sistema.
 */
public record CrearPacienteRequest(
    // Usuario
    @NotBlank
    String username,
    @NotBlank
    String email,
    @NotBlank
    String password,
    @NotBlank
    String nombre,
    @NotBlank
    String apellido,
    // Paciente
    @NotBlank
    String dni,
    @NotNull
    LocalDate fechaNacimiento,
    @NotBlank
    String sexo,
    @NotBlank
    String telefono,
    @NotBlank
    String direccion,
    @NotBlank
    String grupoSanguineo,
    @NotBlank
    String alergias

) implements I_DatosPacientesRequest {}