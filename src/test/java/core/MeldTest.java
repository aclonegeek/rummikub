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
}