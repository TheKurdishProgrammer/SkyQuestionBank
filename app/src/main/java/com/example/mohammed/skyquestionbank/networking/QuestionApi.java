package com.example.mohammed.skyquestionbank.networking;

import com.example.mohammed.skyquestionbank.models.QuestionCategory;
import com.example.mohammed.skyquestionbank.models.QuestionResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.example.mohammed.skyquestionbank.networking.QuestionApiUrl.category;
import static com.example.mohammed.skyquestionbank.networking.QuestionApiUrl.question;

public interface QuestionApi {

    @GET(category)
    Call<QuestionCategory> getCategories();

    @GET(question)
    Call<QuestionResponse> getQuestions(@Query("amount") int amount,
                                        @Query("category") int category,
                                        @Query("type") String type,
                                        @Query("difficulty") String difficulty);
}
