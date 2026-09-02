package com.clinica.module.usuario.interfaces;

/**
 * Contrato que define los datos básicos de un usuario
 * que pueden ser recibidos mediante las solicitudes de la API.
 *
 * <p>
 * Esta interfaz permite agrupar los campos comunes relacionados
 * con la información básica del usuario, evitando duplicar su
 * definición en diferentes DTOs de request.
 * </p>
 *
 * <p>
 * Los DTOs que implementen esta interfaz deben proporcionar acceso
 * a estos datos mediante los métodos definidos en el contrato.
 * </p>
 *
 * <p>
 * La interfaz únicamente define el acceso a los datos y no contiene
 * lógica de negocio, validaciones ni operaciones de persistencia.
 * Estas responsabilidades corresponden a las capas encargadas
 * de procesar las solicitudes.
 * </p>
 */
public interface I_DatosUsuariosRequest {

    /**
     * Obtiene el nombre de usuario utilizado para identificar
     * la cuenta dentro del sistema.
     *
     * @return nombre de usuario.
     */
    String username();

    /**
     * Obtiene la dirección de correo electrónico del usuario.
     *
     * @return email del usuario.
     */
    String email();

    /**
     * Obtiene el nombre del usuario.
     *
     * @return nombre del usuario.
     */
    String nombre();

    /**
     * Obtiene el apellido del usuario.
     *
     * @return apellido del usuario.
     */
    String apellido();

}
