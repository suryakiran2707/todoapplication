package com.suryakiran.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExampleAdapter extends RecyclerView.Adapter< ExampleAdapter.ExampleViewHolder> {
    private ArrayList<ExampleItem> mExampleList;
    public static class ExampleViewHolder extends RecyclerView.ViewHolder{
        public TextView tv1;
        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            tv1=itemView.findViewById(R.id.note1);
        }
    }
    public ExampleAdapter(ArrayList<ExampleItem> exampleList){
        mExampleList=exampleList;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.example_note,parent,false);
        ExampleViewHolder evh=new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        ExampleItem currentItem=mExampleList.get(position);
        holder.tv1.setText(currentItem.gettext1());
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}
