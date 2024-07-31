package com.protsdev.citizens.storage;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import java.util.Optional;
import java.time.LocalDateTime;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.protsdev.citizens.models.FileUpload;
import com.protsdev.citizens.repositories.FileUploadRepository;

@Service
public class FileSystemStorageService implements StorageService {

    private Map<String, String> messages = new HashMap<>();

    private final Path rootLocation;
    private final FileUploadRepository fileUploadRepository;

    public FileSystemStorageService(
            FileUploadRepository fileUploadRepository,
            StorageProperties properties) {

        messages.put("error_dir_location", "File upload location can not be Empty.");
        messages.put("error_dir_create", "Could not initialize storage");
        messages.put("error_store_file_empty", "Failed to store empty file.");
        messages.put("error_store_file", "Failed to store file.");
        messages.put("error_store_file_outside", "Cannot store file outside current directory.");
        // messages.put("error_store_file_read", "Failed to read stored files");
        messages.put("error_read_file", "Could not read file: ");

        if (properties.getLocation().trim().length() == 0) {
            throw new StorageException(messages.get("error_dir_location"));
        }

        this.fileUploadRepository = fileUploadRepository;
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException(messages.get("error_dir_create"), e);
        }
    }

    @Override
    public FileUpload store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException(messages.get("error_store_file_empty"));
            }

            String uniqueFileName = UUID.randomUUID().toString() + "-" + LocalDateTime.now().getNano();

            var ext = getExtensionByStringHandling(file.getOriginalFilename());
            if (ext.isPresent()) {
                uniqueFileName = uniqueFileName + "." + ext.get();
            }

            Path destinationFile = rootLocation.resolve(Paths.get(uniqueFileName))
                    .normalize().toAbsolutePath();

            // This is a security check
            if (!destinationFile.getParent().equals(rootLocation.toAbsolutePath())) {
                throw new StorageException(messages.get("error_store_file_outside"));
            }

            // save
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);

                var fileEntity = new FileUpload();
                fileEntity.setOriginalName(file.getOriginalFilename());
                fileEntity.setFileType(file.getContentType());
                fileEntity.setFileSize(file.getSize());
                fileEntity.setSavedName(uniqueFileName);

                var savedFile = fileUploadRepository.save(fileEntity);
                return savedFile;
            }
        } catch (IOException e) {
            throw new StorageException(messages.get("error_store_file"), e);
        }

    }

    private Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    // @Override
    // public Stream<Path> loadAll() {
    // try {
    // return Files.walk(this.rootLocation, 1)
    // .filter(path -> !path.equals(this.rootLocation))
    // .map(this.rootLocation::relativize);
    // } catch (IOException e) {
    // throw new StorageException(messages.get("error_store_file_read"), e);
    // }
    // }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException(messages.get("error_read_file") + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException(messages.get("error_read_file") + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

}
