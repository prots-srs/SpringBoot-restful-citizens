package com.protsdev.citizens.models;

import java.sql.Date;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Embeddable
public class LifeStageDays {
    private Date birthDay;

    private Date deathDay;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((birthDay == null) ? 0 : birthDay.hashCode());
        result = prime * result + ((deathDay == null) ? 0 : deathDay.hashCode());
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
        LifeStageDays other = (LifeStageDays) obj;

        if (birthDay == null) {
            if (other.birthDay != null)
                return false;
        } else if (!birthDay.equals(other.birthDay))
            return false;
        if (deathDay == null) {
            if (other.deathDay != null)
                return false;
        } else if (!deathDay.equals(other.deathDay))
            return false;

        return true;
    }

}
