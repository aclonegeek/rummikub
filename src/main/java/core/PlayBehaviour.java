package core;

import java.util.ArrayList;

public interface PlayBehaviour {
    public ArrayList<Meld> determineMove(ArrayList<Meld> workspace, Hand hand, boolean initialMove);
    public ArrayList<Meld> determineInitialMove(ArrayList<Meld> workspace, Hand hand);
    public ArrayList<Meld> determineRegularMove(ArrayList<Meld> workspace, Hand hand);
}