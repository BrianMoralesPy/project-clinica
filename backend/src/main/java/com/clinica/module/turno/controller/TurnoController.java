package com.clinica.module.turno.controller;

import com.clinica.module.turno.dto.ActualizarTurnoRequest;
import com.clinica.module.turno.dto.CompletarTurnoRequest;
import com.clinica.module.turno.dto.CrearTurnoRequest;
import com.clinica.module.turno.dto.TurnoResponse;
import com.clinica.module.turno.service.TurnoService;
import com.clinica.shared.EstadoTurno;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/turnos")
@RequiredArgsConstructor
public class TurnoController {

    private final TurnoService turnoService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION', 'MEDICO')")
    public ResponseEntity<Page<TurnoResponse>> findAll(
            @RequestParam(required = false) Long medicoId,
            @RequestParam(required = false) Long pacienteId,
            @RequestParam(required = false) EstadoTurno estado,
            @RequestParam(required = false) Instant fechaDesde,
            @RequestParam(required = false) Instant fechaHasta,
            Pageable pageable) {
        return ResponseEntity.ok(turnoService.findAll(medicoId, pacienteId, estado, fechaDesde, fechaHasta, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION', 'MEDICO')")
    public ResponseEntity<TurnoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(turnoService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<TurnoResponse> create(@Valid @RequestBody CrearTurnoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(turnoService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<TurnoResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarTurnoRequest request) {
        return ResponseEntity.ok(turnoService.update(id, request));
    }

    @PatchMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<TurnoResponse> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(turnoService.cancel(id));
    }

    @PatchMapping("/{id}/completar")
    @PreAuthorize("hasRole('MEDICO')")
    public ResponseEntity<TurnoResponse> complete(
            @PathVariable Long id,
            @RequestBody CompletarTurnoRequest request) {
        return ResponseEntity.ok(turnoService.complete(id, request));
    }
}
