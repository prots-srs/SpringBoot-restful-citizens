package com.protsdev.citizens.models;

import com.protsdev.citizens.enums.Citizenship;
import com.protsdev.citizens.enums.Gender;

import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;

import java.util.Set;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "CITIZENS")
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

    public Set<Marriage> getMarriages() {
        return marriages;
    }

    @OneToMany(mappedBy = "citizen", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private Set<Parenthood> parenthoods;

    public Set<Parenthood> getParenthoods() {
        return parenthoods;
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

    public CitizenName getNames() {
        return names;
    }

    public void setNames(CitizenName names) {
        this.names = names;
    }

    public LifeStageDays getDays() {
        return days;
    }

    public void setDays(LifeStageDays days) {
        this.days = days;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender sex) {
        this.gender = sex;
    }

    public Citizenship getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(Citizenship citizenship) {
        this.citizenship = citizenship;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((names == null) ? 0 : names.hashCode());
        result = prime * result + ((days == null) ? 0 : days.hashCode());
        result = prime * result + ((gender == null) ? 0 : gender.hashCode());
        result = prime * result + ((citizenship == null) ? 0 : citizenship.hashCode());
        // result = prime * result + ((marriages == null) ? 0 : marriages.hashCode());
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

        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
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
        // if (marriages == null) {
        // if (other.marriages != null)
        // return false;
        // } else if (!marriages.equals(other.marriages))
        // return false;

        return true;
    }

}