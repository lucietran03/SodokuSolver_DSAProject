package DancingLink_AlgorithmsX;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;

public class MenuController {
    @FXML
    Button button1;
    @FXML
    ChoiceBox<String> choiceBox;
    @FXML
    Label label1;
    @FXML
    Label label2;

    private String[] difficultyLevels = {"Easy", "Medium", "Hard", "Very Hard", "Super Hard", "Random (6-12)"};

    @FXML
    public void initialize() {
        choiceBox.getItems().addAll(difficultyLevels);
        choiceBox.setOnAction(event -> handleProvideQuestion());
    }

    @FXML
    public void handleProvideQuestion() {
        String selectedDifficulty = choiceBox.getValue();
        if (selectedDifficulty == null) {
            label1.setText("Please select a difficulty level.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SudokuBoard.fxml"));
            Parent root = loader.load();

            DancingLink_AlgorithmsX.SudokuController sudokuController = loader.getController();

            if ("Random (6-12)".equals(selectedDifficulty)) {
                Random random = new Random();
                int randomIndex = 6 + random.nextInt(7); // Random index between 6 and 12
                sudokuController.loadPuzzleByIndex(randomIndex);
            } else {
                sudokuController.loadPuzzle(selectedDifficulty);
            }

            Stage stage = (Stage) button1.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void handleProvideCustomProblem() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SudokuBoard.fxml"));
            Parent root = loader.load();

            DancingLink_AlgorithmsX.SudokuController sudokuController = loader.getController();
            sudokuController.prepareForCustomInput(); // Prepare the board for user input

            Stage stage = (Stage) button1.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}