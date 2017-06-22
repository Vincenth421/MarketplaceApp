package com.example.vince.marketplaceapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.Nullable;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Config;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.vince.marketplaceapp.MainActivity;
import com.example.vince.marketplaceapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileNotFoundException;
import java.io.InputStream;


public class AddOfferActivity extends AppCompatActivity {

    private static int RESULT_LOAD_IMG = 1;
    ImageView targetImage;
    EditText editTextName;
    EditText editTextDescription;
    EditText editTextPrice;
    EditText editTextEmail;
    EditText editTextPhone;
    Button done;

    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference nameRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offer);
        Button buttonLoadImage = (Button)findViewById(R.id.loadimage);
        targetImage = (ImageView)findViewById(R.id.targetimage);
        editTextName = (EditText)findViewById(R.id.editTextName);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        editTextPrice = (EditText) findViewById(R.id.editTextPrice);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);

        buttonLoadImage.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }});
    }


    //Pop-up to confirm order.
    public void confirmOrder(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Confirm Order");
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String sUsername = editTextName.getText().toString();

                        //Checks if all required fields are filled in.
                        if (sUsername.matches("")) {
                            Toast.makeText(getBaseContext(), "You did not provide a name", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        sUsername = editTextPrice.getText().toString();

                        //Checks if all required fields are filled in.
                        if (sUsername.matches("")) {
                            Toast.makeText(getBaseContext(), "You did not provide a price", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        sUsername = editTextDescription.getText().toString();

                        //Checks if all required fields are filled in.
                        if (sUsername.matches("")) {
                            Toast.makeText(getBaseContext(), "You did not provide a description", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        sUsername = editTextEmail.getText().toString();
                        String sUsername2 = editTextPhone.getText().toString();

                        //Checks if all required fields are filled in.
                        if (sUsername.matches("")&&sUsername2.matches("")) {
                            Toast.makeText(getBaseContext(), "You did not provide your contact information", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        //Returns to main menu
                        else {
                            nameRef = rootRef.push();
                            nameRef = rootRef.child("user");
                            String str = editTextName.toString() + "," +
                                    editTextDescription.toString() + "," + editTextPrice.toString()
                                    + "," + editTextEmail.toString() + "," + editTextPhone.toString();
                            nameRef.setValue(str);
                            startActivity(new Intent(getBaseContext(), MainActivity.class));
                        }

                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri targetUri = data.getData();
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                targetImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


}
