package core;

import java.util.ArrayList;
import java.util.Collections;

import core.Globals.Colour;

public class Stock {
    ArrayList<Tile> stock;

    public Stock() {
        this.stock = new ArrayList<>();
    }

    public Tile draw() {
        return this.stock.remove(0);
    }

    public void populate() {
        for (int i = 0; i < 2; i++) {
            for (Colour colour : Colour.values()) {
                for (int value = 1; value < 14; value++) {
                    this.stock.add(new Tile(colour, value));
                }
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(this.stock);
    }
}
