package com.clinica.module.usuario.controller;

import com.clinica.module.usuario.dto.ActualizarUsuarioRequest;
import com.clinica.module.usuario.dto.CambiarEstadoRequest;
import com.clinica.module.usuario.dto.UsuarioResponse;
import com.clinica.module.usuario.service.UsuarioService;
import com.clinica.shared.EstadoUsuario;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UsuarioResponse>> findAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) EstadoUsuario estado,
            Pageable pageable) {
        return ResponseEntity.ok(usuarioService.findAll(search, estado, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarUsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.update(id, request));
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> changeStatus(
            @PathVariable Long id,
            @Valid @RequestBody CambiarEstadoRequest request) {
        return ResponseEntity.ok(usuarioService.changeStatus(id, request));
    }
}
