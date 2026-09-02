package com.clinica.module.medico.controller;

import com.clinica.module.medico.dto.ActualizarMedicoRequest;
import com.clinica.module.medico.dto.ActualizarMedicoYUsuarioRequest;
import com.clinica.module.medico.dto.CrearMedicoRequest;
import com.clinica.module.medico.dto.MedicoResponse;
import com.clinica.module.medico.service.MedicoService;
import com.clinica.shared.EstadoUsuario;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST encargado de gestionar las operaciones relacionadas
 * con los médicos del sistema.
 *
 * <p>
 * Expone los endpoints correspondientes a la consulta, creación,
 * actualización y baja lógica de médicos, además del acceso de un médico
 * autenticado a su propio perfil.
 * </p>
 *
 * <p>
 * El controlador actúa como punto de entrada de las solicitudes HTTP
 * y delega la lógica de negocio en {@link MedicoService}. No contiene
 * reglas de negocio ni lógica de persistencia.
 * </p>
 *
 * <p>
 * Los endpoints administrativos requieren los roles {@code ADMIN} o
 * {@code RECEPCION}, mientras que el acceso al perfil propio está
 * restringido al rol {@code MEDICO}.
 * </p>
 */
@RestController
@RequestMapping("/api/medicos")
@RequiredArgsConstructor
public class MedicoController {

    private final MedicoService medicoService;

    // ============================================================
    // ENDPOINTS ADMINISTRATIVOS
    // Roles permitidos: ADMIN / RECEPCION
    // ============================================================

    /**
     * Obtiene un listado paginado de médicos.
     *
     * <p>
     * Permite filtrar opcionalmente los resultados por especialidad
     * y por estado del usuario asociado al médico.
     * </p>
     *
     * @param especialidadId identificador de la especialidad por la que
     *                       se desea filtrar; opcional
     * @param estado estado del usuario por el que se desea filtrar;
     *               opcional
     * @param pageable parámetros de paginación y ordenamiento
     * @return página de médicos que cumplen con los filtros indicados
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<Page<MedicoResponse>> findAll(
            @RequestParam(required = false) Long especialidadId,
            @RequestParam(required = false) EstadoUsuario estado,
            Pageable pageable) {

        return ResponseEntity.ok(
                medicoService.findAll(especialidadId, estado, pageable)
        );
    }

    /**
     * Obtiene un médico a partir de su identificador.
     *
     * @param id identificador del médico
     * @return información del médico solicitado
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<MedicoResponse> findById(@PathVariable Long id) {

        return ResponseEntity.ok(
                medicoService.findById(id)
        );
    }

    /**
     * Crea un nuevo médico junto con su usuario asociado.
     *
     * <p>
     * Los datos recibidos son validados mediante {@link Valid} antes
     * de ser enviados a la capa de servicio.
     * </p>
     *
     * <p>
     * Al tratarse de una creación exitosa, el endpoint responde con
     * el estado HTTP {@link HttpStatus#CREATED}.
     * </p>
     *
     * @param request datos necesarios para crear el médico y su usuario
     * @return información del médico creado
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<MedicoResponse> create(
            @Valid @RequestBody CrearMedicoRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(medicoService.create(request));
    }

    /**
     * Actualiza los datos específicos de un médico.
     *
     * <p>
     * Este endpoint modifica únicamente la información correspondiente
     * a la entidad {@code Medico}, sin modificar los datos del usuario
     * asociado.
     * </p>
     *
     * @param id identificador del médico a actualizar
     * @param request datos actualizados del médico
     * @return información actualizada del médico
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<MedicoResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarMedicoRequest request) {

        return ResponseEntity.ok(
                medicoService.update(id, request)
        );
    }

    /**
     * Actualiza de forma conjunta los datos del médico y los datos
     * de su usuario asociado.
     *
     * <p>
     * Recibe un DTO compuesto que contiene por separado la información
     * correspondiente al médico y al usuario.
     * </p>
     *
     * @param id identificador del médico a actualizar
     * @param request datos actualizados del médico y del usuario asociado
     * @return información actualizada del médico
     */
    @PutMapping("/full/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<MedicoResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarMedicoYUsuarioRequest request) {

        return ResponseEntity.ok(
                medicoService.update(
                        id,
                        request.medicoRequest(),
                        request.usuarioRequest()
                )
        );
    }

    /**
     * Realiza la baja lógica de un médico.
     *
     * <p>
     * El médico no es eliminado físicamente de la base de datos.
     * La operación modifica el estado del usuario asociado a
     * {@code INACTIVO}, permitiendo conservar la información histórica.
     * </p>
     *
     * @param id identificador del médico que se desea dar de baja
     * @return respuesta sin contenido
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        medicoService.softDelete(id);

        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // ENDPOINTS PROPIOS DEL MÉDICO
    // Rol permitido: MEDICO
    // ============================================================

    /**
     * Obtiene el perfil del médico actualmente autenticado.
     *
     * <p>
     * El médico no debe proporcionar su identificador, ya que la
     * identificación del usuario autenticado se obtiene mediante
     * el contexto de seguridad.
     * </p>
     *
     * @return información del perfil del médico autenticado
     */
    @GetMapping("/me")
    @PreAuthorize("hasRole('MEDICO')")
    public ResponseEntity<MedicoResponse> getMiPerfil() {

        return ResponseEntity.ok(
                medicoService.getMiPerfil()
        );
    }
}