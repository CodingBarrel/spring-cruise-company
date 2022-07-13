package ua.cruise.springcruise.service;

import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;
import ua.cruise.springcruise.repository.LinerRepository;
import ua.cruise.springcruise.util.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class StorageServiceTest {

    @InjectMocks
    StorageService storageService;

    @Mock
    LinerRepository linerRepository;

    @Mock
    MultipartFile file;

    @Mock
    InputStream inputStream;

    Path dirPath = Path.of(Constants.DATA_PATH + "/cruise/");

    String fileExt = "jpeg";

    @BeforeEach
    void setUp() {

    }

    @Test
    void saveSuccessCase() throws IOException {
        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            when(file.getContentType()).thenReturn(ContentType.IMAGE_JPEG.getMimeType());
            mockedFiles.when(() -> Files.exists(any(Path.class))).thenReturn(false).thenReturn(false);
            when(file.getInputStream()).thenReturn(inputStream);
            storageService.save(dirPath, fileExt, file);
            mockedFiles.verify(() -> Files.copy(any(InputStream.class), any(Path.class)), times(1));
        }
    }

    @Test
    void saveWrongFormatCase() {
        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            when(file.getContentType()).thenReturn(ContentType.TEXT_PLAIN.getMimeType());
            assertThrows(IOException.class, () -> storageService.save(dirPath, fileExt, file));
            mockedFiles.verify(() -> Files.copy(any(InputStream.class), any(Path.class)), times(0));
        }
    }

    @Test
    void deleteSuccessCase() throws IOException {
        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.deleteIfExists(dirPath)).thenReturn(true);
            storageService.delete(dirPath);
            mockedFiles.verify(() -> Files.deleteIfExists(any(Path.class)), times(1));
        }
    }

    @Test
    void deleteNotExistsCase() throws IOException {
        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.deleteIfExists(dirPath)).thenReturn(false);
            storageService.delete(dirPath);
            mockedFiles.verify(() -> Files.deleteIfExists(any(Path.class)), times(1));
        }
    }
}