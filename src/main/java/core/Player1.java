package core;

import java.util.ArrayList;

public class Player1 extends Player {
    public Player1() {
        this.playBehaviour = new Strategy1();
    }

    public Player1(String name) {
        this();
        this.name = name;
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
            System.out.println(this.name + " played tiles");
        } else {
            this.drawing = true;
        }
        this.notifyObservers();
        System.out.println(this.toString());
        return workspace;
    }
}
