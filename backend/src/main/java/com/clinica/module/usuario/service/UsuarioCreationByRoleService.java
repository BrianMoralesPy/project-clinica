package com.clinica.module.usuario.service;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.clinica.module.usuario.entity.Rol;
import com.clinica.module.usuario.entity.Usuario;
import com.clinica.module.usuario.repository.RolRepository;
import com.clinica.module.usuario.repository.UsuarioRepository;
import com.clinica.shared.EstadoUsuario;
import com.clinica.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UsuarioCreationByRoleService {

    

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    

    private Usuario crearUsuario(String username,String email,String password,String nombre,String apellido,String nombreRol) {

        if (usuarioRepository.existsByUsername(username)) { throw new BadRequestException("El username ya está en uso");}

        if (usuarioRepository.existsByEmail(email)) { throw new BadRequestException("El email ya está registrado");}

        Rol rol = rolRepository.findByNombre(nombreRol).orElseThrow(() ->new BadRequestException("Rol " + nombreRol + " no encontrado"));

        Usuario usuario = new Usuario();

        usuario.setUsername(username);
        usuario.setEmail(email);
        usuario.setPasswordHash(passwordEncoder.encode(password));
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setEstado(EstadoUsuario.ACTIVO);
        usuario.setRoles(Set.of(rol));
        return usuarioRepository.save(usuario);
    }

    public Usuario crearUsuarioTipoPaciente(String username,String email,String password,String nombre,String apellido) {

        return crearUsuario(username,email,password,nombre,apellido,"PACIENTE");

    }

    public Usuario crearUsuarioTipoMedico(String username,String email,String password,String nombre,String apellido) {

        return crearUsuario(username,email,password,nombre,apellido,"MEDICO");
    }

    public Usuario crearUsuarioTipoRecepcionista(String username,String email,String password,String nombre,String apellido) {

        return crearUsuario(username,email,password,nombre,apellido,"RECEPCION");
    }

    public Usuario crearUsuarioTipoAdministrador(String username,String email,String password,String nombre,String apellido) {

        return crearUsuario(username,email,password,nombre,apellido,"ADMIN");
    }
    
}
