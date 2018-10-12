package core;

import java.util.ArrayList;

import core.Globals.Colour;
import core.Meld.MeldType;
import junit.framework.TestCase;

public class MeldTest extends TestCase {
    // Add tiles to a meld to make a valid RUN
    public void testAddTileValidRun() {
        // Test each colour
        for (Colour colour : Colour.values()) {
            Meld m1 = new Meld();
            ArrayList<Tile> nextTile = new ArrayList<>();
            boolean tileAdded;

            // Initial case: a RUN with 3 tiles to the empty meld
            nextTile.add(new Tile(colour, 3));
            nextTile.add(new Tile(colour, 4));
            nextTile.add(new Tile(colour, 5));

            // Add the tiles to the meld
            tileAdded = m1.addTile(nextTile);

            // Check if tile was successfully added and is of type RUN
            assertEquals(true, tileAdded);
            assertEquals(MeldType.RUN, m1.getMeldType());
            nextTile = new ArrayList<>();

            // Add more tiles to the back of an existing RUN
            nextTile = new ArrayList<>();
            nextTile.add(new Tile(colour, 6));
            nextTile.add(new Tile(colour, 7));
            nextTile.add(new Tile(colour, 8));

            // Add the tiles to the meld
            tileAdded = m1.addTile(nextTile);

            // Check if tile was successfully added and is of type RUN
            assertEquals(true, tileAdded);
            assertEquals(MeldType.RUN, m1.getMeldType());
            nextTile = new ArrayList<>();

            // Add more tiles to the front of an existing RUN
            nextTile = new ArrayList<>();
            nextTile.add(new Tile(colour, 1));
            nextTile.add(new Tile(colour, 2));

            // Add the tiles to the meld
            tileAdded = m1.addTile(nextTile);

            // Check if tile was successfully added and is of type RUN
            assertEquals(true, tileAdded);
            assertEquals(MeldType.RUN, m1.getMeldType());
            nextTile = new ArrayList<>();
        }
    }

    // Add tiles to a meld to make a valid SET
    public void testAddTileValidSet() {
        Meld m1 = new Meld();
        ArrayList<Tile> nextTile = new ArrayList<>();
        boolean tileAdded;

        // Initial case: a SET with 3 tiles to the empty meld
        nextTile.add(new Tile(Colour.RED, 3));
        nextTile.add(new Tile(Colour.GREEN, 3));
        nextTile.add(new Tile(Colour.BLUE, 3));
        System.out.println(nextTile);
        // Add the tiles to the meld
        tileAdded = m1.addTile(nextTile);

        // Check if tile was successfully added and is of type SET
        assertEquals(true, tileAdded);
        assertEquals(MeldType.SET, m1.getMeldType());
        nextTile = new ArrayList<>();

        // Add a fourth tile to the SET to make it valid
        nextTile.add(new Tile(Colour.ORANGE, 3));

        // Add the tile to the meld
        tileAdded = m1.addTile(nextTile);

        // Check if tile was successfully added and is of type SET
        assertEquals(true, tileAdded);
        assertEquals(MeldType.SET, m1.getMeldType());
        nextTile = new ArrayList<>();
    }
    
    // Add tiles to an empty meld that will make it invalid
    public void testAddTileInvalidInitialMeld() {
        Meld m1 = new Meld();
        ArrayList<Tile> nextTile = new ArrayList<>();
        boolean tileAdded;
        
        // Initial case: An invalid set of tiles added to an empty meld
        nextTile.add(new Tile(Colour.RED, 3));
        nextTile.add(new Tile(Colour.GREEN, 4));
        nextTile.add(new Tile(Colour.BLUE, 5));

        // Add the tiles to the meld
        tileAdded = m1.addTile(nextTile);

        // Check if tile was not successfully added as is INVALID
        assertEquals(false, tileAdded);
        assertEquals(MeldType.INVALID, m1.getMeldType());
        nextTile = new ArrayList<>();
        
        // An invalid set of tile (even if its a potential meld) added to an empty meld
        nextTile.add(new Tile(Colour.RED, 3));
        nextTile.add(new Tile(Colour.RED, 3));

        // Add the tiles to the meld
        tileAdded = m1.addTile(nextTile);

        // Check if tile was not successfully added as is INVALID
        assertEquals(false, tileAdded);
        assertEquals(MeldType.INVALID, m1.getMeldType());
        nextTile = new ArrayList<>();
    }
    
    // Add tiles to a RUN that will make it invalid
    public void testAddTileInvalidRun() {
        Meld m1 = new Meld();
        ArrayList<Tile> nextTile = new ArrayList<>();
        boolean tileAdded;
        
        // Initial case: a RUN with 3 tiles to the empty meld
        nextTile.add(new Tile(Colour.RED, 3));
        nextTile.add(new Tile(Colour.RED, 4));
        nextTile.add(new Tile(Colour.RED, 5));

        // Add the tiles to the meld
        tileAdded = m1.addTile(nextTile);

        assertEquals(true, tileAdded);
        assertEquals(MeldType.RUN, m1.getMeldType());
        nextTile = new ArrayList<>();

        // Add a same colour/same value fourth tile
        nextTile.add(new Tile(Colour.RED, 3));
        
        tileAdded = m1.addTile(nextTile);
        
        assertFalse(tileAdded);
        assertEquals(m1.getMeldType(), MeldType.RUN);
        nextTile = new ArrayList<>();
        
        // Add a same colour/different value fourth tile to the RUN
        nextTile.add(new Tile(Colour.RED, 7));
        
        tileAdded = m1.addTile(nextTile);
        
        assertFalse(tileAdded);
        assertEquals(m1.getMeldType(), MeldType.RUN);
        nextTile = new ArrayList<>();
        
        // Add a different colour/same value fourth tile
        nextTile.add(new Tile(Colour.GREEN, 3));
        
        // Add the tiles to the meld
        tileAdded = m1.addTile(nextTile);
        
        assertFalse(tileAdded);
        assertEquals(m1.getMeldType(), MeldType.RUN);
        nextTile = new ArrayList<>();
        
        // Add a different colour/different value fourth tile
        nextTile.add(new Tile(Colour.GREEN, 5));
        
        tileAdded = m1.addTile(nextTile);
        
        assertFalse(tileAdded);
        assertEquals(m1.getMeldType(), MeldType.RUN);
        nextTile = new ArrayList<>();
    }
    
    // Add tiles to a SET that will make it invalid
    public void testAddTileInvalidSet() {
        Meld m1 = new Meld();
        ArrayList<Tile> nextTile = new ArrayList<>();
        boolean tileAdded;
        
        // Initial case: a SET with 3 tiles to the empty meld
        nextTile.add(new Tile(Colour.ORANGE, 1));
        nextTile.add(new Tile(Colour.BLUE, 1));
        nextTile.add(new Tile(Colour.RED, 1));

        tileAdded = m1.addTile(nextTile);

        assertEquals(true, tileAdded);
        assertEquals(MeldType.SET, m1.getMeldType());
        nextTile = new ArrayList<>();

        // Add a same colour/same value fourth tile
        nextTile.add(new Tile(Colour.RED, 3));
        
        tileAdded = m1.addTile(nextTile);
        
        assertFalse(tileAdded);
        assertEquals(MeldType.SET, m1.getMeldType());
        nextTile = new ArrayList<>();
        
        // Add a different colour/same value fourth tile
        nextTile.add(new Tile(Colour.GREEN, 3));
        
        tileAdded = m1.addTile(nextTile);
        
        assertFalse(tileAdded);
        assertEquals(MeldType.SET, m1.getMeldType());
        nextTile = new ArrayList<>();
        
        // Add a different colour/different value fourth tile
        nextTile.add(new Tile(Colour.GREEN, 5));
        
        // Add the tiles to the meld
        tileAdded = m1.addTile(nextTile);
        
        assertFalse(tileAdded);
        assertEquals(MeldType.SET, m1.getMeldType());
        nextTile = new ArrayList<>();
    }
}