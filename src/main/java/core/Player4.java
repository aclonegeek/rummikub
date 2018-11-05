package core;

import java.util.ArrayList;

public class Player4 extends Player {
    public Player4() {
        this.playBehaviour = new Strategy4();
    }

    public Player4(String name) {
        this();
        this.name = name;
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
            System.out.println(this.name + " played tiles");
        }
        this.lowestHandCount = 100;
        System.out.println(this.toString());
        return workspace;
    }
}
