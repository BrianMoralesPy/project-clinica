package com.clinica.module.paciente.dto;
import com.clinica.module.paciente.interfaces.I_DatosPacientesRequest;
import java.time.LocalDate;

/**
 * DTO utilizado para actualizar la información de un paciente existente.
 *
 * Contiene los datos mínimos necesarios para modificar
 * la información de un paciente en el sistema.
 *
 * Las validaciones se ejecutan automáticamente
 * antes de que el controlador invoque al servicio.
 * ademas implementa la interfaz I_DatosPacientesRequest para garantizar que se cumplan los 
 * contratos de datos necesarios para un paciente.
 */
public record ActualizarPacienteRequest(
    // Datos del paciente
    String dni, 
    LocalDate fechaNacimiento, 
    String sexo, 
    String telefono,
    String direccion, 
    String grupoSanguineo, 
    String alergias
) implements I_DatosPacientesRequest{}