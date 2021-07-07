package com.suryakiran.myapplication;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class VerifyPhone extends AppCompatActivity implements View.OnClickListener {

    Button verifyBtn;
    EditText otp;
    ProgressBar otpProgress;
    String phone;
    int otp1;
    private int CODE= 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_verify_phone);

        phone= getIntent().getStringExtra("phone");

        verifyBtn= findViewById(R.id.verifyBtn);
        otp= findViewById(R.id.otp);
        otpProgress= findViewById(R.id.otpProgress);

        verifyBtn.setOnClickListener(this);
        ActivityCompat.requestPermissions(VerifyPhone.this,new String[]{Manifest.permission.SEND_SMS,Manifest.permission.RECEIVE_SMS },CODE);


        StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Random rnd = new Random();
        otp1 = 100000 + rnd.nextInt(900000);
    }

    public void sendSms(){
        SmsManager smsManager=SmsManager.getDefault();

        smsManager.sendTextMessage("91"+phone,null,"your one time verification password for home manager is "+String.valueOf(otp1),null,null);
        Toast.makeText(VerifyPhone.this, "sms sent successfully", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClick(View view) {
        if(otp.getText().toString().equals(String.valueOf(otp1))){
            Toast.makeText(VerifyPhone.this,"otp verified",Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        }
        else{
            Toast.makeText(VerifyPhone.this,"invalid otp",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                sendSms();
                sendMail();
            }else{
                Toast.makeText(VerifyPhone.this,"it is required",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendMail() {
        final String username="suryakiran56191@gmail.com";
        final String password="krspkrspkrspkrsp";
        String messageToSend="your one time verification password for home manager is "+String.valueOf(otp1);

        Properties props=new Properties();
        props.put("mail.smtp.auth",true);
        props.put("mail.smtp.starttls.enable",true);
        props.put("mail.smtp.host","smtp.gmail.com");
        props.put("mail.smtp.port","587");

        Session session=Session.getInstance(props,
                new javax.mail.Authenticator(){
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username,password);
                    }
                });
        try{
            Message message=new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(javax.mail.Message.RecipientType.TO,InternetAddress.parse(getIntent().getStringExtra("mail")));
            message.setSubject("sending mail from trigger app");
            message.setText(messageToSend);
            Transport.send(message);
            Toast.makeText(getApplicationContext(),"email sent successfully",Toast.LENGTH_SHORT).show();
        }catch (MessagingException e){
            e.printStackTrace();
        }

    }
}