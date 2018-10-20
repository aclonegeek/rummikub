package core;

import java.util.ArrayList;

public class StrategyHuman implements PlayBehaviour {
    ArrayList<Meld> workspace;
    
    public ArrayList<Meld> determineMove() {
        // Human logic goes here
        return null;
    }
    
    public ArrayList<Meld> getWorkspace() {
        return workspace;
    }
}
