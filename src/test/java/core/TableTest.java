package core;

import java.util.ArrayList;

import core.Globals.Colour;
import junit.framework.TestCase;

public class TableTest extends TestCase {
    public void testNotifyObserver() {
        Table table = new Table();

        // Create list of melds
        ArrayList<Meld> melds = new ArrayList<>();

        Meld meld1 = new Meld();
        Meld emptyMeld = new Meld();

        ArrayList<Tile> set = new ArrayList<>();
        set.add(new Tile(Colour.RED, 7));
        set.add(new Tile(Colour.BLUE, 7));
        set.add(new Tile(Colour.GREEN, 7));
        set.add(new Tile(Colour.ORANGE, 7));
        meld1.addTile(set);
        melds.add(emptyMeld);
        melds.add(meld1);

        table.setState(melds);

        // Create new players and register them as table observers
        Player player1 = new Player1();
        Player player2 = new Player1();
        ArrayList<Meld> player1Workspace = player1.getWorkspace();
        ArrayList<Meld> player2Workspace = player2.getWorkspace();
        table.registerObserver(player1.getPlayBehaviour());
        table.registerObserver(player2.getPlayBehaviour());

        assertEquals(0, player1Workspace.size());
        assertEquals(0, player2Workspace.size());
        table.notifyObservers();

        // Player 1 and 2's workspace should have the new table state after notifying
        player1Workspace = player1.getWorkspace();
        player2Workspace = player2.getWorkspace();
        assertEquals(1, player1Workspace.size());
        assertEquals(1, player2Workspace.size());
    }

    public void testRemoveObserver() {
        Table table = new Table();
        ArrayList<Meld> melds = new ArrayList<>();

        // Meld to be added to ArrayList of Melds
        Meld meld = new Meld();

        Player player1 = new Player1();
        table.registerObserver(player1.getPlayBehaviour());

        // Player1's workspace should be size 0
        assertEquals(0, player1.getWorkspace().size());

        ArrayList<Tile> set = new ArrayList<>();
        set.add(new Tile(Colour.RED, 7));
        set.add(new Tile(Colour.BLUE, 7));
        set.add(new Tile(Colour.GREEN, 7));
        set.add(new Tile(Colour.ORANGE, 7));
        meld.addTile(set);
        melds.add(meld);

        table.setState(melds);

        // Player1's workspace should be size 1
        assertEquals(1, player1.getWorkspace().size());

        // Remove Player1 as an observer
        table.removeObserver(player1.getPlayBehaviour());

        // Create a new meld and add it to the table
        meld = new Meld();
        ArrayList<Tile> run = new ArrayList<>();
        run.add(new Tile(Colour.RED, 1));
        run.add(new Tile(Colour.RED, 2));
        run.add(new Tile(Colour.RED, 3));
        meld.addTile(run);
        melds.add(meld);

        // Player1's should still only have 1 meld in their workspace
        assertEquals(1, player1.getWorkspace().size());

        // Test removeObserver if the player doesn't exist
        Player player3 = new Player3();
        table.removeObserver(player3.getPlayBehaviour());
    }

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

        assertEquals("1: {R7,B7,G7,O7}\n", table.toString());

        ArrayList<Tile> run = new ArrayList<>();
        run.add(new Tile(Colour.RED, 1));
        run.add(new Tile(Colour.RED, 2));
        run.add(new Tile(Colour.RED, 3));
        meld2.addTile(run);
        melds.add(meld2);
        assertTrue(table.setState(melds));

        assertEquals("1: {R7,B7,G7,O7}\n2: {R1,R2,R3}\n", table.toString());
    }
}