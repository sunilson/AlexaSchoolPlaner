package com.sunilson.bachelorthesis.domain.authentication.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Linus Weiss
 */

@AllArgsConstructor
public class DomainLoginData {

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String password;

}
