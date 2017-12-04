package com.sunilson.bachelorthesis.data.model.user;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Linus Weiss
 */

@Entity(tableName = "Users")
@android.arch.persistence.room.TypeConverters({com.sunilson.bachelorthesis.data.repository.database.TypeConverters.class})
public class UserEntity {

    @PrimaryKey
    public int id = 1;

    @Getter
    @Setter
    @SerializedName("tokens")
    @Expose
    private Tokens tokens;

    @Getter
    @Setter
    @SerializedName("user")
    @Expose
    private User user;

}
