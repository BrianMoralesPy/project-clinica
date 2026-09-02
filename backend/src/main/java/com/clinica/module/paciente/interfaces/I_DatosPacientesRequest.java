package com.clinica.module.paciente.interfaces;

import java.time.LocalDate;

/**
 * Contrato que define los datos específicos de un paciente
 * que pueden ser recibidos mediante las solicitudes de la API.
 *
 * <p>
 * Esta interfaz permite agrupar los campos propios del paciente
 * que son compartidos por diferentes DTOs de request, evitando
 * duplicar la definición de estos datos y facilitando que distintas
 * solicitudes mantengan una estructura común.
 * </p>
 *
 * <p>
 * La interfaz únicamente define el contrato de acceso a los datos.
 * No contiene lógica de negocio, validaciones ni operaciones de
 * persistencia; esas responsabilidades corresponden a las capas
 * encargadas de procesar las solicitudes.
 * </p>
 */
public interface I_DatosPacientesRequest {

    /**
     * Obtiene el Documento Nacional de Identidad del paciente.
     *
     * @return DNI del paciente.
     */
    String dni();

    /**
     * Obtiene la fecha de nacimiento del paciente.
     *
     * @return fecha de nacimiento.
     */
    LocalDate fechaNacimiento();

    /**
     * Obtiene el sexo informado para el paciente.
     *
     * @return valor correspondiente al sexo.
     */
    String sexo();

    /**
     * Obtiene el número de teléfono del paciente.
     *
     * @return teléfono de contacto.
     */
    String telefono();

    /**
     * Obtiene la dirección del paciente.
     *
     * @return dirección de residencia.
     */
    String direccion();

    /**
     * Obtiene el grupo sanguíneo del paciente.
     *
     * @return grupo sanguíneo informado.
     */
    String grupoSanguineo();

    /**
     * Obtiene la información relacionada con alergias conocidas
     * del paciente.
     *
     * @return descripción de las alergias.
     */
    String alergias();
}
