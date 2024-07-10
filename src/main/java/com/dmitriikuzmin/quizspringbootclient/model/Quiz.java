package com.dmitriikuzmin.quizspringbootclient.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Quiz {
    private long id;

    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime dateTimeRequested;

    @NonNull
    private Participant participant;

    @JsonProperty("results")
    @NonNull
    private List<Question> questions = new ArrayList<>();

    @Override
    public String toString() {
        long avg = 0;
        long wrightCounter = 0;
        for (Question question : this.questions) {
            if (question.getResult().equals(QuestionResult.RIGHT)) {
                wrightCounter++;
            }
        }
        avg = wrightCounter / questions.size();
        return questions.get(0).getCategory() + ", " +
                questions.get(0).getDifficulty() + ", average result: " +
                avg;
    }
}
