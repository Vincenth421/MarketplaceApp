package com.example.vince.marketplaceapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;


import android.app.FragmentManager;
import android.provider.ContactsContract;
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

import org.w3c.dom.Text;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends Activity  {

    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    LinearLayout listings;
    DisplayOffer displayOffer;
    TextView offer;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listings = (LinearLayout) findViewById(R.id.listingsLayout);

        spinner = (Spinner) findViewById(R.id.categories_spinner);
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
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                listings.removeAllViews();
                rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.hasChildren()) {
                            TextView offer = new TextView(MainActivity.this);
                            offer.setText("No Offers Yet");
                            offer.setTextSize(24);
                            offer.setTypeface(null, Typeface.BOLD);
                            offer.setClickable(false);
                            listings.addView(offer);
                        } else {
                            Spinner spinner = (Spinner) findViewById(R.id.categories_spinner);
                            int selectedItemOfMySpinner = spinner.getSelectedItemPosition();
                            String category = (String) spinner.getItemAtPosition(selectedItemOfMySpinner);
                            if(category.equals("All")) {
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    final Intent displayIntent = new Intent(MainActivity.this, DisplayOffer.class);
                                    String str = child.getValue().toString();
                                    Character c = ',';
                                    ShapeDrawable sd = new ShapeDrawable();

                                    // Specify the shape of ShapeDrawable
                                    sd.setShape(new RectShape());

                                    // Specify the border color of shape
                                    sd.getPaint().setColor(Color.BLUE);

                                    // Set the border width
                                    sd.getPaint().setStrokeWidth(5f);

                                    // Specify the style is a Stroke
                                    sd.getPaint().setStyle(Paint.Style.STROKE);

                                    offer = new TextView(MainActivity.this);
                                    offer.setText(str.substring(0, str.indexOf(c)));
                                    offer.setTextSize(24);
                                    offer.setTypeface(null, Typeface.BOLD);
                                    offer.setMaxEms(20);
                                    offer.setClickable(true);
                                    // Finally, add the drawable background to TextView
                                    offer.setBackground(sd);
                                    offer.setTag((String) child.getKey());
                                    offer.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            displayIntent.putExtra("KEY", offer.getTag().toString());
                                            startActivity(displayIntent);
                                        }
                                    });
                                    listings.addView(offer);
                                }
                            }
                            else{
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    String s = child.getValue().toString();
                                    String[] temp = s.split(",");

                                    if(temp[3].equals(category)) {
                                        final Intent displayIntent = new Intent(MainActivity.this, DisplayOffer.class);
                                        String str = child.getValue().toString();
                                        Character c = ',';
                                        ShapeDrawable sd = new ShapeDrawable();

                                        // Specify the shape of ShapeDrawable
                                        sd.setShape(new RectShape());

                                        // Specify the border color of shape
                                        sd.getPaint().setColor(Color.BLUE);

                                        // Set the border width
                                        sd.getPaint().setStrokeWidth(5f);

                                        // Specify the style is a Stroke
                                        sd.getPaint().setStyle(Paint.Style.STROKE);

                                        offer = new TextView(MainActivity.this);
                                        offer.setText(str.substring(0, str.indexOf(c)));
                                        offer.setTextSize(24);
                                        offer.setTypeface(null, Typeface.BOLD);
                                        offer.setMaxEms(20);
                                        offer.setClickable(true);
                                        // Finally, add the drawable background to TextView
                                        offer.setBackground(sd);
                                        offer.setTag((String) child.getKey());
                                        offer.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                displayIntent.putExtra("KEY", offer.getTag().toString());
                                                startActivity(displayIntent);
                                            }
                                        });
                                        listings.addView(offer);
                                    }
                                }
                            }

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });


    }


    public void goToAddOfferScreen(View view){
        Intent intent = new Intent(this, AddOfferActivity.class);
        startActivity(intent);
    }

}
