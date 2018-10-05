package com.example.mohammed.skyquestionbank.utils;

import android.util.Log;

import com.example.mohammed.skyquestionbank.models.QuestionResults;

import java.util.List;

import static com.example.mohammed.skyquestionbank.ui.ChooseQuestionCriteriaFragment.DIFFICULTY_EASY;
import static com.example.mohammed.skyquestionbank.ui.ChooseQuestionCriteriaFragment.DIFFICULTY_HARD;
import static com.example.mohammed.skyquestionbank.ui.ChooseQuestionCriteriaFragment.DIFFICULTY_MEDIUM;

public class AnswerChecker {


    public static int getCorrectAnswerPoints(String questionType, List<QuestionResults> results, List<String> userAnswers) {

        int correctAnswerPoints = 0;

        Log.e("SIZE_R", "" + questionType);

        for (int i = 0; i < results.size(); i++) {

            if (results.get(i).getCorrectAnswer().equals(userAnswers.get(i))) {
                switch (questionType) {
                    case DIFFICULTY_EASY:
                        correctAnswerPoints = +1;
                        break;
                    case DIFFICULTY_MEDIUM:
                        correctAnswerPoints = +2;
                        break;
                    case DIFFICULTY_HARD:
                        correctAnswerPoints = +3;
                        break;
                }
            }
        }

        Log.e("POINTS", String.valueOf(correctAnswerPoints));

        return correctAnswerPoints;
    }

}
