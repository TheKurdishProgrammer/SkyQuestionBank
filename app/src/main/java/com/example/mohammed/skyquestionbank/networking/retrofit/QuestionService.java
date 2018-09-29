package com.example.mohammed.skyquestionbank.networking.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuestionService {

    private static Retrofit retrofit = null;


    public static Retrofit getQuestionService() {

        if (retrofit == null) {
            return retrofit = new Retrofit.Builder().
                    addConverterFactory(GsonConverterFactory.create()).
                    baseUrl(QuestionApiUrl.baseUrl).build();
        }


        return retrofit;
    }
}
