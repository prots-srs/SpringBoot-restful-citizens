package com.protsdev.citizens;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.protsdev.citizens.enums.Gender;
import com.protsdev.citizens.enums.ParenthoodType;
import com.protsdev.citizens.jsons.FullJson;
import com.protsdev.citizens.services.JsonLoaderService;

@SpringBootTest
public class DevLoadJson {

    @Autowired
    JsonLoaderService jsonLoader;

    @Test
    void load_json() {
        Optional<FullJson> json = jsonLoader.readJsonFromFile("demo.json");
        assertThat(json.isPresent()).isTrue();

        FullJson js;
        if (json.isPresent()) {
            js = json.get();

            // citizens
            assertThat(js.citizens().get(0).familyName()).isEqualTo("Addams");
            assertThat(js.citizens().get(0).birthDay()).isEqualTo("1970-03-30");
            assertThat(js.citizens().get(0).gender()).isEqualTo(Gender.MALE);
            assertThat(js.citizens().get(1).familyName()).isEqualTo("Simpson");
            assertThat(js.citizens().get(2).firstName()).isEqualTo("Wednesday");
            assertThat(js.citizens().get(3).firstName()).isEqualTo("Pugsley");

            // marriage
            assertThat(js.marriages().get(0).dateOnRights()).isEqualTo("1992-07-17");

            // parenthood
            assertThat(js.parenthoods().get(1).dateOnRights()).isEqualTo("1995-02-16");
            assertThat(js.parenthoods().get(1).parenthood()).isEqualTo(ParenthoodType.BIRTHMOTHER);
        }
    }
}
