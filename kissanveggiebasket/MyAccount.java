package com.example.kissanveggiebasket;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MyAccount extends AppCompatActivity {

    private FirebaseAuth authProfile;
    private DatabaseReference usersRef;

    private ImageView image_profile;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private Button btnSave, btnEdit;
    private EditText name_editText, email_editText, des_editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        btnSave = findViewById(R.id.btn_save);
//        btnEdit = findViewById(R.id.btn_edit);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser user = authProfile.getCurrentUser();

        if (user != null) {
            usersRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
        }

        name_editText = findViewById(R.id.editText_name);
        email_editText = findViewById(R.id.editText_Email);
        des_editText = findViewById(R.id.editText_des);


        // Read data from Realtime Database
        if (usersRef != null) {
            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Retrieve user data
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String des = dataSnapshot.child("description").getValue(String.class);

                    // Update UI with retrieved data
                    updateUI(name, email, des);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save changes to the Realtime Database
                String newName = name_editText.getText().toString();
                String newEmail = email_editText.getText().toString();
                String newDes = des_editText.getText().toString();

                if (usersRef != null) {
                    usersRef.child("name").setValue(newName);
                    usersRef.child("email").setValue(newEmail);
                    usersRef.child("description").setValue(newDes);

                    // If the image is changed, update the image URL
                    if (imageUri != null) {
                        // Upload the new image to storage and update the URL
                        // ...
                    }

                    // Disable editing after saving
                    disableEditing();
                }
            }
        });

//        btnEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {

                image_profile = findViewById(R.id.imageView_profile);

                image_profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openFileChooser();
                    }
                });
        // Enable editing of fields
        enableEditing();
//            }
//        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            // Now you can upload the image to Firebase Storage and save the URL in the Realtime Database
            // ...
        }
    }

    public void sellerActivity(View view){
        startActivity(new Intent(MyAccount.this, Seller.class));
    }
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void updateUI(String name, String email, String des) {
            // Update UI components
            name_editText.setText(name);
            email_editText.setText(email);
            des_editText.setText(des);
        }
    private void enableEditing() {
        name_editText.setEnabled(true);
        email_editText.setEnabled(true);
        des_editText.setEnabled(true);
        image_profile.setEnabled(true);
    }

    private void disableEditing() {
        name_editText.setEnabled(false);
        email_editText.setEnabled(false);
        des_editText.setEnabled(false);
        image_profile.setEnabled(false);
    }
    }