package com.example.vince.marketplaceapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DisplayOffer extends AppCompatActivity {
    ImageView targetImage;
    TextView textViewName;
    TextView textViewDescription;
    TextView textViewPrice;
    TextView textViewEmail;
    TextView textViewPhone;

    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference nameRef;

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
    }
    @Override
    protected void onStart() {
        super.onStart();

        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String str = dataSnapshot.child("user1").getValue().toString();
                String[] store = str.split(",");
                textViewName.setText(store[0]);
                textViewDescription.setText(store[1]);
                textViewPrice.setText(store[2]);
                textViewEmail.setText(store[3]);
                textViewPhone.setText(store[4]);
                if(store[3].isEmpty()){
                    textViewEmail.setText("Not provided.");
                }
                if(store[4].isEmpty()){
                    textViewPhone.setText("Not provided.");
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

}
