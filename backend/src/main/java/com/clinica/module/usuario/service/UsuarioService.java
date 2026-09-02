package com.clinica.module.usuario.service;

import com.clinica.exception.BadRequestException;
import com.clinica.exception.ResourceNotFoundException;
import com.clinica.module.usuario.dto.ActualizarUsuarioRequest;
import com.clinica.module.usuario.dto.CrearUsuarioRequest;
import com.clinica.module.usuario.dto.UsuarioResponse;
import com.clinica.module.usuario.entity.Rol;
import com.clinica.module.usuario.entity.Usuario;
import com.clinica.module.usuario.interfaces.I_DatosUsuariosRequest;
import com.clinica.module.usuario.mapper.UsuarioMapper;
import com.clinica.module.usuario.repository.RolRepository;
import com.clinica.module.usuario.repository.UsuarioRepository;
import com.clinica.shared.EstadoUsuario;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Servicio encargado de gestionar la lógica de negocio relacionada
 * con los usuarios del sistema.
 *
 * Se ocupa de consultar, actualizar, crear y desactivar usuarios,
 * delegando el acceso a la base de datos en los repositorios.
 */
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioCreationByRoleService usuarioCreationByRoleService;


    /**
     * Actualiza los datos comunes de un usuario.
     *
     * Este método recibe una interfaz que contiene los datos compartidos
     * por las diferentes solicitudes de actualización de usuarios.
     *
     * Solo se modifican los campos que hayan sido enviados en la request.
     * Antes de cambiar username o email se verifica que no estén siendo
     * utilizados por otro usuario.
     */
    @Transactional
    public void actualizarDatosUsuario(
        Usuario usuario,
        I_DatosUsuariosRequest request
    ) {

        // Actualizar username si fue enviado y es diferente al actual
        if (request.username() != null &&
            !request.username().equals(usuario.getUsername())) {

            // Verificar que el nuevo username no pertenezca a otro usuario
            if (usuarioRepository.existsByUsername(request.username())) {
                throw new BadRequestException("El username ya está en uso");
            }

            usuario.setUsername(request.username());
        }

        // Actualizar email si fue enviado y es diferente al actual
        if (request.email() != null &&
            !request.email().equals(usuario.getEmail())) {

            // Verificar que el nuevo email no pertenezca a otro usuario
            if (usuarioRepository.existsByEmail(request.email())) {
                throw new BadRequestException("El email ya está registrado");
            }

            usuario.setEmail(request.email());
        }

        // Actualizar nombre únicamente si fue enviado
        if (request.nombre() != null) {
            usuario.setNombre(request.nombre());
        }

        // Actualizar apellido únicamente si fue enviado
        if (request.apellido() != null) {
            usuario.setApellido(request.apellido());
        }
    }


    /**
     * Busca un rol por su nombre.
     *
     * Antes de realizar la búsqueda se normaliza el valor recibido
     * eliminando espacios innecesarios y convirtiéndolo a mayúsculas.
     *
     * Si el rol no existe, se lanza una excepción de solicitud inválida.
     */
    private Rol obtenerRol(String nombreRol) {

        String rolNormalizado = nombreRol.trim().toUpperCase();

        return rolRepository.findByNombre(rolNormalizado)
            .orElseThrow(() ->
                new BadRequestException(
                    "El rol no existe: " + nombreRol
                )
            );
    }


    /**
     * Obtiene todos los usuarios aplicando filtros opcionales
     * de búsqueda y estado.
     *
     * El resultado se devuelve paginado para evitar cargar todos
     * los usuarios en memoria.
     *
     * La entidad Usuario se transforma a UsuarioResponse mediante
     * el mapper antes de devolver el resultado.
     */
    @Transactional(readOnly = true)
    public Page<UsuarioResponse> findAll(
        String search,
        EstadoUsuario estado,
        Pageable pageable
    ) {

        return usuarioRepository
            .findBySearchAndEstado(search, estado, pageable)
            .map(UsuarioMapper::toResponse);
    }


    /**
     * Busca un usuario por su ID.
     *
     * Si no existe, se lanza ResourceNotFoundException.
     *
     * Una vez encontrado, se transforma la entidad a DTO de respuesta
     * mediante UsuarioMapper.
     */
    @Transactional(readOnly = true)
    public UsuarioResponse findById(Long id) {

        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("Usuario", id)
            );

        return UsuarioMapper.toResponse(usuario);
    }


    /**
     * Actualiza los datos de un usuario existente.
     *
     * Primero se busca el usuario por ID. Si existe, se delega la
     * actualización de sus datos comunes al método
     * actualizarDatosUsuario().
     *
     * Finalmente se persiste el usuario y se devuelve su representación
     * mediante UsuarioResponse.
     */
    @Transactional
    public UsuarioResponse update(
        Long id,
        ActualizarUsuarioRequest request
    ) {

        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("Usuario", id)
            );

        actualizarDatosUsuario(usuario, request);

        usuario = usuarioRepository.save(usuario);

        return UsuarioMapper.toResponse(usuario);
    }


    /**
     * Crea usuarios con rol ADMIN o RECEPCION.
     *
     * Primero se obtiene y valida el rol solicitado.
     * Luego se delega la creación del usuario al servicio especializado
     * UsuarioCreationByRoleService.
     *
     * Este endpoint no permite crear otros tipos de usuarios, como
     * PACIENTE o MEDICO, ya que esos roles requieren procesos de creación
     * adicionales asociados a sus respectivas fichas.
     */
    @Transactional
    public UsuarioResponse crearUsuarioAdminOrRecepcion(
        CrearUsuarioRequest request
    ) {

        // Obtener y validar el rol solicitado
        Rol rol = obtenerRol(request.rol());

        Usuario usuario;

        // Crear el usuario utilizando el proceso correspondiente a su rol
        switch (rol.getNombre()) {

            case "ADMIN" ->
                usuario = usuarioCreationByRoleService
                    .crearUsuarioTipoAdministrador(
                        request.username(),
                        request.email(),
                        request.password(),
                        request.nombre(),
                        request.apellido()
                    );

            case "RECEPCION" ->
                usuario = usuarioCreationByRoleService
                    .crearUsuarioTipoRecepcionista(
                        request.username(),
                        request.email(),
                        request.password(),
                        request.nombre(),
                        request.apellido()
                    );

            // Este endpoint no permite crear otros roles
            default ->
                throw new BadRequestException(
                    "Desde este endpoint solamente se pueden crear usuarios ADMIN o RECEPCION"
                );
        }

        return UsuarioMapper.toResponse(usuario);
    }


    /**
     * Desactiva un usuario mediante un soft delete.
     *
     * El registro no se elimina físicamente de la base de datos.
     * En su lugar, su estado pasa a INACTIVO, permitiendo conservar
     * la información histórica asociada al usuario.
     *
     * Además, se impide que un administrador se desactive a sí mismo
     * y que este endpoint sea utilizado para desactivar pacientes o médicos.
     */
    @Transactional
    public void softDelete(Long id, String usernameActual) {

        // Buscar el usuario que se desea desactivar
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("Usuario", id)
            );


        // Evitar que un administrador se elimine a sí mismo
        if (usuario.getUsername().equals(usernameActual)) {
            throw new BadRequestException(
                "No puedes eliminar tu propio usuario"
            );
        }


        // Verificar que el usuario tenga un rol permitido para este endpoint
        boolean esAdminORecepcion = usuario.getRoles().stream()
            .anyMatch(rol ->
                rol.getNombre().equals("ADMIN") ||
                rol.getNombre().equals("RECEPCION")
            );

        if (!esAdminORecepcion) {
            throw new BadRequestException(
                "Solo se pueden eliminar usuarios ADMIN o RECEPCION"
            );
        }


        // Soft delete: se conserva el registro y solamente se cambia su estado
        usuario.setEstado(EstadoUsuario.INACTIVO);

        usuarioRepository.save(usuario);
    }
}