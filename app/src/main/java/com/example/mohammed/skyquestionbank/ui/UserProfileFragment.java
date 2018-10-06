package com.example.mohammed.skyquestionbank.ui;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mohammed.skyquestionbank.R;
import com.example.mohammed.skyquestionbank.databinding.FragmentUserProfileBinding;
import com.example.mohammed.skyquestionbank.firebase.FireBaseUtils;
import com.example.mohammed.skyquestionbank.interfaces.OnUserLoad;
import com.example.mohammed.skyquestionbank.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserProfileFragment extends Fragment implements OnUserLoad {

    public UserProfileFragment() {
        // Required empty public constructor
    }


    FragmentUserProfileBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_profile, container, false);


        FireBaseUtils.getUser(this);

        return binding.getRoot();
    }

    @Override
    public void onLoad(List<User> users) {

    }

    @Override
    public void onLoad(User user) {

        int easyPoints = user.getEasyQuestions();
        int mediumPoints = user.getMediumQuestions();
        int hardPoints = user.getHardQuestions();

        binding.easy.setText(String.valueOf(easyPoints));
        binding.medium.setText(String.valueOf(mediumPoints));
        binding.hard.setText(String.valueOf(hardPoints));

        binding.total.setText(String.valueOf(easyPoints + mediumPoints + hardPoints));

        binding.username.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        Uri userPhoto = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();

        Picasso.get().load(userPhoto).into(binding.userPhoto);
    }
}
