package core;

import java.util.ArrayList;

import core.Globals.Colour;
import junit.framework.TestCase;

public class Player4Test extends TestCase {
    // Test case where player plays initial move
    public void testPlay() {
        Player p4 = new Player4();
        ArrayList<Meld> workspace1 = new ArrayList<>();
        workspace1.add(new Meld("G1,G2,G3"));
        Hand hand1 = new Hand("B10,G10,O10,G4");
        p4.setInitialMove(true);
        p4.setWorkspace(workspace1);
        p4.setHand(hand1);
        assertEquals("[{G1 G2 G3}, {B10 G10 O10}]", p4.play().toString());
    }

    // Test case where player uses Strategy2 (only plays to existing melds), ie. no other player has 3 fewer tiles
    public void testPlay2() {
        Player p4 = new Player4();
        ArrayList<Meld> workspace2 = new ArrayList<>();
        workspace2.add(new Meld("G1,G2,G3"));
        Hand hand2 = new Hand("R1,R2,R3,R4");
        p4.setInitialMove(false);
        p4.setLowestHandCount(4);
        p4.setWorkspace(workspace2);
        p4.setHand(hand2);
        assertEquals("[{G1 G2 G3}, {R1 R2 R3}]", p4.play().toString());
    }

    // Test case where player uses Strategy1 (plays all possible melds), ie. some player has 3 or more fewer tiles
    public void testPlay3() {
        Player p4 = new Player4();
        ArrayList<Meld> workspace3 = new ArrayList<>();
        workspace3.add(new Meld("G1,G2,G3"));
        Hand hand3 = new Hand("B10,G10,O10,G4,B10");
        p4.setInitialMove(false);
        p4.setLowestHandCount(1);
        p4.setWorkspace(workspace3);
        p4.setHand(hand3);
        assertEquals("[{G1 G2 G3 G4}, {B10 G10 O10}]", p4.play().toString());
    }

    // Test case where player does not play any tiles
    public void testPlay4() {
        Player p4 = new Player4();
        ArrayList<Meld> workspace5 = new ArrayList<>();
        workspace5.add(new Meld("G1,G2,G3"));
        Hand hand5 = new Hand("B10,G10,O10,R10,R5,R6,R7,R8");
        p4.setInitialMove(false);
        p4.setWorkspace(workspace5);
        p4.setLowestHandCount(8);
        p4.setHand(hand5);
        assertEquals(null, p4.play());
    }

    public void testToString() {
        Player p4 = new Player4("Player 4");
        assertEquals("Player 4: 0 tiles\n[]\n", p4.toString());
        p4.add(new Tile(Colour.RED, 1));
        p4.add(new Tile(Colour.ORANGE, 12));
        p4.add(new Tile(Colour.BLUE, 6));
        p4.add(new Tile(Colour.ORANGE, 2));
        p4.add(new Tile(Colour.ORANGE, 2));
        assertEquals("Player 4: 5 tiles\n[R1 B6 O2 O2 O12]\n", p4.toString());
    }
}
