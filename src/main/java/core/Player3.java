package core;

import java.util.ArrayList;

public class Player3 extends Player {
    public Player3() {
        this.playBehaviour = new Strategy2();
    }

    protected ArrayList<Meld> play() {
        ArrayList<Meld> workspace = new ArrayList<>();
        if (this.initialMove) {
            workspace = this.playBehaviour.determineInitialMove(hand);
        } else if (this.hand.getSize() - this.playBehaviour.getLowestHandCount() >= 3) {
            this.playBehaviour = new Strategy1();
            workspace = this.playBehaviour.determineRegularMove(hand);
        } else {
            this.playBehaviour = new Strategy2();
            workspace = this.playBehaviour.determineRegularMove(hand);
        }  
        if (workspace != null) {
            this.initialMove = false;
            this.notifyObservers();
        }
        return workspace;
    }

    public String toString() {
        return "Player 3:\n# tiles: " + this.hand.getSize() + "\n\n";
    }
}