package com.example.vince.marketplaceapp;

import android.app.Activity;
import android.media.Image;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by vince on 12/25/2017.
 */

public class OfferFragment extends FragmentActivity{

    TextView title = (TextView) findViewById(R.id.textTitle);
    TextView price = (TextView) findViewById(R.id.editTextPrice);

    /*@Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.offer_layout,container,false);
        return view;
    }*/

    public void setTitle(String str){
        title.setText(str);
    }

    public void setPrice(String str){
        title.setText(str);
    }
}
