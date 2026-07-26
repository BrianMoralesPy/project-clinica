package com.clinica.module.paciente.service;

import com.clinica.exception.BadRequestException;
import com.clinica.exception.ResourceNotFoundException;
import com.clinica.module.paciente.dto.ActualizarPacienteRequest;
import com.clinica.module.paciente.dto.CrearPacienteRequest;
import com.clinica.module.paciente.dto.PacienteResponse;
import com.clinica.module.paciente.entity.Paciente;
import com.clinica.module.paciente.mapper.PacienteMapper;
import com.clinica.module.paciente.repository.PacienteRepository;
import com.clinica.shared.EstadoPaciente;
import com.clinica.shared.GrupoSanguineo;
import com.clinica.shared.Sexo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    @Transactional(readOnly = true)
    public Page<PacienteResponse> findAll(String search, EstadoPaciente estado, Pageable pageable) {
        return pacienteRepository.findBySearchAndEstado(search, estado, pageable)
            .map(PacienteMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public PacienteResponse findById(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Paciente", id));
        return PacienteMapper.toResponse(paciente);
    }

    @Transactional
    public PacienteResponse create(CrearPacienteRequest request) {
        if (pacienteRepository.existsByDni(request.dni())) {
            throw new BadRequestException("Ya existe un paciente con el DNI: " + request.dni());
        }

        Paciente paciente = new Paciente();
        paciente.setNombre(request.nombre());
        paciente.setApellido(request.apellido());
        paciente.setDni(request.dni());
        paciente.setFechaNacimiento(request.fechaNacimiento());
        paciente.setSexo(parseEnum(Sexo.class, request.sexo()));
        paciente.setTelefono(request.telefono());
        paciente.setEmail(request.email());
        paciente.setDireccion(request.direccion());
        paciente.setGrupoSanguineo(parseEnumOrNull(GrupoSanguineo.class, request.grupoSanguineo()));
        paciente.setAlergias(request.alergias());

        paciente = pacienteRepository.save(paciente);
        return PacienteMapper.toResponse(paciente);
    }

    @Transactional
    public PacienteResponse update(Long id, ActualizarPacienteRequest request) {
        Paciente paciente = pacienteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Paciente", id));

        paciente.setNombre(request.nombre());
        paciente.setApellido(request.apellido());
        paciente.setFechaNacimiento(request.fechaNacimiento());
        paciente.setSexo(parseEnum(Sexo.class, request.sexo()));
        paciente.setTelefono(request.telefono());
        paciente.setEmail(request.email());
        paciente.setDireccion(request.direccion());
        paciente.setGrupoSanguineo(parseEnumOrNull(GrupoSanguineo.class, request.grupoSanguineo()));
        paciente.setAlergias(request.alergias());

        paciente = pacienteRepository.save(paciente);
        return PacienteMapper.toResponse(paciente);
    }

    @Transactional
    public void delete(Long id) {
        if (!pacienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Paciente", id);
        }
        pacienteRepository.deleteById(id);
    }

    @Transactional
    public PacienteResponse changeStatus(Long id, String nuevoEstado) {
        Paciente paciente = pacienteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Paciente", id));

        paciente.setEstado(parseEnum(EstadoPaciente.class, nuevoEstado));
        paciente = pacienteRepository.save(paciente);
        return PacienteMapper.toResponse(paciente);
    }

    private <E extends Enum<E>> E parseEnum(Class<E> enumClass, String value) {
        try {
            return Enum.valueOf(enumClass, value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Valor no válido: " + value);
        }
    }

    private <E extends Enum<E>> E parseEnumOrNull(Class<E> enumClass, String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return Enum.valueOf(enumClass, value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Valor no válido: " + value);
        }
    }
}
