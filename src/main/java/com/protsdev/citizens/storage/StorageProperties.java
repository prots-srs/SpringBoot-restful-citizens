package com.protsdev.citizens.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@ConfigurationProperties("storage")
public class StorageProperties {
    @NotBlank
    private String location = "uploads";
}
