package com.protsdev.citizens.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "FILE_UPLOAD")
@Setter
@Getter
public class FileUpload {
  @Id
  @GeneratedValue
  private Long id;

  @NotBlank
  private String fileType;

  @NotNull
  private Long fileSize;

  @NotBlank
  private String originalName;

  @NotBlank
  private String savedName;
}
