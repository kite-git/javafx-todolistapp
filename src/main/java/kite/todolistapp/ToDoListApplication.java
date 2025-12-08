package kite.todolistapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ToDoListApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ToDoListApplication.class.getResource("to-do-gui.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        scene.getStylesheets().add(ToDoListApplication.class.getResource("styles.css").toExternalForm());

        stage.setTitle("To-Do List");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}