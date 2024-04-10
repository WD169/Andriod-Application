package com.example.kissanveggiebasket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText email_id_sc, password_sc;
    Button btn_seller, btn_consumer;
//    DBHelper DB;
    private FirebaseAuth authProfile;
    String userType = String.valueOf(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_id_sc = (EditText) findViewById(R.id.email_id);
        password_sc = (EditText) findViewById(R.id.password);

        btn_seller = (Button) findViewById(R.id.btn_seller);
        btn_consumer = (Button) findViewById(R.id.btn_consumer);

//        DB = new DBHelper(this);
        authProfile = FirebaseAuth.getInstance();

            btn_seller.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        String email_id_con = email_id_sc.getText().toString();
                        String password_con = password_sc.getText().toString();

                        if (email_id_con.isEmpty() || password_con.isEmpty()) {
                            Toast.makeText(LoginActivity.this, "Please enter the email id and password", Toast.LENGTH_SHORT).show();
                        } else {
                            authProfile.signInWithEmailAndPassword(email_id_con, password_con).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        userType = Objects.requireNonNull(authProfile.getCurrentUser()).getDisplayName();
                                        Log.d("RegActivity", "User Type: " + userType);
                                        if (userType.equals("Seller")) {
                                            Toast.makeText(LoginActivity.this, "Signed as a seller successfully", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(LoginActivity.this, Seller.class));
                                            finish();
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Please check its Seller or Consumer credentials !!", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Sign In Failed! .. Invalid Credentials", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
            });


            btn_consumer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        String email_id_con = email_id_sc.getText().toString();
                        String password_con = password_sc.getText().toString();

                        if (email_id_con.isEmpty() || password_con.isEmpty()) {
                            Toast.makeText(LoginActivity.this, "Please enter the email id and password", Toast.LENGTH_SHORT).show();
                        } else {
                            authProfile.signInWithEmailAndPassword(email_id_con, password_con).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        userType = Objects.requireNonNull(authProfile.getCurrentUser()).getDisplayName();
                                        Log.d("LoginActivity", "User Type: " + userType);
                                        if (userType.equals("Consumer")) {
                                            Toast.makeText(LoginActivity.this, "Signed as a consumer successfully", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Please check its Seller or Consumer credentials !!", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Sign In Failed! .. Invalid Credentials", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
            });

    }
    public void register(View view){
        startActivity(new Intent(LoginActivity.this, RegActivity.class));
    }
//    public void mainActivity(View view) {
//        startActivity(new Intent(LoginActivity.this, MainActivity.class));
//    }
//    public void sellerActivity(View view) {
//        startActivity(new Intent(LoginActivity.this, Seller.class));
//    }

}