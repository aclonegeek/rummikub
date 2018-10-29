package core;

public class PlayerHuman extends Player {
    public PlayerHuman() {
        this.playBehaviour = new StrategyHuman();
    }

    public String toString() {
        return "You:\n# tiles: " + this.hand.getSize() + "\n" + this.hand.toString() + "\n\n";
    }
}
