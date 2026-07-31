package com.clinica.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configuración global de CORS (Cross-Origin Resource Sharing).
 *
 * Permite que el frontend (Angular) pueda consumir esta API
 * aunque se ejecute en un origen diferente (por ejemplo,
 * localhost:4200 mientras el backend corre en localhost:8080).
 */
@Configuration
public class CorsConfig {

    /**
     * Define la configuración CORS que se aplicará
     * a todos los endpoints de la aplicación.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        // Objeto donde se definen todas las reglas CORS.
        CorsConfiguration configuration = new CorsConfiguration();

        // Orígenes permitidos para consumir la API.
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));

        // Métodos HTTP que podrá utilizar el frontend.
        configuration.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));

        // Permite enviar cualquier encabezado HTTP.
        configuration.setAllowedHeaders(List.of("*"));

        // Permite enviar credenciales como cookies o JWT.
        configuration.setAllowCredentials(true);

        // El navegador recordará esta configuración durante una hora.
        configuration.setMaxAge(3600L);

        // Aplica esta configuración a todos los endpoints de la API.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}