package core;

import java.util.ArrayList;

public class Player1 extends Player {
    public Player1() {
        this.playBehaviour = new Strategy1();
    }

    protected ArrayList<Meld> play() {
        ArrayList<Meld> workspace = new ArrayList<>();
        if (this.initialMove) {
            workspace = this.playBehaviour.determineInitialMove(hand);
        } else {
            workspace = this.playBehaviour.determineRegularMove(hand);
        }
        if (workspace != null) {
            this.initialMove = false;
            this.drawing = false;
        } else {
            this.drawing = true;
        }
        this.notifyObservers();
        return workspace;
    }

    public String toString() {
        return "Player 1:\n# tiles: " + this.hand.getSize() + "\n\n";
    }
}
