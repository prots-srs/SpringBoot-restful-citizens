package com.protsdev.citizens.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue
    protected Long id;

    @Column(name = "created_on")
    @CreationTimestamp
    protected LocalDateTime createdOn;

    // @Column(name = "created_by")
    // protected String createdBy;

    @Column(name = "updated_on")
    @UpdateTimestamp
    protected LocalDateTime updatedOn;

    // @Column(name = "updated_by")
    // protected String updatedBy;
}

// @PrePersist
// protected void onCreate() {
// }

// @PreUpdate
// protected void onUpdate() {
// }
