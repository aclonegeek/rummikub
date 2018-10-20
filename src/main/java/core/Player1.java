package core;

public class Player1 extends Player {
    public Player1() {
        hand = new Hand();
        playBehaviour = new Strategy1();
    }
    
    public String toString() {
        String returnString = "Player 1:\n";
        returnString += "# tiles: " + hand.getSize() + "\n\n";
        return returnString;
    }
}
