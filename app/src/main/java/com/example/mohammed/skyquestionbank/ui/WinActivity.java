package com.example.mohammed.skyquestionbank.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.mohammed.skyquestionbank.R;
import com.example.mohammed.skyquestionbank.databinding.ActivityWinBinding;

public class WinActivity extends AppCompatActivity {


    public static final String USER_POINTS = "USER_POINTS";
    public static final String GAME_RESULT = "GAME_RESULT";
    public static final int RESULT_WIN = 1;
    public static final int RESULT_LOST = 2;
    private int userWonPoints;
    private int gameResult;
    private ActivityWinBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_win);

        userWonPoints = getIntent().getIntExtra(USER_POINTS, 0);
        gameResult = getIntent().getIntExtra(GAME_RESULT, RESULT_WIN);
        populateUI();
    }

    private void populateUI() {
        if (gameResult == RESULT_WIN)
            binding.gameResult.setImageResource(R.drawable.win);

        else {
            binding.gameResult.setImageResource(R.drawable.time);
            binding.timeUp.setVisibility(View.VISIBLE);
        }
        binding.pointsEarned.setText(getString(R.string.points_earned, userWonPoints));
    }

    public void goMain(View view) {
        finish();
    }
}
