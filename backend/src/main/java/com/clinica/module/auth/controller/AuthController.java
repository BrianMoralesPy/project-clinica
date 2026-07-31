package com.clinica.module.auth.controller;

import com.clinica.module.auth.dto.AuthResponse;
import com.clinica.module.auth.dto.LoginRequest;
import com.clinica.module.auth.dto.RegistroRequest;
import com.clinica.module.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador encargado de exponer los endpoints
 * relacionados con la autenticación de usuarios.
 *
 * Responsabilidades:
 * - Registrar nuevos usuarios.
 * - Autenticar usuarios.
 * - Obtener la información del usuario autenticado.
 *
 * Este controlador no contiene lógica de negocio;
 * simplemente delega las operaciones al AuthService.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    /**
     * Servicio que contiene toda la lógica de autenticación.
     */
    private final AuthService authService;

    /**
     * Registra un nuevo paciente.
     *
     * El cuerpo de la petición se valida automáticamente
     * mediante las anotaciones de Bean Validation presentes
     * en RegistroRequest.
     *
     * Si el registro es exitoso:
     * - crea el usuario.
     * - genera un JWT.
     * - devuelve HTTP 201 (Created).
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegistroRequest request) {

        AuthResponse response = authService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Autentica un usuario utilizando username y contraseña.
     *
     * Si las credenciales son válidas, devuelve un JWT que
     * deberá enviarse en las siguientes peticiones mediante
     * el encabezado Authorization.
     *
     * Devuelve HTTP 200 (OK).
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {

        AuthResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene la información del usuario actualmente autenticado.
     *
     * El objeto Authentication es proporcionado automáticamente
     * por Spring Security luego de que JwtAuthenticationFilter
     * valide correctamente el JWT recibido en la petición.
     *
     * authentication.getName() devuelve el username del usuario
     * autenticado.
     */
    @GetMapping("/me")
    public ResponseEntity<AuthResponse.UsuarioResponse> getCurrentUser(
            Authentication authentication) {

        AuthResponse.UsuarioResponse response = authService.getCurrentUser(authentication.getName());

        return ResponseEntity.ok(response);
    }
}