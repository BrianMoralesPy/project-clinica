package com.clinica.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Maneja de forma centralizada todas las excepciones lanzadas
 * por la aplicación.
 *
 * Evita tener bloques try/catch en cada controlador y garantiza
 * que todas las respuestas de error tengan un formato consistente.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * Captura las excepciones cuando un recurso no existe.
     *
     * Devuelve:
     * HTTP 404
    */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        // Construye el cuerpo de la respuesta que recibirá el frontend.
        Map<String, Object> body = new LinkedHashMap<>();

        // Fecha y hora del error.
        body.put("timestamp", LocalDateTime.now());

        // Código HTTP.
        body.put("status", HttpStatus.NOT_FOUND.value());

        // Descripción del estado HTTP.
        body.put("error", "Not Found");

        // Mensaje específico de la excepción.
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    /**
     * Captura errores relacionados con reglas de negocio.
     *
     * Devuelve:
     * HTTP 400
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(BadRequestException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * Se ejecuta cuando las credenciales de inicio de sesión
     * son incorrectas.
     *
     * Esta excepción es lanzada automáticamente por Spring Security.
     *
     * Devuelve:
     * HTTP 401
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialsException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", HttpStatus.UNAUTHORIZED.value());
        body.put("error", "Unauthorized");
        body.put("message", "Credenciales incorrectas");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    /**
     * Se ejecuta cuando el usuario está autenticado
     * pero no posee permisos suficientes para acceder
     * al recurso solicitado.
     *
     * Devuelve:
     * HTTP 403
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", HttpStatus.FORBIDDEN.value());
        body.put("error", "Forbidden");
        body.put("message", "No tiene permisos para realizar esta acción");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    /**
     * Captura los errores producidos por las anotaciones
     * de validación (@NotBlank, @Email, @Size, etc.).
     *
     * Devuelve un mapa con cada campo y su mensaje de error.
     *
     * Ejemplo:
     * {
     *   "nombre":"El nombre es obligatorio",
     *   "email":"Email inválido"
     * }
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validation Error");
        // Recorre todos los errores de validación encontrados y los agrega al mapa para enviarlos al frontend.
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        body.put("errors", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * Captura cualquier excepción que no haya sido manejada
     * por los métodos anteriores.
     *
     * Actúa como último nivel de protección para evitar
     * que el servidor exponga información interna.
     *
     * Devuelve:
     * HTTP 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        // Imprime el stacktrace completo en la consola
        ex.printStackTrace();
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", "Error interno del servidor");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
