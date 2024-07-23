package com.protsdev.citizens.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.protsdev.citizens.models.FileUpload;

public interface FileUploadRepository extends JpaRepository<FileUpload, Long> {

}
