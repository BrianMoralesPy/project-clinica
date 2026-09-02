package com.clinica.module.usuario.dto;

import com.clinica.module.usuario.interfaces.I_DatosUsuariosRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para recibir los datos necesarios
 * para actualizar la información de un usuario.
 *
 * Implementa I_DatosUsuariosRequest para compartir
 * una estructura común con otros objetos de solicitud
 * relacionados con los datos del usuario.
 */
public record ActualizarUsuarioRequest(

    /**
     * Nombre de usuario.
     * Es opcional durante la actualización, pero si se proporciona
     * no puede superar los 50 caracteres.
     */
    @Size(
        max = 50,
        message = "El username no puede superar los 50 caracteres"
    )
    String username,

    /**
     * Correo electrónico del usuario.
     * Si se proporciona, debe tener un formato de email válido
     * y no puede superar los 100 caracteres.
     */
    @Email(message = "El email debe tener un formato válido")
    @Size(
        max = 100,
        message = "El email no puede superar los 100 caracteres"
    )
    String email,

    /**
     * Nombre del usuario.
     * Si se proporciona, no puede superar los 100 caracteres.
     */
    @Size(
        max = 100,
        message = "El nombre no puede superar los 100 caracteres"
    )
    String nombre,

    /**
     * Apellido del usuario.
     * Si se proporciona, no puede superar los 100 caracteres.
     */
    @Size(
        max = 100,
        message = "El apellido no puede superar los 100 caracteres"
    )
    String apellido

) implements I_DatosUsuariosRequest {}