package com.dmitriikuzmin.quizspringbootclient.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Question {
    private long id;

    @JsonProperty("type")
    @NonNull
    private String type;

    @JsonProperty("difficulty")
    @NonNull
    private String difficulty;

    @JsonProperty("category")
    @NonNull
    private String category;

    @JsonProperty("question")
    @NonNull
    private String question;

    @JsonProperty("correct_answer")
    @NonNull
    private String correctAnswer;

    @JsonProperty("incorrect_answers")
    @NonNull
    List<String> incorrectAnswers;

    @NonNull
    private Quiz quiz;

    @NonNull
    private QuestionResult result;
}