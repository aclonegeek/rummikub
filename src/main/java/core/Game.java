package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import core.Globals.PlayerType;

public class Game {
    protected Stock stock;
    protected ArrayList<Player> players;
    protected Table table;
    protected Map <Player, Integer> playerScores;

    // Testing related attributes
    private boolean testing = false;
    private ArrayList<Player> riggedPlayers;
    private Stock riggedStock;

    public Game() {}

    public Game(boolean testing) {
        this.testing = testing;
    }

    public void start() {
        this.playWithConsole();
    }

    public void rig(ArrayList<Player> riggedPlayers, Stock riggedStock) {
        this.riggedStock = riggedStock;
        this.riggedPlayers = riggedPlayers;
    }

    protected void setup() {
        if (testing) {
            this.stock = this.riggedStock;
            this.players = this.riggedPlayers;
        } else {
            this.stock = new Stock();
            this.stock.populate();
            this.stock.shuffle();
            this.createPlayers();
            
            Stock tempStock = new Stock();
            tempStock.populateForDraw();
            tempStock.shuffle();
            this.determinePlayerOrder(tempStock);
            
            for (Player player : this.players) {
                for (int i = 0; i < 14; i++) {
                    player.add(stock.draw());
                }
            }
        }
        
        this.table = new Table();

        // Display each player's initial hand
        for (Player player : this.players) {
            System.out.println(player.toString());
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

    private void playWithConsole() {
        System.out.println("=== RUMMIKUB ===");
        System.out.println("\n--- SETUP ---");
        this.setup();

        System.out.println("\n--- GAME ---");
        Tile tile; // Used to store the tile drawn from the stock.
        ArrayList<Meld> workspace; // Store the player's workspace.
        Player winner = null;
        boolean winningCondition = false;

        // Game loop
        while (!winningCondition) {
            for (Player player : this.players) {
                System.out.println("Table:\n" + this.table.toString());
                System.out.println(player.getName() + "'s turn");

                workspace = player.play(this.table.getState());
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
                    winner = player;
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
            winner = this.players.get(0);
            for (Player player : this.players.subList(1, this.players.size())) {
                if (player.getHandSize() < lowestHandCount) {
                    lowestHandCount = player.getHandSize();
                    winner = player;
                }
            }
        }

        this.determinePlayerScores(winner);
        System.out.println(winner.getName() + " has won!");
        System.out.println("Scores:");
        for (Player player : this.players) {
            System.out.println(player.getName() + ": " + this.playerScores.get(player));
        }
    }
    
    protected void createPlayers() {
        this.players = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        int playerCount = 0;
        while (playerCount < 4) {
            System.out.println("Enter name for Player " + (playerCount + 1) + ":");
            String name = scanner.nextLine();
            System.out.println("Which strategy should " + name + " use:");
            System.out.println("1. StrategyHuman");
            System.out.println("2. Strategy1");
            System.out.println("3. Strategy2");
            System.out.println("4. Strategy3");
            System.out.println("5. Strategy4");
            
            int choice = 0;
            Player player = null;
            
            while (choice < 1 || choice > 5) {
                // Validate input is an integer
                while (!scanner.hasNextInt()) {
                    System.out.println("Invalid option (that's not an integer).");
                    scanner.next();
                }
                choice = scanner.nextInt();
                scanner.nextLine();
                
                // Validate integer input is within valid range
                if (choice < 1 || choice > 5 ) {
                    System.out.println("Invalid option.");
                }
            }

            if (choice == 1) {
                player = new PlayerHuman(name);
            } else if (choice == 2) {
                player = new Player1(name);
            } else if (choice == 3) {
                player = new Player2(name);
            } else if (choice == 4) {
                player = new Player3(name);
            } else {
                player = new Player4(name);
            } 
 
            this.players.add(player);
            playerCount++;
            System.out.println(name + " created, uses " + player.getPlayerType().toString() + ".\n");
            
            // Prompt user regarding whether they want to add Player 3 and Player 4
            if (playerCount >= 2 && playerCount < 4) {
                String addAnother = "";
                while (!addAnother.equals("y") && !addAnother.equals("n")) {
                    System.out.println("Would you like to add a Player " + (playerCount + 1) + "? (y/n)");
                    addAnother = scanner.nextLine();
                }
                if (addAnother.equals("n")) {
                    break;
                }
            }
        }
    }
    
    protected void determinePlayerOrder(Stock tempStock) {
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
    
    protected void determinePlayerScores(Player winner) {
        this.playerScores = new HashMap<Player, Integer>();
        this.playerScores.put(winner, 0);
        for (Player player : this.players) {
            if (player != winner) {
                this.playerScores.put(player, player.getScore() * -1);
                this.playerScores.put(winner, this.playerScores.get(winner) + player.getScore());
            }
        }
    }
}