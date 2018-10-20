package core;

public class Player1 extends Player {
    public Player1() {
        hand = new Hand();
        playBehaviour = new Strategy1();
    }
    
    public String toString() {
        return "Player 1:\n# tiles: " + hand.getSize() + "\n\n";
    }
}
