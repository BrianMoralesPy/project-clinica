package com.clinica.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción utilizada cuando un recurso solicitado
 * no existe en la base de datos.
 *
 * Ejemplos:
 * - Paciente inexistente.
 * - Médico inexistente.
 * - Turno inexistente.
 *
 * Devuelve automáticamente un HTTP 404 (Not Found).
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructor que recibe un mensaje personalizado.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor que genera automáticamente un mensaje
     * indicando el recurso y su identificador.
     *
     * Ejemplo:
     * "Paciente con id 15 no encontrado"
     */
    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s con id %d no encontrado", resourceName, id));
    }
}