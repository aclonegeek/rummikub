package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.util.Callback;

public class GameController {
    private enum DragSource {
        NONE,
        PLAYER,
        MELD,
    }

    private enum DragDestination {
        NONE,
        NEW_MELD,
        EXISTING_MELD,
    }

    private DragSource dragSource;
    private DragDestination dragDestination;

    private GameModel model;

    @FXML
    private Label p1NameLabel;
    @FXML
    private Label p2NameLabel;
    @FXML
    private Label p3NameLabel;
    @FXML
    private Label p4NameLabel;

    @FXML
    private Label p1StrategyLabel;
    @FXML
    private Label p2StrategyLabel;
    @FXML
    private Label p3StrategyLabel;
    @FXML
    private Label p4StrategyLabel;

    @FXML
    private Label p1HandCountLabel;
    @FXML
    private Label p2HandCountLabel;
    @FXML
    private Label p3HandCountLabel;
    @FXML
    private Label p4HandCountLabel;

    @FXML
    private ListView<Meld> tableListView;
    @FXML
    private ListView<Tile> p1HandListView;
    @FXML
    private ListView<Tile> p2HandListView;
    @FXML
    private ListView<Tile> p3HandListView;
    @FXML
    private ListView<Tile> p4HandListView;

    @FXML
    private Button drawButton;
    @FXML
    private Button finishButton;
    @FXML
    private Button nextAIMoveButton;

    @FXML
    private void initialize() {
        this.model = new GameModel();

        this.tableListView.setCellFactory(new Callback<ListView<Meld>, ListCell<Meld>>() {
            @Override
            public ListCell<Meld> call(ListView<Meld> param) {
                return new MeldListCell();
            }
            });
    }

    private void initializePlayerHands() {
        this.p1HandListView.setItems(this.model.getPlayers().get(0).getHandList());
        this.p2HandListView.setItems(this.model.getPlayers().get(1).getHandList());
        if (this.model.getNumPlayers() >= 3) {
            this.p3HandListView.setItems(this.model.getPlayers().get(2).getHandList());
        }
        if (this.model.getNumPlayers() >= 4) {
            this.p4HandListView.setItems(this.model.getPlayers().get(3).getHandList());
        }

        // Handle dragging from hand for all Human players
        List<ListView<Tile>> handListViews = Arrays.asList(p1HandListView,
                                                           p2HandListView,
                                                           p3HandListView,
                                                           p4HandListView);
        int index = 0;
        for (ListView<Tile> listView : handListViews) {
            if (this.model.getPlayers().get(index).getPlayerType().equals("StrategyHuman")) {
                listView.setOnDragDetected(event -> {
                        this.handleDragFromHand(event, listView);
                    });
            }
            if (++index >= this.model.getNumPlayers()) {
                break;
            }
        }
    }

    private void handleDragFromHand(MouseEvent event, ListView<Tile> listView) {
        System.out.println("[HUMAN] Drag detected!");

        this.dragSource = DragSource.PLAYER;
        Dragboard db = listView.startDragAndDrop(TransferMode.COPY);
        ClipboardContent cc = new ClipboardContent();
        cc.putString(listView.getSelectionModel().getSelectedItem().toString());
        db.setContent(cc);
        event.consume();
    }

    private void initializeTable() {
        // This is needed to display the Table on start if the Human is playing first.
        this.model.getWorkspaceList().add(new Meld());
        this.tableListView.setItems(this.model.getWorkspaceList());
    }

    public class MeldListCell extends ListCell<Meld> {
        private ListView<Tile> tiles;
        private ObservableList<Tile> tilesList;

        public MeldListCell() {
            super();
            this.tiles = new ListView<Tile>();
            this.tiles.setOrientation(Orientation.HORIZONTAL);
            this.tiles.setPrefHeight(30);
            this.tilesList = FXCollections.observableArrayList();
            this.tiles.setItems(tilesList);

            this.setOnDragOver(event -> {
                    System.out.println("[MELDLIST] Drag over!");

                    dragDestination = DragDestination.NEW_MELD;
                    Dragboard db = event.getDragboard();
                    if (db.hasString()) {
                        event.acceptTransferModes(TransferMode.COPY);
                    }
                    event.consume();
                });

            // Playing a new meld from hand or an existing meld
            this.setOnDragDropped(event -> {
                    System.out.println("[MELDLIST] Dropping!");

                    Dragboard db = event.getDragboard();
                    boolean success = false;
                    if (dragDestination == DragDestination.NEW_MELD && db.hasString()) {
                        if (dragSource == DragSource.PLAYER) {
                            System.out.println("[MELDLIST] Dropped FROM Player TO New Meld! " + db.getString());
                            model.playNewMeldFromHand(db.getString());
                            success = true;
                        } else if (dragSource == DragSource.MELD) {
                            System.out.println("[MELDLIST] Dropped FROM Existing Meld to New Meld!");
                            success = model.playNewMeldFromExistingMeld(db.getString());
                        }
                        // The player should no longer be able to draw after playing
                        if (success) {
                            drawButton.setDisable(true);
                            finishButton.setDisable(false);
                        }
                    }

                    dragSource = DragSource.NONE;
                    dragDestination = DragDestination.NONE;
                });

        // Dragging a tile from an existing meld
        this.tiles.setOnDragDetected(event -> {
                System.out.println("[TABLE TILE] Drag FROM Existing Meld detected!");

                dragSource = DragSource.MELD;
                Dragboard db = this.tiles.startDragAndDrop(TransferMode.COPY);
                ClipboardContent cc = new ClipboardContent();
                int tileToMoveIndex = this.tiles.getSelectionModel().getSelectedIndex();

                // Determine which Meld the tile the current player is selecting belongs to
                String currentMeld = this.tilesList.toString()
                    .replace("[", "{")
                    .replace("]", "}")
                    .replace(",", "");
                int meldIndex = 0;
                for (Meld meld : model.getWorkspace()) {
                    if (currentMeld.equals(meld.toString())) {
                        break;
                    }
                    meldIndex++;
                }

                cc.putString(meldIndex + "," + tileToMoveIndex);
                db.setContent(cc);
                event.consume();
            });

        // Dragging over a tile in an existing meld on the table
        this.tiles.setOnDragOver(event -> {
                dragDestination = DragDestination.EXISTING_MELD;

                System.out.println("[TABLE TILE] Drag over!");
                Dragboard db = event.getDragboard();
                if (db.hasString()) {
                    event.acceptTransferModes(TransferMode.COPY);
                }
                event.consume();
            });

        // Adding a tile from the player's hand to an existing meld
        this.tiles.setOnDragDropped(event -> {
                System.out.println("[TABLE TILE] Dropping!");

                Dragboard db = event.getDragboard();
                if (dragDestination == DragDestination.EXISTING_MELD && db.hasString()) {
                    if (dragSource == DragSource.PLAYER) {
                        System.out.println("[TABLE TILE] Dropped From Player TO Existing Meld!");

                        String meldToAddTo = this.tilesList.toString()
                            .replace("[", "{")
                            .replace("]", "}")
                            .replace(",", "");
                        model.playTileFromHandToExistingMeld(db.getString(), meldToAddTo);
                        drawButton.setDisable(true);
                        finishButton.setDisable(false);
                    } else if (dragSource == DragSource.MELD) {
                        System.out.println("[TABLE TILE] Dropped FROM Existing Meld TO Existing Meld!");

                        String meldToAddToStr = this.tiles.getItems().toString()
                            .replace("[", "{")
                            .replace("]", "}")
                            .replace(",", "");
                        if (model.playTileFromMeldToExistingMeld(db.getString(), meldToAddToStr)) {
                            // The player should no longer be able to draw after playing
                            drawButton.setDisable(true);
                            finishButton.setDisable(false);
                        } else {
                            drawButton.setDisable(false);
                            finishButton.setDisable(true);
                        }
                    }
                }

                dragSource = DragSource.NONE;
                dragDestination = DragDestination.NONE;
            });
        }

        @Override
        protected void updateItem(Meld meld, boolean empty) {
            super.updateItem(meld, empty);
            if (meld == null && empty) {
                this.setGraphic(null);
            } else {
                this.tiles.getItems().clear();
                this.tilesList.clear();
                for (int i = 0; i < meld.getSize(); i++) {
                    tilesList.add(meld.getTile(i));
                }
                this.setGraphic(this.tiles);
            }
        }
    }

    public void play() {
        this.model.setPlayerData(this.getPlayerNames(), this.getPlayerStrategies());
        // Set up the game data (stock, table, players, etc.)
        this.model.setup();
        // Bind player hands view with model
        this.initializePlayerHands();
        this.model.determinePlayerOrder();
        this.model.initialDraw();

        this.model.makeWorkspaceCopy();
        this.model.setCurrentPlayer(this.model.getPlayers().get(0));

        this.initializeTable();

        if (!this.model.getCurrentPlayer().getPlayerType().equals("StrategyHuman")) {
            this.nextAIMoveButton.setDisable(false);
            this.drawButton.setDisable(true);
            this.finishButton.setDisable(true);
        } else {
            this.nextAIMoveButton.setDisable(true);
            this.model.createMemento();
        }

        this.setupPlayerButtons();
    }

    private void setupPlayerButtons() {
        this.drawButton.setOnAction(event -> {
                if (this.model.getStock().getSize() == 0) {
                    System.out.println("[GAME] Stock is empty!");
                } else {
                    this.model.getCurrentPlayer().add(this.model.getStock().draw());
                    this.model.getCurrentPlayer().updateHandList();
                }
                this.model.getCurrentPlayer().drawing = true;
                this.model.getCurrentPlayer().notifyObservers();
                this.model.getCurrentPlayer().drawing = false;

                this.model.setCurrentPlayer(null);
                if (!this.model.getCurrentPlayer().getPlayerType().equals("StrategyHuman")) {
                    this.drawButton.setDisable(true);
                    this.finishButton.setDisable(true);
                    this.nextAIMoveButton.setDisable(false);
                } else {
                    this.drawButton.setDisable(false);
                    this.finishButton.setDisable(true);
                    this.nextAIMoveButton.setDisable(true);
                }
            });

        this.finishButton.setOnAction(event -> {
                this.model.getTable().setState(this.model.getWorkspace());
                if (!this.model.determineValidState()) {
                    this.model.restoreMementoWithPenalty(this.model.getCurrentPlayer());
                }
                this.model.makeWorkspaceCopy();
                this.model.updateWorkspaceList();
                this.model.getCurrentPlayer().updateHandList();
                this.model.getCurrentPlayer().notifyObservers();

                this.model.setCurrentPlayer(null);
                if (!this.model.getCurrentPlayer().getPlayerType().equals("StrategyHuman")) {
                    this.drawButton.setDisable(true);
                    this.finishButton.setDisable(true);
                    this.nextAIMoveButton.setDisable(false);
                } else {
                    this.drawButton.setDisable(false);
                    this.finishButton.setDisable(true);
                    this.nextAIMoveButton.setDisable(true);
                }
            });

        this.nextAIMoveButton.setOnAction(event -> {
                this.nextPlayer();
            });
    }

    // Plays an AI if it's the next player, otherwise sets up Human stuff
    private void nextPlayer() {
        if (!this.model.getCurrentPlayer().getPlayerType().equals("StrategyHuman")) {
            this.model.setWorkspace(this.model.getCurrentPlayer().play(this.model.getTable().getState()));
            if (this.model.getWorkspace() == null) {
                this.model.getCurrentPlayer().add(this.model.getStock().draw());
            } else {
                this.model.getTable().setState(this.model.getWorkspace());
                this.model.updateWorkspaceList();
            }
            this.model.getCurrentPlayer().updateHandList();

            // Check for winning conditions
            // 1. Player has a hand size of 0, OR
            // 2. The stock is empty, so the player with the smallest hand wins
            // 3. TODO: Timer
            if (this.model.getCurrentPlayer().getHandSize() == 0) {
                this.model.setWinner(this.model.getCurrentPlayer());
            } else if (this.model.getStock().getSize() == 0) {
                this.determineWinner();
            }

            this.model.setCurrentPlayer(null);
            if (this.model.getCurrentPlayer().getPlayerType().equals("StrategyHuman")) {
                // TODO Move to function?
                this.model.makeWorkspaceCopy();
                this.model.createMemento();
                this.nextAIMoveButton.setDisable(true);
                this.drawButton.setDisable(false);
                this.finishButton.setDisable(true);
            }
        } else {
            this.model.makeWorkspaceCopy();
            this.model.createMemento();
            this.nextAIMoveButton.setDisable(true);
            this.drawButton.setDisable(false);
            this.finishButton.setDisable(true);
        }
    }

    private void determineWinner() {

    }

    public void setNumPlayers(int numPlayers) {
        this.model.setNumPlayers(numPlayers);
    }

    public void setRiggedAttributes(Stock riggedStock, Stock riggedDeciderStock, ArrayList<Hand> riggedHands) {
        this.model.setRiggedData(riggedStock, riggedDeciderStock, riggedHands);
    }

    public ArrayList<String> getPlayerNames() {
        ArrayList<String> names = new ArrayList<>(Arrays.asList(p1NameLabel.getText(), p2NameLabel.getText(), p3NameLabel.getText(), p4NameLabel.getText()));
        names.removeIf(n -> n.isEmpty());
        return names;
    }

    public void setPlayerNames(ArrayList<String> playerNames) {
        ArrayList<Label> labels = new ArrayList<>(Arrays.asList(p1NameLabel, p2NameLabel, p3NameLabel, p4NameLabel));
        int counter = 0;
        for (String name : playerNames) {
            labels.get(counter).setText(name);
            counter++;
        }
    }

    public ArrayList<String> getPlayerStrategies() {
        ArrayList<String> strategies = new ArrayList<>(Arrays.asList(p1StrategyLabel.getText(), p2StrategyLabel.getText(), p3StrategyLabel.getText(), p4StrategyLabel.getText()));
        strategies.removeIf(s -> s.isEmpty());
        return strategies;
    }

    public void setPlayerStrategies(ArrayList<String> playerStrategies) {
        ArrayList<Label> labels = new ArrayList<>(Arrays.asList(p1StrategyLabel, p2StrategyLabel, p3StrategyLabel, p4StrategyLabel));
        int counter = 0;
        for (String name : playerStrategies) {
            labels.get(counter).setText(name);
            counter++;
        }
    }
}
