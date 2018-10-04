package core;

import junit.framework.TestCase;

public class TileTest extends TestCase {
    public void testTile() {
        Tile t1 = new Tile('R', 1);
        assertNotNull(t1);
    }
    
    public void testToString() {
        Tile t1 = new Tile('O', 6);
        assertEquals("O6", t1.toString());
    }
}
