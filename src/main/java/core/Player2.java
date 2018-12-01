package core;

import java.util.ArrayList;

import core.Globals.PlayerType;

public class Player2 extends Player {
    public Player2() {
        this.playBehaviour = new Strategy2();
    }

    public Player2(String name) {
        this();
        this.name = name;
        this.playerType = PlayerType.PLAYER2;
    }

    protected ArrayList<Meld> play(ArrayList<Meld> tableState) {
        ArrayList<Meld> tableCopy = new ArrayList<>();
        for (Meld meld : tableState) {
            tableCopy.add(new Meld(meld));
        }
        
        ArrayList<Meld> workspace = new ArrayList<>();
        
        // If the workspace is not empty and it is the player's initial move
        if (this.initialMove && tableCopy.size() > 0) {
            workspace = this.playBehaviour.determineInitialMove(hand, tableCopy);
        } else if (!this.initialMove) {
            workspace = this.playBehaviour.determineRegularMove(hand, tableCopy);
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
