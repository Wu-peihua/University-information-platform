package com.example.uipfrontend.Student.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uipfrontend.R;

import java.util.List;

public class StudentGroupRecyclerViewAdapter extends RecyclerView.Adapter {

    private Context context;
    private List list;

    public StudentGroupRecyclerViewAdapter(Context context,List list) {
        this.context = context;
        this.list = list;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.tv_student_group);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_student_group_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {

        ViewHolder viewHolder = new ViewHolder(holder.itemView);
//        viewHolder.textView.setText(list.get(pos).toString());

   }

    @Override
    public int getItemCount() {

//        return list.size();

        return 30;
    }


}