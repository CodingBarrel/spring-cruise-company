package ua.cruise.springcruise.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class StorageService {

    public String save(Path dirPath, String fileExt, MultipartFile file) throws IOException {
        if (!Files.exists(dirPath))
            Files.createDirectories(dirPath);
        String fileName = "";
        try (InputStream inputStream = file.getInputStream()) {
            Path filePath;
            do {
                fileName = UUID.randomUUID() + "." + fileExt;
                filePath = dirPath.resolve(fileName);
            } while (Files.exists(filePath));
            Files.copy(inputStream, filePath);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return fileName;
    }

    public void delete(Path path) throws IOException {
            Files.deleteIfExists(path);
    }
}
