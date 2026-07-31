package com.clinica.config;

import com.clinica.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración principal de Spring Security.
 *
 * Define:
 * - Qué rutas requieren autenticación.
 * - Qué rutas son públicas.
 * - Cómo se validan los JWT.
 * - Cómo se encriptan las contraseñas.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
        /**
         * Filtro encargado de validar el JWT enviado
         * en cada petición antes de que llegue al controlador.
         */
        private final JwtAuthenticationFilter jwtAuthenticationFilter;

        /**
         * Configura las reglas de seguridad de toda la aplicación.
         */
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception {

                return http

                        // La API utiliza JWT, por lo que no necesita protección CSRF.
                        .csrf(AbstractHttpConfigurer::disable)

                        // Nunca se almacenarán sesiones en el servidor.
                        .sessionManagement(session ->
                                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        )

                        .authorizeHttpRequests(auth -> auth

                                // Endpoints públicos para autenticación.
                                .requestMatchers("/api/auth/**").permitAll()

                                // Documentación pública.
                                .requestMatchers(
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**"
                                ).permitAll()

                                // Actuator público (opcional según el entorno).
                                .requestMatchers("/actuator/**").permitAll()

                                // Permite las solicitudes OPTIONS utilizadas por CORS.
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                                // Cualquier otra petición requiere autenticación.
                                .anyRequest().authenticated()
                        )

                        // El filtro JWT se ejecutará antes del filtro
                        // de autenticación estándar de Spring Security.
                        .addFilterBefore(
                                jwtAuthenticationFilter,
                                UsernamePasswordAuthenticationFilter.class
                        )

                        .build();
        }

        /**
         * Expone el AuthenticationManager como un Bean para
         * poder utilizarlo durante el proceso de login.
         */
        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)throws Exception {
                return configuration.getAuthenticationManager();
        }

        /**
         * Encoder utilizado para almacenar las contraseñas
         * de forma segura utilizando BCrypt.
         *
         * Nunca se guarda una contraseña en texto plano.
         */
        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}
