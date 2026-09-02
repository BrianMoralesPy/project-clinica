package com.clinica.module.medico.dto;

import java.time.LocalDate;

import com.clinica.module.medico.interfaces.I_DatosMedicosRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para recibir los datos necesarios para crear un médico
 * junto con su usuario asociado.
 *
 * <p>
 * Esta solicitud agrupa en un único objeto los datos correspondientes a
 * {@code Usuario} y los datos específicos de {@code Medico}, permitiendo
 * realizar la creación de ambas entidades como parte de una misma operación.
 * </p>
 *
 * <p>
 * Implementa {@link I_DatosMedicosRequest} para mantener un contrato común
 * con las solicitudes que contienen información específica del médico.
 * </p>
 *
 * <p>
 * Las anotaciones de Jakarta Validation permiten validar los datos recibidos
 * antes de que sean procesados por la capa de servicio.
 * </p>
 *
 * <p>
 * Este DTO únicamente representa y valida los datos de entrada. La creación
 * de las entidades, sus relaciones y las reglas de negocio correspondientes
 * deben ser gestionadas por la capa de servicio.
 * </p>
 *
 * @param username nombre de usuario utilizado para acceder al sistema
 * @param email dirección de correo electrónico del usuario
 * @param password contraseña del usuario
 * @param nombre nombre del usuario
 * @param apellido apellido del usuario
 * @param dni documento nacional de identidad del médico
 * @param matricula matrícula profesional del médico
 * @param especialidadId identificador de la especialidad médica
 * @param telefono número de teléfono del médico
 * @param fechaNacimiento fecha de nacimiento del médico
 */
public record CrearMedicoRequest(

    // =========================
    // Datos del Usuario
    // =========================

    /**
     * Nombre de usuario utilizado para autenticarse en el sistema.
     */
    @NotBlank
    String username,

    /**
     * Dirección de correo electrónico del usuario.
     */
    @NotBlank
    String email,

    /**
     * Contraseña utilizada para autenticarse en el sistema.
     *
     * <p>
     * La contraseña debe ser procesada y almacenada de forma segura
     * por la capa correspondiente antes de persistirse.
     * </p>
     */
    @NotBlank
    String password,

    /**
     * Nombre del usuario.
     */
    @NotBlank
    String nombre,

    /**
     * Apellido del usuario.
     */
    @NotBlank
    String apellido,

    // =========================
    // Datos del Médico
    // =========================

    /**
     * Documento nacional de identidad del médico.
     */
    @NotBlank
    String dni,

    /**
     * Matrícula profesional del médico.
     */
    @NotBlank
    String matricula,

    /**
     * Identificador de la especialidad médica que tendrá el profesional.
     */
    @NotNull
    Long especialidadId,

    /**
     * Número de teléfono del médico.
     *
     * <p>
     * Debe contener exactamente 10 caracteres.
     * </p>
     */
    @NotBlank
    @Size(min = 10, max = 10)
    String telefono,

    /**
     * Fecha de nacimiento del médico.
     */
    @NotNull
    LocalDate fechaNacimiento

) implements I_DatosMedicosRequest {}
