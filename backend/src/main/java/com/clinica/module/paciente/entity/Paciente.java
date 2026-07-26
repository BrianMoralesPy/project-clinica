package com.clinica.module.paciente.entity;

import com.clinica.shared.BaseEntity;
import com.clinica.shared.EstadoPaciente;
import com.clinica.shared.GrupoSanguineo;
import com.clinica.shared.Sexo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "paciente")
@Getter
@Setter
public class Paciente extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @Column(nullable = false, unique = true, length = 20)
    private String dni;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Sexo sexo;

    @Column(length = 30)
    private String telefono;

    @Column(length = 100)
    private String email;

    @Column(length = 200)
    private String direccion;

    @Enumerated(EnumType.STRING)
    @Column(name = "grupo_sanguineo", length = 10)
    private GrupoSanguineo grupoSanguineo;

    @Column(columnDefinition = "TEXT")
    private String alergias;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPaciente estado = EstadoPaciente.ACTIVO;
}
