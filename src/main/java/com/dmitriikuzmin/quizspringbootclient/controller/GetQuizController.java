package com.dmitriikuzmin.quizspringbootclient.controller;

import com.dmitriikuzmin.quizspringbootclient.App;
import com.dmitriikuzmin.quizspringbootclient.model.Participant;
import com.dmitriikuzmin.quizspringbootclient.model.Quiz;
import com.dmitriikuzmin.quizspringbootclient.retrofit.ParticipantRepository;
import com.dmitriikuzmin.quizspringbootclient.retrofit.QuizRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

public class GetQuizController implements ControllerData<String> {
    private String token;
    private ParticipantRepository participantRepository;
    private Participant participant;
    private HashMap<String, Integer> category = new HashMap<>();

    @FXML
    public TextField numberOfQuestionsTextField;

    @FXML
    public ComboBox<String> categoryComboBox;

    @FXML
    public ComboBox<String> difficultyComboBox;

    @Override
    public void initData(String value) {
        this.token = value;
        Jws<Claims> claims = Jwts.parser()
                .setSigningKey(Base64.getEncoder().encodeToString("quiz".getBytes()))
                .parseClaimsJws(this.token);
        this.participantRepository = new ParticipantRepository(this.token);

        try {
            this.participant = this.participantRepository.getByUsername(claims.getBody().getSubject());
            this.category.put("Animals", 27);
            this.category.put("Geography", 22);
            this.category.put("General Knowledge", 9);
            this.category.put("Science & Nature", 17);
            this.categoryComboBox.getItems().addAll(category.keySet());

            List<String> difficulty = Arrays.asList(new String[]{"Easy", "Medium", "Hard"});
            this.difficultyComboBox.getItems().addAll(difficulty);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void startButton(ActionEvent actionEvent) {
        int numberOfQuestions = Integer.parseInt(numberOfQuestionsTextField.getText());
        if (numberOfQuestions < 1 || numberOfQuestions > 10) {
            App.showAlert("Error", "Number must be from 1 to 10", Alert.AlertType.ERROR);
        }
        String category = categoryComboBox.getSelectionModel().getSelectedItem();
        int categoryInt = this.category.get(category);
        if (category == null || category.isEmpty()) {
            App.showAlert("Error", "Please select a category", Alert.AlertType.ERROR);
        }
        String difficulty = difficultyComboBox.getSelectionModel().getSelectedItem();
        if (difficulty == null || difficulty.isEmpty()) {
            App.showAlert("Error", "Please select a difficulty", Alert.AlertType.ERROR);
        }
        String settings = "?amount=" + numberOfQuestions + "&category=" + categoryInt + "&difficulty=" + difficulty;

        QuizRepository quizRepository = new QuizRepository(token);
        try {
            Quiz quiz = quizRepository.post(this.participant.getId(), settings);
            System.out.println(quiz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void saveButton(ActionEvent actionEvent) {
    }
}
