package com.example.vince.marketplaceapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.example.vince.marketplaceapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends Activity  {

    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference offerRef = rootRef.child("name");
    TextView offers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         offers = (TextView) findViewById(R.id.textViewOffers);

        Spinner spinner = (Spinner) findViewById(R.id.categories_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        //mNetworkFragment = NetworkFragment.getInstance(getSupportFragmentManager(), "https://www.google.com");


    }

    @Override
    protected void onStart() {
        super.onStart();

        offerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                offers.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void goToAddOfferScreen(View view){
        Intent intent = new Intent(this, AddOfferActivity.class);
        startActivity(intent);
    }

    public void goToDisplayOffer(View view)
    {
        Intent intent = new Intent(this, DisplayOffer.class);
        startActivity(intent);
    }

}
