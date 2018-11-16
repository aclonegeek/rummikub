package core;

import java.util.ArrayList;

import core.Globals.PlayerType;

public class PlayerHuman extends Player {
    public PlayerHuman() {
        this.playBehaviour = new StrategyHuman();
    }

    public PlayerHuman(String name) {
        this();
        this.name = name;
        this.playerType = PlayerType.PLAYERHUMAN;
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
        
        System.out.println(this.toString());
        ArrayList<Meld> workspace = new ArrayList<>();
        if (this.initialMove) {
            workspace = this.playBehaviour.determineInitialMove(this.hand, tableCopy);
        } else {
            workspace = this.playBehaviour.determineRegularMove(this.hand, tableCopy);
        }
        if (workspace != null) {
            this.initialMove = false;
            this.drawing = false;
        } else {
            this.drawing = true;
        }
        this.notifyObservers();
        this.drawing = false;
        System.out.println(this.toString());
        return workspace;
    }
}
