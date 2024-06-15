package com.protsdev.citizens;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.protsdev.citizens.services.VerifyFields;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class VerifyTests {

    @Autowired
    private VerifyFields verify;

    @Test
    void testValidEmail() {
        String rightEmail = "john.wick@example.com";
        String wrongEmail1 = "john.wick#example.com";
        String wrongEmail2 = "john.wick@";
        String wrongEmail3 = "@example.com";

        assertThat(verify.checkEmail(rightEmail)).isTrue();
        assertThat(verify.checkEmail(wrongEmail1)).isFalse();
        assertThat(verify.checkEmail(wrongEmail2)).isFalse();
        assertThat(verify.checkEmail(wrongEmail3)).isFalse();
    }

}
