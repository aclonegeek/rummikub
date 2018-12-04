package core;

import java.util.ArrayList;
import java.util.Arrays;

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
    private ArrayList<Meld> workspace;
    // TODO: Move to the model?
    private Player currentPlayer;
    private Player winner;

    private ObservableList<Meld> workspaceList = FXCollections.observableArrayList();

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

        // TODO: Support only if player is a Human. And allow for multiple Humans!
        this.p1HandListView.setOnDragDetected(event -> {
                System.out.println("[HUMAN] Drag detected!");

                this.dragSource = DragSource.PLAYER;
                Dragboard db = this.p1HandListView.startDragAndDrop(TransferMode.COPY);
                ClipboardContent cc = new ClipboardContent();
                System.out.println(this.p1HandListView.getSelectionModel().getSelectedItem().toString());
                cc.putString(this.p1HandListView.getSelectionModel().getSelectedItem().toString());
                db.setContent(cc);
                event.consume();
            });
    }

    private void updateWorkspaceList() {
        this.workspaceList.clear();
        for (Meld meld : this.workspace) {
            this.workspaceList.add(meld);
        }
    }

    private void initializeTable() {
        // This is needed to display the Table on start if the Human is playing first.
        this.workspaceList.add(new Meld());
        this.tableListView.setItems(this.workspaceList);
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
                    if (dragDestination == DragDestination.NEW_MELD && db.hasString()) {
                        if (dragSource == DragSource.PLAYER) {
                            System.out.println("[MELDLIST] Dropped FROM Player TO New Meld! " + db.getString());

                            Tile tile = currentPlayer.getHand().remove(new Tile(db.getString()));
                            workspace.add(new Meld(tile.toString()));
                            currentPlayer.updateHandList();
                            updateWorkspaceList();
                        } else if (dragSource == DragSource.MELD) {
                            System.out.println("[MELDLIST] Dropped FROM Existing Meld to New Meld!");

                            String[] indices = db.getString().split(",");
                            int meldIndex = Integer.parseInt(indices[0]);
                            int tileToRemoveIndex = Integer.parseInt(indices[1]);
                            Meld meld = workspace.get(meldIndex);

                            Tile removedTile = meld.removeTile(tileToRemoveIndex);
                            if (removedTile != null) {
                                // Only split the meld if it's not the first or last tile in the meld
                                if (tileToRemoveIndex > 0 && tileToRemoveIndex < meld.getSize() - 1) {
                                    Meld secondHalf = meld.splitMeld(tileToRemoveIndex - 1);
                                    workspace.add(secondHalf);
                                }

                                Meld newMeld = new Meld(removedTile.toString());
                                workspace.add(newMeld);
                                updateWorkspaceList();
                            }
                        }
                        // The player should no longer be able to draw after playing
                        drawButton.setDisable(true);
                        finishButton.setDisable(false);
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

                // Determine which Meld it belongs to
                String currentMeld = this.tilesList.toString().replace("[", "{").replace("]", "}").replace(",", "");
                int meldIndex = 0;
                for (Meld meld : workspace) {
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

                        String currentMeld = this.tilesList.toString().replace("[", "{").replace("]", "}").replace(",", "");
                        int index = 0;
                        for (Meld meld : workspace) {
                            if (currentMeld.equals(meld.toString())) {
                                break;
                            }
                            index++;
                        }

                        Tile tile = currentPlayer.getHand().remove(new Tile(db.getString()));
                        workspace.get(index).addTile(tile);
                        currentPlayer.updateHandList();
                        updateWorkspaceList();

                        drawButton.setDisable(true);
                        finishButton.setDisable(false);
                    } else if (dragSource == DragSource.MELD) {
                        System.out.println("[TABLE TILE] Dropped FROM Existing Meld TO Existing Meld!");

                        String[] indices = db.getString().split(",");
                        int meldIndex = Integer.parseInt(indices[0]);
                        int tileToRemoveIndex = Integer.parseInt(indices[1]);
                        Meld meld = workspace.get(meldIndex);

                        Tile removedTile = meld.removeTile(tileToRemoveIndex);
                        if (removedTile != null) {
                            if (tileToRemoveIndex > 0 && tileToRemoveIndex < meld.getSize() - 1) {
                                Meld secondHalf = meld.splitMeld(tileToRemoveIndex - 1);
                                workspace.add(secondHalf);
                            }

                            String meldToAddToStr = this.tiles.getItems().toString()
                                .replace("[", "{")
                                .replace("]", "}")
                                .replace(",", "");

                            int index = 0;
                            for (Meld m : tableListView.getItems()) {
                                if (meldToAddToStr.equals(m.toString())) {
                                    break;
                                }
                                index++;
                            }

                            Meld meldToAddTo = tableListView.getItems().get(index);
                            Tile tile = meldToAddTo.addTile(removedTile);
                            if (tile != null && tile.isJoker()) {
                                if (tile.isJoker()) {
                                    workspace.add(new Meld(tile.toString()));
                                }
                                updateWorkspaceList();

                                // The player should no longer be able to draw after playing
                                drawButton.setDisable(true);
                                finishButton.setDisable(false);
                            }
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

        this.makeWorkspaceCopy();
        this.currentPlayer = this.model.getPlayers().get(0);

        this.initializeTable();

        if (!this.currentPlayer.getPlayerType().equals("StrategyHuman")) {
            this.nextAIMoveButton.setDisable(false);
            this.drawButton.setDisable(true);
            this.finishButton.setDisable(true);
        } else {
            this.nextAIMoveButton.setDisable(true);
            this.model.createMemento(currentPlayer);
        }

        this.setupPlayerButtons();
    }

    private void setupPlayerButtons() {
        this.drawButton.setOnAction(event -> {
                if (this.model.getStock().getSize() == 0) {
                    System.out.println("[GAME] Stock is empty!");
                } else {
                    this.currentPlayer.add(this.model.getStock().draw());
                    this.currentPlayer.updateHandList();
                }
                this.currentPlayer.drawing = true;
                this.currentPlayer.notifyObservers();
                this.currentPlayer.drawing = false;

                this.setNextCurrentPlayer();
                if (!this.currentPlayer.getPlayerType().equals("StrategyHuman")) {
                    this.drawButton.setDisable(true);
                    this.finishButton.setDisable(true);
                    this.nextAIMoveButton.setDisable(false);
                }
            });

        this.finishButton.setOnAction(event -> {
                this.model.getTable().setState(this.workspace);
                if (!this.model.determineValidState()) {
                    this.model.restoreMementoWithPenalty(currentPlayer);
                }
                this.makeWorkspaceCopy();
                this.updateWorkspaceList();
                this.currentPlayer.updateHandList();
                this.currentPlayer.notifyObservers();

                this.setNextCurrentPlayer();
                if (!this.currentPlayer.getPlayerType().equals("StrategyHuman")) {
                    this.drawButton.setDisable(true);
                    this.finishButton.setDisable(true);
                    this.nextAIMoveButton.setDisable(false);
                }
            });

        this.nextAIMoveButton.setOnAction(event -> {
                this.nextPlayer();
            });
    }

    // Plays an AI if it's the next player, otherwise sets up Human stuff
    private void nextPlayer() {
        if (!this.currentPlayer.getPlayerType().equals("StrategyHuman")) {
            this.workspace = this.currentPlayer.play(this.model.getTable().getState());
            if (workspace == null) {
                this.currentPlayer.add(this.model.getStock().draw());
            } else {
                this.model.getTable().setState(workspace);
                this.updateWorkspaceList();
            }
            this.currentPlayer.updateHandList();

            // Check for winning conditions
            // 1. Player has a hand size of 0, OR
            // 2. The stock is empty, so the player with the smallest hand wins
            // 3. TODO: Timer
            if (this.currentPlayer.getHandSize() == 0) {
                this.winner = this.currentPlayer;
            } else if (this.model.getStock().getSize() == 0) {
                this.determineWinner();
            }

            this.setNextCurrentPlayer();
            if (this.currentPlayer.getPlayerType().equals("StrategyHuman")) {
                this.makeWorkspaceCopy();
                this.model.createMemento(currentPlayer);
                this.nextAIMoveButton.setDisable(true);
                this.drawButton.setDisable(false);
                this.finishButton.setDisable(true);
            }
        } else {
            this.makeWorkspaceCopy();
            this.model.createMemento(currentPlayer);
        }
    }

    private void setNextCurrentPlayer() {
        int index = this.model.getPlayers().indexOf(this.currentPlayer);
        if (index >= (this.model.getNumPlayers() - 1)) {
            this.currentPlayer = this.model.getPlayers().get(0);
        } else {
            this.currentPlayer = this.model.getPlayers().get(index + 1);
        }
    }

    private void determineWinner() {
        this.winner = null;
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

    private void makeWorkspaceCopy() {
        this.workspace = new ArrayList<Meld>();
        for (Meld meld : this.model.getTable().getState()) {
            Meld newMeld = new Meld(meld);
            this.workspace.add(newMeld);
        }
    }
}
