package core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

import core.Globals.PlayerType;

public class Game {
    protected Stock stock;
    protected ArrayList<Player> players;
    protected Table table;

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
            this.determinePlayerOrder();
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
        String winner = null;
        boolean winningCondition = false;

        // Game loop.
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
    
    protected void determinePlayerOrder() {
        // Rearrange players based on value of tile drawn (highest to lowest)
        for (Player player : this.players) {
            player.setDrawnTile(this.stock.draw());
        }
        this.players.sort(Comparator.comparing(o -> ((Player) o).getDrawnTile()).reversed());
        
        // Print the new player order
        for (int i = 0; i < players.size(); i++) {
            String order = "";
            if (i == 0) {       order = "first"; }
            else if (i == 1) {  order = "second"; }
            else if (i == 2) {  order = "third"; }
            else {              order = "fourth"; }
            System.out.println(players.get(i).getName() + " drew " + players.get(i).getDrawnTile().toString() + " and goes " + order);
        }
        
        // Add tiles back to stock and shuffle
        for (Player player : this.players) {
            this.stock.add(player.getDrawnTile());
        }
        this.stock.shuffle();
        System.out.println();
    }
}