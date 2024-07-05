module com.dmitriikuzmin.quizspringbootclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires com.fasterxml.jackson.annotation;
    requires okhttp3;
    requires com.fasterxml.jackson.databind;
    requires retrofit2;
    requires retrofit2.converter.jackson;
    requires jjwt;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires java.prefs;


    opens com.dmitriikuzmin.quizspringbootclient to javafx.fxml;
    exports com.dmitriikuzmin.quizspringbootclient;
    exports com.dmitriikuzmin.quizspringbootclient.controller to javafx.fxml;
    opens com.dmitriikuzmin.quizspringbootclient.controller to javafx.fxml;
    exports com.dmitriikuzmin.quizspringbootclient.dto to com.fasterxml.jackson.databind;
    exports com.dmitriikuzmin.quizspringbootclient.model to com.fasterxml.jackson.databind;
}