package com.bas.android.muralmaps;


import com.google.android.gms.maps.model.LatLng;

import io.realm.RealmObject;


/**
 * Created by Mitchell on 10/15/2017.
 */

public class Art extends RealmObject{
    private String name;
    private String artist;
    private User user;
    private int likes;
    private String address;

    private LatLng location;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
