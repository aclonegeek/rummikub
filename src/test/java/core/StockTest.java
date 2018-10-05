package core;

import static org.junit.Assert.assertNotEquals;

import java.util.stream.Collectors;

import junit.framework.TestCase;

public class StockTest extends TestCase {
    public void testCreate() {
        Stock stock = new Stock();

        assertTrue(stock.getStock().isEmpty());
        stock.populate();
        assertFalse(stock.getStock().isEmpty());
        assertEquals(104, stock.getStock().size());

        // The first and last elements of a fresh stock should be R1 and O13
        assertEquals("R1", stock.getStock().get(0).toString());
        assertEquals("O13", stock.getStock().get(stock.getStock().size() - 1).toString());
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
