package com.clinica.module.usuario.entity;

import com.clinica.shared.BaseEntity;
import com.clinica.shared.EstadoUsuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Entidad que representa a un usuario del sistema.
 *
 * <p>
 * Un usuario contiene la información necesaria para identificar,
 * autenticar y gestionar una cuenta dentro de la aplicación.
 * Además, puede tener uno o varios roles que determinan los permisos
 * y funcionalidades a las que tiene acceso.
 * </p>
 *
 * <p>
 * La entidad hereda de {@link BaseEntity} para reutilizar los atributos
 * y comportamiento comunes definidos para las entidades del sistema.
 * </p>
 *
 * <p>
 * La relación con {@link Rol} es de muchos a muchos, ya que un usuario
 * puede tener múltiples roles y un mismo rol puede estar asignado
 * a múltiples usuarios. Esta relación se persiste mediante la tabla
 * intermedia {@code usuario_rol}.
 * </p>
 *
 * <p>
 * Esta entidad pertenece a la capa de persistencia y representa el
 * modelo de datos del usuario. Las reglas de negocio, validaciones
 * específicas y operaciones sobre la cuenta son responsabilidad
 * de las capas correspondientes.
 * </p>
 */
@Entity
@Table(name = "usuario")
@Getter
@Setter
public class Usuario extends BaseEntity {

    /**
     * Identificador único del usuario.
     *
     * <p>
     * El valor es generado automáticamente por la base de datos
     * mediante una estrategia de identidad.
     * </p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de usuario utilizado para identificar la cuenta
     * dentro del sistema.
     *
     * <p>
     * El valor es obligatorio y no puede repetirse entre usuarios.
     * </p>
     */
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    /**
     * Dirección de correo electrónico asociada a la cuenta.
     *
     * <p>
     * El email es obligatorio y debe ser único dentro del sistema.
     * </p>
     */
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    /**
     * Hash de la contraseña del usuario.
     *
     * <p>
     * Se almacena el resultado de aplicar un algoritmo de hash
     * a la contraseña, nunca la contraseña en texto plano.
     * </p>
     *
     * <p>
     * Este campo contiene información sensible y no debe ser
     * expuesto directamente en los DTOs de respuesta de la API.
     * </p>
     */
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    /**
     * Nombre del usuario.
     */
    @Column(nullable = false, length = 100)
    private String nombre;

    /**
     * Apellido del usuario.
     */
    @Column(nullable = false, length = 100)
    private String apellido;

    /**
     * Estado actual de la cuenta del usuario.
     *
     * <p>
     * Al crear una nueva instancia, el estado se establece
     * automáticamente como {@link EstadoUsuario#ACTIVO}.
     * </p>
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoUsuario estado = EstadoUsuario.ACTIVO;

    /**
     * Roles asignados al usuario.
     *
     * <p>
     * La relación es de muchos a muchos: un usuario puede poseer
     * varios roles y un mismo rol puede pertenecer a varios usuarios.
     * </p>
     *
     * <p>
     * La relación utiliza la tabla intermedia {@code usuario_rol},
     * que contiene las claves foráneas correspondientes al usuario
     * y al rol.
     * </p>
     *
     * <p>
     * Se utiliza {@link FetchType#EAGER} para que los roles estén
     * disponibles al cargar el usuario, algo especialmente relevante
     * para los procesos de autenticación y autorización que necesitan
     * conocer los roles asociados.
     * </p>
     *
     * <p>
     * La colección se inicializa como un {@link HashSet} para evitar
     * valores duplicados y permitir agregar o quitar roles directamente
     * sobre la entidad.
     * </p>
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuario_rol",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<Rol> roles = new HashSet<>();
}