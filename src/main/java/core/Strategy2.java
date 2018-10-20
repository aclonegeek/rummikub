package core;

import java.util.ArrayList;

public class Strategy2 implements PlayBehaviour {
    ArrayList<Meld> workspace;
    
    public ArrayList<Meld> determineMove(){
        // Player2 logic goes here
        return null;
    }
    
    public ArrayList<Meld> getWorkspace(){
        return workspace;
    }
}