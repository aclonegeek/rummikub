package core;

import java.util.ArrayList;

import junit.framework.TestCase;

public class Strategy2Test extends TestCase {
    // Test first move with one meld >= 30
    public void testDetermineInitialMove2() {
        Strategy2 strategy2 = new Strategy2();
        ArrayList<Meld> workspace = new ArrayList<>();
        Meld meld1 = new Meld("R1,R2,R3");
        Meld meld2 = new Meld("R10,O10,G10,B10");
        Meld meld3 = new Meld("B5,B6,B7,B8");
        workspace.add(meld1);
        workspace.add(meld2);
        workspace.add(meld3);
        assertEquals("[{R1 R2 R3}, {R10 O10 G10 B10}, {B5 B6 B7 B8}]", workspace.toString());

        Hand hand = new Hand("G10,O10,B10,R10,R8,R7,R6,R5,R4");
        assertEquals("[{R1 R2 R3}, {R10 O10 G10 B10}, {B5 B6 B7 B8}, {R4 R5 R6 R7 R8}]", strategy2.determineInitialMove(hand, workspace).toString());
    }

    // Test first move such that multiple melds total >= 30, should use as few melds as possible
    public void testDetermineInitialMove3() {
        Strategy2 strategy2 = new Strategy2();
        ArrayList<Meld> workspace = new ArrayList<>();
        Meld meld1 = new Meld("R1,R2,R3");
        Meld meld2 = new Meld("R10,O10,G10,B10");
        Meld meld3 = new Meld("B5,B6,B7,B8");
        workspace.add(meld1);
        workspace.add(meld2);
        workspace.add(meld3);
        assertEquals("[{R1 R2 R3}, {R10 O10 G10 B10}, {B5 B6 B7 B8}]", workspace.toString());

        Hand hand = new Hand("R3,R4,R5,G5,G6,G7,O2,O3,O4,B1");
        assertEquals("[{R1 R2 R3}, {R10 O10 G10 B10}, {B5 B6 B7 B8}, {R3 R4 R5}, {G5 G6 G7}]", strategy2.determineInitialMove(hand, workspace).toString());
    }

    // Test case where melds are being constructed but hand size reaches 0 (needed for full coverage)
    public void testDetermineInitialMove4() {
        Strategy2 strategy2 = new Strategy2();
        ArrayList<Meld> workspace = new ArrayList<>();
        Meld meld1 = new Meld("R1,R2,R3");
        Meld meld2 = new Meld("R10,O10,G10,B10");
        Meld meld3 = new Meld("B5,B6,B7,B8");
        workspace.add(meld1);
        workspace.add(meld2);
        workspace.add(meld3);
        assertEquals("[{R1 R2 R3}, {R10 O10 G10 B10}, {B5 B6 B7 B8}]", workspace.toString());

        Hand hand = new Hand("R3,R4,R5,G1,G2,G3");
        assertEquals(null, strategy2.determineInitialMove(hand, workspace));
    }

    // Test first move where no combination of melds totals >= 30
    public void testDetermineInitialMove5() {
        Strategy2 strategy2 = new Strategy2();
        ArrayList<Meld> workspace = new ArrayList<>();
        Meld meld1 = new Meld("R1,R2,R3");
        Meld meld2 = new Meld("R10,O10,G10,B10");
        Meld meld3 = new Meld("B5,B6,B7,B8");
        workspace.add(meld1);
        workspace.add(meld2);
        workspace.add(meld3);
        assertEquals("[{R1 R2 R3}, {R10 O10 G10 B10}, {B5 B6 B7 B8}]", workspace.toString());

        Hand hand = new Hand("G1,G2,G3,G4,G8,O10,B2,R9,R4,G5,O2,R11,R12,B7");
        assertEquals(null, strategy2.determineInitialMove(hand, workspace));
    }

    // Plays all its tiles and wins
    public void testDetermineRegularMove1() {
        Strategy2 strategy2 = new Strategy2();
        ArrayList<Meld> workspace = new ArrayList<>();
        Meld meld1 = new Meld("R1,R2,R3");
        workspace.add(meld1);
        assertEquals("[{R1 R2 R3}]", workspace.toString());

        Hand hand = new Hand("G1,G2,G3,G4");
        assertEquals("[{R1 R2 R3}, {G1 G2 G3 G4}]", strategy2.determineRegularMove(hand, workspace).toString());
        assertEquals(0, hand.getSize());
    }

    // Doesn't play because it can't win on this turn (can't create new meld G1,G2,G3,G4)
    public void testDetermineRegularMove2() {
        Strategy2 strategy2 = new Strategy2();
        ArrayList<Meld> workspace = new ArrayList<>();
        Meld meld1 = new Meld("R1,R2,R3");
        workspace.add(meld1);
        assertEquals("[{R1 R2 R3}]", workspace.toString());

        Hand hand = new Hand("G1,G2,G3,G4,G8");
        assertEquals(null, strategy2.determineRegularMove(hand, workspace));
        assertEquals(5, hand.getSize());
    }

    // Plays melds using tiles on table with no tiles left over
    public void testDetermineRegularMove4() {
        Strategy2 strategy2 = new Strategy2();
        ArrayList<Meld> workspace = new ArrayList<>();

        workspace.add(new Meld("R1,R2,R3,R4,R5,R6,R7"));
        workspace.add(new Meld("G2,G3,G4,G5"));
        workspace.add(new Meld("G3,R3,B3,O3"));
        assertEquals("[{R1 R2 R3 R4 R5 R6 R7}, {G2 G3 G4 G5}, {G3 R3 B3 O3}]", workspace.toString());

        Hand hand = new Hand("R2,R3,R8,G1");
        assertEquals("[{R2 R3 R4 R5 R6 R7 R8}, {G3 G4 G5}, {R3 B3 O3}, {R1 R2 R3}, {G1 G2 G3}]", strategy2.determineRegularMove(hand, workspace).toString());
        assertEquals(0, hand.getSize());
    }

    // Plays melds using tiles on table with tiles left over
    public void testDetermineRegularMove5() {
        Strategy2 strategy2 = new Strategy2();
        ArrayList<Meld> workspace = new ArrayList<>();

        workspace.add(new Meld("R1,R2,R3,R4,R5,R6,R7"));
        workspace.add(new Meld("G2,G3,G4,G5"));
        workspace.add(new Meld("G3,R3,B3,O3"));
        assertEquals("[{R1 R2 R3 R4 R5 R6 R7}, {G2 G3 G4 G5}, {G3 R3 B3 O3}]", workspace.toString());

        Hand hand = new Hand("R2,R3,R8,G1,O7");
        assertEquals("[{R2 R3 R4 R5 R6 R7 R8}, {G3 G4 G5}, {R3 B3 O3}, {R1 R2 R3}, {G1 G2 G3}]", strategy2.determineRegularMove(hand, workspace).toString());
        assertEquals(1, hand.getSize());
    }
}
