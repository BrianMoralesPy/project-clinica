package com.clinica.module.turno.entity;

import com.clinica.module.medico.entity.Medico;
import com.clinica.module.paciente.entity.Paciente;
import com.clinica.shared.BaseEntity;
import com.clinica.shared.EstadoTurno;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "turno")
@Getter
@Setter
public class Turno extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @Column(name = "fecha_hora", nullable = false)
    private Instant fechaHora;

    @Column(name = "duracion_minutos", nullable = false)
    private Integer duracionMinutos = 30;

    @Column(length = 300)
    private String motivo;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoTurno estado = EstadoTurno.PROGRAMADO;
}
