package core;

import static org.junit.Assert.assertNotEquals;

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
    
    public void testCopyConstructor() {
        Tile originalTile1 = new Tile("R1");
        Tile originalTile2 = new Tile("J");
        
        Tile alternateTile1 = new Tile("O2");
        Tile alternateTile2 = new Tile("G2");
        
        originalTile1.setOnTable(true);
        originalTile2.setOnTable(true);
        originalTile2.setIsReplaced(true);
        originalTile2.addAlternateState(alternateTile1);
        originalTile2.addAlternateState(alternateTile2);
        
        Tile newTile1 = new Tile(originalTile1);
        assertNotEquals(originalTile1, newTile1);
        assertEquals(originalTile1.getColour(), newTile1.getColour());
        assertEquals(originalTile1.getValue(), newTile1.getValue());
        assertEquals(originalTile1.isOnTable(), newTile1.isOnTable());
        assertEquals(originalTile1.isJoker(), newTile1.isJoker());
        assertEquals(originalTile1.isReplaced(), newTile1.isReplaced());
        assertEquals(originalTile1.toString(), newTile1.toString());
        
        Tile newTile2 = new Tile(originalTile2);
        assertNotEquals(originalTile2, newTile2);
        assertEquals(originalTile2.getColour(), newTile2.getColour());
        assertEquals(originalTile2.getValue(), newTile2.getValue());
        assertEquals(originalTile2.isOnTable(), newTile1.isOnTable());
        assertEquals(originalTile2.isJoker(), newTile2.isJoker());
        assertEquals(originalTile2.isReplaced(), newTile2.isReplaced());
        assertEquals(originalTile2.getAlternateState().toString(), newTile2.getAlternateState().toString());
        assertEquals(originalTile2.toString(), newTile2.toString());
    }
}