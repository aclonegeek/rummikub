package core;

import java.util.ArrayList;

import junit.framework.TestCase;

public class PlayBehaviourTest extends TestCase {
    public void testCreateMeldsFromHand() {
        PlayBehaviour strategy1 = new Strategy1();

        Hand hand1 = new Hand();
        assertEquals("[]", strategy1.createMeldsFromHand(hand1).toString());

        Hand hand2 = new Hand("R1,R2,R3,G2,B2");
        assertEquals("[{R1 R2 R3}, {R2 B2 G2}]", strategy1.createMeldsFromHand(hand2).toString());

        Hand hand3 = new Hand("R1,R2,R5,R6,R7,R10,R11,R12,G1,G2,G3,G11,G13,O6,O7,O10,B1,B6,B11");
        assertEquals("[{R1 B1 G1}, {R5 R6 R7}, {R6 B6 O6}, {R10 R11 R12}, {R11 B11 G11}, {G1 G2 G3}]",
                     strategy1.createMeldsFromHand(hand3).toString());

        Hand hand4 = new Hand("R5,R6,R7,R6,B6,O6,O6,B11,B12,B13,O1,O2,O3,O3,O4,O5,O7,G4,G5,G6,G11,R11,O11");
        assertEquals("[{R5 R6 R7}, {R5 G5 O5}, {R6 B6 G6}, {R6 B6 O6}, {R6 G6 O6}, {R6 B6 G6 O6}, {R11 B11 G11}, {R11 B11 O11}, {R11 G11 O11}, {R11 B11 G11 O11}, {B6 G6 O6}, {B11 B12 B13}, {B11 G11 O11}, {G4 G5 G6}, {O1 O2 O3}, {O1 O2 O3 O4}, {O1 O2 O3 O4 O5}, {O1 O2 O3 O4 O5 O6}, {O1 O2 O3 O4 O5 O6 O7}, {O2 O3 O4}, {O2 O3 O4 O5}, {O2 O3 O4 O5 O6}, {O2 O3 O4 O5 O6 O7}, {O3 O4 O5}, {O3 O4 O5 O6}, {O3 O4 O5 O6 O7}, {O4 O5 O6}, {O4 O5 O6 O7}, {O5 O6 O7}]",
                      strategy1.createMeldsFromHand(hand4).toString());

        Hand hand5 = new Hand("R1,R2,R3,R4,R5,R6,R7");
        assertEquals("[{R1 R2 R3}, {R1 R2 R3 R4}, {R1 R2 R3 R4 R5}, {R1 R2 R3 R4 R5 R6}, {R1 R2 R3 R4 R5 R6 R7}, {R2 R3 R4}, {R2 R3 R4 R5}, {R2 R3 R4 R5 R6}, {R2 R3 R4 R5 R6 R7}, {R3 R4 R5}, {R3 R4 R5 R6}, {R3 R4 R5 R6 R7}, {R4 R5 R6}, {R4 R5 R6 R7}, {R5 R6 R7}]",
                     strategy1.createMeldsFromHand(hand5).toString());
    }

    public void testCreatePotentialMeldsFromHand() {
        PlayBehaviour strategy1 = new Strategy1();

        Hand hand1 = new Hand();
        assertEquals("[]", strategy1.createPotentialMeldsFromHand(hand1).toString());

        Hand hand2 = new Hand("R1,R2");
        assertEquals("[{R1 R2}]", strategy1.createPotentialMeldsFromHand(hand2).toString());

        Hand hand3 = new Hand("R1,R2,R3");
        assertEquals("[{R1 R2}, {R2 R3}]", strategy1.createPotentialMeldsFromHand(hand3).toString());

        Hand hand4 = new Hand("R1,O1,B1");
        assertEquals("[{R1 B1}, {R1 O1}, {B1 O1}]", strategy1.createPotentialMeldsFromHand(hand4).toString());
    }

    public void testGetLargestMeldOver30() {
        PlayBehaviour strategy1 = new Strategy1();

        // Case where there are no melds
        ArrayList<Meld> melds1 = new ArrayList<>();
        assertEquals(null, strategy1.getLargestMeldOver30(melds1));

        // Case where there are melds but none >= 30
        ArrayList<Meld> melds2 = new ArrayList<>();
        Meld meld1 = new Meld("R8,G8,B8");
        Meld meld2 = new Meld("O7,O8,O9");
        melds2.add(meld1);
        melds2.add(meld2);
        assertEquals(null, strategy1.getLargestMeldOver30(melds2));

        // Case where one meld has value of 30
        ArrayList<Meld> melds3 = new ArrayList<>();
        Meld meld3 = new Meld("R8,G8,B8");
        Meld meld4 = new Meld("O10,B10,G10");
        melds3.add(meld3);
        melds3.add(meld4);
        assertEquals("{O10 B10 G10}", strategy1.getLargestMeldOver30(melds3).toString());

        // Case where multiple melds have values >= 30, chooses largest instead of greatest valued
        ArrayList<Meld> melds4 = new ArrayList<>();
        Meld meld5 = new Meld("R9,R10,R11,R12");
        Meld meld6 = new Meld("O10,B10,G10,R10");
        Meld meld7 = new Meld("G8,G7,G6,G5,G4,G3");
        melds4.add(meld5);
        melds4.add(meld6);
        melds4.add(meld7);
        assertEquals("{G3 G4 G5 G6 G7 G8}", strategy1.getLargestMeldOver30(melds4).toString());
    }

    public void testGetLargestMeld() {
        PlayBehaviour strategy1 = new Strategy1();

        // Case where there are no melds
        ArrayList<Meld> melds1 = new ArrayList<>();
        assertEquals(null, strategy1.getLargestMeld(melds1));

        // Case where there is one meld
        ArrayList<Meld> melds2 = new ArrayList<>();
        Meld meld1 = new Meld("R7,R8,R9");
        melds2.add(meld1);
        assertEquals("{R7 R8 R9}", strategy1.getLargestMeld(melds2).toString());

        // Case where there are multiple melds, when two are largest it chooses first
        ArrayList<Meld> melds3 = new ArrayList<>();
        Meld meld5 = new Meld("R1,R2,R3");
        Meld meld2 = new Meld("R1,O1,G1,B1");
        Meld meld3 = new Meld("O9,O10,O11,O12");
        Meld meld4 = new Meld("R10,R11,R12");
        melds3.add(meld2);
        melds3.add(meld3);
        melds3.add(meld4);
        melds3.add(meld5);
        assertEquals("{R1 O1 G1 B1}", strategy1.getLargestMeld(melds3).toString());
    }

    public void testGetGreatestMeld() {
        PlayBehaviour strategy1 = new Strategy1();

        // Case where there are no melds
        ArrayList<Meld> melds1 = new ArrayList<>();
        assertEquals(null, strategy1.getGreatestMeld(melds1));

        // Case where there is one meld
        ArrayList<Meld> melds2 = new ArrayList<>();
        Meld meld1 = new Meld("R7,R8,R9");
        melds2.add(meld1);
        assertEquals("{R7 R8 R9}", strategy1.getGreatestMeld(melds2).toString());

        // Case where there are multiple melds, when two are greatest it chooses first
        ArrayList<Meld> melds3 = new ArrayList<>();
        Meld meld4 = new Meld("R10,R11,R12");
        Meld meld5 = new Meld("R1,R2,R3");
        Meld meld2 = new Meld("R12,O12,G12");
        Meld meld3 = new Meld("O12,B12,G12");
        melds3.add(meld2);
        melds3.add(meld3);
        melds3.add(meld4);
        melds3.add(meld5);
        assertEquals("{R12 O12 G12}", strategy1.getGreatestMeld(melds3).toString());
    }

    // Cases where player creates new melds from tiles in hand (not using tiles on table)
    public void testPlayUsingHand1() {
        Strategy2 strategy2 = new Strategy2();

        ArrayList<Meld> workspace1 = new ArrayList<>();
        Meld meld1 = new Meld("R1,R2,R3");
        Meld meld2 = new Meld("O10,B10,G10");
        workspace1.add(meld1);
        workspace1.add(meld2);
        strategy2.setWorkspace(workspace1);

        Hand hand1 = new Hand("O11,B11,G11,B3,R4,B4,B5");
        assertEquals("[{R1 R2 R3}, {O10 B10 G10}, {B3 B4 B5}, {B11 G11 O11}]", strategy2.playUsingHand(hand1).toString());
        assertEquals("[R4]", hand1.toString());
    }

    // Cases where player adds tiles on table to valid melds in hand
    public void testPlayUsingHandAndTable1() {
        Strategy2 strategy2 = new Strategy2();

        // Remove from front of meld
        ArrayList<Meld> workspace1 = new ArrayList<>();
        Meld meld1 = new Meld("R1,R2,R3,R4");
        Meld meld2 = new Meld("G10,O10,B10");
        workspace1.add(meld1);
        workspace1.add(meld2);
        strategy2.setWorkspace(workspace1);

        Hand hand1 = new Hand("O1,G1,B1");
        assertEquals("[{R2 R3 R4}, {G10 O10 B10}, {B1 G1 R1 O1}]", strategy2.playUsingHandAndTable(hand1).toString());

        // Remove from back of meld
        ArrayList<Meld> workspace2 = new ArrayList<>();
        Meld meld3 = new Meld("O1,B1,G1,R1");
        Meld meld4 = new Meld("G9,G10,G11,G12");
        workspace2.add(meld3);
        workspace2.add(meld4);
        strategy2.setWorkspace(workspace2);

        Hand hand2 = new Hand("R2,R3,R4");
        assertEquals("[{O1 B1 G1}, {G9 G10 G11 G12}, {R1 R2 R3 R4}]", strategy2.playUsingHandAndTable(hand2).toString());
    }

    // Cases where player adds tiles on table to potential melds in hand (making the valid)
    public void testPlayUsingHandAndTable2() {
        Strategy2 strategy2 = new Strategy2();

        // Remove from front of meld
        ArrayList<Meld> workspace1 = new ArrayList<>();
        Meld meld1 = new Meld("R1,R2,R3,R4");
        Meld meld2 = new Meld("G10,O10,B10");
        workspace1.add(meld1);
        workspace1.add(meld2);
        strategy2.setWorkspace(workspace1);

        Hand hand1 = new Hand("O1,G1");
        assertEquals("[{R2 R3 R4}, {G10 O10 B10}, {G1 O1 R1}]", strategy2.playUsingHandAndTable(hand1).toString());

        // Remove from back of meld
        ArrayList<Meld> workspace2 = new ArrayList<>();
        Meld meld3 = new Meld("O1,B1,G1,R1");
        Meld meld4 = new Meld("G9,G10,G11,G12");
        workspace2.add(meld3);
        workspace2.add(meld4);
        strategy2.setWorkspace(workspace2);

        Hand hand2 = new Hand("R2,R3");
        assertEquals("[{O1 B1 G1}, {G9 G10 G11 G12}, {R1 R2 R3}]", strategy2.playUsingHandAndTable(hand2).toString());
    }

    // Cases where player adds tiles on table to tiles in hand (making new valid melds)
    public void testPlayUsingHandAndTable3() {
        Strategy2 strategy2 = new Strategy2();

        // Remove from front of melds
        ArrayList<Meld> workspace1 = new ArrayList<>();
        Meld meld1 = new Meld("O2,B2,G2,R2");
        Meld meld2 = new Meld("O1,B1,G1,R1");
        workspace1.add(meld1);
        workspace1.add(meld2);
        strategy2.setWorkspace(workspace1);

        Hand hand1 = new Hand("O3,B10,B11,B12");
        assertEquals("[{B2 G2 R2}, {B1 G1 R1}, {O1 O2 O3}]", strategy2.playUsingHandAndTable(hand1).toString());
        assertEquals("[B10 B11 B12]", hand1.toString());

        // Remove from front and back of melds
        ArrayList<Meld> workspace2 = new ArrayList<>();
        Meld meld3 = new Meld("R1,R2,R3,R4");
        Meld meld4 = new Meld("R1,R2,R3,R4,R5,R6");
        workspace2.add(meld3);
        workspace2.add(meld4);
        strategy2.setWorkspace(workspace2);

        Hand hand2 = new Hand("B10,B11,B12,R5");
        assertEquals("[{R1 R2 R3}, {R1 R2 R3 R4 R5}, {R4 R5 R6}]", strategy2.playUsingHandAndTable(hand2).toString());
        assertEquals("[B10 B11 B12]", hand2.toString());

        // Remove from front and back of melds
        ArrayList<Meld> workspace3 = new ArrayList<>();
        Meld meld5 = new Meld("G1,G2,G3,G4");
        Meld meld6 = new Meld("G6,G7,G8");      // should not remove from this one
        Meld meld7 = new Meld("G6,G7,G8,G9");
        workspace3.add(meld5);
        workspace3.add(meld6);
        workspace3.add(meld7);
        strategy2.setWorkspace(workspace3);

        Hand hand3 = new Hand("G1,G5,R10,R11");
        assertEquals("[{G1 G2 G3}, {G6 G7 G8}, {G7 G8 G9}, {G4 G5 G6}]", strategy2.playUsingHandAndTable(hand3).toString());
        assertEquals("[R10 R11 G1]", hand3.toString());
    }

    // Cases where player adds tile in hand to existing melds on table
    public void testPlayUsingHandAndTable4() {
        Strategy2 strategy2 = new Strategy2();

        // Add multiple tiles to multiple melds on table
        ArrayList<Meld> workspace1 = new ArrayList<>();
        Meld meld1 = new Meld("R1,R2,R3");
        Meld meld2 = new Meld("G10,G11,G12");
        Meld meld3 = new Meld("B9,B10,B11,B12");
        Meld meld4 = new Meld("B1,G1,O1");
        workspace1.add(meld1);
        workspace1.add(meld2);
        workspace1.add(meld3);
        workspace1.add(meld4);
        strategy2.setWorkspace(workspace1);

        Hand hand1 = new Hand("R1,R4,B8,B13,O1,G9");
        assertEquals("[{R1 R2 R3 R4}, {G9 G10 G11 G12}, {B8 B9 B10 B11 B12 B13}, {B1 G1 O1 R1}]", strategy2.playUsingHandAndTable(hand1).toString());
        assertEquals("[O1]", hand1.toString());
    }

    public void testHasWinningHand() {
        Strategy2 strategy2 = new Strategy2();
        ArrayList<Meld> workspace1 = new ArrayList<>();
        Meld meld1 = new Meld("R1,R2,R3,R4");
        Meld meld2 = new Meld("B10,O10,G10");
        workspace1.add(meld1);
        workspace1.add(meld2);
        strategy2.setWorkspace(workspace1);

        Hand winningHand = new Hand("R5,R8,G8,O8");
        assertEquals("[{R1 R2 R3 R4 R5}, {B10 O10 G10}, {R8 G8 O8}]", strategy2.hasWinningHand(winningHand).toString());

        Hand losingHand = new Hand("R5,R8,G8,O8,B1");
        assertEquals(null, strategy2.hasWinningHand(losingHand));
    }
    
    public void testTilesAddedToWorkspace() {
        Strategy1 strategy1 = new Strategy1();
        
        // Case where newWorkspace > oldWorkspace (returns true)
        ArrayList<Meld> newWorkspace1 = new ArrayList<Meld>();
        ArrayList<Meld> oldWorkspace1 = new ArrayList<Meld>();
        newWorkspace1.add(new Meld("R1,R2,R3,R4"));
        newWorkspace1.add(new Meld("G1,O1,B1"));
        oldWorkspace1.add(new Meld("R1,R2,R3"));
        oldWorkspace1.add(new Meld("G1,O1,B1"));
        assertTrue(strategy1.tilesAddedToWorkspace(newWorkspace1, oldWorkspace1));
        
        // Case where newWorkspace == oldWorkspace (returns false)
        ArrayList<Meld> newWorkspace2 = new ArrayList<Meld>();
        ArrayList<Meld> oldWorkspace2 = new ArrayList<Meld>();
        newWorkspace2.add(new Meld("R1,R2,R3"));
        newWorkspace2.add(new Meld("G1,O1,B1"));
        oldWorkspace2.add(new Meld("R1,R2,R3"));
        oldWorkspace2.add(new Meld("G1,O1,B1"));
        assertFalse(strategy1.tilesAddedToWorkspace(newWorkspace2, oldWorkspace2));
        
        // Case where newWorkspace < oldWorkspace (returns false)
        ArrayList<Meld> newWorkspace3 = new ArrayList<Meld>();
        ArrayList<Meld> oldWorkspace3 = new ArrayList<Meld>();
        newWorkspace3.add(new Meld("R1,R2,R3"));
        oldWorkspace3.add(new Meld("R1,R2,R3"));
        oldWorkspace3.add(new Meld("G1,O1,B1"));
        assertFalse(strategy1.tilesAddedToWorkspace(newWorkspace3, oldWorkspace3));
    }
}