package core;

import java.util.ArrayList;

import core.Globals.Colour;
import junit.framework.TestCase;

public class TableTest extends TestCase {
    public void testSetState() {
        Table table = new Table();
        Meld meld1 = new Meld();
        ArrayList<Meld> melds = new ArrayList<>();

        // Test setState failure
        // Create list of melds
        ArrayList<Tile> set = new ArrayList<>();
        set.add(new Tile(Colour.RED, 7));
        set.add(new Tile(Colour.BLUE, 7));
        set.add(new Tile(Colour.GREEN, 7));
        set.add(new Tile(Colour.ORANGE, 7));
        meld1.addTile(set);

        assertTrue(meld1.isValidMeld());

        // Split the meld to make an invalid meld
        Meld invalidMeld = meld1.splitMeld(1);
        melds.add(invalidMeld);
        melds.add(meld1);

        // Should be false because the table has one valid and one invalid meld, therefore the table is invalid
        assertFalse(table.setState(melds));

        // Test setState success
        // Create a run
        melds = new ArrayList<>();
        meld1 = new Meld();

        // Make a new run and add it to melds
        ArrayList<Tile> run = new ArrayList<>();
        run.add(new Tile(Colour.RED, 1));
        run.add(new Tile(Colour.RED, 2));
        run.add(new Tile(Colour.RED, 3));

        meld1.addTile(run);
        melds.add(meld1);

        // Should be true since meld1 is now a run
        assertTrue(table.setState(melds));
    }

    public void testToString() {
        Table table = new Table();
        Meld meld1 = new Meld();
        Meld meld2 = new Meld();
        ArrayList<Meld> melds = new ArrayList<>();

        assertEquals("", table.toString());

        ArrayList<Tile> set = new ArrayList<>();
        set.add(new Tile(Colour.RED, 7));
        set.add(new Tile(Colour.BLUE, 7));
        set.add(new Tile(Colour.GREEN, 7));
        set.add(new Tile(Colour.ORANGE, 7));
        meld1.addTile(set);
        melds.add(meld1);
        assertTrue(table.setState(melds));

        assertEquals("1: {R7 B7 G7 O7}\n", table.toString());

        ArrayList<Tile> run = new ArrayList<>();
        run.add(new Tile(Colour.RED, 1));
        run.add(new Tile(Colour.RED, 2));
        run.add(new Tile(Colour.RED, 3));
        meld2.addTile(run);
        melds.add(meld2);
        assertTrue(table.setState(melds));

        assertEquals("1: {R7 B7 G7 O7}\n2: {R1 R2 R3}\n", table.toString());
    }
}
