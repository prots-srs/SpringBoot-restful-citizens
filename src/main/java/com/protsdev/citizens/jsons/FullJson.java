package com.protsdev.citizens.jsons;

import java.util.List;

public record FullJson(
        List<CitizenJson> citizens,
        List<MarriageJson> marriages,
        List<ParenthoodJson> parenthoods) {
}
