package com.clinica.module.paciente.entity;

import com.clinica.module.usuario.entity.Usuario;
import com.clinica.shared.BaseEntity;
import com.clinica.shared.GrupoSanguineo;
import com.clinica.shared.Sexo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Entidad que representa la información específica de un paciente
 * dentro del sistema.
 *
 * <p>
 * Un paciente mantiene una relación uno a uno con {@link Usuario},
 * ya que cada paciente posee una cuenta de usuario asociada para
 * autenticarse y acceder a las funcionalidades correspondientes.
 * </p>
 *
 * <p>
 * La entidad hereda de {@link BaseEntity} para reutilizar los atributos
 * y comportamiento comunes definidos para las entidades del sistema.
 * </p>
 *
 * <p>
 * El identificador del paciente coincide con el identificador del usuario
 * asociado mediante {@link MapsId}. Esto establece una relación de identidad
 * compartida: el {@code id} del paciente no se genera de manera independiente,
 * sino que utiliza el mismo valor que el {@code id} de {@link Usuario}.
 * </p>
 *
 * <p>
 * Esta entidad pertenece a la capa de persistencia y su responsabilidad
 * principal es representar el modelo de datos del paciente. Las reglas
 * de negocio y las operaciones sobre estos datos son responsabilidad
 * de la capa de servicio.
 * </p>
 */
@Entity
@Table(name = "paciente")
@Getter
@Setter
public class Paciente extends BaseEntity {

    /**
     * Usuario asociado al paciente.
     *
     * <p>
     * La relación es uno a uno y utiliza carga diferida ({@link FetchType#LAZY})
     * para evitar cargar los datos completos del usuario cuando no son necesarios.
     * </p>
     *
     * <p>
     * {@link MapsId} indica que esta entidad comparte su identificador
     * con el usuario asociado. Por lo tanto, el {@code id} del paciente
     * corresponde al {@code id} del usuario.
     * </p>
     *
     * <p>
     * La relación es obligatoria, ya que un paciente debe estar asociado
     * a una cuenta de usuario.
     * </p>
     */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "id")
    private Usuario usuario;

    /**
     * Identificador del paciente.
     *
     * <p>
     * Este identificador es compartido con la entidad {@link Usuario}
     * debido al uso de {@link MapsId}.
     * </p>
     */
    @Id
    private Long id;

    /**
     * Documento Nacional de Identidad del paciente.
     *
     * <p>
     * El campo es opcional, pero cuando se informa debe ser único
     * dentro de la tabla de pacientes.
     * </p>
     */
    @Column(nullable = true, unique = true, length = 20)
    private String dni;

    /**
     * Fecha de nacimiento del paciente.
     */
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    /**
     * Sexo del paciente.
     *
     * <p>
     * Se almacena como texto en la base de datos mediante
     * {@link EnumType#STRING}, evitando depender de la posición
     * ordinal del enum.
     * </p>
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = true, length = 20)
    private Sexo sexo;

    /**
     * Número de teléfono de contacto del paciente.
     */
    @Column(length = 30)
    private String telefono;

    /**
     * Dirección de residencia del paciente.
     */
    @Column(length = 200)
    private String direccion;

    /**
     * Grupo sanguíneo del paciente.
     *
     * <p>
     * Se almacena como texto en lugar de utilizar el valor ordinal
     * del enum para mantener estabilidad ante posibles modificaciones
     * en la definición de {@link GrupoSanguineo}.
     * </p>
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "grupo_sanguineo", length = 10)
    private GrupoSanguineo grupoSanguineo;

    /**
     * Información sobre alergias conocidas del paciente.
     *
     * <p>
     * Se utiliza un campo de tipo TEXT para permitir almacenar
     * una cantidad de información mayor que la permitida por una
     * columna VARCHAR convencional.
     * </p>
     */
    @Column(columnDefinition = "TEXT")
    private String alergias;

    /**
     * Indica si el paciente completó la información requerida de su perfil.
     *
     * <p>
     * Se inicializa en {@code false}, ya que al momento de crear
     * el paciente se considera que su perfil todavía no está completo.
     * La actualización de este estado debe ser gestionada por la
     * lógica de negocio correspondiente.
     * </p>
     */
    @Column(name = "perfil_completo", nullable = false)
    private Boolean perfilCompleto = false;

}