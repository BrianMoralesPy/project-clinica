package com.clinica.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro encargado de autenticar cada petición HTTP mediante JWT.
 *
 * Su responsabilidad es:
 * - Extraer el token del encabezado Authorization.
 * - Validar que el token sea correcto.
 * - Obtener el usuario asociado.
 * - Registrar el usuario autenticado en el contexto de Spring Security.
 *
 * Se ejecuta una única vez por cada petición.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * Utilidad para generar y validar tokens JWT.
     */
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Servicio encargado de cargar el usuario desde la base de datos.
     */
    private final UserDetailsService userDetailsService;

    /**
     * Intercepta todas las peticiones HTTP antes de que lleguen
     * al controlador.
     *
     * Si el JWT es válido, autentica al usuario en Spring Security.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain filterChain) throws ServletException, IOException {
        // Obtiene el JWT enviado en el encabezado Authorization.
        String token = extractToken(request);
        // Solo intenta autenticar si existe un token y éste es válido.
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            // Extrae el nombre de usuario almacenado dentro del JWT.
            String username = jwtTokenProvider.getUsernameFromToken(token);
            // Recupera el usuario y sus permisos desde la base de datos.
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            // Crea el objeto de autenticación que utilizará Spring Security.
            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
                );
            // Agrega información adicional de la petición (IP, sesión, etc.).
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // Registra al usuario como autenticado para el resto de la petición.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Continúa la ejecución hacia el siguiente filtro o controlador.
        filterChain.doFilter(request, response);
    }

    /**
     * Extrae el JWT del encabezado Authorization.
     *
     * Espera el siguiente formato:
     *
     * Authorization: Bearer eyJhbGciOi...
     *
     * Si el encabezado no existe o el formato es inválido,
     * devuelve null.
     */
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
