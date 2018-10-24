package core;

import java.util.ArrayList;

public class Strategy1 implements PlayBehaviour {
    public ArrayList<Meld> determineMove(ArrayList<Meld> workspace, Hand hand, boolean initialMove) {
        if (initialMove) {
            return determineInitialMove(workspace, hand);
        }
        return determineRegularMove(workspace, hand);
    }
    
    // Plays as many melds as it can using only the tiles in its hand and only if the total of the melds is >= 30
    public ArrayList<Meld> determineInitialMove(ArrayList<Meld> workspace, Hand hand) {
        // First attempt to find largest meld that is >= 30 points
        ArrayList<Meld> melds1 = new ArrayList<>();
        for (int i = 0; i < hand.getSize(); i++) {
            // For each tile in hand, add it to a new meld
            ArrayList<Tile> tiles = new ArrayList<>();
            tiles.add(hand.getTile(i));
            Meld meld = new Meld();
            meld.addTile(tiles);
            melds1.add(meld);
            
            // For each tile in hand, attempt to add it to every preceding meld
            for (Meld tempMeld : melds1) {
                tempMeld.addTile(tiles);
            }      
        }
        
        // Filter melds totalling >= 30
        ArrayList<Meld> filteredMelds = new ArrayList<>();
        for (Meld tempMeld : melds1) {
            if (tempMeld.getValue() >= 30) {
                filteredMelds.add(tempMeld);
            }
        }

        // If there are melds with 30+ points, add largest to workspace and return
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
        
        // Otherwise, add as many melds as possible to workspace such that total points >= 30
        int totalTileValue = 0;
        boolean hasMelds = true;
        while (hasMelds && hand.getSize() != 0) {
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
           
            if (greatestMeld.isValidMeld()) {
                workspace.add(greatestMeld);
                totalTileValue += greatestMeld.getValue();
                for (int j = 0; j < greatestMeld.getSize(); j++) {
                    hand.remove(greatestMeld.getTile(j));
                }
            } else {
                hasMelds = false;
            }
        }
        
        if (totalTileValue >= 30) {
            return workspace;
        }
        return null;
    }
    
    // Plays all the tiles it can using its hand and the table
    public ArrayList<Meld> determineRegularMove(ArrayList<Meld> workspace, Hand hand) {
        boolean canPlayUsingHand = true;
        boolean canPlayUsingTable = true;
        boolean workspaceChanged = false;
        while (canPlayUsingHand && hand.getSize() > 0) {
            ArrayList<Meld> tempMelds = new ArrayList<>();
            for (int i = 0; i < hand.getSize(); i++)  {
                // For each time in hand, add it to a new meld
                ArrayList<Tile> tiles = new ArrayList<>();
                tiles.add(hand.getTile(i));
                Meld meld = new Meld();
                meld.addTile(tiles);
                tempMelds.add(meld);
                
                // For each tile in hand, attempt to add it to every preceding meld
                for (Meld tempMeld : tempMelds) {
                    tempMeld.addTile(tiles);
                }
            }
            
            // Once all tiles have been added to melds, find the largest meld
            Meld largestMeld = tempMelds.get(0);
            for (Meld tempMeld : tempMelds) {
                if (tempMeld.getSize() > largestMeld.getSize()) {
                    largestMeld = tempMeld;
                }
            }
            
            if (largestMeld.isValidMeld()) {
                workspace.add(largestMeld);
                workspaceChanged = true;
                for (int j = 0; j < largestMeld.getSize(); j++) {
                    hand.remove(largestMeld.getTile(j));
                }
            } else {
                canPlayUsingHand = false;
            }
        }
        
        while (canPlayUsingTable && hand.getSize() > 0) {
            // Attempt to add each tile in hand to each meld, removing from hand if added
            int tilesAdded = 0;
            for (int i = 0; i < hand.getSize(); i++) {
                for (Meld tempMeld : workspace) {
                    ArrayList<Tile> tiles = new ArrayList<>();
                    tiles.add(hand.getTile(i));
                    if (tempMeld.addTile(tiles)) {
                        tilesAdded++;
                        workspaceChanged = true;
                        hand.remove(i);
                        break;
                    }
                }
            }
            if (tilesAdded == 0) {
                canPlayUsingTable = false;
            }
        }
        
        if (workspaceChanged == true) {
            return workspace;
        }
        return null;
    }
}