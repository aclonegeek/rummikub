package core;

import java.util.ArrayList;

import core.Globals.Colour;
import junit.framework.TestCase;

public class Player1Test extends TestCase {
    // Test case where player plays initial move
    public void testInitialMove1() {
        Player p1 = new Player1();
        ArrayList<Meld> workspace1 = new ArrayList<>();
        workspace1.add(new Meld("G1,G2,G3"));
        Hand hand1 = new Hand("B10,G10,O10,G4");
        p1.setInitialMove(true);
        p1.setHand(hand1);
        assertEquals("[{G1 G2 G3}, {B10 G10 O10}]", p1.play(workspace1).toString());
    }
    
    // Test case where player plays initial move with joker
    public void testInitialMove2() {
        Player p1 = new Player1();
        ArrayList<Meld> workspace1 = new ArrayList<>();
        workspace1.add(new Meld("G1,G2,G3"));
        Hand hand1 = new Hand("B10,G10,J,G4");
        p1.setInitialMove(true);
        p1.setHand(hand1);
        assertEquals("[{G1 G2 G3}, {B10 G10 J}]", p1.play(workspace1).toString());
    }

    // Test case where player plays regular move
    public void testRegularMove1() {
        Player p1 = new Player1();
        ArrayList<Meld> workspace2 = new ArrayList<>();
        workspace2.add(new Meld("G1,G2,G3"));
        Hand hand2 = new Hand("B10,G10,O10,G4");
        p1.setInitialMove(false);
        p1.setHand(hand2);
        assertEquals("[{G1 G2 G3 G4}, {B10 G10 O10}]", p1.play(workspace2).toString());
    }

    // Test case where player plays regular move with joker
    public void testRegularMove2() {
        Player p1 = new Player1();
        ArrayList<Meld> workspace2 = new ArrayList<>();
        workspace2.add(new Meld("G1,G2,G3"));
        Hand hand2 = new Hand("B10,G10,J,G4");
        p1.setInitialMove(false);
        p1.setHand(hand2);
        assertEquals("[{G1 G2 G3 G4}, {B10 G10 J}]", p1.play(workspace2).toString());
    }

    // Test case where player plays regular move but cannot play any tiles
    public void testRegularMove3() {
        Player p1 = new Player1();
        ArrayList<Meld> workspace3 = new ArrayList<>();
        workspace3.add(new Meld("G1,G2,G3"));
        Hand hand3 = new Hand("B1,G1,O10,G9");
        p1.setInitialMove(false);
        p1.setHand(hand3);
        assertEquals(null, p1.play(workspace3));
    }
    
    // Test case for simple1 requirement
    public void testRegularMove6() {
        Player p1 = new Player1();
        ArrayList<Meld> workspace4 = new ArrayList<>();
        workspace4.add(new Meld("R9,R10,R11"));
        workspace4.add(new Meld("B9,G9,O9"));
        Hand hand4 = new Hand("R12,B10,B11");
        p1.setInitialMove(false);
        p1.setHand(hand4);
        assertEquals("[{R10 R11 R12}, {G9 O9 R9}, {B9 B10 B11}]", p1.play(workspace4).toString());
    }
    
    // Test case for simple2 requirement
    public void testRegularMove7() {
        Player p1 = new Player1();
        ArrayList<Meld> workspace4 = new ArrayList<>();
        workspace4.add(new Meld("R7,R8,R9,R10,R11,R12"));
        Hand hand4 = new Hand("B10,O10,R13");
        p1.setInitialMove(false);
        p1.setHand(hand4);
        assertEquals("[{R7 R8 R9}, {R11 R12 R13}, {B10 O10 R10}]", p1.play(workspace4).toString());
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
