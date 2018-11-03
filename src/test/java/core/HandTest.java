package core;

import core.Globals.Colour;
import junit.framework.TestCase;

public class HandTest extends TestCase {
    public void testStringConstructor() {
        Hand hand1 = new Hand("");
        assertEquals(0, hand1.getSize());
        assertEquals("[]", hand1.toString());
        
        Hand hand2 = new Hand("R4,G5,O1,R1,B12");
        assertEquals(5, hand2.getSize());
        assertEquals("[R1, R4, B12, G5, O1]", hand2.toString());
    }
    
    // Tests add()
    // Also tests sort() and toString()
    public void testAdd() {
        Hand hand = new Hand();
        assertEquals(0, hand.getSize());
        assertEquals("[]", hand.toString());
        
        Tile tile1 = new Tile(Colour.RED, 1);
        Tile tile2 = new Tile(Colour.RED, 2);
        Tile tile3 = new Tile(Colour.GREEN, 3);
        Tile tile4 = new Tile(Colour.GREEN, 5);
        Tile tile5 = new Tile(Colour.BLUE, 1);
        Tile tile6 = new Tile(Colour.BLUE, 10);
        Tile tile7 = new Tile(Colour.ORANGE, 7);
        Tile tile8 = new Tile(Colour.ORANGE, 12);
        Tile tile9 = new Tile(Colour.ORANGE, 13);
        
        hand.add(tile9);
        hand.add(tile1);
        hand.add(tile8);
        hand.add(tile2);
        hand.add(tile7);
        hand.add(tile3);
        hand.add(tile6);
        hand.add(tile4);
        hand.add(tile5);
        assertEquals(9, hand.getSize());
        assertEquals("[R1, R2, B1, B10, G3, G5, O7, O12, O13]", hand.toString());
    }
  
    // Tests remove() using index
    // Also tests add(), sort(), and toString()
    public void testRemoveByIndex() {
        Hand hand = new Hand();
        assertEquals(null, hand.remove(0));
        
        Tile tile1 = new Tile(Colour.RED, 1);
        Tile tile2 = new Tile(Colour.RED, 2);
        Tile tile3 = new Tile(Colour.GREEN, 3);
        hand.add(tile1);
        hand.add(tile2);
        hand.add(tile3);
        
        assertEquals(3, hand.getSize());
        assertEquals(null, hand.remove(5));
        assertEquals(null, hand.remove(-1));
        assertEquals("R1", hand.remove(0).toString());
        assertEquals("G3", hand.remove(1).toString());
        assertEquals("R2", hand.remove(0).toString());
        assertEquals(null, hand.remove(0));
        assertEquals(0, hand.getSize());
    }
    
    // Tests remove() using Tile
    // Also tests add(), sort(), and toString()
    public void testRemoveByTile() {
        Hand hand = new Hand();
        Tile tile1 = new Tile(Colour.RED, 1);
        Tile tile2 = new Tile(Colour.RED, 2);
        Tile tile3 = new Tile(Colour.GREEN, 3);
        Tile tile4 = new Tile(Colour.RED, 1);
        hand.add(tile1);
        hand.add(tile2);
        hand.add(tile3);
        hand.add(tile4);
        
        assertEquals("R1", hand.remove(tile1).toString());
        assertEquals("[R1, R2, G3]", hand.toString());
        assertEquals("R1", hand.remove(tile1).toString());
        assertEquals(null, hand.remove(tile1));
        assertEquals("R2", hand.remove(tile2).toString());
        assertEquals("G3", hand.remove(tile3).toString());
        assertEquals(0, hand.getSize());
    }
}