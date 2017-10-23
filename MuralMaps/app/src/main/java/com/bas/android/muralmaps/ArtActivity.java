package com.bas.android.muralmaps;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import io.realm.Realm;

public class ArtActivity extends AppCompatActivity {

    private TextView name;
    private TextView artist;
    private TextView address;
    private Realm realm;
    private ImageView picture;
    private User owner;
    private Art art;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_art);

        name = (TextView) findViewById(R.id.art_name);
        artist = (TextView) findViewById(R.id.artist_name);
        address = (TextView) findViewById(R.id.location);
        realm = Realm.getDefaultInstance();
        picture = (ImageView) findViewById(R.id.art_image);

        String username = (String) getIntent().getStringExtra("username");
        owner = realm.where(User.class).equalTo("username", username).findFirst();

        String id = (String) getIntent().getStringExtra("art");
        art = realm.where(Art.class).equalTo("id",id).findFirst();
        name.setText(art.getName());
        artist.setText(art.getArtist());
        address.setText(art.getLocation());


        if(art.getImage() != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(art.getImage(), 0, art.getImage().length);
            picture.setImageBitmap(bmp);
        }
        name.setText(art.getName());

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the Realm instance.
        realm.close();
    }

}

