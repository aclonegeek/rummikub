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
}
