package com.sunilson.bachelorthesis.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Linus Weiss
 */

@Entity(tableName = "Events")
@TypeConverters({com.sunilson.bachelorthesis.data.repository.database.TypeConverters.class})
public class EventEntity {

    @SerializedName("_id")
    @PrimaryKey
    @android.support.annotation.NonNull
    @Expose
    @Getter
    @Setter
    private String id;

    @SerializedName("description")
    @Expose
    @Getter
    @Setter
    private String description;

    @SerializedName("summary")
    @Expose
    @Getter
    @Setter
    private String summary;

    @SerializedName("from")
    @Expose
    @Getter
    @Setter
    private String from;

    @SerializedName("to")
    @Expose
    @Getter
    @Setter
    private String to;

    @SerializedName("location")
    @Expose
    @Getter
    @Setter
    private String location;

    @SerializedName("author")
    @Expose
    @Getter
    @Setter
    private String author;

    @SerializedName("type")
    @Expose
    @Getter
    @Setter
    private Integer type;

    @SerializedName("__v")
    @Expose
    @Getter
    @Setter
    private Integer v;
}
