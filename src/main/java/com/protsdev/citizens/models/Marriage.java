package com.protsdev.citizens.models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "CITIZEN_MARRIAGES")
@Getter
@Setter
public class Marriage {
    private @Id @GeneratedValue Long id;

    @ManyToOne
    // (fetch = FetchType.LAZY)
    @JoinColumn(name = "citizen_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Citizen citizen;

    @ManyToOne
    @JoinColumn(name = "partner_id", referencedColumnName = "id")
    private Citizen partner;

    @Embedded
    private DateRights dateRights;

    public boolean isNew() {
        return this.id == null;
    }
}