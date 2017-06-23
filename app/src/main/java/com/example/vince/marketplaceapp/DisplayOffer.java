package com.example.vince.marketplaceapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.io.IOException;
import java.util.ArrayList;

public class DisplayOffer extends AppCompatActivity {
    ImageView targetImage;
    TextView textViewName;
    TextView textViewDescription;
    TextView textViewPrice;
    TextView textViewEmail;
    TextView textViewPhone;

    MainActivity main = new MainActivity();

    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference nameRef;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://marketplaceapp-c265f.appspot.com/").child("image.jpg");


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
                if(store[3].equals("none")){
                    textViewEmail.setText("Not provided.");
                }
                if(store[4].equals("none")){
                    textViewPhone.setText("Not provided.");
                }
                try {
                    final File localFile = File.createTempFile("images", "jpg");
                    storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            targetImage.setImageBitmap(bitmap);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                        }
                    });
                } catch (IOException e ) {}


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

}
