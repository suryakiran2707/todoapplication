package com.suryakiran.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

public class schedule extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        Button button=(Button)findViewById(R.id.scheduleButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timepicker=new TimePickerFragment();
                timepicker.show(getSupportFragmentManager(),"time picker");
            }
        });
    }
    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        TextView textView=(TextView)findViewById(R.id.textClock);
        textView.setText("Hour:"+i+"minute:"+i1);
    }
}