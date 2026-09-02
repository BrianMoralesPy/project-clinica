package com.clinica.module.medico.interfaces;

import java.time.LocalDate;

/**
 * Define el contrato de los datos correspondientes a la información médica
 * que puede ser recibida en una solicitud.
 *
 * <p>
 * Esta interfaz permite estandarizar el acceso a los datos médicos
 * independientemente de la clase concreta que los implemente.
 * </p>
 *
 * <p>
 * Se utiliza como una abstracción para agrupar los datos propios del médico,
 * evitando duplicar la definición de estos campos cuando diferentes objetos
 * de solicitud necesitan trabajar con la misma información.
 * </p>
 */
public interface I_DatosMedicosRequest {

    /**
     * Obtiene el documento nacional de identidad del médico.
     *
     * @return DNI del médico
     */
    String dni();

    /**
     * Obtiene la matrícula profesional del médico.
     *
     * @return matrícula profesional
     */
    String matricula();

    /**
     * Obtiene el identificador de la especialidad médica seleccionada.
     *
     * @return identificador de la especialidad
     */
    Long especialidadId();

    /**
     * Obtiene el número de teléfono del médico.
     *
     * @return teléfono del médico
     */
    String telefono();

    /**
     * Obtiene la fecha de nacimiento del médico.
     *
     * @return fecha de nacimiento
     */
    LocalDate fechaNacimiento();
}
