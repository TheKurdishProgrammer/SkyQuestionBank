package com.example.mohammed.skyquestionbank.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.mohammed.skyquestionbank.models.QuestionResults;

import java.util.List;

import static com.example.mohammed.skyquestionbank.ui.ChooseQuestionCriteriaFragment.DIFFICULTY_EASY;
import static com.example.mohammed.skyquestionbank.ui.ChooseQuestionCriteriaFragment.DIFFICULTY_HARD;
import static com.example.mohammed.skyquestionbank.ui.ChooseQuestionCriteriaFragment.DIFFICULTY_MEDIUM;
import static com.example.mohammed.skyquestionbank.ui.WinActivity.USER_POINTS;

public class AnswerChecker {


    public static int getCorrectAnswerPoints(String questionType, List<QuestionResults> results, List<String> userAnswers) {

        int correctAnswerPoints = 0;


        for (int i = 0; i < results.size(); i++) {

            if (results.get(i).getCorrectAnswer().equals(userAnswers.get(i))) {

                switch (questionType) {
                    case DIFFICULTY_EASY:
                        correctAnswerPoints = correctAnswerPoints + 1;
                        break;
                    case DIFFICULTY_MEDIUM:
                        correctAnswerPoints = correctAnswerPoints + 2;
                        break;
                    case DIFFICULTY_HARD:
                        correctAnswerPoints = correctAnswerPoints + 3;
                        break;
                }
            }
        }

        Log.e("POINTS", String.valueOf(correctAnswerPoints));

        return correctAnswerPoints;
    }

    public static void storeUserPoints(Context context, int wontPoints) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt(USER_POINTS, wontPoints);
        editor.apply();
//
//        AppWidgetManager widget = AppWidgetManager.getInstance(context);
//        ComponentName name = new ComponentName(context,);
//        widget.getAppWidgetIds(name);
//
//        widget.updateAppWidget(, );

    }

}
