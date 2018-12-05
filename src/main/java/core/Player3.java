package core;

import java.util.ArrayList;

import core.Globals.PlayerType;

public class Player3 extends Player {
    public Player3() {
        // Player 3's strategy is the same as Strategy 2 in the initial move
        // the only difference is the circumstance in which it is used (see play())
        this.playBehaviour = new Strategy2();
    }

    public Player3(String name) {
        this();
        this.name = name;
        this.playerType = PlayerType.PLAYER3;
    }

    protected ArrayList<Meld> play(ArrayList<Meld> tableState) {
        ArrayList<Meld> tableCopy = new ArrayList<>();
        for (Meld meld : tableState) {
            tableCopy.add(new Meld(meld));
        }
        
        ArrayList<Meld> workspace = new ArrayList<>();
        if (this.initialMove) {
            workspace = this.playBehaviour.determineInitialMove(hand, tableCopy);
        } else if (this.hand.getSize() - this.lowestHandCount >= 3) {
            // If a player is beating player 3 by 3 or more tiles, will play all that it can (Strategy 1)
            this.playBehaviour = new Strategy1();
            workspace = this.playBehaviour.determineRegularMove(hand, tableCopy);
            if (workspace == null) {
                System.out.println("p3 could play but has not tile to play");
            }
        } else {
            // Otherwise, will play only with existing tiles (Strategy 2)
            this.playBehaviour = new Strategy2();
            workspace = this.playBehaviour.determineRegularMove(hand, tableCopy);
        }
        if (workspace != null) {
            this.initialMove = false;
        }
        this.lowestHandCount = 100;
        return workspace;
    }
}
