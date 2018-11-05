package core;

import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.TestCase;

public class GameTest extends TestCase {
    private ArrayList<Meld> playHuman(Player human, String command) {
        human.playBehaviour.parseInput(human.hand, command);
        if (human.playBehaviour.workspace != null) {
            human.initialMove = false;
            human.drawing = false;
        } else {
            human.drawing = true;
        }
        human.notifyObservers();
        return human.playBehaviour.workspace;
    }

    public void testGame2() {
        Hand humanHand = new Hand("B1,R6,R13,B2,B8,B10,B11,B12,B13,G5,G8,G9,O3,O13");
        Hand p1Hand = new Hand("R1,R2,R3,R4,R6,R7,R8,G9,O9,B9,O2,O3,O4,O5");
        Hand p2Hand = new Hand("R1,R5,B3,G1,G2,G3,G8,G12,O2,O5,G10,O11,O11,O12");
        Hand p3Hand = new Hand("B9,G7,R10,R11,B1,B5,B6,G6,G10,O1,O7,O10,O12,O13");
        ArrayList<Hand> hands = new ArrayList<>(Arrays.asList(humanHand, p1Hand, p2Hand, p3Hand));
        Stock stock = new Stock();
        stock.remaining(hands);

        Game game = new Game(true);
        game.rig(hands, stock);
        game.setup();

        ArrayList<Meld> workspace;
        workspace = this.playHuman(game.players.get(0), "B10 B11 B12 B13 > NM");
        game.table.setState(workspace);
        assertEquals("1: {B10 B11 B12 B13}\n", game.table.toString());

        workspace = game.players.get(1).play();
        game.table.setState(workspace);
        assertEquals("1: {B10 B11 B12 B13}\n2: {B9 G9 O9}\n3: {R6 R7 R8}\n4: {O2 O3 O4 O5}\n5: {R1 R2 R3 R4}\n", game.table.toString());  // req. 4c, req. 10b
        assertEquals(0, game.players.get(1).getHandSize());
    }

    public void testGame9() {
        Hand humanHand = new Hand("B3,B4,B5,B6,B7,B8,B9,B10,B11,B12,B13,B13,O1,G2");
        Hand p1Hand = new Hand("G1,G3,G6,G7,G9,G12,G13,B2,B3,B4,O1,O2,O3,O11");
        Hand p2Hand = new Hand("R2,R3,R4,R5,R6,R7,R8,R9,R10,R11,R12,B1,B2,B3");
        Hand p3Hand = new Hand("G4,G10,G11,G13,B5,B6,B7,B12,O5,O7,O8,O10,O12,O13");
        ArrayList<Hand> hands = new ArrayList<>(Arrays.asList(humanHand, p1Hand, p2Hand, p3Hand));
        ArrayList<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile("G2"));
        tiles.add(new Tile("G3"));
        tiles.add(new Tile("G1"));
        tiles.add(new Tile("B9"));
        Stock stock = new Stock(tiles);

        Game game = new Game(true);
        game.rig(hands, stock);
        game.setup();

        ArrayList<Meld> workspace;
        workspace = this.playHuman(game.players.get(0),"B3 B4 B5 B6 B7 B8 B9 B10 B11 B12 B13 > NM");
        game.table.setState(workspace);
        assertEquals("1: {B3 B4 B5 B6 B7 B8 B9 B10 B11 B12 B13}\n", game.table.toString());

        workspace = game.players.get(1).play();
        assertEquals(null, workspace);
        game.players.get(1).add(stock.draw());

        workspace = game.players.get(2).play();
        game.table.setState(workspace);
        assertEquals("1: {B3 B4 B5 B6 B7 B8 B9 B10 B11 B12 B13}\n2: {R2 R3 R4 R5 R6 R7 R8 R9 R10 R11 R12}\n", game.table.toString());

        workspace = game.players.get(3).play();
        assertEquals(null, workspace);
        game.players.get(3).add(stock.draw());

        game.players.get(0).add(stock.draw());

        workspace = game.players.get(1).play();
        assertEquals(null, workspace);
        game.players.get(1).add(stock.draw());

        workspace = game.players.get(2).play();
        game.table.setState(workspace);
        assertEquals("1: {B3 B4 B5 B6 B7 B8 B9 B10 B11 B12 B13}\n2: {R2 R3 R4 R5 R6 R7 R8 R9 R10 R11 R12}\n3: {B1 B2 B3}\n", game.table.toString());  // req. 16, req. 16a
        assertEquals(0, game.players.get(2).getHandSize());
    }
    
    public void testGame1() {
        Hand humanHand = new Hand("R1,R11,R13,B2,B3,B9,G1,G8,G9,G11,G12,G13,O1,O7");
        Hand p1Hand = new Hand("R5,R7,R8,R11,R12,B3,B10,B11,G5,G8,G10,O2,O5,O10");
        Hand p2Hand = new Hand("R1,R6,R10,B9,B11,G1,G3,G7,G9,G10,G13,O11,O12,O13");
        Hand p3Hand = new Hand("R2,R3,R4,B8,B12,B13,G4,G5,G6,O1,O2,O3,O9,O12");
        ArrayList<Hand> hands = new ArrayList<>(Arrays.asList(humanHand, p1Hand, p2Hand, p3Hand));
        Stock stock = new Stock();
        stock.remaining(hands);
        
        Game game = new Game(true);
        game.rig(hands, stock);
        game.setup();
        
        ArrayList<Meld> workspace;
        workspace = this.playHuman(game.players.get(0), "G11 G12 G13 > NM");
        workspace = this.playHuman(game.players.get(0), "R1 G1 O1 > NM");
        game.table.setState(workspace);
        assertEquals("1: {G11 G12 G13}\n2: {R1 G1 O1}\n", game.table.toString());   // req. 8e
        
        workspace = game.players.get(1).play();
        game.table.setState(workspace);
        assertEquals("1: {G11 G12 G13}\n2: {R1 G1 O1}\n3: {B10 G10 O10}\n", game.table.toString());     // req. 4a1, 8b, 10a

        workspace = game.players.get(2).play();
        game.table.setState(workspace);
        assertEquals("1: {G11 G12 G13}\n2: {R1 G1 O1}\n3: {B10 G10 O10}\n4: {O11 O12 O13}\n", game.table.toString());   // req. 4a2, 8a
        
        workspace = game.players.get(3).play();
        game.table.setState(workspace);
        assertEquals("1: {G11 G12 G13}\n2: {R1 G1 O1}\n3: {B10 G10 O10}\n4: {O11 O12 O13}\n5: {R2 R3 R4}\n6: {G4 G5 G6}\n7: {O1 O2 O3}\n", game.table.toString());  // req. 4ba, 8c, 11a
    }
    
    public void testGame1b() {
        Hand humanHand = new Hand("R1,R11,R13,B2,B3,B9,G1,G8,G9,G11,G12,G13,O1,O7");
        Hand p1Hand = new Hand("R5,R6,R9,R12,R13,B2,B5,B6,G11,O4,O3,O5,O6,O8");
        Hand p2Hand = new Hand("R1,R6,R10,B9,B11,G1,G3,G7,G9,G10,G13,O11,O12,O13");
        Hand p3Hand = new Hand("R5,R7,R8,R11,R12,B3,B10,B11,G5,G8,G10,O2,O5,O10");
        ArrayList<Hand> hands = new ArrayList<>(Arrays.asList(humanHand, p1Hand, p2Hand, p3Hand));
        Stock stock = new Stock();
        stock.remaining(hands);
        
        Game game = new Game(true);
        game.rig(hands, stock);
        game.setup();
        
        ArrayList<Meld> workspace = new ArrayList<>();
        game.players.get(0).add(game.stock.draw());
        game.table.setState(workspace);
        assertEquals("", game.table.toString());
        
        workspace = game.players.get(1).play();
        game.table.setState(workspace);
        assertEquals("1: {R6 B6 O6}\n2: {R5 B5 O5}\n", game.table.toString());  // req. 4b2, 8d
    }
    
    public void testGame3() {
        Hand humanHand = new Hand("R4,B6,B7,B9,B10,B12,G4,G11,G13,O1,O4,O8,O8,O12");
        Hand p1Hand = new Hand("R2,R6,B1,B2,B3,B8,B12,G8,G12,O2,O3,O6,O11,O11");
        Hand p2Hand = new Hand("R1,R1,R8,R10,R11,R13,B8,B13,G7,G7,G8,O2,O5,O13");
        Hand p3Hand = new Hand("R2,R7,R11,R12,B3,B1,B11,G2,G10,G12,G13,O1,O5,O10");
        ArrayList<Hand> hands = new ArrayList<>(Arrays.asList(humanHand, p1Hand, p2Hand, p3Hand));
     
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
        game.rig(hands, stock);
        game.setup();
        
        // human draws
        ArrayList<Meld> workspace = new ArrayList<>();
        game.players.get(0).add(game.stock.draw());
        game.table.setState(workspace);
        assertEquals("", game.table.toString());

        // p1 draws
        workspace = game.players.get(1).play();
        System.out.println(workspace);
        game.players.get(1).add(game.stock.draw());     // req. 6, 10e
        assertEquals("", game.table.toString());
        
        // p2 has valid melds but does not play because no one else has
        workspace = game.players.get(2).play();
        game.players.get(2).add(game.stock.draw());     // req. 15
        assertEquals("", game.table.toString());
        
        // p3 draws tile
        workspace = game.players.get(3).play();
        game.players.get(3).add(game.stock.draw());
        assertEquals("", game.table.toString());

        workspace = this.playHuman(game.players.get(0), "B9 B10 B11 B12 > NM");
        game.table.setState(workspace);
        assertEquals("1: {B9 B10 B11 B12}\n", game.table.toString());
        
        workspace = game.players.get(1).play();
        game.players.get(1).add(game.stock.draw());     // req. 10f
        assertEquals("1: {B9 B10 B11 B12}\n", game.table.toString());
        
        workspace = game.players.get(2).play();
        game.table.setState(workspace);
        assertEquals("1: {B9 B10 B11 B12}\n2: {R13 B13 O13}\n", game.table.toString()); // req. 15a
        
        workspace = game.players.get(3).play();
        game.table.setState(workspace);
        assertEquals("1: {B9 B10 B11 B12}\n2: {R13 B13 O13}\n3: {G10 G11 G12 G13}\n", game.table.toString());   // req. 11b
        
        workspace = this.playHuman(game.players.get(0), "R4 G4 O4 > NM");
        game.table.setState(workspace);
        assertEquals("1: {B9 B10 B11 B12}\n2: {R13 B13 O13}\n3: {G10 G11 G12 G13}\n4: {R4 G4 O4}\n", game.table.toString());
        
        workspace = game.players.get(1).play();
        game.table.setState(workspace);
        assertEquals("1: {B9 B10 B11 B12}\n2: {R13 B13 O13}\n3: {G10 G11 G12 G13}\n4: {R4 G4 O4}\n5: {R12 B12 G12}\n", game.table.toString());
        
        // p2 draws tile
        workspace = game.players.get(2).play();
        game.players.get(2).add(game.stock.draw());
        assertEquals("1: {B9 B10 B11 B12}\n2: {R13 B13 O13}\n3: {G10 G11 G12 G13}\n4: {R4 G4 O4}\n5: {R12 B12 G12}\n", game.table.toString());  // req. 17b
        
        // p3 tries to play all the tiles it can but cannot so draws tile
        workspace = game.players.get(3).play();
        game.players.get(3).add(game.stock.draw());
        assertEquals("1: {B9 B10 B11 B12}\n2: {R13 B13 O13}\n3: {G10 G11 G12 G13}\n4: {R4 G4 O4}\n5: {R12 B12 G12}\n", game.table.toString());  // req. 13a
    
        // human draws tile
        game.players.get(0).add(game.stock.draw());
        assertEquals("1: {B9 B10 B11 B12}\n2: {R13 B13 O13}\n3: {G10 G11 G12 G13}\n4: {R4 G4 O4}\n5: {R12 B12 G12}\n", game.table.toString());
        
        workspace = game.players.get(1).play();
        game.table.setState(workspace);
        assertEquals("1: {B8 B9 B10 B11 B12}\n2: {R13 B13 O13}\n3: {G10 G11 G12 G13}\n4: {R4 G4 O4}\n5: {R12 B12 G12}\n6: {R2 B2 O2}\n7: {R3 B3 O3}\n", game.table.toString()); // req. 10c
    }
    
    public void testGame4() {
        Hand humanHand = new Hand("B7,B8,B9,B10,G6,G7,O6,O8");
        Hand p1Hand = new Hand("R9,R10,R11,R13,G6,G7,O1,O2");
        Hand p2Hand = new Hand("R8,B13,G13,O13,O1,O2,O4,O5");
        Hand p3Hand = new Hand("B6,B11,G8,G9,G10,G11,O3,O4");
        ArrayList<Hand> hands = new ArrayList<>(Arrays.asList(humanHand, p1Hand, p2Hand, p3Hand));
        Stock stock = new Stock();
        stock.remaining(hands);

        Game game = new Game(true);
        game.rig(hands, stock);
        game.setup();
        
        ArrayList<Meld> workspace;
        workspace = this.playHuman(game.players.get(0), "B7 B8 B9 B10 > NM");
        game.table.setState(workspace);
        assertEquals("1: {B7 B8 B9 B10}\n", game.table.toString());
        
        workspace = game.players.get(1).play();
        game.table.setState(workspace);
        assertEquals("1: {B7 B8 B9 B10}\n2: {R9 R10 R11}\n", game.table.toString());
        
        workspace = game.players.get(2).play();
        game.table.setState(workspace);
        assertEquals("1: {B7 B8 B9 B10}\n2: {R9 R10 R11}\n3: {B13 G13 O13}\n", game.table.toString());
        
        workspace = game.players.get(3).play();
        game.table.setState(workspace);
        assertEquals("1: {B7 B8 B9 B10}\n2: {R9 R10 R11}\n3: {B13 G13 O13}\n4: {G8 G9 G10 G11}\n", game.table.toString());
        
        workspace = this.playHuman(game.players.get(0), "M4G8 > NM");
        workspace = this.playHuman(game.players.get(0), "G7 G6 > M5");
        game.table.setState(workspace);
        assertEquals("1: {B7 B8 B9 B10}\n2: {R9 R10 R11}\n3: {B13 G13 O13}\n4: {G9 G10 G11}\n5: {G6 G7 G8}\n", game.table.toString());  // req. 5, req. 9d
        
        workspace = game.players.get(1).play();
        game.table.setState(workspace);
        assertEquals("1: {B7 B8 B9 B10}\n2: {R9 R10 R11}\n3: {B13 G13 O13 R13}\n4: {G9 G10 G11}\n5: {G6 G7 G8}\n", game.table.toString());  // req. 9b
        
        workspace = game.players.get(2).play();
        game.table.setState(workspace);
        assertEquals("1: {B7 B8 B9 B10}\n2: {R8 R9 R10 R11}\n3: {B13 G13 O13 R13}\n4: {G9 G10 G11}\n5: {G6 G7 G8}\n", game.table.toString());   // req. 9a, 17a
        
        workspace = game.players.get(3).play();
        game.table.setState(workspace);
        assertEquals("1: {B6 B7 B8 B9 B10 B11}\n2: {R8 R9 R10 R11}\n3: {B13 G13 O13 R13}\n4: {G9 G10 G11}\n5: {G6 G7 G8}\n", game.table.toString());    // req. 9c, 14a
    }
    
    public void testGame5() {
        Hand humanHand = new Hand("B12,O12,R12,G12,O4,O6,O8,O10");
        Hand p1Hand = new Hand("R10,G10,B10,R8,R12,G8,O1,O3,O5");
        Hand p2Hand = new Hand("R9,R10,R11,G12,B12,O1,O3,O5");
        Hand p3Hand = new Hand("G9,G10,G11,O2,O4,O6,O8,O11");
        ArrayList<Hand> hands = new ArrayList<>(Arrays.asList(humanHand, p1Hand, p2Hand, p3Hand));
     
        ArrayList<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile("B4"));
        tiles.add(new Tile("R1"));   
        Stock stock = new Stock(tiles);
        stock.remaining(hands);

        Game game = new Game(true);
        game.rig(hands, stock);
        game.setup();
        
        ArrayList<Meld> workspace;
        workspace = this.playHuman(game.players.get(0), "B12 O12 R12 G12 > NM");
        game.table.setState(workspace);
        assertEquals("1: {B12 O12 R12 G12}\n", game.table.toString());
        
        workspace = game.players.get(1).play();
        game.table.setState(workspace);
        assertEquals("1: {B12 O12 R12 G12}\n2: {R10 B10 G10}\n", game.table.toString());
        
        workspace = game.players.get(2).play();
        game.table.setState(workspace);
        assertEquals("1: {B12 O12 R12 G12}\n2: {R10 B10 G10}\n3: {R9 R10 R11}\n", game.table.toString());
        
        workspace = game.players.get(3).play();
        game.table.setState(workspace);
        assertEquals("1: {B12 O12 R12 G12}\n2: {R10 B10 G10}\n3: {R9 R10 R11}\n4: {G9 G10 G11}\n", game.table.toString());
        
        game.players.get(0).add(game.stock.draw());
        game.table.setState(workspace);
        assertEquals("1: {B12 O12 R12 G12}\n2: {R10 B10 G10}\n3: {R9 R10 R11}\n4: {G9 G10 G11}\n", game.table.toString());
        
        workspace = game.players.get(1).play();
        game.table.setState(workspace);
        assertEquals("1: {O12 R12 G12 B12}\n2: {R10 B10 G10}\n3: {R8 R9 R10 R11 R12}\n4: {G8 G9 G10 G11}\n", game.table.toString());    // req. 9f
        
        workspace = game.players.get(2).play();
        game.table.setState(workspace);
        assertEquals("1: {R12 G12 B12}\n2: {R10 B10 G10}\n3: {R8 R9 R10 R11 R12}\n4: {G8 G9 G10 G11}\n5: {B12 G12 O12}\n", game.table.toString());  // req. 9e
    }
    
    public void testGame6() {
        Hand humanHand = new Hand("B1,B3,B4,O1,O2,O3,O7,O8,O9,O10");
        Hand p1Hand = new Hand("R1,R2,R3,R10,R11,R12,R13,G1,G2,G3,O5");
        Hand p2Hand = new Hand("O1,O2,O4,O5,O7,O8,B1,B2,B3,B5");
        Hand p3Hand = new Hand("G9,G10,G11,B10,O1,O2,B3,B5,B6,B8");
        ArrayList<Hand> hands = new ArrayList<>(Arrays.asList(humanHand, p1Hand, p2Hand, p3Hand));
     
        ArrayList<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile("G1"));
        tiles.add(new Tile("G2"));
        Stock stock = new Stock(tiles);
        stock.remaining(hands);

        Game game = new Game(true);
        game.rig(hands, stock);
        game.setup();
        
        ArrayList<Meld> workspace;
        workspace = this.playHuman(game.players.get(0), "O7 O8 O9 O10 > NM");
        game.table.setState(workspace);
        assertEquals("1: {O7 O8 O9 O10}\n", game.table.toString());
        
        workspace = game.players.get(1).play();
        game.table.setState(workspace);
        assertEquals("1: {O7 O8 O9 O10}\n2: {R10 R11 R12 R13}\n", game.table.toString());   
        
        workspace = game.players.get(2).play();
        game.players.get(2).add(game.stock.draw());
        assertEquals("1: {O7 O8 O9 O10}\n2: {R10 R11 R12 R13}\n", game.table.toString());   // req. 15b
        
        workspace = game.players.get(3).play();
        game.table.setState(workspace);
        assertEquals("1: {O7 O8 O9 O10}\n2: {R10 R11 R12 R13}\n3: {G9 G10 G11}\n", game.table.toString());
        
        workspace = this.playHuman(game.players.get(0), "O1 O2 O3 > NM");
        game.table.setState(workspace);
        assertEquals("1: {O7 O8 O9 O10}\n2: {R10 R11 R12 R13}\n3: {G9 G10 G11}\n4: {O1 O2 O3}\n", game.table.toString());
        
        workspace = game.players.get(1).play();
        game.table.setState(workspace);
        assertEquals("1: {O7 O8 O9 O10}\n2: {R10 R11 R12 R13}\n3: {G9 G10 G11}\n4: {O1 O2 O3}\n5: {R1 R2 R3}\n6: {G1 G2 G3}\n", game.table.toString());     // req. 10d
        
        workspace = game.players.get(2).play();
        game.players.get(2).add(game.stock.draw());
        assertEquals("1: {O7 O8 O9 O10}\n2: {R10 R11 R12 R13}\n3: {G9 G10 G11}\n4: {O1 O2 O3}\n5: {R1 R2 R3}\n6: {G1 G2 G3}\n", game.table.toString());
        
        workspace = game.players.get(3).play();
        game.table.setState(workspace);
        assertEquals("1: {O7 O8 O9}\n2: {R11 R12 R13}\n3: {G9 G10 G11}\n4: {O1 O2 O3}\n5: {R1 R2 R3}\n6: {G1 G2 G3}\n7: {B10 O10 R10}\n", game.table.toString());   // req. 9g
    }
    
    public void testGame8() {
        Hand humanHand = new Hand("R1,R2,R4,R5,R7,R8,R10");
        Hand p1Hand = new Hand("O1,O2,O4,O5,O7,O8,O10");
        Hand p2Hand = new Hand("B1,B2,B4,B5,B7,B8,B10");
        Hand p3Hand = new Hand("R10,R11,R12,G1,G2,G4,G5");
        ArrayList<Hand> hands = new ArrayList<>(Arrays.asList(humanHand, p1Hand, p2Hand, p3Hand));
     
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
        game.rig(hands, stock);
        game.setup();
        
        ArrayList<Meld> workspace = new ArrayList<>();
        game.players.get(0).add(game.stock.draw());
        
        workspace = game.players.get(1).play();
        game.players.get(1).add(game.stock.draw());
        
        workspace = game.players.get(2).play();
        game.players.get(2).add(game.stock.draw());

        workspace = game.players.get(3).play();
        game.table.setState(workspace);
        assertEquals("1: {R10 R11 R12}\n", game.table.toString());
        
        game.players.get(0).add(game.stock.draw());
        
        workspace = game.players.get(1).play();
        game.players.get(1).add(game.stock.draw());
        
        workspace = game.players.get(2).play();
        game.players.get(2).add(game.stock.draw());
        
        workspace = game.players.get(3).play();
        game.players.get(3).add(game.stock.draw());
        
        game.players.get(0).add(game.stock.draw());
        
        workspace = game.players.get(1).play();
        game.players.get(1).add(game.stock.draw());
        
        workspace = game.players.get(2).play();
        game.players.get(2).add(game.stock.draw());
        
        workspace = game.players.get(3).play();
        game.table.setState(workspace);
        assertEquals("1: {R10 R11 R12}\n2: {G1 G2 G3 G4 G5}\n", game.table.toString());
        assertEquals(0, game.players.get(3).getHandSize());
        if(game.players.get(3).getHandSize() == 0) { System.out.println("p3 wins"); }
    }
    
    public void testGame11() {
        Hand humanHand = new Hand("R10,B10,G10,R1,R2,R3,O1,O6");
        Hand p1Hand = new Hand("R11,B11,G11,G1,G2,G3,O2,O5");
        Hand p2Hand = new Hand("R12,B12,G12,B1,B2,B3,O3,O3");
        Hand p3Hand = new Hand("R13,B13,G13,O1,O2,O4,O5,O9");
        Hand p4Hand = new Hand("R9,B9,G9,O9,R1,R2,R3,R4,O1");
        ArrayList<Hand> hands = new ArrayList<>(Arrays.asList(humanHand, p1Hand, p2Hand, p3Hand, p4Hand));
        Stock stock = new Stock();
        stock.remaining(hands);
        
        Game game = new Game(true, true);
        game.rig(hands, stock);
        game.setup();
        
        ArrayList<Meld> workspace;
        workspace = this.playHuman(game.players.get(0), "R10 B10 G10 > NM");
        game.table.setState(workspace);
        assertEquals("1: {R10 B10 G10}\n", game.table.toString());
        
        workspace = game.players.get(1).play();
        game.table.setState(workspace);
        assertEquals("1: {R10 B10 G10}\n2: {R11 B11 G11}\n", game.table.toString());
        
        workspace = game.players.get(2).play();
        game.table.setState(workspace);
        assertEquals("1: {R10 B10 G10}\n2: {R11 B11 G11}\n3: {R12 B12 G12}\n", game.table.toString());
        
        workspace = game.players.get(3).play();
        game.table.setState(workspace);
        assertEquals("1: {R10 B10 G10}\n2: {R11 B11 G11}\n3: {R12 B12 G12}\n4: {R13 B13 G13}\n", game.table.toString());
        
        workspace = game.players.get(4).play();
        game.table.setState(workspace);
        assertEquals("1: {R10 B10 G10}\n2: {R11 B11 G11}\n3: {R12 B12 G12}\n4: {R13 B13 G13}\n5: {R9 B9 G9 O9}\n", game.table.toString());
        
        workspace = game.players.get(1).play();
        game.table.setState(workspace);
        assertEquals("1: {R10 B10 G10}\n2: {R11 B11 G11}\n3: {R12 B12 G12}\n4: {R13 B13 G13}\n5: {R9 B9 G9 O9}\n6: {G1 G2 G3}\n", game.table.toString());
        
        workspace = game.players.get(2).play();
        game.players.get(2).add(game.stock.draw());
        assertEquals("1: {R10 B10 G10}\n2: {R11 B11 G11}\n3: {R12 B12 G12}\n4: {R13 B13 G13}\n5: {R9 B9 G9 O9}\n6: {G1 G2 G3}\n", game.table.toString());
        
        workspace = game.players.get(3).play();
        game.players.get(3).add(game.stock.draw());
        assertEquals("1: {R10 B10 G10}\n2: {R11 B11 G11}\n3: {R12 B12 G12}\n4: {R13 B13 G13}\n5: {B9 G9 O9 R9}\n6: {G1 G2 G3}\n", game.table.toString());
        
        game.players.get(4).setLowestHandCount(3);
        workspace = game.players.get(4).play();
        game.table.setState(workspace);
        assertEquals("1: {R10 B10 G10}\n2: {R11 B11 G11}\n3: {R12 B12 G12}\n4: {R13 B13 G13}\n5: {B9 G9 O9 R9}\n6: {G1 G2 G3}\n7: {R1 R2 R3}\n", game.table.toString());    // req. 21
    }
}
