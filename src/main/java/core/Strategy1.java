package core;

import java.util.ArrayList;

public class Strategy1 extends PlayBehaviour {
    // Plays as many melds as it can using only the tiles in its hand and only if the total of the melds is >= 30
    public ArrayList<Meld> determineInitialMove(Hand hand) {
        // Return largest meld >= 30, if it exists
        ArrayList<Meld> melds = this.createMeldsFromHand(hand);
        Meld largestMeldOver30 = this.getLargestMeldOver30(melds);
        if (largestMeldOver30 != null) {
            this.workspace.add(largestMeldOver30);
            return this.workspace;
        }
        
        // Otherwise, adds as many melds as possible to workspace such that total points >= 30
        int totalTileValue = 0;
        while (hand.getSize() != 0) {
            ArrayList<Meld> currentMelds = this.createMeldsFromHand(hand);
            Meld greatestMeld = this.getGreatestMeld(currentMelds);
            if (greatestMeld != null) {
                this.workspace.add(greatestMeld);
                totalTileValue += greatestMeld.getValue();
                for (int j = 0; j < greatestMeld.getSize(); j++) {
                    hand.remove(greatestMeld.getTile(j));
                }
            } else {
                break;
            }
        }
        
        if (totalTileValue >= 30) {
            return this.workspace;
        }
        return null;
    }
    
    // Plays all the tiles it can using its hand and the table
    public ArrayList<Meld> determineRegularMove(Hand hand) {
        // Make deep copy of workspace
        ArrayList<Meld> workspaceCopy = new ArrayList<Meld>();
        for (Meld meld : this.workspace) {
            Meld newMeld = new Meld();
            for (int i = 0; i < meld.getSize(); i++) {
                newMeld.addTile(new Tile(meld.getTile(i).toString()));
            }
            workspaceCopy.add(newMeld);
        }
    
        this.workspace = this.playUsingHand(hand);
        this.workspace = this.playUsingHandAndTable(hand);
        
        if (!workspace.toString().equals(workspaceCopy.toString())) {
            return this.workspace;
        }
        return null;
    }
}