package com.suryakiran.myapplication;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static com.facebook.internal.CallbackManagerImpl.RequestCodeOffset.Message;

public class diary extends AppCompatActivity {
    EditText phoneNumber,messageToSend,emailSend;
    EditText emailContent;
    Button sendMessageButton,sendEmailButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);


        phoneNumber=(EditText) findViewById(R.id.phoneNumber);
        messageToSend=(EditText) findViewById(R.id.messageToSend);
        emailSend=(EditText) findViewById(R.id.emailSend);
        emailContent=(EditText) findViewById(R.id.emailContent);

        sendMessageButton=(Button)findViewById(R.id.sendMessageButton);
        sendEmailButton=(Button)findViewById(R.id.sendEmailButton);

        ActivityCompat.requestPermissions(diary.this,new String[]{Manifest.permission.SEND_SMS,Manifest.permission.RECEIVE_SMS}, PackageManager.PERMISSION_GRANTED);



        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message=messageToSend.getText().toString();
                String phone_num=phoneNumber.getText().toString();
                try {
                    SmsManager sms = SmsManager.getDefault();
                    Toast.makeText(getApplicationContext(),"sms sending", Toast.LENGTH_SHORT).show();
                    sms.sendTextMessage(phone_num, null, message, null, null);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"sms not sent", Toast.LENGTH_SHORT).show();
                }
            }
        });


        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username="suryakiran56191@gmail.com";
                final String password="krspkrspkrspkrsp";
                String messageToSend=emailContent.getText().toString();
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
                    message.setRecipients(javax.mail.Message.RecipientType.TO,InternetAddress.parse(emailSend.getText().toString()));
                    message.setSubject("sending mail from trigger app");
                    message.setText(messageToSend);
                    Transport.send(message);
                    Toast.makeText(getApplicationContext(),"email sent successfully",Toast.LENGTH_SHORT).show();
                }catch (MessagingException e){
                    throw new RuntimeException(e);
                }

            }
        });

        StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);



    }
}