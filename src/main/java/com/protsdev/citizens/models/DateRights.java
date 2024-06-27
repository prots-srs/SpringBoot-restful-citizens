package com.protsdev.citizens.models;

import java.sql.Date;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Embeddable
public class DateRights {

    @Column(name = "start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDay;

    @Column(name = "end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDay;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((startDay == null) ? 0 : startDay.hashCode());
        result = prime * result + ((endDay == null) ? 0 : endDay.hashCode());
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
        DateRights other = (DateRights) obj;

        if (startDay == null) {
            if (other.startDay != null)
                return false;
        } else if (!startDay.equals(other.startDay))
            return false;
        if (endDay == null) {
            if (other.endDay != null)
                return false;
        } else if (!endDay.equals(other.endDay))
            return false;

        return true;
    }
}
