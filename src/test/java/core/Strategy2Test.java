package core;

import java.util.ArrayList;

import core.Globals.Colour;
import junit.framework.TestCase;

public class Strategy2Test extends TestCase {
    // Test first move with empty table (should not make a move)
    public void testDetermineInitialMove1() {
        Player p2 = new Player2();
        ArrayList<Meld> workspace = new ArrayList<>();

        p2.add(new Tile(Colour.GREEN, 6));
        p2.add(new Tile(Colour.GREEN, 7));
        p2.add(new Tile(Colour.GREEN, 8));
        p2.add(new Tile(Colour.GREEN, 9));
        p2.add(new Tile(Colour.GREEN, 10));
        p2.getPlayBehaviour().setWorkspace(workspace);

        assertTrue(p2.getInitialMove());
        assertEquals(null, p2.play());
        assertTrue(p2.getInitialMove());
    }

    // Test first move with one meld >= 30 (R10, B10, G10, O10)
    public void testDetermineInitialMove2() {
        Player p2 = new Player2();
        ArrayList<Meld> workspace = new ArrayList<>();

        ArrayList<Tile> tiles1 = new ArrayList<>();
        tiles1.add(new Tile(Colour.RED, 1));
        tiles1.add(new Tile(Colour.RED, 2));
        tiles1.add(new Tile(Colour.RED, 3));
        Meld meld1 = new Meld();
        meld1.addTile(tiles1);
        workspace.add(meld1);

        ArrayList<Tile> tiles2 = new ArrayList<>();
        tiles2.add(new Tile(Colour.RED, 10));
        tiles2.add(new Tile(Colour.ORANGE, 10));
        tiles2.add(new Tile(Colour.GREEN, 10));
        tiles2.add(new Tile(Colour.BLUE, 10));
        Meld meld2 = new Meld();
        meld2.addTile(tiles2);
        workspace.add(meld2);

        ArrayList<Tile> tiles3 = new ArrayList<>();
        tiles3.add(new Tile(Colour.BLUE, 5));
        tiles3.add(new Tile(Colour.BLUE, 6));
        tiles3.add(new Tile(Colour.BLUE, 7));
        tiles3.add(new Tile(Colour.BLUE, 8));
        Meld meld3 = new Meld();
        meld3.addTile(tiles3);
        workspace.add(meld3);

        assertEquals("[{R1,R2,R3}, {R10,O10,G10,B10}, {B5,B6,B7,B8}]", workspace.toString());

        p2.add(new Tile(Colour.GREEN, 10));
        p2.add(new Tile(Colour.ORANGE, 10));
        p2.add(new Tile(Colour.BLUE, 10));
        p2.add(new Tile(Colour.RED, 10));
        p2.add(new Tile(Colour.RED, 8));
        p2.add(new Tile(Colour.RED, 7));
        p2.add(new Tile(Colour.RED, 6));
        p2.add(new Tile(Colour.RED, 5));
        p2.add(new Tile(Colour.RED, 4));
        p2.getPlayBehaviour().setWorkspace(workspace);

//        assertTrue(strategy2.getInitialMove());
//        assertEquals("[{R1,R2,R3}, {R10,O10,G10,B10}, {B5,B6,B7,B8}, {R10,B10,G10,O10}]", strategy2.determineMove(hand).toString());
//        assertFalse(strategy2.getInitialMove());
    }

    // Test first move such that multiple melds total >= 30, should use as few melds as possible
    // Uses example given by prof
    public void testDetermineInitialMove3() {
        Player p2 = new Player2();
        ArrayList<Meld> workspace = new ArrayList<>();

        ArrayList<Tile> tiles1 = new ArrayList<>();
        tiles1.add(new Tile(Colour.RED, 1));
        tiles1.add(new Tile(Colour.RED, 2));
        tiles1.add(new Tile(Colour.RED, 3));
        Meld meld1 = new Meld();
        meld1.addTile(tiles1);
        workspace.add(meld1);

        ArrayList<Tile> tiles2 = new ArrayList<>();
        tiles2.add(new Tile(Colour.RED, 10));
        tiles2.add(new Tile(Colour.ORANGE, 10));
        tiles2.add(new Tile(Colour.GREEN, 10));
        tiles2.add(new Tile(Colour.BLUE, 10));
        Meld meld2 = new Meld();
        meld2.addTile(tiles2);
        workspace.add(meld2);

        ArrayList<Tile> tiles3 = new ArrayList<>();
        tiles3.add(new Tile(Colour.BLUE, 5));
        tiles3.add(new Tile(Colour.BLUE, 6));
        tiles3.add(new Tile(Colour.BLUE, 7));
        tiles3.add(new Tile(Colour.BLUE, 8));
        Meld meld3 = new Meld();
        meld3.addTile(tiles3);
        workspace.add(meld3);

        assertEquals("[{R1,R2,R3}, {R10,O10,G10,B10}, {B5,B6,B7,B8}]", workspace.toString());

        p2.add(new Tile(Colour.RED, 3));
        p2.add(new Tile(Colour.RED, 4));
        p2.add(new Tile(Colour.RED, 5));
        p2.add(new Tile(Colour.GREEN, 5));
        p2.add(new Tile(Colour.GREEN, 6));
        p2.add(new Tile(Colour.GREEN, 7));
        p2.add(new Tile(Colour.ORANGE, 2));
        p2.add(new Tile(Colour.ORANGE, 3));
        p2.add(new Tile(Colour.ORANGE, 4));
        p2.getPlayBehaviour().setWorkspace(workspace);

//        assertTrue(strategy2.getInitialMove());
//        assertEquals("[{R1,R2,R3}, {R10,O10,G10,B10}, {B5,B6,B7,B8}, {G5,G6,G7}, {R3,R4,R5}]", strategy2.determineMove(hand).toString());
//        assertFalse(strategy2.getInitialMove());
    }

    // Test first move where no combination of melds totals >= 30
    public void testDetermineInitialMove4( ) {
        Player p2 = new Player2();
        ArrayList<Meld> workspace = new ArrayList<>();

        ArrayList<Tile> tiles1 = new ArrayList<>();
        tiles1.add(new Tile(Colour.RED, 1));
        tiles1.add(new Tile(Colour.RED, 2));
        tiles1.add(new Tile(Colour.RED, 3));
        Meld meld1 = new Meld();
        meld1.addTile(tiles1);
        workspace.add(meld1);

        ArrayList<Tile> tiles2 = new ArrayList<>();
        tiles2.add(new Tile(Colour.RED, 10));
        tiles2.add(new Tile(Colour.ORANGE, 10));
        tiles2.add(new Tile(Colour.GREEN, 10));
        tiles2.add(new Tile(Colour.BLUE, 10));
        Meld meld2 = new Meld();
        meld2.addTile(tiles2);
        workspace.add(meld2);

        ArrayList<Tile> tiles3 = new ArrayList<>();
        tiles3.add(new Tile(Colour.BLUE, 5));
        tiles3.add(new Tile(Colour.BLUE, 6));
        tiles3.add(new Tile(Colour.BLUE, 7));
        tiles3.add(new Tile(Colour.BLUE, 8));
        Meld meld3 = new Meld();
        meld3.addTile(tiles3);
        workspace.add(meld3);

        assertEquals("[{R1,R2,R3}, {R10,O10,G10,B10}, {B5,B6,B7,B8}]", workspace.toString());

        p2.add(new Tile(Colour.GREEN, 1));
        p2.add(new Tile(Colour.GREEN, 2));
        p2.add(new Tile(Colour.GREEN, 3));
        p2.add(new Tile(Colour.GREEN, 4));
        p2.add(new Tile(Colour.GREEN, 8));
        p2.add(new Tile(Colour.ORANGE, 10));
        p2.add(new Tile(Colour.BLUE, 2));
        p2.add(new Tile(Colour.RED, 9));
        p2.add(new Tile(Colour.RED, 4));
        p2.add(new Tile(Colour.GREEN, 5));
        p2.add(new Tile(Colour.ORANGE, 2));
        p2.add(new Tile(Colour.RED, 11));
        p2.add(new Tile(Colour.RED, 12));
        p2.add(new Tile(Colour.BLUE, 7));
        p2.getPlayBehaviour().setWorkspace(workspace);

//        assertTrue(strategy2.getInitialMove());
//        assertEquals(null, strategy2.determineMove(hand));
//        assertTrue(strategy2.getInitialMove());
    }
}