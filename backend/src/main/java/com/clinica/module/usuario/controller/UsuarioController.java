package com.clinica.module.usuario.controller;

import com.clinica.module.usuario.dto.ActualizarUsuarioRequest;
import com.clinica.module.usuario.dto.CrearUsuarioRequest;
import com.clinica.module.usuario.dto.UsuarioResponse;
import com.clinica.module.usuario.service.UsuarioService;
import com.clinica.shared.EstadoUsuario;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

/**
 * Controlador encargado de gestionar las operaciones relacionadas
 * con los usuarios del sistema.
 *
 * Expone los endpoints REST para consultar, crear, actualizar
 * y eliminar usuarios.
 *
 * El acceso a estos endpoints está restringido mediante
 * autorización basada en roles.
 */
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    /**
     * Servicio encargado de contener la lógica de negocio
     * relacionada con los usuarios.
     */
    private final UsuarioService usuarioService;

    /**
     * Obtiene un listado paginado de usuarios.
     *
     * Permite filtrar opcionalmente por un texto de búsqueda
     * y por el estado del usuario.
     *
     * Solo los usuarios con rol ADMIN pueden acceder.
     *
     * @param search texto utilizado para buscar usuarios
     * @param estado estado por el cual se desea filtrar
     * @param pageable parámetros de paginación y ordenamiento
     * @return página de usuarios encontrados
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UsuarioResponse>> findAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) EstadoUsuario estado,
            Pageable pageable) {

        return ResponseEntity.ok(
            usuarioService.findAll(search, estado, pageable)
        );
    }

    /**
     * Obtiene un usuario a partir de su identificador.
     *
     * Solo los usuarios con rol ADMIN pueden acceder.
     *
     * @param id identificador del usuario
     * @return información del usuario solicitado
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> findById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
            usuarioService.findById(id)
        );
    }

    /**
     * Crea un nuevo usuario.
     *
     * @Valid permite ejecutar las validaciones definidas
     * en CrearUsuarioRequest antes de procesar la solicitud.
     *
     * Solo los usuarios con rol ADMIN pueden acceder.
     *
     * @param request datos del nuevo usuario
     * @return usuario creado
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> create(
            @Valid @RequestBody CrearUsuarioRequest request) {

        UsuarioResponse response =
            usuarioService.crearUsuarioAdminOrRecepcion(request);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }

    /**
     * Actualiza los datos de un usuario existente.
     *
     * @Valid permite validar los datos recibidos
     * en ActualizarUsuarioRequest.
     *
     * Solo los usuarios con rol ADMIN pueden acceder.
     *
     * @param id identificador del usuario a actualizar
     * @param request nuevos datos del usuario
     * @return usuario actualizado
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarUsuarioRequest request) {

        return ResponseEntity.ok(
            usuarioService.update(id, request)
        );
    }

    /**
     * Realiza la eliminación lógica de un usuario.
     *
     * En lugar de eliminar físicamente el registro de la base
     * de datos, el servicio cambia su estado para indicar
     * que el usuario ya no se encuentra activo.
     *
     * Authentication permite obtener información del usuario
     * autenticado que está realizando la operación.
     *
     * Solo los usuarios con rol ADMIN pueden acceder.
     *
     * @param id identificador del usuario a eliminar
     * @param authentication información del usuario autenticado
     * @return respuesta sin contenido
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            Authentication authentication) {

        usuarioService.softDelete(id, authentication.getName());

        return ResponseEntity.noContent().build();
    }
}