package com.suryakiran.firstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class sign_up_details extends AppCompatActivity {
    EditText name,email,phonenumber,profession,password;
    Button signup,vermail;
    ProgressBar progressBar;
    DatabaseReference databaseReference;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_details);
        name=findViewById(R.id.name);
        email=findViewById(R.id.Email);
        phonenumber=findViewById(R.id.Phoneno);
        password=findViewById(R.id.Password);
        profession=findViewById(R.id.Profession);
        signup=findViewById(R.id.Signup);
        progressBar=findViewById(R.id.probar);
        vermail=findViewById(R.id.vermail);
        FirebaseAuth auth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        final String[] UID = new String[1];
        signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(phonenumber.getText().toString().length()!=10)
                {
                    phonenumber.setError("Enter 10 digit Phone Number");
                    phonenumber.setHighlightColor(Color.RED);
                    phonenumber.requestFocus();
                }
                else if(!email.getText().toString().matches("^.+@gmail.com$") && !email.getText().toString().matches("^.+@.ac.in$"))
                {
                    email.setError("Enter valid gmail");
                    email.requestFocus();
                }
                else if (name.getText().toString().length()==0)
                {
                    name.setError("Please enter name");
                    name.requestFocus();
                }
                else if (!password.getText().toString().matches("^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[@#%&-+=())]).{8,20}$"))
                {
                    password.setError("Password must contain Atleast One Captail letter, One Small Letter, One Number, One special Character");
                    password.requestFocus();
                }
                else
                {
                    progressBar.setVisibility(View.VISIBLE);
                    PhoneAuthOptions options =
                            PhoneAuthOptions.newBuilder(auth)
                                    .setPhoneNumber("+91"+phonenumber.getText().toString())       // Phone number to verify
                                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                    .setActivity(sign_up_details.this)                 // Activity (for callback binding)
                                    .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                    .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);
                }
            }
        });
        mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                /*auth.createUserWithEmailAndPassword(phonenumber.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            credentials cre=new credentials(name.getText().toString(),phonenumber.getText().toString(),email.getText().toString(),profession.getText().toString(),FirebaseAuth.getInstance().getUid());
                            databaseReference.child(FirebaseAuth.getInstance().getUid()).child("credential").setValue(cre);
                            UID[0] =FirebaseAuth.getInstance().getUid().toString();
                            Intent intent = new Intent(sign_up_details.this, DashBoard.class);
                            startActivity(intent);
                        }
                    }
                });*/
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(sign_up_details.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                progressBar.setVisibility(View.GONE);
                Intent intent=new Intent(sign_up_details.this,otp_verification.class);
                intent.putExtra("OTP",s);
                intent.putExtra("email",email.getText().toString().trim());
                intent.putExtra("password",password.getText().toString());
                intent.putExtra("profession",profession.getText().toString());
                intent.putExtra("name",name.getText().toString());
                intent.putExtra("phonenumber",phonenumber.getText().toString());
                startActivity(intent);
            }
        };
        vermail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                auth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task1) {
                                    if (task1.isSuccessful()) {
                                        Log.d("HAI SURYA", "ENTERED");
                                        Toast.makeText(sign_up_details.this, "Verifacation Link sent to " + auth.getCurrentUser().getEmail()+" Login after verification", Toast.LENGTH_SHORT).show();
                                            credentials cre=new credentials(name.getText().toString(),phonenumber.getText().toString(),email.getText().toString(),profession.getText().toString(),FirebaseAuth.getInstance().getUid());
                                            databaseReference.child(FirebaseAuth.getInstance().getUid()).child("credential").setValue(cre);
                                            databaseReference.child(FirebaseAuth.getInstance().getUid()).child("naming").setValue(name.getText().toString());
                                            FirebaseAuth.getInstance().signOut();
                                            Intent intent = new Intent(sign_up_details.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    else {
                                        Toast.makeText(sign_up_details.this, task1.getException().toString(), Toast.LENGTH_LONG).show();
                                    }

                                }
                            });

                        }
                        else
                        {
                            Toast.makeText(sign_up_details.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                    }
        });

    }
    /*@Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
    {
        if (firebaseAuth.getCurrentUser() == null) {
            Intent intent = new Intent(sign_up_details.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(this);
    }*/
}