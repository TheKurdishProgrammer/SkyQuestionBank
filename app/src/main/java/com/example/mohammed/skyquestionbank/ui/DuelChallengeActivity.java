package com.example.mohammed.skyquestionbank.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.mohammed.skyquestionbank.R;
import com.example.mohammed.skyquestionbank.databinding.ActivityDuelChellengeBinding;
import com.example.mohammed.skyquestionbank.firebase.FireBaseUtils;
import com.example.mohammed.skyquestionbank.interfaces.OnChallengeStatusChange;
import com.example.mohammed.skyquestionbank.models.OnlineUser;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import static com.example.mohammed.skyquestionbank.ui.QuestionActivity.PLAYER_TYPE;
import static com.example.mohammed.skyquestionbank.ui.QuestionActivity.TYPE_CHALLENGOR;

public class DuelChallengeActivity extends AppCompatActivity implements OnChallengeStatusChange {


    public static final String OPPONENT_KEY = "OPPONENT_KEY";
    public static final int OPPONENR_ON_CRITERIA = 1;
    public static final int DUEL_MATCH_READY = 2;
    public static final int START_DUEL_NOW = 3;
    private ActivityDuelChellengeBinding binding;
    private OnlineUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_duel_chellenge);

        user = getIntent().getParcelableExtra(OPPONENT_KEY);
        populateUI();


        setChallengeStatusChangeListener();
    }

    private void setChallengeStatusChangeListener() {

        FireBaseUtils.getChallengeStatusChanges(this, user.getUid());

    }

    private void populateUI() {

        Picasso.get().load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl())
                .resize(84, 84).into(binding.playerMePhoto);
        Picasso.get().load(user.getImgUrl()).resize(84, 84).into(binding.playerHeShePhoto);


        binding.playerMeName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        binding.playerMeName.setText(user.getUserName());

        binding.opponentStatus.setText(getString(R.string.waiting_for_opponent));


    }

    @Override
    public void onChange(int status) {


        switch (status) {
            case START_DUEL_NOW:
                Intent intent = new Intent(this, QuestionActivity.class);
                intent.putExtra("uid", user.getUid());
                intent.putExtra(QuestionActivity.GAME_STATUS_KEY, QuestionActivity.STATUS_MULTIPLE);
                intent.putExtra(PLAYER_TYPE, TYPE_CHALLENGOR);
                startActivity(intent);
                break;
            case DUEL_MATCH_READY:
                binding.opponentStatus.setText(getString(R.string.match_will_start_now));
                break;
            case OPPONENR_ON_CRITERIA:
                binding.opponentStatus.setText(getString(R.string.player_on_creteria));
                break;
        }
    }
}
