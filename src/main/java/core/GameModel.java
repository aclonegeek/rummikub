package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import core.Globals.PlayerType;

public class GameModel {
    private Stock stock;
    private ArrayList<Player> players;
    private Table table;

    private int numPlayers;
    private Map<String, String> playerData;

    private GameMemento memento;

    // Rigging related
    private boolean riggedGame = false;
    private Stock riggedStock;
    private Stock riggedDeciderStock;
    private ArrayList<Hand> riggedHands;

    public GameModel() {
        this.stock = new Stock();
        this.table = new Table();
        this.players = new ArrayList<>();
        this.playerData = new HashMap<String, String>();
    }

    public void setup() {
        if (this.riggedGame) {
            this.stock = this.riggedStock;
        } else {
            this.stock.populate();
            this.stock.shuffle();
        }

        // Create the players
        this.playerData.forEach((key, value) -> {
                if (value == "StrategyHuman") {
                    this.players.add(new PlayerHuman(key));
                } else if (value == "Strategy1") {
                    this.players.add(new Player1(key));
                } else if (value == "Strategy2") {
                    this.players.add(new Player2(key));
                } else if (value == "Strategy3") {
                    this.players.add(new Player3(key));
                } else {
                    this.players.add(new Player4(key));
                }
            });

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

    public int getNumPlayers() {
        return this.numPlayers;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public ArrayList<Player> getPlayers() {
        return this.players;
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
            this.playerData.put(namesIt.next(), strategiesIt.next());
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
    protected void createMemento(Player player) {
        this.memento = new GameMemento(this.table, player.getHand());
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
