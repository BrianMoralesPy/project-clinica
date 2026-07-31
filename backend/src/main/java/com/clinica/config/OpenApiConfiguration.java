package com.clinica.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de OpenAPI (Swagger).
 *
 * Define la información que aparecerá en la documentación
 * interactiva de la API.
 */
@Configuration
public class OpenApiConfiguration {

        /**
         * Configura los datos generales de la documentación.
         */
        @Bean
        public OpenAPI customOpenAPI() {

        return new OpenAPI()
                // Información principal que se visualizará
                // en la interfaz de Swagger.
                .info(
                        new Info()
                                // Nombre de la API.
                                .title("Sistema de Gestión Clínica API")
                                // Versión actual.
                                .version("1.0.0")
                                // Breve descripción del proyecto.
                                .description(
                                        "API REST para la gestión de una clínica general"
                                )
                );
        }
}