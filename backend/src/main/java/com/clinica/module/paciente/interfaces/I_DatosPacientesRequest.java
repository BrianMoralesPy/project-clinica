package com.clinica.module.paciente.interfaces;

import java.time.LocalDate;

public interface I_DatosPacientesRequest {
    
    String dni();
    LocalDate fechaNacimiento();
    String sexo();
    String telefono();
    String direccion();
    String grupoSanguineo();
    String alergias();
}