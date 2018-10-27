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
        boolean workspaceChanged = false;
        // First tries to add new melds using tiles in hand
        while (hand.getSize() > 0) {
            ArrayList<Meld> melds = this.createMeldsFromHand(hand);
            Meld largestMeld = this.getLargestMeld(melds);
            if (largestMeld != null) {
                this.workspace.add(largestMeld);
                workspaceChanged = true;
                for (int j = 0; j < largestMeld.getSize(); j++) {
                    hand.remove(largestMeld.getTile(j));
                }
            } else {
                break;
            }
        }
        
        // Then tries to add each tiles to every existing melds
        while (hand.getSize() > 0) {
            boolean tileAdded = false;
            for (int i = 0; i < hand.getSize(); i++) {
                ArrayList<Tile> tiles = new ArrayList<>();
                tiles.add(hand.getTile(i));
                for (Meld tempMeld : this.workspace) {
                    if (tempMeld.addTile(tiles)) {
                        tileAdded = true;
                        workspaceChanged = true;
                        hand.remove(i);
                        break;
                    }
                }
            }
            if (!tileAdded) {
                break;
            }
        }
        if (workspaceChanged) {
            return this.workspace;
        }
        return null;
    }
}