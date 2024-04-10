package com.example.kissanveggiebasket;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;

public class Seller extends AppCompatActivity {
    private FirebaseAuth authProfile;
    private DatabaseReference userRef;
    private Uri imageUri;
    public ImageView pro_image;
    public TextView name, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser user = authProfile.getCurrentUser();

        if (user != null) {
            userRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
        }
        pro_image = findViewById(R.id.profile_image);
        name = findViewById(R.id.textView8);
        email = findViewById(R.id.textView9);

        // Read data from Realtime Database
        if (userRef != null) {
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Retrieve user data
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String image = dataSnapshot.child("imageUri").getValue(String.class);

                    // Update UI with retrieved data
                    updateUI(name, email,image);

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    private void updateUI(String name, String email, String image) {
        this.name.setText(name);
        this.email.setText(email);

        // Load image into ImageView using a library like Glide or Picasso
        // Example using Glide:
        Glide.with(this)
                .load(image)
                .placeholder(R.drawable.baseline_account_box_24)
                .error(R.drawable.baseline_account_box_24)
                .into(pro_image);
    }

    public void addproduct(View view){
        startActivity(new Intent(Seller.this, AddProduct.class));
    }
    public void myproduct(View view){
        startActivity(new Intent(Seller.this, MyProduct.class));
    }
    public void mywallet(View view){
        startActivity(new Intent(Seller.this, MyWallet.class));
    }
    public void myaccount(View view){
        startActivity(new Intent(Seller.this, MyAccount.class));
    }
}
