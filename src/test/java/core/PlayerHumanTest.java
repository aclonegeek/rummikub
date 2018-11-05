package core;

import core.Globals.Colour;
import junit.framework.TestCase;

public class PlayerHumanTest extends TestCase {
    public void testToString() {
        Player human = new PlayerHuman("PlayerHuman");
        assertEquals("PlayerHuman: 0 tiles\n[]\n", human.toString());
        human.add(new Tile(Colour.RED, 1));
        human.add(new Tile(Colour.RED, 2));
        human.add(new Tile(Colour.BLUE, 1));
        human.add(new Tile(Colour.GREEN, 3));
        human.add(new Tile(Colour.GREEN, 5));
        assertEquals(5, human.getHandSize());
        assertEquals("PlayerHuman: 5 tiles\n[R1 R2 B1 G3 G5]\n", human.toString());
    }
}
