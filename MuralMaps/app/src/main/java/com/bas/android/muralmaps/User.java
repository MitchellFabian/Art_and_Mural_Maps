package com.bas.android.muralmaps;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by Mitchell on 10/15/2017.
 */

public class User extends RealmObject implements Serializable{

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
