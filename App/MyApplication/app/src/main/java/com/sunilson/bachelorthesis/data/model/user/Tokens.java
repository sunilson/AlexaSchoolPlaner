package com.sunilson.bachelorthesis.data.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Linus Weiss
 */

public class Tokens {

    @Getter
    @Setter
    @SerializedName("token_type")
    @Expose
    private String tokenType;

    @Getter
    @Setter
    @SerializedName("access_token")
    @Expose
    private String accessToken;

    @Getter
    @Setter
    @SerializedName("refresh_token")
    @Expose
    private String refreshToken;

    @Getter
    @Setter
    @SerializedName("expires_in")
    @Expose
    private Long expiresIn;

}
