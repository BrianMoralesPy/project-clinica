package com.clinica.module.paciente.service;

import com.clinica.module.paciente.entity.Paciente;
import com.clinica.module.paciente.repository.PacienteRepository;
import com.clinica.module.usuario.entity.Usuario;
import com.clinica.module.usuario.service.UsuarioCreationByRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PacienteRegistrationService {

    // Servicio encargado de crear un Usuario con rol PACIENTE
    private final UsuarioCreationByRoleService usuarioCreationByRoleService;

    // Repositorio para persistir la ficha del paciente
    private final PacienteRepository pacienteRepository;

    /**
     * Crea un nuevo paciente "incompleto".
     *
     * Este método centraliza toda la lógica de alta de un paciente
     * para evitar duplicarla en distintos servicios.
     *
     * Flujo:
     *
     * 1. Crea el Usuario con rol PACIENTE.
     * 2. Crea la ficha del Paciente asociada al Usuario.
     * 3. Marca el perfil como incompleto.
     * 4. Guarda la ficha del paciente.
     * 5. Devuelve el Usuario creado.
     *
     * Es utilizado por:
     *
     * - AuthService (registro público).
     * - PacienteService (alta administrativa).
     */
    public Usuario crearPacienteIncompleto(String username,String email,String password,String nombre,String apellido) {

        // Crea el Usuario y le asigna automáticamente el rol PACIENTE
        Usuario usuario = usuarioCreationByRoleService.crearUsuarioTipoPaciente(username,email,password,nombre,apellido);
        // Crea la ficha del paciente asociada al usuario
        Paciente paciente = new Paciente();
        paciente.setUsuario(usuario);

        // El perfil comienza incompleto hasta que el paciente
        // cargue sus datos personales
        paciente.setPerfilCompleto(false);

        // Guarda la ficha del paciente
        pacienteRepository.save(paciente);

        // Devuelve el usuario para que el servicio que lo llamó
        // pueda continuar (por ejemplo, generar el JWT)
        return usuario;
    }
}