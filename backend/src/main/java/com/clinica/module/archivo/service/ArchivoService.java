package com.clinica.module.archivo.service;

import com.clinica.exception.BadRequestException;
import com.clinica.exception.ResourceNotFoundException;
import com.clinica.module.archivo.dto.ArchivoAdjuntoResponse;
import com.clinica.module.archivo.entity.ArchivoAdjunto;
import com.clinica.module.archivo.mapper.ArchivoMapper;
import com.clinica.module.archivo.repository.ArchivoAdjuntoRepository;
import com.clinica.module.usuario.entity.Usuario;
import com.clinica.module.usuario.repository.UsuarioRepository;
import com.clinica.shared.EntidadTipo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArchivoService {

    private final ArchivoAdjuntoRepository archivoRepository;
    private final UsuarioRepository usuarioRepository;
    private final SupabaseStorageService storageService;

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Transactional
    public ArchivoAdjuntoResponse upload(MultipartFile file, EntidadTipo entidadTipo, Long entidadId,
                                          String bucket, Long subidoPorId) throws IOException {
        if (file.isEmpty()) {
            throw new BadRequestException("El archivo está vacío");
        }

        Usuario subidoPor = null;
        if (subidoPorId != null) {
            subidoPor = usuarioRepository.findById(subidoPorId)
                .orElse(null);
        }

        String extension = getFileExtension(file.getOriginalFilename());
        String rutaArchivo = entidadId + "/" + UUID.randomUUID() + "." + extension;

        storageService.upload(bucket, rutaArchivo, file.getBytes(), file.getContentType());

        ArchivoAdjunto archivo = new ArchivoAdjunto();
        archivo.setEntidadTipo(entidadTipo);
        archivo.setEntidadId(entidadId);
        archivo.setBucket(bucket);
        archivo.setRutaArchivo(rutaArchivo);
        archivo.setNombreOriginal(file.getOriginalFilename());
        archivo.setTipoMime(file.getContentType());
        archivo.setTamanoBytes(file.getSize());
        archivo.setSubidoPor(subidoPor);

        archivo = archivoRepository.save(archivo);

        String url = buildPublicUrl(bucket, rutaArchivo);
        return ArchivoMapper.toResponse(archivo, url);
    }

    @Transactional(readOnly = true)
    public List<ArchivoAdjuntoResponse> findByEntidad(EntidadTipo entidadTipo, Long entidadId) {
        return archivoRepository.findByEntidadTipoAndEntidadId(entidadTipo, entidadId).stream()
            .map(a -> {
                String url = "fotos-pacientes".equals(a.getBucket())
                    ? buildPublicUrl(a.getBucket(), a.getRutaArchivo())
                    : null;
                return ArchivoMapper.toResponse(a, url);
            })
            .toList();
    }

    @Transactional(readOnly = true)
    public String getFileUrl(Long id) {
        ArchivoAdjunto archivo = archivoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Archivo", id));

        if ("fotos-pacientes".equals(archivo.getBucket())) {
            return buildPublicUrl(archivo.getBucket(), archivo.getRutaArchivo());
        }
        return storageService.getSignedUrl(archivo.getBucket(), archivo.getRutaArchivo());
    }

    @Transactional
    public void delete(Long id) {
        ArchivoAdjunto archivo = archivoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Archivo", id));

        storageService.delete(archivo.getBucket(), archivo.getRutaArchivo());
        archivoRepository.deleteById(id);
    }

    private String buildPublicUrl(String bucket, String rutaArchivo) {
        return supabaseUrl + "/storage/v1/object/public/" + bucket + "/" + rutaArchivo;
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "bin";
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
