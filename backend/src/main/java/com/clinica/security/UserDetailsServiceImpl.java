package com.clinica.security;

import com.clinica.module.usuario.entity.Usuario;
import com.clinica.module.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(
                "Usuario no encontrado: " + username
            ));

        var authorities = usuario.getRoles().stream()
            .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getNombre()))
            .collect(Collectors.toList());

        return new User(
            usuario.getUsername(),
            usuario.getPasswordHash(),
            usuario.getEstado().name().equals("ACTIVO"),
            true,
            true,
            true,
            authorities
        );
    }
}
