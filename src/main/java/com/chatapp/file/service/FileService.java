package com.chatapp.file.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FileService {

    public void upload(MultipartFile file) throws Exception {
        Files.createDirectories(Paths.get("uploads"));
        Files.write(Paths.get("uploads/" + file.getOriginalFilename()),
                file.getBytes());
    }
}

