package com.example.mohammed.skyquestionbank.ui;

import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
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
import com.example.mohammed.skyquestionbank.interfaces.OnFirebaseValueSent;
import com.example.mohammed.skyquestionbank.interfaces.OnRecyclerItemClick;
import com.example.mohammed.skyquestionbank.interfaces.OnResponseCallback;
import com.example.mohammed.skyquestionbank.models.QuestionResponse;
import com.example.mohammed.skyquestionbank.models.QuestionResults;
import com.example.mohammed.skyquestionbank.networking.QuestionDataDownloader;
import com.example.mohammed.skyquestionbank.utils.AnswerChecker;
import com.example.mohammed.skyquestionbank.utils.HTMLDecoder;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.services.common.SafeToast;

import static com.example.mohammed.skyquestionbank.ui.MainActivity.CHALLENGE_STATUS;
import static com.example.mohammed.skyquestionbank.ui.MainActivity.PLAYER_SINGLE;

public class QuestionActivity extends AppCompatActivity implements OnResponseCallback<QuestionResponse>, OnRecyclerItemClick, OnFirebaseValueSent {

    public static final String AMOUNT = "amount";
    public static final String TYPE = "type";
    public static final String DIFFICULTY = "difficulty";
    public static final String CAT_ID = "CAT_ID";
    private static final int NO_QUESTION_RESULT = 1;
    private List<QuestionResults> results;
    private ActivityQuestionBinding binding;
    private int currentQuestion;
    private TimerAsyncTask asyncTask;
    private List<String> userChosenAnswers;
    private String currentChosenAnswer;
    private String uid;
    private int challengeStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_question);

        uid = getIntent().getStringExtra("uid");
        challengeStatus = getIntent().getIntExtra(CHALLENGE_STATUS, PLAYER_SINGLE);

        setupRecyclerView();
        getQuestions();

        binding.nextQuestion.setOnClickListener(v -> {


            // before going to the next question ,storing the current chosenAnswers

            userChosenAnswers.add(currentChosenAnswer);

            populateQuestion();
        });


    }

    private void setupRecyclerView() {
        binding.answers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.answers.setHasFixedSize(true);

    }

    private void populateQuestion() {


        if (currentQuestion == results.size()) {
            Toast.makeText(this, "We are out of question", Toast.LENGTH_LONG).show();
            int wonPoints = AnswerChecker.getCorrectAnswerPoints(results, userChosenAnswers);
            FireBaseUtils.sendUserWonPoints(wonPoints, this);
            return;
        }
        QuestionResults result = results.get(currentQuestion++);


        //setting the question


        binding.question.setText(HTMLDecoder.decodeHtml(result.getQuestion()));

        //setting the answers
        binding.answers.setAdapter(new QuestionAdapter(this, result.getIncorrectAnswers()
                , result.getCorrectAnswer(), this));

        Log.e("CORRECT", currentQuestion + "");

        Log.e("CORRECT", result.getCorrectAnswer());

        //making the next question button greyed out and un-clickable
        binding.nextQuestion.setCardBackgroundColor(ContextCompat.getColor(this, R.color.next_question_disabled));
        binding.nextQuestion.setClickable(false);

    }

    private void getQuestions() {
        int amount = getIntent().getIntExtra(AMOUNT, 10);
        String type = getIntent().getStringExtra(TYPE);
        int catId = getIntent().getIntExtra(CAT_ID, 9);
        String difficulty = getIntent().getStringExtra(DIFFICULTY);

        QuestionDataDownloader dataDownloader = new QuestionDataDownloader();
        dataDownloader.getQuestions(amount, catId, type, difficulty, this);
    }

    @Override
    public void onResponse(QuestionResponse response) {

        if (response.getResponseCode() == NO_QUESTION_RESULT) {
            binding.noResult.setVisibility(View.VISIBLE);
        } else {
            binding.questionLayout.setVisibility(View.VISIBLE);
            results = response.getResults();
            populateQuestion();
            userChosenAnswers = new ArrayList<>();
        }

        binding.waitDialogLayout.waitDialog.setVisibility(View.GONE);

        asyncTask = new TimerAsyncTask();
        asyncTask.execute();


    }

    @Override
    public void onError(Throwable throwable) {
        Log.e(getLocalClassName(), throwable.getMessage());

    }

    @Override
    protected void onPause() {
        super.onPause();
        asyncTask.cancel(true);
    }

    @Override
    public void onItemClicked(int position, String chosenAnswer) {
        binding.nextQuestion.setClickable(true);
        binding.nextQuestion.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
        currentChosenAnswer = chosenAnswer;
    }

    @Override
    public void onItemClicked(int position) {

    }

    @Override
    public void onSent() {
        Toast.makeText(this, "Tamam valueaka roisht", Toast.LENGTH_SHORT).show();

    }

    private class TimerAsyncTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            for (int i = 10; i >= 0 && !isCancelled(); i--) {
                try {
                    Thread.sleep(1000);

                    onProgressUpdate(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // here check weather its finished with true result or failed with 0
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            runOnUiThread(() -> {
                binding.timeProgress.setStepCountText(String.valueOf(values[0]));
                binding.timeProgress.setPercentage(6 * values[0]);
                if (values[0] == 0)
                    SafeToast.makeText(QuestionActivity.this, "Time is Up, try Again", Toast.LENGTH_LONG).show();
            });
        }
    }

}
