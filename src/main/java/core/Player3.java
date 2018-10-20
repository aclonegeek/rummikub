package core;

public class Player3 extends Player{
    public Player3() {
        hand = new Hand();
        playBehaviour = new Strategy3();
    }
    
    public String toString() {
        String returnString = "Player 3:\n";
        returnString += "# tiles: " + hand.getSize() + "\n\n";
        return returnString;
    }
}
