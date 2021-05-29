package com.tptogiar.youchat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tptogiar.youchat.R;
import com.tptogiar.youchat.User;

import java.util.ArrayList;

public class ChattingListAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<User> users=new ArrayList();

    public ChattingListAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_chatting_cell, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,200));
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DataViewHolder dataViewHolder = (DataViewHolder) holder;
        User user = users.get(position);
        String userName = user.getUserName();
        int userAvatars = user.getUserAvatars();
        String message = user.getMessage();
        dataViewHolder.userName.setText(userName);
        dataViewHolder.userMessage.setText(message);
        dataViewHolder.userAvatars.setBackgroundResource(userAvatars);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class DataViewHolder extends RecyclerView.ViewHolder{
        ImageView userAvatars;
        TextView userName;
        TextView userMessage;
        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            userAvatars=(ImageView)itemView.findViewById(R.id.userAvatars);
            userName=(TextView)itemView.findViewById(R.id.userName);
            userMessage=(TextView)itemView.findViewById(R.id.userMessage);
        }
    }

}
