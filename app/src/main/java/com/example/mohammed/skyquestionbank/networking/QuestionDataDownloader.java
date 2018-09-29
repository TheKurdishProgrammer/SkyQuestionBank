package com.example.mohammed.skyquestionbank.networking;

import com.example.mohammed.skyquestionbank.networking.retrofit.MyRetrofitCallback;
import com.example.mohammed.skyquestionbank.networking.retrofit.QuestionApi;
import com.example.mohammed.skyquestionbank.networking.retrofit.QuestionService;
import com.example.mohammed.skyquestionbank.ui.ChooseQuestionCriteriaFragment;
import com.example.mohammed.skyquestionbank.ui.QuestionActivity;

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

}
