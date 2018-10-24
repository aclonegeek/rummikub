package core;

import java.util.ArrayList;

public class Strategy2 implements PlayBehaviour {
    public ArrayList<Meld> determineMove(ArrayList<Meld> workspace, Hand hand, boolean initialMove) {
        if (initialMove && workspace.size() == 0) {
            return null;
        } else if (initialMove) {
            return determineInitialMove(workspace, hand);
        }
        return determineRegularMove(workspace, hand);
    }
    
    // Plays as few melds as it can using only the tiles in its hand and only if the total of the melds is >= 30
    public ArrayList<Meld> determineInitialMove(ArrayList<Meld> workspace, Hand hand) {
        // For each tile in hand, add it to a new meld and attempt to add it to every preceding meld
        ArrayList<Meld> melds1 = new ArrayList<>();
        for (int i = 0; i < hand.getSize(); i++) {
            ArrayList<Tile> tiles = new ArrayList<>();
            tiles.add(hand.getTile(i));
            Meld meld = new Meld();
            meld.addTile(tiles);
            melds1.add(meld);
            for (Meld tempMeld : melds1) {
                tempMeld.addTile(tiles);
            }      
        }
        
        // If there are melds with >= 30 points, add larget to workspace and return
        ArrayList<Meld> filteredMelds = new ArrayList<>();
        for (Meld tempMeld : melds1) {
            if (tempMeld.getValue() >= 30) {
                filteredMelds.add(tempMeld);
            }
        }

        if (filteredMelds.size() > 0) {
            Meld largestMeld = filteredMelds.get(0);
            for (Meld tempMeld : filteredMelds) {
                if (tempMeld.getValue() > largestMeld.getValue()) {
                    largestMeld = tempMeld;
                }
            }
            workspace.add(largestMeld);
            return workspace;
        }
        
        // Otherwise, add as few melds as possible to workspace such that total points >= 30
        int totalTileValue = 0;
        boolean hasMelds = true;
        while (hasMelds) {
            ArrayList<Meld> melds2 = new ArrayList<>();
            for (int i = 0; i < hand.getSize(); i++) {
                ArrayList<Tile> tiles = new ArrayList<>();
                tiles.add(hand.getTile(i));
                Meld meld = new Meld();
                meld.addTile(tiles);
                melds2.add(meld);  
                for (Meld tempMeld : melds2) {
                    tempMeld.addTile(tiles);
                }
            }

            // Find meld with greatest value
            Meld greatestMeld = melds2.get(0);
            for (Meld tempMeld : melds2) {
                if ((tempMeld.isValidMeld() && !greatestMeld.isValidMeld()) || 
                    (tempMeld.getValue() > greatestMeld.getValue() && tempMeld.isValidMeld())) {
                    greatestMeld = tempMeld;
                }
            }
           
            // If at any point totalTileValue is >= 30, we've found the fewest melds
            if (greatestMeld.isValidMeld()) {
                workspace.add(greatestMeld);
                totalTileValue += greatestMeld.getValue();
                if (totalTileValue >= 30) {
                    return workspace;
                }
                for (int j = 0; j < greatestMeld.getSize(); j++) {
                    hand.remove(greatestMeld.getTile(j));
                }
            } else {
                hasMelds = false;
            }
        }
        
        return null;
    }
    
    // If it can play all its tiles (possibly using some tiles already on the table), it does
    // Otherwise, uses only the tiles of its hand that require using tiles on the table to make melds
    public ArrayList<Meld> determineRegularMove(ArrayList<Meld> workspace, Hand hand) {
        return null;
    }
}