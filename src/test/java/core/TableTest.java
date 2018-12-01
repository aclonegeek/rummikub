package core;

import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;

import core.Globals.Colour;
import junit.framework.TestCase;

public class TableTest extends TestCase {
    public void testCopyConstructor() {
        ArrayList<Meld> melds = new ArrayList<>();
        melds.add(new Meld("R1,R2,R3,J"));
        melds.add(new Meld("O5,G5,B5"));
        
        Table table1 = new Table();
        table1.setState(melds);
        Table table2 = new Table(table1);
        assertNotEquals(table1, table2);
        assertEquals(table1.toString(), table2.toString());
    }
    
    public void testSetState() {
        Table table = new Table();
        
        // Create a set and split it so table is invalid
        ArrayList<Meld> melds1 = new ArrayList<>();
        Meld meld1 = new Meld("R7,B7,O7,G7");
        Meld meld2 = meld1.splitMeld(1);
        melds1.add(meld2);
        melds1.add(meld1);
        table.setState(melds1);
        assertEquals("1: {B7 O7 G7}\n2: {R7}\n", table.toString());
        
        // Create a run
        ArrayList<Meld> melds2 = new ArrayList<>();
        Meld meld3 = new Meld("R1,R2,R3");
        melds2.add(meld3);
        table.setState(melds2);
        assertEquals("1: {R1 R2 R3}\n", table.toString());
    }
    
    public void testSetStateJokers() {
        Table table = new Table();

        // Create a set and split it so table is invalid
        ArrayList<Meld> melds1 = new ArrayList<>();
        Meld meld1 = new Meld("R7,B7,O7,G7");
        Meld meld2 = meld1.splitMeld(1);
        melds1.add(meld2);
        melds1.add(meld1);
        table.setState(melds1);
        assertEquals("1: {B7 O7 G7}\n2: {R7}\n", table.toString());
        
        // Create a run
        ArrayList<Meld> melds2 = new ArrayList<>();
        Meld meld3 = new Meld("R1,R2,R3,J");
        melds2.add(meld3);
        table.setState(melds2);
        assertEquals("1: {R1 R2 R3 J}\n", table.toString());
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
        table.setState(melds);

        assertEquals("1: {R7 B7 G7 O7}\n", table.toString());

        ArrayList<Tile> run = new ArrayList<>();
        run.add(new Tile(Colour.RED, 1));
        run.add(new Tile(Colour.RED, 2));
        run.add(new Tile(Colour.RED, 3));
        meld2.addTile(run);
        melds.add(meld2);
        table.setState(melds);

        assertEquals("1: {R7 B7 G7 O7}\n2: {R1 R2 R3}\n", table.toString());
    }
}
