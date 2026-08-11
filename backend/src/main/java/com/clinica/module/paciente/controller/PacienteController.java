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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;
    // ENDPOINTS ADMINISTRATIVOS Y RECEPCION
    
    // BUSCAR TODOS LOS PACIENTES
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<Page<PacienteResponse>> findAll(@RequestParam(required = false) String search,@RequestParam(required = false) EstadoUsuario estado,Pageable pageable) {
        return ResponseEntity.ok(pacienteService.findAll(search, estado, pageable));
    }
    // BUSCAR PACIENTE POR ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION', 'MEDICO')")
    public ResponseEntity<PacienteResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(pacienteService.findById(id));
    }
    // CREAR PACIENTE ADMIN
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<PacienteResponse> create(@Valid @RequestBody CrearPacienteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteService.create(request));
    }
    // ACTUALIZAR SOLO LOS DATOS DE LA TABLA PACIENTE
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<PacienteResponse> update(@PathVariable Long id, @Valid @RequestBody ActualizarPacienteRequest request) {
        return ResponseEntity.ok(pacienteService.update(id, request));
    }
    
    //ACTUALIZAR LOS DATOS DE LA TABLA PACIENTE Y LOS DATOS DEL USUARIO ASOCIADO
    @PutMapping("/full/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCION')")
    public ResponseEntity<PacienteResponse> update(@PathVariable Long id, @Valid @RequestBody ActualizarPacienteYUsuarioRequest request) {
        return ResponseEntity.ok(pacienteService.update(id, request.pacienteRequest(), request.usuarioRequest()));
    } 

    // SOFT DELETE PACIENTE (CAMBIA EL ESTADO DEL USUARIO ASOCIADO A INACTIVO)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pacienteService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    

    //ENDPOINTS AUTOSERVICIO PACIENTE
    // PERMITE AL PACIENTE COMPLETAR SU PERFIL
    @PutMapping("/me")
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<PacienteResponse> completarMiPerfil(@Valid @RequestBody ActualizarPacienteRequest request) {
        return ResponseEntity.ok(pacienteService.completarMiPerfil(request));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<PacienteResponse> getMiPerfil() {
        return ResponseEntity.ok(pacienteService.getMiPerfil());
        // return ResponseEntity.ok(pacienteService.getMiPerfil());
        
    }
    

}
