package com.sunilson.bachelorthesis.domain.authentication.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Linus Weiss
 */

@AllArgsConstructor
public class DomainUser {

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String password;

}
