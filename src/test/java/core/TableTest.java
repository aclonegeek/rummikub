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
        Player player1 = new Player();
        Player player2 = new Player();
        table.registerObserver(player1);
        table.registerObserver(player2);
        
        table.notifyObservers();
        
        // Player 1 and 2's workspace should have the new table state after notifying
        ArrayList<Meld> player1Workspace = player1.getWorkspace();
        assertEquals(2, player1Workspace.size());
        
        ArrayList<Meld> player2Workspace = player2.getWorkspace();
        assertEquals(2, player2Workspace.size());
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
        
        // Create new players and register them as table observers
        Player player1 = new Player();
        Player player2 = new Player();
        table.registerObserver(player1);
        table.registerObserver(player2);
        
        table.removeObserver(player2);
        table.notifyObservers();
        
        // Player 2's workspace shouldn't have the new table state after notifying
        ArrayList<Meld> player2Workspace = player2.getWorkspace();
        assertEquals(0, player2Workspace.size());
    }
    
    public void testSetState() {
        Table table = new Table();
        
        // Create list of melds
        ArrayList<Meld> melds = new ArrayList<>();
        Meld meld1 = new Meld();
        melds.add(meld1);
        
        // Should be false because meld1 is an empty meld, therefore of type INVALID/NONE
        assertFalse(table.setState(melds));
        
        melds = new ArrayList<>();
        
        // Create a run
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