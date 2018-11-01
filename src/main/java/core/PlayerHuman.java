package core;

import java.util.ArrayList;

public class PlayerHuman extends Player {
    public PlayerHuman() {
        this.playBehaviour = new StrategyHuman();
    }
    
    protected ArrayList<Meld> play() {
        return null;
    }

    public String toString() {
        return "You:\n# tiles: " + this.hand.getSize() + "\n" + this.hand.toString() + "\n\n";
    }
}
