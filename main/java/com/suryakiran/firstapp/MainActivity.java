package com.suryakiran.firstapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn=findViewById(R.id.button);
        Button loginbtn=findViewById(R.id.btnlogin);
        EditText username,password;
        username=findViewById(R.id.editname);
        DatabaseReference databaseReference;
        databaseReference= FirebaseDatabase.getInstance().getReference();
        password=findViewById(R.id.editpassword);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,sign_up_details.class));
            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().length()==0)
                {
                    Toast.makeText(MainActivity.this, "Please enter Name", Toast.LENGTH_SHORT).show();
                    username.requestFocus();
                }
                else if (password.getText().toString().length()==0)
                {
                    Toast.makeText(MainActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                    password.requestFocus();
                }
                else if(username.getText().toString().toLowerCase().matches("^.+@gmail.com$") || username.getText().toString().toLowerCase().matches("^.+.ac.in$")) {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(username.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (FirebaseAuth.getInstance().getCurrentUser() != null && FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {

                                    FirebaseUser fbu = FirebaseAuth.getInstance().getCurrentUser();
                                    startActivity(new Intent(MainActivity.this, DashBoard.class));
                                }
                             else if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                                    Toast.makeText(MainActivity.this, "Please Check Details Once Again", Toast.LENGTH_SHORT).show();
                                } else if (!FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                                    FirebaseAuth.getInstance().signOut();
                                    Toast.makeText(MainActivity.this, "Email not verified Plesae check your email", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(MainActivity.this, "please check once again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else if(username.getText().toString().matches("^[0-9]{10}$")) {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(username.getText().toString()+"addbyus@gmail.com", password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (FirebaseAuth.getInstance().getCurrentUser() != null) {

                                    FirebaseUser fbu = FirebaseAuth.getInstance().getCurrentUser();
                                    startActivity(new Intent(MainActivity.this, DashBoard.class));
                                } else if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                                    Toast.makeText(MainActivity.this, "Please Check Details Once Again", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if(firebaseAuth.getCurrentUser()==null)
        {

        }
        else
        {
            if(firebaseAuth.getCurrentUser().isEmailVerified()) {
                Intent intent = new Intent(MainActivity.this, DashBoard.class);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}