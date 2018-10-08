package com.example.mohammed.skyquestionbank.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.mohammed.skyquestionbank.R;
import com.example.mohammed.skyquestionbank.adapters.LeaderBoardAdapter;
import com.example.mohammed.skyquestionbank.firebase.FireBaseUtils;
import com.example.mohammed.skyquestionbank.interfaces.OnUserLoad;
import com.example.mohammed.skyquestionbank.models.User;
import com.example.mohammed.skyquestionbank.utils.InternetConnectivityChecker;

import java.util.List;


public class LeaderBoardsFragment extends Fragment implements OnUserLoad {


    public LeaderBoardsFragment() {
        // Required empty public constructor
    }

    private RecyclerView leaderBoardRecyler;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leader_boards, container, false);

        leaderBoardRecyler = view.findViewById(R.id.leader_board);
        progressBar = view.findViewById(R.id.progressBar);

        setupRecycler();
        if (InternetConnectivityChecker.isConnected(getContext()))
            Snackbar.make(view.findViewById(R.id.refresh_list), R.string.no_connection, Snackbar.LENGTH_LONG).show();
        else

            FireBaseUtils.getUsers(this);


        return view;
    }

    private void setupRecycler() {
        leaderBoardRecyler.setHasFixedSize(true);
        leaderBoardRecyler.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onLoad(User user) {

    }

    @Override
    public void onLoad(List<User> users) {
        LeaderBoardAdapter adapter = new LeaderBoardAdapter(getContext(), users);
        leaderBoardRecyler.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);

    }
}
