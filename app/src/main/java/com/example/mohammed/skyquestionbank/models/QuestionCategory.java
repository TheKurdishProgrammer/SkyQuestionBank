package com.example.mohammed.skyquestionbank.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuestionCategory {

    @SerializedName("trivia_categories")
    private List<TriviaCategoriesBean> triviaCategories;

    public List<TriviaCategoriesBean> getTriviaCategories() {
        return triviaCategories;
    }

    public void setTriviaCategories(List<TriviaCategoriesBean> triviaCategories) {
        this.triviaCategories = triviaCategories;
    }
}
