package com.clinica.module.archivo.controller;

import com.clinica.module.archivo.dto.ArchivoAdjuntoResponse;
import com.clinica.module.archivo.service.ArchivoService;
import com.clinica.shared.EntidadTipo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/archivos")
@RequiredArgsConstructor
public class ArchivoController {

    private final ArchivoService archivoService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION', 'MEDICO')")
    public ResponseEntity<ArchivoAdjuntoResponse> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("entidadTipo") EntidadTipo entidadTipo,
            @RequestParam("entidadId") Long entidadId,
            @RequestParam("bucket") String bucket,
            Authentication authentication) throws IOException {

        Long subidoPorId = null;
        if (authentication.getPrincipal() instanceof com.clinica.module.usuario.entity.Usuario usuario) {
            subidoPorId = usuario.getId();
        }

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(archivoService.upload(file, entidadTipo, entidadId, bucket, subidoPorId));
    }

    @GetMapping
    public ResponseEntity<List<ArchivoAdjuntoResponse>> findByEntidad(
            @RequestParam EntidadTipo entidadTipo,
            @RequestParam Long entidadId) {
        return ResponseEntity.ok(archivoService.findByEntidad(entidadTipo, entidadId));
    }

    @GetMapping("/{id}/url")
    public ResponseEntity<Map<String, String>> getFileUrl(@PathVariable Long id) {
        String url = archivoService.getFileUrl(id);
        return ResponseEntity.ok(Map.of("url", url));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        archivoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
