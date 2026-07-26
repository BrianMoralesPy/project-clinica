package com.clinica.module.especialidad.controller;

import com.clinica.module.especialidad.dto.ActualizarEspecialidadRequest;
import com.clinica.module.especialidad.dto.CrearEspecialidadRequest;
import com.clinica.module.especialidad.dto.EspecialidadResponse;
import com.clinica.module.especialidad.service.EspecialidadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/especialidades")
@RequiredArgsConstructor
public class EspecialidadController {

    private final EspecialidadService especialidadService;

    @GetMapping
    public ResponseEntity<List<EspecialidadResponse>> findAll() {
        return ResponseEntity.ok(especialidadService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspecialidadResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(especialidadService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EspecialidadResponse> create(@Valid @RequestBody CrearEspecialidadRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(especialidadService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EspecialidadResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarEspecialidadRequest request) {
        return ResponseEntity.ok(especialidadService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        especialidadService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
