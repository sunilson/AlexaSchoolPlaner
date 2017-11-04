package com.sunilson.bachelorthesis.presentation.models;

import com.sunilson.bachelorthesis.presentation.utilities.ViewUtilities;

import java.util.Date;

/**
 * @author Linus Weiss
 *
 * Base Model class of a single event for the Presentation layer
 */

public class Event {

    private Date from, to;
    private String title;
    private int color;
    private int width = 1;

    public Event() {

    }

    public Event(Date from, Date to, String title) {
        this.from = from;
        this.to = to;
        this.title = title;
        this.color = ViewUtilities.randomColor();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
