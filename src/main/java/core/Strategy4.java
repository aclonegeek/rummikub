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
        // Make deep copy of workspace
        ArrayList<Meld> workspaceCopy = new ArrayList<Meld>();
        for (Meld meld : workspace) {
            workspaceCopy.add(new Meld(meld));
        }
        
        for (int i = 0; i < 3; i++) {
            workspace = this.playUsingHandWithPriority(hand, workspace);
            workspace = this.playUsingHandAndTable_AddToPotentialMeldsInHand(hand, workspace);
            workspace = this.playUsingHandAndTable_AddToTilesInHand(hand, workspace);
            workspace = this.playUsingHandAndTable_AddToMeldsOnTable(hand, workspace);
            workspace = this.rearrangeWorkspace(workspace);
        }
        
        if (tilesAddedToWorkspace(workspace, workspaceCopy)) {
            return workspace;
        }
        return null;
    }
}