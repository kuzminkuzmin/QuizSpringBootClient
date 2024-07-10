package com.dmitriikuzmin.quizspringbootclient.controller;

import com.dmitriikuzmin.quizspringbootclient.App;
import com.dmitriikuzmin.quizspringbootclient.model.Admin;
import com.dmitriikuzmin.quizspringbootclient.model.Participant;
import com.dmitriikuzmin.quizspringbootclient.model.User;
import com.dmitriikuzmin.quizspringbootclient.retrofit.AdminRepository;
import com.dmitriikuzmin.quizspringbootclient.retrofit.AuthRepository;
import com.dmitriikuzmin.quizspringbootclient.retrofit.ParticipantRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.prefs.Preferences;

public class AuthorizationController {
    @FXML
    public TextField loginTextField;
    @FXML
    public TextField passwordTextField;

    @FXML
    public void loginButton(ActionEvent actionEvent) {
        String login = loginTextField.getText();
        String password = passwordTextField.getText();
        AuthRepository authRepository = new AuthRepository();
        try {
            String secret = Base64.getEncoder().encodeToString("quiz".getBytes());
            String token = authRepository.auth(login, password);
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            String role = claims.getBody().get("role", String.class);

            if (role.equals("ADMIN")) {
                AdminRepository adminRepository = new AdminRepository(token);
                Admin admin = adminRepository.getByName(claims.getBody().getSubject());
                saveUserData(token, role, admin.getId(), admin);
                App.openWindow("mainAdmin.fxml", "Quiz administration", token);
                App.closeWindow(actionEvent);

            } else if (role.equals("PARTICIPANT")) {
                ParticipantRepository participantRepository = new ParticipantRepository(token);
                Participant participant = participantRepository.getByUsername(claims.getBody().getSubject());
                saveUserData(token, role, participant.getId(), participant);
                App.openWindow("main.fxml", "Quiz", token);
                App.closeWindow(actionEvent);
            }
        } catch (IOException | IllegalArgumentException e) {
            App.showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void saveUserData(String token, String role, long id, User user) {
        Preferences preferences = Preferences.userRoot();
        preferences.putLong("quizUserId", id);
        preferences.put("quizUserToken", token);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSS");
        preferences.put("quizTokenDateTime", LocalDateTime.now().format(dateTimeFormatter));
        preferences.put("quizUserRole", role);
    }

    @FXML
    public void signUpLink(MouseEvent mouseEvent) {
        try {
            App.openWindowAndWait("registration.fxml", "Registration", null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
