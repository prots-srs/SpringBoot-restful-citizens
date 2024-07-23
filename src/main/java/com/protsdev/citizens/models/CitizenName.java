package com.protsdev.citizens.models;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Embeddable
public class CitizenName {
    private String firstName;
    private String familyName;
    private String maidenName;
    private String secondName;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((familyName == null) ? 0 : familyName.hashCode());
        result = prime * result + ((maidenName == null) ? 0 : maidenName.hashCode());
        result = prime * result + ((secondName == null) ? 0 : secondName.hashCode());
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
        CitizenName other = (CitizenName) obj;

        if (firstName == null) {
            if (other.firstName != null)
                return false;
        } else if (!firstName.equals(other.firstName))
            return false;
        if (familyName == null) {
            if (other.familyName != null)
                return false;
        } else if (!familyName.equals(other.familyName))
            return false;
        if (maidenName == null) {
            if (other.maidenName != null)
                return false;
        } else if (!maidenName.equals(other.maidenName))
            return false;
        if (secondName == null) {
            if (other.secondName != null)
                return false;
        } else if (!secondName.equals(other.secondName))
            return false;

        return true;
    }

    @Override
    public String toString() {
        String name = firstName + " " + familyName;
        if (maidenName.length() > 0) {
            name = name + " (" + maidenName + ")";
        }
        if (secondName.length() > 0) {
            name = name + " " + secondName;
        }
        return name;
    }

}
