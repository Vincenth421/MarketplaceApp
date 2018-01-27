package com.example.vince.marketplaceapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
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

    LinearLayout layout;

    TextView textViewCategory;
    String userKey = "";

    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

    StorageReference storageRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_offer);
        targetImage = (ImageView)findViewById(R.id.targetimage);
        textViewName = (TextView)findViewById(R.id.textViewTitle);
        textViewDescription = (TextView) findViewById(R.id.textViewDes);
        textViewPrice = (TextView) findViewById(R.id.price);
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewPhone = (TextView) findViewById(R.id.textViewNumber);
        textViewCategory = (TextView) findViewById(R.id.category);

        /*if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                userKey = null;
            } else {
                userKey = extras.getString("KEY");
            }
        } else {
            userKey = (String) savedInstanceState.getSerializable("KEY");
        }*/

        userKey = (String) getIntent().getExtras().getString("KEY");

        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(userKey)) {
                    String str = dataSnapshot.child(userKey).getValue().toString();
                    String[] store = str.split(",");
                    textViewName.setText(store[0]);
                    textViewDescription.setText(store[1]);
                    textViewPrice.setText(store[2]);
                    textViewCategory.setText(store[3]);
                    if (store[4].equals("none")) {
                        textViewEmail.setText("Not provided.");
                    }
                    else{
                        textViewEmail.setText(store[4]);
                    }
                    if (store[5].equals("none")) {
                        textViewPhone.setText("Not provided.");
                    }
                    else{
                        textViewPhone.setText(store[5]);
                    }

                    storageRef = FirebaseStorage.getInstance().getReference().child(userKey + ".jpg");
                    Glide.with(DisplayOffer.this)
                            .using(new FirebaseImageLoader())
                            .load(storageRef)
                            .into(targetImage );
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

}
