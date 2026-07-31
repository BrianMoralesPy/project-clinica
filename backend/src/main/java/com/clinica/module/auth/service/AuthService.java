package com.clinica.module.auth.service;

import com.clinica.exception.BadRequestException;
import com.clinica.exception.ResourceNotFoundException;
import com.clinica.module.auth.dto.AuthResponse;
import com.clinica.module.auth.dto.LoginRequest;
import com.clinica.module.auth.dto.RegistroRequest;
import com.clinica.module.auth.mapper.AuthMapper;
import com.clinica.module.usuario.entity.Rol;
import com.clinica.module.usuario.entity.Usuario;
import com.clinica.module.usuario.repository.RolRepository;
import com.clinica.module.usuario.repository.UsuarioRepository;
import com.clinica.security.JwtTokenProvider;
import com.clinica.shared.EstadoUsuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

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
     * Repositorio de roles.
     */
    private final RolRepository rolRepository;

    /**
     * Encripta las contraseñas antes de almacenarlas.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * Flujo:
     * 1. Verifica que el username no exista.
     * 2. Verifica que el email no exista.
     * 3. Obtiene el rol por defecto.
     * 4. Encripta la contraseña.
     * 5. Guarda el usuario.
     * 6. Genera un JWT.
     * 7. Devuelve la información para iniciar sesión.
     */
    @Transactional
    public AuthResponse register(RegistroRequest request) {
        // Verifica que el nombre de usuario no esté registrado.
        if (usuarioRepository.existsByUsername(request.username())) {
            throw new BadRequestException("El username ya está en uso");
        }
        // Verifica que el correo electrónico sea único.
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new BadRequestException("El email ya está registrado");
        }
        // Obtiene el rol por defecto asignado a los nuevos usuarios.
        Rol rolPaciente = rolRepository.findByNombre("PACIENTE")
            .orElseThrow(() -> new BadRequestException("Rol PACIENTE no encontrado"));
        
        Usuario usuario = new Usuario();
        usuario.setUsername(request.username());
        usuario.setEmail(request.email());
        // Encripta la contraseña antes de almacenarla en la base de datos.
        usuario.setPasswordHash(passwordEncoder.encode(request.password())); 
        usuario.setNombre(request.nombre());
        usuario.setApellido(request.apellido());
        usuario.setEstado(EstadoUsuario.ACTIVO);
        usuario.setRoles(Set.of(rolPaciente));
        // Persiste el nuevo usuario.
        usuario = usuarioRepository.save(usuario); 
        // Genera un token para que el usuario quede autenticado inmediatamente después del registro.
        String token = jwtTokenProvider.generateToken(usuario.getUsername());
        // Convierte la entidad Usuario en la respuesta que consumirá el frontend.
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
