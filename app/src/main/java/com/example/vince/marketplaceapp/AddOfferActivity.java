package com.example.vince.marketplaceapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Config;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.vince.marketplaceapp.MainActivity;
import com.example.vince.marketplaceapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class AddOfferActivity extends AppCompatActivity {

    ImageView targetImage;
    EditText editTextName;
    EditText editTextDescription;
    EditText editTextPrice;
    EditText editTextEmail;
    EditText editTextPhone;
    Uri filePath;
    long userNumber;
    ProgressDialog pd;
    long num = 0;
    String str;

    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference nameRef;

    // MY_PREFS_NAME - a static String variable like:
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public static final String MY_PREFS_NAME_2 = "MyPrefsFile2";

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

        pd = new ProgressDialog(this);
        pd.setMessage("Uploading....");

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

                        sUsername = editTextDescription.getText().toString();

                        //Checks if description is filled in.
                        if (sUsername.matches("")) {
                            Toast.makeText(getBaseContext(), "You did not provide a description", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        sUsername = editTextPrice.getText().toString();

                        //Checks if price is filled in.
                        if (sUsername.matches("")) {
                            Toast.makeText(getBaseContext(), "You did not provide a price", Toast.LENGTH_SHORT).show();
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
                            str = editTextName.getText().toString() + "," +
                                    editTextDescription.getText().toString() + "," + "$" + editTextPrice.getText().toString()
                                    + ",";
                            if(editTextEmail.getText().toString().equals(""))
                            {
                                str += "none,";
                                str += editTextPhone.getText().toString();
                            }
                            else if(editTextPhone.getText().toString().equals("")) {
                                str += editTextEmail.getText().toString();
                                str += ",none";
                            }

                            DatabaseReference newRef = rootRef.push();
                            newRef.setValue(str);

                            if(filePath != null) {
                                pd.show();

                                rootRef.runTransaction(new Transaction.Handler() {
                                    @Override
                                    public Transaction.Result doTransaction(MutableData mutableData) {
                                        if (mutableData.getValue() == null) {
                                            mutableData.setValue(1);
                                            userNumber= mutableData.getValue(Integer.class);
                                        } else {
                                            int count = mutableData.getValue(Integer.class);
                                            mutableData.setValue(count + 1);
                                            userNumber= mutableData.getValue(Integer.class);
                                        }
                                        return Transaction.success(mutableData);
                                    }

                                    @Override
                                    public void onComplete(DatabaseError databaseError, boolean success, DataSnapshot dataSnapshot) {
                                        // Analyse databaseError for any error during increment
                                    }
                                });

                                StorageReference childRef = storageRef.child(newRef.getKey() + ".jpg");

                                //uploading the image
                                UploadTask uploadTask = childRef.putFile(filePath);

                                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        pd.dismiss();
                                        Toast.makeText(AddOfferActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getBaseContext(), MainActivity.class));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        pd.dismiss();
                                        Toast.makeText(AddOfferActivity.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            else {
                                //Toast.makeText(AddOfferActivity.this, "Select an image", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getBaseContext(), MainActivity.class));
                            }

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

        if (resultCode == RESULT_OK) {
            filePath = data.getData();
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

    public void setChildrenCount(long i)
    {
        userNumber = i;
    }
}



