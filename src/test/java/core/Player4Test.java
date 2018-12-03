package core;

import java.util.ArrayList;

import core.Globals.Colour;
import junit.framework.TestCase;

public class Player4Test extends TestCase {
    // Test case where player plays initial move
    public void testInitialMove1() {
        Player p4 = new Player4();
        ArrayList<Meld> workspace1 = new ArrayList<>();
        workspace1.add(new Meld("G1,G2,G3"));
        Hand hand1 = new Hand("B10,G10,O10,G4");
        p4.setInitialMove(true);
        p4.setHand(hand1);
        assertEquals("[{G1 G2 G3}, {B10 G10 O10}]", p4.play(workspace1).toString());
    }
    
    // Test case where player plays initial move with joker
    public void testInitialMove2() {
        Player p4 = new Player4();
        ArrayList<Meld> workspace1 = new ArrayList<>();
        workspace1.add(new Meld("G1,G2,G3"));
        Hand hand1 = new Hand("R10,R11,J,G4");
        p4.setInitialMove(true);
        p4.setHand(hand1);
        assertEquals("[{G1 G2 G3}, {R10 R11 J}]", p4.play(workspace1).toString());
    }

    public void testRegularMove1() {
        Player p4 = new Player4();
        ArrayList<Meld> workspace2 = new ArrayList<>();
        workspace2.add(new Meld("G1,G2,G3"));
        Hand hand2 = new Hand("R1,R2,R3,R4");
        p4.setInitialMove(false);
        p4.setHand(hand2);
        assertEquals("[{G1 G2 G3}, {R1 R2 R3 R4}]", p4.play(workspace2).toString());
    }

    public void testRegularMove2() {
        Player p4 = new Player4();
        ArrayList<Meld> workspace3 = new ArrayList<>();
        workspace3.add(new Meld("G1,G2,G3"));
        Hand hand3 = new Hand("B10,G10,O10,G4,B10");
        p4.setInitialMove(false);
        p4.setHand(hand3);
        assertEquals("[{G1 G2 G3 G4}, {B10 G10 O10}]", p4.play(workspace3).toString());
    }
    
    // Test case where player plays joker
    public void testRegularMove3() {
        Player p4 = new Player4();
        ArrayList<Meld> workspace3 = new ArrayList<>();
        workspace3.add(new Meld("G1,G2,G3"));
        Hand hand3 = new Hand("B10,G10,O10,G4,B10,J");
        p4.setInitialMove(false);
        p4.setHand(hand3);
        assertEquals("[{G1 G2 G3 G4}, {B10 G10 O10 J}]", p4.play(workspace3).toString());
    }
    
    // Test case where player uses prioritization
    public void testRegularMove4() {
        Player p4 = new Player4();
        ArrayList<Meld> workspace = new ArrayList<>();
        workspace.add(new Meld("R1,R2,R3"));
        workspace.add(new Meld("G2,R2,B2"));
        workspace.add(new Meld("R9,R10,R11"));
        Hand hand = new Hand("R12,B2,G2,O2,B3,B4,B5,B6,O1,O2");
        p4.setInitialMove(false);
        p4.setHand(hand);
        assertEquals("[{R1 R2 R3}, {G2 R2 B2 O2}, {R9 R10 R11 R12}, {B2 G2 O2}, {B3 B4 B5 B6}]", p4.play(workspace).toString());
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
