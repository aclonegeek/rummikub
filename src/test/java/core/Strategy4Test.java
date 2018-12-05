package core;

import java.util.ArrayList;

import junit.framework.TestCase;

public class Strategy4Test extends TestCase {
    // Test first move with one meld >= 30
    public void testDetermineInitialMove2() {
        Strategy4 strategy4 = new Strategy4();
        ArrayList<Meld> workspace = new ArrayList<>();
        Meld meld1 = new Meld("R1,R2,R3");
        Meld meld2 = new Meld("R10,O10,G10,B10");
        Meld meld3 = new Meld("B5,B6,B7,B8");
        workspace.add(meld1);
        workspace.add(meld2);
        workspace.add(meld3);
        assertEquals("[{R1 R2 R3}, {R10 O10 G10 B10}, {B5 B6 B7 B8}]", workspace.toString());

        Hand hand = new Hand("G10,O10,B10,R10,R8,R7,R6,R5,R4");
        assertEquals("[{R1 R2 R3}, {R10 O10 G10 B10}, {B5 B6 B7 B8}, {R4 R5 R6 R7 R8}]", strategy4.determineInitialMove(hand, workspace).toString());
    }

    // Test first move such that multiple melds total >= 30, should use as few melds as possible
    public void testDetermineInitialMove3() {
        Strategy4 strategy4 = new Strategy4();
        ArrayList<Meld> workspace = new ArrayList<>();
        Meld meld1 = new Meld("R1,R2,R3");
        Meld meld2 = new Meld("R10,O10,G10,B10");
        Meld meld3 = new Meld("B5,B6,B7,B8");
        workspace.add(meld1);
        workspace.add(meld2);
        workspace.add(meld3);
        assertEquals("[{R1 R2 R3}, {R10 O10 G10 B10}, {B5 B6 B7 B8}]", workspace.toString());

        Hand hand = new Hand("R3,R4,R5,G5,G6,G7,O2,O3,O4,B1");
        assertEquals("[{R1 R2 R3}, {R10 O10 G10 B10}, {B5 B6 B7 B8}, {R3 R4 R5}, {G5 G6 G7}]", strategy4.determineInitialMove(hand, workspace).toString());
    }

    // Test case where melds are being constructed but hand size reaches 0 (needed for full coverage)
    public void testDetermineInitialMove4() {
        Strategy4 strategy4 = new Strategy4();
        ArrayList<Meld> workspace = new ArrayList<>();
        Meld meld1 = new Meld("R1,R2,R3");
        Meld meld2 = new Meld("R10,O10,G10,B10");
        Meld meld3 = new Meld("B5,B6,B7,B8");
        workspace.add(meld1);
        workspace.add(meld2);
        workspace.add(meld3);
        assertEquals("[{R1 R2 R3}, {R10 O10 G10 B10}, {B5 B6 B7 B8}]", workspace.toString());

        Hand hand = new Hand("R3,R4,R5,G1,G2,G3");
        assertEquals(null, strategy4.determineInitialMove(hand, workspace));
    }

    // Test first move where no combination of melds totals >= 30
    public void testDetermineInitialMove5() {
        Strategy4 strategy4 = new Strategy4();
        ArrayList<Meld> workspace = new ArrayList<>();
        Meld meld1 = new Meld("R1,R2,R3");
        Meld meld2 = new Meld("R10,O10,G10,B10");
        Meld meld3 = new Meld("B5,B6,B7,B8");
        workspace.add(meld1);
        workspace.add(meld2);
        workspace.add(meld3);
        assertEquals("[{R1 R2 R3}, {R10 O10 G10 B10}, {B5 B6 B7 B8}]", workspace.toString());

        Hand hand = new Hand("G1,G2,G3,G4,G8,O10,B2,R9,R4,G5,O2,R11,R12,B7");
        assertEquals(null, strategy4.determineInitialMove(hand, workspace));
    }
    
    // Test first move with joker
    public void testDetermineInitialMove6() {
        Strategy4 strategy4 = new Strategy4();
        ArrayList<Meld> workspace = new ArrayList<>();
        Meld meld1 = new Meld("R1,R2,R3");
        Meld meld2 = new Meld("R10,O10,G10,B10");
        Meld meld3 = new Meld("B5,B6,B7,B8");
        workspace.add(meld1);
        workspace.add(meld2);
        workspace.add(meld3);
        assertEquals("[{R1 R2 R3}, {R10 O10 G10 B10}, {B5 B6 B7 B8}]", workspace.toString());

        Hand hand = new Hand("J,R4,R5,G5,G6,G7,O2,O3,O4,B1");
        assertEquals("[{R1 R2 R3}, {R10 O10 G10 B10}, {B5 B6 B7 B8}, {G5 G6 G7 J}, {O2 O3 O4}]", strategy4.determineInitialMove(hand, workspace).toString());
    }

    public void testDetermineRegularMove1() {
        Strategy4 strategy4 = new Strategy4();
        ArrayList<Meld> workspace = new ArrayList<>();
        Meld meld1 = new Meld("R1,R2,R3");
        workspace.add(meld1);
        assertEquals("[{R1 R2 R3}]", workspace.toString());

        Hand hand = new Hand("G1,G2,G3,G4");
        assertEquals("[{R1 R2 R3}, {G1 G2 G3 G4}]", strategy4.determineRegularMove(hand, workspace).toString());
    }

    public void testDetermineRegularMove2() {
        Strategy4 strategy4 = new Strategy4();
        ArrayList<Meld> workspace = new ArrayList<>();
        Meld meld1 = new Meld("R1,R2,R3");
        workspace.add(meld1);
        assertEquals("[{R1 R2 R3}]", workspace.toString());

        Hand hand = new Hand("B2,G3,O1");
        assertEquals(null, strategy4.determineRegularMove(hand, workspace));
        assertEquals("[B2 G3 O1]", hand.toString());
    }
    
    public void testDetermineRegularMove3() {
        Strategy4 strategy4 = new Strategy4();
        ArrayList<Meld> workspace = new ArrayList<>();
        Meld meld1 = new Meld("R1,R2,R3");
        workspace.add(meld1);
        assertEquals("[{R1 R2 R3}]", workspace.toString());

        Hand hand = new Hand("G1,G2,G3,J");
        assertEquals("[{R1 R2 R3}, {G1 G2 G3 J}]", strategy4.determineRegularMove(hand, workspace).toString());
    }
    
    // Determine regular move with set prioritization, then playing a run, then adding to an existing run and set
    public void testDetermineRegularMove4() {
        Strategy4 strategy4 = new Strategy4();
        ArrayList<Meld> workspace = new ArrayList<>();
        workspace.add(new Meld("R1,R2,R3"));
        workspace.add(new Meld("G2,R2,B2"));
        workspace.add(new Meld("R9,R10,R11"));
        
        Hand hand = new Hand("R12,B2,G2,O2,B3,B4,B5,B6,O1,O2");
        assertEquals("[{R1 R2 R3}, {G2 R2 B2 O2}, {R9 R10 R11 R12}, {B2 G2 O2}, {B3 B4 B5 B6}]", strategy4.determineRegularMove(hand, workspace).toString());
    }
    
    // Determine regular move with set prioritization, where doing so means the other run cannot be played
    public void testDetermineRegularMove5() {
        Strategy4 strategy4 = new Strategy4();
        ArrayList<Meld> workspace = new ArrayList<>();
        workspace.add(new Meld("R1,R2,R3"));
        workspace.add(new Meld("G2,R2,B2"));
        
        Hand hand = new Hand("O2,G2,B2,B3,B4");
        assertEquals("[{R1 R2 R3}, {G2 R2 B2}, {B2 G2 O2}]", strategy4.determineRegularMove(hand, workspace).toString());
        assertEquals("[B3 B4]", hand.toString());
    }
}
