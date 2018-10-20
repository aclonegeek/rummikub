package core;

import java.util.ArrayList;

public interface PlayBehaviour {
    public ArrayList<Meld> determineMove();
    public ArrayList<Meld> getWorkspace();
}
