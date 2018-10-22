package core;

import java.util.ArrayList;

public abstract class PlayBehaviour {
    public ArrayList<Meld> determineMove(ArrayList<Meld> workspace, Hand hand, boolean initialMove) {
        if (initialMove) {
            return determineInitialMove(workspace, hand);
        }
        return determineRegularMove(workspace, hand);
    }
    
    // Plays as many melds as it can using only the tiles in its hand and only if the total of the melds is >= 30
    public ArrayList<Meld> determineInitialMove(ArrayList<Meld> workspace, Hand hand) {
        boolean hasMelds = true;
        int totalTileValue = 0;
        while (hasMelds) {
            ArrayList<Meld> tempMelds = new ArrayList<>();
            for (int i = 0; i < hand.getSize(); i++) {      
                // For each tile in hand, add it to a new meld
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
            
            // Once all tiles have been added to melds, find the meld with the greatest value
            Meld greatestMeld = tempMelds.get(0);
            for (Meld tempMeld : tempMelds) {
                if ((tempMeld.isValidMeld() && !greatestMeld.isValidMeld()) || 
                    (tempMeld.getValue() > greatestMeld.getValue() && tempMeld.isValidMeld())) {
                    greatestMeld = tempMeld;
                }
            }
            
            if (greatestMeld.isValidMeld()) {
                totalTileValue += greatestMeld.getValue();
                workspace.add(greatestMeld);
                
                // Remove tiles in largestMeld from hand
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
    
    public ArrayList<Meld> determineRegularMove(ArrayList<Meld> workspace, Hand hand) {
        System.out.println("This should never print because this method should have been overridden.");
        return null;
    }
}