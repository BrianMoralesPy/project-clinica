package com.clinica.module.especialidad.service;

import com.clinica.exception.BadRequestException;
import com.clinica.exception.ResourceNotFoundException;
import com.clinica.module.especialidad.dto.ActualizarEspecialidadRequest;
import com.clinica.module.especialidad.dto.CrearEspecialidadRequest;
import com.clinica.module.especialidad.dto.EspecialidadResponse;
import com.clinica.module.especialidad.entity.Especialidad;
import com.clinica.module.especialidad.mapper.EspecialidadMapper;
import com.clinica.module.especialidad.repository.EspecialidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EspecialidadService {

    private final EspecialidadRepository especialidadRepository;

    @Transactional(readOnly = true)
    public List<EspecialidadResponse> findAll() {
        return especialidadRepository.findAll().stream()
            .map(EspecialidadMapper::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public EspecialidadResponse findById(Long id) {
        Especialidad especialidad = especialidadRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Especialidad", id));
        return EspecialidadMapper.toResponse(especialidad);
    }

    @Transactional
    public EspecialidadResponse create(CrearEspecialidadRequest request) {
        if (especialidadRepository.existsByNombre(request.nombre())) {
            throw new BadRequestException("Ya existe una especialidad con el nombre: " + request.nombre());
        }

        Especialidad especialidad = new Especialidad();
        especialidad.setNombre(request.nombre());
        especialidad.setDescripcion(request.descripcion());

        especialidad = especialidadRepository.save(especialidad);
        return EspecialidadMapper.toResponse(especialidad);
    }

    @Transactional
    public EspecialidadResponse update(Long id, ActualizarEspecialidadRequest request) {
        Especialidad especialidad = especialidadRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Especialidad", id));

        if (!especialidad.getNombre().equals(request.nombre())) {
            if (especialidadRepository.existsByNombre(request.nombre())) {
                throw new BadRequestException("Ya existe una especialidad con el nombre: " + request.nombre());
            }
            especialidad.setNombre(request.nombre());
        }

        especialidad.setDescripcion(request.descripcion());

        especialidad = especialidadRepository.save(especialidad);
        return EspecialidadMapper.toResponse(especialidad);
    }

    @Transactional
    public void delete(Long id) {
        if (!especialidadRepository.existsById(id)) {
            throw new ResourceNotFoundException("Especialidad", id);
        }
        especialidadRepository.deleteById(id);
    }
}
