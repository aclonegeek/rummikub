package core;

import java.util.ArrayList;

public class Player3 extends Player {
    public Player3() {
        workspace = new ArrayList<>();
        hand = new Hand();
        playBehaviour = new Strategy3();
    }
    
    public String toString() {
        return "Player 3:\n# tiles: " + hand.getSize() + "\n\n";
    }
}
