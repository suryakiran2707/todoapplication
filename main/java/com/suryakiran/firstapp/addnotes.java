package com.suryakiran.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class addnotes extends AppCompatActivity {
    EditText et;
    Button btn;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnotes);
        et=(EditText)findViewById(R.id.addNotesText);
        btn=(Button)findViewById(R.id.addNoteButton);
        final ArrayList<ExampleItem> exampleList=new ArrayList<>();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String st=et.getText().toString();
                exampleList.add(new ExampleItem(st));

                et.setText("");
                mRecyclerView=findViewById(R.id.recyclerView);
                mRecyclerView.setHasFixedSize(true);
                mLayoutManager=new LinearLayoutManager(addnotes.this);
                mAdapter =new ExampleAdapter(exampleList);

                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);

            }
        });





    }
}