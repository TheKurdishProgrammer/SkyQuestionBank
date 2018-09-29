package com.example.mohammed.skyquestionbank.models;

public class User {
    private int points;
    private int hardQuestions;
    private int mediumQuestions;
    private int easyQuestions;

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getHardQuestions() {
        return hardQuestions;
    }

    public void setHardQuestions(int hardQuestions) {
        this.hardQuestions = hardQuestions;
    }

    public int getMediumQuestions() {
        return mediumQuestions;
    }

    public void setMediumQuestions(int mediumQuestions) {
        this.mediumQuestions = mediumQuestions;
    }

    public int getEasyQuestions() {
        return easyQuestions;
    }

    public void setEasyQuestions(int easyQuestions) {
        this.easyQuestions = easyQuestions;
    }
}
