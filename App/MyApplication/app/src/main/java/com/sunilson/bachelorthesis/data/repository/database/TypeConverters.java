package com.sunilson.bachelorthesis.data.repository.database;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.sunilson.bachelorthesis.data.model.user.Tokens;
import com.sunilson.bachelorthesis.data.model.user.User;

/**
 * @author Linus Weiss
 */

public class TypeConverters {

    @TypeConverter
    public static String toString(User user) {
        Gson gson = new Gson();
        return gson.toJson(user, User.class);
    }

    @TypeConverter
    public static User toUser(String string) {
        Gson gson = new Gson();
        return gson.fromJson(string, User.class);
    }

    @TypeConverter
    public static String toString(Tokens tokens) {
        Gson gson = new Gson();
        return gson.toJson(tokens, Tokens.class);
    }

    @TypeConverter
    public static Tokens toTokens(String string) {
        Gson gson = new Gson();
        return gson.fromJson(string, Tokens.class);
    }

}
