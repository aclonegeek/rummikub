package core;

import java.util.ArrayList;
import java.util.Arrays;

public class Game {
    protected Stock stock;
    protected ArrayList<Player> players;
    protected Table table;

    // Testing related attributes
    private boolean testing = false;
    private boolean fourthPlayer = false;
    private ArrayList<Hand> riggedHands;
    private Stock riggedStock;

    public Game() {}

    public Game(boolean testing) {
        this.testing = testing;
    }

    public Game(boolean testing, boolean fourthPlayer) {
        this.testing = testing;
        this.fourthPlayer = fourthPlayer;
    }

    public void start() {
        this.playWithConsole();
    }

    public void rig(ArrayList<Hand> riggedHands, Stock riggedStock) {
        this.riggedStock = riggedStock;
        this.riggedHands = riggedHands;
    }

    protected void setup() {
        if (testing) {
            this.stock = this.riggedStock;
        } else {
            this.stock = new Stock();
            this.stock.populate();
            this.stock.shuffle();
        }
        this.table = new Table();

        Player playerHuman = new PlayerHuman("PlayerHuman");
        Player player1     = new Player1("Player1");
        Player player2     = new Player2("Player2");
        Player player3     = new Player3("Player3");
        Player player4     = null;
        this.players = new ArrayList<>(Arrays.asList(playerHuman, player1, player2, player3));
        if (this.fourthPlayer) {
            player4 = new Player4("Player4");
            this.players.add(player4);
        }

        // Register each player's PlayBehaviour as an observer of the table and give each player 14 tiles.
        int index = 0;
        for (Player player : this.players) {
            this.table.registerObserver(player.getPlayBehaviour());
            if (testing) {
                Hand hand = this.riggedHands.get(index);
                for (int i = 0; i < hand.getSize(); i++) {
                    player.add(hand.getTile(i));
                }
                index++;
            } else {
                for (int i = 0; i < 14; i++) {
                    player.add(stock.draw());
                }
            }
        }

        // Display each player's initial hand
        for (Player player : this.players) {
            System.out.println(player.toString());
        }

        // Register player 3's subjects.
        playerHuman.registerObserver(player3);
        player1.registerObserver(player3);
        player2.registerObserver(player3);

        if (this.fourthPlayer) {
            playerHuman.registerObserver(player4);
            player1.registerObserver(player4);
            player2.registerObserver(player4);
        }
    }

    private void playWithConsole() {
        System.out.println("=== RUMMIKUB ===");
        this.setup();

        Tile tile; // Used to store the tile drawn from the stock.
        ArrayList<Meld> workspace; // Store the player's workspace.
        String winner = null;
        boolean winningCondition = false;

        // Game loop.
        while (!winningCondition) {
            for (Player player : this.players) {
                System.out.println("Table:\n" + this.table.toString());
                System.out.println(player.getName() + "'s turn");

                workspace = player.play();
                if (workspace == null) {
                    tile = this.stock.draw();
                    player.add(tile);
                } else {
                    this.table.setState(workspace);
                }

                // Check for winning conditions
                // 1. Player has a hand size of 0, OR
                // 2. The stock is empty, so the player with the smallest hand wins
                if (player.getHandSize() == 0) {
                    winner = player.getName();
                    winningCondition = true;
                    break;
                } else if (this.stock.getStock().size() == 0) {
                    winningCondition = true;
                    break;
                }
            }
        }

        if (winningCondition && winner == null) {
            int lowestHandCount = this.players.get(0).getHandSize();
            winner = this.players.get(0).getName();
            for (Player player : this.players.subList(1, this.players.size())) {
                if (player.getHandSize() < lowestHandCount) {
                    lowestHandCount = player.getHandSize();
                    winner = player.getName();
                }
            }
        }

        System.out.println(winner + " has won!");
    }
}
