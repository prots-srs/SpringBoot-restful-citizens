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
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "CITIZEN_PARENTHOOD")
@Setter
@Getter
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

    @Column
    private TypeParenthood type;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        // result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((citizen == null) ? 0 : citizen.hashCode());
        result = prime * result + ((child == null) ? 0 : child.hashCode());
        result = prime * result + ((dateRights == null) ? 0 : dateRights.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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

        // if (id == null) {
        // if (other.id != null)
        // return false;
        // } else if (!id.equals(other.id))
        // return false;
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
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;

        return true;
    }

}
