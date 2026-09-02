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


/**
 * Servicio encargado de centralizar la creación de usuarios
 * según el rol que tendrán dentro del sistema.
 *
 * La lógica común de creación se encuentra en el método privado
 * crearUsuario(), mientras que los métodos públicos indican
 * explícitamente qué tipo de usuario se desea crear.
 */
@Service
@RequiredArgsConstructor
public class UsuarioCreationByRoleService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;


    /**
     * Crea un usuario asignándole el rol indicado.
     *
     * Este método concentra toda la lógica común de creación:
     * - Validar que username y email no estén registrados.
     * - Buscar el rol correspondiente.
     * - Crear y completar la entidad Usuario.
     * - Encriptar la contraseña.
     * - Establecer el estado inicial como ACTIVO.
     * - Asignar el rol al usuario.
     * - Guardar el usuario en la base de datos.
     *
     * Es privado porque los demás servicios no necesitan indicar
     * directamente el nombre del rol. Para eso existen los métodos
     * públicos específicos de cada tipo de usuario.
     */
    private Usuario crearUsuario(
        String username,
        String email,
        String password,
        String nombre,
        String apellido,
        String nombreRol
    ) {

        // Verificar que el username no esté siendo utilizado
        if (usuarioRepository.existsByUsername(username)) {
            throw new BadRequestException(
                "El username ya está en uso"
            );
        }

        // Verificar que el email no esté siendo utilizado
        if (usuarioRepository.existsByEmail(email)) {
            throw new BadRequestException(
                "El email ya está registrado"
            );
        }


        // Buscar el rol solicitado en la base de datos
        Rol rol = rolRepository.findByNombre(nombreRol)
            .orElseThrow(() ->
                new BadRequestException(
                    "Rol " + nombreRol + " no encontrado"
                )
            );


        // Crear la nueva entidad Usuario
        Usuario usuario = new Usuario();

        usuario.setUsername(username);
        usuario.setEmail(email);

        // La contraseña nunca se almacena en texto plano.
        // Se guarda únicamente su hash mediante PasswordEncoder.
        usuario.setPasswordHash(
            passwordEncoder.encode(password)
        );

        usuario.setNombre(nombre);
        usuario.setApellido(apellido);

        // Todo usuario nuevo comienza activo
        usuario.setEstado(EstadoUsuario.ACTIVO);

        // Asignar el rol correspondiente al usuario
        usuario.setRoles(Set.of(rol));


        // Persistir el usuario y devolver la entidad guardada
        return usuarioRepository.save(usuario);
    }


    /**
     * Crea un usuario con rol PACIENTE.
     *
     * Delega toda la lógica de creación en crearUsuario(),
     * indicando únicamente el rol correspondiente.
     */
    public Usuario crearUsuarioTipoPaciente(
        String username,
        String email,
        String password,
        String nombre,
        String apellido
    ) {

        return crearUsuario(
            username,
            email,
            password,
            nombre,
            apellido,
            "PACIENTE"
        );
    }


    /**
     * Crea un usuario con rol MEDICO.
     *
     * Delega toda la lógica de creación en crearUsuario(),
     * indicando el rol correspondiente.
     */
    public Usuario crearUsuarioTipoMedico(
        String username,
        String email,
        String password,
        String nombre,
        String apellido
    ) {

        return crearUsuario(
            username,
            email,
            password,
            nombre,
            apellido,
            "MEDICO"
        );
    }


    /**
     * Crea un usuario con rol RECEPCION.
     *
     * Delega toda la lógica de creación en crearUsuario(),
     * indicando el rol correspondiente.
     */
    public Usuario crearUsuarioTipoRecepcionista(
        String username,
        String email,
        String password,
        String nombre,
        String apellido
    ) {

        return crearUsuario(
            username,
            email,
            password,
            nombre,
            apellido,
            "RECEPCION"
        );
    }


    /**
     * Crea un usuario con rol ADMIN.
     *
     * Delega toda la lógica de creación en crearUsuario(),
     * indicando el rol correspondiente.
     */
    public Usuario crearUsuarioTipoAdministrador(
        String username,
        String email,
        String password,
        String nombre,
        String apellido
    ) {

        return crearUsuario(
            username,
            email,
            password,
            nombre,
            apellido,
            "ADMIN"
        );
    }
}