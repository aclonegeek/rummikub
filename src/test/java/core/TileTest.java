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
}
