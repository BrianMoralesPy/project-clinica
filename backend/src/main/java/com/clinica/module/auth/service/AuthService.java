package com.clinica.module.auth.service;

import com.clinica.exception.BadRequestException;
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

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthResponse register(RegistroRequest request) {
        if (usuarioRepository.existsByUsername(request.username())) {
            throw new BadRequestException("El username ya está en uso");
        }

        if (usuarioRepository.existsByEmail(request.email())) {
            throw new BadRequestException("El email ya está registrado");
        }

        Rol rolRecepcion = rolRepository.findByNombre("RECEPCION")
            .orElseThrow(() -> new BadRequestException("Rol RECEPCION no encontrado"));

        Usuario usuario = new Usuario();
        usuario.setUsername(request.username());
        usuario.setEmail(request.email());
        usuario.setPasswordHash(passwordEncoder.encode(request.password()));
        usuario.setNombre(request.nombre());
        usuario.setApellido(request.apellido());
        usuario.setEstado(EstadoUsuario.ACTIVO);
        usuario.setRoles(Set.of(rolRecepcion));

        usuario = usuarioRepository.save(usuario);

        String token = jwtTokenProvider.generateToken(usuario.getUsername());

        return new AuthResponse(
            token,
            "Bearer",
            86400L,
            AuthMapper.toUsuarioResponse(usuario)
        );
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.username(),
                request.password()
            )
        );

        String token = jwtTokenProvider.generateToken(authentication);

        Usuario usuario = usuarioRepository.findByUsername(request.username())
            .orElseThrow(() -> new BadRequestException("Usuario no encontrado"));

        return new AuthResponse(
            token,
            "Bearer",
            86400L,
            AuthMapper.toUsuarioResponse(usuario)
        );
    }

    @Transactional(readOnly = true)
    public AuthResponse.UsuarioResponse getCurrentUser(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new BadRequestException("Usuario no encontrado"));

        return AuthMapper.toUsuarioResponse(usuario);
    }
}
