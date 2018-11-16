package core;

import java.util.ArrayList;

import core.Globals.Colour;
import junit.framework.TestCase;

public class PlayerTest extends TestCase {
    public void testPlayer() {
        // Create ArrayList of melds representing the table
        ArrayList<Meld> workspace = new ArrayList<>();
        
        Meld meld1 = new Meld("G1,G2,G3,G4");
        for (int i = 0; i < meld1.getSize(); i++) {
            meld1.getTile(i).setOnTable(true);
        }
        
        Meld meld2 = new Meld("G2,B2,O2");
        for (int i = 0; i < meld2.getSize(); i++) {
            meld2.getTile(i).setOnTable(true);
        }
        
        workspace.add(meld1);
        workspace.add(meld2);

        // Create player and add card's to hand
        Player p1 = new Player1("p1");
        p1.setWorkspace(workspace);
        assertEquals(0, p1.getHandSize());
        assertEquals("p1: 0 tiles\n[]\n", p1.toString());
        p1.add(new Tile("R1"));
        p1.add(new Tile("R2"));
        p1.add(new Tile("R3"));
        p1.add(new Tile("O5"));
        p1.add(new Tile("O6"));
        assertEquals(5, p1.getHandSize());
        assertEquals("p1: 5 tiles\n[R1 R2 R3 O5 O6]\n", p1.toString());

        // Have player play their turn and verify tiles were removed from their hand
        p1.setInitialMove(false);
        p1.play();
        assertEquals(2, p1.getHandSize());
        assertEquals("p1: 2 tiles\n[O5 O6]\n", p1.toString());
    }

    public void testNotifyObserver() {
        Player playerHuman = new PlayerHuman();
        Player player1 = new Player1();
        Player player2 = new Player2();
        Player player3 = new Player3();

        Tile tile1 = new Tile(Colour.GREEN, 1);
        Tile tile2 = new Tile(Colour.GREEN, 2);
        Tile tile3 = new Tile(Colour.GREEN, 3);
        Tile tile4 = new Tile(Colour.GREEN, 4);
        Tile tile5 = new Tile(Colour.GREEN, 5);
        Tile tile6 = new Tile(Colour.GREEN, 6);

        playerHuman.registerObserver(player3);
        player1.registerObserver(player3);
        player2.registerObserver(player3);

        // The observer has not been notified yet.
        assertEquals(100, player3.getLowestHandCount());

        playerHuman.add(tile1);
        playerHuman.add(tile2);
        playerHuman.notifyObservers();
        assertEquals(2, player3.getLowestHandCount());

        player1.add(tile3);
        player1.notifyObservers();
        assertEquals(1, player3.getLowestHandCount());

        player2.add(tile4);
        player2.add(tile5);
        player2.add(tile6);
        player2.notifyObservers();
        assertEquals(1, player3.getLowestHandCount());
    }

    public void testRemoveObserver() {
        Player playerHuman = new PlayerHuman();
        Player player1 = new Player1();
        Player player2 = new Player2();
        Player player3 = new Player3();

        playerHuman.registerObserver(player3);
        player1.registerObserver(player3);
        player2.registerObserver(player3);

        assertEquals(1, playerHuman.getObservers().size());
        assertEquals(1, player1.getObservers().size());
        assertEquals(1, player2.getObservers().size());

        playerHuman.removeObserver(player3);
        player1.removeObserver(player3);
        player2.removeObserver(player3);

        assertEquals(0, playerHuman.getObservers().size());
        assertEquals(0, player1.getObservers().size());
        assertEquals(0, player2.getObservers().size());
    }
}