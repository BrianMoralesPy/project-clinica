package com.clinica.module.dashboard.service;

import com.clinica.module.dashboard.dto.DashboardResponse;
import com.clinica.module.especialidad.repository.EspecialidadRepository;
import com.clinica.module.medico.repository.MedicoRepository;
import com.clinica.module.paciente.repository.PacienteRepository;
import com.clinica.module.turno.repository.TurnoRepository;
import com.clinica.shared.EstadoTurno;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final EspecialidadRepository especialidadRepository;
    private final TurnoRepository turnoRepository;

    @Transactional(readOnly = true)
    public DashboardResponse getDashboard() {
        Instant inicioHoy = LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant finHoy = LocalDate.now().plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();

        return new DashboardResponse(
            pacienteRepository.count(),
            medicoRepository.count(),
            especialidadRepository.count(),
            turnoRepository.count(),
            turnoRepository.countByEstado(EstadoTurno.PROGRAMADO) + turnoRepository.countByEstado(EstadoTurno.CONFIRMADO),
            turnoRepository.countByEstado(EstadoTurno.COMPLETADO)
        );
    }
}
