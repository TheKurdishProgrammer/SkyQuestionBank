package com.example.mohammed.skyquestionbank.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mohammed.skyquestionbank.R;
import com.example.mohammed.skyquestionbank.interfaces.OnRecyclerItemClick;
import com.example.mohammed.skyquestionbank.utils.HTMLDecoder;

import java.util.List;
import java.util.Random;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.AnswerViewHolder> {

    private Context context;
    private List<String> incorrectAnswers;
    private String correctAnswer;
    private int correctAnswerPositon;
    private boolean isCorrectAnswerSet;
    private OnRecyclerItemClick onRecyclerItemClick;
    private AnswerViewHolder lastHolder;
    private int lastSelectedAnswerBeforeRoration;


    public QuestionAdapter(int lastSelectedAnswerBeforeRoration, Context context, List<String> incorrectAnswers, String correctAnswer, OnRecyclerItemClick onRecyclerItemClick) {
        this.context = context;
        this.incorrectAnswers = incorrectAnswers;
        this.correctAnswer = correctAnswer;
        correctAnswerPositon = new Random().nextInt(incorrectAnswers.size() + 1);
        this.onRecyclerItemClick = onRecyclerItemClick;
        this.lastSelectedAnswerBeforeRoration = lastSelectedAnswerBeforeRoration;
    }


    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.question_answer_item, parent, false);

        return new AnswerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder holder, int position) {


        if (lastSelectedAnswerBeforeRoration != -1 && position == lastSelectedAnswerBeforeRoration) {
            holder.answerCard.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
            lastHolder = holder;
        }

        if (position == correctAnswerPositon) {
            holder.answer.setText(HTMLDecoder.decodeHtml(correctAnswer));
            isCorrectAnswerSet = true;
        } else {

            holder.answer.setText(isCorrectAnswerSet ?
                    HTMLDecoder.decodeHtml(incorrectAnswers.get(position - 1))
                    : HTMLDecoder.decodeHtml(incorrectAnswers.get(position)));

        }

        holder.itemView.setOnClickListener(v -> {


            onRecyclerItemClick.onItemClicked(holder.getAdapterPosition(), holder.answer.getText().toString());

            holder.answerCard.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));

            if (lastHolder != null && lastHolder != holder) {
                lastHolder.answerCard.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
            }

            lastHolder = holder;

        });
    }

    @Override
    public int getItemCount() {
        return incorrectAnswers.size() + 1;
    }

    public class AnswerViewHolder extends RecyclerView.ViewHolder {

        private TextView answer;
        private CardView answerCard;

        AnswerViewHolder(View itemView) {
            super(itemView);

            answer = itemView.findViewById(R.id.question_answer);
            answerCard = itemView.findViewById(R.id.answer_cardview);
        }
    }


}
