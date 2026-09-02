package com.clinica.module.medico.dto;

import java.time.LocalDate;

import com.clinica.module.medico.interfaces.I_DatosMedicosRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para recibir los datos necesarios para actualizar
 * la información específica de un médico.
 *
 * <p>
 * Este objeto representa exclusivamente los datos de entrada de la solicitud
 * y no debe contener lógica de negocio ni lógica de persistencia.
 * </p>
 *
 * <p>
 * Implementa {@link I_DatosMedicosRequest} para mantener un contrato común
 * con las solicitudes que manejan datos propios del médico.
 * </p>
 *
 * <p>
 * Las restricciones de validación se aplican automáticamente cuando el DTO
 * es utilizado en un endpoint que realiza validación mediante Jakarta Validation.
 * </p>
 *
 * @param dni documento nacional de identidad del médico.
 *            Debe ser obligatorio y contener exactamente 8 caracteres.
 * @param matricula matrícula profesional del médico.
 *                  Es un dato obligatorio.
 * @param especialidadId identificador de la especialidad médica.
 *                      Es un dato obligatorio.
 * @param telefono número de teléfono del médico.
 *                 Es un dato obligatorio.
 * @param fechaNacimiento fecha de nacimiento del médico.
 *                       Es un dato obligatorio.
 */
public record ActualizarMedicoRequest(

    /**
     * Documento nacional de identidad del médico.
     */
    @NotBlank
    @Size(min = 8, max = 8)
    String dni,

    /**
     * Matrícula profesional del médico.
     */
    @NotBlank
    String matricula,

    /**
     * Identificador de la especialidad médica.
     */
    @NotNull
    Long especialidadId,

    /**
     * Número de teléfono del médico.
     */
    @NotBlank
    String telefono,

    /**
     * Fecha de nacimiento del médico.
     */
    @NotNull
    LocalDate fechaNacimiento

) implements I_DatosMedicosRequest {}
