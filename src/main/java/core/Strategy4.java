package core;

import java.util.ArrayList;

public class Strategy4 extends PlayBehaviour {
    public ArrayList<Meld> determineInitialMove(Hand hand, ArrayList<Meld> workspace) {
        // Add the largest meld >= 30 pts, if it exists, to the workspace
        ArrayList<Meld> melds = this.createMeldsFromHand(hand);
        Meld largestMeldOver30 = this.getLargestMeldOver30(melds);
        if (largestMeldOver30 != null) {
            workspace.add(largestMeldOver30);
            for (int i = 0; i < largestMeldOver30.getSize(); i++) {
                hand.remove(largestMeldOver30.getTile(i));
            }
            return workspace;
        }

        // Otherwise, adds as few melds as possible to workspace such that total points >= 30 pts
        int totalTileValue = 0;
        ArrayList<Tile> tilesRemoved = new ArrayList<>();
        ArrayList<Meld> meldsToAdd   = new ArrayList<>();
        while (hand.getSize() != 0) {
            ArrayList<Meld> currentMelds = this.createMeldsFromHand(hand);
            Meld largestMeld = this.getLargestMeld(currentMelds);

            if (largestMeld == null) {
                break;
            }

            meldsToAdd.add(largestMeld);
            totalTileValue += largestMeld.getValue();
            for (int i = 0; i < largestMeld.getSize(); i++) {
                hand.remove(largestMeld.getTile(i));
                tilesRemoved.add(largestMeld.getTile(i));
            }

            // Break out of the loop as soon as the player reaches 30 points (or more)
            if (totalTileValue >= 30) {
                for (Meld meld : meldsToAdd) {
                    workspace.add(meld);
                }
                return workspace;
            }
        }

        for (Tile tile : tilesRemoved) {
            hand.add(tile);
        }

        return null;
    }
    
    // Plays all the tiles it can but only if their values already appear on the table
    public ArrayList<Meld> determineRegularMove(Hand hand, ArrayList<Meld> workspace) {
        // Make copy of hand that only includes tiles with values already on table
        Hand filteredHand = new Hand();
        for (int i = 0; i < hand.getSize(); i++) {
            Tile tile = hand.getTile(i);
            for (Meld meld : workspace) {
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
        for (Meld meld : workspace) {
            Meld newMeld = new Meld();
            for (int i = 0; i < meld.getSize(); i++) {
                newMeld.addTile(new Tile(meld.getTile(i).toString()));
            }
            workspaceCopy.add(newMeld);
        }
        
        workspace = this.playUsingHand(filteredHand, workspace);
        workspace = this.playUsingHandAndTable(filteredHand, workspace);
        
        // Remove tiles from hand that were removed from filteredHand
        for (int i = 0; i < filteredHandCopy.getSize(); i++) {
            if (!filteredHand.containsTile(filteredHandCopy.getTile(i))) {
                hand.remove(filteredHandCopy.getTile(i));
            }
        }
        
        if (tilesAddedToWorkspace(workspace, workspaceCopy)) {
            return workspace;
        }
        return null;
    }
}