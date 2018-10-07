package com.example.mohammed.skyquestionbank.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mohammed.skyquestionbank.R;
import com.example.mohammed.skyquestionbank.adapters.QuestionAdapter;
import com.example.mohammed.skyquestionbank.databinding.ActivityQuestionBinding;
import com.example.mohammed.skyquestionbank.firebase.FireBaseUtils;
import com.example.mohammed.skyquestionbank.firebase.FirebaseQuestionReferences;
import com.example.mohammed.skyquestionbank.interfaces.OnChallengeDifficulityLoad;
import com.example.mohammed.skyquestionbank.interfaces.OnFirebaseValueSent;
import com.example.mohammed.skyquestionbank.interfaces.OnQuestionNumberListener;
import com.example.mohammed.skyquestionbank.interfaces.OnRecyclerItemClick;
import com.example.mohammed.skyquestionbank.interfaces.OnResponseCallback;
import com.example.mohammed.skyquestionbank.interfaces.OnTimeProgressUpdate;
import com.example.mohammed.skyquestionbank.models.QuestionResponse;
import com.example.mohammed.skyquestionbank.models.QuestionResults;
import com.example.mohammed.skyquestionbank.networking.QuestionDataDownloader;
import com.example.mohammed.skyquestionbank.utils.AnswerChecker;
import com.example.mohammed.skyquestionbank.utils.HTMLDecoder;
import com.example.mohammed.skyquestionbank.utils.TimerAsyncTask;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import static com.example.mohammed.skyquestionbank.firebase.FirebaseQuestionReferences.getChallengerOnCurrentQuestionRef;
import static com.example.mohammed.skyquestionbank.firebase.FirebaseQuestionReferences.getMeAsOpponentRef;
import static com.example.mohammed.skyquestionbank.firebase.FirebaseQuestionReferences.getOpponentOnCurrentQuestionRef;
import static com.example.mohammed.skyquestionbank.interfaces.FirebaseRefLinks.CHALLENGER_ON_QUESTION;
import static com.example.mohammed.skyquestionbank.interfaces.FirebaseRefLinks.DUEL_CHALLENGE_QUESTIONS;
import static com.example.mohammed.skyquestionbank.interfaces.FirebaseRefLinks.DUEL_CHALLENGE_STATUS;
import static com.example.mohammed.skyquestionbank.interfaces.FirebaseRefLinks.OPPONENT_ON_QUESTION;
import static com.example.mohammed.skyquestionbank.interfaces.FirebaseRefLinks.QUESTION_DIFFICULITY;
import static com.example.mohammed.skyquestionbank.ui.DuelChallengeActivity.START_DUEL_NOW;
import static com.example.mohammed.skyquestionbank.ui.MainActivity.UID;
import static com.example.mohammed.skyquestionbank.ui.WinActivity.GAME_RESULT;
import static com.example.mohammed.skyquestionbank.ui.WinActivity.RESULT_LOST;
import static com.example.mohammed.skyquestionbank.ui.WinActivity.RESULT_WIN;

public class QuestionActivity extends AppCompatActivity implements
        OnTimeProgressUpdate, OnQuestionNumberListener, OnChallengeDifficulityLoad, OnResponseCallback<QuestionResponse>, OnRecyclerItemClick, OnFirebaseValueSent {

    public static final String AMOUNT = "amount";
    public static final String TYPE = "type";
    public static final String DIFFICULTY = "difficulty";
    public static final String CAT_ID = "CAT_ID";
    private static final int NO_QUESTION_RESULT = 1;
    private static final String CURRENT_TIME = "CURRENT_TIME";
    private static final String CURRENT_QUESTION = "CURRENT_QUESTION";
    private static final String USER_ANSWERS = "USER_ANSWERS";
    private static final String CURRENT_CHOSEN_ASNWER = "CURRENT_CHOSEN_ASNWER";
    private static final String QUESTION_RESULT = "QUESTION_RESULT";
    private static final String CURRENT_CHOSEN_ASNWER_POSITION = "CURRENT_CHOSEN_ASNWER_POSITION";
    private ArrayList<QuestionResults> results;
    private ActivityQuestionBinding binding;
    private int currentQuestion;
    private TimerAsyncTask asyncTask;
    private ArrayList<String> userChosenAnswers;
    private String currentChosenAnswer;
    private String uid;

    public static final int TYPE_CHALLENGOR = 3;
    public static final int TYPE_OPPONENT = 4;
    public static final String PLAYER_TYPE = "PLAYER_TYPE";
    public static final String GAME_STATUS_KEY = "gameStatus";
    public static final int STATUS_SINGLE = -1;
    public static final int STATUS_MULTIPLE = 1;
    private int playerType;
    private DatabaseReference oppRef;
    private int gamesStatus;
    private DatabaseReference userOnQuestion;
    private ProgressDialog progressDialog;
    private String difficulty;
    private int startTime;
    private int currentChosenAnswerPosition;


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        int time = asyncTask.getCurrentTime();
        outState.putInt(CURRENT_TIME, time);


        if (currentChosenAnswerPosition != -1)
            outState.putInt(CURRENT_CHOSEN_ASNWER_POSITION, currentChosenAnswerPosition);

        if (currentQuestion > 0)
            outState.putInt(CURRENT_QUESTION, currentQuestion - 1);

        if (userChosenAnswers != null)
            outState.putStringArrayList(USER_ANSWERS, userChosenAnswers);

        if (currentChosenAnswer != null)
            outState.putString(CURRENT_CHOSEN_ASNWER, currentChosenAnswer);

        if (results != null)
            outState.putParcelableArrayList(QUESTION_RESULT, results);

        if (!getIntent().hasExtra(DIFFICULTY))
            outState.putString(DIFFICULTY, difficulty);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_question);


        gamesStatus = getIntent().getIntExtra(GAME_STATUS_KEY, STATUS_SINGLE);


        if (gamesStatus == STATUS_MULTIPLE) {
            uid = getIntent().getStringExtra(UID);

            playerType = getIntent().getIntExtra(PLAYER_TYPE, TYPE_CHALLENGOR);
            setUserOnQuestionRef();
        }

        if (savedInstanceState != null) {

            restoreStates(savedInstanceState);
            populateQuestion();


        } else {
            startTime = 60;
            currentChosenAnswerPosition = -1;
            getQuestions();

        }


        setupRecyclerView();
        setListeners();


    }

    private void restoreStates(Bundle savedInstanceState) {

        startTime = savedInstanceState.getInt(CURRENT_TIME, 60);

        binding.timeProgress.setPercentage(startTime * 6);
        binding.timeProgress.setStepCountText(String.valueOf(startTime));
        currentQuestion = savedInstanceState.getInt(CURRENT_QUESTION, 0);

        Log.e("TIME", String.valueOf(startTime));

        if (savedInstanceState.containsKey(USER_ANSWERS))
            userChosenAnswers = savedInstanceState.getStringArrayList(USER_ANSWERS);

        if (savedInstanceState.containsKey(CURRENT_CHOSEN_ASNWER))
            currentChosenAnswer = savedInstanceState.getString(CURRENT_CHOSEN_ASNWER);


        if (savedInstanceState.containsKey(QUESTION_RESULT))
            results = savedInstanceState.getParcelableArrayList(QUESTION_RESULT);

        currentChosenAnswerPosition = savedInstanceState.getInt(CURRENT_CHOSEN_ASNWER_POSITION, -1);


        if (savedInstanceState.containsKey(DIFFICULTY))
            difficulty = savedInstanceState.getString(DIFFICULTY);

    }

    private void setListeners() {
        binding.nextQuestion.setOnClickListener(v -> {

            userChosenAnswers.add(currentChosenAnswer);

            if (checkIfGameFinished())
                return;

            // before going to the next question ,storing the current chosenAnswers
            currentChosenAnswer = null;
            currentChosenAnswerPosition = -1;

            populateQuestion();
        });

    }

    private void setUserOnQuestionRef() {

        //set a reference to the current player to update his/her current question
        // and set a reference listener to listen to the opponents question chaning
        if (playerType == TYPE_OPPONENT) {

            userOnQuestion = getOpponentOnCurrentQuestionRef(uid, OPPONENT_ON_QUESTION);
            FireBaseUtils.setOnCurrentQuestionNumberListener(
                    this, getOpponentOnCurrentQuestionRef(uid, CHALLENGER_ON_QUESTION));

        } else {

            userOnQuestion = getChallengerOnCurrentQuestionRef(uid, CHALLENGER_ON_QUESTION);
            FireBaseUtils.setOnCurrentQuestionNumberListener(this
                    , getChallengerOnCurrentQuestionRef(uid, OPPONENT_ON_QUESTION));
        }
    }

    private void setupRecyclerView() {
        binding.answers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.answers.setHasFixedSize(true);

    }

    private void populateQuestion() {

        //updating the on current question to let the user on which question im on now!

        QuestionResults result = results.get(currentQuestion++);

        if (gamesStatus == STATUS_MULTIPLE)
            userOnQuestion.setValue(currentQuestion);


        //setting the question


        binding.question.setText(HTMLDecoder.decodeHtml(result.getQuestion()));

        //setting the answers
        binding.answers.setAdapter(new QuestionAdapter(currentChosenAnswerPosition, this, result.getIncorrectAnswers()
                , result.getCorrectAnswer(), this));


        Log.e("CORRECT", result.getCorrectAnswer());


        shouldMakeNextQuestionButtonUnClickable(currentChosenAnswer != null);

        hideProgressAndStartTiming();

    }

    void shouldMakeNextQuestionButtonUnClickable(boolean should) {
        //making the next question button greyed out and un-clickable

        if (should)
            binding.nextQuestion.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
        else
            binding.nextQuestion.setCardBackgroundColor(ContextCompat.getColor(this, R.color.next_question_disabled));


        binding.nextQuestion.setClickable(should);
    }

    private boolean checkIfGameFinished() {

        if (currentQuestion == results.size()) {

            if (gamesStatus == STATUS_MULTIPLE)
                userOnQuestion.setValue(currentQuestion + 1);


            Toast.makeText(this, "We are out of question", Toast.LENGTH_LONG).show();
            int wonPoints = AnswerChecker.getCorrectAnswerPoints(difficulty, results, userChosenAnswers);
            FireBaseUtils.sendUserWonPoints(difficulty, wonPoints, this);
            AnswerChecker.storeUserPoints(this, wonPoints);
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Calculating Points");
            progressDialog.show();
            return true;

        }


        return false;
    }

    private void getQuestions() {

        if (gamesStatus == STATUS_MULTIPLE) {
            if (playerType == TYPE_CHALLENGOR) {

                QuestionDataDownloader downloader = new QuestionDataDownloader();
                downloader.getChallengeQuestions(this, uid);
                downloader.getChallengeQuestionDifficulity(this, uid);
            }

            difficulty = getIntent().getStringExtra(DIFFICULTY);
            DatabaseReference questionDiff = FirebaseQuestionReferences.getMeAsOpponentRef(uid);
            questionDiff.child(QUESTION_DIFFICULITY).setValue(difficulty);

        } else {

            int amount = getIntent().getIntExtra(AMOUNT, 10);
            String type = getIntent().getStringExtra(TYPE);
            int catId = getIntent().getIntExtra(CAT_ID, 9);

            difficulty = getIntent().getStringExtra(DIFFICULTY);

            QuestionDataDownloader dataDownloader = new QuestionDataDownloader();
            dataDownloader.getQuestions(amount, catId, type, difficulty, this);

        }

    }

    @Override
    public void onResponse(QuestionResponse response) {

        if (response.getResponseCode() == NO_QUESTION_RESULT)
            binding.noResult.setVisibility(View.VISIBLE);
        else {

            results = response.getResults();

            if (playerType == TYPE_OPPONENT) {

                oppRef = getMeAsOpponentRef(uid);
                oppRef.child(DUEL_CHALLENGE_QUESTIONS).setValue(response)
                        .addOnCompleteListener(task -> oppRef.child(DUEL_CHALLENGE_STATUS).setValue(START_DUEL_NOW).
                                addOnCompleteListener(task1 -> populateQuestion()));
            } else
                populateQuestion();

            userChosenAnswers = new ArrayList<>();
        }


    }

    private void hideProgressAndStartTiming() {
        binding.questionLayout.setVisibility(View.VISIBLE);
        binding.waitDialogLayout.waitDialog.setVisibility(View.GONE);

        asyncTask = new TimerAsyncTask(startTime, this);
        asyncTask.execute();

    }


    @Override
    public void onError(Throwable throwable) {
        Log.e(getLocalClassName(), throwable.getMessage());

    }


    @Override
    protected void onPause() {
        super.onPause();
        if (asyncTask != null) {
            asyncTask.cancel(true);
        }
    }

    @Override
    public void onItemClicked(int position, String chosenAnswer) {
        currentChosenAnswer = chosenAnswer;
        currentChosenAnswerPosition = position;
        shouldMakeNextQuestionButtonUnClickable(true);
    }


    @Override
    public void onItemClicked(int position) {

    }

    @Override
    public void onSent(int wonPoints) {

        progressDialog.cancel();
        Intent intent = new Intent(this, WinActivity.class);
        intent.putExtra(WinActivity.USER_POINTS, wonPoints);
        intent.putExtra(GAME_RESULT, RESULT_WIN);
        startActivity(intent);
        finish();
    }

    @Override
    public void onChange(int questionNumber) {

        if (results != null)
            if (questionNumber == -1 || questionNumber == results.size() + 1) {
                Intent intent = new Intent(this, WinActivity.class);
                intent.putExtra(GAME_RESULT, RESULT_LOST);
                startActivity(intent);
                userOnQuestion.getParent().removeValue();
                finish();
                return;
            }

        binding.userOnQuestion.setText(getString(R.string.user_on_question, questionNumber));

    }


    @Override
    public void onUpdate(int value) {

        runOnUiThread(() -> {

            binding.timeProgress.setStepCountText(String.valueOf(value));
            binding.timeProgress.setPercentage(6 * value);

            if (value == 0) {
                Intent intent = new Intent(this, WinActivity.class);
                intent.putExtra(WinActivity.GAME_RESULT, WinActivity.RESULT_LOST);
                startActivity(intent);
                if (gamesStatus == STATUS_MULTIPLE)
                    userOnQuestion.setValue(-1);

                finish();
            }
        });


    }

    @Override
    public void onLoad(String difficulty) {
        this.difficulty = difficulty;
    }
}
