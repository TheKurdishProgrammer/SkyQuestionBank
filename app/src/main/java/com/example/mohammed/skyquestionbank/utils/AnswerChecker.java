package com.example.mohammed.skyquestionbank.utils;

import android.util.Log;

import com.example.mohammed.skyquestionbank.models.QuestionResults;

import java.util.List;

public class AnswerChecker {


    public static int getCorrectAnswerPoints(List<QuestionResults> results, List<String> correctAnswers) {

        int correctAnswerPoints = 0;

        for (int i = 0; i < results.size(); i++) {
            if (results.get(i).getCorrectAnswer().equals(correctAnswers.get(i)))
                correctAnswerPoints++;
        }

        Log.e("POINTS", String.valueOf(correctAnswerPoints));

        return correctAnswerPoints;
    }

}
