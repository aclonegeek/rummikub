package core;

import java.util.ArrayList;

import com.sun.org.apache.bcel.internal.generic.NEW;

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
            Tile tilesAdded;

            // Initial case: a RUN with 3 tiles to the empty meld
            meldTiles.add(new Tile(colour, 3));
            meldTiles.add(new Tile(colour, 4));
            meldTiles.add(new Tile(colour, 5));

            // Add the tiles to the meld
            tilesAdded = meld.addTile(meldTiles);

            // Check if tile was successfully added and is of type RUN
            assertNull(tilesAdded.colour);
            assertEquals(MeldType.RUN, meld.getMeldType());
            meldTiles = new ArrayList<>();

            // Add more tiles to the back of an existing RUN
            meldTiles = new ArrayList<>();
            meldTiles.add(new Tile(colour, 6));
            meldTiles.add(new Tile(colour, 7));
            meldTiles.add(new Tile(colour, 8));

            tilesAdded = meld.addTile(meldTiles);
            assertNull(tilesAdded.colour);
            assertEquals(MeldType.RUN, meld.getMeldType());
            meldTiles = new ArrayList<>();

            // Add more tiles to the front of an existing RUN
            meldTiles = new ArrayList<>();
            meldTiles.add(new Tile(colour, 1));
            meldTiles.add(new Tile(colour, 2));
            tilesAdded = meld.addTile(meldTiles);
            assertNull(tilesAdded.colour);
            assertEquals(MeldType.RUN, meld.getMeldType());
            meldTiles = new ArrayList<>();
        }
    }

    // Add tiles to a meld to make a valid SET
    public void testAddTileValidSet() {
        Meld meld = new Meld();
        ArrayList<Tile> meldTiles = new ArrayList<>();
        Tile tilesAdded;

        // Initial case: a SET with 3 tiles to the empty meld
        meldTiles.add(new Tile(Colour.RED, 3));
        meldTiles.add(new Tile(Colour.GREEN, 3));
        meldTiles.add(new Tile(Colour.BLUE, 3));
        tilesAdded = meld.addTile(meldTiles);
        assertNull(tilesAdded.colour);
        assertEquals(MeldType.SET, meld.getMeldType());
        meldTiles = new ArrayList<>();

        // Add a fourth tile to the SET to make it valid
        meldTiles.add(new Tile(Colour.ORANGE, 3));
        tilesAdded = meld.addTile(meldTiles);
        assertNull(tilesAdded.colour);
        assertEquals(MeldType.SET, meld.getMeldType());
        meldTiles = new ArrayList<>();
    }

    // Add tiles to an empty meld that will make it invalid
    public void testAddTileInvalidInitialMeld() {
        Meld meld = new Meld();
        ArrayList<Tile> meldTiles = new ArrayList<>();
        Tile tilesAdded;

        // Initial case: An invalid set of tiles added to an empty meld
        meldTiles.add(new Tile(Colour.RED, 3));
        meldTiles.add(new Tile(Colour.GREEN, 4));
        meldTiles.add(new Tile(Colour.BLUE, 5));
        tilesAdded = meld.addTile(meldTiles);

        // Check if tile was not successfully added as is INVALID
        assertNull(tilesAdded);
        assertEquals(MeldType.INVALID, meld.getMeldType());
        meldTiles = new ArrayList<>();

        // An invalid set of tile (even if its a potential meld) added to an empty meld
        meldTiles.add(new Tile(Colour.RED, 3));
        meldTiles.add(new Tile(Colour.RED, 3));
        tilesAdded = meld.addTile(meldTiles);
        assertNull(tilesAdded);
        assertEquals(MeldType.INVALID, meld.getMeldType());
        meldTiles = new ArrayList<>();
    }

    // Add tiles to a RUN that will make it invalid
    public void testAddTileInvalidRun() {
        Meld meld = new Meld();
        ArrayList<Tile> meldTiles = new ArrayList<>();
        Tile tilesAdded;

        // Initial case: a RUN with 3 tiles to the empty meld
        meldTiles.add(new Tile(Colour.RED, 3));
        meldTiles.add(new Tile(Colour.RED, 4));
        meldTiles.add(new Tile(Colour.RED, 5));
        tilesAdded = meld.addTile(meldTiles);
        meldTiles = new ArrayList<>();

        // Add a same colour/same value fourth tile
        meldTiles.add(new Tile(Colour.RED, 3));
        tilesAdded = meld.addTile(meldTiles);
        assertNull(tilesAdded);
        assertEquals(meld.getMeldType(), MeldType.RUN);
        meldTiles = new ArrayList<>();

        // Add a same colour/different value fourth tile to the RUN
        meldTiles.add(new Tile(Colour.RED, 7));
        tilesAdded = meld.addTile(meldTiles);
        assertNull(tilesAdded);
        assertEquals(meld.getMeldType(), MeldType.RUN);
        meldTiles = new ArrayList<>();

        // Add a different colour/same value fourth tile
        meldTiles.add(new Tile(Colour.GREEN, 3));
        tilesAdded = meld.addTile(meldTiles);
        assertNull(tilesAdded);
        assertEquals(meld.getMeldType(), MeldType.RUN);
        meldTiles = new ArrayList<>();

        // Add a different colour/different value fourth tile
        meldTiles.add(new Tile(Colour.GREEN, 5));
        tilesAdded = meld.addTile(meldTiles);
        assertNull(tilesAdded);
        assertEquals(meld.getMeldType(), MeldType.RUN);
        meldTiles = new ArrayList<>();
    }

    // Add tiles to a SET that will make it invalid
    public void testAddTileInvalidSet() {
        Meld meld = new Meld();
        ArrayList<Tile> meldTiles = new ArrayList<>();
        Tile tilesAdded;

        // Initial case: a SET with 3 tiles to the empty meld
        meldTiles.add(new Tile(Colour.ORANGE, 1));
        meldTiles.add(new Tile(Colour.BLUE, 1));
        meldTiles.add(new Tile(Colour.RED, 1));
        tilesAdded = meld.addTile(meldTiles);
        assertNull(tilesAdded.colour);
        assertEquals(MeldType.SET, meld.getMeldType());
        meldTiles = new ArrayList<>();

        // Add a same colour/same value fourth tile
        meldTiles.add(new Tile(Colour.RED, 3));
        tilesAdded = meld.addTile(meldTiles);
        assertNull(tilesAdded);
        assertEquals(MeldType.SET, meld.getMeldType());
        meldTiles = new ArrayList<>();

        // Add a different colour/same value fourth tile
        meldTiles.add(new Tile(Colour.GREEN, 3));
        tilesAdded = meld.addTile(meldTiles);
        assertNull(tilesAdded);
        assertEquals(MeldType.SET, meld.getMeldType());
        meldTiles = new ArrayList<>();

        // Add a different colour/different value fourth tile
        meldTiles.add(new Tile(Colour.GREEN, 5));
        tilesAdded = meld.addTile(meldTiles);
        assertNull(tilesAdded);
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
        assertEquals(MeldType.POTENTIAL, meld.getMeldType());
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
        assertEquals(MeldType.POTENTIAL, meld.getMeldType());
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
        assertEquals("{R1 R2 R3}", meld.toString());
        assertEquals("{R4 R5 R6}", returnedMeld1.toString());

        // Split at front - invalid
        Meld returnedMeld2;
        returnedMeld2 = meld.splitMeld(0);
        assertEquals("{R1 R2 R3}", meld.toString());
        assertEquals(null, returnedMeld2);

        // Split at end - invalid
        Meld returnedMeld3;
        returnedMeld3 = meld.splitMeld(meld.getSize());
        assertEquals("{R1 R2 R3}", meld.toString());
        assertEquals(null, returnedMeld3);
    }

    public void testToString() {
        Meld meld = new Meld();
        ArrayList<Tile> meldTiles = new ArrayList<>();

        // Initial case: a RUN with 3 tiles to the empty meld
        meldTiles.add(new Tile(Colour.ORANGE, 1));
        meldTiles.add(new Tile(Colour.BLUE, 1));
        meldTiles.add(new Tile(Colour.RED, 1));
        meld.addTile(meldTiles);
        assertEquals("{O1 B1 R1}", meld.toString());
        
        // With joker
        Meld meld2 = new Meld("G1,G2,J");
        meld2.addTile(new Tile("G3"));
        assertEquals("{G1 G2 G3 J}", meld2.toString());
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

    public void testContainsTile() {
        Meld meld = new Meld();
        ArrayList<Tile> meldTiles = new ArrayList<>();
        meldTiles.add(new Tile(Colour.GREEN, 10));
        meldTiles.add(new Tile(Colour.GREEN, 11));
        meldTiles.add(new Tile(Colour.GREEN, 12));
        Tile tile1 = new Tile(Colour.GREEN, 10);
        Tile tile2 = new Tile(Colour.BLUE, 7);
        meld.addTile(meldTiles);
        assertTrue(meld.containsTile(tile1));
        assertFalse(meld.containsTile(tile2));
    }

    public void testContainsTileValue() {
        Meld meld = new Meld("G1,G2,G3,G4");
        assertTrue(meld.containsTileValue(new Tile("G1")));
        assertTrue(meld.containsTileValue(new Tile("R1")));
        assertTrue(meld.containsTileValue(new Tile("B4")));
        assertFalse(meld.containsTileValue(new Tile("G5")));
    }

    public void testEquals() {
        Meld meld1 = new Meld();
        Meld meld2 = new Meld();
        Meld meld3 = new Meld();
        Meld meld4 = new Meld();
        Meld meld5 = new Meld();
        Meld meld6 = new Meld();

        meld1.addTile(new Tile(Colour.GREEN, 1));
        meld1.addTile(new Tile(Colour.GREEN, 2));
        meld1.addTile(new Tile(Colour.GREEN, 3));
        meld2.addTile(new Tile(Colour.GREEN, 1));
        meld2.addTile(new Tile(Colour.GREEN, 2));
        meld2.addTile(new Tile(Colour.GREEN, 3));
        meld3.addTile(new Tile(Colour.GREEN, 8));
        meld3.addTile(new Tile(Colour.BLUE, 8));
        meld3.addTile(new Tile(Colour.RED, 8));
        meld4.addTile(new Tile(Colour.RED, 8));
        meld4.addTile(new Tile(Colour.BLUE, 8));
        meld4.addTile(new Tile(Colour.GREEN, 8));
        meld5.addTile(new Tile(Colour.RED, 2));
        meld5.addTile(new Tile(Colour.RED, 3));
        meld5.addTile(new Tile(Colour.RED, 4));
        meld6.addTile(new Tile(Colour.RED, 1));

        assertTrue(meld1.equals(meld2));
        assertTrue(meld2.equals(meld1));
        assertTrue(meld3.equals(meld4));
        assertTrue(meld4.equals(meld3));
        assertFalse(meld1.equals(meld5));
        assertFalse(meld5.equals(meld1));
        assertFalse(meld2.equals(meld6));
    }

    public void testStringConstructor() {
        Meld meld1 = new Meld();
        meld1.addTile(new Tile(Colour.GREEN, 1));
        meld1.addTile(new Tile(Colour.GREEN, 2));
        meld1.addTile(new Tile(Colour.GREEN, 3));

        Meld meld2 = new Meld("G1,G2,G3");
        assertEquals(meld1.toString(), meld2.toString());
        assertEquals(meld2.isValidMeld(), true);

        Meld meld3 = new Meld();
        meld3.addTile(new Tile(Colour.GREEN, 1));
        meld3.addTile(new Tile(Colour.GREEN, 2));

        Meld meld4 = new Meld("G1,G2");
        assertEquals(meld3.toString(), meld4.toString());
        assertEquals(meld4.isValidMeld(), false);

        Meld meld5 = new Meld();
        Meld meld6 = new Meld("");
        assertEquals(meld5.toString(), meld6.toString());
    }

    public void testIsValidIfRemoveTile() {
        Meld meld1 = new Meld("R1,R2,R3,R4");
        assertTrue(meld1.isValidIfRemoveTile(0));

        Meld meld2 = new Meld("R1,R2,R3");
        assertFalse(meld2.isValidIfRemoveTile(0));
    }
    
    // Add a joker to a RUN
    public void testAddJokerRun() {
        Meld meld;
        ArrayList<Tile> meldTiles;
        Tile releasedJoker;
        Tile joker = new Tile("J");
        
        // Force joker to back (normal meld)
        meldTiles = new ArrayList<>();
        meld = new Meld("O1,O2,O3,O4,O5,O6,O7,O8,O9,O10,O11,O12");
        meldTiles.add(joker);
        releasedJoker = meld.addTile(meldTiles);
        assertNull(releasedJoker.colour);
        assertEquals(MeldType.RUN, meld.getMeldType());
        assertEquals("{O1 O2 O3 O4 O5 O6 O7 O8 O9 O10 O11 O12 J}", meld.toString());
        
        // Force joker to front (normal meld)
        meldTiles = new ArrayList<>();
        meld = new Meld("G2,G3,G4,G5,G6,G7,G8,G9,G10,G11,G12,G13");
        meldTiles.add(joker);
        releasedJoker = meld.addTile(meldTiles);
        assertNull(releasedJoker.colour);
        assertEquals(MeldType.RUN, meld.getMeldType());
        assertEquals("{J G2 G3 G4 G5 G6 G7 G8 G9 G10 G11 G12 G13}", meld.toString());
    }
    
    // Add a joker to a potential RUN
    public void testAddJokerPotentialRun() {
        Meld meld;
        ArrayList<Tile> meldTiles;
        Tile releasedJoker;
        Tile joker = new Tile("J");
        
        // Force joker to back (potential meld)
        meldTiles = new ArrayList<>();
        meld = new Meld("R1,R2");
        meldTiles.add(joker);
        releasedJoker = meld.addTile(meldTiles);
        assertNull(releasedJoker.colour);
        assertEquals(MeldType.RUN, meld.getMeldType());
        assertEquals("{R1 R2 J}", meld.toString());
        
        // Force joker to front (potential meld)
        meldTiles = new ArrayList<>();
        meld = new Meld("B12,B13");
        meldTiles.add(joker);
        releasedJoker = meld.addTile(meldTiles);
        assertNull(releasedJoker.colour);
        assertEquals(MeldType.RUN, meld.getMeldType());
        assertEquals("{J B12 B13}", meld.toString());
    }
    
    // Add a joker to a SET
    public void testAddJokerSet() {
        Meld meld;
        ArrayList<Tile> meldTiles;
        Tile releasedJoker;
        Tile joker = new Tile("J");
        
        // Default joker to back (normal meld)
        meldTiles = new ArrayList<>();
        meld = new Meld("R13,B13,G13");
        meldTiles.add(joker);
        releasedJoker = meld.addTile(meldTiles);
        assertNull(releasedJoker.colour);
        assertEquals(MeldType.SET, meld.getMeldType());
        assertEquals("{R13 B13 G13 J}", meld.toString());
    }
    
    // Add a joker to a potential SET
    public void testAddJokerPotentialSet() {
        Meld meld;
        ArrayList<Tile> meldTiles;
        Tile releasedJoker;
        Tile joker = new Tile("J");
        
        // Default joker to back (potential meld)
        meldTiles = new ArrayList<>();
        meld = new Meld("R1,B1");
        meldTiles.add(joker);
        releasedJoker = meld.addTile(meldTiles);
        assertNull(releasedJoker.colour);
        assertEquals(MeldType.SET, meld.getMeldType());
        assertEquals("{R1 B1 J}", meld.toString());
    }
    
    // Add a joker to a single tile to make it a potential meld
    public void testAddJokerValidPotentialMeld() {
        Meld meld;
        ArrayList<Tile> meldTiles;
        Tile releasedJoker;
        Tile joker = new Tile("J");
        
        // Default joker to back (potential run/set)
        meldTiles = new ArrayList<>();
        meld = new Meld("R1");
        meldTiles.add(joker);
        releasedJoker = meld.addTile(meldTiles);
        assertNull(releasedJoker.colour);
        assertEquals(MeldType.POTENTIAL, meld.getMeldType());
        assertEquals("{R1 J}", meld.toString());
        
        // Default joker to back (potential run/set)
        meldTiles = new ArrayList<>();
        meld = new Meld("R13");
        meldTiles.add(joker);
        releasedJoker = meld.addTile(meldTiles);
        assertNull(releasedJoker.colour);
        assertEquals(MeldType.POTENTIAL, meld.getMeldType());
        assertEquals("{R13 J}", meld.toString()); // Jokers favour sets rather than runs {R12 R13}
    }
    
    // Replace a joker in a RUN
    public void testReplaceJokerRun() {
        Meld meld;
        ArrayList<Tile> meldTiles;
        Tile tilesAdded;
        
        // Replace a joker at the back with single tile (potential meld)
        meldTiles = new ArrayList<>();
        meld = new Meld("R1,J");
        meldTiles.add(new Tile("R2"));
        meld.setIsInitialMeld(false); // Mimic a meld that is on the table and not coming from the hand
        
        tilesAdded = meld.addTile(meldTiles);
        assertTrue(tilesAdded.isJoker());
        assertEquals(MeldType.POTENTIAL, meld.getMeldType());
        assertEquals("{R1 R2}", meld.toString());
        
        // Replace a joker at the front with single tile (potential meld)
        meldTiles = new ArrayList<>();
        meld = new Meld("J,R2");
        meldTiles.add(new Tile("R1"));
        meld.setIsInitialMeld(false); // Mimic a meld that is on the table and not coming from the hand
        
        tilesAdded = meld.addTile(meldTiles);
        assertTrue(tilesAdded.isJoker());
        assertEquals(MeldType.POTENTIAL, meld.getMeldType());
        assertEquals("{R1 R2}", meld.toString());
        
        // Replace a joker at the front with single tile
        meldTiles = new ArrayList<>();
        meld = new Meld("J,R12,R13");
        meld.setIsInitialMeld(false); // Mimic a meld that is on the table and not coming from the hand
        
        meldTiles = new ArrayList<>();
        meldTiles.add(new Tile("R11"));
        tilesAdded = meld.addTile(meldTiles);
        assertTrue(tilesAdded.isJoker());
        assertEquals(MeldType.RUN, meld.getMeldType());
        assertEquals("{R11 R12 R13}", meld.toString());
        
        // Replace a joker at the back with single tile
        meldTiles = new ArrayList<>();
        meld = new Meld("O1,O2,J");
        meldTiles.add(new Tile("O4"));
        meld.setIsInitialMeld(false); // Mimic a meld that is on the table and not coming from the hand
        
        tilesAdded = meld.addTile(meldTiles);
        assertNull(tilesAdded.colour);
        assertEquals(MeldType.RUN, meld.getMeldType());
        assertEquals("{O1 O2 J O4}", meld.toString());
        
        // Replace a joker at the back with single tile
        meldTiles = new ArrayList<>();
        meld = new Meld("J,B11,B12");
        meldTiles.add(new Tile("B10"));
        meld.setIsInitialMeld(false); // Mimic a meld that is on the table and not coming from the hand
        
        tilesAdded = meld.addTile(meldTiles);
        assertTrue(tilesAdded.isJoker());
        assertEquals(MeldType.RUN, meld.getMeldType());
        assertEquals("{B10 B11 B12}", meld.toString());
        
        // Replace a joker in middle with single tile
        meldTiles = new ArrayList<>();
        meld = new Meld("G3,G4,G5,J,G7,G8");
        meldTiles.add(new Tile("G6"));
        meld.setIsInitialMeld(false); // Mimic a meld that is on the table and not coming from the hand
        
        tilesAdded = meld.addTile(meldTiles);
        assertTrue(tilesAdded.isJoker());
        assertEquals(MeldType.RUN, meld.getMeldType());
        assertEquals("{G3 G4 G5 G6 G7 G8}", meld.toString());
        
        // Replace a joker at the back of a full run with single tile
        meldTiles = new ArrayList<>();
        meld = new Meld("G3,G4,G5,G6,G7,J");
        meldTiles.add(new Tile("G8"));
        meld.setIsInitialMeld(false); // Mimic a meld that is on the table and not coming from the hand
        
        tilesAdded = meld.addTile(meldTiles);
        assertTrue(tilesAdded.isJoker());
        assertEquals(MeldType.RUN, meld.getMeldType());
        assertEquals("{G3 G4 G5 G6 G7 G8}", meld.toString());
        
        // Replace a joker at the back of a full run (that starts with a joker) with single tile
        meldTiles = new ArrayList<>();
        meld = new Meld("J,G4,G5,G6,G7,G8"); // The joker goes to the back of the meld, do not be confused!
        meldTiles.add(new Tile("G9"));
        meld.setIsInitialMeld(false); // Mimic a meld that is on the table and not coming from the hand
        
        tilesAdded = meld.addTile(meldTiles);
        assertTrue(tilesAdded.isJoker());
        assertEquals(MeldType.RUN, meld.getMeldType());
        assertEquals("{G4 G5 G6 G7 G8 G9}", meld.toString());
        
        // Replace a joker at the front of a full run with single tile
        meldTiles = new ArrayList<>();
        meld = new Meld("J,G8,G9,G10,G11,G12,G13");
        meld.setIsInitialMeld(false); // Mimic a meld that is on the table and not coming from the hand
        
        meldTiles.add(new Tile("G7"));
        tilesAdded = meld.addTile(meldTiles);
        assertTrue(tilesAdded.isJoker());
        assertEquals(MeldType.RUN, meld.getMeldType());
        assertEquals("{G7 G8 G9 G10 G11 G12 G13}", meld.toString());
    }
    
    // Replace a joker in a SET
    public void testReplaceJokerSet() {
        Meld meld;
        ArrayList<Tile> meldTiles;
        Tile tilesAdded;
        
        // Replace a joker at the back with single tile (potential meld)
        meldTiles = new ArrayList<>();
        meld = new Meld("R1,J");
        meldTiles.add(new Tile("B1"));
        meld.setIsInitialMeld(false); // Mimic a meld that is on the table and not coming from the hand
        
        tilesAdded = meld.addTile(meldTiles);
        assertTrue(tilesAdded.isJoker());
        assertEquals(MeldType.POTENTIAL, meld.getMeldType());
        assertEquals("{B1 R1}", meld.toString()); //TODO: Colour sorting
        
        // Replace a joker at the front with single tile (potential meld)
        meldTiles = new ArrayList<>();
        meld = new Meld("J,R2");
        meldTiles.add(new Tile("B2"));
        meld.setIsInitialMeld(false); // Mimic a meld that is on the table and not coming from the hand
        
        tilesAdded = meld.addTile(meldTiles);
        assertTrue(tilesAdded.isJoker());
        assertEquals(MeldType.POTENTIAL, meld.getMeldType());
        assertEquals("{B2 R2}", meld.toString()); //TODO: Colour sorting
        
        // Replace a joker at the front with single tile
        meldTiles = new ArrayList<>();
        meld = new Meld("J,R1,B1");
        meld.setIsInitialMeld(false); // Mimic a meld that is on the table and not coming from the hand
        
        meldTiles.add(new Tile("G1"));
        tilesAdded = meld.addTile(meldTiles);
        assertTrue(tilesAdded.isJoker());
        assertEquals(MeldType.SET, meld.getMeldType());
        assertEquals("{G1 R1 B1}", meld.toString()); //TODO: Colour sorting
        
        // Replace a joker at the back with single tile
        meldTiles = new ArrayList<>();
        meld = new Meld("R1,B1,J");
        meldTiles.add(new Tile("G1"));
        meld.setIsInitialMeld(false); // Mimic a meld that is on the table and not coming from the hand
        
        tilesAdded = meld.addTile(meldTiles);
        assertTrue(tilesAdded.isJoker());
        assertEquals(MeldType.SET, meld.getMeldType());
        assertEquals("{G1 R1 B1}", meld.toString()); //TODO: Colour sorting
        
        // Replace a joker at the back with single tile
        meldTiles = new ArrayList<>();
        meld = new Meld("B9,G9,J");
        meldTiles.add(new Tile("O9"));
        meld.setIsInitialMeld(false); // Mimic a meld that is on the table and not coming from the hand
        
        tilesAdded = meld.addTile(meldTiles);
        assertTrue(tilesAdded.isJoker());
        assertEquals(MeldType.SET, meld.getMeldType());
        assertEquals("{O9 B9 G9}", meld.toString()); //TODO: Colour sorting
        
        // Replace a joker in middle with single tile
        meldTiles = new ArrayList<>();
        meld = new Meld("R3,B3,J,O3");
        meldTiles.add(new Tile("G3"));
        meld.setIsInitialMeld(false); // Mimic a meld that is on the table and not coming from the hand
        
        tilesAdded = meld.addTile(meldTiles);
        assertTrue(tilesAdded.isJoker());
        assertEquals(MeldType.SET, meld.getMeldType());
        assertEquals("{G3 R3 B3 O3}", meld.toString()); //TODO: Colour sorting
        
        // Replace a joker at the back of a full set with single tile
        meldTiles = new ArrayList<>();
        meld = new Meld("R3,B3,G3,J");
        meldTiles.add(new Tile("O3"));
        meld.setIsInitialMeld(false); // Mimic a meld that is on the table and not coming from the hand
        
        tilesAdded = meld.addTile(meldTiles);
        assertTrue(tilesAdded.isJoker());
        assertEquals(MeldType.SET, meld.getMeldType());
        assertEquals("{O3 R3 B3 G3}", meld.toString()); //TODO: Colour sorting
        
        // Replace a joker at the front of a full set (that starts with a joker) with single tile
        meldTiles = new ArrayList<>();
        meld = new Meld("J,B4,G4,O4");
        meldTiles.add(new Tile("R4"));
        meld.setIsInitialMeld(false); // Mimic a meld that is on the table and not coming from the hand
        
        tilesAdded = meld.addTile(meldTiles);
        assertTrue(tilesAdded.isJoker());
        assertEquals(MeldType.SET, meld.getMeldType());
        assertEquals("{R4 B4 G4 O4}", meld.toString()); //TODO: Colour sorting
    }
    
    // Add a tile to a meld with an irreplaceable joker
    public void testIrreplaceableJoker() {
        Meld meld;
        ArrayList<Tile> meldTiles;
        Tile tilesAdded;
        
        // Add a tile to the back of a single meld
        meldTiles = new ArrayList<>();
        meld = new Meld("R1,J");
        meldTiles.add(new Tile("R3"));
        tilesAdded = meld.addTile(meldTiles);
        assertNull(tilesAdded.colour);
        assertEquals(MeldType.RUN, meld.getMeldType());
        assertEquals("{R1 J R3}", meld.toString());
        
        // Add a tile to the front of a single meld
        meldTiles = new ArrayList<>();
        meld = new Meld("J,R3");
        meldTiles.add(new Tile("R1"));
        tilesAdded = meld.addTile(meldTiles);
        assertNull(tilesAdded.colour);
        assertEquals(MeldType.RUN, meld.getMeldType());
        assertEquals("{R1 J R3}", meld.toString());
        
        // Add to the front of a potential meld
        meldTiles = new ArrayList<>();
        meld = new Meld("J,R12,R13");
        meldTiles.add(new Tile("R10"));
        tilesAdded = meld.addTile(meldTiles);
        
        assertNull(tilesAdded.colour);
        assertEquals(MeldType.RUN, meld.getMeldType());
        assertEquals("{R10 J R12 R13}", meld.toString());
        
        // Replace a joker at the back with single tile
        meldTiles = new ArrayList<>();
        meld = new Meld("O1,O2,J");
        meldTiles.add(new Tile("O4"));
        tilesAdded = meld.addTile(meldTiles);
        
        assertNull(tilesAdded.colour);
        assertEquals(MeldType.RUN, meld.getMeldType());
        assertEquals("{O1 O2 J O4}", meld.toString());
        
        // Replace a joker at the back with single tile
        meldTiles = new ArrayList<>();
        meld = new Meld("B12,B11,J");
        meldTiles.add(new Tile("B13"));
        tilesAdded = meld.addTile(meldTiles);
        
        assertNull(tilesAdded.colour);
        assertEquals(MeldType.RUN, meld.getMeldType());
        assertEquals("{J B11 B12 B13}", meld.toString());
        
        // Replace a joker in middle with single tile
        meldTiles = new ArrayList<>();
        meld = new Meld("G3,G4,G5,J,G7,G8");
        meldTiles.add(new Tile("G2"));
        tilesAdded = meld.addTile(meldTiles);
        
        assertNull(tilesAdded.colour);
        assertEquals(MeldType.RUN, meld.getMeldType());
        assertEquals("{G2 G3 G4 G5 J G7 G8}", meld.toString());
        
        // Replace a joker at the back of a full run with single tile
        meldTiles = new ArrayList<>();
        meld = new Meld("G3,G4,G5,G6,G7,J");
        meldTiles.add(new Tile("G9"));
        tilesAdded = meld.addTile(meldTiles);
        
        assertNull(tilesAdded.colour);
        assertEquals(MeldType.RUN, meld.getMeldType());
        assertEquals("{G3 G4 G5 G6 G7 J G9}", meld.toString());
        
        // Replace a joker at the back of a full run (that starts with a joker) with single tile
        meldTiles = new ArrayList<>();
        meld = new Meld("G4,G5,G6,G7,G8,J");
        meldTiles.add(new Tile("G9"));
        
        tilesAdded = meld.addTile(meldTiles);
        assertNull(tilesAdded.colour);
        assertEquals(MeldType.RUN, meld.getMeldType());
        assertEquals("{G4 G5 G6 G7 G8 G9 J}", meld.toString());
        
        // Replace a joker at the front of a full run with single tile
        meldTiles = new ArrayList<>();
        meld = new Meld("J,G8,G9,G10,G11,G12,G13");
        meldTiles.add(new Tile("G6"));
        
        tilesAdded = meld.addTile(meldTiles);
        assertNull(tilesAdded.colour);
        assertEquals(MeldType.RUN, meld.getMeldType());
        assertEquals("{G6 J G8 G9 G10 G11 G12 G13}", meld.toString());
        
        // Default joker to back (potential meld)
        Tile releasedJoker;
        meldTiles = new ArrayList<>();
        meld = new Meld("R2,R3");
        tilesAdded = meld.addTile(new Tile("J"));
        assertEquals("{R2 R3 J}", meld.toString());
        
        releasedJoker = meld.addTile(new Tile("R1"));
        assertNull(releasedJoker.colour);
        assertEquals(MeldType.RUN, meld.getMeldType());
        assertEquals("{R1 R2 R3 J}", meld.toString());
        
        // Default joker to back (potential meld)
        meldTiles = new ArrayList<>();
        meld = new Meld("R12,R13");
        
        tilesAdded = meld.addTile(new Tile("J"));
        assertEquals("{J R12 R13}", meld.toString());
        meld.setIsInitialMeld(false); // Mimic a meld that is on the table and not coming from the hand
        
        releasedJoker = meld.addTile(new Tile("R11"));
        assertNull(releasedJoker.colour);
        assertEquals(MeldType.RUN, meld.getMeldType());
        assertEquals("{R11 R12 R13}", meld.toString());
    }
    
    // Remove a joker from a meld 
    public void testRemoveJoker() {
        Meld meld;
        Tile removedTile;
        Tile joker;

        // Remove joker at the back
        meld = new Meld();
        meld = new Meld("R3,R4,R5,R6,J");
        meld.setIsLocked(false); // Unlock the meld
        
        removedTile = meld.removeTile(meld.getSize() - 1);
        assertEquals(MeldType.RUN, meld.getMeldType());
        assertTrue(removedTile.isJoker());
        assertNull(removedTile.getColour());
        assertEquals("{R3 R4 R5 R6}", meld.toString());
        
        // Remove joker at front
        meld = new Meld();
        joker = new Tile("J");
        
        meld.addTile(joker);
        meld.addTile(new Tile("R11")); 
        meld.addTile(new Tile("R12")); 
        meld.addTile(new Tile("R13"));
        meld.setIsLocked(false); // Unlock the meld
        
        removedTile = meld.removeTile(0);
        assertEquals(MeldType.RUN, meld.getMeldType());
        assertTrue(removedTile.isJoker());
        assertNull(removedTile.getColour());
        assertEquals("{R11 R12 R13}", meld.toString());
        
        // Remove joker in middle
        meld = new Meld();
        
        joker = new Tile("J");
        joker.setOnTable(true);
        
        meld.addTile(new Tile("R10")); 
        meld.addTile(joker);
        meld.addTile(new Tile("R12")); 
        meld.addTile(new Tile("R13")); 
        meld.setIsLocked(false); // Unlock the meld
        
        removedTile = meld.removeTile(1);
        assertEquals(MeldType.INVALID, meld.getMeldType());
        assertTrue(removedTile.isJoker()); 
        assertNull(removedTile.getColour());
        assertEquals("{R10 R12 R13}", meld.toString());
    }
    
    // Split a meld with a joker
    public void testSplitMeldWithJoker() {
        Meld meld = new Meld();

        meld.addTile(new Tile("R10")); 
        meld.addTile(new Tile("J"));
        meld.addTile(new Tile("R12")); 
        meld.addTile(new Tile("R13")); 
        meld.setIsLocked(false); // Unlock the meld

        // Split in half
        Meld returnedMeld1;
        returnedMeld1 = meld.splitMeld(meld.getSize() / 2);
        assertEquals("{R10 J}", meld.toString());
        assertEquals(MeldType.POTENTIAL, meld.getMeldType());
        assertEquals("{R12 R13}", returnedMeld1.toString());
        assertEquals(MeldType.POTENTIAL, returnedMeld1.getMeldType());

        // Split first in middle - invalid
        Meld returnedMeld2;
        returnedMeld2 = meld.splitMeld(1);
        assertEquals("{R10}", meld.toString());
        assertEquals("{J}", returnedMeld2.toString()); // Edge case when the meld has one joker left, reset it to 0
        assertEquals(0, returnedMeld2.getValue());
        assertEquals(MeldType.POTENTIAL, returnedMeld2.getMeldType());
    }
    
    // Test the restrictions on jokers and/or melds with jokers
    public void testJokerRestrictions() {
        Meld meld;
        Tile removedTile;
        Tile releasedJoker;
        ArrayList<Tile> meldTiles = new ArrayList<>();
        
        // Disallow removing jokers from a locked meld
        meldTiles = new ArrayList<>();
        meld = new Meld();
        
        meld.addTile(new Tile("R10")); 
        meld.addTile(new Tile("J"));
        meld.addTile(new Tile("R12")); 
        meld.addTile(new Tile("R13"));

        removedTile = meld.removeTile(1);
        assertEquals(MeldType.RUN, meld.getMeldType());
        assertNull(removedTile);
        assertEquals("{R10 J R12 R13}", meld.toString());
        
        // Disallow removing jokers from a locked meld
        meldTiles = new ArrayList<>();
        meld = new Meld();
        
        meld.addTile(new Tile("R10")); 
        meld.addTile(new Tile("J"));
        meld.addTile(new Tile("R12")); 
        meld.addTile(new Tile("R13"));

        removedTile = meld.removeTile(1);
        assertEquals(MeldType.RUN, meld.getMeldType());
        assertNull(removedTile);
        assertEquals("{R10 J R12 R13}", meld.toString());
        
        // Disallow addition of joker, run full
        meldTiles = new ArrayList<>();
        meld = new Meld("B1,B2,B3,B4,B5,B6,B7,B8,B9,B10,B11,B12,B13");
        meldTiles.add(new Tile("J"));
        releasedJoker = meld.addTile(meldTiles);
        assertNull(releasedJoker);
        assertEquals(MeldType.RUN, meld.getMeldType());
        assertEquals("{B1 B2 B3 B4 B5 B6 B7 B8 B9 B10 B11 B12 B13}", meld.toString());
        
        // Disallow addition of joker, set full
        meldTiles = new ArrayList<>();
        meld = new Meld("R9,B9,G9,O9");
        meldTiles.add(new Tile("J"));
        releasedJoker = meld.addTile(meldTiles);
        assertNull(releasedJoker);
        assertEquals(MeldType.SET, meld.getMeldType());
        assertEquals("{R9 B9 G9 O9}", meld.toString());
        
        // Disallow adding multiple tiles containing a joker (affects console game only)
        meldTiles = new ArrayList<>();
        meld = new Meld();
        meldTiles.add(new Tile("R1"));
        meldTiles.add(new Tile("R2"));
        meldTiles.add(new Tile("J"));
        releasedJoker = meld.addTile(meldTiles);
        assertNull(releasedJoker);
        assertEquals("{}", meld.toString());
        
        // Disallow replacing a joker (that has not yet been replaced) with a tile that is on the table
        meld = new Meld("J,G8,G9,G10,G11,G12,G13");
        Tile tileOnTable = new Tile("G7");
        tileOnTable.setOnTable(true);
        meld.setIsInitialMeld(false); // Mimic a meld that is on the table and not coming from the hand
        Tile tilesAdded = meld.addTile(tileOnTable);
        
        assertNull(tilesAdded);
        assertEquals(MeldType.RUN, meld.getMeldType());
        assertEquals("{J G8 G9 G10 G11 G12 G13}", meld.toString());
        
        // Allow replacing a joker (that has been previously replaced) with a tile that is on the table
        meld = new Meld("J,G8,G9,G10,G11,G12,G13");
        meld.setIsLocked(false); // Mimic a meld that is not locked by a joker
        meld.setIsInitialMeld(false); // Mimic a meld that is on the table and not coming from the hand
        
        tileOnTable = new Tile("G7");
        tileOnTable.setOnTable(true);
        tilesAdded = meld.addTile(tileOnTable);

        System.out.println(meld);
        System.out.println(tilesAdded);
        assertNull(tilesAdded.colour);
        assertTrue(tilesAdded.isJoker());
        assertEquals(MeldType.RUN, meld.getMeldType());
        assertEquals("{G7 G8 G9 G10 G11 G12 G13}", meld.toString());
        
        // Allow adding a tile that does not replace a joker to a locked meld
        meld = new Meld("J,G8,G9,G10,G11,G12,G13");
        tileOnTable = new Tile("G6");
        tileOnTable.setOnTable(true);
        tilesAdded = meld.addTile(tileOnTable);

        assertNull(tilesAdded.colour);
        assertFalse(tilesAdded.isJoker());
        assertEquals(MeldType.RUN, meld.getMeldType());
        assertEquals("{G6 J G8 G9 G10 G11 G12 G13}", meld.toString());
        
        // Allow adding a tile that does not replace a joker to an unlocked meld
        meld = new Meld("J,G8,G9,G10,G11,G12,G13");
        tileOnTable = new Tile("G6");
        tileOnTable.setOnTable(true);
        meld.setIsLocked(false); // Mimic a meld that is not locked by a joker
        meld.setIsInitialMeld(false); // Mimic a meld that is on the table and not coming from the hand
        tilesAdded = meld.addTile(tileOnTable);

        assertNull(tilesAdded.colour);
        assertFalse(tilesAdded.isJoker());
        assertEquals(MeldType.RUN, meld.getMeldType());
        assertEquals("{G6 J G8 G9 G10 G11 G12 G13}", meld.toString());
    }
}