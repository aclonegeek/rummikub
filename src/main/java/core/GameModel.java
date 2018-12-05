package core;

import java.util.ArrayList;
import java.util.Iterator;

import core.Globals.PlayerType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;

public class GameModel {
    private Stock stock;
    private ArrayList<Player> players;
    private Table table;
    private ArrayList<Meld> workspace;

    private int numPlayers;
    private ArrayList<Pair<String, String>> playerData;

    private GameMemento memento;

    private Player currentPlayer;
    private Player winner;

    // Rigging related
    private boolean riggedGame = false;
    private Stock riggedStock;
    private Stock riggedDeciderStock;
    private ArrayList<Hand> riggedHands;

    private ObservableList<Meld> workspaceList = FXCollections.observableArrayList();

    public GameModel() {
        this.stock = new Stock();
        this.table = new Table();
        this.players = new ArrayList<>();
        this.playerData = new ArrayList<>();
    }

    public void setup() {
        if (this.riggedGame) {
            this.stock = this.riggedStock;
        } else {
            this.stock.populate();
            this.stock.shuffle();
        }

        // Create the players
        for (Pair<String, String> data : this.playerData) {
            if (data.getValue() == "StrategyHuman") {
                this.players.add(new PlayerHuman(data.getKey()));
            } else if (data.getValue() == "Strategy1") {
                this.players.add(new Player1(data.getKey()));
            } else if (data.getValue() == "Strategy2") {
                this.players.add(new Player2(data.getKey()));
            } else if (data.getValue() == "Strategy3") {
                this.players.add(new Player3(data.getKey()));
            } else {
                this.players.add(new Player4(data.getKey()));
            }
        }

        // Register each player that uses Strategy3 or Strategy4 as observers of the other players
        for (Player player : this.players) {
            for (Player otherPlayer : this.players) {
                if (otherPlayer != player && (otherPlayer.getPlayerType().equals(PlayerType.PLAYER3) || otherPlayer.getPlayerType().equals(PlayerType.PLAYER4))) {
                    player.registerObserver(otherPlayer);
                }
            }
        }
    }

    public void initialDraw() {
        if (this.riggedGame) {
            for (int i = 0; i < this.numPlayers; i++) {
                Player player = this.players.get(i);
                player.setHand(this.riggedHands.get(i));
                player.updateHandList();
            }
        } else {
            for (Player player : this.players) {
                for (int i = 0; i < 14; i++) {
                    player.add(this.stock.draw());
                }
                player.updateHandList();
            }
        }
    }

    public void determinePlayerOrder() {
        Stock tempStock = null;
        if (this.riggedGame) {
            tempStock = this.riggedDeciderStock;
        } else {
            tempStock = new Stock();
            tempStock.populateForDraw();
            tempStock.shuffle();
        }

        // Each player draws tile, keeping track of the player who draws the highest
        Player firstPlayer = this.players.get(0);
        Tile highestTile = new Tile("R0");
        for (Player player : this.players) {
            Tile drawnTile = tempStock.draw();
            while (drawnTile.getValue() == highestTile.getValue()) {
                drawnTile = tempStock.draw();
            }
            if (drawnTile.getValue() > highestTile.getValue()) {
                highestTile = drawnTile;
                firstPlayer = player;
            }
            System.out.println(player.getName() + " drew initial tile " + drawnTile.toString());
        }

        // Create new ArrayList with firstPlayer at index 0, other players maintaining relative positions
        ArrayList<Player> orderedPlayers = new ArrayList<>();
        int firstPlayerIndex = this.players.indexOf(firstPlayer);
        for (int i = firstPlayerIndex; i < this.players.size(); i++) {
            orderedPlayers.add(this.players.get(i));
        }
        for (int i = 0; i < firstPlayerIndex; i++) {
            orderedPlayers.add(this.players.get(i));
        }
        this.players = orderedPlayers;
        System.out.println(this.players.get(0).getName() + " goes first!");
    }

    // Play a new meld on the table
    public void playNewMeldFromHand(String tile) {
        Tile t = this.currentPlayer.getHand().remove(new Tile(tile));
        this.workspace.add(new Meld(t.toString()));
        this.currentPlayer.updateHandList();
        this.updateWorkspaceList();
    }

    // Play a new meld on the table from an already existing meld
    // Data is formatted as meldIndex,tileToRemoveIndex
    public boolean playNewMeldFromExistingMeld(String data) {
        String[] indices = data.split(",");
        int meldIndex = Integer.parseInt(indices[0]);
        int tileToRemoveIndex = Integer.parseInt(indices[1]);
        Meld meld = this.workspace.get(meldIndex);

        Tile removedTile = meld.removeTile(tileToRemoveIndex);
        if (removedTile != null) {
            // Only split the meld if it's not the first or last tile in the meld
            if (tileToRemoveIndex > 0 && tileToRemoveIndex < meld.getSize() - 1) {
                Meld secondHalf = meld.splitMeld(tileToRemoveIndex - 1);
                this.workspace.add(secondHalf);
            }

            Meld newMeld = new Meld(removedTile.toString());
            this.workspace.add(newMeld);
            this.updateWorkspaceList();
            return true;
        }
        return false;
    }

    public void playTileFromHandToExistingMeld(String tileToAdd, String meldToAddTo) {
        Meld meld = this.findMatchingMeld(meldToAddTo);
        Tile t = this.currentPlayer.getHand().remove(new Tile(tileToAdd));
        meld.addTile(t);
        this.currentPlayer.updateHandList();
        this.updateWorkspaceList();
    }

    public boolean playTileFromMeldToExistingMeld(String data, String meldToAddToStr) {
        String[] indices = data.split(",");
        int meldIndex = Integer.parseInt(indices[0]);
        int tileToRemoveIndex = Integer.parseInt(indices[1]);

        Meld meld = this.workspace.get(meldIndex);
        Tile removedTile = meld.removeTile(tileToRemoveIndex);
        if (removedTile != null) {
            if (tileToRemoveIndex > 0 && tileToRemoveIndex < meld.getSize() - 1) {
                Meld secondHalf = meld.splitMeld(tileToRemoveIndex - 1);
                this.workspace.add(secondHalf);
            }

            Meld meldToAddTo = this.findMatchingMeld(meldToAddToStr);
            Tile tile = meldToAddTo.addTile(removedTile);
            if (tile != null) {
                if (tile.isJoker()) {
                    this.workspace.add(new Meld(tile.toString()));
                }

                this.updateWorkspaceList();
                return true;
            }
        }
        return false;
    }

    private Meld findMatchingMeld(String meldToFind) {
        Meld meld = null;
        for (Meld m : this.workspace) {
            if (meldToFind.equals(m.toString())) {
                meld = m;
                break;
            }
        }
        return meld;
    }

    public ArrayList<Meld> getWorkspace() {
        return this.workspace;
    }

    public void setWorkspace(ArrayList<Meld> workspace) {
        this.workspace = workspace;
    }

    public void updateWorkspaceList() {
        this.workspaceList.clear();

        if (this.workspace.isEmpty()) {
            this.workspaceList.add(new Meld());
            return;
        }

        for (Meld meld : this.workspace) {
            this.workspaceList.add(meld);
        }
    }

    public void makeWorkspaceCopy() {
        this.workspace = new ArrayList<Meld>();
        for (Meld meld : this.table.getState()) {
            Meld newMeld = new Meld(meld);
            this.workspace.add(newMeld);
        }
    }

    public ObservableList<Meld> getWorkspaceList() {
        return this.workspaceList;
    }

    public int getNumPlayers() {
        return this.numPlayers;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    public void setCurrentPlayer(Player player) {
        if (player != null) {
            this.currentPlayer = player;
        } else {
            int index = this.players.indexOf(this.currentPlayer);
            if (index >= (this.numPlayers - 1)) {
                this.currentPlayer = this.players.get(0);
            } else {
                this.currentPlayer = this.players.get(index + 1);
            }
        }
    }

    public Player getWinner() {
        return this.winner;
    }

    public void setWinner(Player player) {
        this.winner = player;
    }

    public Stock getStock() {
        return this.stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public Table getTable() {
        return this.table;
    }

    public void setPlayerData(ArrayList<String> playerNames, ArrayList<String> playerStrategies) {
        Iterator<String> namesIt = playerNames.iterator();
        Iterator<String> strategiesIt = playerStrategies.iterator();
        while (namesIt.hasNext() && strategiesIt.hasNext()) {
            this.playerData.add(new Pair<>(namesIt.next(), strategiesIt.next()));
        }
    }

    public void setRiggedData(Stock riggedStock, Stock riggedDeciderStock, ArrayList<Hand> riggedHands) {
        this.riggedGame = true;
        this.riggedStock = riggedStock;
        this.riggedDeciderStock = riggedDeciderStock;
        this.riggedHands = riggedHands;
    }

    public boolean determineValidState() {
        return this.table.isValidState();
    }

    // Create GameMemento representing game state (table and current player's hand)
    protected void createMemento() {
        this.memento = new GameMemento(this.table, this.currentPlayer.getHand());
    }

    // Restore state (table and current player's hand)
    protected void restoreMementoWithPenalty(Player player) {
        this.table = this.memento.getTableState();
        player.setHand(this.memento.getHandState());
        if (this.stock.getSize() > 0) { player.add(this.stock.draw()); }
        if (this.stock.getSize() > 0) { player.add(this.stock.draw()); }
        if (this.stock.getSize() > 0) { player.add(this.stock.draw()); }

        // Reset lowest hand counts
        int newLowestHandCount = 100;
        for (Player p : this.players) {
            if (p.getHandSize() < newLowestHandCount) {
                newLowestHandCount = p.getHandSize();
            }
        }
        for (Player p : this.players) {
            p.setLowestHandCount(newLowestHandCount);
        }
    }
}
