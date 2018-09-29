package com.example.mohammed.skyquestionbank.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mohammed.skyquestionbank.R;
import com.example.mohammed.skyquestionbank.models.OnlineUser;
import com.example.mohammed.skyquestionbank.oneSignal.OSNotificationSender;
import com.example.mohammed.skyquestionbank.ui.DuelChallengeActivity;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OnlineFriendsAdapter extends RecyclerView.Adapter<OnlineFriendsAdapter.OnlineFriendViewHolder> {

    private Context context;
    private List<OnlineUser> users;
//    private OnRecyclerItemClick listener;

    public OnlineFriendsAdapter(Context context, List<OnlineUser> users) {

        this.context = context;
        this.users = users;
    }

//    private OnlineFriendViewHolder lastHolder;


    @NonNull
    @Override
    public OnlineFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.user_online_item, parent, false);


        return new OnlineFriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnlineFriendViewHolder holder, int position) {

        if (users.get(holder.getAdapterPosition()).isPlaying())
            holder.userStatus.setBackgroundResource(R.drawable.busy_icon);
        else {
            holder.userStatus.setBackgroundResource(R.drawable.online_icon);

            holder.itemView.setOnClickListener(v -> {
                String playerId = users.get(holder.getAdapterPosition()).getPlayerId();
                String uid = users.get(holder.getAdapterPosition()).getUid();

                OSNotificationSender.sendNotification(playerId, uid);

                Intent intent = new Intent(context, DuelChallengeActivity.class);
                intent.putExtra(DuelChallengeActivity.OPPONENT_KEY,
                        users.get(holder.getAdapterPosition()));

                context.startActivity(intent);
            });
        }


        holder.userName.setText(users.get(holder.getAdapterPosition()).getUserName());

        Picasso.get().load(users.get(holder.getAdapterPosition()).getImgUrl()).
                resize(56, 56).centerCrop()
                .into(holder.userPhoto);


    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class OnlineFriendViewHolder extends RecyclerView.ViewHolder {


        private TextView userName;
        private CircularImageView userPhoto, userStatus;

        OnlineFriendViewHolder(View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.username);
            userStatus = itemView.findViewById(R.id.user_status);
            userPhoto = itemView.findViewById(R.id.user_photo);
        }
    }

}
