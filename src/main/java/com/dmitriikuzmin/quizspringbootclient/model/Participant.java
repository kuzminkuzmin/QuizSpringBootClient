package com.dmitriikuzmin.quizspringbootclient.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Participant extends User {
    private List<Quiz> quizzes = new ArrayList<>();

    @Override
    public String toString() {
        return this.getFirstName() + " " + this.getLastName();
    }
}
