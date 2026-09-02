package com.clinica.module.medico.dto;

import java.time.LocalDate;

/**
 * DTO utilizado para representar la información de un médico
 * en las respuestas de la API.
 *
 * <p>
 * Contiene únicamente los datos que deben ser expuestos hacia el cliente,
 * evitando devolver directamente la entidad {@link com.clinica.module.medico.entity.Medico}
 * y sus relaciones persistentes.
 * </p>
 *
 * <p>
 * La información presentada combina datos propios del médico con datos
 * básicos de su usuario asociado y un resumen de su especialidad.
 * </p>
 *
 * @param id identificador único del médico
 * @param nombre nombre del usuario asociado al médico
 * @param apellido apellido del usuario asociado al médico
 * @param email correo electrónico del usuario asociado
 * @param dni documento nacional de identidad del médico
 * @param especialidad especialidad médica asociada al profesional
 * @param matricula matrícula profesional del médico
 * @param fechaNacimiento fecha de nacimiento del médico
 * @param telefono número de teléfono del médico
 * @param estado estado actual del usuario asociado al médico
 */
public record MedicoResponse(

    /**
     * Identificador único del médico.
     */
    Long id,

    /**
     * Nombre del médico.
     */
    String nombre,

    /**
     * Apellido del médico.
     */
    String apellido,

    /**
     * Correo electrónico del usuario asociado al médico.
     */
    String email,

    /**
     * Documento nacional de identidad del médico.
     */
    String dni,

    /**
     * Información resumida de la especialidad del médico.
     */
    EspecialidadResumen especialidad,

    /**
     * Matrícula profesional del médico.
     */
    String matricula,

    /**
     * Fecha de nacimiento del médico.
     */
    LocalDate fechaNacimiento,

    /**
     * Número de teléfono del médico.
     */
    String telefono,

    /**
     * Estado actual del usuario asociado al médico.
     */
    String estado

) {}