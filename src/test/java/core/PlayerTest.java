package core;

import java.util.ArrayList;

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
        p1.play(workspace);
        assertEquals(2, p1.getHandSize());
        assertEquals("p1: 2 tiles\n[O5 O6]\n", p1.toString());
    }
}