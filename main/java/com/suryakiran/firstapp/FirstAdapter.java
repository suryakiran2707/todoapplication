package com.suryakiran.myapplication;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import static androidx.recyclerview.widget.RecyclerView.*;

public class FirstAdapter extends FirebaseRecyclerAdapter<model,FirstAdapter.myViewHolder> {

    public FirstAdapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }



    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_does,parent,false);
        return new myViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull model model) {
        holder.title.setText(model.getName());
        holder.desc.setText(model.getInfo());
        String str="";
        for(String s:model.getDocuments()){
            str+=" "+s;
        }
        holder.documents.setText(str);
        holder.date.setText(model.getDate());
        holder.time.setText(model.getTime());
    }



    class myViewHolder extends ViewHolder{
        TextView title,desc,documents,date,time;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.title);
            desc=(TextView)itemView.findViewById(R.id.description);
            documents=(TextView)itemView.findViewById(R.id.docs);
            date=(TextView)itemView.findViewById(R.id.date);
            time=(TextView)itemView.findViewById(R.id.time);

        }
    }
}
