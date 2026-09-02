package com.clinica.module.paciente.service;

import com.clinica.exception.BadRequestException;
import com.clinica.exception.ResourceNotFoundException;

import com.clinica.module.paciente.dto.ActualizarPacienteRequest;
import com.clinica.module.paciente.dto.CrearPacienteRequest;
import com.clinica.module.paciente.dto.PacienteResponse;
import com.clinica.module.paciente.entity.Paciente;
import com.clinica.module.paciente.mapper.PacienteMapper;
import com.clinica.module.paciente.repository.PacienteRepository;
import com.clinica.module.usuario.dto.ActualizarUsuarioRequest;
import com.clinica.module.usuario.entity.Usuario;
import com.clinica.module.usuario.repository.UsuarioRepository;
import com.clinica.module.paciente.interfaces.I_DatosPacientesRequest;
import com.clinica.module.usuario.service.UsuarioService;

import com.clinica.shared.EstadoUsuario;
import com.clinica.shared.GrupoSanguineo;
import com.clinica.shared.Sexo;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
@Service
@RequiredArgsConstructor
public class PacienteService {

    // Repositorio para acceder a la entidad Paciente
    private final PacienteRepository pacienteRepository;

    // Repositorio para buscar el usuario autenticado
    private final UsuarioRepository usuarioRepository;

    // Servicio encargado de crear un Usuario + Paciente durante el registro
    private final PacienteRegistrationService pacienteRegistrationService;

    // Servicio encargado de gestionar el Usuario
    private final UsuarioService usuarioService;
    
    /**
     * Obtiene el paciente asociado al usuario que inició sesión.
     *
     * Flujo:
     * JWT -> Username -> Usuario -> Paciente
     *
     * Se utiliza en los endpoints:
     * GET /api/pacientes/me
     * PUT /api/pacientes/me
     */
    private Paciente getPacienteAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return pacienteRepository.findByUsuarioId(usuario.getId()).orElseThrow(() -> new ResourceNotFoundException("Paciente", usuario.getId()));
    }

    /**
     * Determina si el perfil del paciente está completo.
     *
     * Este valor NO lo envía el frontend.
     * Siempre se calcula desde el backend para mantener la lógica centralizada.
     */
    private boolean calcularPerfilCompleto(Paciente paciente) {
        return paciente.getDni() != null && paciente.getFechaNacimiento() != null && paciente.getSexo() != null;
    }

    /**
     * Convierte un String recibido desde el frontend
     * al Enum correspondiente.
     *
     * Ejemplo:
     * "masculino" -> Sexo.MASCULINO
     *
     * Si el valor no existe lanza BadRequest.
     */
    private <E extends Enum<E>> E parseEnum(Class<E> enumClass, String value) {
        try {
            return Enum.valueOf(enumClass, value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Valor no válido: " + value);
        }
    } 

    /**
     * Igual que parseEnum(), pero permite valores nulos
     * o cadenas vacías.
     *
     * Se utiliza para campos opcionales como
     * GrupoSanguineo.
     */
    private <E extends Enum<E>> E parseEnumOrNull(Class<E> enumClass, String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return Enum.valueOf(enumClass, value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Valor no válido: " + value);
        }
    }

    /**
     * Copia los datos del DTO hacia la entidad Paciente.
     *
     * Este método es reutilizado por:
     *
     * - Alta administrativa
     * - Actualización por administrador
     * - Completar perfil del paciente
     *
     * De esta manera evitamos duplicar la misma lógica
     * en distintos métodos del servicio.
     */
    private void actualizarDatosPaciente(Paciente paciente, I_DatosPacientesRequest request) {
        paciente.setDni(request.dni());
        paciente.setFechaNacimiento(request.fechaNacimiento());
        paciente.setSexo(parseEnum(Sexo.class, request.sexo()));
        paciente.setTelefono(request.telefono());
        paciente.setDireccion(request.direccion());
        paciente.setGrupoSanguineo(parseEnumOrNull(GrupoSanguineo.class, request.grupoSanguineo()));
        paciente.setAlergias(request.alergias());

        paciente.setPerfilCompleto(calcularPerfilCompleto(paciente));
    }

    
    
    // ===========================================================
    // FUNCIONALIDAD DEL PACIENTE AUTENTICADO
    // ===========================================================
    
    /**
     * Devuelve la información del paciente que inició sesión.
     *
     * Endpoint:
     * GET /api/pacientes/me
     */
    @Transactional(readOnly = true)
    public PacienteResponse getMiPerfil() {
        Paciente paciente = getPacienteAutenticado();
        return PacienteMapper.toResponse(paciente);
    }

    /**
     * Permite al paciente completar o actualizar
     * su propia información personal.
     *
     * Endpoint:
     * PUT /api/pacientes/me
     */
    @Transactional
    public PacienteResponse completarMiPerfil(ActualizarPacienteRequest request) {
        Paciente paciente = getPacienteAutenticado();
        actualizarDatosPaciente(paciente, request); 
        paciente = pacienteRepository.save(paciente);
        return PacienteMapper.toResponse(paciente);

    }
    
    // ===========================================================
    // FUNCIONALIDADES DE ADMINISTRACIÓN / RECEPCIÓN
    // ===========================================================

    /**
     * Lista pacientes con búsqueda y paginación.
     */
    @Transactional(readOnly = true)
    public Page<PacienteResponse> findAll(String search, EstadoUsuario estado, Pageable pageable) {
        return pacienteRepository.findBySearchAndEstado(search, estado, pageable).map(PacienteMapper::toResponse);
    }

    /**
     * Obtiene un paciente por su ID.
     */
    @Transactional(readOnly = true)
    public PacienteResponse findById(Long id) {
        Paciente paciente = pacienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Paciente", id));
        return PacienteMapper.toResponse(paciente);
    }

    /**
     * Alta administrativa de un paciente.
     *
     * A diferencia del registro público,
     * este método es utilizado por ADMIN o RECEPCIÓN.
     *
     * Flujo:
     *
     * Crear Usuario
     * ↓
     * Crear Paciente vacío
     * ↓
     * Completar datos del paciente
     * ↓
     * Guardar
     */
    @Transactional
    public PacienteResponse create(CrearPacienteRequest request) {

        if (pacienteRepository.existsByDni(request.dni())) { throw new BadRequestException("Ya existe un paciente con el DNI: " + request.dni());}
        // Crea el Usuario y el Paciente vacío
        Usuario usuario = pacienteRegistrationService.crearPacienteIncompleto(request.username(),request.email(),request.password(),request.nombre(),request.apellido());
        // Recupera el Paciente recién creado
        Paciente paciente = pacienteRepository.findByUsuarioId(usuario.getId()).orElseThrow(() ->new ResourceNotFoundException("Paciente", usuario.getId()));

        // Completa los datos propios del paciente
        actualizarDatosPaciente(paciente, request);

        paciente = pacienteRepository.save(paciente);

        return PacienteMapper.toResponse(paciente);
    }

    /*Permite al administrador modificar cualquier paciente, solo los datos propios de la tabla pacientes.*/
    @Transactional
    public PacienteResponse update(Long id, ActualizarPacienteRequest request) {
        Paciente paciente = pacienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Paciente", id));
        actualizarDatosPaciente(paciente, request);
        paciente = pacienteRepository.save(paciente);
        return PacienteMapper.toResponse(paciente);
    }
    /* Permite al administrador modificar cualquier paciente, tanto los datos propios de la tabla pacientes como los datos del usuario asociado. */
    @Transactional
    public PacienteResponse update(Long id,ActualizarPacienteRequest pacienteRequest,ActualizarUsuarioRequest usuarioRequest) {
        Paciente paciente = pacienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Paciente", id));

        actualizarDatosPaciente(paciente, pacienteRequest);

        Usuario usuario = paciente.getUsuario();
        usuarioService.actualizarDatosUsuario(usuario, usuarioRequest);

        paciente = pacienteRepository.save(paciente);

        return PacienteMapper.toResponse(paciente);
    }
    /* Permite al administrador desactivar cualquier paciente, cambiando el estado del usuario asociado a INACTIVO.*/
    @Transactional
    public void softDelete(Long id) {
        Paciente paciente = pacienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Paciente", id));
        Usuario usuario = paciente.getUsuario();
        usuario.setEstado(EstadoUsuario.INACTIVO);
        usuarioRepository.save(usuario);
        
    }
}
