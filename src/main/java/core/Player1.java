package core;

import java.util.ArrayList;

import core.Globals.PlayerType;

public class Player1 extends Player {
    public Player1() {
        this.playBehaviour = new Strategy1();
    }

    public Player1(String name) {
        this();
        this.name = name;
        this.playerType = PlayerType.PLAYER1;
    }

    protected ArrayList<Meld> play(ArrayList<Meld> tableState) {
        ArrayList<Meld> tableCopy = new ArrayList<>();
        for (Meld meld : tableState) {
            Meld newMeld = new Meld();
            for (int i = 0; i < meld.getSize(); i++) {
                newMeld.addTile(new Tile(meld.getTile(i).toString()));
            }
            tableCopy.add(newMeld);
        }
        
        ArrayList<Meld> workspace = new ArrayList<>();
        if (this.initialMove) {
            workspace = this.playBehaviour.determineInitialMove(hand, tableCopy);
        } else {
            workspace = this.playBehaviour.determineRegularMove(hand, tableCopy);
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
