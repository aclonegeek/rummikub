package core;

import java.util.ArrayList;

public class Player2 extends Player {
    public Player2() {
        this.workspace = new ArrayList<>();
        this.hand = new Hand();
        this.playBehaviour = new Strategy2();
    }
    
    public String toString() {
        return "Player 2:\n# tiles: " + hand.getSize() + "\n\n";
    }
}
