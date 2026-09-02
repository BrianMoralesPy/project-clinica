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
import com.clinica.module.usuario.dto.ActualizarUsuarioRequest;
import com.clinica.module.usuario.entity.Usuario;
import com.clinica.module.medico.interfaces.I_DatosMedicosRequest;
import com.clinica.shared.EstadoUsuario;
import com.clinica.module.usuario.service.UsuarioService;
import com.clinica.module.usuario.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;


/**
 * Servicio principal encargado de gestionar la lógica de negocio
 * relacionada con los médicos.
 *
 * Responsabilidades principales:
 * - Consultar médicos.
 * - Crear médicos junto con su usuario asociado.
 * - Actualizar información profesional y personal del médico.
 * - Actualizar información del usuario asociado al médico.
 * - Desactivar médicos mediante soft delete.
 * - Obtener el perfil del médico autenticado.
 *
 * La lógica de persistencia se delega a los repositorios,
 * mientras que las reglas de negocio se centralizan en este servicio.
 */
@Service
@RequiredArgsConstructor
public class MedicoService {

    /**
     * Repositorio encargado de acceder y persistir
     * las entidades Médico.
     */
    private final MedicoRepository medicoRepository;

    /**
     * Repositorio encargado de consultar las especialidades
     * disponibles para asignarlas a un médico.
     */
    private final EspecialidadRepository especialidadRepository;

    /**
     * Servicio encargado del proceso de registro de un médico.
     *
     * Su responsabilidad es crear el Usuario y el Médico
     * manteniendo centralizada la lógica de creación.
     */
    private final MedicoRegistrationService medicoRegistrationService;

    /**
     * Servicio encargado de gestionar los datos generales
     * de la entidad Usuario asociada al médico.
     */
    private final UsuarioService usuarioService;

    /**
     * Repositorio encargado de consultar y actualizar
     * los usuarios asociados a los médicos.
     */
    private final UsuarioRepository usuarioRepository;


    /**
     * Obtiene el médico asociado al usuario actualmente autenticado.
     *
     * Flujo de autenticación:
     *
     * JWT
     *   ↓
     * username
     *   ↓
     * Usuario
     *   ↓
     * Médico
     *
     * Spring Security almacena la autenticación actual dentro
     * del SecurityContext. Desde allí se obtiene el username
     * del usuario autenticado.
     *
     * Este método se utiliza principalmente para los endpoints
     * que trabajan con el propio perfil del médico.
     *
     * Endpoint relacionado:
     * GET /api/medicos/me
     */
    private Medico getMedicoAutenticado() {

        // Obtiene la información de autenticación almacenada
        // por Spring Security para la petición actual.
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        // En la configuración actual, authentication.getName()
        // devuelve el username del usuario autenticado.
        String username = authentication.getName();

        // Busca en la base de datos el Usuario correspondiente
        // al username obtenido desde Spring Security.
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuario no encontrado"));

        // Busca el Médico asociado al Usuario encontrado.
        // La relación se establece mediante el ID del usuario.
        return medicoRepository.findByUsuarioId(usuario.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Medico", usuario.getId()));
    }


    /**
     * Actualiza los datos propios de la entidad Médico.
     *
     * Este método no modifica información de Usuario.
     *
     * Se utiliza tanto en actualizaciones administrativas
     * como en otros flujos que necesiten modificar únicamente
     * los datos profesionales/personales del médico.
     */
    private void actualizarDatosMedico(
            Medico medico,
            I_DatosMedicosRequest request) {

        // Actualiza el DNI del médico.
        medico.setDni(request.dni());

        // Actualiza la matrícula profesional.
        medico.setMatricula(request.matricula());

        // Busca y asigna la especialidad indicada.
        // Si la especialidad no existe, se interrumpe la operación.
        medico.setEspecialidad(
                especialidadRepository.findById(request.especialidadId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Especialidad",
                                        request.especialidadId()))
        );

        // Actualiza el número de teléfono.
        medico.setTelefono(request.telefono());

        // Actualiza la fecha de nacimiento.
        medico.setFechaNacimiento(request.fechaNacimiento());
    }


    /**
     * Obtiene una lista paginada de médicos.
     *
     * Permite filtrar los resultados por:
     * - Especialidad.
     * - Estado del usuario asociado.
     *
     * La paginación se recibe mediante Pageable.
     *
     * @param especialidadId identificador de la especialidad.
     * @param estado estado actual del usuario asociado.
     * @param pageable configuración de paginación y ordenamiento.
     * @return página de médicos convertidos a MedicoResponse.
     */
    @Transactional(readOnly = true)
    public Page<MedicoResponse> findAll(
            Long especialidadId,
            EstadoUsuario estado,
            Pageable pageable) {

        return medicoRepository
                .findByEspecialidadAndEstado(
                        especialidadId,
                        estado,
                        pageable)
                .map(MedicoMapper::toResponse);
    }


    /**
     * Busca un médico mediante su identificador.
     *
     * @param id identificador del médico.
     * @return información pública del médico.
     * @throws ResourceNotFoundException si el médico no existe.
     */
    @Transactional(readOnly = true)
    public MedicoResponse findById(Long id) {

        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Medico", id));

        return MedicoMapper.toResponse(medico);
    }


    /**
     * Crea un nuevo médico.
     *
     * Antes de realizar la creación se validan las restricciones
     * de unicidad propias del médico:
     * - DNI.
     * - Matrícula profesional.
     *
     * Posteriormente se verifica que la especialidad exista
     * y se delega la creación de Usuario + Médico
     * al servicio especializado de registro.
     *
     * Toda la operación se ejecuta dentro de una única transacción.
     *
     * @param request datos necesarios para crear el médico.
     * @return información del médico creado.
     */
    @Transactional
    public MedicoResponse create(CrearMedicoRequest request) {

        // Verifica que no exista otro médico con el mismo DNI.
        if (medicoRepository.existsByDni(request.dni())) {
            throw new BadRequestException(
                    "Ya existe un médico con el DNI: " + request.dni());
        }

        // Verifica que no exista otro médico con la misma matrícula.
        if (medicoRepository.existsByMatricula(request.matricula())) {
            throw new BadRequestException(
                    "Ya existe un médico con la matrícula: "
                            + request.matricula());
        }

        // Verifica que la especialidad indicada exista.
        Especialidad especialidad =
                especialidadRepository.findById(request.especialidadId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Especialidad",
                                        request.especialidadId()));

        /*
         * Delega la creación completa al servicio de registro.
         *
         * Este proceso crea:
         * 1. El Usuario.
         * 2. El Médico.
         * 3. La relación entre ambos.
         *
         * También centraliza la lógica relacionada con
         * contraseña, roles y datos iniciales.
         */
        Medico medico = medicoRegistrationService.crearMedico(
                request.username(),
                request.email(),
                request.password(),
                request.nombre(),
                request.apellido(),
                request.dni(),
                request.matricula(),
                especialidad,
                request.telefono(),
                request.fechaNacimiento()
        );

        // Convierte la entidad a un DTO de respuesta.
        return MedicoMapper.toResponse(medico);
    }


    /**
     * Actualiza únicamente los datos correspondientes
     * a la entidad Médico.
     *
     * NO modifica la entidad Usuario asociada.
     *
     * Se utiliza cuando solamente se desea modificar
     * información propia del perfil médico.
     *
     * @param id identificador del médico.
     * @param request datos médicos a actualizar.
     * @return información actualizada del médico.
     */
    @Transactional
    public MedicoResponse update(
            Long id,
            ActualizarMedicoRequest request) {

        // Busca el médico que se desea modificar.
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Medico", id));

        // Verifica que el nuevo DNI no pertenezca a otro médico.
        if (medicoRepository.existsByDniAndIdNot(
                request.dni(), id)) {

            throw new BadRequestException(
                    "Ya existe un médico con el DNI: "
                            + request.dni());
        }

        // Verifica que la nueva matrícula no pertenezca
        // a otro médico.
        if (medicoRepository.existsByMatriculaAndIdNot(
                request.matricula(), id)) {

            throw new BadRequestException(
                    "Ya existe un médico con la matrícula: "
                            + request.matricula());
        }

        // Actualiza únicamente los datos propios del médico.
        actualizarDatosMedico(medico, request);

        // Persiste los cambios.
        medico = medicoRepository.save(medico);

        // Devuelve la representación DTO del médico actualizado.
        return MedicoMapper.toResponse(medico);
    }


    /**
     * Actualiza simultáneamente:
     *
     * - Los datos de la entidad Médico.
     * - Los datos de la entidad Usuario asociada.
     *
     * Este método permite modificar en una única operación
     * información profesional y datos generales de la cuenta.
     *
     * Ambas modificaciones forman parte de la misma transacción,
     * por lo que si una operación falla, se revierte la operación completa.
     *
     * @param id identificador del médico.
     * @param medicoRequest datos propios del médico.
     * @param usuarioRequest datos generales del usuario.
     * @return información actualizada del médico.
     */
    @Transactional
    public MedicoResponse update(
            Long id,
            ActualizarMedicoRequest medicoRequest,
            ActualizarUsuarioRequest usuarioRequest) {

        // Busca el médico que se desea modificar.
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Medico", id));

        // Verifica que el DNI no pertenezca a otro médico.
        if (medicoRepository.existsByDniAndIdNot(
                medicoRequest.dni(), id)) {

            throw new BadRequestException(
                    "Ya existe un médico con el DNI: "
                            + medicoRequest.dni());
        }

        // Verifica que la matrícula no pertenezca a otro médico.
        if (medicoRepository.existsByMatriculaAndIdNot(
                medicoRequest.matricula(), id)) {

            throw new BadRequestException(
                    "Ya existe un médico con la matrícula: "
                            + medicoRequest.matricula());
        }

        // Actualiza los datos propios del médico.
        actualizarDatosMedico(medico, medicoRequest);

        // Obtiene el Usuario asociado al Médico.
        Usuario usuario = medico.getUsuario();

        /*
         * Actualiza los datos generales del Usuario.
         *
         * La lógica de validación de username y email,
         * incluyendo sus restricciones de unicidad,
         * está centralizada en UsuarioService.
         */
        usuarioService.actualizarDatosUsuario(
                usuario,
                usuarioRequest);

        // Persiste los cambios realizados sobre el médico.
        medico = medicoRepository.save(medico);

        // Devuelve la información actualizada.
        return MedicoMapper.toResponse(medico);
    }


    /**
     * Desactiva un médico mediante un soft delete.
     *
     * No se elimina físicamente el registro del médico
     * ni el usuario asociado.
     *
     * Únicamente se cambia el estado del Usuario a INACTIVO.
     *
     * Esto permite conservar:
     * - Historial médico.
     * - Turnos anteriores.
     * - Relaciones existentes.
     * - Información histórica del profesional.
     *
     * @param id identificador del médico.
     */
    @Transactional
    public void softDelete(Long id) {

        // Busca el médico que se desea desactivar.
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Medico", id));

        // Obtiene el usuario asociado al médico.
        Usuario usuario = medico.getUsuario();

        // Cambia el estado del usuario a INACTIVO.
        usuario.setEstado(EstadoUsuario.INACTIVO);

        // Persiste el nuevo estado.
        usuarioRepository.save(usuario);
    }


    /**
     * Obtiene el perfil del médico actualmente autenticado.
     *
     * No recibe un ID como parámetro, ya que el médico
     * se identifica mediante el usuario autenticado
     * en Spring Security.
     *
     * Endpoint:
     * GET /api/medicos/me
     *
     * @return información del perfil del médico autenticado.
     */
    @Transactional(readOnly = true)
    public MedicoResponse getMiPerfil() {

        // Obtiene el médico correspondiente al usuario autenticado.
        Medico medico = getMedicoAutenticado();

        // Convierte la entidad a su DTO de respuesta.
        return MedicoMapper.toResponse(medico);
    }
}

