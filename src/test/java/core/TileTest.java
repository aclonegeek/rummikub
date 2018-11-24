package core;

import core.Globals.Colour;
import junit.framework.TestCase;

public class TileTest extends TestCase {
    public void testToString() {
        Tile t1 = new Tile(Colour.ORANGE, 6);
        assertEquals("O6", t1.toString());
    }
    
    public void testStringConstructor() {
        Tile t1 = new Tile(Colour.ORANGE, 9);
        Tile t2 = new Tile("O9");
        assertEquals(t1.toString(), t2.toString());
        
        t1 = new Tile(Colour.ORANGE, 13);
        t2 = new Tile("O13");
        assertEquals(t1.toString(), t2.toString());
    }
    
    // Returns 0 if arg equal, < 0 if arg greater, > 0 if arg less
    public void testCompareTo() {
        // Different colour, different value
        Tile t1 = new Tile("R3");
        Tile t2 = new Tile("G10");
        assertTrue(t1.compareTo(t2) < 0);
        assertTrue(t2.compareTo(t1) > 0);
        
        // Different colour, same value (R=1, B=2, G=3, O=4)
        Tile t3 = new Tile("G5");
        Tile t4 = new Tile("R5");
        assertTrue(t3.compareTo(t4) > 0);
        assertTrue(t4.compareTo(t3) < 0);
        
        // Same colour, different value
        Tile t5 = new Tile("R8");
        Tile t6 = new Tile("R4");
        assertTrue(t6.compareTo(t5) < 0);
        assertTrue(t5.compareTo(t6) > 0);
        
        // Same colour, same value
        Tile t7 = new Tile("R1");
        Tile t8 = new Tile("R1");
        assertTrue(t7.compareTo(t8) == 0);
        assertTrue(t8.compareTo(t7) == 0);
    }
}