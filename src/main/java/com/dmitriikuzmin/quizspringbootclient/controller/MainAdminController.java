package com.dmitriikuzmin.quizspringbootclient.controller;

import com.dmitriikuzmin.quizspringbootclient.App;
import com.dmitriikuzmin.quizspringbootclient.model.Admin;
import com.dmitriikuzmin.quizspringbootclient.model.Participant;
import com.dmitriikuzmin.quizspringbootclient.model.Question;
import com.dmitriikuzmin.quizspringbootclient.model.Quiz;
import com.dmitriikuzmin.quizspringbootclient.retrofit.AdminRepository;
import com.dmitriikuzmin.quizspringbootclient.retrofit.ParticipantRepository;
import javafx.animation.ParallelTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.util.List;
import java.util.prefs.Preferences;

public class MainAdminController implements ControllerData<String> {
    private String token;

    private AdminRepository adminRepository;

    @FXML
    public ListView<Participant> participantsListView;

    @FXML
    public ListView<Quiz> quizzesListView;

    @FXML
    public Label positionLabel;

    @FXML
    public Label firstnameLabel;

    @FXML
    public Label lastnameLabel;

    @Override
    public void initData(String value) {
        this.token = value;
        this.adminRepository = new AdminRepository(token);
        Preferences preferences = Preferences.userRoot();
        try {
            Admin admin = this.adminRepository.get(preferences.getLong("quizUserId", -1));
            this.positionLabel.setText(admin.getPosition());
            this.firstnameLabel.setText(admin.getFirstName());
            this.lastnameLabel.setText(admin.getLastName());
            ParticipantRepository participantRepository = new ParticipantRepository(token);
            List<Participant> participants = participantRepository.getAll();
            this.participantsListView.setItems(FXCollections.observableList(participants));
            this.quizzesListView.setItems(FXCollections.observableList(participants.get(0).getQuizzes()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void logoutButton(ActionEvent actionEvent) {
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
