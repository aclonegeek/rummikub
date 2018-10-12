package core;

import java.util.ArrayList;

public class Meld {
    public enum MeldType {
        INVALID, NONE, RUN, SET;
    }
    
    private ArrayList<Tile> meld;
    private MeldType meldType;
    
    public Meld() {
        this.meld = new ArrayList<>();
        this.meldType = MeldType.INVALID;
    }
    
    public MeldType getMeldType() {
        return this.meldType;
    }
}