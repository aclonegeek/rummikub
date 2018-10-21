package core;

import java.util.ArrayList;

public interface PlayBehaviour {
    public ArrayList<Meld> determineMove(ArrayList<Meld> workspace, Hand hand);
}
