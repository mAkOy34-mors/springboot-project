package com.homeservices.app.controller;

import com.homeservices.app.dto.response.ApiResponse;
import com.homeservices.app.service.impl.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService fileStorageService;

    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        try {
            Path filePath = fileStorageService.loadFile(filename);
            
            System.out.println("=== FILE EXISTS: " + Files.exists(filePath));
            System.out.println("=== FILE READABLE: " + Files.isReadable(filePath));
            System.out.println("=== ABSOLUTE PATH: " + filePath.toAbsolutePath());

            if (!Files.exists(filePath)) {
                System.out.println("=== FILE NOT FOUND AT: " + filePath);
                return ResponseEntity.notFound().build();
            }

            Resource resource = new UrlResource(filePath.toUri());
            String contentType = determineContentType(filename);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + resource.getFilename() + "\"")
                    .header(HttpHeaders.CACHE_CONTROL, "max-age=86400")
                    .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*")
                    .body(resource);

        } catch (Exception e) {
            System.out.println("=== FILE ERROR: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<String>> uploadFile(
            @RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);
        String fileUrl = "http://localhost:8080/api/files/download/" + fileName;
        return ResponseEntity.ok(ApiResponse.ok("File uploaded successfully", fileUrl));
    }

    private String determineContentType(String filename) {
        String lower = filename.toLowerCase();
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
        if (lower.endsWith(".png"))  return "image/png";
        if (lower.endsWith(".gif"))  return "image/gif";
        if (lower.endsWith(".webp")) return "image/webp";
        if (lower.endsWith(".avif")) return "image/avif";
        if (lower.endsWith(".bmp"))  return "image/bmp";
        if (lower.endsWith(".pdf"))  return "application/pdf";
        return "application/octet-stream"; // ← fallback for all other files
    }
}