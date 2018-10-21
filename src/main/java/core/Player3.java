package core;

import java.util.ArrayList;

public class Player3 extends Player {
    public Player3() {
        this.workspace = new ArrayList<>();
        this.hand = new Hand();
        this.playBehaviour = new Strategy3();
    }
    
    public String toString() {
        return "Player 3:\n# tiles: " + hand.getSize() + "\n\n";
    }
}
