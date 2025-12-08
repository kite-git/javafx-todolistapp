package kite.todolistapp;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ToDoListController {
    @FXML
    private TextField taskField;

    @FXML
    private TextField dueTimeField;

    @FXML
    private TextField newCategoryField;

    @FXML
    private DatePicker dueDatePicker;

    @FXML
    private Button addTaskButton;

    @FXML
    private Button deleteTaskButton;

    @FXML
    private Button updateTaskButton;

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private ListView<Task> taskListView;

    @FXML
    private ListView<String> categoryListView;

    @FXML
    private Button addCategoryButton;

    @FXML
    private Button deleteCategoryButton;

    @FXML
    private Label statusLabel;

    private ArrayList<Task> tasks = new ArrayList<>();
    private ArrayList<String> categories = new ArrayList<>();

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .setPrettyPrinting()
            .create();

    @FXML
    private void initialize() {
        loadDataFromJSON();

        if (categories.isEmpty()) {
            categories.add("Personal");
        }

        categoryComboBox.getItems().add("Select Category");
        categoryComboBox.getSelectionModel().select(0);

        categoryComboBox.getItems().addAll(categories);
        categoryListView.getItems().setAll(categories);

        statusComboBox.getItems().add("Select Status");
        statusComboBox.getSelectionModel().select(0);

        statusComboBox.getItems().addAll("Not Started", "In Progress", "On Hold", "Done");

        categoryComboBox.setCellFactory(list -> new ListCell<String>() {
           @Override
           protected void updateItem(String item, boolean empty) {
               super.updateItem(item, empty);
               if (empty || item == null) {
                   setText(null);
               } else {
                   setText(item);
                   setDisable(item.equals("Select Category"));
               }
           }
        });

        categoryComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item);
            }
        });

        statusComboBox.setCellFactory(list -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setDisable(item.equals("Select Status"));
                }
            }
        });

        statusComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item);
            }
        });


        taskListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                taskField.setText(newVal.getTaskName());
                dueTimeField.setText(newVal.getDueTime());
                dueDatePicker.setValue(newVal.getDueDate());
                categoryComboBox.setValue(newVal.getCategory());
                statusComboBox.setValue(newVal.getStatus());
            }
        });

        updateListTask();
    }

    @FXML
    private void handleAddCategory() {
        String category = newCategoryField.getText().trim();
        if (category.isEmpty()) {
            statusLabel.setText("Category is empty.");
            statusLabel.setStyle("-fx-text-fill: red");
            return;
        }
        if (categories.contains(category)) {
            statusLabel.setText("Category already exists.");
            statusLabel.setStyle("-fx-text-fill: red");
        } else {
            categories.add(category);
            categoryListView.getItems().setAll(categories);
            saveDataToJSON();

            categoryComboBox.getItems().clear();
            categoryComboBox.getItems().add("Select Category");
            categoryComboBox.getItems().addAll(categories);

            categoryComboBox.getSelectionModel().select(0);

            statusLabel.setText("Category added.");
            statusLabel.setStyle("-fx-text-fill: green");
            newCategoryField.clear();
        }
    }

    @FXML
    protected void handleDeleteCategory() {
        String selected = categoryListView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            statusLabel.setText("Select a Category.");
            statusLabel.setStyle("-fx-text-fill: red");
            return;
        }

        categories.remove(selected);

        categoryListView.getItems().setAll(categories);
        saveDataToJSON();

        categoryComboBox.getItems().clear();
        categoryComboBox.getItems().add("Select Category");
        categoryComboBox.getItems().addAll(categories);

        categoryComboBox.getSelectionModel().select(0);

        taskListView.getItems().removeIf(task -> task.getCategory().equals(selected));

        statusLabel.setText("Category deleted.");
        statusLabel.setStyle("-fx-text-fill: green");
    }

    @FXML
    protected void handleAddTask() {
        if (!isInputValid()) {
            return;
        }

        String taskName = taskField.getText().trim();
        String dueTime = dueTimeField.getText().trim();
        LocalDate dueDate = dueDatePicker.getValue();
        String category = categoryComboBox.getValue().trim();
        String status = statusComboBox.getValue().trim();

        Task newT = new Task(taskName, dueDate, dueTime, category, status);

        tasks.add(newT);
        updateListTask();
        saveDataToJSON();
        clearForm();

        statusLabel.setText("Task added.");
        statusLabel.setStyle("-fx-text-fill: green");
        }

    @FXML
    protected void handleDeleteTask() {
        Task selected = taskListView.getSelectionModel().getSelectedItem();

        if (selected != null) {
            tasks.remove(selected);
            updateListTask();
            saveDataToJSON();
            statusLabel.setText("Task deleted.");
            statusLabel.setStyle("-fx-text-fill: green");
            clearForm();
        } else {
            statusLabel.setText("Task not found.");
            statusLabel.setStyle("-fx-text-fill: red");
        }
    }

    @FXML
    protected void handleUpdateTask() {
        Task selected = taskListView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            statusLabel.setText("Select a task.");
            statusLabel.setStyle("-fx-text-fill: red");
            return;
        }

        if (!isInputValid()) {
            return;
        }

        selected.setTaskName(taskField.getText().trim());
        selected.setDueTime(dueTimeField.getText().trim());
        selected.setDueDate(dueDatePicker.getValue());
        selected.setCategory(categoryComboBox.getValue().trim());
        selected.setStatus(statusComboBox.getValue().trim());

        updateListTask();
        saveDataToJSON();
        clearForm();

        statusLabel.setText("Task edited.");
        statusLabel.setStyle("-fx-text-fill: green");
    }

    private void updateListTask() {
        taskListView.getItems().setAll(tasks);
    }

    private boolean isInputValid() {
        if (taskField.getText().isEmpty()) {
            statusLabel.setText("Please enter a task.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return false;
        }
        if (dueTimeField.getText().isEmpty()) {
            statusLabel.setText("Please enter a due time.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return false;
        }
        if (dueDatePicker.getValue() == null) {
            statusLabel.setText("Please enter a due date.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return false;
        }
        if (categoryComboBox.getSelectionModel().getSelectedIndex() == 0) {
            statusLabel.setText("Please select a category.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return false;
        }
        if (statusComboBox.getSelectionModel().getSelectedIndex() == 0) {
            statusLabel.setText("Please select a status.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return false;
        }
        return true;
    }

    private void clearForm() {
        taskField.clear();
        dueTimeField.clear();
        dueDatePicker.setValue(null);

        categoryComboBox.getSelectionModel().select(0);
        statusComboBox.getSelectionModel().select(0);
    }

    private void saveDataToJSON() {
        SaveData data = new SaveData(tasks, categories);

        try (FileWriter writer = new FileWriter("todolist_data.json")) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            System.out.println("Error while saving data to JSON.");
        }
    }

    private void loadDataFromJSON() {
        try (FileReader reader = new FileReader("todolist_data.json")) {
            SaveData data = gson.fromJson(reader, SaveData.class);

            if (data != null) {
                tasks = data.tasks != null ? data.tasks : new ArrayList<>();
                categories = data.categories != null ? data.categories : new ArrayList<>();
            }
        } catch (IOException e) {
            System.out.println("Error while loading data from JSON.");
        }
    }
}