package core;

import java.util.ArrayList;

public class Strategy2 implements PlayBehaviour {
    public ArrayList<Meld> determineMove(ArrayList<Meld> workspace, Hand hand, boolean initialMove) {
        if (initialMove && workspace.size() == 0) {
            return null;
        } else if (initialMove) {
            return determineInitialMove(workspace, hand);
        }
        return determineRegularMove(workspace, hand);
    }
    
    // Plays as few melds as it can using only the tiles in its hand and only if the total of the melds is >= 30
    public ArrayList<Meld> determineInitialMove(ArrayList<Meld> workspace, Hand hand) {
        return null; 
    }
    
    // If it can play all its tiles (possibly using some tiles already on the table), it does
    // Otherwise, uses only the tiles of its hand that require using tiles on the table to make melds
    public ArrayList<Meld> determineRegularMove(ArrayList<Meld> workspace, Hand hand) {
        return null;
    }
}