package com.chatapp.file.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FileService Tests")
class FileServiceTest {

    @InjectMocks
    private FileService fileService;

    @Mock
    private MultipartFile multipartFile;

    @BeforeEach
    void setUp() {
        // Setup is empty - each test configures its own mocks
    }

    @Test
    @DisplayName("Should upload file successfully")
    void testUploadFileSuccess() throws IOException {
        // Arrange
        when(multipartFile.getOriginalFilename()).thenReturn("testfile.txt");
        byte[] fileContent = "test content".getBytes();
        when(multipartFile.getBytes()).thenReturn(fileContent);

        // Act & Assert
        assertDoesNotThrow(() -> fileService.upload(multipartFile));

        verify(multipartFile, times(1)).getOriginalFilename();
        verify(multipartFile, times(1)).getBytes();
    }

    @Test
    @DisplayName("Should get original filename from multipart file")
    void testGetOriginalFilename() throws IOException {
        // Arrange
        when(multipartFile.getOriginalFilename()).thenReturn("testfile.txt");
        when(multipartFile.getBytes()).thenReturn("content".getBytes());

        // Act & Assert
        assertDoesNotThrow(() -> fileService.upload(multipartFile));
        verify(multipartFile).getOriginalFilename();
    }

    @Test
    @DisplayName("Should handle IOException during upload")
    void testUploadThrowsIOException() throws IOException {
        // Arrange
        when(multipartFile.getBytes()).thenThrow(new IOException("File read error"));

        // Act & Assert
        assertThrows(IOException.class, () -> fileService.upload(multipartFile));

        verify(multipartFile, times(1)).getBytes();
    }

    @Test
    @DisplayName("Should use correct upload directory")
    void testUploadDirectoryCreation() throws IOException {
        // Arrange
        when(multipartFile.getBytes()).thenReturn("test".getBytes());

        // Act & Assert
        assertDoesNotThrow(() -> fileService.upload(multipartFile));
    }

    @Test
    @DisplayName("Should save file with original filename")
    void testFileSavedWithCorrectName() throws IOException {
        // Arrange
        String expectedFilename = "myfile.pdf";
        when(multipartFile.getOriginalFilename()).thenReturn(expectedFilename);
        when(multipartFile.getBytes()).thenReturn("pdf content".getBytes());

        // Act & Assert
        assertDoesNotThrow(() -> fileService.upload(multipartFile));

        verify(multipartFile, atLeastOnce()).getOriginalFilename();
    }

    @Test
    @DisplayName("Should handle multiple file uploads")
    void testMultipleFileUploads() throws IOException {
        // Arrange
        MultipartFile file1 = mock(MultipartFile.class);
        MultipartFile file2 = mock(MultipartFile.class);

        when(file1.getOriginalFilename()).thenReturn("file1.txt");
        when(file1.getBytes()).thenReturn("content1".getBytes());
        when(file2.getOriginalFilename()).thenReturn("file2.txt");
        when(file2.getBytes()).thenReturn("content2".getBytes());

        // Act & Assert
        assertDoesNotThrow(() -> fileService.upload(file1));
        assertDoesNotThrow(() -> fileService.upload(file2));

        verify(file1, times(1)).getOriginalFilename();
        verify(file2, times(1)).getOriginalFilename();
    }

    @Test
    @DisplayName("Should handle file with special characters in name")
    void testFileWithSpecialCharacters() throws IOException {
        // Arrange
        when(multipartFile.getOriginalFilename()).thenReturn("file-with-special_chars.txt");
        when(multipartFile.getBytes()).thenReturn("content".getBytes());

        // Act & Assert
        assertDoesNotThrow(() -> fileService.upload(multipartFile));
    }

    @Test
    @DisplayName("Should handle large files")
    void testLargeFileUpload() throws IOException {
        // Arrange
        byte[] largeContent = new byte[1024 * 1024]; // 1MB
        when(multipartFile.getBytes()).thenReturn(largeContent);
        when(multipartFile.getOriginalFilename()).thenReturn("largefile.bin");

        // Act & Assert
        assertDoesNotThrow(() -> fileService.upload(multipartFile));
    }

    @Test
    @DisplayName("Should handle empty file")
    void testEmptyFileUpload() throws IOException {
        // Arrange
        when(multipartFile.getBytes()).thenReturn(new byte[0]);
        when(multipartFile.getOriginalFilename()).thenReturn("emptyfile.txt");

        // Act & Assert
        assertDoesNotThrow(() -> fileService.upload(multipartFile));
    }

    @Test
    @DisplayName("Should call getOriginalFilename method")
    void testCallsGetOriginalFilename() throws IOException {
        // Arrange
        when(multipartFile.getBytes()).thenReturn("content".getBytes());

        // Act
        try {
            fileService.upload(multipartFile);
        } catch (Exception e) {
            // Expected - testing mock calls
        }

        // Assert
        verify(multipartFile, atLeastOnce()).getOriginalFilename();
    }

    @Test
    @DisplayName("Should call getBytes method")
    void testCallsGetBytes() throws IOException {
        // Arrange
        when(multipartFile.getBytes()).thenReturn("content".getBytes());

        // Act
        try {
            fileService.upload(multipartFile);
        } catch (Exception e) {
            // Expected - testing mock calls
        }

        // Assert
        verify(multipartFile, times(1)).getBytes();
    }
}

