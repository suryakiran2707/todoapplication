package com.suryakiran.myapplication;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;



public class todo extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    EditText name,info,documents,datePicker,bigPick;
    Button button,showButton;
    RecyclerView recView;
    FirstAdapter adapter;
    private int count ;
    final Calendar myCalendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);




        name=(EditText)findViewById(R.id.nameText);
        info=(EditText)findViewById(R.id.infoText);
        documents=(EditText)findViewById(R.id.doumentsText);
        datePicker=(EditText)findViewById(R.id.datePick);
        bigPick=(EditText)findViewById(R.id.bigPick);
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timepicker=new TimePickerFragment();
                timepicker.show(getSupportFragmentManager(),"time picker");
            }
        });




        button=(Button)findViewById(R.id.addButton);
        showButton=(Button)findViewById(R.id.showButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirstTime fsob=new FirstTime();
                fsob.setName(name.getText().toString());
                fsob.setInfo(info.getText().toString());
                fsob.setTime(datePicker.getText().toString());
                fsob.setDate(bigPick.getText().toString());
                List<String> l=new ArrayList<String>();
                String[] arr=documents.getText().toString().split("\\s");
                for(int i=0;i<arr.length;i++) {
                    l.add(arr[i]);
                }
                fsob.setDocuments(l);
                HashMap<String ,Object> mp=new HashMap<>();
                String sp=name.getText().toString();
                mp.put(sp,fsob);




//                FirebaseDatabase.getInstance().getReference().child("trigger").push().updateChildren(mp);
                FirebaseDatabase.getInstance().getReference("users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/trigger").child("to do").updateChildren(mp);
                name.setText("");
                info.setText("");
                documents.setText("");
                datePicker.setText("");
                bigPick.setText("");

            }
        });


        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setContentView(R.layout.imp_does);
                recView=findViewById(R.id.recView);
                recView.setLayoutManager(new LinearLayoutManager(todo.this,LinearLayoutManager.VERTICAL,false));
                FirebaseRecyclerOptions<model> options=new FirebaseRecyclerOptions.Builder<model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/trigger").child("to do"),model.class)
                        .build();
                adapter=new FirstAdapter(options);
                adapter.startListening();

                recView.setAdapter(adapter);


            }





        });



    }



    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        TextView textView=(TextView)findViewById(R.id.datePick);
        textView.setText("Hour:"+i+"minute:"+i1);
    }
}