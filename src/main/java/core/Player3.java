package core;

public class Player3 extends Player {
    public Player3() {
        this.playBehaviour = new Strategy3();
    }

    public String toString() {
        return "Player 3:\n# tiles: " + this.hand.getSize() + "\n\n";
    }
}
