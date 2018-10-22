package core;

import java.util.ArrayList;

public class Strategy2 implements PlayBehaviour {
    public ArrayList<Meld> determineMove(ArrayList<Meld> workspace, Hand hand, boolean initialMove) {
        if (initialMove) {
            return determineInitialMove(workspace, hand);
        } else {
            return determineRegularMove(workspace, hand);
        }
    }
    
    public ArrayList<Meld> determineInitialMove(ArrayList<Meld> workspace, Hand hand) {
        return null;
    }
    
    public ArrayList<Meld> determineRegularMove(ArrayList<Meld> workspace, Hand hand) {
        return null;
    }
}