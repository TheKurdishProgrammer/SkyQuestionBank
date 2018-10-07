package com.example.mohammed.skyquestionbank.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class QuestionResponse {

    /**
     * response_code : 0
     * results : [{"category":"Science: Computers","type":"multiple","difficulty":"easy","question":"In any programming language, what is the most common way to iterate through an array?","correct_answer":"&#039;For&#039; loops","incorrect_answers":["&#039;If&#039; Statements","&#039;Do-while&#039; loops","&#039;While&#039; loops"]},{"category":"Science: Computers","type":"multiple","difficulty":"easy","question":"HTML is what type of language?","correct_answer":"Markup Language","incorrect_answers":["Macro Language","Programming Language","Scripting Language"]},{"category":"Science: Computers","type":"multiple","difficulty":"easy","question":"What amount of bits commonly equals one byte?","correct_answer":"8","incorrect_answers":["1","2","64"]},{"category":"Science: Computers","type":"multiple","difficulty":"easy","question":"What is the most preferred image format used for logos in the Wikimedia database?","correct_answer":".svg","incorrect_answers":[".png",".jpeg",".gif"]},{"category":"Science: Computers","type":"multiple","difficulty":"easy","question":"In web design, what does CSS stand for?","correct_answer":"Cascading Style Sheet","incorrect_answers":["Counter Strike: Source","Corrective Style Sheet","Computer Style Sheet"]},{"category":"Science: Computers","type":"multiple","difficulty":"easy","question":"On Twitter, what is the character limit for a Tweet?","correct_answer":"140","incorrect_answers":["120","160","100"]},{"category":"Science: Computers","type":"multiple","difficulty":"easy","question":"What does LTS stand for in the software market?","correct_answer":"Long Term Support","incorrect_answers":["Long Taco Service","Ludicrous Transfer Speed","Ludicrous Turbo Speed"]},{"category":"Science: Computers","type":"multiple","difficulty":"easy","question":"The numbering system with a radix of 16 is more commonly referred to as ","correct_answer":"Hexidecimal","incorrect_answers":["Binary","Duodecimal","Octal"]},{"category":"Science: Computers","type":"multiple","difficulty":"easy","question":"This mobile OS held the largest market share in 2012.","correct_answer":"iOS","incorrect_answers":["Android","BlackBerry","Symbian"]},{"category":"Science: Computers","type":"multiple","difficulty":"easy","question":"Which programming language shares its name with an island in Indonesia?","correct_answer":"Java","incorrect_answers":["Python","C","Jakarta"]}]
     */

    @SerializedName("response_code")
    private int responseCode;
    @SerializedName("results")
    private ArrayList<QuestionResults> results;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public ArrayList<QuestionResults> getResults() {
        return results;
    }

    public void setResults(ArrayList<QuestionResults> results) {
        this.results = results;
    }
}
