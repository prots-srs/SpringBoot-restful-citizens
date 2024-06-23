package com.protsdev.citizens;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;

import com.protsdev.citizens.controllers.CitizenController;

@SpringBootTest
public class HateoasTest {

    @Test
    void link_test() {
        Link link = linkTo(CitizenController.class).withRel("family");

        System.out.println("-->> link:" + link);

        assertThat(link.getRel()).isEqualTo(LinkRelation.of("family"));
        assertThat(link.getHref()).endsWith("/api/family");
    }
}
