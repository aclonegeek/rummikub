package core;

import java.util.ArrayList;

public class Strategy2 extends PlayBehaviour {
    // Overridden because Strategy2 only plays initial move if workspace is non-empty
    public ArrayList<Meld> determineMove(ArrayList<Meld> workspace, Hand hand, boolean initialMove) {
        if (initialMove && workspace.size() == 0) {
            return null;
        } else if (initialMove) {
            return determineInitialMove(workspace, hand);
        }
        return determineRegularMove(workspace, hand);
    }
    
    // Game logic unique to Strategy2
    public ArrayList<Meld> determineRegularMove(ArrayList<Meld> workspace, Hand hand) {
        return null;
    }
}