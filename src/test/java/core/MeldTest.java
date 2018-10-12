package core;

import java.util.ArrayList;
import java.util.Random;

import core.Globals.Colour;
import core.Meld.MeldType;
import junit.framework.TestCase;

public class MeldTest extends TestCase {
    // Add tiles to a meld to make a valid RUN
    public void testAddTileValidRun() {
        // Test each colour
        for (Colour colour : Colour.values()) {
            Meld meld = new Meld();
            ArrayList<Tile> meldTiles = new ArrayList<>();
            boolean tileAdded;

            // Initial case: a RUN with 3 tiles to the empty meld
            meldTiles.add(new Tile(colour, 3));
            meldTiles.add(new Tile(colour, 4));
            meldTiles.add(new Tile(colour, 5));

            // Add the tiles to the meld
            tileAdded = meld.addTile(meldTiles);

            // Check if tile was successfully added and is of type RUN
            assertTrue(tileAdded);
            assertEquals(MeldType.RUN, meld.getMeldType());
            meldTiles = new ArrayList<>();

            // Add more tiles to the back of an existing RUN
            meldTiles = new ArrayList<>();
            meldTiles.add(new Tile(colour, 6));
            meldTiles.add(new Tile(colour, 7));
            meldTiles.add(new Tile(colour, 8));

            tileAdded = meld.addTile(meldTiles);
            assertTrue(tileAdded);
            assertEquals(MeldType.RUN, meld.getMeldType());
            meldTiles = new ArrayList<>();

            // Add more tiles to the front of an existing RUN
            meldTiles = new ArrayList<>();
            meldTiles.add(new Tile(colour, 1));
            meldTiles.add(new Tile(colour, 2));
            tileAdded = meld.addTile(meldTiles);
            assertTrue(tileAdded);
            assertEquals(MeldType.RUN, meld.getMeldType());
            meldTiles = new ArrayList<>();
        }
    }

    // Add tiles to a meld to make a valid SET
    public void testAddTileValidSet() {
        Meld meld = new Meld();
        ArrayList<Tile> meldTiles = new ArrayList<>();
        boolean tileAdded;

        // Initial case: a SET with 3 tiles to the empty meld
        meldTiles.add(new Tile(Colour.RED, 3));
        meldTiles.add(new Tile(Colour.GREEN, 3));
        meldTiles.add(new Tile(Colour.BLUE, 3));
        tileAdded = meld.addTile(meldTiles);
        assertTrue(tileAdded);
        assertEquals(MeldType.SET, meld.getMeldType());
        meldTiles = new ArrayList<>();

        // Add a fourth tile to the SET to make it valid
        meldTiles.add(new Tile(Colour.ORANGE, 3));
        tileAdded = meld.addTile(meldTiles);
        assertTrue(tileAdded);
        assertEquals(MeldType.SET, meld.getMeldType());
        meldTiles = new ArrayList<>();
    }
    
    // Add tiles to an empty meld that will make it invalid
    public void testAddTileInvalidInitialMeld() {
        Meld meld = new Meld();
        ArrayList<Tile> meldTiles = new ArrayList<>();
        boolean tileAdded;
        
        // Initial case: An invalid set of tiles added to an empty meld
        meldTiles.add(new Tile(Colour.RED, 3));
        meldTiles.add(new Tile(Colour.GREEN, 4));
        meldTiles.add(new Tile(Colour.BLUE, 5));
        tileAdded = meld.addTile(meldTiles);

        // Check if tile was not successfully added as is INVALID
        assertFalse(tileAdded);
        assertEquals(MeldType.INVALID, meld.getMeldType());
        meldTiles = new ArrayList<>();
        
        // An invalid set of tile (even if its a potential meld) added to an empty meld
        meldTiles.add(new Tile(Colour.RED, 3));
        meldTiles.add(new Tile(Colour.RED, 3));
        tileAdded = meld.addTile(meldTiles);
        assertFalse(tileAdded);
        assertEquals(MeldType.INVALID, meld.getMeldType());
        meldTiles = new ArrayList<>();
    }
    
    // Add tiles to a RUN that will make it invalid
    public void testAddTileInvalidRun() {
        Meld meld = new Meld();
        ArrayList<Tile> meldTiles = new ArrayList<>();
        boolean tileAdded;
        
        // Initial case: a RUN with 3 tiles to the empty meld
        meldTiles.add(new Tile(Colour.RED, 3));
        meldTiles.add(new Tile(Colour.RED, 4));
        meldTiles.add(new Tile(Colour.RED, 5));
        tileAdded = meld.addTile(meldTiles);
        meldTiles = new ArrayList<>();

        // Add a same colour/same value fourth tile
        meldTiles.add(new Tile(Colour.RED, 3));
        tileAdded = meld.addTile(meldTiles);
        assertFalse(tileAdded);
        assertEquals(meld.getMeldType(), MeldType.RUN);
        meldTiles = new ArrayList<>();
        
        // Add a same colour/different value fourth tile to the RUN
        meldTiles.add(new Tile(Colour.RED, 7));
        tileAdded = meld.addTile(meldTiles);
        assertFalse(tileAdded);
        assertEquals(meld.getMeldType(), MeldType.RUN);
        meldTiles = new ArrayList<>();
        
        // Add a different colour/same value fourth tile
        meldTiles.add(new Tile(Colour.GREEN, 3));
        tileAdded = meld.addTile(meldTiles);
        assertFalse(tileAdded);
        assertEquals(meld.getMeldType(), MeldType.RUN);
        meldTiles = new ArrayList<>();
        
        // Add a different colour/different value fourth tile
        meldTiles.add(new Tile(Colour.GREEN, 5));
        tileAdded = meld.addTile(meldTiles);
        assertFalse(tileAdded);
        assertEquals(meld.getMeldType(), MeldType.RUN);
        meldTiles = new ArrayList<>();
    }
    
    // Add tiles to a SET that will make it invalid
    public void testAddTileInvalidSet() {
        Meld meld = new Meld();
        ArrayList<Tile> meldTiles = new ArrayList<>();
        boolean tileAdded;
        
        // Initial case: a SET with 3 tiles to the empty meld
        meldTiles.add(new Tile(Colour.ORANGE, 1));
        meldTiles.add(new Tile(Colour.BLUE, 1));
        meldTiles.add(new Tile(Colour.RED, 1));
        tileAdded = meld.addTile(meldTiles);
        assertEquals(true, tileAdded);
        assertEquals(MeldType.SET, meld.getMeldType());
        meldTiles = new ArrayList<>();

        // Add a same colour/same value fourth tile
        meldTiles.add(new Tile(Colour.RED, 3));
        tileAdded = meld.addTile(meldTiles);  
        assertFalse(tileAdded);
        assertEquals(MeldType.SET, meld.getMeldType());
        meldTiles = new ArrayList<>();
        
        // Add a different colour/same value fourth tile
        meldTiles.add(new Tile(Colour.GREEN, 3));
        tileAdded = meld.addTile(meldTiles);
        assertFalse(tileAdded);
        assertEquals(MeldType.SET, meld.getMeldType());
        meldTiles = new ArrayList<>();
        
        // Add a different colour/different value fourth tile
        meldTiles.add(new Tile(Colour.GREEN, 5));
        tileAdded = meld.addTile(meldTiles);
        assertFalse(tileAdded);
        assertEquals(MeldType.SET, meld.getMeldType());
        meldTiles = new ArrayList<>();
    }
    
    // Remove tiles from a RUN that will make it valid
    public void testRemoveTileValidRun() {
        Meld meld = new Meld();
        ArrayList<Tile> meldTiles = new ArrayList<>();
        
        // Initial case: a RUN with 3 tiles to the empty meld
        meldTiles.add(new Tile(Colour.RED, 3));
        meldTiles.add(new Tile(Colour.RED, 4));
        meldTiles.add(new Tile(Colour.RED, 5));
        meldTiles.add(new Tile(Colour.RED, 6));
        meldTiles.add(new Tile(Colour.RED, 7));
        meld.addTile(meldTiles);
        meldTiles = new ArrayList<>();
        
        // Remove last element
        meld.removeTile(meld.getSize() - 1);
        assertEquals(MeldType.RUN, meld.getMeldType());
        
        // Remove first element
        meld.removeTile(0);
        assertEquals(MeldType.RUN, meld.getMeldType());   
    }
    
    // Remove tiles from a SET that will make it valid
    public void testRemoveTileValidSet() {
        Meld meld = new Meld();
        ArrayList<Tile> meldTiles = new ArrayList<>();
        
        // Initial case: a SET with 3 tiles to the empty meld
        meldTiles.add(new Tile(Colour.ORANGE, 1));
        meldTiles.add(new Tile(Colour.BLUE, 1));
        meldTiles.add(new Tile(Colour.RED, 1));
        meldTiles.add(new Tile(Colour.GREEN, 1));
        meld.addTile(meldTiles);
        meldTiles = new ArrayList<>();
        
        // Remove last element
        meld.removeTile(meld.getSize() - 1);
        assertEquals(MeldType.SET, meld.getMeldType());
        meldTiles.add(new Tile(Colour.GREEN, 1));
        meld.addTile(meldTiles);
        meldTiles = new ArrayList<>();
        
        // Remove first element
        meld.removeTile(0);
        assertEquals(MeldType.SET, meld.getMeldType()); 
        meldTiles.add(new Tile(Colour.ORANGE, 1));
        meld.addTile(meldTiles);
        meldTiles = new ArrayList<>();
        
        // Remove middle element
        meld.removeTile(2);
        assertEquals(MeldType.SET, meld.getMeldType()); 
    }
    
    // Remove tiles from a RUN that will make it invalid
    public void testRemoveTileInvalidRun() {
        Meld meld = new Meld();
        ArrayList<Tile> meldTiles = new ArrayList<>();
        
        // Initial case: a RUN with 3 tiles to the empty meld
        meldTiles.add(new Tile(Colour.RED, 3));
        meldTiles.add(new Tile(Colour.RED, 4));
        meldTiles.add(new Tile(Colour.RED, 5));
        meldTiles.add(new Tile(Colour.RED, 6));
        meld.addTile(meldTiles);
        meldTiles = new ArrayList<>();
        
        // Remove any element
        meld.removeTile(2);
        assertEquals(MeldType.INVALID, meld.getMeldType());
        
        // Remove any element again
        meld.removeTile(2);
        // Expected NONE because melds less than size 3 are yet to be determined 
        // and may be part of an intermediate move
        assertEquals(MeldType.NONE, meld.getMeldType());
    }
    
    // Remove tiles from a SET that will make it invalid
    public void testRemoveTileInvalidSet() {
        Meld meld = new Meld();
        ArrayList<Tile> meldTiles = new ArrayList<>();
        
        // Initial case: a RUN with 3 tiles to the empty meld
        meldTiles.add(new Tile(Colour.ORANGE, 1));
        meldTiles.add(new Tile(Colour.BLUE, 1));
        meldTiles.add(new Tile(Colour.RED, 1));
        meld.addTile(meldTiles);
        meldTiles = new ArrayList<>();
       
        // Remove any element
        meld.removeTile(0);
        // Expected NONE because melds less than size 3 are yet to be determined 
        // and may be part of an intermediate move
        assertEquals(MeldType.NONE, meld.getMeldType());
    }
}