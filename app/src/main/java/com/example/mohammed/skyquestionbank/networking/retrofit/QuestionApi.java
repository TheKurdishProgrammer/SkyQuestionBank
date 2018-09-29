package com.example.mohammed.skyquestionbank.networking.retrofit;

import com.example.mohammed.skyquestionbank.models.CategoryResponse;
import com.example.mohammed.skyquestionbank.models.QuestionResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.example.mohammed.skyquestionbank.networking.retrofit.QuestionApiUrl.category;
import static com.example.mohammed.skyquestionbank.networking.retrofit.QuestionApiUrl.question;

public interface QuestionApi {

    @GET(category)
    Call<CategoryResponse> getCategories();

    @GET(question)
    Call<QuestionResponse> getQuestions(@Query("amount") int amount,
                                        @Query("category") int category,
                                        @Query("type") String type,
                                        @Query("difficulty") String difficulty);
}
