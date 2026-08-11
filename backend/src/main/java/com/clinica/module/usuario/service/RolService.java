package com.clinica.module.usuario.service;

import com.clinica.exception.BadRequestException;
import com.clinica.exception.ResourceNotFoundException;
import com.clinica.module.usuario.dto.ActualizarRolRequest;
import com.clinica.module.usuario.dto.CrearRolRequest;
import com.clinica.module.usuario.dto.RolResponse;
import com.clinica.module.usuario.entity.Rol;
import com.clinica.module.usuario.mapper.RolMapper;
import com.clinica.module.usuario.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RolService {

    private final RolRepository rolRepository;

    @Transactional(readOnly = true)
    public List<RolResponse> findAll() {
        return rolRepository.findAll().stream().map(RolMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public RolResponse findById(Long id) {
        Rol rol = rolRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Rol", id));
        return RolMapper.toResponse(rol);
    }

    @Transactional
    public RolResponse create(CrearRolRequest request) {
        if (rolRepository.existsByNombre(request.nombre())) {
            throw new BadRequestException("Ya existe un rol con el nombre: " + request.nombre());
        }

        Rol rol = new Rol();
        rol.setNombre(request.nombre());
        rol.setDescripcion(request.descripcion());

        rol = rolRepository.save(rol);
        return RolMapper.toResponse(rol);
    }

    @Transactional
    public RolResponse update(Long id, ActualizarRolRequest request) {
        Rol rol = rolRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Rol", id));

        if (!rol.getNombre().equals(request.nombre())) {
            if (rolRepository.existsByNombre(request.nombre())) {
                throw new BadRequestException("Ya existe un rol con el nombre: " + request.nombre());
            }
            rol.setNombre(request.nombre());
        }

        rol.setDescripcion(request.descripcion());

        rol = rolRepository.save(rol);
        return RolMapper.toResponse(rol);
    }
}
