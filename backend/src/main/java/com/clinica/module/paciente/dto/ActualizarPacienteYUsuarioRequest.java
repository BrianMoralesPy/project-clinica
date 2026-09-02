package com.clinica.module.paciente.dto;

import com.clinica.module.usuario.dto.ActualizarUsuarioRequest;

/**
 * DTO compuesto utilizado para recibir en una única solicitud los datos
 * necesarios para actualizar tanto la información del paciente como la
 * de su usuario asociado.
 *
 * <p>
 * Agrupa {@link ActualizarPacienteRequest}, que contiene los datos
 * específicos del paciente, y {@link ActualizarUsuarioRequest}, que
 * contiene los datos correspondientes al usuario asociado.
 * </p>
 *
 * <p>
 * Este objeto actúa únicamente como contenedor de datos. La coordinación
 * de la actualización de ambas entidades y las reglas de negocio
 * correspondientes deben ser gestionadas por la capa de servicio.
 * </p>
 *
 * @param pacienteRequest datos correspondientes a la información específica
 *                        del paciente
 * @param usuarioRequest datos correspondientes a la información del usuario
 *                       asociado al paciente
 */
public record ActualizarPacienteYUsuarioRequest(

    /**
     * Datos necesarios para actualizar la información del paciente.
     */
    ActualizarPacienteRequest pacienteRequest,

    /**
     * Datos necesarios para actualizar la información del usuario asociado.
     */
    ActualizarUsuarioRequest usuarioRequest

) {}