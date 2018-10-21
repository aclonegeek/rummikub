package core;

import java.util.ArrayList;

public class PlayerHuman extends Player {
    public PlayerHuman() {
        workspace = new ArrayList<>();
        hand = new Hand();
        playBehaviour = new StrategyHuman();
    }
    
    public String toString() {
        return "You:\n# tiles: " + hand.getSize() + "\n" + hand.toString() + "\n\n";
    }
}
