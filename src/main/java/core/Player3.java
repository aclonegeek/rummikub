package core;

import java.util.ArrayList;

public class Player3 extends Player {
    public Player3() {
        // Player 3's strategy is the same as Strategy 2 in the initial move
        // the only difference is the circumstance in which it is used (see play())
        this.playBehaviour = new Strategy2();
    }

    protected ArrayList<Meld> play() {
        ArrayList<Meld> workspace = new ArrayList<>();
        if (this.initialMove) {
            workspace = this.playBehaviour.determineInitialMove(hand);
        } else if (this.hand.getSize() - this.lowestHandCount >= 3) {
            // If a player is beating player 3 by 3 or more tiles, will play all that it can (Strategy 1)
            ArrayList<Meld> originalWorkspace = this.playBehaviour.getWorkspace();
            this.playBehaviour = new Strategy1();
            this.playBehaviour.setWorkspace(originalWorkspace);
            workspace = this.playBehaviour.determineRegularMove(hand);
        } else {
            // Otherwise, will play only with existing tiles (Strategy 2)
            ArrayList<Meld> originalWorkspace = this.playBehaviour.getWorkspace();
            this.playBehaviour = new Strategy2();
            this.playBehaviour.setWorkspace(originalWorkspace);
            workspace = this.playBehaviour.determineRegularMove(hand);
        }
        if (workspace != null) {
            this.initialMove = false;
        }
        // Reset lowestHandCount for the next turn
        this.lowestHandCount = 100;
        return workspace;
    }

    public String toString() {
        return "Player 3:\n# tiles: " + this.hand.getSize() + "\n\n";
    }
}
