package core;

import java.util.ArrayList;

import core.Globals.PlayerType;

public class Player4 extends Player {
    public Player4() {
        this.playBehaviour = new Strategy4();
    }

    public Player4(String name) {
        this();
        this.name = name;
        this.playerType = PlayerType.PLAYER4;
    }

    protected ArrayList<Meld> play(ArrayList<Meld> tableState) {
        ArrayList<Meld> tableCopy = new ArrayList<>();
        for (Meld meld : tableState) {
            tableCopy.add(new Meld(meld));
        }
        
        ArrayList<Meld> workspace = new ArrayList<>();
        if (this.initialMove) {
            workspace = this.playBehaviour.determineInitialMove(hand, tableCopy);
        } else {
            workspace = this.playBehaviour.determineRegularMove(hand, tableCopy);
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
