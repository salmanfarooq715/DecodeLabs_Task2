import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class DecodeLabs_JavaFX_P2 extends Application {

    private Stage stage;
    private List<TextField> subjectNameFields = new ArrayList<>();
    private List<TextField> marksFields = new ArrayList<>();
    private Label errorLabel;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        stage.setTitle("DecodeLabs - Student Grade Calculator");
        showSubjectCountScreen();
    }

    // ---------------- SCREEN 1: Ask number of subjects ----------------
    private void showSubjectCountScreen() {

        Label title = new Label("Student Grade Calculator");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        Label subtitle = new Label("How many subjects do you want to enter?");
        subtitle.setFont(Font.font("Arial", 14));

        TextField countField = new TextField();
        countField.setMaxWidth(100);
        countField.setAlignment(Pos.CENTER);
        countField.setStyle("-fx-font-size: 16px;");
        countField.setPromptText("e.g. 5");

        errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        Button nextButton = new Button("Continue");
        nextButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        nextButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        nextButton.setOnAction(e -> {
            try {
                int count = Integer.parseInt(countField.getText().trim());
                if (count <= 0 || count > 20) {
                    errorLabel.setText("Please enter a number between 1 and 20.");
                    return;
                }
                showMarksInputScreen(count);
            } catch (NumberFormatException ex) {
                errorLabel.setText("Please enter a valid whole number.");
            }
        });

        countField.setOnAction(e -> nextButton.fire());

        VBox root = new VBox(15, title, subtitle, countField, nextButton, errorLabel);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #E6F5E9;");

        Scene scene = new Scene(root, 480, 350);
        stage.setScene(scene);
        stage.show();
    }

    // ---------------- SCREEN 2: Input marks for each subject ----------------
    private void showMarksInputScreen(int count) {

        subjectNameFields.clear();
        marksFields.clear();

        Label title = new Label("Enter Marks (out of 100)");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.CENTER);

        Label colHeader1 = new Label("Subject Name");
        colHeader1.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        Label colHeader2 = new Label("Marks (0-100)");
        colHeader2.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        grid.add(colHeader1, 0, 0);
        grid.add(colHeader2, 1, 0);

        for (int i = 0; i < count; i++) {
            TextField nameField = new TextField("Subject " + (i + 1));
            nameField.setPrefWidth(180);

            TextField marksField = new TextField();
            marksField.setPrefWidth(100);
            marksField.setPromptText("0-100");
            marksField.setAlignment(Pos.CENTER);

            subjectNameFields.add(nameField);
            marksFields.add(marksField);

            grid.add(nameField, 0, i + 1);
            grid.add(marksField, 1, i + 1);
        }

        errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        errorLabel.setWrapText(true);

        Button calculateButton = new Button("Calculate Grade");
        calculateButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        calculateButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        calculateButton.setOnAction(e -> calculateAndShowResult());

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> showSubjectCountScreen());

        HBox buttonRow = new HBox(10, backButton, calculateButton);
        buttonRow.setAlignment(Pos.CENTER);

        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(300);
        scrollPane.setStyle("-fx-background: #E6F5E9; -fx-background-color: transparent;");

        VBox root = new VBox(15, title, scrollPane, errorLabel, buttonRow);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: #E6F5E9;");

        Scene scene = new Scene(root, 500, 550);
        stage.setScene(scene);
        stage.show();
    }

    // ---------------- Validate, Calculate, and show result ----------------
    private void calculateAndShowResult() {

        List<String> names = new ArrayList<>();
        List<Integer> marksList = new ArrayList<>();

        // Reset field colors first
        for (TextField f : marksFields) {
            f.setStyle("-fx-alignment: center;");
        }

        for (int i = 0; i < marksFields.size(); i++) {
            String marksText = marksFields.get(i).getText().trim();
            String name = subjectNameFields.get(i).getText().trim();
            if (name.isEmpty()) {
                name = "Subject " + (i + 1);
            }

            int marks;
            try {
                marks = Integer.parseInt(marksText);
            } catch (NumberFormatException ex) {
                errorLabel.setText("Invalid marks for \"" + name + "\". Please enter numbers only.");
                marksFields.get(i).setStyle("-fx-alignment: center; -fx-background-color: #ffcccc;");
                return;
            }

            if (marks < 0 || marks > 100) {
                errorLabel.setText("Marks for \"" + name + "\" must be between 0 and 100.");
                marksFields.get(i).setStyle("-fx-alignment: center; -fx-background-color: #ffcccc;");
                return;
            }

            names.add(name);
            marksList.add(marks);
        }

        errorLabel.setText("");

        int totalMarks = 0;
        for (int m : marksList) {
            totalMarks += m;
        }

        int maxPossible = marksList.size() * 100;
        double percentage = (double) totalMarks / maxPossible * 100;

        String grade;
        Color gradeColor;

        if (percentage >= 90) {
            grade = "A";
            gradeColor = Color.web("#2e7d32"); // Green
        } else if (percentage >= 80) {
            grade = "B";
            gradeColor = Color.web("#1565c0"); // Blue
        } else if (percentage >= 70) {
            grade = "C";
            gradeColor = Color.web("#ef6c00"); // Orange
        } else if (percentage >= 60) {
            grade = "D";
            gradeColor = Color.web("#d84315"); // Dark orange/red
        } else {
            grade = "F";
            gradeColor = Color.web("#c62828"); // Red
        }

        showResultScreen(names, marksList, totalMarks, maxPossible, percentage, grade, gradeColor);
    }

    // ---------------- SCREEN 3: Result Screen ----------------
    private void showResultScreen(List<String> names, List<Integer> marksList, int totalMarks,
                                   int maxPossible, double percentage, String grade, Color gradeColor) {

        Label title = new Label("Result Summary");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        // Grade Badge
        Label gradeBadge = new Label(grade);
        gradeBadge.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        gradeBadge.setTextFill(Color.WHITE);
        gradeBadge.setStyle("-fx-background-color: " + toRgbCode(gradeColor) + "; -fx-background-radius: 50; -fx-padding: 10 25 10 25;");

        Label totalLabel = new Label("Total Marks: " + totalMarks + " / " + maxPossible);
        totalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));

        Label percentLabel = new Label(String.format("Percentage: %.2f%%", percentage));
        percentLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));

        ProgressBar progressBar = new ProgressBar(percentage / 100);
        progressBar.setPrefWidth(300);
        progressBar.setStyle("-fx-accent: " + toRgbCode(gradeColor) + ";");

        String status = percentage >= 40 ? "Overall Status: PASS ✅" : "Overall Status: FAIL ❌";
        Label statusLabel = new Label(status);
        statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        statusLabel.setTextFill(percentage >= 40 ? Color.GREEN : Color.RED);

        // Subject-wise breakdown table
        GridPane breakdown = new GridPane();
        breakdown.setHgap(15);
        breakdown.setVgap(6);
        breakdown.setAlignment(Pos.CENTER);
        breakdown.setPadding(new Insets(10));

        Label h1 = new Label("Subject");
        h1.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        Label h2 = new Label("Marks");
        h2.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        Label h3 = new Label("Status");
        h3.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        breakdown.add(h1, 0, 0);
        breakdown.add(h2, 1, 0);
        breakdown.add(h3, 2, 0);

        for (int i = 0; i < names.size(); i++) {
            Label nameLbl = new Label(names.get(i));
            Label marksLbl = new Label(marksList.get(i) + " / 100");
            boolean pass = marksList.get(i) >= 40;
            Label statusLbl = new Label(pass ? "Pass ✅" : "Fail ❌");
            statusLbl.setTextFill(pass ? Color.GREEN : Color.RED);

            breakdown.add(nameLbl, 0, i + 1);
            breakdown.add(marksLbl, 1, i + 1);
            breakdown.add(statusLbl, 2, i + 1);
        }

        ScrollPane scrollPane = new ScrollPane(breakdown);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(180);
        scrollPane.setStyle("-fx-background: #E6F5E9; -fx-background-color: transparent;");

        Button restartButton = new Button("Start New Calculation");
        restartButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        restartButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        restartButton.setOnAction(e -> showSubjectCountScreen());

        VBox root = new VBox(12, title, gradeBadge, totalLabel, percentLabel, progressBar,
                statusLabel, scrollPane, restartButton);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: #E6F5E9;");

        Scene scene = new Scene(root, 480, 620);
        stage.setScene(scene);
        stage.show();
    }

    // Helper: Convert javafx Color object to a CSS-compatible rgb string
    private String toRgbCode(Color color) {
        return String.format("rgb(%d,%d,%d)",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    public static void main(String[] args) {
        launch(args);
    }
}