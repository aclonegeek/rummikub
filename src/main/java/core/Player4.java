package core;

import java.util.ArrayList;

public class Player4 extends Player {
    public Player4() {
        this.playBehaviour = new Strategy4();
    }

    protected ArrayList<Meld> play() {
        ArrayList<Meld> workspace = new ArrayList<>();
        if (this.initialMove) {
            workspace = this.playBehaviour.determineInitialMove(hand);
        } else if (this.hand.getSize() - this.playBehaviour.getLowestHandCount() >= 3) {
            // If a player is beating player 3 by 3 or more tiles, will play all that it can (Strategy 1)
            ArrayList<Meld> originalWorkspace = this.playBehaviour.getWorkspace();
            this.playBehaviour = new Strategy1();
            this.playBehaviour.setWorkspace(originalWorkspace);
            workspace = this.playBehaviour.determineRegularMove(hand);
        } else {
            // Otherwise, they will play only using tiles with values alredy on the table (Strategy 4)
            // Because if the value is not already on the table, there is a higher chance they can create a set using this tile
            ArrayList<Meld> originalWorkspace = this.playBehaviour.getWorkspace();
            this.playBehaviour = new Strategy4();
            this.playBehaviour.setWorkspace(originalWorkspace);
            workspace = this.playBehaviour.determineRegularMove(hand);
        }  
        if (workspace != null) {
            this.initialMove = false;
            this.notifyObservers();
        }
        return workspace;
    }
    
    public String toString() {
        return "Player 4:\n# tiles: " + this.hand.getSize() + "\n\n";
    }
}
