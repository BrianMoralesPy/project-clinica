package com.clinica.module.usuario.controller;

import com.clinica.module.usuario.dto.ActualizarRolRequest;
import com.clinica.module.usuario.dto.CrearRolRequest;
import com.clinica.module.usuario.dto.RolResponse;
import com.clinica.module.usuario.service.RolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador encargado de gestionar las operaciones relacionadas
 * con los roles del sistema.
 *
 * Expone los endpoints REST para consultar, crear y actualizar roles.
 *
 * El acceso a estos endpoints está restringido a usuarios
 * que posean el rol ADMIN.
 */
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RolController {

    /**
     * Servicio encargado de contener la lógica de negocio
     * relacionada con los roles.
     */
    private final RolService rolService;

    /**
     * Obtiene todos los roles registrados en el sistema.
     *
     * Solo los usuarios con rol ADMIN pueden acceder.
     *
     * @return lista de roles
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RolResponse>> findAll() {
        return ResponseEntity.ok(
            rolService.findAll()
        );
    }

    /**
     * Obtiene un rol a partir de su identificador.
     *
     * Solo los usuarios con rol ADMIN pueden acceder.
     *
     * @param id identificador del rol
     * @return información del rol solicitado
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RolResponse> findById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
            rolService.findById(id)
        );
    }

    /**
     * Crea un nuevo rol.
     *
     * @Valid permite ejecutar las validaciones definidas
     * en CrearRolRequest antes de procesar la solicitud.
     *
     * Solo los usuarios con rol ADMIN pueden acceder.
     *
     * @param request datos del nuevo rol
     * @return rol creado
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RolResponse> create(
            @Valid @RequestBody CrearRolRequest request) {

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(rolService.create(request));
    }

    /**
     * Actualiza los datos de un rol existente.
     *
     * @Valid permite validar los datos recibidos
     * en ActualizarRolRequest.
     *
     * Solo los usuarios con rol ADMIN pueden acceder.
     *
     * @param id identificador del rol a actualizar
     * @param request nuevos datos del rol
     * @return rol actualizado
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RolResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarRolRequest request) {

        return ResponseEntity.ok(
            rolService.update(id, request)
        );
    }
}