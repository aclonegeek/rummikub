package core;

import java.util.ArrayList;

public class Player2 extends Player {
    public Player2() {
        this.playBehaviour = new Strategy2();
    }

    protected ArrayList<Meld> play() {
        ArrayList<Meld> workspace = new ArrayList<>();
        if (this.initialMove && this.getWorkspace().size() > 0) {
            workspace = this.playBehaviour.determineInitialMove(hand);
        } else {
            workspace = this.playBehaviour.determineRegularMove(hand);
        }
        if (workspace != null) {
            this.initialMove = false;
            this.notifyObservers();
        }
        return workspace;
    }

    public String toString() {
        return "Player 2:\n# tiles: " + this.hand.getSize() + "\n\n";
    }
}