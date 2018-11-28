package core;

import static org.junit.Assert.assertNotEquals;

import java.util.stream.Collectors;

import core.Globals.Colour;
import junit.framework.TestCase;

public class StockTest extends TestCase {
    public void testCreate() {
        Stock stock = new Stock();

        assertTrue(stock.getStock().isEmpty());
        stock.populate();
        assertFalse(stock.getStock().isEmpty());
        assertEquals(104, stock.getStock().size());

        int counter = 0;
        // Check the first 52 tiles
        for (Colour colour : Colour.values()) {
            for (int value = 1; value < 14; value++) {
                assertEquals(colour.getSymbol() + String.valueOf(value), stock.getStock().get(counter).toString());
                counter++;
            }
        }

        assertEquals(counter, 52);

        // Check the remaining 52 tiles
        for (Colour colour : Colour.values()) {
            for (int value = 1; value < 14; value++) {
                assertEquals(colour.getSymbol() + String.valueOf(value), stock.getStock().get(counter).toString());
                counter++;
            }
        }

        assertEquals(counter, 104);
    }
    
    public void testPopulateForDraw() {
        Stock stock = new Stock();
        
        assertTrue(stock.getStock().isEmpty());
        stock.populateForDraw();
        assertFalse(stock.getStock().isEmpty());
        assertEquals(52, stock.getStock().size());

        // Check all 52 tiles
        int counter = 0;
        for (Colour colour : Colour.values()) {
            for (int value = 1; value < 14; value++) {
                assertEquals(colour.getSymbol() + String.valueOf(value), stock.getStock().get(counter).toString());
                counter++;
            }
        }

        assertEquals(counter, 52);
    }

    public void testShuffle() {
        Stock stock = new Stock();
        Stock shuffledStock = new Stock();
        stock.populate();
        shuffledStock.populate();

        // Convert the stocks to strings so that we can compare them
        String stockTiles = stock.getStock().stream()
            .map(Object::toString)
            .collect(Collectors.joining(","));

        String shuffledStockTiles = shuffledStock.getStock().stream()
            .map(Object::toString)
            .collect(Collectors.joining(","));

        assertEquals(stockTiles, shuffledStockTiles);
        // Ensure that the shuffled stock gets shuffled differently
        while (stockTiles.equals(shuffledStockTiles)) {
            shuffledStock.shuffle();
            shuffledStockTiles = shuffledStock.getStock().stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));   
        }
        assertNotEquals(stockTiles, shuffledStockTiles);
    }
}
