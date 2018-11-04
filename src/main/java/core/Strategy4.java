package core;

import java.util.ArrayList;

public class Strategy4 extends PlayBehaviour {
    public ArrayList<Meld> determineInitialMove(Hand hand) {
        // Add the largest meld >= 30 pts, if it exists, to the workspace
        ArrayList<Meld> melds = this.createMeldsFromHand(hand);
        Meld largestMeldOver30 = this.getLargestMeldOver30(melds);
        if (largestMeldOver30 != null) {
            this.workspace.add(largestMeldOver30);
            for (int i = 0; i < largestMeldOver30.getSize(); i++) {
                hand.remove(largestMeldOver30.getTile(i));
            }
            return this.workspace;
        }

        // Otherwise, adds as few melds as possible to workspace such that total points >= 30 pts
        int totalTileValue = 0;
        while (hand.getSize() != 0) {
            ArrayList<Meld> currentMelds = this.createMeldsFromHand(hand);
            Meld largestMeld = this.getLargestMeld(currentMelds);
            if (largestMeld != null) {
                this.workspace.add(largestMeld);
                totalTileValue += largestMeld.getValue();
                for (int i = 0; i < largestMeld.getSize(); i++) {
                    hand.remove(largestMeld.getTile(i));
                }

                // Break out of the loop as soon as the player reaches 30 points (or more)
                if (totalTileValue >= 30) {
                    return this.workspace;
                }
            } else {
                break;
            }
        }
        return null;
    }
    
    // Plays all the tiles it can but only if their values already appear on the table
    public ArrayList<Meld> determineRegularMove(Hand hand) {
        // Make copy of hand that only includes tiles with values already on table
        Hand filteredHand = new Hand();
        for (int i = 0; i < hand.getSize(); i++) {
            Tile tile = hand.getTile(i);
            for (Meld meld : this.workspace) {
                if (meld.containsTileValue(tile)) {
                    filteredHand.add(tile);
                    break;
                }
            }
        }
        
        Hand filteredHandCopy = new Hand();
        for (int i = 0; i < filteredHand.getSize(); i++) {
            filteredHandCopy.add(filteredHand.getTile(i));
        }
        
        // Make deep copy of workspace
        ArrayList<Meld> workspaceCopy = new ArrayList<Meld>();
        for (Meld meld : this.workspace) {
            Meld newMeld = new Meld();
            for (int i = 0; i < meld.getSize(); i++) {
                newMeld.addTile(new Tile(meld.getTile(i).toString()));
            }
            workspaceCopy.add(newMeld);
        }
        
        this.workspace = this.playUsingHand(filteredHand);
        this.workspace = this.playUsingHandAndTable(filteredHand);
        
        // Remove tiles from hand that were removed from filteredHand
        for (int i = 0; i < filteredHandCopy.getSize(); i++) {
            if (!filteredHand.containsTile(filteredHandCopy.getTile(i))) {
                hand.remove(filteredHandCopy.getTile(i));
            }
        }
        
        if (!workspace.toString().equals(workspaceCopy.toString())) {
            return this.workspace;
        }
        return null;
    }
}