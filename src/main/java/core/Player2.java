package core;

public class Player2 extends Player {
    public Player2() {
        hand = new Hand();
        playBehaviour = new Strategy2();
    }
    
    public String toString() {
        String returnString = "Player 2:\n";
        returnString += "# tiles: " + hand.getSize() + "\n\n";
        return returnString;
    }
}
