package core;

import java.util.ArrayList;

public class PlayerHuman extends Player {
    public PlayerHuman() {
        this.workspace = new ArrayList<>();
        this.hand = new Hand();
        this.playBehaviour = new StrategyHuman();
    }
    
    public String toString() {
        return "You:\n# tiles: " + hand.getSize() + "\n" + hand.toString() + "\n\n";
    }
}
