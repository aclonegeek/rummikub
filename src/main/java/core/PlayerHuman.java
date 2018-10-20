package core;

public class PlayerHuman extends Player {
    public PlayerHuman() {
        hand = new Hand();
        playBehaviour = new StrategyHuman();
    }
    
    public String toString() {
        return "You:\n# tiles: " + hand.getSize() + "\n" + hand.toString() + "\n\n";
    }
}
