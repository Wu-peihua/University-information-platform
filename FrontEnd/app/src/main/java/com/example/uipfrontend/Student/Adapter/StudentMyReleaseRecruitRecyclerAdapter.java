package com.example.uipfrontend.Student.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uipfrontend.R;

import java.util.ArrayList;
import java.util.HashMap;

public class StudentMyReleaseRecruitRecyclerAdapter extends RecyclerView.Adapter {

    private Context context;

    private LayoutInflater inflater;
    private ArrayList<HashMap<String, Object>> listItem;

    //构造函数，传入数据
    public StudentMyReleaseRecruitRecyclerAdapter(Context context, ArrayList<HashMap<String, Object>> listItem) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.listItem = listItem;
    }


    //定义Viewholder
    class ViewHolder extends RecyclerView.ViewHolder  {
        private TextView Title, Text;

        public ViewHolder(View root) {
            super(root);
            Title = (TextView) root.findViewById(R.id.tv_studentMyReleaseNew);
            Text = (TextView) root.findViewById(R.id.tv_studentMyReleaseItem);

        }

        public TextView getTitle() {
            return Title;
        }

        public TextView getText() {
            return Text;
        }


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_release_recruit, null);
        return new StudentMyReleaseRecruitRecyclerAdapter.ViewHolder(view);
//        return new ViewHolder(inflater.inflate(R.layout.item_my_release_recruit, null));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;
        // 绑定数据到ViewHolder里面
        vh.Title.setText("title");
        vh.Text.setText("content");
    }

    @Override
    public int getItemCount() {
//        return listItem.size();
        return 10;
    }
}
