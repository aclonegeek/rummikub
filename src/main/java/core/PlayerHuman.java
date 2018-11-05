package core;

import java.util.ArrayList;

public class PlayerHuman extends Player {
    public PlayerHuman() {
        this.playBehaviour = new StrategyHuman();
    }

    public PlayerHuman(String name) {
        this();
        this.name = name;
    }

    protected ArrayList<Meld> play() {
        System.out.println(this.toString());
        ArrayList<Meld> workspace = new ArrayList<>();
        if (this.initialMove) {
            workspace = this.playBehaviour.determineInitialMove(this.hand);
        } else {
            workspace = this.playBehaviour.determineRegularMove(this.hand);
        }
        if (workspace != null) {
            this.initialMove = false;
            this.drawing = false;
        } else {
            this.drawing = true;
        }
        this.notifyObservers();
        this.drawing = false;
        System.out.println(this.toString());
        return workspace;
    }
}
