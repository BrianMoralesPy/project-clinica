package com.clinica.module.medico.service;

import com.clinica.module.especialidad.entity.Especialidad;
import com.clinica.module.medico.entity.Medico;
import com.clinica.module.medico.repository.MedicoRepository;
import com.clinica.module.usuario.entity.Usuario;
import com.clinica.module.usuario.service.UsuarioCreationByRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;


/**
 * Servicio encargado del proceso de registro de un médico.
 *
 * Su responsabilidad es coordinar la creación de las dos entidades
 * que forman parte de un médico dentro del sistema:
 *
 * 1. Usuario:
 *    Contiene los datos de acceso y datos generales de la cuenta.
 *
 * 2. Médico:
 *    Contiene la información profesional y específica del médico.
 *
 * El Usuario se crea mediante UsuarioCreationByRoleService para
 * centralizar la lógica de creación, asignación del rol y gestión
 * de credenciales.
 *
 * La creación de Usuario y Médico se realiza dentro de una única
 * transacción, garantizando que ambas operaciones se completen
 * correctamente o se reviertan en caso de error.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class MedicoRegistrationService {

    /**
     * Servicio encargado de crear usuarios según el rol correspondiente.
     *
     * En este caso se utiliza para crear un Usuario con el rol MEDICO.
     *
     * La lógica de contraseña, validación de username/email y asignación
     * del rol se mantiene centralizada en este servicio para evitar
     * duplicación de código.
     */
    private final UsuarioCreationByRoleService usuarioCreationByRoleService;

    /**
     * Repositorio encargado de persistir la entidad Médico.
     */
    private final MedicoRepository medicoRepository;


    /**
     * Registra un nuevo médico en el sistema.
     *
     * El proceso consiste en:
     *
     * 1. Crear el Usuario correspondiente al médico.
     * 2. Crear la entidad Médico.
     * 3. Asociar el Médico con el Usuario.
     * 4. Asignar los datos profesionales y personales.
     * 5. Persistir el Médico en la base de datos.
     *
     * La asociación entre Usuario y Médico permite separar los datos
     * generales de la cuenta de los datos específicos del profesional.
     *
     * @param username nombre de usuario para acceder al sistema.
     * @param email correo electrónico del usuario.
     * @param password contraseña del usuario.
     * @param nombre nombre del médico.
     * @param apellido apellido del médico.
     * @param dni documento nacional de identidad.
     * @param matricula matrícula profesional del médico.
     * @param especialidad especialidad médica asignada.
     * @param telefono número de teléfono del médico.
     * @param fechaNacimiento fecha de nacimiento del médico.
     *
     * @return entidad Médico creada y persistida.
     */
    public Medico crearMedico(
            String username,
            String email,
            String password,
            String nombre,
            String apellido,
            String dni,
            String matricula,
            Especialidad especialidad,
            String telefono,
            LocalDate fechaNacimiento) {

        /*
         * Crea el Usuario utilizando el servicio centralizado
         * de creación por rol.
         *
         * Este método asigna automáticamente el rol MEDICO.
         */
        Usuario usuario =
                usuarioCreationByRoleService.crearUsuarioTipoMedico(
                        username,
                        email,
                        password,
                        nombre,
                        apellido
                );

        /*
         * Crea la entidad Médico que contiene la información
         * específica del profesional.
         */
        Medico medico = new Medico();

        /*
         * Vincula el Médico con el Usuario recién creado.
         *
         * De esta manera, el médico queda asociado a su cuenta
         * de acceso al sistema.
         */
        medico.setUsuario(usuario);

        // Asigna el documento de identidad del médico.
        medico.setDni(dni);

        // Asigna la matrícula profesional.
        medico.setMatricula(matricula);

        // Asigna la especialidad médica.
        medico.setEspecialidad(especialidad);

        // Asigna el número de teléfono.
        medico.setTelefono(telefono);

        // Asigna la fecha de nacimiento.
        medico.setFechaNacimiento(fechaNacimiento);

        /*
         * Persiste la entidad Médico.
         *
         * El Usuario ya fue creado previamente por
         * UsuarioCreationByRoleService.
         */
        return medicoRepository.save(medico);
    }
}
