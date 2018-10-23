package core;

import java.util.ArrayList;

public class Strategy3 implements PlayBehaviour { 
    public ArrayList<Meld> determineMove(ArrayList<Meld> workspace, Hand hand, boolean initialMove) {
        if (initialMove) {
            return determineInitialMove(workspace, hand);
        }
        return determineRegularMove(workspace, hand);
    }
    
    // Plays as few melds as it can using only the tiles in its hand and only if the total of the melds is >= 30
    public ArrayList<Meld> determineInitialMove(ArrayList<Meld> workspace, Hand hand) {
        return null;
    }
    
    // If it can play all its tiles (possibly using some tiles already on the table), it does
    // If no other player has 3 fewer tiles, uses only the tiles in its hand that require using tiles on table to make melds
    // Otherwise, plays all the tiles it can
    public ArrayList<Meld> determineRegularMove(ArrayList<Meld> workspace, Hand hand) {
        return null;
    }
}