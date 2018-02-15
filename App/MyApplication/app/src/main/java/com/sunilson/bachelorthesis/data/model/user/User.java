package com.sunilson.bachelorthesis.data.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Linus Weiss
 */

public class User {
    @Getter
    @Setter
    @SerializedName("id")
    @Expose
    private String id;

    @Getter
    @Setter
    @SerializedName("username")
    @Expose
    private String username;

    @Getter
    @Setter
    @SerializedName("email")
    @Expose
    private String email;

    @Getter
    @Setter
    @SerializedName("icalurl")
    @Expose
    private String icalurl;
}
