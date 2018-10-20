package core;

import java.util.ArrayList;

public class Strategy1 implements PlayBehaviour {
    ArrayList<Meld> workspace;
    
    public ArrayList<Meld> determineMove(){
        // Player1 logic goes here
        return null;
    }
    
    public ArrayList<Meld> getWorkspace(){
        return workspace;
    }
}