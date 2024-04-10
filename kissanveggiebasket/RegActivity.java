package com.example.kissanveggiebasket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegActivity extends AppCompatActivity {
    EditText full_sc, email_id_sc, password_sc;
    Button btn_seller, btn_consumer, btn_register;
    private RadioGroup radio_reg;
    private RadioButton radio_reg_selected, radio_sell, radio_con;
    private FirebaseAuth authProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        full_sc = (EditText) findViewById(R.id.full_name_sc);
        email_id_sc = (EditText) findViewById(R.id.email_id_sc);
        password_sc = (EditText) findViewById(R.id.password_sc);
        radio_sell = (RadioButton) findViewById(R.id.radiobutton_sell);
        radio_con = (RadioButton) findViewById(R.id.radiobutton_con);
        radio_reg = (RadioGroup) findViewById(R.id.radiogroup);
        radio_reg.clearCheck();

        btn_register = (Button) findViewById(R.id.btn_register);
//        btn_seller = (Button) findViewById(R.id.btn_seller);
//        btn_consumer = (Button) findViewById(R.id.btn_consumer);

        authProfile = FirebaseAuth.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_id = email_id_sc.getText().toString();
                String password = password_sc.getText().toString();
                String full_name_sc = full_sc.getText().toString();

                int select_radio_reg = radio_reg.getCheckedRadioButtonId();
                radio_reg_selected = findViewById(select_radio_reg);
//                String r_r_select = radio_reg_selected.getText().toString();
                
                Log.d("RegActivity", "Full Name: " + full_name_sc);
                Log.d("RegActivity", "Email ID: " + email_id);
                Log.d("RegActivity", "Password: " + password);
                Log.d("RegActivity", "Selected Radio: " + radio_reg_selected);

                final String userType = (radio_reg_selected == radio_sell) ? "Seller" : "Consumer";

                if (full_name_sc.isEmpty() || email_id.isEmpty() || password.isEmpty() || radio_reg_selected == null) {
                    Toast.makeText(RegActivity.this, "Please fill the required fields", Toast.LENGTH_SHORT).show();
                } else {
                    authProfile.createUserWithEmailAndPassword(email_id, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = authProfile.getCurrentUser();
                                assert user != null;
                                String userId = user.getUid();

                                // Save user information to Realtime Database
                                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                                usersRef.child(userId).child("email").setValue(email_id);
                                usersRef.child(userId).child("name").setValue(full_name_sc);
                                usersRef.child(userId).child("userType").setValue(userType);

                                Objects.requireNonNull(authProfile.getCurrentUser()).updateProfile(new UserProfileChangeRequest.Builder()
                                                .setDisplayName(userType)
                                                .build())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    // Reload the user profile to get the updated display name
                                                    authProfile.getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> reloadTask) {
                                                            if (reloadTask.isSuccessful()) {
                                                                // Now you can use getCurrentUser().getDisplayName() to get the updated display name
                                                                String updatedUserType = authProfile.getCurrentUser().getDisplayName();
                                                                Log.d("LoginActivity", "Updated User Type: " + updatedUserType);
                                                            } else {
                                                                Log.e("LoginActivity", "Failed to reload user profile: " + reloadTask.getException());
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    Log.e("LoginActivity", "Failed to update user profile: " + task.getException());
                                                }
                                            }
                                        });

                                // Retrieve user type after successful registration
//                                FirebaseUser user = authProfile.getCurrentUser();
//                                if (user != null) {
//                                    String userType = user.getDisplayName();
//                                    if (userType != null) {
//                                        Log.d("RegActivity", "User Type: " + userType);
//                                    }
//                                }

                                // Sign in success, update UI with the signed-in user's information
                                if (userType.equals("Seller")) {
                                    Toast.makeText(RegActivity.this, "Registered as a seller successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegActivity.this, Seller.class));
                                    finish();
                                } else {
                                    Toast.makeText(RegActivity.this, "Registered as a consumer successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegActivity.this, MainActivity.class));
                                    finish();
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(RegActivity.this, "Registration Failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


//                                              else if (userType.equals("Consumer")){
//                                                  authProfile.createUserWithEmailAndPassword(email_id, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                                                      @Override
//                                                      public void onComplete(@NonNull Task<AuthResult> task) {
//                                                          if (task.isSuccessful()) {
//                                                              authProfile.getCurrentUser().updateProfile(new UserProfileChangeRequest.Builder()
//                                                                      .setDisplayName(userType)
//                                                                      .build());
//                                                              // Sign in success, update UI with the signed-in user's information
//                                                              Toast.makeText(RegActivity.this, "Registered as a consumer successfully", Toast.LENGTH_SHORT).show();
//                                                              startActivity(new Intent(RegActivity.this, MainActivity.class));
//                                                          }
//                                                          else {
//                                                              // If sign in fails, display a message to the user.
//                                                              Toast.makeText(RegActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
//                                                          }
//                                                      }
//                                                  });
//                                              }
//                                          }
//                                      });
//
//        btn_consumer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String email_id_con = email_id_sc.getText().toString();
//                String password_con = password_sc.getText().toString();
//
//                if (email_id_con.equals(null) || password_con.equals(null))
//                    Toast.makeText(RegActivity.this, "Please enter the email id and password", Toast.LENGTH_SHORT).show();
//                else {
//                    authProfile.createUserWithEmailAndPassword(email_id_con, password_con).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//                                // Sign in success, update UI with the signed-in user's information
//                                Toast.makeText(RegActivity.this, "Registered as a consumer successfully", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(RegActivity.this, MainActivity.class));
//                            } else {
//                                // If sign in fails, display a message to the user.
//                                Toast.makeText(RegActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//                }
//            }
//        });
    }

    public void login(View view) {
        startActivity(new Intent(RegActivity.this, LoginActivity.class));
    }

//    public void mainActivity(View view) {
//        startActivity(new Intent(RegActivity.this, MainActivity.class));
//    }
//    public void sellerActivity(View view) {
//        startActivity(new Intent(RegActivity.this, Seller.class));
//    }

}