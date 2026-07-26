package com.clinica.module.paciente.controller;

import com.clinica.module.paciente.dto.ActualizarPacienteRequest;
import com.clinica.module.paciente.dto.CrearPacienteRequest;
import com.clinica.module.paciente.dto.PacienteResponse;
import com.clinica.module.paciente.service.PacienteService;
import com.clinica.shared.EstadoPaciente;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<Page<PacienteResponse>> findAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) EstadoPaciente estado,
            Pageable pageable) {
        return ResponseEntity.ok(pacienteService.findAll(search, estado, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION', 'MEDICO')")
    public ResponseEntity<PacienteResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(pacienteService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<PacienteResponse> create(@Valid @RequestBody CrearPacienteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<PacienteResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarPacienteRequest request) {
        return ResponseEntity.ok(pacienteService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pacienteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<PacienteResponse> changeStatus(
            @PathVariable Long id,
            @RequestBody java.util.Map<String, String> body) {
        return ResponseEntity.ok(pacienteService.changeStatus(id, body.get("estado")));
    }
}
