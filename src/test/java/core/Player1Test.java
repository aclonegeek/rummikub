package core;

import java.util.ArrayList;

import core.Globals.Colour;
import junit.framework.TestCase;

public class Player1Test extends TestCase {
    // Test case where player plays initial move
    public void testPlay1() {
        Player p1 = new Player1();
        ArrayList<Meld> workspace1 = new ArrayList<>();
        workspace1.add(new Meld("G1,G2,G3"));
        Hand hand1 = new Hand("B10,G10,O10,G4");
        p1.setInitialMove(true);
        p1.setHand(hand1);
        assertEquals("[{G1 G2 G3}, {B10 G10 O10}]", p1.play(workspace1).toString());
    }

    // Test case where player plays regular move
    public void testPlay2() {
        Player p1 = new Player1();
        ArrayList<Meld> workspace2 = new ArrayList<>();
        workspace2.add(new Meld("G1,G2,G3"));
        Hand hand2 = new Hand("B10,G10,O10,G4");
        p1.setInitialMove(false);
        p1.setHand(hand2);
        assertEquals("[{G1 G2 G3 G4}, {B10 G10 O10}]", p1.play(workspace2).toString());
    }


    // Test case where player plays regular move but cannot play any tiles
    public void testPlay3() {
        Player p1 = new Player1();
        ArrayList<Meld> workspace3 = new ArrayList<>();
        workspace3.add(new Meld("G1,G2,G3"));
        Hand hand3 = new Hand("B1,G1,O10,G9");
        p1.setInitialMove(false);
        p1.setHand(hand3);
        assertEquals(null, p1.play(workspace3));
    }

    public void testToString() {
        Player p1 = new Player1("Player 1");
        assertEquals("Player 1: 0 tiles\n[]\n", p1.toString());
        p1.add(new Tile(Colour.GREEN, 1));
        p1.add(new Tile(Colour.ORANGE, 5));
        p1.add(new Tile(Colour.BLUE, 12));
        assertEquals("Player 1: 3 tiles\n[B12 G1 O5]\n", p1.toString());
    }
}
