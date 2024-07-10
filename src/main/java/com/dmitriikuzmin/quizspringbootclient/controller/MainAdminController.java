package com.dmitriikuzmin.quizspringbootclient.controller;

import com.dmitriikuzmin.quizspringbootclient.App;
import com.dmitriikuzmin.quizspringbootclient.model.Admin;
import com.dmitriikuzmin.quizspringbootclient.model.Participant;
import com.dmitriikuzmin.quizspringbootclient.model.Quiz;
import com.dmitriikuzmin.quizspringbootclient.retrofit.AdminRepository;
import com.dmitriikuzmin.quizspringbootclient.retrofit.ParticipantRepository;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

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

            this.participantsListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() == 2) {
                        Participant participant = participantsListView.getSelectionModel().getSelectedItem();
                        System.out.println(participant);
                        showParticipantQuizzes(participant);
                    }
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void logoutButton(ActionEvent actionEvent) {
        try {
            Preferences preferences = Preferences.userRoot();
            preferences.putLong("quizUserId", -1);
            preferences.put("quizUserToken", "");
            preferences.put("quizTokenDateTime", "");
            preferences.put("quizUserRole", "");
            App.openWindow("authorization.fxml", "Authorization", null);
            App.closeWindow(actionEvent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showParticipantQuizzes(Participant participant) {
        //TODO ad no quizzes case
        this.quizzesListView.setItems(FXCollections.observableList(participant.getQuizzes()));
    }
}
