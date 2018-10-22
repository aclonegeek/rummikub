package core;

import java.util.ArrayList;

public class StrategyHuman extends PlayBehaviour {
    // Will probably override both methods, not sure how these will be implemented yet
    public ArrayList<Meld> determineInitialMove(ArrayList<Meld> workspace, Hand hand) {
        return null;
    }
    
    public ArrayList<Meld> determineRegularMove(ArrayList<Meld> workspace, Hand hand) {
        return null;
    }
}