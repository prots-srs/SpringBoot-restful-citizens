package com.protsdev.citizens.services;

import java.util.regex.Pattern;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Service
// @Configuration
public class VerifyFields {

    private final String EMAIL_PATTERN = "^(.+)@(\\S+)$";

    // @Bean
    // public VerifyFields verifyFields() {
    // return new VerifyFields();
    // }

    public boolean checkEmail(String email) {
        return Pattern.compile(this.EMAIL_PATTERN)
                .matcher(email)
                .matches();
    }
}
