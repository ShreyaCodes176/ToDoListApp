import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ToDoList extends Application {

    private final ObservableList<String> tasks = FXCollections.observableArrayList();
    private final String FILE_PATH = "tasks.txt";

    @Override
    public void start(Stage stage) {
    stage.setTitle("To-Do List App");

        // ---- Title ----
    Label title = new Label("My To-Do List");
        title.setFont(Font.font("Poppins", 24));
        title.setTextFill(Color.web("#2c3e50"));

        // ---- Input ----
        TextField taskInput = new TextField();
        taskInput.setPromptText("Enter a new task...");
        taskInput.setFont(Font.font("Poppins", 14));
        taskInput.setPrefWidth(400);
        taskInput.setStyle("-fx-background-radius: 10; -fx-padding: 8;");

        // ---- Buttons ----
    Button addButton = makeButton("Add", "#27ae60");
    Button doneButton = makeButton("Done", "#2980b9");
    Button deleteButton = makeButton("Delete", "#c0392b");

        HBox buttonBox = new HBox(10, addButton, doneButton, deleteButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));

        // ---- Task List ----
    ListView<String> listView = new ListView<>(tasks);
    listView.setStyle(
        "-fx-font-family: 'Poppins';\n"
            + "-fx-font-size: 14;\n"
            + "-fx-background-radius: 12;\n"
            + "-fx-control-inner-background: #f8f9fa;\n"
            + "-fx-border-color: #dcdde1;\n"
            + "-fx-border-radius: 12;"
    );

        // ---- Layout ----
        VBox root = new VBox(15, title, listView, taskInput, buttonBox);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #dff9fb, #c7ecee);");

        // ---- Load saved tasks ----
        loadTasksFromFile();

        // ---- Button Actions ----
        addButton.setOnAction(e -> {
            String task = taskInput.getText().trim();
            if (!task.isEmpty()) {
                tasks.add(task);
                taskInput.clear();
                saveTasksToFile();
            }
        });

        deleteButton.setOnAction(e -> {
            String selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                tasks.remove(selected);
                saveTasksToFile();
            }
        });

        doneButton.setOnAction(e -> {
            String selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null && !selected.startsWith("[Done] ")) {
                int index = tasks.indexOf(selected);
                tasks.set(index, "[Done] " + selected);
                saveTasksToFile();
            }
        });

        // ---- Scene Setup ----
        Scene scene = new Scene(root, 520, 480);
        stage.setScene(scene);
        stage.show();
    }

    // Helper to create styled buttons
    private Button makeButton(String text, String color) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Poppins", 14));
        btn.setTextFill(Color.WHITE);
        btn.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 10;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: derive(" + color + ", 20%); -fx-background-radius: 10;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 10;"));
        btn.setPrefWidth(100);
        btn.setPrefHeight(35);
        return btn;
    }

    private void loadTasksFromFile() {
        try {
            if (Files.exists(Paths.get(FILE_PATH))) {
                tasks.addAll(Files.readAllLines(Paths.get(FILE_PATH)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveTasksToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String task : tasks) {
                writer.write(task + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
