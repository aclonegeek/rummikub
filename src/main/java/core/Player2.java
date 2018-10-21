package core;

import java.util.ArrayList;

public class Player2 extends Player {
    public Player2() {
        workspace = new ArrayList<>();
        hand = new Hand();
        playBehaviour = new Strategy2();
    }
    
    public String toString() {
        return "Player 2:\n# tiles: " + hand.getSize() + "\n\n";
    }
}
