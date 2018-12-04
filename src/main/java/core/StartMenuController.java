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
import javafx.scene.control.CheckBox;
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
    private CheckBox enableRiggingCheckBox;
    @FXML
    private CheckBox showPlayerHandsCheckBox;

    @FXML
    private Button loadFileButton;

    @FXML
    private TextField stockField;
    @FXML
    private TextField deciderStockField;
    @FXML
    private TextField p1HandField;
    @FXML
    private TextField p2HandField;
    @FXML
    private TextField p3HandField;
    @FXML
    private TextField p4HandField;

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
                    this.p3NameField.setDisable(true);
                    this.p3StrategyChoice.setDisable(true);
                    this.p4NameField.setDisable(true);
                    this.p4StrategyChoice.setDisable(true);

                    if (this.enableRiggingCheckBox.isSelected()) {
                        this.enableRiggingFields(true);
                    }

                    break;
                case 3:
                    this.p3NameField.setDisable(false);
                    this.p3StrategyChoice.setDisable(false);
                    this.p4NameField.setDisable(true);
                    this.p4StrategyChoice.setDisable(true);

                    if (this.enableRiggingCheckBox.isSelected()) {
                        this.enableRiggingFields(true);
                    }

                    break;
                case 4:
                    this.p3NameField.setDisable(false);
                    this.p3StrategyChoice.setDisable(false);
                    this.p4NameField.setDisable(false);
                    this.p4StrategyChoice.setDisable(false);

                    if (this.enableRiggingCheckBox.isSelected()) {
                        this.enableRiggingFields(true);
                    }

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

        // Rigging fields
        this.enableRiggingFields(false);
        this.enableRiggingCheckBox.selectedProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
                if (newValue) {
                    this.enableRiggingFields(true);
                } else {
                    this.enableRiggingFields(false);
                }
			});
    }

    private void enableRiggingFields(boolean setting) {
        setting = !setting;
        this.showPlayerHandsCheckBox.setDisable(setting);
        this.loadFileButton.setDisable(setting);
        this.stockField.setDisable(setting);
        this.deciderStockField.setDisable(setting);
        this.p1HandField.setDisable(setting);
        this.p2HandField.setDisable(setting);
        if (this.numPlayersChoice.getValue() >= 3) {
            this.p3HandField.setDisable(setting);
        } else {
            this.p3HandField.setDisable(true);
        }
        if (this.numPlayersChoice.getValue() >= 4) {
            this.p4HandField.setDisable(setting);
        } else {
            this.p4HandField.setDisable(true);
        }
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
