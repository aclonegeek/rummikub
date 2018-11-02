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

        // Otherwise, adds as few melds as possible to workspace such that total points
        // >= 30 pts
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

    // Try to play all its tiles to the table and win
    private ArrayList<Meld> hasWinningHand(Hand hand) {
        ArrayList<Meld> originalWorkspace = new ArrayList<>(this.workspace);
        ArrayList<Meld> success =  new ArrayList<>();
        
        success = this.playMeldsWithoutExistingTiles(hand);
        if (success != null) { this.workspace = success; }
        if (hand.getSize() == 0) { return this.workspace; }
        
        success = this.playPotentialMeldsUsingExistingTiles(hand);
        if (success != null) { this.workspace = success; }
        if (hand.getSize() == 0) { return this.workspace; }
        
        success = this.playTileUsingExistingTiles(hand);
        if (success != null) { this.workspace = success; }
        if (hand.getSize() == 0) { return this.workspace; }
        
        success = this.playTilesToExistingMelds(hand);
        if (success != null) { this.workspace = success; }
        if (hand.getSize() == 0) { return this.workspace; }
        
        this.workspace = originalWorkspace;
        return null;
    }
   
    // Plays all the tiles it can using its hand and the table
    public ArrayList<Meld> determineRegularMove(Hand hand) {
        ArrayList<Meld> success = null;
        
        success = this.hasWinningHand(hand);
        if (success != null) { return this.workspace = success; } 
        
        
        success = this.playPotentialMeldsUsingExistingTiles(hand);
        if (success != null) { this.workspace = success; }
        
        
        success = this.playTileUsingExistingTiles(hand);
        if (success != null) {this.workspace = success; }
        
        success = this.playTilesToExistingMelds(hand);
        if (success != null) { this.workspace = success; }

        return success;
    }
}