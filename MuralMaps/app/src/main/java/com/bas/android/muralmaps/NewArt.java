package com.bas.android.muralmaps;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import io.realm.Realm;

public class NewArt extends AppCompatActivity {

    private EditText artist;
    private Realm realm;
    private EditText name;
    private Button saveButton;
    private ImageButton imageButton;
    private EditText longitude;
    private EditText latitude;
    private final static int REQUEST_GALLERY = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_art);

        name = (EditText) findViewById(R.id.new_name);
        artist = (EditText) findViewById(R.id.new_artist);
        saveButton = (Button) findViewById(R.id.save_button);
        imageButton = (ImageButton) findViewById(R.id.image_button);
        longitude = (EditText) findViewById(R.id.new_longitude);
        latitude = (EditText) findViewById(R.id.new_latitude);
        realm = Realm.getDefaultInstance();

        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!name.getText().toString().matches("")
                        && !artist.getText().toString().matches("")
                        && imageButton.getDrawable() != null) {
                    realm.executeTransaction(new Realm.Transaction(){
                        @Override
                        public void execute(Realm realm){
                            Art art = new Art();
                            art.setArtist(artist.getText().toString());
                            art.setName(name.getText().toString());
                            art.setLng(Double.parseDouble(longitude.getText().toString()));
                            art.setLat(Double.parseDouble(latitude.getText().toString()));
                            art.setLike(Boolean.FALSE);
                            art.setPopularity(0);

                            //check to see if any Arts in the Realm
                            if (realm.where(Art.class).findAllSorted("id").isEmpty()) {
                                art.setId("0");
                            } else {
                                art.setId(realm.where(Art.class).findAllSorted("id").last().getId() + 1);
                            }

                            BitmapDrawable image = (BitmapDrawable) imageButton.getDrawable();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            image.getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] imageInByte = baos.toByteArray();
                            art.setImage(imageInByte);
                            realm.copyToRealm(art);
                            finish();
                        }
                    });
                }

            }
        });

        imageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, REQUEST_GALLERY);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("check","in activity result");
        Uri selectedPhoto = data.getData();
        System.out.println(selectedPhoto);
        Picasso.with(NewArt.this).load(selectedPhoto).into((ImageButton)findViewById(R.id.image_button));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the Realm instance.
        realm.close();
    }
}

