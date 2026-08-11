package com.clinica.module.medico.controller;

import com.clinica.module.medico.dto.ActualizarMedicoRequest;
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

@RestController
@RequestMapping("/api/medicos")
@RequiredArgsConstructor
public class MedicoController {

    private final MedicoService medicoService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<Page<MedicoResponse>> findAll(
            @RequestParam(required = false) Long especialidadId,
            @RequestParam(required = false) EstadoUsuario estado,
            Pageable pageable) {
        return null;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<MedicoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(medicoService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MedicoResponse> create(@Valid @RequestBody CrearMedicoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(medicoService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MedicoResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarMedicoRequest request) {
        return ResponseEntity.ok(medicoService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        medicoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MedicoResponse> changeStatus(
            @PathVariable Long id,
            @RequestBody java.util.Map<String, String> body) {
        return ResponseEntity.ok(medicoService.changeStatus(id, body.get("estado")));
    }
}
