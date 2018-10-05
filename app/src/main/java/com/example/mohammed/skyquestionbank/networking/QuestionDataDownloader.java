package com.example.mohammed.skyquestionbank.networking;

import android.support.annotation.NonNull;

import com.example.mohammed.skyquestionbank.firebase.FirebaseQuestionReferences;
import com.example.mohammed.skyquestionbank.interfaces.OnChallengeDifficulityLoad;
import com.example.mohammed.skyquestionbank.interfaces.OnResponseCallback;
import com.example.mohammed.skyquestionbank.models.QuestionResponse;
import com.example.mohammed.skyquestionbank.networking.retrofit.MyRetrofitCallback;
import com.example.mohammed.skyquestionbank.networking.retrofit.QuestionApi;
import com.example.mohammed.skyquestionbank.networking.retrofit.QuestionService;
import com.example.mohammed.skyquestionbank.ui.ChooseQuestionCriteriaFragment;
import com.example.mohammed.skyquestionbank.ui.QuestionActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import static com.example.mohammed.skyquestionbank.interfaces.FirebaseRefLinks.DUEL_CHALLENGE_QUESTIONS;
import static com.example.mohammed.skyquestionbank.interfaces.FirebaseRefLinks.QUESTION_DIFFICULITY;

public class QuestionDataDownloader {

    private QuestionApi questionApi;


    public QuestionDataDownloader() {

        questionApi = QuestionService.getQuestionService().create(QuestionApi.class);
    }

    public void getCategories(ChooseQuestionCriteriaFragment challengeYourself) {


        questionApi.getCategories().enqueue(new MyRetrofitCallback<>(challengeYourself));
    }


    public void getQuestions(int amount, int catId, String type, String difficulty, QuestionActivity questionActivity) {


        questionApi.getQuestions(amount, catId, type, difficulty)
                .enqueue(new MyRetrofitCallback<>(questionActivity));

    }

    public void getChallengeQuestions(OnResponseCallback<QuestionResponse> responseCallback, String uid) {
        DatabaseReference meAsChallengor = FirebaseQuestionReferences.getMeAsChallengingRef(uid);

        meAsChallengor.child(DUEL_CHALLENGE_QUESTIONS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    QuestionResponse response = dataSnapshot.getValue(QuestionResponse.class);
                    responseCallback.onResponse(response);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getChallengeQuestionDifficulity(OnChallengeDifficulityLoad load, String uid) {
        DatabaseReference challengeQuestionDiff = FirebaseQuestionReferences.getMeAsChallengingRef(uid)
                .child(QUESTION_DIFFICULITY);

        challengeQuestionDiff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                load.onLoad(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
