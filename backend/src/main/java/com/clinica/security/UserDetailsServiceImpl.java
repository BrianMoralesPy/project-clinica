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

/**
 * Implementación de UserDetailsService utilizada por
 * Spring Security para obtener usuarios desde la base de datos.
 *
 * Se ejecuta durante el login y cada vez que se valida un JWT.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    /**
     * Repositorio encargado de buscar usuarios.
     */
    private final UsuarioRepository usuarioRepository;

    /**
     * Busca un usuario por su nombre de usuario o email, la funcion dice ByUsername pero es para ambos
     *
     * Si existe, lo transforma al formato que entiende
     * Spring Security (UserDetails).
     */
    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        // Busca el usuario en la base de datos.
        Usuario usuario = usuarioRepository.findByUsernameOrEmail(identifier, identifier).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + identifier));
        // Convierte los roles del usuario en autoridades reconocidas por Spring Security.
        var authorities = usuario.getRoles().stream().map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getNombre())).collect(Collectors.toList());
        return new User(usuario.getUsername(),usuario.getPasswordHash(),usuario.getEstado().name().equals("ACTIVO"),true,true,true,authorities);
    }
}
