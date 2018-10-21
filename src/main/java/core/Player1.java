package core;

import java.util.ArrayList;

public class Player1 extends Player {
    public Player1() {
        this.workspace = new ArrayList<>();
        this.hand = new Hand();
        this.playBehaviour = new Strategy1();
    }
    
    public String toString() {
        return "Player 1:\n# tiles: " + hand.getSize() + "\n\n";
    }
}
