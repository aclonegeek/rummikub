package core;

import static org.junit.Assert.assertNotEquals;

import java.util.stream.Collectors;

import junit.framework.TestCase;

public class StockTest extends TestCase {
    public void testCreate() {
        Stock stock = new Stock();

        assertTrue(stock.stock.isEmpty());
        stock.populate();
        assertFalse(stock.stock.isEmpty());
        assertEquals(104, stock.stock.size());

        // The first and last elements of a fresh stock should be R1 and O13
        assertEquals("R1", stock.stock.get(0).toString());
        assertEquals("O13", stock.stock.get(stock.stock.size() - 1).toString());
    }

    public void testShuffle() {
        Stock stock = new Stock();
        Stock shuffledStock = new Stock();
        stock.populate();
        shuffledStock.populate();

        // Convert the stocks to strings so that we can compare them
        String stockTiles = stock.stock.stream()
            .map(Object::toString)
            .collect(Collectors.joining(","));

        String shuffledStockTiles = shuffledStock.stock.stream()
            .map(Object::toString)
            .collect(Collectors.joining(","));

        assertEquals(stockTiles, shuffledStockTiles);
        shuffledStock.shuffle();
        // Ensure that the shuffled stock gets shuffled properly
        while (stockTiles.equals(shuffledStockTiles)) {
            shuffledStockTiles = shuffledStock.stock.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));   
        }
        assertNotEquals(stockTiles, shuffledStockTiles);
    }
}
