package com.protsdev.citizens.models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.protsdev.citizens.enums.TypeParenthood;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "CITIZEN_PARENTHOOD")
public class Parenthood {
    private @Id @GeneratedValue Long id;

    @ManyToOne
    @JoinColumn(name = "citizen_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Citizen citizen;

    @ManyToOne
    @JoinColumn(name = "child_id", referencedColumnName = "id")
    private Citizen child;

    @Embedded
    private DateRights dateRights;

    @Column(name = "type")
    private TypeParenthood parenthoodType;

    public TypeParenthood getParenthoodType() {
        return parenthoodType;
    }

    public void setParenthoodType(TypeParenthood parenthoodType) {
        this.parenthoodType = parenthoodType;
    }

    public boolean isNew() {
        return this.id == null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Citizen getCitizen() {
        return citizen;
    }

    public void setCitizen(Citizen citizen) {
        this.citizen = citizen;
    }

    public Citizen getChild() {
        return child;
    }

    public void setChild(Citizen child) {
        this.child = child;
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
        result = prime * result + ((child == null) ? 0 : child.hashCode());
        result = prime * result + ((dateRights == null) ? 0 : dateRights.hashCode());
        result = prime * result + ((parenthoodType == null) ? 0 : parenthoodType.hashCode());
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
        Parenthood other = (Parenthood) obj;

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
        if (child == null) {
            if (other.child != null)
                return false;
        } else if (!child.equals(other.child))
            return false;
        if (dateRights == null) {
            if (other.dateRights != null)
                return false;
        } else if (!dateRights.equals(other.dateRights))
            return false;
        if (parenthoodType == null) {
            if (other.parenthoodType != null)
                return false;
        } else if (!parenthoodType.equals(other.parenthoodType))
            return false;

        return true;
    }

}
