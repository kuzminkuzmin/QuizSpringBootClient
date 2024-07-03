package com.dmitriikuzmin.quizspringbootclient.controller;

import com.dmitriikuzmin.quizspringbootclient.App;
import com.dmitriikuzmin.quizspringbootclient.model.Participant;
import com.dmitriikuzmin.quizspringbootclient.retrofit.ParticipantRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class RegistrationController {
    @FXML
    public TextField loginTextField;

    @FXML
    public TextField passwordTextField;

    @FXML
    public TextField firstNameTextField;

    @FXML
    public TextField lastNameTextField;

    @FXML
    public void registerButton(ActionEvent actionEvent) {
        String login = loginTextField.getText();
        String password = passwordTextField.getText();
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        ParticipantRepository participantRepository = new ParticipantRepository(null);
        try {
            Participant participant = Participant.builder()
                    .username(login)
                    .password(password)
                    .firstName(firstName)
                    .lastName(lastName)
                    .build();
            Participant newParticipant = participantRepository.post(participant);
            App.showAlert(
                    "Registration",
                    newParticipant.getUsername() + " created. Please sign in to start.",
                    Alert.AlertType.INFORMATION);
            App.closeWindow(actionEvent);
        } catch (IOException | IllegalArgumentException e) {
            App.showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void loginLabel(MouseEvent mouseEvent) {
        try {
            App.openWindow("authorization.fxml", "Authorization", null);
            App.closeWindow(mouseEvent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
