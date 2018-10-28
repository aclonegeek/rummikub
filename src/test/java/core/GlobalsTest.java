package core;

import core.Globals.Colour;
import junit.framework.TestCase;

public class GlobalsTest extends TestCase {
    public void testGetEnum() {
        // Test valid cases
        for (Colour colour : Colour.values()) {
            assertEquals(colour, Colour.getEnum(colour.getSymbol()));
        }
        
        // Test null case
        Colour colour = Colour.getEnum('Z');
        assertNull(colour);
    }
}