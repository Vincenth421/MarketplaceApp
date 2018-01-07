package com.example.vince.marketplaceapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class DisplayOffer extends AppCompatActivity {
    ImageView targetImage;
    TextView textViewName;
    TextView textViewDescription;
    TextView textViewPrice;
    TextView textViewEmail;
    TextView textViewPhone;
    String key = "";
    LinearLayout layout;

    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

    StorageReference storageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_offer);
        targetImage = (ImageView)findViewById(R.id.targetimage);
        textViewName = (TextView)findViewById(R.id.textView);
        textViewDescription = (TextView) findViewById(R.id.textView2);
        textViewPrice = (TextView) findViewById(R.id.textView3);
        textViewEmail = (TextView) findViewById(R.id.textView5);
        textViewPhone = (TextView) findViewById(R.id.textView6);
        layout = (LinearLayout) findViewById(R.id)
    }

    @Override
    protected void onStart() {
        super.onStart();
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(key)) {
                    String str = dataSnapshot.child(key).getValue().toString();
                    storageRef.child(key);
                    String[] store = str.split(",");
                    textViewName.setText(store[0]);
                    textViewDescription.setText(store[1]);
                    textViewPrice.setText(store[2]);
                    textViewEmail.setText(store[3]);
                    textViewPhone.setText(store[4]);
                    if (store[3].equals("none")) {
                        textViewEmail.setText("Not provided.");
                    }
                    if (store[4].equals("none")) {
                        textViewPhone.setText("Not provided.");
                    }


                    storageRef = FirebaseStorage.getInstance().getReference().child(key + ".jpg");

                    // Load the image using Glide
                    try{
                        Glide.with(DisplayOffer.this)
                                .using(new FirebaseImageLoader())
                                .load(storageRef)
                                .into(targetImage);
                    }catch(NullPointerException e)
                    {

                    }


                }
            }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });

    }

    public void setKey(String key){
        this.key = key;
    }

}
