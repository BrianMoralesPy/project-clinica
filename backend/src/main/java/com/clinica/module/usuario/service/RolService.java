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


/**
 * Servicio encargado de gestionar los roles del sistema.
 *
 * Contiene la lógica de negocio relacionada con la consulta,
 * creación y actualización de roles.
 */
@Service
@RequiredArgsConstructor
public class RolService {

    private final RolRepository rolRepository;


    /**
     * Obtiene todos los roles registrados.
     *
     * La consulta se realiza en modo solo lectura y cada entidad Rol
     * se transforma a RolResponse mediante RolMapper antes de devolverla.
     */
    @Transactional(readOnly = true)
    public List<RolResponse> findAll() {

        return rolRepository.findAll()
            .stream()
            .map(RolMapper::toResponse)
            .toList();
    }


    /**
     * Busca un rol por su ID.
     *
     * Si el rol no existe, se lanza una excepción indicando
     * que el recurso solicitado no fue encontrado.
     */
    @Transactional(readOnly = true)
    public RolResponse findById(Long id) {

        Rol rol = rolRepository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("Rol", id)
            );

        return RolMapper.toResponse(rol);
    }


    /**
     * Crea un nuevo rol.
     *
     * Antes de crear el registro se verifica que no exista otro
     * rol con el mismo nombre.
     */
    @Transactional
    public RolResponse create(CrearRolRequest request) {

        // Verificar que el nombre del rol no esté registrado
        if (rolRepository.existsByNombre(request.nombre())) {
            throw new BadRequestException(
                "Ya existe un rol con el nombre: " + request.nombre()
            );
        }


        // Crear y completar la nueva entidad Rol
        Rol rol = new Rol();

        rol.setNombre(request.nombre());
        rol.setDescripcion(request.descripcion());


        // Persistir el rol y devolver la entidad guardada
        rol = rolRepository.save(rol);

        return RolMapper.toResponse(rol);
    }


    /**
     * Actualiza un rol existente.
     *
     * Primero se verifica que el rol exista.
     * Si se modifica el nombre, se comprueba que el nuevo nombre
     * no pertenezca a otro rol.
     *
     * La descripción se actualiza directamente con el valor recibido.
     */
    @Transactional
    public RolResponse update(
        Long id,
        ActualizarRolRequest request
    ) {

        // Buscar el rol que se desea actualizar
        Rol rol = rolRepository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("Rol", id)
            );


        // Solo validar duplicados si el nombre realmente cambió
        if (!rol.getNombre().equals(request.nombre())) {

            // Verificar que el nuevo nombre no pertenezca a otro rol
            if (rolRepository.existsByNombre(request.nombre())) {
                throw new BadRequestException(
                    "Ya existe un rol con el nombre: " + request.nombre()
                );
            }

            rol.setNombre(request.nombre());
        }


        // Actualizar la descripción del rol
        rol.setDescripcion(request.descripcion());


        // Persistir los cambios
        rol = rolRepository.save(rol);

        return RolMapper.toResponse(rol);
    }
}