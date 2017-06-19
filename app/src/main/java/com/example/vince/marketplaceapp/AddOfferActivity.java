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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.vince.marketplaceapp.MainActivity;
import com.example.vince.marketplaceapp.R;

import java.io.FileNotFoundException;
import java.io.InputStream;


public class AddOfferActivity extends Activity {

    private static int RESULT_LOAD_IMG = 1;
    ImageView targetImage;




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
                        EditText usernameEditText = (EditText) findViewById(R.id.editText4);
                        String sUsername = usernameEditText.getText().toString();

                        //Checks if all required fields are filled in.
                        if (sUsername.matches("")) {
                            Toast.makeText(getBaseContext(), "You did not provide a name", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        usernameEditText = (EditText) findViewById(R.id.editText10);
                        sUsername = usernameEditText.getText().toString();

                        //Checks if all required fields are filled in.
                        if (sUsername.matches("")) {
                            Toast.makeText(getBaseContext(), "You did not provide a price", Toast.LENGTH_SHORT).show();
                            return;
                        }


                        usernameEditText = (EditText) findViewById(R.id.editText5);
                        sUsername = usernameEditText.getText().toString();

                        //Checks if all required fields are filled in.
                        if (sUsername.matches("")) {
                            Toast.makeText(getBaseContext(), "You did not provide a description", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        usernameEditText = (EditText) findViewById(R.id.editText3);
                        sUsername = usernameEditText.getText().toString();
                        EditText usernameEditText2 = (EditText) findViewById(R.id.editText6);
                        String sUsername2 = usernameEditText2.getText().toString();

                        //Checks if all required fields are filled in.
                        if (sUsername.matches("")&&sUsername2.matches("")) {
                            Toast.makeText(getBaseContext(), "You did not provide your contact information", Toast.LENGTH_SHORT).show();
                            return;
                        }





                        //Returns to main menu
                        else {
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offer);
        Button buttonLoadImage = (Button)findViewById(R.id.loadimage);
        //textTargetUri = (TextView)findViewById(R.id.targeturi);
        targetImage = (ImageView)findViewById(R.id.targetimage);

        buttonLoadImage.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }});
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
