package com.example.mohammed.skyquestionbank.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mohammed.skyquestionbank.R;
import com.example.mohammed.skyquestionbank.interfaces.OnRecyclerItemClick;
import com.example.mohammed.skyquestionbank.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.LeaderBoardViewHolder> {

    private Context context;
    private List<User> users;
    private OnRecyclerItemClick listener;

    public LeaderBoardAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public LeaderBoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.leaderboard_user_item, parent, false);

        return new LeaderBoardViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull LeaderBoardViewHolder holder, int position) {

        User user = users.get(holder.getAdapterPosition());

        holder.userName.setText(user.getUserName());
        holder.userPoints.setText(String.valueOf(user.getPoints()));
        holder.position.setText(String.valueOf(holder.getAdapterPosition()));

        Picasso.get().load(Uri.parse(user.getUserPhoto())).placeholder(R.drawable.ic_launcher_background).into(holder.userPhoto);
    }


    @Override
    public int getItemCount() {
        return users.size();
    }

    class LeaderBoardViewHolder extends RecyclerView.ViewHolder {


        private TextView userName, userPoints, position;
        private ImageView userPhoto;

        LeaderBoardViewHolder(View itemView) {
            super(itemView);
            position = itemView.findViewById(R.id.user_position);
            userName = itemView.findViewById(R.id.username);
            userPoints = itemView.findViewById(R.id.user_points);
            userPhoto = itemView.findViewById(R.id.user_photo);
        }
    }

}
