package com.a.k.n.b.fileStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileUrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.net.MalformedURLException;
import java.net.URLEncoder;

@RestController
@RequestMapping("/api/file-storage")
public class FileStorageController {
    private final FileStorageService fileStorageService;
    @Value("${upload.path}")
    private String uploadPath;

    public FileStorageController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile multipartFile) {
        fileStorageService.save(multipartFile);
        return ResponseEntity.ok(multipartFile.getOriginalFilename() + ": file saqlandi.");
    }

    @GetMapping("/get-file/{token}")
    public ResponseEntity getFile(@PathVariable("token") String token) throws MalformedURLException {
        FileStorage fileStorage = fileStorageService.getFile(token);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; fileName=\"" + URLEncoder.encode(fileStorage.getName()))
                .contentType(MediaType.parseMediaType(fileStorage.getContentType()))
                .contentLength(fileStorage.getFileSize())
                .body(new FileUrlResource(String.format("/%s/%s", uploadPath, fileStorage.getUploadPath())));
    }
}
