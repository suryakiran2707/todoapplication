package com.suryakiran.myapplication;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class meeting extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    EditText meetingText,meetingTime,meetingDate;
    Button meetingButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);

        meetingText=(EditText)findViewById(R.id.meetingText);
        meetingTime=(EditText)findViewById(R.id.meetingTime);
        meetingDate=(EditText)findViewById(R.id.meetingDate);
        meetingButton=(Button) findViewById(R.id.meetingButton);


        meetingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timepicker=new TimePickerFragment();
                timepicker.show(getSupportFragmentManager(),"time picker");
            }
        });

        meetingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String st=meetingText.getText().toString();
                String time=meetingTime.getText().toString();
                HashMap<String,Object> hm=new HashMap<>();
                hm.put(time,st);

                FirebaseDatabase.getInstance().getReference("users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/trigger").child("meeting").updateChildren(hm);

                meetingText.setText("");
                meetingTime.setText("");
                meetingDate.setText("");

            }
        });



    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        TextView textView=(TextView)findViewById(R.id.meetingTime);
        textView.setText(""+i+":"+i1);
    }
}