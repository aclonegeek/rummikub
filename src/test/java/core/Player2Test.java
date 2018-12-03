package core;

import java.util.ArrayList;

import core.Globals.Colour;
import junit.framework.TestCase;

public class Player2Test extends TestCase {
    // Test case where player plays initial move
    public void testInitialMove1() {
        Player p2 = new Player2();
        ArrayList<Meld> workspace1 = new ArrayList<>();
        workspace1.add(new Meld("G1,G2,G3"));
        Hand hand1 = new Hand("B10,G10,O10,G4");
        p2.setInitialMove(true);
        p2.setHand(hand1);
        assertEquals("[{G1 G2 G3}, {B10 G10 O10}]", p2.play(workspace1).toString());
    }

    // Test case where table is empty so player does not play initial move
    public void testInitialMove2() {
        Player p2 = new Player2();
        ArrayList<Meld> workspace2 = new ArrayList<>();
        Hand hand2 = new Hand("B10,G10,O10,G4");
        p2.setInitialMove(true);
        p2.setHand(hand2);
        assertEquals(null, p2.play(workspace2));
    }
    
    // Test case where player plays initial move with joker
    public void testInitialMove3() {
        Player p2 = new Player2();
        ArrayList<Meld> workspace1 = new ArrayList<>();
        workspace1.add(new Meld("G1,G2,G3"));
        Hand hand1 = new Hand("B1,B2,G4,B10,B11,J");
        p2.setInitialMove(true);
        p2.setHand(hand1);
        assertEquals("[{G1 G2 G3}, {B10 B11 J}]", p2.play(workspace1).toString());
    }

    // Test case where player plays regular move and plays tiles to existing melds
    public void testRegularMove1() {
        Player p2 = new Player2();
        ArrayList<Meld> workspace3 = new ArrayList<>();
        workspace3.add(new Meld("G1,G2,G3"));
        Hand hand3 = new Hand("B10,G10,O9,G4");
        p2.setInitialMove(false);
        p2.setHand(hand3);
        assertEquals("[{G1 G2 G3 G4}]", p2.play(workspace3).toString());
    }

    // Test case where player plays regular move and wins (can play new melds from hand)
    public void testRegularMove2() {
        Player p2 = new Player2();
        ArrayList<Meld> workspace4 = new ArrayList<>();
        workspace4.add(new Meld("G1,G2,G3"));
        Hand hand4 = new Hand("B10,G10,O10,G4");
        p2.setInitialMove(false);
        p2.setHand(hand4);
        assertEquals("[{G1 G2 G3 G4}, {B10 G10 O10}]", p2.play(workspace4).toString());
    }

    // Test case where player does not play any tiles
    public void testRegularMove3() {
        Player p2 = new Player2();
        ArrayList<Meld> workspace5 = new ArrayList<>();
        workspace5.add(new Meld("G1,G2,G3"));
        Hand hand5 = new Hand("B10,G10,O10,G5");
        p2.setInitialMove(false);
        p2.setHand(hand5);
        assertEquals(null, p2.play(workspace5));
    }
    
    // Test case where user plays regular move with joker
    public void testRegularMove4() {
        Player p2 = new Player2();
        ArrayList<Meld> workspace4 = new ArrayList<>();
        workspace4.add(new Meld("G1,G2,G3"));
        Hand hand4 = new Hand("B10,G10,J,G4");
        p2.setInitialMove(false);
        p2.setHand(hand4);
        assertEquals("[{G1 G2 G3 G4}, {B10 G10 J}]", p2.play(workspace4).toString());
    }

    public void testToString() {
        Player p2 = new Player2("Player 2");
        assertEquals("Player 2: 0 tiles\n[]\n", p2.toString());
        p2.add(new Tile(Colour.RED, 10));
        p2.add(new Tile(Colour.BLUE, 2));
        p2.add(new Tile(Colour.BLUE, 12));
        p2.add(new Tile(Colour.ORANGE, 2));
        assertEquals("Player 2: 4 tiles\n[R10 B2 B12 O2]\n", p2.toString());
    }
}
