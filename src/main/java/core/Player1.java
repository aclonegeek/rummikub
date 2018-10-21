package core;

import java.util.ArrayList;

public class Player1 extends Player {
    public Player1() {
        workspace = new ArrayList<>();
        hand = new Hand();
        playBehaviour = new Strategy1();
    }
    
    public String toString() {
        return "Player 1:\n# tiles: " + hand.getSize() + "\n\n";
    }
}
