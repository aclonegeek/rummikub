package core;

import java.io.IOException;
import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class StartMenuController {
    @FXML
    private TextField p1NameField;
    @FXML
    private TextField p2NameField;
    @FXML
    private TextField p3NameField;
    @FXML
    private TextField p4NameField;

    @FXML
    private ChoiceBox<Integer> numPlayersChoice;
    @FXML
    private ChoiceBox<String> p1StrategyChoice;
    @FXML
    private ChoiceBox<String> p2StrategyChoice;
    @FXML
    private ChoiceBox<String> p3StrategyChoice;
    @FXML
    private ChoiceBox<String> p4StrategyChoice;

    @FXML
    private Button playButton;

    @FXML
    private void handlePlayButtonPressed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/core/GameScreen.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene((Pane)loader.load()));
        stage.show();

        GameController gameController = loader.getController();
        gameController.setNumPlayers(this.numPlayersChoice.getValue());
        gameController.setPlayerNames(this.getPlayerNames());
        gameController.setPlayerStrategies(this.getPlayerStrategies());

        gameController.play();
    }

    @FXML
    private void initialize() {
        ObservableList<Integer> numPlayers = FXCollections.observableArrayList(2, 3, 4);
        this.numPlayersChoice.setItems(numPlayers);
        this.numPlayersChoice.getSelectionModel().selectFirst();
        this.numPlayersChoice.valueProperty().addListener((ChangeListener<Integer>) (observable, oldValue, newValue) -> {
                switch (newValue) {
                case 2:
                    p3NameField.setDisable(true);
                    p3StrategyChoice.setDisable(true);
                    p4NameField.setDisable(true);
                    p4StrategyChoice.setDisable(true);
                    break;
                case 3:
                    p3NameField.setDisable(false);
                    p3StrategyChoice.setDisable(false);
                    p4NameField.setDisable(true);
                    p4StrategyChoice.setDisable(true);
                    break;
                case 4:
                    p3NameField.setDisable(false);
                    p3StrategyChoice.setDisable(false);
                    p4NameField.setDisable(false);
                    p4StrategyChoice.setDisable(false);
                    break;
                }
            });

        ObservableList<String> strategies = FXCollections.observableArrayList("StrategyHuman", "Strategy1", "Strategy2", "Strategy3", "Strategy4");
        this.p1StrategyChoice.setItems(strategies);
        this.p2StrategyChoice.setItems(strategies);
        this.p3StrategyChoice.setItems(strategies);
        this.p4StrategyChoice.setItems(strategies);

        this.p3NameField.setDisable(true);
        this.p3StrategyChoice.setDisable(true);
        this.p4NameField.setDisable(true);
        this.p4StrategyChoice.setDisable(true);
    }

    private ArrayList<String> getPlayerNames() {
        ArrayList<String> playerNames = new ArrayList<>();

        if (this.numPlayersChoice.getValue() >= 2) {
            playerNames.add(p1NameField.getText());
            playerNames.add(p2NameField.getText());
        }
        if (this.numPlayersChoice.getValue() >= 3) {
            playerNames.add(p3NameField.getText());
        }
        if (this.numPlayersChoice.getValue() >= 4) {
            playerNames.add(p4NameField.getText());
        }

        return playerNames;
    }

    private ArrayList<String> getPlayerStrategies() {
        ArrayList<String> playerStrategies = new ArrayList<>();

        if (this.numPlayersChoice.getValue() >= 2) {
            playerStrategies.add(p1StrategyChoice.getValue());
            playerStrategies.add(p2StrategyChoice.getValue());
        }
        if (this.numPlayersChoice.getValue() >= 3) {
            playerStrategies.add(p3StrategyChoice.getValue());
        }
        if (this.numPlayersChoice.getValue() >= 4) {
            playerStrategies.add(p4StrategyChoice.getValue());
        }

        return playerStrategies;
    }
}
