package com.dmitriikuzmin.quizspringbootclient;

import com.dmitriikuzmin.quizspringbootclient.controller.ControllerData;
import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.prefs.Preferences;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Preferences preferences = Preferences.userRoot();
        long userId = preferences.getLong("quizUserId", -1);
        if (userId == -1) {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("authorization.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 240, 360);
            stage.setTitle("Log In");
            stage.setScene(scene);
            stage.show();
        } else {
            //TODO auth by id?

            App.openWindow("main.fxml", "Quiz", null);
        }

    }

    public static void main(String[] args) {
        launch();
    }

    public static void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static <T> Stage getStage(String name, String title, T data) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource(name));
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setScene(new Scene(loader.load()));
        stage.setTitle(title);
        if (data != null) {
            ControllerData<T> controller = loader.getController();
            controller.initData(data);
        }
        return stage;
    }

    public static <T> Stage openWindow(String name, String title, T data) throws IOException {
        Stage stage = getStage(name, title, data);
        stage.show();
        return stage;
    }

    public static <T> Stage openWindowAndWait(String name, String title, T data) throws IOException {
        Stage stage = getStage(name, title, data);
        stage.showAndWait();
        return stage;
    }

    public static void closeWindow(Event event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}