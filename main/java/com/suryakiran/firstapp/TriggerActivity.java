package com.suryakiran.myapplication;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class TriggerActivity extends AppCompatActivity implements View.OnClickListener {
    CardView card1,card2,card3,card4,card5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trigger);
        card1=(CardView)findViewById(R.id.card_view1);
        card2=(CardView)findViewById(R.id.card_view2);
        card3=(CardView)findViewById(R.id.card_view3);
        card4=(CardView)findViewById(R.id.card_view4);
        card5=(CardView)findViewById(R.id.card_view5);
        card1.setOnClickListener(this);
        card2.setOnClickListener(this);
        card3.setOnClickListener(this);
        card4.setOnClickListener(this);
        card5.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()){
            case R.id.card_view1:
                i=new Intent(this,todo.class);
                startActivity(i);
                break;
            case R.id.card_view2:
                i=new Intent(this,meeting.class);
                startActivity(i);
                break;
            case R.id.card_view3:
                i=new Intent(this,schedule.class);
                startActivity(i);
                break;
            case R.id.card_view4:
                i=new Intent(this,diary.class);
                startActivity(i);
                break;
            case R.id.card_view5:
                i=new Intent(this,addnotes.class);
                startActivity(i);
                break;
            default:break;
        }
//        FirebaseDatabase.getInstance().getReference().child("trigger").child("android").setValue("abcd");
//        HashMap<String,Object> map=new HashMap<>();
//        map.put("name","rohan");
//        map.put("email","rohan@gmail.com");
//        FirebaseDatabase.getInstance().getReference().child("trigger").child("apple").updateChildren(map);

    }
}