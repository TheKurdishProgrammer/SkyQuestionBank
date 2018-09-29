package com.example.mohammed.skyquestionbank.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mohammed.skyquestionbank.R;
import com.example.mohammed.skyquestionbank.adapters.OnlineFriendsAdapter;
import com.example.mohammed.skyquestionbank.databinding.FragmentChallengeFriendBinding;
import com.example.mohammed.skyquestionbank.firebase.FireBaseUtils;
import com.example.mohammed.skyquestionbank.interfaces.OnOnlineFriendListLoad;
import com.example.mohammed.skyquestionbank.models.OnlineUser;

import java.util.List;


public class ChallengeFriendFragment extends Fragment implements OnOnlineFriendListLoad {


    private FragmentChallengeFriendBinding binding;

    public ChallengeFriendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_challenge_friend, container, false);

        FireBaseUtils.getOnlineFriendList(this);

        return binding.getRoot();

    }

    @Override
    public void onLoadList(List<OnlineUser> users) {

        if (users.isEmpty()) {
            binding.noFriend.setVisibility(View.VISIBLE);
            binding.onlineUsers.setVisibility(View.GONE);

        } else {

            OnlineFriendsAdapter adapter = new OnlineFriendsAdapter(getContext(), users);
            binding.onlineUsers.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.onlineUsers.setHasFixedSize(true);
            binding.onlineUsers.setAdapter(adapter);

        }

        binding.waitDialog.waitDialog.setVisibility(View.GONE);
    }
}

