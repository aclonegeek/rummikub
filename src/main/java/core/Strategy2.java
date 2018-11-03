package core;

import java.util.ArrayList;

public class Strategy2 extends PlayBehaviour {
    public ArrayList<Meld> determineInitialMove(Hand hand) {
        // Add the largest meld >= 30 pts, if it exists, to the workspace
        ArrayList<Meld> melds = this.createMeldsFromHand(hand);
        Meld largestMeldOver30 = this.getLargestMeldOver30(melds);
        if (largestMeldOver30 != null) {
            this.workspace.add(largestMeldOver30);
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
                for (int j = 0; j < largestMeld.getSize(); j++) {
                    hand.remove(largestMeld.getTile(j));
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
   
    // Plays all the tiles it can using its hand and the table
    public ArrayList<Meld> determineRegularMove(Hand hand) {
        // Make deep copy of hand
        Hand handCopy = new Hand();
        for (int i = 0; i < hand.getSize(); i++) {
            handCopy.add(hand.getTile(i));
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
        
        // Check if player can win this turn, if so return the winning workspace and remove tiles from hand
        ArrayList<Meld> winningWorkspace = hasWinningHand(handCopy);
        if (winningWorkspace != null) {
            while (hand.getSize() > 0) {
                hand.remove(0);
            }
            return winningWorkspace;
        }
        
        // Otherwise add as many tiles as possible using tiles already on the board
        this.workspace = this.playUsingHandAndTable(hand);
        if (!workspace.toString().equals(workspaceCopy.toString())) {
            return this.workspace;
        }
        
        return null;
    }
}