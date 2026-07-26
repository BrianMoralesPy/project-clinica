package com.clinica.module.turno.service;

import com.clinica.exception.BadRequestException;
import com.clinica.exception.ResourceNotFoundException;
import com.clinica.module.medico.entity.Medico;
import com.clinica.module.medico.repository.MedicoRepository;
import com.clinica.module.paciente.entity.Paciente;
import com.clinica.module.paciente.repository.PacienteRepository;
import com.clinica.module.turno.dto.ActualizarTurnoRequest;
import com.clinica.module.turno.dto.CompletarTurnoRequest;
import com.clinica.module.turno.dto.CrearTurnoRequest;
import com.clinica.module.turno.dto.TurnoResponse;
import com.clinica.module.turno.entity.Turno;
import com.clinica.module.turno.mapper.TurnoMapper;
import com.clinica.module.turno.repository.TurnoRepository;
import com.clinica.shared.EstadoTurno;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TurnoService {

    private final TurnoRepository turnoRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;

    @Transactional(readOnly = true)
    public Page<TurnoResponse> findAll(Long medicoId, Long pacienteId, EstadoTurno estado,
                                       Instant fechaDesde, Instant fechaHasta, Pageable pageable) {
        return turnoRepository.findByFilters(medicoId, pacienteId, estado, fechaDesde, fechaHasta, pageable)
            .map(TurnoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public TurnoResponse findById(Long id) {
        Turno turno = turnoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Turno", id));
        return TurnoMapper.toResponse(turno);
    }

    @Transactional
    public TurnoResponse create(CrearTurnoRequest request) {
        Paciente paciente = pacienteRepository.findById(request.pacienteId())
            .orElseThrow(() -> new ResourceNotFoundException("Paciente", request.pacienteId()));

        Medico medico = medicoRepository.findById(request.medicoId())
            .orElseThrow(() -> new ResourceNotFoundException("Medico", request.medicoId()));

        Turno turno = new Turno();
        turno.setPaciente(paciente);
        turno.setMedico(medico);
        turno.setFechaHora(request.fechaHora());
        turno.setDuracionMinutos(request.duracionMinutos() != null ? request.duracionMinutos() : 30);
        turno.setMotivo(request.motivo());

        turno = turnoRepository.save(turno);
        return TurnoMapper.toResponse(turno);
    }

    @Transactional
    public TurnoResponse update(Long id, ActualizarTurnoRequest request) {
        Turno turno = turnoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Turno", id));

        if (turno.getEstado() != EstadoTurno.PROGRAMADO && turno.getEstado() != EstadoTurno.CONFIRMADO) {
            throw new BadRequestException("No se puede reprogramar un turno con estado: " + turno.getEstado());
        }

        turno.setFechaHora(request.fechaHora());
        if (request.duracionMinutos() != null) {
            turno.setDuracionMinutos(request.duracionMinutos());
        }

        turno = turnoRepository.save(turno);
        return TurnoMapper.toResponse(turno);
    }

    @Transactional
    public TurnoResponse cancel(Long id) {
        Turno turno = turnoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Turno", id));

        if (turno.getEstado() == EstadoTurno.COMPLETADO || turno.getEstado() == EstadoTurno.CANCELADO) {
            throw new BadRequestException("No se puede cancelar un turno con estado: " + turno.getEstado());
        }

        turno.setEstado(EstadoTurno.CANCELADO);
        turno = turnoRepository.save(turno);
        return TurnoMapper.toResponse(turno);
    }

    @Transactional
    public TurnoResponse complete(Long id, CompletarTurnoRequest request) {
        Turno turno = turnoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Turno", id));

        if (turno.getEstado() == EstadoTurno.CANCELADO || turno.getEstado() == EstadoTurno.COMPLETADO) {
            throw new BadRequestException("No se puede completar un turno con estado: " + turno.getEstado());
        }

        turno.setEstado(EstadoTurno.COMPLETADO);
        turno.setObservaciones(request.observaciones());

        turno = turnoRepository.save(turno);
        return TurnoMapper.toResponse(turno);
    }

    @Transactional(readOnly = true)
    public long countByEstado(EstadoTurno estado) {
        return turnoRepository.countByEstado(estado);
    }
}
