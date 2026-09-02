package com.clinica.module.paciente.controller;

import com.clinica.module.paciente.dto.ActualizarPacienteRequest;
import com.clinica.module.paciente.dto.ActualizarPacienteYUsuarioRequest;
import com.clinica.module.paciente.dto.CrearPacienteRequest;
import com.clinica.module.paciente.dto.PacienteResponse;
import com.clinica.module.paciente.service.PacienteService;
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
 * con los pacientes del sistema.
 *
 * <p>
 * Expone los endpoints necesarios para la consulta, creación, actualización
 * y baja lógica de pacientes, además de proporcionar funcionalidades de
 * autoservicio para que un paciente autenticado pueda consultar y completar
 * su propio perfil.
 * </p>
 *
 * <p>
 * El controlador actúa como punto de entrada de las solicitudes HTTP
 * y delega la lógica de negocio en {@link PacienteService}. No contiene
 * reglas de negocio ni lógica de persistencia.
 * </p>
 *
 * <p>
 * Los endpoints administrativos requieren los roles {@code ADMIN} o
 * {@code RECEPCION}, mientras que determinadas operaciones de consulta
 * también permiten el acceso al rol {@code MEDICO}. Las operaciones
 * de autoservicio están restringidas al propio rol {@code PACIENTE}.
 * </p>
 */
@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;

    // ============================================================
    // ENDPOINTS ADMINISTRATIVOS
    // Roles principales: ADMIN / RECEPCION
    // ============================================================

    /**
     * Obtiene un listado paginado de pacientes.
     *
     * <p>
     * Permite realizar una búsqueda opcional mediante un texto y filtrar
     * los resultados según el estado del usuario asociado al paciente.
     * </p>
     *
     * @param search texto utilizado para buscar pacientes; opcional
     * @param estado estado del usuario por el que se desea filtrar; opcional
     * @param pageable parámetros de paginación y ordenamiento
     * @return página de pacientes que cumplen con los filtros indicados
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<Page<PacienteResponse>> findAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) EstadoUsuario estado,
            Pageable pageable) {

        return ResponseEntity.ok(
                pacienteService.findAll(search, estado, pageable)
        );
    }

    /**
     * Obtiene un paciente a partir de su identificador.
     *
     * <p>
     * Este endpoint puede ser utilizado por usuarios con rol
     * {@code ADMIN}, {@code RECEPCION} o {@code MEDICO}.
     * </p>
     *
     * @param id identificador del paciente
     * @return información del paciente solicitado
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION', 'MEDICO')")
    public ResponseEntity<PacienteResponse> findById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                pacienteService.findById(id)
        );
    }

    /**
     * Crea un nuevo paciente junto con su usuario asociado.
     *
     * <p>
     * Los datos recibidos son validados mediante Jakarta Validation
     * antes de ser enviados a la capa de servicio.
     * </p>
     *
     * <p>
     * En caso de creación exitosa, se devuelve el estado HTTP
     * {@link HttpStatus#CREATED}.
     * </p>
     *
     * @param request datos necesarios para crear el paciente y su usuario
     * @return información del paciente creado
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<PacienteResponse> create(
            @Valid @RequestBody CrearPacienteRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pacienteService.create(request));
    }

    /**
     * Actualiza únicamente los datos específicos del paciente.
     *
     * <p>
     * Esta operación no modifica los datos correspondientes al usuario
     * asociado al paciente.
     * </p>
     *
     * @param id identificador del paciente a actualizar
     * @param request datos actualizados del paciente
     * @return información actualizada del paciente
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<PacienteResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarPacienteRequest request) {

        return ResponseEntity.ok(
                pacienteService.update(id, request)
        );
    }

    /**
     * Actualiza de forma conjunta los datos del paciente y los datos
     * de su usuario asociado.
     *
     * <p>
     * Recibe un DTO compuesto que separa la información correspondiente
     * al paciente de la información correspondiente al usuario.
     * </p>
     *
     * @param id identificador del paciente a actualizar
     * @param request datos actualizados del paciente y del usuario asociado
     * @return información actualizada del paciente
     */
    @PutMapping("/full/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<PacienteResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarPacienteYUsuarioRequest request) {

        return ResponseEntity.ok(
                pacienteService.update(
                        id,
                        request.pacienteRequest(),
                        request.usuarioRequest()
                )
        );
    }

    /**
     * Realiza la baja lógica de un paciente.
     *
     * <p>
     * El paciente no se elimina físicamente de la base de datos.
     * La operación modifica el estado del usuario asociado a
     * {@code INACTIVO}, permitiendo conservar la información histórica.
     * </p>
     *
     * @param id identificador del paciente que se desea dar de baja
     * @return respuesta sin contenido
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        pacienteService.softDelete(id);

        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // ENDPOINTS DE AUTOSERVICIO
    // Rol permitido: PACIENTE
    // ============================================================

    /**
     * Permite al paciente autenticado completar o actualizar
     * la información correspondiente a su propio perfil.
     *
     * <p>
     * El identificador del paciente no se recibe como parámetro,
     * ya que el paciente se determina a partir del contexto de seguridad.
     * </p>
     *
     * @param request datos que se desean actualizar en el perfil
     * @return información actualizada del paciente
     */
    @PutMapping("/me")
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<PacienteResponse> completarMiPerfil(
            @Valid @RequestBody ActualizarPacienteRequest request) {

        return ResponseEntity.ok(
                pacienteService.completarMiPerfil(request)
        );
    }

    /**
     * Obtiene el perfil del paciente actualmente autenticado.
     *
     * <p>
     * El identificador del paciente no se recibe como parámetro,
     * ya que el paciente se obtiene a partir del contexto de seguridad.
     * </p>
     *
     * @return información del perfil del paciente autenticado
     */
    @GetMapping("/me")
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<PacienteResponse> getMiPerfil() {

        return ResponseEntity.ok(
                pacienteService.getMiPerfil()
        );
    }
}
