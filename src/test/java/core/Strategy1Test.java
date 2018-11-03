package core;

import java.util.ArrayList;

import core.Globals.Colour;
import junit.framework.TestCase;

public class Strategy1Test extends TestCase {
    // Test first move with one meld >= 30 (G10, O10, B10)
    public void testDetermineInitialMove1() {
        Strategy1 strategy1 = new Strategy1();
        Hand hand = new Hand("G10,O10,B10,R10,R8,R7,R6,R5,R4");
        assertEquals("[{R4,R5,R6,R7,R8}]", strategy1.determineInitialMove(hand).toString());
    }
    
    // Test first move such that multiple melds total >= 30, should use as many melds as possible
    public void testDetermineInitialMove2() {
        Strategy1 strategy1 = new Strategy1();
        Hand hand = new Hand("R3,R4,R5,G5,G6,G7,O2,O3,O4");
        assertEquals("[{G5,G6,G7}, {R3,R4,R5}, {O2,O3,O4}]", strategy1.determineInitialMove(hand).toString());
    }
    
    // Test first move where no combination of melds totals >= 30
    public void testDetermineInitialMove3() {
        Strategy1 strategy1 = new Strategy1();
        Hand hand = new Hand("G1,G2,G3,G4,G8,O10,B2,R9,R4,G5,O2,R11,R12,B7");
        assertEquals(null, strategy1.determineInitialMove(hand));
    }
    
    // Test first move with existing melds in workspace (should not interact with existing melds, should only create new melds)
    public void testDetermineInitialMove4() {
        Strategy1 strategy1 = new Strategy1();
        ArrayList<Meld> workspace = new ArrayList<>();
        
        Meld meld1 = new Meld("R1,R2,R3");
        Meld meld2 = new Meld("R10,O10,G10,B10");;
        Meld meld3 = new Meld("B5,B6,B7,B8");
        workspace.add(meld1);
        workspace.add(meld2);
        workspace.add(meld3);
        strategy1.setWorkspace(workspace);
        assertEquals("[{R1,R2,R3}, {R10,O10,G10,B10}, {B5,B6,B7,B8}]", strategy1.getWorkspace().toString());
        
        Hand hand = new Hand("G1,G2,G3,G4,G8,O8,B8,R9,G5,O2,R11,R12,R7");
        assertEquals("[{R1,R2,R3}, {R10,O10,G10,B10}, {B5,B6,B7,B8}, {B8,G8,O8}, {G1,G2,G3,G4,G5}]", strategy1.determineInitialMove(hand).toString());
    }
    
    // 8a - player can play a single run
    public void testRegularMove1() {
        Strategy1 strategy1 = new Strategy1();
        ArrayList<Meld> workspace = new ArrayList<>();
        Meld meld1 = new Meld("R1,R2,R3");
        workspace.add(meld1);
        strategy1.setWorkspace(workspace);
        
        Hand hand = new Hand("G1,G2,G3,G4,B1,B2");
        assertEquals("[{R1,R2,R3}, {G1,G2,G3,G4}]", strategy1.determineRegularMove(hand).toString());
        assertEquals("[B1, B2]", hand.toString());
    }
    
    // 8b - player can play a single set
    public void testRegularMove2() {
        Strategy1 strategy1 = new Strategy1();
        ArrayList<Meld> workspace = new ArrayList<>();
        Meld meld1 = new Meld("R1,R2,R3");
        workspace.add(meld1);
        strategy1.setWorkspace(workspace);
        
        Hand hand = new Hand("G1,O1,B1,B1,B2");
        assertEquals("[{R1,R2,R3}, {B1,G1,O1}]", strategy1.determineRegularMove(hand).toString());
        assertEquals("[B1, B2]", hand.toString());
    }
    
    // 8c - player can play several runs
    public void testRegularMove3() {
        Strategy1 strategy1 = new Strategy1();
        ArrayList<Meld> workspace = new ArrayList<>();
        Meld meld1 = new Meld("R1,R2,R3");
        workspace.add(meld1);
        strategy1.setWorkspace(workspace);
        
        Hand hand = new Hand("B3,B2,B1,O9,O10,O11,R5,R6,R7");
        assertEquals("[{R1,R2,R3}, {R5,R6,R7}, {B1,B2,B3}, {O9,O10,O11}]", strategy1.determineRegularMove(hand).toString());
        assertEquals("[]", hand.toString());
    }
    
    // 8d - player can play several sets
    public void testRegularMove4() {
        Strategy1 strategy1 = new Strategy1();
        ArrayList<Meld> workspace = new ArrayList<>();
        Meld meld1 = new Meld("R1,R2,R3");
        workspace.add(meld1);
        strategy1.setWorkspace(workspace);
        
        Hand hand = new Hand("O1,B1,G1,R10,B10,G10,O6,B6,G6");
        assertEquals("[{R1,R2,R3}, {R10,B10,G10}, {B1,G1,O1}, {B6,G6,O6}]", strategy1.determineRegularMove(hand).toString());
        assertEquals("[]", hand.toString());
    }
    
    // 8e - player can play a mix of runs and sets
    public void testRegularMove5() {
        Strategy1 strategy1 = new Strategy1();
        ArrayList<Meld> workspace = new ArrayList<>();
        Meld meld1 = new Meld("R1,R2,R3");
        workspace.add(meld1);
        strategy1.setWorkspace(workspace);
        
        Hand hand = new Hand("O1,B1,G1,R6,R7,R8");
        assertEquals("[{R1,R2,R3}, {R6,R7,R8}, {B1,G1,O1}]", strategy1.determineRegularMove(hand).toString());
        assertEquals("[]", hand.toString());
    }
    
    // 9a - player can add a tile to a run on the table
    public void testRegularMove6() {
        Strategy1 strategy1 = new Strategy1();
        ArrayList<Meld> workspace = new ArrayList<>();
        Meld meld1 = new Meld("R1,R2,R3");
        workspace.add(meld1);
        strategy1.setWorkspace(workspace);
        
        Hand hand = new Hand("R4,G8,B9");
        assertEquals("[{R1,R2,R3,R4}]", strategy1.determineRegularMove(hand).toString());
        assertEquals("[B9, G8]", hand.toString());
    }
    
    // 9b - player can add a tile to a set on the table
    public void testRegularMove7() {
        Strategy1 strategy1 = new Strategy1();
        ArrayList<Meld> workspace = new ArrayList<>();
        Meld meld1 = new Meld("R1,G1,B1");
        workspace.add(meld1);
        strategy1.setWorkspace(workspace);
        
        Hand hand = new Hand("R4,G8,O1");
        assertEquals("[{R1,G1,B1,O1}]", strategy1.determineRegularMove(hand).toString());
        assertEquals("[R4, G8]", hand.toString());
    }
    
    // 9c - player can add several tiles to a run on the table
    public void testRegularMove8() {
        Strategy1 strategy1 = new Strategy1();
        ArrayList<Meld> workspace = new ArrayList<>();
        Meld meld1 = new Meld("R2,R3,R4");
        workspace.add(meld1);
        strategy1.setWorkspace(workspace);
        
        Hand hand = new Hand("R1,R5,R6");
        assertEquals("[{R1,R2,R3,R4,R5,R6}]", strategy1.determineRegularMove(hand).toString());
        assertEquals("[]", hand.toString());
    }
    
    // 9d - player can play a meld that uses a card from an existing run on the table
    public void testRegularMove9() {
        Strategy1 strategy1 = new Strategy1();
        ArrayList<Meld> workspace = new ArrayList<>();
        Meld meld1 = new Meld("R1,R2,R3,R4");
        workspace.add(meld1);
        strategy1.setWorkspace(workspace);
        
        Hand hand = new Hand("R2,R3,G10,G11");
        assertEquals("[{R2,R3,R4}, {R1,R2,R3}]", strategy1.determineRegularMove(hand).toString());
        assertEquals("[G10, G11]", hand.toString());
    }
    
    // 9e - player can play a meld that uses a card from an existing set on the table
    public void testRegularMove10() {
        Strategy1 strategy1 = new Strategy1();
        ArrayList<Meld> workspace = new ArrayList<>();
        Meld meld1 = new Meld("O1,G1,B1,R1");
        Meld meld2 = new Meld("R1,R2,R3");
        workspace.add(meld1);
        workspace.add(meld2);
        strategy1.setWorkspace(workspace);
        
        Hand hand = new Hand("O3,O2,O9,O10");
        assertEquals("[{G1,B1,R1}, {R1,R2,R3}, {O1,O2,O3}]", strategy1.determineRegularMove(hand).toString());
        assertEquals("[O9, O10]", hand.toString());
    }
    
    // 9f - player can play several tiles to add to several melds on the table
    public void testRegularMove11() {
        Strategy1 strategy1 = new Strategy1();
        ArrayList<Meld> workspace = new ArrayList<>();
        Meld meld1 = new Meld("R1,R2,R3");
        Meld meld2 = new Meld("G10,G11,G12");
        Meld meld3 = new Meld("B9,B10,B11,B12");
        Meld meld4 = new Meld("B1,G1,O1");
        workspace.add(meld1);
        workspace.add(meld2);
        workspace.add(meld3);
        workspace.add(meld4);
        strategy1.setWorkspace(workspace);
        
        Hand hand = new Hand("R1,R4,B8,B13,O1,G9"); 
        assertEquals("[{R1,R2,R3,R4}, {G9,G10,G11,G12}, {B8,B9,B10,B11,B12,B13}, {B1,G1,O1,R1}]", strategy1.determineRegularMove(hand).toString());
        assertEquals("[O1]", hand.toString());
    }
    
    // 9g - player can play a meld that requires organization of several melds on the table
    public void testRegularMove12() {
        Strategy1 strategy1 = new Strategy1();
        ArrayList<Meld> workspace = new ArrayList<>();
        Meld meld1 = new Meld("R1,R2,R3,R4");
        Meld meld2 = new Meld("O4,O5,O6,O7,O8");
        Meld meld3 = new Meld("B1,B2,B3,B4");
        workspace.add(meld1);
        workspace.add(meld2);
        workspace.add(meld3);
        strategy1.setWorkspace(workspace);
        
        Hand hand = new Hand("G4");
        assertEquals("[{R1,R2,R3}, {O5,O6,O7,O8}, {B1,B2,B3}, {G4,R4,O4,B4}]", strategy1.determineRegularMove(hand).toString());
        assertEquals("[]", hand.toString());
    }
    
    // Regular move where player cannot play
    public void testRegularMove13() {
        Strategy1 strategy1 = new Strategy1();
        ArrayList<Meld> workspace = new ArrayList<>();
        Meld meld1 = new Meld("R1,R2,R3,R4");
        Meld meld2 = new Meld("O4,O5,O6,O7,O8");
        Meld meld3 = new Meld("B1,B2,B3,B4");
        workspace.add(meld1);
        workspace.add(meld2);
        workspace.add(meld3);
        strategy1.setWorkspace(workspace);
        
        Hand hand = new Hand("G8");
        assertEquals(null, strategy1.determineRegularMove(hand));
        assertEquals("[G8]", hand.toString());
    }
}