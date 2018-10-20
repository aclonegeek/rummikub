package core;

public class Player3 extends Player {
    public Player3() {
        hand = new Hand();
        playBehaviour = new Strategy3();
    }
    
    public String toString() {
        return "Player 3:\n# tiles: " + hand.getSize() + "\n\n";
    }
}
