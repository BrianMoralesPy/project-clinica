package com.clinica.module.auth.service;
import com.clinica.exception.ResourceNotFoundException;

import com.clinica.module.auth.dto.AuthResponse;
import com.clinica.module.auth.dto.LoginRequest;
import com.clinica.module.auth.dto.RegistroRequest;
import com.clinica.module.auth.mapper.AuthMapper;
import com.clinica.module.usuario.entity.Usuario;
import com.clinica.module.usuario.repository.UsuarioRepository;
import com.clinica.module.paciente.service.PacienteRegistrationService;

import com.clinica.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio encargado de gestionar la autenticación
 * y el registro de usuarios.
 *
 * Responsabilidades:
 * - Registrar nuevos usuarios.
 * - Validar credenciales durante el login.
 * - Generar el JWT para usuarios autenticados.
 * - Obtener la información del usuario autenticado.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    /**
     * Gestiona el proceso de autenticación de Spring Security.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * Genera y valida los tokens JWT.
     */
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Repositorio de usuarios.
     */
    private final UsuarioRepository usuarioRepository;

    /**
     * Servicio para crear pacientes.
     */
    private final PacienteRegistrationService pacienteRegistrationService;

    /**
     * Registra un nuevo usuario y crea automáticamente
     * su ficha de paciente asociada.
     *
     * @param request DTO con los datos del nuevo usuario.
     * @return DTO con el token JWT y la información del usuario.
     */
    @Transactional
    public AuthResponse register(RegistroRequest request) {

        Usuario usuario = pacienteRegistrationService.crearPacienteIncompleto(request.username(),request.email(),request.password(),request.nombre(),request.apellido());

        String token = jwtTokenProvider.generateToken(usuario.getUsername());

        return new AuthResponse(token,"Bearer",86400L,AuthMapper.toUsuarioResponse(usuario));
    }
    /**
     * Autentica un usuario utilizando Spring Security.
     *
     * Si las credenciales son correctas:
     * - genera un JWT.
     * - devuelve la información del usuario.
     *
     * Si son incorrectas, Spring Security lanzará
     * BadCredentialsException.
     */
    public AuthResponse login(LoginRequest request) {
        // Delega la validación de usuario o email y contraseña al AuthenticationManager de Spring Security.
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.identifier(), request.password())
        );
        // Recupera la información completa del usuario autenticado desde la base de datos.
        String token = jwtTokenProvider.generateToken(authentication);
        // Recupera el username del usuario autenticado.
        String username = authentication.getName();
        // Busca el usuario por su username.
        Usuario usuario = usuarioRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        
        // Genera un JWT firmado que será utilizado en las siguientes peticiones.
        return new AuthResponse(token,"Bearer",86400L,AuthMapper.toUsuarioResponse(usuario));
    }
    /**
     * Obtiene la información del usuario actualmente autenticado.
     *
     * El username es obtenido previamente por el controlador desde
     * el contexto de Spring Security (Authentication).
     *
     * La transacción es de solo lectura porque únicamente consulta
     * información de la base de datos.
     */
    @Transactional(readOnly = true)
    public AuthResponse.UsuarioResponse getCurrentUser(String username) {
        // Busca el usuario por su nombre de usuario.
        Usuario usuario = usuarioRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        // Convierte la entidad Usuario en el DTO que será enviado al frontend
        return AuthMapper.toUsuarioResponse(usuario);
    }
}
