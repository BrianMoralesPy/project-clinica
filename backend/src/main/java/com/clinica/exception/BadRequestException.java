package com.clinica.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción utilizada cuando el cliente envía una solicitud inválida
 * desde el punto de vista de las reglas de negocio.
 *
 * Ejemplos:
 * - DNI duplicado.
 * - Email ya registrado.
 * - Turno no disponible.
 *
 * Devuelve automáticamente un HTTP 400 (Bad Request).
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    /**
     * Crea una excepción con el mensaje que será enviado al cliente.
     */
    public BadRequestException(String message) {
        super(message);
    }
}