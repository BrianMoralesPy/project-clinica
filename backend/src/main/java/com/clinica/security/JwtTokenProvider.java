package com.clinica.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Clase responsable de todas las operaciones relacionadas
 * con los JWT.
 *
 * Funciones principales:
 * - Generar tokens.
 * - Validar tokens.
 * - Obtener información almacenada dentro del token.
 */
@Component
public class JwtTokenProvider {

    /**
     * Clave secreta utilizada para firmar y validar los JWT.
     */
    @Value("${jwt.secret}")
    private String jwtSecret;

    /**
     * Tiempo de vida del token en milisegundos.
     */
    @Value("${jwt.expiration}")
    private long jwtExpiration;

    /**
     * Genera un JWT a partir del usuario autenticado.
     */
    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return generateToken(userDetails.getUsername());
    }

    /**
     * Construye un JWT firmado utilizando:
     *
     * - Usuario.
     * - Fecha de creación.
     * - Fecha de expiración.
     * - Clave secreta.
     */
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        // Construye el contenido del JWT.
        return Jwts.builder()

                // Usuario propietario del token.
                .subject(username)

                // Fecha de creación.
                .issuedAt(now)

                // Fecha de expiración.
                .expiration(expiryDate)

                // Firma digital del token.
                .signWith(getSigningKey())

                // Genera el JWT final.
                .compact();
    }

    /**
     * Obtiene el nombre de usuario almacenado
     * dentro del JWT.
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
        return claims.getSubject();
    }

    /**
     * Comprueba que el token:
     *
     * - Esté correctamente firmado.
     * - No haya expirado.
     * - No haya sido modificado.
     *
     * Devuelve true únicamente si el token es válido.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
            return true;
        } 
        // Cualquier excepción indica que el JWT no es válido.
        catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Convierte la clave configurada en application.properties
     * al formato requerido por la librería JWT.
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
