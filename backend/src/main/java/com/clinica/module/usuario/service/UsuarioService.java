package com.clinica.module.usuario.service;

import com.clinica.exception.BadRequestException;
import com.clinica.exception.ResourceNotFoundException;
import com.clinica.module.usuario.dto.ActualizarUsuarioRequest;
import com.clinica.module.usuario.dto.CambiarEstadoRequest;
import com.clinica.module.usuario.dto.UsuarioResponse;
import com.clinica.module.usuario.entity.Usuario;
import com.clinica.module.usuario.interfaces.I_DatosUsuariosRequest;
import com.clinica.module.usuario.mapper.UsuarioMapper;
import com.clinica.module.usuario.repository.UsuarioRepository;
import com.clinica.shared.EstadoUsuario;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public void actualizarDatosUsuario(Usuario usuario, I_DatosUsuariosRequest request) {
        if (request.username() != null) {
            usuario.setUsername(request.username());
        }
        if (request.email() != null) {
            usuario.setEmail(request.email());
        }
        if (request.nombre() != null) {
            usuario.setNombre(request.nombre());
        }
        if (request.apellido() != null) {
            usuario.setApellido(request.apellido());
        }
    }

    @Transactional(readOnly = true)
    public Page<UsuarioResponse> findAll(String search, EstadoUsuario estado, Pageable pageable) {
        return usuarioRepository.findBySearchAndEstado(search, estado, pageable)
            .map(UsuarioMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public UsuarioResponse findById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
        return UsuarioMapper.toResponse(usuario);
    }

    @Transactional
    public UsuarioResponse update(Long id, ActualizarUsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario", id));

        if (request.email() != null && !request.email().equals(usuario.getEmail())) {
            if (usuarioRepository.existsByEmail(request.email())) {
                throw new BadRequestException("El email ya está registrado");
            }
            usuario.setEmail(request.email());
        }

        if (request.nombre() != null) {
            usuario.setNombre(request.nombre());
        }

        if (request.apellido() != null) {
            usuario.setApellido(request.apellido());
        }

        usuario = usuarioRepository.save(usuario);
        return UsuarioMapper.toResponse(usuario);
    }

    @Transactional
    public UsuarioResponse changeStatus(Long id, CambiarEstadoRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));

        EstadoUsuario nuevoEstado;
        try {
            nuevoEstado = EstadoUsuario.valueOf(request.estado());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Estado no válido: " + request.estado());
        }

        usuario.setEstado(nuevoEstado);
        usuario = usuarioRepository.save(usuario);
        return UsuarioMapper.toResponse(usuario);
    }
}
