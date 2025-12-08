module kite.todolistapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens kite.todolistapp to javafx.fxml, com.google.gson;
    exports kite.todolistapp;
}