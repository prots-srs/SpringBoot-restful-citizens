package com.protsdev.citizens.jsons;

import com.protsdev.citizens.enums.TypeParenthood;

public record ParenthoodJson(
        String citizen,
        String child,
        String dateOnRights,
        TypeParenthood parenthood) {

}
