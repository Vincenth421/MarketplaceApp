package com.example.vince.marketplaceapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Config;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.vince.marketplaceapp.MainActivity;
import com.example.vince.marketplaceapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
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
    private int userNumber = 1;
    Button done;
    Button chooseImg;
    int PICK_IMAGE_REQUEST = 111;
    Uri filePath;
    ProgressDialog pd;

    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference nameRef;

    //creating reference to firebase storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://marketplaceapp-c265f.appspot.com");

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
        Spinner spinner = (Spinner) findViewById(R.id.spinner2);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array2, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        //mNetworkFragment = NetworkFragment.getInstance(getSupportFragmentManager(), "https://www.google.com");


        buttonLoadImage.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }});
        //Image stuff
        chooseImg = (Button)findViewById(R.id.loadimage);

        pd = new ProgressDialog(this);
        pd.setMessage("Uploading....");


        chooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
            }
        });
    }

    public void cancel(View view)
    {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
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

                        //Checks if name is filled in.
                        if (sUsername.matches("")) {
                            Toast.makeText(getBaseContext(), "You did not provide a name", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        sUsername = editTextPrice.getText().toString();

                        //Checks if price is filled in.
                        if (sUsername.matches("")) {
                            Toast.makeText(getBaseContext(), "You did not provide a price", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        sUsername = editTextDescription.getText().toString();

                        //Checks if description is filled in.
                        if (sUsername.matches("")) {
                            Toast.makeText(getBaseContext(), "You did not provide a description", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        sUsername = editTextEmail.getText().toString();
                        String sUsername2 = editTextPhone.getText().toString();

                        //Checks if contact info is filled in.
                        if (sUsername.matches("")&&sUsername2.matches("")) {
                            Toast.makeText(getBaseContext(), "You did not provide your contact information", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        //Returns to main menu
                        else {
                            //nameRef = rootRef.push().setValue(str);
                            //nameRef = rootRef.child("user");
                            String str = editTextName.getText().toString() + "," +
                                    editTextDescription.getText().toString() + "," + "$" + editTextPrice.getText().toString()
                                    + ",";
                            if(editTextEmail.getText().toString().equals(""))
                            {
                                str += "none,";
                                str += editTextPhone.getText().toString();
                            }
                            else if(editTextPhone.getText().toString().equals(""))
                            {
                                str += editTextEmail.getText().toString();
                                str += ",none";

                            }
                            rootRef.child("user2").setValue(str);
                            userNumber++;
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
    protected void onActivityResult ( int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            try {
                //getting image from gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                //Setting image to ImageView
                targetImage.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}



