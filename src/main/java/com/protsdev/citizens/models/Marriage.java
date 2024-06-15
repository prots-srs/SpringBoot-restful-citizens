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

@Entity
@Table(name = "CITIZEN_MARRIAGES")
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

    public Citizen getCitizen() {
        return citizen;
    }

    public void setCitizen(Citizen citizen) {
        this.citizen = citizen;
    }

    @Embedded
    private DateRights dateRights;

    public boolean isNew() {
        return this.id == null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Citizen getPartner() {
        return partner;
    }

    public void setPartner(Citizen partner) {
        this.partner = partner;
    }

    public DateRights getDateRights() {
        return dateRights;
    }

    public void setDateRights(DateRights dateRights) {
        this.dateRights = dateRights;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((citizen == null) ? 0 : citizen.hashCode());
        // result = prime * result + ((partner == null) ? 0 : partner.hashCode());
        result = prime * result + ((dateRights == null) ? 0 : dateRights.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Marriage other = (Marriage) obj;

        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (citizen == null) {
            if (other.citizen != null)
                return false;
        } else if (!citizen.equals(other.citizen))
            return false;
        if (partner == null) {
            if (other.partner != null)
                return false;
        } else if (!partner.equals(other.partner))
            return false;
        if (dateRights == null) {
            if (other.dateRights != null)
                return false;
        } else if (!dateRights.equals(other.dateRights))
            return false;

        return true;
    }
}