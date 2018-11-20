package core;

import java.util.ArrayList;

public class Joker extends Tile {
    public Joker() {
        super(null, 0);
        this.isJoker = true;
        this.alternateState = new ArrayList<>();
    }
}