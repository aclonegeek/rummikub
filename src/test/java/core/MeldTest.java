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
            Meld meld = new Meld();
            ArrayList<Tile> meldTiles = new ArrayList<>();
            boolean tilesAdded;

            // Initial case: a RUN with 3 tiles to the empty meld
            meldTiles.add(new Tile(colour, 3));
            meldTiles.add(new Tile(colour, 4));
            meldTiles.add(new Tile(colour, 5));

            // Add the tiles to the meld
            tilesAdded = meld.addTile(meldTiles);

            // Check if tile was successfully added and is of type RUN
            assertTrue(tilesAdded);
            assertEquals(MeldType.RUN, meld.getMeldType());
            meldTiles = new ArrayList<>();

            // Add more tiles to the back of an existing RUN
            meldTiles = new ArrayList<>();
            meldTiles.add(new Tile(colour, 6));
            meldTiles.add(new Tile(colour, 7));
            meldTiles.add(new Tile(colour, 8));

            tilesAdded = meld.addTile(meldTiles);
            assertTrue(tilesAdded);
            assertEquals(MeldType.RUN, meld.getMeldType());
            meldTiles = new ArrayList<>();

            // Add more tiles to the front of an existing RUN
            meldTiles = new ArrayList<>();
            meldTiles.add(new Tile(colour, 1));
            meldTiles.add(new Tile(colour, 2));
            tilesAdded = meld.addTile(meldTiles);
            assertTrue(tilesAdded);
            assertEquals(MeldType.RUN, meld.getMeldType());
            meldTiles = new ArrayList<>();
        }
    }

    // Add tiles to a meld to make a valid SET
    public void testAddTileValidSet() {
        Meld meld = new Meld();
        ArrayList<Tile> meldTiles = new ArrayList<>();
        boolean tilesAdded;

        // Initial case: a SET with 3 tiles to the empty meld
        meldTiles.add(new Tile(Colour.RED, 3));
        meldTiles.add(new Tile(Colour.GREEN, 3));
        meldTiles.add(new Tile(Colour.BLUE, 3));
        tilesAdded = meld.addTile(meldTiles);
        assertTrue(tilesAdded);
        assertEquals(MeldType.SET, meld.getMeldType());
        meldTiles = new ArrayList<>();

        // Add a fourth tile to the SET to make it valid
        meldTiles.add(new Tile(Colour.ORANGE, 3));
        tilesAdded = meld.addTile(meldTiles);
        assertTrue(tilesAdded);
        assertEquals(MeldType.SET, meld.getMeldType());
        meldTiles = new ArrayList<>();
    }
    
    // Add tiles to an empty meld that will make it invalid
    public void testAddTileInvalidInitialMeld() {
        Meld meld = new Meld();
        ArrayList<Tile> meldTiles = new ArrayList<>();
        boolean tilesAdded;
        
        // Initial case: An invalid set of tiles added to an empty meld
        meldTiles.add(new Tile(Colour.RED, 3));
        meldTiles.add(new Tile(Colour.GREEN, 4));
        meldTiles.add(new Tile(Colour.BLUE, 5));
        tilesAdded = meld.addTile(meldTiles);

        // Check if tile was not successfully added as is INVALID
        assertFalse(tilesAdded);
        assertEquals(MeldType.INVALID, meld.getMeldType());
        meldTiles = new ArrayList<>();
        
        // An invalid set of tile (even if its a potential meld) added to an empty meld
        meldTiles.add(new Tile(Colour.RED, 3));
        meldTiles.add(new Tile(Colour.RED, 3));
        tilesAdded = meld.addTile(meldTiles);
        assertFalse(tilesAdded);
        assertEquals(MeldType.INVALID, meld.getMeldType());
        meldTiles = new ArrayList<>();
    }
    
    // Add tiles to a RUN that will make it invalid
    public void testAddTileInvalidRun() {
        Meld meld = new Meld();
        ArrayList<Tile> meldTiles = new ArrayList<>();
        boolean tilesAdded;
        
        // Initial case: a RUN with 3 tiles to the empty meld
        meldTiles.add(new Tile(Colour.RED, 3));
        meldTiles.add(new Tile(Colour.RED, 4));
        meldTiles.add(new Tile(Colour.RED, 5));
        tilesAdded = meld.addTile(meldTiles);
        meldTiles = new ArrayList<>();

        // Add a same colour/same value fourth tile
        meldTiles.add(new Tile(Colour.RED, 3));
        tilesAdded = meld.addTile(meldTiles);
        assertFalse(tilesAdded);
        assertEquals(meld.getMeldType(), MeldType.RUN);
        meldTiles = new ArrayList<>();
        
        // Add a same colour/different value fourth tile to the RUN
        meldTiles.add(new Tile(Colour.RED, 7));
        tilesAdded = meld.addTile(meldTiles);
        assertFalse(tilesAdded);
        assertEquals(meld.getMeldType(), MeldType.RUN);
        meldTiles = new ArrayList<>();
        
        // Add a different colour/same value fourth tile
        meldTiles.add(new Tile(Colour.GREEN, 3));
        tilesAdded = meld.addTile(meldTiles);
        assertFalse(tilesAdded);
        assertEquals(meld.getMeldType(), MeldType.RUN);
        meldTiles = new ArrayList<>();
        
        // Add a different colour/different value fourth tile
        meldTiles.add(new Tile(Colour.GREEN, 5));
        tilesAdded = meld.addTile(meldTiles);
        assertFalse(tilesAdded);
        assertEquals(meld.getMeldType(), MeldType.RUN);
        meldTiles = new ArrayList<>();
    }
    
    // Add tiles to a SET that will make it invalid
    public void testAddTileInvalidSet() {
        Meld meld = new Meld();
        ArrayList<Tile> meldTiles = new ArrayList<>();
        boolean tilesAdded;
        
        // Initial case: a SET with 3 tiles to the empty meld
        meldTiles.add(new Tile(Colour.ORANGE, 1));
        meldTiles.add(new Tile(Colour.BLUE, 1));
        meldTiles.add(new Tile(Colour.RED, 1));
        tilesAdded = meld.addTile(meldTiles);
        assertEquals(true, tilesAdded);
        assertEquals(MeldType.SET, meld.getMeldType());
        meldTiles = new ArrayList<>();

        // Add a same colour/same value fourth tile
        meldTiles.add(new Tile(Colour.RED, 3));
        tilesAdded = meld.addTile(meldTiles);  
        assertFalse(tilesAdded);
        assertEquals(MeldType.SET, meld.getMeldType());
        meldTiles = new ArrayList<>();
        
        // Add a different colour/same value fourth tile
        meldTiles.add(new Tile(Colour.GREEN, 3));
        tilesAdded = meld.addTile(meldTiles);
        assertFalse(tilesAdded);
        assertEquals(MeldType.SET, meld.getMeldType());
        meldTiles = new ArrayList<>();
        
        // Add a different colour/different value fourth tile
        meldTiles.add(new Tile(Colour.GREEN, 5));
        tilesAdded = meld.addTile(meldTiles);
        assertFalse(tilesAdded);
        assertEquals(MeldType.SET, meld.getMeldType());
        meldTiles = new ArrayList<>();
    }
    
    // Remove tiles from a RUN and ensure validity
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
    
    // Remove tiles from a SET and ensure validity
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
        
        // Remove middle element to break the run
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
        
        // Initial case: a SET with 3 tiles to the empty meld
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
    
    public void testSplitMeld() {
        Meld meld = new Meld();
        ArrayList<Tile> meldTiles = new ArrayList<>();
        
        meldTiles.add(new Tile(Colour.RED, 1));
        meldTiles.add(new Tile(Colour.RED, 2));
        meldTiles.add(new Tile(Colour.RED, 3));
        meldTiles.add(new Tile(Colour.RED, 4));
        meldTiles.add(new Tile(Colour.RED, 5));
        meldTiles.add(new Tile(Colour.RED, 6));
        meld.addTile(meldTiles);
        meldTiles = new ArrayList<>();
       
        // Split in half
        Meld returnedMeld1;
        returnedMeld1 = meld.splitMeld(meld.getSize() / 2);
        assertEquals("{R1,R2,R3}", meld.toString());
        assertEquals("{R4,R5,R6}", returnedMeld1.toString());
        
        // Split at front
        Meld returnedMeld2;
        returnedMeld2 = meld.splitMeld(0);
        assertEquals(0, meld.getSize());
        assertEquals("{R1,R2,R3}", returnedMeld2.toString());
        
        // Split at back
        Meld returnedMeld3;
        returnedMeld3 = returnedMeld2.splitMeld(returnedMeld2.getSize());
        assertEquals("{R1,R2,R3}", returnedMeld2.toString());
        assertEquals(0, returnedMeld3.getSize()); 
    }
    
    public void testToString() {
        Meld meld = new Meld();
        ArrayList<Tile> meldTiles = new ArrayList<>();
        
        // Initial case: a RUN with 3 tiles to the empty meld
        meldTiles.add(new Tile(Colour.ORANGE, 1));
        meldTiles.add(new Tile(Colour.BLUE, 1));
        meldTiles.add(new Tile(Colour.RED, 1));
        meld.addTile(meldTiles);
        assertEquals("{O1,B1,R1}", meld.toString());
    }
    
    public void testGetValue() {
        Meld meld = new Meld();
        assertEquals(0, meld.getValue());
        
        ArrayList<Tile> meldTiles = new ArrayList<>();
        meldTiles.add(new Tile(Colour.GREEN, 10));
        meldTiles.add(new Tile(Colour.GREEN, 11));
        meldTiles.add(new Tile(Colour.GREEN, 12));
        meld.addTile(meldTiles);
        assertEquals(33, meld.getValue());
    }
}