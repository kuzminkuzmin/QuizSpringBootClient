package com.dmitriikuzmin.quizspringbootclient.controller;

import com.dmitriikuzmin.quizspringbootclient.App;
import com.dmitriikuzmin.quizspringbootclient.model.Question;
import com.dmitriikuzmin.quizspringbootclient.model.QuestionResult;
import com.dmitriikuzmin.quizspringbootclient.model.Quiz;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizController implements ControllerData<Quiz> {
    private Quiz quiz;

    private int quizProgress = 0;
    private final HBox header = new HBox();
    private final VBox body = new VBox();
    private final HBox buttonsHBox = new HBox();
    private boolean chosen = false;
    private boolean right = false;

    @FXML
    public VBox contentVBox;

    @Override
    public void initData(Quiz value) {
        this.quiz = value;
        this.contentVBox.setSpacing(16);
        this.body.setSpacing(8);
        this.body.getChildren().add(nextQuestion());

        Button nextButton = new Button("Next");
        this.buttonsHBox.getChildren().add(nextButton);
        EventHandler<ActionEvent> nextButtonHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (chosen && quizProgress + 1 == quiz.getQuestions().size()) {
                    body.getChildren().add(showResult());
                } else if (chosen) {
                    // answer result
                    if (right) {
                        quiz.getQuestions().get(quizProgress).setResult(QuestionResult.RIGHT);
                    } else {
                        quiz.getQuestions().get(quizProgress).setResult(QuestionResult.WRONG);
                    }

                    // clear bool
                    chosen = false;

                    // clear containers
                    clearContainers();

                    // ++progress
                    quizProgress++;

                    // show next question
                    body.getChildren().add(nextQuestion());
                } else {
                    App.showAlert("Warning", "Choose answer", Alert.AlertType.WARNING);
                }
            }
        };
        nextButton.setOnAction(nextButtonHandler);
        this.contentVBox.getChildren().addAll(header, body, buttonsHBox);
    }

    private VBox nextQuestion() {
        Label progressLabel = new Label(quizProgress + 1 + "/" + quiz.getQuestions().size());
        header.getChildren().add(progressLabel);

        Label questionLabel = new Label(this.quiz.getQuestions().get(quizProgress).getQuestion());
        questionLabel.setStyle("-fx-font-weight: bold;");

        List<String> answers = new ArrayList<>(this.quiz.getQuestions().get(quizProgress).getIncorrectAnswers());
        answers.add(this.quiz.getQuestions().get(quizProgress).getCorrectAnswer());
        Collections.shuffle(answers);
        ArrayList<RadioButton> radioButtons = new ArrayList<>();
        ToggleGroup radioGroup = new ToggleGroup();
        for (String answer : answers) {
            RadioButton radioButton = new RadioButton(answer);
            radioButton.setToggleGroup(radioGroup);
            radioButtons.add(radioButton);
        }
        radioGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle t1) {
                RadioButton radioButton = (RadioButton) radioGroup.getSelectedToggle();
                if (radioButton != null) {
                    chosen = true;
                    right = quiz.getQuestions().get(quizProgress).getCorrectAnswer().equals(radioButton.getText());
                }
            }
        });

        VBox answersVBox = new VBox();
        answersVBox.setSpacing(8);
        radioButtons.forEach(radioButton -> {
            answersVBox.getChildren().add(radioButton);
        });

        VBox questionVBox = new VBox();
        questionVBox.setSpacing(8);
        questionVBox.getChildren().addAll(questionLabel, answersVBox);

        return questionVBox;
    }

    private ScrollPane showResult() {
        clearContainers();
        this.buttonsHBox.getChildren().clear();
        Button doneButton = new Button("Done");
        this.buttonsHBox.setSpacing(24);
        this.buttonsHBox.getChildren().add(doneButton);

        Label resultLabel = new Label("Your quiz result:"
                + this.quiz.getQuestions().stream().filter(x -> x.getResult() == QuestionResult.RIGHT).count()
                + "/"
                + this.quiz.getQuestions().size());
        header.getChildren().add(resultLabel);

        ScrollPane resultPane = new ScrollPane();
        VBox vBox = new VBox();
        resultPane.setContent(vBox);
        vBox.setSpacing(16);

        for (Question question : this.quiz.getQuestions()) {
            VBox questionVBox = new VBox();
            questionVBox.setSpacing(8);
            questionVBox.getChildren().add(new Label(question.getQuestion()));
            questionVBox.getChildren().add(new Label(question.getCorrectAnswer()));
            vBox.getChildren().add(questionVBox);
        }

        EventHandler<ActionEvent> doneButtonHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (chosen && quizProgress + 1 == quiz.getQuestions().size()) {
                    App.closeWindow(actionEvent);
                }
            }
        };
        doneButton.setOnAction(doneButtonHandler);
        return resultPane;
    }

    private void clearContainers() {
        this.header.getChildren().clear();
        this.body.getChildren().clear();
    }
}
