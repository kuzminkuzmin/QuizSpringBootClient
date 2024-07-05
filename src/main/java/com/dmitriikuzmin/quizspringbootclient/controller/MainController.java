package com.dmitriikuzmin.quizspringbootclient.controller;

import com.dmitriikuzmin.quizspringbootclient.App;
import com.dmitriikuzmin.quizspringbootclient.model.Participant;
import com.dmitriikuzmin.quizspringbootclient.model.Quiz;
import com.dmitriikuzmin.quizspringbootclient.retrofit.ParticipantRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.util.Base64;
import java.util.prefs.Preferences;

public class MainController implements ControllerData<String> {
    private String token;

    private ParticipantRepository participantRepository;

    @FXML
    public ListView<Quiz> quizzesListView;

    @FXML
    public Label firstNameLabel;

    @FXML
    public Label lastNameLabel;

    @Override
    public void initData(String value) {
        this.token = value;
        this.participantRepository = new ParticipantRepository(this.token);
        Preferences preferences = Preferences.userRoot();
        try {
            Participant participant = this.participantRepository.get(preferences.getLong("quizUserId", -1));
            this.firstNameLabel.setText(participant.getFirstName());
            this.lastNameLabel.setText(participant.getLastName());
            this.quizzesListView.setItems(FXCollections.observableList(participant.getQuizzes()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void takeQuizButton(ActionEvent actionEvent) {
        try {
            App.openWindow("getQuiz.fxml", "Get new quiz", token);
            App.closeWindow(actionEvent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void exitButton(ActionEvent actionEvent) {
        try {
            Preferences preferences = Preferences.userRoot();
            preferences.putLong("quizUserId", -1);
            App.openWindow("authorization.fxml", "Authorization", null);
            App.closeWindow(actionEvent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
