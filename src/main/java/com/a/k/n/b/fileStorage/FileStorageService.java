package com.a.k.n.b.fileStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;
import static com.a.k.n.b.fileStorage.FileStorageStatus.DRAFT;

@Service
public class FileStorageService {

    private final FileStorageRepository fileStorageRepository;

    @Value("${upload.path}")
    private String uploadPath;

    public FileStorageService(FileStorageRepository fileStorageRepository) {
        this.fileStorageRepository = fileStorageRepository;
    }

    public void save(MultipartFile multipartFile) {

        FileStorage fileStorage = new FileStorage();

        fileStorage.setName(multipartFile.getOriginalFilename());
        fileStorage.setExtension(getExt(multipartFile.getOriginalFilename()));
        fileStorage.setFileSize(multipartFile.getSize());
        fileStorage.setContentType(multipartFile.getContentType());
        fileStorage.setFileStorageStatus(DRAFT);

        UUID uuid = UUID.randomUUID();
        fileStorage.setToken(String.valueOf(uuid));

        String path = String.format("/uploaded_files/%s", getYMD());

        File file = new File(uploadPath + path);
        if (!file.exists() && file.mkdirs()){
            System.out.println("Folders created!");
        }
        String hashedName = String.format("%s.%s", fileStorage.getToken(), fileStorage.getExtension());
        fileStorage.setUploadPath(String.format("%s/%s", path, hashedName));
        fileStorageRepository.save(fileStorage);

        File file1 = new File(uploadPath, hashedName);

        try {
            multipartFile.transferTo(file1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileStorage getFile(String token) {
        return fileStorageRepository.findByToken(token).orElse(null);
    }



    // |-> SECONDARY FUNCTIONS <-|

    private String getExt(String fileName) {

        String ext = null;
        if (fileName != null && !fileName.isEmpty()) {
            int dot = fileName.lastIndexOf(".");
            if (dot > 0 && dot <= fileName.length() - 2) {
                ext = fileName.substring(dot + 1);
            }
        }
        return ext;
    }

    private String getYMD() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        return String.format("%s/%s/%s", year, month + 1, day);
    }
}
