package com.bas.android.muralmaps;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import io.realm.Realm;

public class ArtActivity extends AppCompatActivity {

    private TextView name;
    private TextView artist;
    private TextView longitude;
    private TextView latitude;
    private Realm realm;
    private ImageView picture;
    private User owner;
    private Art art;
    private Button likeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_art);

        name = (TextView) findViewById(R.id.art_name);
        artist = (TextView) findViewById(R.id.artist_name);
        longitude = (TextView) findViewById(R.id.longitude);
        latitude = (TextView) findViewById(R.id.latitude);
        realm = Realm.getDefaultInstance();
        picture = (ImageView) findViewById(R.id.art_image);
        likeButton = (Button) findViewById(R.id.like_button);

        String username = (String) getIntent().getStringExtra("username");
        owner = realm.where(User.class).equalTo("username", username).findFirst();

        String id = (String) getIntent().getStringExtra("art");
        art = realm.where(Art.class).equalTo("id",id).findFirst();
        name.setText(art.getName());
        artist.setText(art.getArtist());
        System.out.println(art.getName());
        System.out.println(art.getArtist());
        longitude.setText(Double.toString(art.getLng()));
        latitude.setText(Double.toString(art.getLat()));

        if(art.getImage() != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(art.getImage(), 0, art.getImage().length);
            picture.setImageBitmap(bmp);
        }
        name.setText(art.getName());

        likeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                realm.executeTransaction(new Realm.Transaction(){
                    @Override
                    public void execute(Realm realm){
                        art.setLike((art.getLike()^Boolean.TRUE));
                        art.setPopularity(art.getPopularity() + 1);
                        finish();
                    }
                });
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the Realm instance.
        realm.close();
    }

}

