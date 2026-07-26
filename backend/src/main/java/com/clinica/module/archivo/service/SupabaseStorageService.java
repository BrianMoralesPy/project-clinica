package com.clinica.module.archivo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class SupabaseStorageService {

    private final RestTemplate restTemplate;
    private final String supabaseUrl;
    private final String serviceKey;

    public SupabaseStorageService(
            @Value("${supabase.url}") String supabaseUrl,
            @Value("${supabase.service-key}") String serviceKey) {
        this.restTemplate = new RestTemplate();
        this.supabaseUrl = supabaseUrl;
        this.serviceKey = serviceKey;
    }

    public void upload(String bucket, String path, byte[] content, String contentType) {
        String url = supabaseUrl + "/storage/v1/object/" + bucket + "/" + path;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + serviceKey);
        headers.setContentType(MediaType.parseMediaType(contentType));

        HttpEntity<byte[]> request = new HttpEntity<>(content, headers);

        try {
            restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        } catch (Exception e) {
            log.error("Error uploading file to Supabase: {}", e.getMessage());
            throw new RuntimeException("Error al subir archivo", e);
        }
    }

    public String getSignedUrl(String bucket, String path) {
        String url = supabaseUrl + "/storage/v1/object/sign/" + bucket + "/" + path;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + serviceKey);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Error getting signed URL: {}", e.getMessage());
            throw new RuntimeException("Error al obtener URL", e);
        }
    }

    public void delete(String bucket, String path) {
        String url = supabaseUrl + "/storage/v1/object/" + bucket + "/" + path;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + serviceKey);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);
        } catch (Exception e) {
            log.error("Error deleting file from Supabase: {}", e.getMessage());
        }
    }
}
