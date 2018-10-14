package core;

public class PlayerHuman extends Player {
    public PlayerHuman() {
        hand = new Hand();
        //playBehaviour = new StrategyHuman();
    }
    
    public String toString() {
        String returnString = "You:\n";
        returnString += "# tiles: " + hand.getSize() + "\n";
        returnString += hand.toString() + "\n\n";
        return returnString;
    }
}
