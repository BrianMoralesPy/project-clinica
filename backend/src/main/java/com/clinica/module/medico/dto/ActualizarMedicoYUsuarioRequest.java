package com.clinica.module.medico.dto;

import com.clinica.module.usuario.dto.ActualizarUsuarioRequest;

/**
 * DTO compuesto utilizado para recibir en una única solicitud los datos
 * necesarios para actualizar tanto la información del médico como la de
 * su usuario asociado.
 *
 * <p>
 * Agrupa {@link ActualizarMedicoRequest}, que contiene los datos específicos
 * del médico, y {@link ActualizarUsuarioRequest}, que contiene los datos
 * correspondientes al usuario.
 * </p>
 *
 * <p>
 * Este objeto actúa únicamente como contenedor de datos. La coordinación
 * y las reglas de negocio necesarias para actualizar ambas entidades
 * corresponden a la capa de servicio.
 * </p>
 *
 * @param medicoRequest datos correspondientes a la información específica
 *                      del médico
 * @param usuarioRequest datos correspondientes a la información del usuario
 *                       asociado al médico
 */
public record ActualizarMedicoYUsuarioRequest(

    /**
     * Datos necesarios para actualizar la información del médico.
     */
    ActualizarMedicoRequest medicoRequest,

    /**
     * Datos necesarios para actualizar la información del usuario asociado.
     */
    ActualizarUsuarioRequest usuarioRequest

) {}
