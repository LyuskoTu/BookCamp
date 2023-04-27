package com.turquoise.hotelbookrecomendation.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Camping implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("visits")
    @Expose
    private String visits;
    @SerializedName("completedBookings")
    @Expose
    private String completedBookings;
    @SerializedName("draftBookings")
    @Expose
    private String draftBookings;
    @SerializedName("tags")
    @Expose
    private String tags;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getVisits() {
        return visits;
    }

    public void setVisits(String visits) {
        this.visits = visits;
    }

    public String getCompletedBookings() {
        return completedBookings;
    }

    public void setCompletedBookings(String completedBookings) {
        this.completedBookings = completedBookings;
    }

    public String getDraftBookings() {
        return draftBookings;
    }

    public void setDraftBookings(String draftBookings) {
        this.draftBookings = draftBookings;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
