package core;

import java.util.ArrayList;

import core.Globals.Colour;
import junit.framework.TestCase;

public class Player3Test extends TestCase {
    public void testPlay() {
        Player p3 = new Player3();
        
        // Test case where player plays initial move
        ArrayList<Meld> workspace1 = new ArrayList<>();
        workspace1.add(new Meld("G1,G2,G3"));
        Hand hand1 = new Hand("B10,G10,O10,G4");
        p3.setInitialMove(true);
        p3.setWorkspace(workspace1);
        p3.setHand(hand1);
        assertEquals("[{G1,G2,G3}, {B10,G10,O10}]", p3.play().toString());
        
        // Test case where player uses Strategy2 (only plays to existing melds), ie. no other player has 3 fewer tiles
        ArrayList<Meld> workspace2 = new ArrayList<>();
        workspace2.add(new Meld("G1,G2,G3"));
        Hand hand2 = new Hand("B10,G10,O10,G4,B5");
        p3.setInitialMove(false);
        p3.setLowestHandCount(4);
        p3.setWorkspace(workspace2);
        p3.setHand(hand2);
        assertEquals("[{G1,G2,G3,G4}]", p3.play().toString());
        
        // Test case where player uses Strategy1 (plays all possible melds), ie. some player has 3 or more fewer tiles
        ArrayList<Meld> workspace3 = new ArrayList<>();
        workspace3.add(new Meld("G1,G2,G3"));
        Hand hand3 = new Hand("B10,G10,O10,G4,B10");
        p3.setInitialMove(false);
        p3.setLowestHandCount(1);
        p3.setWorkspace(workspace3);
        p3.setHand(hand3);
        assertEquals("[{G1,G2,G3,G4}, {B10,G10,O10}]", p3.play().toString());
        
        // Test case where player cannot play any tiles
        ArrayList<Meld> workspace5 = new ArrayList<>();
        workspace5.add(new Meld("G1,G2,G3"));
        Hand hand5 = new Hand("B10,G10,O10,G5");
        p3.setInitialMove(false);
        p3.setWorkspace(workspace5);
        p3.setLowestHandCount(4);
        p3.setHand(hand5);
        assertEquals(null, p3.play());
    }
    
    public void testToString() {
        Player p3 = new Player3();
        assertEquals("Player 3:\n# tiles: 0\n\n", p3.toString());
        p3.add(new Tile(Colour.RED, 1));
        p3.add(new Tile(Colour.ORANGE, 12));
        p3.add(new Tile(Colour.BLUE, 6));
        p3.add(new Tile(Colour.ORANGE, 2));
        p3.add(new Tile(Colour.ORANGE, 2));
        assertEquals("Player 3:\n# tiles: 5\n\n", p3.toString());
    }
}
