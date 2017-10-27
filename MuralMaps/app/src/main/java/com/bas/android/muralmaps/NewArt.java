package com.bas.android.muralmaps;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.ByteArrayOutputStream;

import io.realm.Realm;

public class NewArt extends AppCompatActivity {

    private EditText artist;
    private Realm realm;
    private EditText name;
    private Button saveButton;
    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_art);

        name = (EditText) findViewById(R.id.new_name);
        artist = (EditText) findViewById(R.id.new_artist);
        saveButton = (Button) findViewById(R.id.save_button);
        imageButton = (ImageButton) findViewById(R.id.image_button);
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
                            art.setId(realm.where(Art.class).findAllSorted("id").last().getId() + 1);
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
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(takePictureIntent.resolveActivity(getPackageManager())!= null){
                    startActivityForResult(takePictureIntent, 1);
                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageButton.setImageBitmap(imageBitmap);
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the Realm instance.
        realm.close();
    }
}

