package core;

import junit.framework.TestCase;

public class HandTest extends TestCase {
    //tests add(), in doing so also tests sort() and toString()
    public void testAdd() {
        Hand hand = new Hand();
        assertEquals(0, hand.getSize());
        assertEquals("[]", hand.toString());
        
        Tile tile1 = new Tile('R', 1);
        Tile tile2 = new Tile('R', 2);
        Tile tile3 = new Tile('G', 3);
        Tile tile4 = new Tile('G', 5);
        Tile tile5 = new Tile('B', 1);
        Tile tile6 = new Tile('B', 10);
        Tile tile7 = new Tile('O', 7);
        Tile tile8 = new Tile('O', 13);
        
        hand.add(tile7);
        hand.add(tile3);
        hand.add(tile5);
        hand.add(tile8);
        hand.add(tile1);
        hand.add(tile4);
        hand.add(tile2);
        hand.add(tile6);
        assertEquals(8, hand.getSize());
        assertEquals("[R1, R2, G3, G5, B1, B10, O7, O13]", hand.toString());
    }
    
    //tests remove(), in doing so also tests add(), sort(), and toString()
    public void testRemove() {
        Hand hand = new Hand();
        
        Tile tile1 = new Tile('R', 1);
        Tile tile2 = new Tile('R', 2);
        Tile tile3 = new Tile('G', 3);
        hand.add(tile1);
        hand.add(tile2);
        hand.add(tile3);
        
        assertEquals(3, hand.getSize());
        assertEquals(null, hand.remove(5));
        assertEquals("R1", hand.remove(0).toString());
        assertEquals("G3", hand.remove(1).toString());
        assertEquals("R2", hand.remove(0).toString());
        assertEquals(null, hand.remove(0));
        assertEquals(0, hand.getSize());
    }
}
