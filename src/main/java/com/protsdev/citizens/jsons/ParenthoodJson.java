package com.protsdev.citizens.jsons;

import com.protsdev.citizens.enums.ParenthoodType;

public record ParenthoodJson(
        String citizen,
        String child,
        String dateOnRights,
        ParenthoodType parenthood) {

}
