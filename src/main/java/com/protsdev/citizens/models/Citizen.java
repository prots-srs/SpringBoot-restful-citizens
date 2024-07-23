package com.protsdev.citizens.models;

import com.protsdev.citizens.enums.Citizenship;
import com.protsdev.citizens.enums.Gender;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "CITIZENS")
@Setter
@Getter
@ToString
public class Citizen {

    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private CitizenName names;
    @Embedded
    private LifeStageDays days;

    private Gender gender;
    private Citizenship citizenship;

    @OneToMany(mappedBy = "citizen", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private Set<Marriage> marriages;

    @OneToMany(mappedBy = "citizen", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private Set<Parenthood> parenthoods;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "avatar_id", referencedColumnName = "id")
    private FileUpload avatar;

    public boolean isNew() {
        return this.id == null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((names == null) ? 0 : names.hashCode());
        result = prime * result + ((days == null) ? 0 : days.hashCode());
        result = prime * result + ((gender == null) ? 0 : gender.hashCode());
        result = prime * result + ((citizenship == null) ? 0 : citizenship.hashCode());
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
        Citizen other = (Citizen) obj;

        if (names == null) {
            if (other.names != null)
                return false;
        } else if (!names.equals(other.names))
            return false;
        if (days == null) {
            if (other.days != null)
                return false;
        } else if (!days.equals(other.days))
            return false;
        if (gender != other.gender)
            return false;
        if (citizenship != other.citizenship)
            return false;

        return true;
    }

}