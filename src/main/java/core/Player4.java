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
            Meld newMeld = new Meld();
            for (int i = 0; i < meld.getSize(); i++) {
                newMeld.addTile(new Tile(meld.getTile(i).toString()));
            }
            tableCopy.add(newMeld);
        }
        
        ArrayList<Meld> workspace = new ArrayList<>();
        if (this.initialMove) {
            workspace = this.playBehaviour.determineInitialMove(hand, tableCopy);
        } else if (this.hand.getSize() - this.lowestHandCount >= 3) {
            // If a player is beating player 3 by 3 or more tiles, will play all that it can (Strategy 1)
            this.playBehaviour = new Strategy1();
            workspace = this.playBehaviour.determineRegularMove(hand, tableCopy);
        } else {
            // Otherwise, they will play only using tiles with values alredy on the table (Strategy 4)
            // Because if the value is not already on the table, there is a higher chance they can create a set using this tile
            this.playBehaviour = new Strategy4();
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
