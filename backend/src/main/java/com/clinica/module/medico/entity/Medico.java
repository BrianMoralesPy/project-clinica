package com.clinica.module.medico.entity;

import java.time.LocalDate;

import com.clinica.module.especialidad.entity.Especialidad;
import com.clinica.module.usuario.entity.Usuario;
import com.clinica.shared.BaseEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad que representa la información específica de un médico dentro del sistema.
 *
 * <p>
 * La entidad {@code Medico} complementa la información general almacenada en
 * {@link Usuario} con los datos propios de un profesional de la salud, como
 * DNI, matrícula, especialidad, teléfono y fecha de nacimiento.
 * </p>
 *
 * <p>
 * La entidad utiliza una relación {@link OneToOne} con {@link Usuario} mediante
 * un identificador compartido. Esto permite que el registro de médico utilice
 * el mismo {@code id} que su usuario asociado.
 * </p>
 *
 * <p>
 * Extiende {@link BaseEntity} para heredar los atributos comunes de auditoría
 * definidos para las entidades del sistema.
 * </p>
 */
@Entity
@Table(name = "medico")
@Getter
@Setter
public class Medico extends BaseEntity {

    /**
     * Usuario asociado al médico.
     *
     * <p>
     * La relación es uno a uno y utiliza {@link MapsId}, por lo que el
     * identificador de {@code Medico} es compartido con el {@link Usuario}
     * asociado.
     * </p>
     *
     * <p>
     * Se utiliza carga {@link FetchType#LAZY} para evitar cargar automáticamente
     * la información completa del usuario cuando no sea necesaria.
     * </p>
     */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "id")
    private Usuario usuario;

    /**
     * Identificador del médico.
     *
     * <p>
     * Este identificador coincide con el {@code id} del usuario asociado,
     * debido al uso de {@link MapsId}.
     * </p>
     */
    @Id
    private Long id;

    /**
     * Especialidad médica a la que pertenece el profesional.
     *
     * <p>
     * Un médico posee una única especialidad, mientras que una especialidad
     * puede estar asociada a múltiples médicos.
     * </p>
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "especialidad_id", nullable = false)
    private Especialidad especialidad;

    /**
     * Documento nacional de identidad del médico.
     *
     * <p>
     * Es obligatorio y único dentro del sistema.
     * </p>
     */
    @Column(nullable = false, unique = true, length = 20)
    private String dni;

    /**
     * Matrícula profesional del médico.
     *
     * <p>
     * Es obligatoria y única, ya que identifica profesionalmente al médico
     * dentro del sistema.
     * </p>
     */
    @Column(nullable = false, unique = true, length = 50)
    private String matricula;

    /**
     * Número de teléfono de contacto del médico.
     *
     * <p>
     * Este dato es opcional.
     * </p>
     */
    @Column(length = 30)
    private String telefono;

    /**
     * Fecha de nacimiento del médico.
     *
     * <p>
     * Es un dato obligatorio y se almacena en la columna
     * {@code fecha_nacimiento}.
     * </p>
     */
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;
}
