package core;

import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.TestCase;

public class GameTest extends TestCase {
    private ArrayList<Meld> playHuman(Player human, String command, ArrayList<Meld> workspace) {
        workspace = human.playBehaviour.parseInput(human.hand, command, workspace);
        if (workspace != null) {
            human.initialMove = false;
            human.drawing = false;
        } else {
            human.drawing = true;
        }
        human.notifyObservers();
        return workspace;
    }
    
    public void testDeterminePlayerOrder() {
        Player playerHuman = new PlayerHuman("Human");
        Player player1 = new Player1("p1");
        Player player2 = new Player2("p2");
        Player player3 = new Player3("p3");
        ArrayList<Player> players = new ArrayList<>(Arrays.asList(playerHuman, player1, player2, player3));
        
        ArrayList<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile("G4"));      // Human draws G4
        tiles.add(new Tile("R4"));      // p1 draws R4 but draws again (because R4 = G4)
        tiles.add(new Tile("B1"));      // p1 draws B1
        tiles.add(new Tile("B7"));      // p2 draws B7
        tiles.add(new Tile("O5"));      // p3 draws O5
        Stock stock = new Stock(tiles);
        
        Game game = new Game();
        game.players = players;
        game.stock = stock;
        
        // Verify initial order of players
        assertEquals("Human", game.players.get(0).getName());
        assertEquals("p1", game.players.get(1).getName());
        assertEquals("p2", game.players.get(2).getName());
        assertEquals("p3", game.players.get(3).getName());
        
        // Test new ordering of players
        game.determinePlayerOrder(stock);
        assertEquals("p2", game.players.get(0).getName());
        assertEquals("p3", game.players.get(1).getName());
        assertEquals("Human", game.players.get(2).getName());
        assertEquals("p1", game.players.get(3).getName());
    }
    
    public void testDeterminePlayerScores() {
        Hand humanHand = new Hand("R2,R3,R4,R5");
        Hand p1Hand = new Hand("G1,G2,O10,R12,B13");
        Hand p2Hand = new Hand("J,O1,B9");
        Hand p3Hand = new Hand("");
        Player playerHuman = new PlayerHuman("Human");
        Player player1 = new Player1("p1");
        Player player2 = new Player2("p2");
        Player player3 = new Player3("p3");
        playerHuman.setHand(humanHand);
        player1.setHand(p1Hand);
        player2.setHand(p2Hand);
        player3.setHand(p3Hand);
        
        ArrayList<Player> players = new ArrayList<>(Arrays.asList(playerHuman, player1, player2, player3));
        Game game = new Game();
        game.players = players;
        game.determinePlayerScores(player3); // player3 is the winner
        
        assertTrue(game.playerScores.get(playerHuman).equals(-14));
        assertTrue(game.playerScores.get(player1).equals(-42));
        assertTrue(game.playerScores.get(player2).equals(-49));
        assertTrue(game.playerScores.get(player3).equals(105));
    }
    
    public void testGame1() {
        Hand humanHand = new Hand("R1,R11,R13,B2,B3,B9,G1,G8,G9,G11,G12,G13,O1,O7");
        Hand p1Hand = new Hand("R5,R7,R8,R11,R12,B3,B10,B11,G5,G8,G10,O2,O5,O10");
        Hand p2Hand = new Hand("R1,R6,R10,B9,B11,G1,G3,G7,G9,G10,G13,O11,O12,O13");
        Hand p3Hand = new Hand("R2,R3,R4,B8,B12,B13,G4,G5,G6,O1,O2,O3,O9,O12");
        Player playerHuman = new PlayerHuman("Human");
        Player player1 = new Player1("p1");
        Player player2 = new Player2("p2");
        Player player3 = new Player3("p3");
        playerHuman.setHand(humanHand);
        player1.setHand(p1Hand);
        player2.setHand(p2Hand);
        player3.setHand(p3Hand);
        
        ArrayList<Hand> hands = new ArrayList<>(Arrays.asList(humanHand, p1Hand, p2Hand, p3Hand));
        ArrayList<Player> players = new ArrayList<>(Arrays.asList(playerHuman, player1, player2, player3));
        Stock stock = new Stock();
        stock.remaining(hands);
        
        Game game = new Game(true);
        game.rig(players, stock);
        game.setup();
        
        ArrayList<Meld> workspace = new ArrayList<>();
        workspace = this.playHuman(game.players.get(0), "G11 G12 G13 > NM", game.table.getState());
        workspace = this.playHuman(game.players.get(0), "R1 G1 O1 > NM", game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(0).add(game.stock.draw()); }
        assertEquals("1: {G11 G12 G13}\n2: {R1 G1 O1}\n", game.table.toString());   // req. 8e
        
        workspace = game.players.get(1).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(1).add(game.stock.draw()); }
        assertEquals("1: {G11 G12 G13}\n2: {R1 G1 O1}\n3: {B10 G10 O10}\n", game.table.toString());     // req. 4a1, 8b, 10a

        workspace = game.players.get(2).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(2).add(game.stock.draw()); }
        assertEquals("1: {G11 G12 G13}\n2: {R1 G1 O1}\n3: {B10 G10 O10}\n4: {O11 O12 O13}\n", game.table.toString());   // req. 4a2, 8a
        
        workspace = game.players.get(3).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(3).add(game.stock.draw()); }
        assertEquals("1: {G11 G12 G13}\n2: {R1 G1 O1}\n3: {B10 G10 O10}\n4: {O11 O12 O13}\n5: {R2 R3 R4}\n6: {G4 G5 G6}\n7: {O1 O2 O3}\n", game.table.toString());  // req. 4ba, 8c, 11a
    }
    
    public void testGame1b() {
        Hand humanHand = new Hand("R1,R11,R13,B2,B3,B9,G1,G8,G9,G11,G12,G13,O1,O7");
        Hand p1Hand = new Hand("R5,R6,R9,R12,R13,B2,B5,B6,G11,O4,O3,O5,O6,O8");
        Hand p2Hand = new Hand("R1,R6,R10,B9,B11,G1,G3,G7,G9,G10,G13,O11,O12,O13");
        Hand p3Hand = new Hand("R5,R7,R8,R11,R12,B3,B10,B11,G5,G8,G10,O2,O5,O10");
        Player playerHuman = new PlayerHuman("Human");
        Player player1 = new Player1("p1");
        Player player2 = new Player2("p2");
        Player player3 = new Player3("p3");
        playerHuman.setHand(humanHand);
        player1.setHand(p1Hand);
        player2.setHand(p2Hand);
        player3.setHand(p3Hand);
        
        ArrayList<Hand> hands = new ArrayList<>(Arrays.asList(humanHand, p1Hand, p2Hand, p3Hand));
        ArrayList<Player> players = new ArrayList<>(Arrays.asList(playerHuman, player1, player2, player3));
        Stock stock = new Stock();
        stock.remaining(hands);
        
        Game game = new Game(true);
        game.rig(players, stock);
        game.setup();
        
        ArrayList<Meld> workspace = new ArrayList<>();
        game.players.get(0).add(game.stock.draw());
        assertEquals("", game.table.toString());
        
        workspace = game.players.get(1).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(1).add(game.stock.draw()); }
        assertEquals("1: {R6 B6 O6}\n2: {R5 B5 O5}\n", game.table.toString());  // req. 4b2, 8d
    }
    
    public void testGame2() {
        Hand humanHand = new Hand("B1,R6,R13,B2,B8,B10,B11,B12,B13,G5,G8,G9,O3,O13");
        Hand p1Hand = new Hand("R1,R2,R3,R4,R6,R7,R8,G9,O9,B9,O2,O3,O4,O5");
        Hand p2Hand = new Hand("R1,R5,B3,G1,G2,G3,G8,G12,O2,O5,G10,O11,O11,O12");
        Hand p3Hand = new Hand("B9,G7,R10,R11,B1,B5,B6,G6,G10,O1,O7,O10,O12,O13");
        Player playerHuman = new PlayerHuman("Human");
        Player player1 = new Player1("p1");
        Player player2 = new Player2("p2");
        Player player3 = new Player3("p3");
        playerHuman.setHand(humanHand);
        player1.setHand(p1Hand);
        player2.setHand(p2Hand);
        player3.setHand(p3Hand);
        
        ArrayList<Hand> hands = new ArrayList<>(Arrays.asList(humanHand, p1Hand, p2Hand, p3Hand));
        ArrayList<Player> players = new ArrayList<>(Arrays.asList(playerHuman, player1, player2, player3));
        Stock stock = new Stock();
        stock.remaining(hands);

        Game game = new Game(true);
        game.rig(players, stock);
        game.setup();

        ArrayList<Meld> workspace = new ArrayList<>();
        workspace = this.playHuman(game.players.get(0), "B10 B11 B12 B13 > NM", game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(0).add(game.stock.draw()); }
        assertEquals("1: {B10 B11 B12 B13}\n", game.table.toString());

        workspace = game.players.get(1).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(1).add(game.stock.draw()); }
        assertEquals("1: {B10 B11 B12 B13}\n2: {B9 G9 O9}\n3: {R6 R7 R8}\n4: {O2 O3 O4 O5}\n5: {R1 R2 R3 R4}\n", game.table.toString());  // req. 4c, req. 10b
        assertEquals(0, game.players.get(1).getHandSize());
    }
    
    public void testGame3() {
        Hand humanHand = new Hand("R4,B6,B7,B9,B10,B12,G4,G11,G13,O1,O4,O8,O8,O12");
        Hand p1Hand = new Hand("R2,R6,B1,B2,B3,B8,B12,G8,G12,O2,O3,O6,O11,O11");
        Hand p2Hand = new Hand("R1,R1,R8,R10,R11,R13,B9,B13,G7,G7,G8,O2,O5,O13");
        Hand p3Hand = new Hand("R2,R7,R11,R12,B3,B1,B11,G2,G10,G12,G13,O1,O5,O10");
        Player playerHuman = new PlayerHuman("Human");
        Player player1 = new Player1("p1");
        Player player2 = new Player2("p2");
        Player player3 = new Player3("p3");
        playerHuman.setHand(humanHand);
        player1.setHand(p1Hand);
        player2.setHand(p2Hand);
        player3.setHand(p3Hand);
        
        ArrayList<Hand> hands = new ArrayList<>(Arrays.asList(humanHand, p1Hand, p2Hand, p3Hand));
        ArrayList<Player> players = new ArrayList<>(Arrays.asList(playerHuman, player1, player2, player3));
     
        ArrayList<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile("B11"));
        tiles.add(new Tile("R3"));
        tiles.add(new Tile("R3"));
        tiles.add(new Tile("G11"));
        tiles.add(new Tile("R12"));
        tiles.add(new Tile("R2"));
        tiles.add(new Tile("R3"));
        tiles.add(new Tile("G1"));     
        Stock stock = new Stock(tiles);
        stock.remaining(hands);

        Game game = new Game(true);
        game.rig(players, stock);
        game.setup();
        
        // human draws
        ArrayList<Meld> workspace = new ArrayList<>();
        game.players.get(0).add(game.stock.draw());
        assertEquals("", game.table.toString());

        // p1 draws
        workspace = game.players.get(1).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(1).add(game.stock.draw()); }     // req. 6, 10e
        assertEquals(null, workspace);
        assertEquals("", game.table.toString());
        
        // p2 has valid melds but does not play because no one else has
        workspace = game.players.get(2).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(2).add(game.stock.draw()); }     // req. 15
        assertEquals(null, workspace);
        assertEquals("", game.table.toString());
        
        // p3 draws tile
        workspace = game.players.get(3).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(3).add(game.stock.draw()); }
        assertEquals("", game.table.toString());

        workspace = this.playHuman(game.players.get(0), "B9 B10 B11 B12 > NM", game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(0).add(game.stock.draw()); }
        assertEquals("1: {B9 B10 B11 B12}\n", game.table.toString());
        
        workspace = game.players.get(1).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(1).add(game.stock.draw()); }     // req. 10f
        assertEquals(null, workspace);
        assertEquals("1: {B9 B10 B11 B12}\n", game.table.toString());
        
        workspace = game.players.get(2).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(2).add(game.stock.draw()); }
        assertEquals("1: {B9 B10 B11 B12}\n2: {R13 B13 O13}\n", game.table.toString()); // req. 15a
        
        workspace = game.players.get(3).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(3).add(game.stock.draw()); }
        assertEquals("1: {B9 B10 B11 B12}\n2: {R13 B13 O13}\n3: {G10 G11 G12 G13}\n", game.table.toString());   // req. 11b
        
        workspace = this.playHuman(game.players.get(0), "R4 G4 O4 > NM", game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(0).add(game.stock.draw()); }
        assertEquals("1: {B9 B10 B11 B12}\n2: {R13 B13 O13}\n3: {G10 G11 G12 G13}\n4: {R4 G4 O4}\n", game.table.toString());
        
        workspace = game.players.get(1).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(1).add(game.stock.draw()); }
        assertEquals("1: {B9 B10 B11 B12}\n2: {R13 B13 O13}\n3: {G10 G11 G12 G13}\n4: {R4 G4 O4}\n5: {R12 B12 G12}\n", game.table.toString());
        
        // p2 draws tile
        workspace = game.players.get(2).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(2).add(game.stock.draw()); }
        assertEquals(null, workspace);
        assertEquals("1: {B9 B10 B11 B12}\n2: {R13 B13 O13}\n3: {G10 G11 G12 G13}\n4: {R4 G4 O4}\n5: {R12 B12 G12}\n", game.table.toString());  // req. 17b
        
        // p3 tries to play all the tiles it can but cannot so draws tile
        workspace = game.players.get(3).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(3).add(game.stock.draw()); }
        assertEquals(null, workspace);
        assertEquals("1: {B9 B10 B11 B12}\n2: {R13 B13 O13}\n3: {G10 G11 G12 G13}\n4: {R4 G4 O4}\n5: {R12 B12 G12}\n", game.table.toString());  // req. 13a
    
        // human draws tile
        game.players.get(0).add(game.stock.draw());
        assertEquals("1: {B9 B10 B11 B12}\n2: {R13 B13 O13}\n3: {G10 G11 G12 G13}\n4: {R4 G4 O4}\n5: {R12 B12 G12}\n", game.table.toString());
        
        workspace = game.players.get(1).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(1).add(game.stock.draw()); }
        assertEquals("1: {B8 B9 B10 B11 B12}\n2: {R13 B13 O13}\n3: {G10 G11 G12 G13}\n4: {R4 G4 O4}\n5: {R12 B12 G12}\n6: {R2 B2 O2}\n7: {R3 B3 O3}\n", game.table.toString()); // req. 10c
    }
    
    public void testGame4() {
        Hand humanHand = new Hand("B7,B8,B9,B10,G6,G7,O6,O8");
        Hand p1Hand = new Hand("R9,R10,R11,R13,G6,G7,O1,O2");
        Hand p2Hand = new Hand("R8,B13,G13,O13,O1,O2,O4,O5");
        Hand p3Hand = new Hand("B6,B11,G8,G9,G10,G11,O3,O4");
        Player playerHuman = new PlayerHuman("Human");
        Player player1 = new Player1("p1");
        Player player2 = new Player2("p2");
        Player player3 = new Player3("p3");
        playerHuman.setHand(humanHand);
        player1.setHand(p1Hand);
        player2.setHand(p2Hand);
        player3.setHand(p3Hand);
        
        ArrayList<Hand> hands = new ArrayList<>(Arrays.asList(humanHand, p1Hand, p2Hand, p3Hand));
        ArrayList<Player> players = new ArrayList<>(Arrays.asList(playerHuman, player1, player2, player3));
        Stock stock = new Stock();
        stock.remaining(hands);

        Game game = new Game(true);
        game.rig(players, stock);
        game.setup();
        
        ArrayList<Meld> workspace = new ArrayList<>();
        workspace = this.playHuman(game.players.get(0), "B7 B8 B9 B10 > NM", game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(0).add(game.stock.draw()); }
        assertEquals("1: {B7 B8 B9 B10}\n", game.table.toString());
        
        workspace = game.players.get(1).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(1).add(game.stock.draw()); }
        assertEquals("1: {B7 B8 B9 B10}\n2: {R9 R10 R11}\n", game.table.toString());
        
        workspace = game.players.get(2).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(2).add(game.stock.draw()); }
        assertEquals("1: {B7 B8 B9 B10}\n2: {R9 R10 R11}\n3: {B13 G13 O13}\n", game.table.toString());
        
        workspace = game.players.get(3).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(3).add(game.stock.draw()); }
        assertEquals("1: {B7 B8 B9 B10}\n2: {R9 R10 R11}\n3: {B13 G13 O13}\n4: {G8 G9 G10 G11}\n", game.table.toString());
        
        workspace = this.playHuman(game.players.get(0), "M4G8 > NM", game.table.getState());
        workspace = this.playHuman(game.players.get(0), "G7 G6 > M5", game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(0).add(game.stock.draw()); }
        assertEquals("1: {B7 B8 B9 B10}\n2: {R9 R10 R11}\n3: {B13 G13 O13}\n4: {G9 G10 G11}\n5: {G6 G7 G8}\n", game.table.toString());  // req. 5, req. 9d
        
        workspace = game.players.get(1).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(1).add(game.stock.draw()); }
        assertEquals("1: {B7 B8 B9 B10}\n2: {R9 R10 R11}\n3: {B13 G13 O13 R13}\n4: {G9 G10 G11}\n5: {G6 G7 G8}\n", game.table.toString());  // req. 9b
        
        workspace = game.players.get(2).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(2).add(game.stock.draw()); }
        assertEquals("1: {B7 B8 B9 B10}\n2: {R8 R9 R10 R11}\n3: {B13 G13 O13 R13}\n4: {G9 G10 G11}\n5: {G6 G7 G8}\n", game.table.toString());   // req. 9a, 17a
        
        workspace = game.players.get(3).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(3).add(game.stock.draw()); }
        assertEquals("1: {B6 B7 B8 B9 B10 B11}\n2: {R8 R9 R10 R11}\n3: {B13 G13 O13 R13}\n4: {G9 G10 G11}\n5: {G6 G7 G8}\n", game.table.toString());    // req. 9c, 14a
    }
    
    public void testGame5() {
        Hand humanHand = new Hand("B12,O12,R12,G12,O4,O6,O8,O10");
        Hand p1Hand = new Hand("R10,G10,B10,R8,R12,G8,O1,O3,O5");
        Hand p2Hand = new Hand("R9,R10,R11,G12,B12,O1,O3,O5");
        Hand p3Hand = new Hand("G9,G10,G11,O2,O4,O6,O8,O11");
        Player playerHuman = new PlayerHuman("Human");
        Player player1 = new Player1("p1");
        Player player2 = new Player2("p2");
        Player player3 = new Player3("p3");
        playerHuman.setHand(humanHand);
        player1.setHand(p1Hand);
        player2.setHand(p2Hand);
        player3.setHand(p3Hand);
        
        ArrayList<Hand> hands = new ArrayList<>(Arrays.asList(humanHand, p1Hand, p2Hand, p3Hand));
        ArrayList<Player> players = new ArrayList<>(Arrays.asList(playerHuman, player1, player2, player3));
     
        ArrayList<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile("B4"));
        tiles.add(new Tile("R1"));   
        Stock stock = new Stock(tiles);
        stock.remaining(hands);

        Game game = new Game(true);
        game.rig(players, stock);
        game.setup();
        
        ArrayList<Meld> workspace = new ArrayList<>();
        workspace = this.playHuman(game.players.get(0), "B12 O12 R12 G12 > NM", game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(0).add(game.stock.draw()); }
        assertEquals("1: {B12 O12 R12 G12}\n", game.table.toString());
        
        workspace = game.players.get(1).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(1).add(game.stock.draw()); }
        assertEquals("1: {B12 O12 R12 G12}\n2: {R10 B10 G10}\n", game.table.toString());
        
        workspace = game.players.get(2).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(2).add(game.stock.draw()); }
        assertEquals("1: {B12 O12 R12 G12}\n2: {R10 B10 G10}\n3: {R9 R10 R11}\n", game.table.toString());
        
        workspace = game.players.get(3).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(3).add(game.stock.draw()); }
        assertEquals("1: {B12 O12 R12 G12}\n2: {R10 B10 G10}\n3: {R9 R10 R11}\n4: {G9 G10 G11}\n", game.table.toString());
        
        game.players.get(0).add(game.stock.draw());
        assertEquals("1: {B12 O12 R12 G12}\n2: {R10 B10 G10}\n3: {R9 R10 R11}\n4: {G9 G10 G11}\n", game.table.toString());
        
        workspace = game.players.get(1).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(1).add(game.stock.draw()); }
        assertEquals("1: {O12 R12 G12 B12}\n2: {R10 B10 G10}\n3: {R8 R9 R10 R11 R12}\n4: {G8 G9 G10 G11}\n", game.table.toString());    // req. 9f
        
        workspace = game.players.get(2).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(2).add(game.stock.draw()); }
        assertEquals("1: {R12 G12 B12}\n2: {R10 B10 G10}\n3: {R8 R9 R10 R11 R12}\n4: {G8 G9 G10 G11}\n5: {B12 G12 O12}\n", game.table.toString());  // req. 9e
    }
    
    public void testGame6() {
        Hand humanHand = new Hand("B1,B3,B4,O1,O2,O3,O7,O8,O9,O10");
        Hand p1Hand = new Hand("R1,R2,R3,R10,R11,R12,R13,G1,G2,G3,O5");
        Hand p2Hand = new Hand("O1,O2,O4,O5,O7,O8,B1,B2,B3,B5");
        Hand p3Hand = new Hand("G9,G10,G11,B10,O1,O2,B3,B5,B6,B8");
        Player playerHuman = new PlayerHuman("Human");
        Player player1 = new Player1("p1");
        Player player2 = new Player2("p2");
        Player player3 = new Player3("p3");
        playerHuman.setHand(humanHand);
        player1.setHand(p1Hand);
        player2.setHand(p2Hand);
        player3.setHand(p3Hand);
        
        ArrayList<Hand> hands = new ArrayList<>(Arrays.asList(humanHand, p1Hand, p2Hand, p3Hand));
        ArrayList<Player> players = new ArrayList<>(Arrays.asList(playerHuman, player1, player2, player3));
     
        ArrayList<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile("G1"));
        tiles.add(new Tile("G2"));
        Stock stock = new Stock(tiles);
        stock.remaining(hands);

        Game game = new Game(true);
        game.rig(players, stock);
        game.setup();
        
        ArrayList<Meld> workspace = new ArrayList<>();
        workspace = this.playHuman(game.players.get(0), "O7 O8 O9 O10 > NM", game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(0).add(game.stock.draw()); }
        assertEquals("1: {O7 O8 O9 O10}\n", game.table.toString());
        
        workspace = game.players.get(1).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(1).add(game.stock.draw()); }
        assertEquals("1: {O7 O8 O9 O10}\n2: {R10 R11 R12 R13}\n", game.table.toString());   
        
        workspace = game.players.get(2).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(2).add(game.stock.draw()); }
        assertEquals(null, workspace);
        assertEquals("1: {O7 O8 O9 O10}\n2: {R10 R11 R12 R13}\n", game.table.toString());   // req. 15b
        
        workspace = game.players.get(3).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(3).add(game.stock.draw()); }
        assertEquals("1: {O7 O8 O9 O10}\n2: {R10 R11 R12 R13}\n3: {G9 G10 G11}\n", game.table.toString());
        
        workspace = this.playHuman(game.players.get(0), "O1 O2 O3 > NM", game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(0).add(game.stock.draw()); }
        assertEquals("1: {O7 O8 O9 O10}\n2: {R10 R11 R12 R13}\n3: {G9 G10 G11}\n4: {O1 O2 O3}\n", game.table.toString());
        
        workspace = game.players.get(1).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(1).add(game.stock.draw()); }
        assertEquals("1: {O7 O8 O9 O10}\n2: {R10 R11 R12 R13}\n3: {G9 G10 G11}\n4: {O1 O2 O3}\n5: {R1 R2 R3}\n6: {G1 G2 G3}\n", game.table.toString());     // req. 10d
        
        workspace = game.players.get(2).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(2).add(game.stock.draw()); }
        assertEquals(null, workspace);
        assertEquals("1: {O7 O8 O9 O10}\n2: {R10 R11 R12 R13}\n3: {G9 G10 G11}\n4: {O1 O2 O3}\n5: {R1 R2 R3}\n6: {G1 G2 G3}\n", game.table.toString());
        
        workspace = game.players.get(3).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(3).add(game.stock.draw()); }
        assertEquals("1: {O7 O8 O9}\n2: {R11 R12 R13}\n3: {G9 G10 G11}\n4: {O1 O2 O3}\n5: {R1 R2 R3}\n6: {G1 G2 G3}\n7: {B10 O10 R10}\n", game.table.toString());   // req. 9g
    }
    
    public void testGame7() {
        // Deal cards
        Hand humanHand = new Hand("B1,B2,B3,O1,O2,O3,O7,O8,O9,O12");
        Hand p1Hand = new Hand("R1,R2,R3,R5,R8,R10,R12,G2,G3,G13");
        Hand p2Hand = new Hand("B5,G9,O1,O2,O4,O5,O7,O8,O11,O13");
        Hand p3Hand = new Hand("B2,B3,B4,G7,G8,G9,O5,O6,O10,O11");
        Player playerHuman = new PlayerHuman("Human");
        Player player1 = new Player1("p1");
        Player player2 = new Player2("p2");
        Player player3 = new Player3("p3");
        playerHuman.setHand(humanHand);
        player1.setHand(p1Hand);
        player2.setHand(p2Hand);
        player3.setHand(p3Hand);
        
        ArrayList<Player> players = new ArrayList<>(Arrays.asList(playerHuman, player1, player2, player3));
        
        // Determine first several tiles to be drawn by the players
        ArrayList<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile("O6"));
        tiles.add(new Tile("B6"));
        tiles.add(new Tile("B7"));
        tiles.add(new Tile("R12"));
        tiles.add(new Tile("B12"));
        Stock stock = new Stock(tiles);

        Game game = new Game(true);
        game.rig(players, stock);
        game.setup();

        ArrayList<Meld> workspace = new ArrayList<>();
        // Human plays initial melds
        workspace = this.playHuman(game.players.get(0), "B1 B2 B3 > NM", game.table.getState());
        workspace = this.playHuman(game.players.get(0), "O1 O2 O3 > NM", game.table.getState());
        workspace = this.playHuman(game.players.get(0), "O7 O8 O9 > NM", game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(0).add(game.stock.draw()); }
        assertEquals("1: {B1 B2 B3}\n2: {O1 O2 O3}\n3: {O7 O8 O9}\n", game.table.toString());
        
        // P1 draws O6
        workspace = game.players.get(1).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(1).add(game.stock.draw()); }
        assertEquals(null, workspace);
        assertEquals("1: {B1 B2 B3}\n2: {O1 O2 O3}\n3: {O7 O8 O9}\n", game.table.toString());
        
        // P2 draws B6
        workspace = game.players.get(2).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(2).add(game.stock.draw()); }
        assertEquals(null, workspace);
        assertEquals("1: {B1 B2 B3}\n2: {O1 O2 O3}\n3: {O7 O8 O9}\n", game.table.toString());
        
        // P3 plays initial melds
        workspace = game.players.get(3).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(3).add(game.stock.draw()); }
        assertEquals("1: {B1 B2 B3}\n2: {O1 O2 O3}\n3: {O7 O8 O9}\n4: {B2 B3 B4}\n5: {G7 G8 G9}\n", game.table.toString());
        
        // Human draws B7
        game.players.get(0).add(game.stock.draw());
        assertEquals("1: {B1 B2 B3}\n2: {O1 O2 O3}\n3: {O7 O8 O9}\n4: {B2 B3 B4}\n5: {G7 G8 G9}\n", game.table.toString());
        
        // P1 draws R12
        workspace = game.players.get(1).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(1).add(game.stock.draw()); }
        assertEquals(null, workspace);
        assertEquals("1: {B1 B2 B3}\n2: {O1 O2 O3}\n3: {O7 O8 O9}\n4: {B2 B3 B4}\n5: {G7 G8 G9}\n", game.table.toString());
        
        // P2 draws B12
        workspace = game.players.get(2).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(2).add(game.stock.draw()); }
        assertEquals(null, workspace);
        assertEquals("1: {B1 B2 B3}\n2: {O1 O2 O3}\n3: {O7 O8 O9}\n4: {B2 B3 B4}\n5: {G7 G8 G9}\n", game.table.toString());
        
        // P3 wins using some tiles of the table on its last turn, AND using all the tiles it can since another player has 3 fewer tiles (Player Human) 
        workspace = game.players.get(3).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(3).add(game.stock.draw()); }
        // req. 12b & req. 13b
        assertEquals("1: {B1 B2 B3}\n2: {O1 O2 O3}\n3: {O5 O6 O7 O8 O9 O10 O11}\n4: {B2 B3 B4}\n5: {G7 G8 G9}\n", game.table.toString());
        assertEquals(0, game.players.get(3).getHandSize());
    }
    
    public void testGame8() {
        Hand humanHand = new Hand("R1,R2,R4,R5,R7,R8,R10");
        Hand p1Hand = new Hand("O1,O2,O4,O5,O7,O8,O10");
        Hand p2Hand = new Hand("B1,B2,B4,B5,B7,B8,B10");
        Hand p3Hand = new Hand("R10,R11,R12,G1,G2,G4,G5");
        Player playerHuman = new PlayerHuman("Human");
        Player player1 = new Player1("p1");
        Player player2 = new Player2("p2");
        Player player3 = new Player3("p3");
        playerHuman.setHand(humanHand);
        player1.setHand(p1Hand);
        player2.setHand(p2Hand);
        player3.setHand(p3Hand);
        
        ArrayList<Hand> hands = new ArrayList<>(Arrays.asList(humanHand, p1Hand, p2Hand, p3Hand));
        ArrayList<Player> players = new ArrayList<>(Arrays.asList(playerHuman, player1, player2, player3));
     
        ArrayList<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile("R1"));
        tiles.add(new Tile("R2"));
        tiles.add(new Tile("R3"));
        tiles.add(new Tile("G1"));
        tiles.add(new Tile("G2"));
        tiles.add(new Tile("G3"));
        tiles.add(new Tile("G3"));
        tiles.add(new Tile("O3"));
        tiles.add(new Tile("O4"));
        tiles.add(new Tile("O7"));
        Stock stock = new Stock(tiles);
        stock.remaining(hands);

        Game game = new Game(true);
        game.rig(players, stock);
        game.setup();
        
        ArrayList<Meld> workspace = new ArrayList<>();
        game.players.get(0).add(game.stock.draw());
        
        workspace = game.players.get(1).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(1).add(game.stock.draw()); }
        assertEquals(null, workspace);
        
        workspace = game.players.get(2).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(2).add(game.stock.draw()); }
        assertEquals(null, workspace);

        workspace = game.players.get(3).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(3).add(game.stock.draw()); }
        assertEquals("1: {R10 R11 R12}\n", game.table.toString());
        
        game.players.get(0).add(game.stock.draw());
        
        workspace = game.players.get(1).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(1).add(game.stock.draw()); }
        assertEquals(null, workspace);
        
        workspace = game.players.get(2).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(2).add(game.stock.draw()); }
        assertEquals(null, workspace);
        
        workspace = game.players.get(3).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(3).add(game.stock.draw()); }
        assertEquals(null, workspace);
        
        game.players.get(0).add(game.stock.draw());
        
        workspace = game.players.get(1).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(1).add(game.stock.draw()); }
        assertEquals(null, workspace);
        
        workspace = game.players.get(2).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(2).add(game.stock.draw()); }
        assertEquals(null, workspace);
        
        workspace = game.players.get(3).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(3).add(game.stock.draw()); }
        assertEquals("1: {R10 R11 R12}\n2: {G1 G2 G3 G4 G5}\n", game.table.toString());
        assertEquals(0, game.players.get(3).getHandSize());
        if(game.players.get(3).getHandSize() == 0) { System.out.println("p3 wins"); }
    }
    
    public void testGame9() {
        Hand humanHand = new Hand("B3,B4,B5,B6,B7,B8,B9,B10,B11,B12,B13,B13,O1,G2");
        Hand p1Hand = new Hand("G1,G3,G6,G7,G9,G12,G13,B2,B3,B4,O1,O2,O3,O11");
        Hand p2Hand = new Hand("R2,R3,R4,R5,R6,R7,R8,R9,R10,R11,R12,B1,B2,B3");
        Hand p3Hand = new Hand("G4,G10,G11,G13,B5,B6,B7,B12,O5,O7,O8,O10,O12,O13");
        Player playerHuman = new PlayerHuman("Human");
        Player player1 = new Player1("p1");
        Player player2 = new Player2("p2");
        Player player3 = new Player3("p3");
        playerHuman.setHand(humanHand);
        player1.setHand(p1Hand);
        player2.setHand(p2Hand);
        player3.setHand(p3Hand);
        
        ArrayList<Player> players = new ArrayList<>(Arrays.asList(playerHuman, player1, player2, player3));
        
        ArrayList<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile("G2"));
        tiles.add(new Tile("G3"));
        tiles.add(new Tile("G1"));
        tiles.add(new Tile("B9"));
        Stock stock = new Stock(tiles);

        Game game = new Game(true);
        game.rig(players, stock);
        game.setup();

        ArrayList<Meld> workspace = new ArrayList<>();
        workspace = this.playHuman(game.players.get(0),"B3 B4 B5 B6 B7 B8 B9 B10 B11 B12 B13 > NM", game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(0).add(game.stock.draw()); }
        assertEquals("1: {B3 B4 B5 B6 B7 B8 B9 B10 B11 B12 B13}\n", game.table.toString());

        workspace = game.players.get(1).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(1).add(game.stock.draw()); }
        assertEquals(null, workspace);

        workspace = game.players.get(2).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(2).add(game.stock.draw()); }
        assertEquals("1: {B3 B4 B5 B6 B7 B8 B9 B10 B11 B12 B13}\n2: {R2 R3 R4 R5 R6 R7 R8 R9 R10 R11 R12}\n", game.table.toString());

        workspace = game.players.get(3).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(3).add(game.stock.draw()); }
        assertEquals(null, workspace);

        game.players.get(0).add(stock.draw());

        workspace = game.players.get(1).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(1).add(game.stock.draw()); }
        assertEquals(null, workspace);

        workspace = game.players.get(2).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(2).add(game.stock.draw()); }
        assertEquals("1: {B3 B4 B5 B6 B7 B8 B9 B10 B11 B12 B13}\n2: {R2 R3 R4 R5 R6 R7 R8 R9 R10 R11 R12}\n3: {B1 B2 B3}\n", game.table.toString());  // req. 16, req. 16a
        assertEquals(0, game.players.get(2).getHandSize());
    }
    
    public void testGame10() {
        Hand humanHand = new Hand("B1,B2,B3,O1,O2,O3,O7,O8,O9,O12");
        Hand p1Hand = new Hand("R1,R2,R3,R5,R8,R10,R12,G2,G3,G13");
        Hand p2Hand = new Hand("B2,B3,B4,G7,G8,G9,O5,O6,O10,O11");
        Hand p3Hand = new Hand("B5,G9,O1,O2,O4,O5,O7,O8,O11,O13");
        Player playerHuman = new PlayerHuman("Human");
        Player player1 = new Player1("p1");
        Player player2 = new Player2("p2");
        Player player3 = new Player3("p3");
        playerHuman.setHand(humanHand);
        player1.setHand(p1Hand);
        player2.setHand(p2Hand);
        player3.setHand(p3Hand);
        
        ArrayList<Player> players = new ArrayList<>(Arrays.asList(playerHuman, player1, player2, player3));

        ArrayList<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile("O6"));
        tiles.add(new Tile("B6"));
        tiles.add(new Tile("B7"));
        tiles.add(new Tile("R12"));
        tiles.add(new Tile("B12"));
        Stock stock = new Stock(tiles);

        Game game = new Game(true);
        game.rig(players, stock);
        game.setup();

        ArrayList<Meld> workspace = new ArrayList<>();
        workspace = this.playHuman(game.players.get(0), "B1 B2 B3 > NM", game.table.getState());
        workspace = this.playHuman(game.players.get(0), "O1 O2 O3 > NM", game.table.getState());
        workspace = this.playHuman(game.players.get(0), "O7 O8 O9 > NM", game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(0).add(game.stock.draw()); }
        assertEquals("1: {B1 B2 B3}\n2: {O1 O2 O3}\n3: {O7 O8 O9}\n", game.table.toString());

        // P1 draws O6
        workspace = game.players.get(1).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(1).add(game.stock.draw()); }
        assertEquals(null, workspace);
        assertEquals("1: {B1 B2 B3}\n2: {O1 O2 O3}\n3: {O7 O8 O9}\n", game.table.toString());

        // P2 plays initial melds
        workspace = game.players.get(2).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(2).add(game.stock.draw()); }
        assertEquals("1: {B1 B2 B3}\n2: {O1 O2 O3}\n3: {O7 O8 O9}\n4: {B2 B3 B4}\n5: {G7 G8 G9}\n", game.table.toString());

        // P3 draws B6
        workspace = game.players.get(3).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(3).add(game.stock.draw()); }
        assertEquals(null, workspace);
        assertEquals("1: {B1 B2 B3}\n2: {O1 O2 O3}\n3: {O7 O8 O9}\n4: {B2 B3 B4}\n5: {G7 G8 G9}\n", game.table.toString());

        // Human draws B7
        game.players.get(0).add(game.stock.draw());
        assertEquals("1: {B1 B2 B3}\n2: {O1 O2 O3}\n3: {O7 O8 O9}\n4: {B2 B3 B4}\n5: {G7 G8 G9}\n", game.table.toString());

        // P1 draws R12
        workspace = game.players.get(1).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(1).add(game.stock.draw()); }
        assertEquals(null, workspace);
        assertEquals("1: {B1 B2 B3}\n2: {O1 O2 O3}\n3: {O7 O8 O9}\n4: {B2 B3 B4}\n5: {G7 G8 G9}\n", game.table.toString());

        // P2 wins using some tiles of the table on its last turn
        workspace = game.players.get(2).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(2).add(game.stock.draw()); }
        // req. 16b
        assertEquals("1: {B1 B2 B3}\n2: {O1 O2 O3}\n3: {O5 O6 O7 O8 O9 O10 O11}\n4: {B2 B3 B4}\n5: {G7 G8 G9}\n", game.table.toString());
        assertEquals(0, game.players.get(2).getHandSize());
    }
    
    public void testGame11() {
        Hand humanHand = new Hand("R10,B10,G10,R1,R2,R3,O1,O6");
        Hand p1Hand = new Hand("R11,B11,G11,G1,G2,G3,O2,O5");
        Hand p2Hand = new Hand("R12,B12,G12,B1,B2,B3,O3,O3");
        Hand p3Hand = new Hand("R13,B13,G13,O1,O2,O4,O5,O9");
        Hand p4Hand = new Hand("R9,B9,G9,O9,R1,R2,R3,R4,O1");
        Player playerHuman = new PlayerHuman("Human");
        Player player1 = new Player1("p1");
        Player player2 = new Player2("p2");
        Player player3 = new Player3("p3");
        Player player4 = new Player4("p4");
        playerHuman.setHand(humanHand);
        player1.setHand(p1Hand);
        player2.setHand(p2Hand);
        player3.setHand(p3Hand);
        player4.setHand(p4Hand);
        
        ArrayList<Player> players = new ArrayList<>(Arrays.asList(playerHuman, player1, player2, player3, player4));
        ArrayList<Hand> hands = new ArrayList<>(Arrays.asList(humanHand, p1Hand, p2Hand, p3Hand, p4Hand));
        Stock stock = new Stock();
        stock.remaining(hands);
        
        Game game = new Game(true);
        game.rig(players, stock);
        game.setup();
        
        ArrayList<Meld> workspace = new ArrayList<>();;
        workspace = this.playHuman(game.players.get(0), "R10 B10 G10 > NM", game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(0).add(game.stock.draw()); }
        assertEquals("1: {R10 B10 G10}\n", game.table.toString());
        
        workspace = game.players.get(1).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(1).add(game.stock.draw()); }
        assertEquals("1: {R10 B10 G10}\n2: {R11 B11 G11}\n", game.table.toString());
        
        workspace = game.players.get(2).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(2).add(game.stock.draw()); }
        assertEquals("1: {R10 B10 G10}\n2: {R11 B11 G11}\n3: {R12 B12 G12}\n", game.table.toString());
        
        workspace = game.players.get(3).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(3).add(game.stock.draw()); }
        assertEquals("1: {R10 B10 G10}\n2: {R11 B11 G11}\n3: {R12 B12 G12}\n4: {R13 B13 G13}\n", game.table.toString());
        
        workspace = game.players.get(4).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(4).add(game.stock.draw()); }
        assertEquals("1: {R10 B10 G10}\n2: {R11 B11 G11}\n3: {R12 B12 G12}\n4: {R13 B13 G13}\n5: {R9 B9 G9 O9}\n", game.table.toString());
        
        workspace = game.players.get(1).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(1).add(game.stock.draw()); }
        assertEquals("1: {R10 B10 G10}\n2: {R11 B11 G11}\n3: {R12 B12 G12}\n4: {R13 B13 G13}\n5: {R9 B9 G9 O9}\n6: {G1 G2 G3}\n", game.table.toString());
        
        workspace = game.players.get(2).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(2).add(game.stock.draw()); }
        assertEquals(null, workspace);
        assertEquals("1: {R10 B10 G10}\n2: {R11 B11 G11}\n3: {R12 B12 G12}\n4: {R13 B13 G13}\n5: {R9 B9 G9 O9}\n6: {G1 G2 G3}\n", game.table.toString());
        
        workspace = game.players.get(3).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(3).add(game.stock.draw()); }
        //assertEquals(null, workspace);
        assertEquals("1: {R10 B10 G10}\n2: {R11 B11 G11}\n3: {R12 B12 G12}\n4: {R13 B13 G13}\n5: {R9 B9 G9 O9}\n6: {G1 G2 G3}\n", game.table.toString());
        
        game.players.get(4).setLowestHandCount(3);
        workspace = game.players.get(4).play(game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(4).add(game.stock.draw()); }
        assertEquals("1: {R10 B10 G10}\n2: {R11 B11 G11}\n3: {R12 B12 G12}\n4: {R13 B13 G13}\n5: {R9 B9 G9 O9}\n6: {G1 G2 G3}\n7: {R1 R2 R3}\n", game.table.toString());    // req. 21
    }
    
    // Test case where table state is invalid and reverts back
    public void testGameWithMemento1() {
        Hand humanHand = new Hand("R4,B9,B10");
        Hand p1Hand = new Hand("R1,R2,R3,R4,R5,R6,R7,R8,R9,R10,R11,R12");
        Player playerHuman = new PlayerHuman("Human");
        playerHuman.setInitialMove(false);
        Player player1 = new Player3("p3");
        playerHuman.setHand(humanHand);
        player1.setHand(p1Hand);
        ArrayList<Player> players = new ArrayList<>(Arrays.asList(playerHuman, player1));
        
        ArrayList<Meld> melds = new ArrayList<>();
        Table table = new Table();
        melds.add(new Meld("R1,R2,R3"));
        melds.add(new Meld("G4,O4,B4"));
        melds.add(new Meld("O10,O11,O12"));
        table.setState(melds);
        
        ArrayList<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile("B1"));
        tiles.add(new Tile("B2"));
        tiles.add(new Tile("B3"));
        Stock stock = new Stock(tiles);
        
        Game game = new Game(true);
        game.rig(players, stock);
        game.setup();
        game.table = table;
        
        // Verify table and player hand are reverted and player picks up 3 tiles (B1, B2, B3)
        assertEquals("1: {R1 R2 R3}\n2: {G4 O4 B4}\n3: {O10 O11 O12}\n", game.table.toString());
        assertEquals(3, game.players.get(0).getHandSize());
        
        GameMemento savedState = game.createMemento(game.players.get(0));

        ArrayList<Meld> workspace = new ArrayList<>();
        workspace = this.playHuman(game.players.get(0), "R4 > NM", game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(0).add(game.stock.draw()); }
        
        if (!game.determineValidState()) {
            game.restoreMementoWithPenalty(savedState, game.players.get(0));
        }
        
        assertEquals("1: {R1 R2 R3}\n2: {G4 O4 B4}\n3: {O10 O11 O12}\n", game.table.toString());
        assertEquals(6, game.players.get(0).getHandSize());
        assertEquals(6, game.players.get(1).getLowestHandCount());
    }
    
    // Test case where table state is valid and does not revert back
    public void testGameWithMemento2() {
        Hand humanHand = new Hand("G1,G2,G3,O7");
        Hand p1Hand = new Hand("R1,R2,R3,R4,R5,R6,R7,R8,R9,R10,R11,R12");
        Player playerHuman = new PlayerHuman("Human");
        playerHuman.setInitialMove(false);
        Player player1 = new Player3("p3");
        playerHuman.setHand(humanHand);
        player1.setHand(p1Hand);
        ArrayList<Player> players = new ArrayList<>(Arrays.asList(playerHuman, player1));
        
        ArrayList<Meld> melds = new ArrayList<>();
        Table table = new Table();
        melds.add(new Meld("R1,R2,R3"));
        melds.add(new Meld("G4,O4,B4"));
        melds.add(new Meld("O10,O11,O12"));
        table.setState(melds);
        
        ArrayList<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile("B1"));
        tiles.add(new Tile("B2"));
        tiles.add(new Tile("B3"));
        Stock stock = new Stock(tiles);
        
        Game game = new Game(true);
        game.rig(players, stock);
        game.setup();
        game.table = table;
        
        // Verify table and player hand are not reverted and player does not incur penalty
        assertEquals("1: {R1 R2 R3}\n2: {G4 O4 B4}\n3: {O10 O11 O12}\n", game.table.toString());
        assertEquals(4, game.players.get(0).getHandSize());
        
        GameMemento savedState = game.createMemento(game.players.get(0));
        
        ArrayList<Meld> workspace = new ArrayList<>();
        workspace = this.playHuman(game.players.get(0), "G1 G2 G3 > NM", game.table.getState());
        if (workspace != null) { game.table.setState(workspace); }
        else { game.players.get(0).add(game.stock.draw()); }
        
        if (!game.determineValidState()) {
            game.restoreMementoWithPenalty(savedState, game.players.get(0));
        }
        
        assertEquals("1: {R1 R2 R3}\n2: {G4 O4 B4}\n3: {O10 O11 O12}\n4: {G1 G2 G3}\n", game.table.toString());
        assertEquals(1, game.players.get(0).getHandSize());
        assertEquals(1, game.players.get(1).getLowestHandCount());
    }
}