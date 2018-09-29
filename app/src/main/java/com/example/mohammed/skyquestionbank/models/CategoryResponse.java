package com.example.mohammed.skyquestionbank.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryResponse {

    @SerializedName("trivia_categories")
    private List<Category> triviaCategories;

    public List<Category> getTriviaCategories() {
        return triviaCategories;
    }

    public void setTriviaCategories(List<Category> triviaCategories) {
        this.triviaCategories = triviaCategories;
    }

}
