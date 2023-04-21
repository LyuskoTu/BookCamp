
package com.turquoise.hotelbookrecomendation.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Booking {

    @SerializedName("user_hotel")
    @Expose
    private List<UserCamp> userCamps = new ArrayList<>();
    //todo aaa
    public List<UserCamp> getUserHotels() {
        return userCamps;
    }

    public void setUserHotels(List<UserCamp> userCamps) {
        this.userCamps = userCamps;
    }

}
