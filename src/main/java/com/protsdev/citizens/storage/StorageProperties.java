package com.protsdev.citizens.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
// @Configuration
@ConfigurationProperties("storage")
public class StorageProperties {
  @NotBlank
  private String location = "uploads";
}