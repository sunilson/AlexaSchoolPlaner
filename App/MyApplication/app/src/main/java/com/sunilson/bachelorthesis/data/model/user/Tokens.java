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
    @SerializedName("accessToken")
    @Expose
    private String accessToken;

    @Getter
    @Setter
    @SerializedName("refreshToken")
    @Expose
    private String refreshToken;
}
