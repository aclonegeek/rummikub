package core;

import java.util.ArrayList;

import core.Globals.Colour;
import junit.framework.TestCase;

public class Strategy2Test extends TestCase {
    // Test first move with valid melds >= 30 and an empty workspace
    public void testDetermineInitialMove1() {
        PlayBehaviour strategy2 = new Strategy2();
        ArrayList<Meld> workspace = new ArrayList<>();
        Hand hand = new Hand();
        hand.add(new Tile(Colour.GREEN, 1));
        hand.add(new Tile(Colour.GREEN, 2));
        hand.add(new Tile(Colour.GREEN, 6));
        hand.add(new Tile(Colour.GREEN, 7));
        hand.add(new Tile(Colour.GREEN, 10));
        hand.add(new Tile(Colour.ORANGE, 10));
        hand.add(new Tile(Colour.BLUE, 10));
        hand.add(new Tile(Colour.RED, 9));
        hand.add(new Tile(Colour.RED, 8));
        hand.add(new Tile(Colour.GREEN, 12));
        hand.add(new Tile(Colour.ORANGE, 2));
        hand.add(new Tile(Colour.RED, 11));
        hand.add(new Tile(Colour.RED, 12));
        hand.add(new Tile(Colour.BLUE, 7));
        
        assertEquals(null, strategy2.determineMove(workspace, hand, true));
    }
    
    // Test first move with one meld >= 30 (G10, O10, B10) and a non-empty workspace
    public void testDetermineInitialMove2() {
        PlayBehaviour strategy2 = new Strategy2();
        ArrayList<Meld> workspace = new ArrayList<>();
        Hand hand = new Hand();
        
        ArrayList<Tile> tiles1 = new ArrayList<>();
        tiles1.add(new Tile(Colour.RED, 1));
        tiles1.add(new Tile(Colour.RED, 2));
        tiles1.add(new Tile(Colour.RED, 3));
        Meld meld1 = new Meld();
        meld1.addTile(tiles1);
        workspace.add(meld1);
        
        assertEquals("[{R1,R2,R3}]", workspace.toString());
        
        hand.add(new Tile(Colour.GREEN, 1));
        hand.add(new Tile(Colour.GREEN, 2));
        hand.add(new Tile(Colour.GREEN, 6));
        hand.add(new Tile(Colour.GREEN, 7));
        hand.add(new Tile(Colour.GREEN, 10));
        hand.add(new Tile(Colour.ORANGE, 10));
        hand.add(new Tile(Colour.BLUE, 10));
        hand.add(new Tile(Colour.RED, 9));
        hand.add(new Tile(Colour.RED, 8));
        hand.add(new Tile(Colour.GREEN, 12));
        hand.add(new Tile(Colour.ORANGE, 2));
        hand.add(new Tile(Colour.RED, 11));
        hand.add(new Tile(Colour.RED, 12));
        hand.add(new Tile(Colour.BLUE, 7));
        
        assertEquals("[{R1,R2,R3}, {B10,G10,O10}]", strategy2.determineMove(workspace, hand, true).toString());
    }
    
    // Test first move with multiple melds totalling >= 30 (G8, O8, B8 and G1, G2, G3, G4) and a non-empty workspace
    public void testDetermineInitialMove3() {
        PlayBehaviour strategy2 = new Strategy2();
        ArrayList<Meld> workspace = new ArrayList<>();
        Hand hand = new Hand();
        
        ArrayList<Tile> tiles1 = new ArrayList<>();
        tiles1.add(new Tile(Colour.RED, 1));
        tiles1.add(new Tile(Colour.RED, 2));
        tiles1.add(new Tile(Colour.RED, 3));
        Meld meld1 = new Meld();
        meld1.addTile(tiles1);
        workspace.add(meld1);
        
        assertEquals("[{R1,R2,R3}]", workspace.toString());
        
        hand.add(new Tile(Colour.GREEN, 1));
        hand.add(new Tile(Colour.GREEN, 2));
        hand.add(new Tile(Colour.GREEN, 3));
        hand.add(new Tile(Colour.GREEN, 4));
        hand.add(new Tile(Colour.GREEN, 8));
        hand.add(new Tile(Colour.ORANGE, 8));
        hand.add(new Tile(Colour.BLUE, 8));
        hand.add(new Tile(Colour.RED, 9));
        hand.add(new Tile(Colour.RED, 8));
        hand.add(new Tile(Colour.GREEN, 5));
        hand.add(new Tile(Colour.ORANGE, 2));
        hand.add(new Tile(Colour.RED, 11));
        hand.add(new Tile(Colour.RED, 12));
        hand.add(new Tile(Colour.BLUE, 7));
        
        assertEquals("[{R1,R2,R3}, {B8,G8,O8}, {G1,G2,G3,G4,G5}]", strategy2.determineMove(workspace, hand, true).toString());
    }
    
    // Test first move where no combination of melds totals >= 30 and a non-empty workspace
    public void testDetermineInitialMove4() {
        PlayBehaviour strategy2 = new Strategy2();
        ArrayList<Meld> workspace = new ArrayList<>();
        Hand hand = new Hand();
        
        ArrayList<Tile> tiles1 = new ArrayList<>();
        tiles1.add(new Tile(Colour.RED, 1));
        tiles1.add(new Tile(Colour.RED, 2));
        tiles1.add(new Tile(Colour.RED, 3));
        Meld meld1 = new Meld();
        meld1.addTile(tiles1);
        workspace.add(meld1);
        
        assertEquals("[{R1,R2,R3}]", workspace.toString());
        
        hand.add(new Tile(Colour.GREEN, 1));
        hand.add(new Tile(Colour.GREEN, 2));
        hand.add(new Tile(Colour.GREEN, 3));
        hand.add(new Tile(Colour.GREEN, 4));
        hand.add(new Tile(Colour.GREEN, 8));
        hand.add(new Tile(Colour.ORANGE, 10));
        hand.add(new Tile(Colour.BLUE, 2));
        hand.add(new Tile(Colour.RED, 9));
        hand.add(new Tile(Colour.RED, 4));
        hand.add(new Tile(Colour.GREEN, 5));
        hand.add(new Tile(Colour.ORANGE, 2));
        hand.add(new Tile(Colour.RED, 11));
        hand.add(new Tile(Colour.RED, 12));
        hand.add(new Tile(Colour.BLUE, 7));
        
        assertEquals(null, strategy2.determineMove(workspace, hand, true));
    }
    
    // Test first move where no combination of melds totals >= 30 and an empty workspace
    public void testDetermineInitialMove5() {
        PlayBehaviour strategy2 = new Strategy2();
        ArrayList<Meld> workspace = new ArrayList<>();
        Hand hand = new Hand();
        
        assertEquals("[]", workspace.toString());
        
        hand.add(new Tile(Colour.GREEN, 1));
        hand.add(new Tile(Colour.GREEN, 2));
        hand.add(new Tile(Colour.GREEN, 3));
        hand.add(new Tile(Colour.GREEN, 4));
        hand.add(new Tile(Colour.GREEN, 8));
        hand.add(new Tile(Colour.ORANGE, 10));
        hand.add(new Tile(Colour.BLUE, 2));
        hand.add(new Tile(Colour.RED, 9));
        hand.add(new Tile(Colour.RED, 4));
        hand.add(new Tile(Colour.GREEN, 5));
        hand.add(new Tile(Colour.ORANGE, 2));
        hand.add(new Tile(Colour.RED, 11));
        hand.add(new Tile(Colour.RED, 12));
        hand.add(new Tile(Colour.BLUE, 7));
        
        assertEquals(null, strategy2.determineMove(workspace, hand, true));
    }
}