package com.clinica.module.archivo.repository;

import com.clinica.module.archivo.entity.ArchivoAdjunto;
import com.clinica.shared.EntidadTipo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArchivoAdjuntoRepository extends JpaRepository<ArchivoAdjunto, Long> {

    List<ArchivoAdjunto> findByEntidadTipoAndEntidadId(EntidadTipo entidadTipo, Long entidadId);
}
