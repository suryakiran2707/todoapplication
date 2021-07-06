package com.suryakiran.firstapp;

import androidx.annotation.NonNull;
import java.util.*;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class otp_verification extends AppCompatActivity {
    Button otpbtn;
    EditText otpedit;
    FirebaseAuth auth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        otpbtn=findViewById(R.id.otpbtn);
        otpedit=findViewById(R.id.otpedit);
        String vercode=getIntent().getStringExtra("OTP");
        String email=getIntent().getStringExtra("email");
        String password=getIntent().getStringExtra("password");
        String profession=getIntent().getStringExtra("profession");
        String name=getIntent().getStringExtra("name");
        String phonenumber=getIntent().getStringExtra("phonenumber");
        progressBar=findViewById(R.id.bar);
        auth=FirebaseAuth.getInstance();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        otpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(otpedit.getText().toString().length()!=6)
                {
                    Toast.makeText(otp_verification.this, "Please enter valid OTP", Toast.LENGTH_SHORT).show();
                }
                else
                {
                        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(vercode,otpedit.getText().toString());
                        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    auth.createUserWithEmailAndPassword(phonenumber+"addbyus"+"@gmail.com",password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful())
                                            {
                                                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                                                credentials cre=new credentials(name,phonenumber,email,profession,FirebaseAuth.getInstance().getUid());
                                                databaseReference.child(FirebaseAuth.getInstance().getUid()).child("credential").setValue(cre);
                                                Intent intent = new Intent(otp_verification.this, DashBoard.class);
                                                startActivity(intent);
                                            }
                                            else
                                            {
                                                Toast.makeText(otp_verification.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(otp_verification.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            }
                        );
                }
            }
        });
    }
}