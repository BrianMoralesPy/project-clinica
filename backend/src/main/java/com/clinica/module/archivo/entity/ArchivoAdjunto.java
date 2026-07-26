package com.clinica.module.archivo.entity;

import com.clinica.module.usuario.entity.Usuario;
import com.clinica.shared.BaseEntity;
import com.clinica.shared.EntidadTipo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "archivo_adjunto")
@Getter
@Setter
public class ArchivoAdjunto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "entidad_tipo", nullable = false, length = 30)
    private EntidadTipo entidadTipo;

    @Column(name = "entidad_id", nullable = false)
    private Long entidadId;

    @Column(nullable = false, length = 50)
    private String bucket;

    @Column(name = "ruta_archivo", nullable = false, length = 500)
    private String rutaArchivo;

    @Column(name = "nombre_original", nullable = false, length = 200)
    private String nombreOriginal;

    @Column(name = "tipo_mime", nullable = false, length = 100)
    private String tipoMime;

    @Column(name = "tamano_bytes", nullable = false)
    private Long tamanoBytes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subido_por")
    private Usuario subidoPor;

    @Column(name = "created_at", nullable = false, updatable = false)
    private java.time.Instant createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = java.time.Instant.now();
    }
}
