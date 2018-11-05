package core;

import java.util.ArrayList;

public class Player2 extends Player {
    public Player2() {
        this.playBehaviour = new Strategy2();
    }

    protected ArrayList<Meld> play() {
        ArrayList<Meld> workspace = new ArrayList<>();
        
        // If the workspace is not empty and it is the player's initial move
        if (this.initialMove && this.getWorkspace().size() > 0) {
            workspace = this.playBehaviour.determineInitialMove(hand);
        } else if (!this.initialMove) {
            workspace = this.playBehaviour.determineRegularMove(hand);
        } else {
            this.drawing = true;
            this.notifyObservers();
            return null;
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
        return "Player 2:\n# tiles: " + this.hand.getSize() + "\n\n";
    }
}
