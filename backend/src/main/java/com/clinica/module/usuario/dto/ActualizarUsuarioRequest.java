package com.clinica.module.usuario.dto;
import com.clinica.module.usuario.interfaces.I_DatosUsuariosRequest;
public record ActualizarUsuarioRequest(
    String username,
    String email,
    String nombre,
    String apellido


) implements I_DatosUsuariosRequest{}
