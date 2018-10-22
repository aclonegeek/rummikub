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
        Meld meld2 = new Meld();
        melds.add(meld1);
        melds.add(meld2);
        
        table.setState(melds);
        
        // Create new players and register them as table observers
        Player player1 = new Player1();
        Player player2 = new Player1();
        ArrayList<Meld> player1Workspace = player1.getWorkspace();
        ArrayList<Meld> player2Workspace = player2.getWorkspace();
        table.registerObserver(player1);
        table.registerObserver(player2);
        
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
        
        // Create list of melds
        ArrayList<Meld> melds = new ArrayList<>();
        Meld meld1 = new Meld();
        Meld meld2 = new Meld();
        melds.add(meld1);
        melds.add(meld2);
        
        table.setState(melds);
        
        // Create new player and register it as a table observer
        Player player1 = new Player1();
        ArrayList<Meld> player1Workspace = player1.getWorkspace();
        assertEquals(0, player1Workspace.size());
        table.registerObserver(player1);
        
        table.removeObserver(player1);
        table.notifyObservers();
        
        // Player 2's workspace shouldn't have the new table state after notifying
        player1Workspace = player1.getWorkspace();
        assertEquals(0, player1Workspace.size());
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
        
        // Make sure meld1 is a valid meld
        meld1.addTile(set);
        assertTrue(meld1.isValidMeld());
        
        // Split the meld at index 1 to make one invalid (NONE) meld {R7} and one valid meld {B7, G7, O7} and add to melds
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
}