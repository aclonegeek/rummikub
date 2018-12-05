package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
import javafx.stage.FileChooser;
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
        if (this.enableRiggingCheckBox.isSelected()) {
            gameController.setRiggedAttributes(this.getRiggedStock(this.stockField.getText()),
                                               this.getRiggedStock(this.deciderStockField.getText()),
                                               this.getRiggedHands());
        }

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

        // Rigging fields
        this.enableRiggingFields(false);
        this.enableRiggingCheckBox.selectedProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
                if (newValue) {
                    this.enableRiggingFields(true);
                } else {
                    this.enableRiggingFields(false);
                }
			});

        this.loadFileButton.setOnAction(event -> {
                FileChooser fc = new FileChooser();
                fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
                File file = fc.showOpenDialog(null);
                if (file == null) {
                    return;
                }

                ArrayList<String> lines = new ArrayList<String>();

                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        lines.add(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                this.parseFile(lines);
            });
    }

    private void parseFile(ArrayList<String> lines) {
        int numPlayers = Integer.parseInt(lines.get(0).split(":")[1]);
        String stockLine = lines.get(1).split(":")[1];
        String deciderStockLine = lines.get(2).split(":")[1];
        String[] p1 = lines.get(3).split(":");
        String[] p2 = lines.get(4).split(":");
        String[] p3 = lines.get(5).split(":");
        String[] p4 = lines.get(6).split(":");

        this.numPlayersChoice.setValue(numPlayers);

        this.stockField.setText(stockLine);
        this.deciderStockField.setText(deciderStockLine);

        this.p1NameField.setText(p1[1]);
        this.p1StrategyChoice.getSelectionModel().select(p1[2]);
        this.p1HandField.setText(p1[3]);

        this.p2NameField.setText(p2[1]);
        this.p2StrategyChoice.getSelectionModel().select(p2[2]);
        this.p2HandField.setText(p2[3]);

        if (numPlayers >= 3) {
            this.p3NameField.setText(p3[1]);
            this.p3StrategyChoice.getSelectionModel().select(p3[2]);
            this.p3HandField.setText(p3[3]);
        }
        if (numPlayers >= 4) {
            this.p4NameField.setText(p4[1]);
            this.p4StrategyChoice.getSelectionModel().select(p4[2]);
            this.p4HandField.setText(p4[3]);
        }
    }

    private void enableRiggingFields(boolean setting) {
        setting = !setting;
        this.showPlayerHandsCheckBox.setDisable(setting);
        this.showPlayerHandsCheckBox.setSelected(!setting);
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

        playerNames.add(p1NameField.getText());
        playerNames.add(p2NameField.getText());
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

        playerStrategies.add(p1StrategyChoice.getValue());
        playerStrategies.add(p2StrategyChoice.getValue());
        if (this.numPlayersChoice.getValue() >= 3) {
            playerStrategies.add(p3StrategyChoice.getValue());
        }
        if (this.numPlayersChoice.getValue() >= 4) {
            playerStrategies.add(p4StrategyChoice.getValue());
        }

        return playerStrategies;
    }

    private Stock getRiggedStock(String text) {
        Stock stock = new Stock();

        for (String tile : text.split(",")) {
            Tile t = new Tile(tile);
            stock.add(t);
        }

        return stock;
    }

    private ArrayList<Hand> getRiggedHands() {
        ArrayList<Hand> hands = new ArrayList<Hand>();

        Hand p1Hand = new Hand(this.p1HandField.getText());
        Hand p2Hand = new Hand(this.p2HandField.getText());
        hands.add(p1Hand);
        hands.add(p2Hand);

        if (this.numPlayersChoice.getValue() >= 3) {
            Hand p3Hand = new Hand(this.p3HandField.getText());
            hands.add(p3Hand);
        }
        if (this.numPlayersChoice.getValue() >= 4) {
            Hand p4Hand = new Hand(this.p4HandField.getText());
            hands.add(p4Hand);
        }

        return hands;
    }
}
