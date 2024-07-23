package com.protsdev.citizens.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.protsdev.citizens.models.FileUpload;

import java.nio.file.Path;
// import java.util.stream.Stream;

public interface StorageService {
    void init();

    void deleteAll();

    FileUpload store(MultipartFile file);

    Path load(String filename);

    Resource loadAsResource(String filename);

    // Stream<Path> loadAll();
}
