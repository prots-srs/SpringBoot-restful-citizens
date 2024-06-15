package com.protsdev.citizens.jsons;

import com.protsdev.citizens.enums.Citizenship;
import com.protsdev.citizens.enums.Gender;

public record CitizenJson(
                String firstName,
                String familyName,
                String birthDay,
                Gender gender,
                Citizenship citizenship) {
}
