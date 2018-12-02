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
        
        Hand hand6 = new Hand("R1,R2,R3,J,O10,B10");
        ArrayList<Meld> result = strategy1.createMeldsFromHand(hand6);
        assertEquals("[{R1 R2 J}, {R1 J R3}, {R2 R3 J}, {R1 R2 R3 J}, {B10 O10 J}, {R1 R2 R3}]", result.toString());
        assertEquals(6, result.get(0).getValue());
        assertEquals(6, result.get(1).getValue());
        assertEquals(9, result.get(2).getValue());
        assertEquals(10, result.get(3).getValue());
        assertEquals(30, result.get(4).getValue());
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
        
        // Does not create {J R1} or {J R2}
        Hand hand5 = new Hand("R1,R2,J");
        assertEquals("[{R1 J}, {R2 J}, {R1 R2}]", strategy1.createPotentialMeldsFromHand(hand5).toString());
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
        
        // Case where melds contain jokers
        ArrayList<Meld> melds5 = new ArrayList<>();
        Meld meld8 = new Meld("R9,R10,R11,R12");
        Meld meld9 = new Meld("O10,B10,G10,R10,J");
        Meld meld10 = new Meld("J,G8,G7,G6,G5,G4,G3");
        melds5.add(meld8);
        melds5.add(meld9);
        melds5.add(meld10);
        assertEquals("{G3 G4 G5 G6 G7 G8 J}", strategy1.getLargestMeldOver30(melds5).toString());
        
        // Case where melds contain jokers
        ArrayList<Meld> melds6 = new ArrayList<>();
        Meld meld11 = new Meld("B1,B2,J");
        Meld meld12 = new Meld("B10,B11,J");
        melds6.add(meld11);
        melds6.add(meld12);
        assertEquals("{B10 B11 J}", strategy1.getLargestMeldOver30(melds6).toString());
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
        
        // Case where melds contain jokers
        ArrayList<Meld> melds4 = new ArrayList<>();
        Meld meld6 = new Meld("R1,R2,R3");
        Meld meld7 = new Meld("R1,O1,G1,J");
        Meld meld9 = new Meld("R10,R11,R12");
        melds4.add(meld6);
        melds4.add(meld7);
        melds4.add(meld9);
        assertEquals("{R1 O1 G1 J}", strategy1.getLargestMeld(melds4).toString());
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
        
        // Case where melds contain jokers
        ArrayList<Meld> melds4 = new ArrayList<>();
        Meld meld6 = new Meld("R10,R11,R12");
        Meld meld7 = new Meld("R1,R2,R3");
        Meld meld8 = new Meld("R12,O12,G12,J");
        Meld meld9 = new Meld("O12,B12,G12");
        melds4.add(meld6);
        melds4.add(meld7);
        melds4.add(meld8);
        melds4.add(meld9);
        assertEquals("{R12 O12 G12 J}", strategy1.getGreatestMeld(melds4).toString());
    }

    // Cases where player creates new melds from tiles in hand (not using tiles on table)
    public void testPlayUsingHand1() {
        Strategy2 strategy2 = new Strategy2();

        ArrayList<Meld> workspace1 = new ArrayList<>();
        Meld meld1 = new Meld("R1,R2,R3");
        Meld meld2 = new Meld("O10,B10,G10");
        workspace1.add(meld1);
        workspace1.add(meld2);

        Hand hand1 = new Hand("O11,B11,G11,B3,R4,B4,B5");
        assertEquals("[{R1 R2 R3}, {O10 B10 G10}, {B3 B4 B5}, {B11 G11 O11}]", strategy2.playUsingHand(hand1, workspace1).toString());
        assertEquals("[R4]", hand1.toString());
    }
    
    // Cases where player creates new melds from tiles in hand (not using tiles on table), with jokers
    public void testPlayUsingHand2() {
        Strategy2 strategy2 = new Strategy2();

        ArrayList<Meld> workspace1 = new ArrayList<>();
        Meld meld1 = new Meld("R1,R2,R3");
        Meld meld2 = new Meld("O10,B10,G10");
        workspace1.add(meld1);
        workspace1.add(meld2);

        Hand hand1 = new Hand("J,O11,B11,G11,B3,R4,B4,B5");
        assertEquals("[{R1 R2 R3}, {O10 B10 G10}, {B3 B4 B5 J}, {B11 G11 O11}]", strategy2.playUsingHand(hand1, workspace1).toString());
        assertEquals("[R4]", hand1.toString());
    }
    
    // Cases where player adds tiles on table to potential melds in hand (making them valid)
    public void testPlayUsingHandAndTable2() {
        Strategy2 strategy2 = new Strategy2();

        // Remove from front of meld
        ArrayList<Meld> workspace1 = new ArrayList<>();
        Meld meld1 = new Meld("R1,R2,R3,R4");
        Meld meld2 = new Meld("G10,O10,B10");
        workspace1.add(meld1);
        workspace1.add(meld2);

        Hand hand1 = new Hand("O1,G1");
        assertEquals("[{R2 R3 R4}, {G10 O10 B10}, {G1 O1 R1}]", strategy2.playUsingHandAndTable_AddToPotentialMeldsInHand(hand1, workspace1).toString());

        // Remove from back of meld
        ArrayList<Meld> workspace2 = new ArrayList<>();
        Meld meld3 = new Meld("O1,B1,G1,R1");
        Meld meld4 = new Meld("G9,G10,G11,G12");
        workspace2.add(meld3);
        workspace2.add(meld4);

        Hand hand2 = new Hand("R2,R3");
        assertEquals("[{O1 B1 G1}, {G9 G10 G11 G12}, {R1 R2 R3}]", strategy2.playUsingHandAndTable_AddToPotentialMeldsInHand(hand2, workspace2).toString());
        
        // Remove from back of meld with joker
        ArrayList<Meld> workspace3 = new ArrayList<>();
        Meld meld5 = new Meld("O1,B1,G1,R1");
        workspace3.add(meld5);
        
        Hand hand3 = new Hand("R2,J");
        assertEquals("[{O1 B1 G1}, {R1 R2 J}]", strategy2.playUsingHandAndTable_AddToPotentialMeldsInHand(hand3, workspace3).toString());
        
        // Remove from front of meld with joker
        ArrayList<Meld> workspace4 = new ArrayList<>();
        Meld meld6 = new Meld("R1,R2,R3,R4");
        workspace4.add(meld6);
        
        Hand hand4 = new Hand("O1,J,O10,R9");
        assertEquals("[{R2 R3 R4}, {O1 R1 J}]", strategy2.playUsingHandAndTable_AddToPotentialMeldsInHand(hand4, workspace4).toString());
        assertEquals("[R9 O10]", hand4.toString());
        
        // Remove from meld with joker in it - should not be able to
        ArrayList<Meld> workspace5 = new ArrayList<>();
        Meld meld7 = new Meld("R1,R2,R3,J");
        workspace5.add(meld7);
        
        Hand hand5 = new Hand("G1,O1");
        assertEquals("[{R1 R2 R3 J}]", strategy2.playUsingHandAndTable_AddToPotentialMeldsInHand(hand5, workspace5).toString());
        assertEquals("[G1 O1]", hand5.toString());
    }
    
    // Cases where player splits melds on the table
    public void testPlayUsingHandAndTable3() {
        Strategy2 strategy2 = new Strategy2();

        ArrayList<Meld> workspace1 = new ArrayList<>();
        Meld meld1 = new Meld("R6,R7,R8,R9,R10,R11,R12,R13");
        workspace1.add(meld1);

        Hand hand1 = new Hand("O10,G10");
        assertEquals("[{R6 R7 R8 R9}, {R11 R12 R13}, {G10 O10 R10}]", strategy2.playUsingHandAndTable_AddToPotentialMeldsInHand(hand1, workspace1).toString());
    }

    // Cases where player adds tiles on table to tiles in hand (making new valid melds)
    public void testPlayUsingHandAndTable4() {
        Strategy2 strategy2 = new Strategy2();

        // Remove from front of melds
        ArrayList<Meld> workspace1 = new ArrayList<>();
        Meld meld1 = new Meld("O2,B2,G2,R2");
        Meld meld2 = new Meld("O1,B1,G1,R1");
        workspace1.add(meld1);
        workspace1.add(meld2);

        Hand hand1 = new Hand("O3,B10,B11,B12");
        assertEquals("[{B2 G2 R2}, {B1 G1 R1}, {O1 O2 O3}]", strategy2.playUsingHandAndTable_AddToTilesInHand(hand1, workspace1).toString());
        assertEquals("[B10 B11 B12]", hand1.toString());

        // Remove from front and back of melds
        ArrayList<Meld> workspace2 = new ArrayList<>();
        Meld meld3 = new Meld("R1,R2,R3,R4");
        Meld meld4 = new Meld("R1,R2,R3,R4,R5,R6");
        workspace2.add(meld3);
        workspace2.add(meld4);

        Hand hand2 = new Hand("B10,B11,B12,R5");
        assertEquals("[{R1 R2 R3}, {R1 R2 R3 R4 R5}, {R4 R5 R6}]", strategy2.playUsingHandAndTable_AddToTilesInHand(hand2, workspace2).toString());
        assertEquals("[B10 B11 B12]", hand2.toString());

        // Remove from front and back of melds
        ArrayList<Meld> workspace3 = new ArrayList<>();
        Meld meld5 = new Meld("G1,G2,G3,G4");
        Meld meld6 = new Meld("G6,G7,G8");      // should not remove from this one
        Meld meld7 = new Meld("G6,G7,G8,G9");
        workspace3.add(meld5);
        workspace3.add(meld6);
        workspace3.add(meld7);

        Hand hand3 = new Hand("G1,G5,R10,R11");
        assertEquals("[{G1 G2 G3}, {G6 G7 G8}, {G7 G8 G9}, {G4 G5 G6}]", strategy2.playUsingHandAndTable_AddToTilesInHand(hand3, workspace3).toString());
        assertEquals("[R10 R11 G1]", hand3.toString());
        
        // Remove from front and back of melds with joker in hand
        ArrayList<Meld> workspace4 = new ArrayList<>();
        Meld meld8 = new Meld("G2,G3,G4,G5");
        Meld meld9 = new Meld("O2,B2,R2,G2");
        workspace4.add(meld8);
        workspace4.add(meld9);

        Hand hand4 = new Hand("J,B4,G13");
        assertEquals("[{G3 G4 G5}, {B2 R2 G2}, {G2 O2 J}]", strategy2.playUsingHandAndTable_AddToTilesInHand(hand4, workspace4).toString());
        assertEquals("[B4 G13]", hand4.toString());
        
        // Remove from meld with joker in it - should not be able to
        ArrayList<Meld> workspace5 = new ArrayList<>();
        Meld meld10 = new Meld("R1,R2,R3,J");
        Meld meld11 = new Meld("R1,B1,G1,O1");
        workspace5.add(meld10);
        workspace5.add(meld11);
        
        Hand hand5 = new Hand("G1");
        assertEquals("[{R1 R2 R3 J}, {B1 G1 O1 R1}]", strategy2.playUsingHandAndTable_AddToTilesInHand(hand5, workspace5).toString());
        assertEquals("[G1]", hand5.toString());
    }

    // Cases where player adds tile in hand to existing melds on table
    public void testPlayUsingHandAndTable5() {
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

        Hand hand1 = new Hand("R1,R4,B8,B13,O1,G9");
        assertEquals("[{R1 R2 R3 R4}, {G9 G10 G11 G12}, {B8 B9 B10 B11 B12 B13}, {B1 G1 O1 R1}]", strategy2.playUsingHandAndTable_AddToMeldsOnTable(hand1, workspace1).toString());
        assertEquals("[O1]", hand1.toString());
        
        // Add tiles to melds with jokers - should not be able to
        ArrayList<Meld> workspace2 = new ArrayList<>();
        Meld meld5 = new Meld("R1,R2,R3,J");
        workspace2.add(meld5);
        Hand hand2 = new Hand("R4");
        assertEquals("[{R1 R2 R3 J}]", strategy2.playUsingHandAndTable_AddToMeldsOnTable(hand2, workspace2).toString());
        assertEquals("[R4]", hand2.toString());
    }

    public void testHasWinningHand() {
        Strategy2 strategy2 = new Strategy2();
        ArrayList<Meld> workspace1 = new ArrayList<>();
        Meld meld1 = new Meld("R1,R2,R3,R4");
        Meld meld2 = new Meld("B10,O10,G10");
        workspace1.add(meld1);
        workspace1.add(meld2);

        Hand winningHand = new Hand("R5,R8,G8,O8,J");
        assertEquals("[{R1 R2 R3 R4 R5}, {B10 O10 G10}, {R8 G8 O8 J}]", strategy2.hasWinningHand(winningHand, workspace1).toString());

        Hand losingHand = new Hand("R5,R8,G8,O8,B1");
        assertEquals(null, strategy2.hasWinningHand(losingHand, workspace1));
    }
    
    public void testTilesAddedToWorkspace() {
        Strategy1 strategy1 = new Strategy1();
        
        // Case where newWorkspace > oldWorkspace (returns true)
        ArrayList<Meld> newWorkspace1 = new ArrayList<Meld>();
        ArrayList<Meld> oldWorkspace1 = new ArrayList<Meld>();
        newWorkspace1.add(new Meld("R1,R2,R3,J"));
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