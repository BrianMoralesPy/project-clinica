package com.clinica.module.medico.service;

import com.clinica.exception.BadRequestException;
import com.clinica.exception.ResourceNotFoundException;
import com.clinica.module.especialidad.entity.Especialidad;
import com.clinica.module.especialidad.repository.EspecialidadRepository;
import com.clinica.module.medico.dto.ActualizarMedicoRequest;
import com.clinica.module.medico.dto.CrearMedicoRequest;
import com.clinica.module.medico.dto.MedicoResponse;
import com.clinica.module.medico.entity.Medico;
import com.clinica.module.medico.mapper.MedicoMapper;
import com.clinica.module.medico.repository.MedicoRepository;
import com.clinica.shared.EstadoMedico;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MedicoService {

    private final MedicoRepository medicoRepository;
    private final EspecialidadRepository especialidadRepository;

    @Transactional(readOnly = true)
    public Page<MedicoResponse> findAll(Long especialidadId, EstadoMedico estado, Pageable pageable) {
        return medicoRepository.findByFilters(especialidadId, estado, pageable)
            .map(MedicoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public MedicoResponse findById(Long id) {
        Medico medico = medicoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Medico", id));
        return MedicoMapper.toResponse(medico);
    }

    @Transactional
    public MedicoResponse create(CrearMedicoRequest request) {
        if (medicoRepository.existsByDni(request.dni())) {
            throw new BadRequestException("Ya existe un médico con el DNI: " + request.dni());
        }
        if (medicoRepository.existsByMatricula(request.matricula())) {
            throw new BadRequestException("Ya existe un médico con la matrícula: " + request.matricula());
        }

        Especialidad especialidad = especialidadRepository.findById(request.especialidadId())
            .orElseThrow(() -> new ResourceNotFoundException("Especialidad", request.especialidadId()));

        Medico medico = new Medico();
        medico.setNombre(request.nombre());
        medico.setApellido(request.apellido());
        medico.setDni(request.dni());
        medico.setMatricula(request.matricula());
        medico.setEspecialidad(especialidad);
        medico.setTelefono(request.telefono());
        medico.setEmail(request.email());

        medico = medicoRepository.save(medico);
        return MedicoMapper.toResponse(medico);
    }

    @Transactional
    public MedicoResponse update(Long id, ActualizarMedicoRequest request) {
        Medico medico = medicoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Medico", id));

        Especialidad especialidad = especialidadRepository.findById(request.especialidadId())
            .orElseThrow(() -> new ResourceNotFoundException("Especialidad", request.especialidadId()));

        medico.setNombre(request.nombre());
        medico.setApellido(request.apellido());
        medico.setEspecialidad(especialidad);
        medico.setTelefono(request.telefono());
        medico.setEmail(request.email());

        medico = medicoRepository.save(medico);
        return MedicoMapper.toResponse(medico);
    }

    @Transactional
    public void delete(Long id) {
        if (!medicoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Medico", id);
        }
        medicoRepository.deleteById(id);
    }

    @Transactional
    public MedicoResponse changeStatus(Long id, String nuevoEstado) {
        Medico medico = medicoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Medico", id));

        try {
            medico.setEstado(EstadoMedico.valueOf(nuevoEstado.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Estado no válido: " + nuevoEstado);
        }

        medico = medicoRepository.save(medico);
        return MedicoMapper.toResponse(medico);
    }
}
