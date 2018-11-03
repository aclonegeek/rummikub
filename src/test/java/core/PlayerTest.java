package core;

import java.util.ArrayList;

import core.Globals.Colour;
import junit.framework.TestCase;

public class PlayerTest extends TestCase {
    public void testPlayerHuman() { 
        // Create ArrayList of melds representing the table
        ArrayList<Meld> melds = new ArrayList<>();  
        ArrayList<Tile> tiles1 = new ArrayList<Tile>();
        Meld meld1 = new Meld();
        Tile tile1 = new Tile(Colour.GREEN, 1);
        Tile tile2 = new Tile(Colour.GREEN, 2);
        Tile tile3 = new Tile(Colour.GREEN, 3);
        Tile tile4 = new Tile(Colour.GREEN, 4);
        tile1.setOnTable(true);
        tile2.setOnTable(true);
        tile3.setOnTable(true);
        tile4.setOnTable(true);
        tiles1.add(tile1);
        tiles1.add(tile2);
        tiles1.add(tile3);
        tiles1.add(tile4);
        meld1.addTile(tiles1);
        
        ArrayList<Tile> tiles2 = new ArrayList<Tile>();
        Meld meld2 = new Meld();
        Tile tile5 = new Tile(Colour.GREEN, 2);
        Tile tile6 = new Tile(Colour.BLUE, 2);
        Tile tile7 = new Tile(Colour.ORANGE, 2);
        tile5.setOnTable(true);
        tile6.setOnTable(true);
        tile7.setOnTable(true);
        tiles2.add(tile5);
        tiles2.add(tile6);
        tiles2.add(tile7);
        meld2.addTile(tiles2);
        
        // Create player and player's hand
        Player human = new PlayerHuman();
        assertEquals(0, human.getHandSize());
        assertEquals("You:\n# tiles: 0\n[]\n\n", human.toString());
        
        Tile tile8 = new Tile(Colour.RED, 1);
        Tile tile9 = new Tile(Colour.RED, 2);
        Tile tile10 = new Tile(Colour.BLUE, 1);
        Tile tile11 = new Tile(Colour.GREEN, 3);
        Tile tile12 = new Tile(Colour.GREEN, 5);
        human.add(tile8);
        human.add(tile9);
        human.add(tile10);
        human.add(tile11);
        human.add(tile12);
        assertEquals(5, human.getHandSize());
        assertEquals("You:\n# tiles: 5\n[R1, R2, B1, G3, G5]\n\n", human.toString());
        
        // Add player's tiles to ArrayList of melds representing the temporary table state
        ArrayList<Tile> tiles3 = new ArrayList<Tile>();
        tiles3.add(tile12);
        meld1.addTile(tiles3);
        
        ArrayList<Tile> tiles4 = new ArrayList<Tile>();
        tiles4.add(tile9);
        meld2.addTile(tiles4);
        
        melds.add(meld1);
        melds.add(meld2);
        
        // Remove tiles from player's hand for which isOnTable is false
        human.removeTilesFromHand(melds);
        assertEquals(3, human.getHandSize());
        assertEquals("You:\n# tiles: 3\n[R1, B1, G3]\n\n", human.toString());
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

        playerHuman.registerObserver(player3.getPlayBehaviour());
        player1.registerObserver(player3.getPlayBehaviour());
        player2.registerObserver(player3.getPlayBehaviour());

        // The observer has not been notified yet.
        assertEquals(-1, player3.getPlayBehaviour().getLowestHandCount());

        playerHuman.add(tile1);
        playerHuman.add(tile2);
        playerHuman.notifyObservers();
        assertEquals(2, player3.getPlayBehaviour().getLowestHandCount());

        player1.add(tile3);
        player1.notifyObservers();
        assertEquals(1, player3.getPlayBehaviour().getLowestHandCount());

        player2.add(tile4);
        player2.add(tile5);
        player2.add(tile6);
        player2.notifyObservers();
        assertEquals(1, player3.getPlayBehaviour().getLowestHandCount());
    }

    public void testRemoveObserver() {
        Player playerHuman = new PlayerHuman();
        Player player1 = new Player1();
        Player player2 = new Player2();
        Player player3 = new Player3();

        playerHuman.registerObserver(player3.getPlayBehaviour());
        player1.registerObserver(player3.getPlayBehaviour());
        player2.registerObserver(player3.getPlayBehaviour());

        assertEquals(1, playerHuman.getObservers().size());
        assertEquals(1, player1.getObservers().size());
        assertEquals(1, player2.getObservers().size());

        playerHuman.removeObserver(player3.getPlayBehaviour());
        player1.removeObserver(player3.getPlayBehaviour());
        player2.removeObserver(player3.getPlayBehaviour());

        assertEquals(0, playerHuman.getObservers().size());
        assertEquals(0, player1.getObservers().size());
        assertEquals(0, player2.getObservers().size());
    }
}
