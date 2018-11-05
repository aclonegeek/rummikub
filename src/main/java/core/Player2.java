package core;

import java.util.ArrayList;

public class Player2 extends Player {
    public Player2() {
        this.playBehaviour = new Strategy2();
    }

    public Player2(String name) {
        this();
        this.name = name;
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
            System.out.println(this.toString());
            return null;
        }
        if (workspace != null) {
            this.initialMove = false;
            this.drawing = false;
            System.out.println(this.name + " played tiles");
        } else {
            this.drawing = true;
        }
        this.notifyObservers();
        System.out.println(this.toString());
        return workspace;
    }
}
