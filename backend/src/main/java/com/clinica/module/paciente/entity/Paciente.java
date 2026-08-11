package com.clinica.module.paciente.entity;

import com.clinica.module.usuario.entity.Usuario;
import com.clinica.shared.BaseEntity;
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

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "id")
    private Usuario usuario;
    
    @Id
    private Long id;

    @Column(nullable = true, unique = true, length = 20)
    private String dni;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, length = 20)
    private Sexo sexo;

    @Column(length = 30)
    private String telefono;

    @Column(length = 200)
    private String direccion;

    @Enumerated(EnumType.STRING)
    @Column(name = "grupo_sanguineo", length = 10)
    private GrupoSanguineo grupoSanguineo;

    @Column(columnDefinition = "TEXT")
    private String alergias;

    @Column(name = "perfil_completo", nullable = false)
    private Boolean perfilCompleto = false;

}
