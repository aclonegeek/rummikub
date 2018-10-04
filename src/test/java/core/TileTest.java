package core;

import core.Globals.Colour;
import junit.framework.TestCase;

public class TileTest extends TestCase {
    public void testTile() {
        Tile t1 = new Tile(Colour.RED, 1);
        assertNotNull(t1);
    }
    
    public void testToString() {
        Tile t1 = new Tile(Colour.ORANGE, 6);
        assertEquals("O6", t1.toString());
    }
}
