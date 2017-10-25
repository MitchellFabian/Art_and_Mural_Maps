package com.bas.android.muralmaps;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by Mitchell on 10/25/2017.
 */

public class MuralApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
