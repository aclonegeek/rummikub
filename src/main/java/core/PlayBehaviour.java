package core;

import java.util.ArrayList;

public abstract class PlayBehaviour implements TableObserver, PlayerObserver {
    protected int lowestHandCount = -1;
    protected ArrayList<Meld> workspace;

    public PlayBehaviour() {
        this.workspace = new ArrayList<>();
    }
    
    abstract ArrayList<Meld> determineInitialMove(Hand hand);
    abstract ArrayList<Meld> determineRegularMove(Hand hand);

    public void update(ArrayList<Meld> melds) {
        this.workspace = new ArrayList<>(melds);
    }

    public void update(int lowestHandCount) {
        if (this.lowestHandCount == -1 || lowestHandCount < this.lowestHandCount) {
            this.lowestHandCount = lowestHandCount;
        }
    }

    public ArrayList<Meld> getWorkspace() {
        return this.workspace;
    }
    
    public void setWorkspace(ArrayList<Meld> workspace) {
        this.workspace = workspace;
    }
    
    public int getLowestHandCount() {
        return this.lowestHandCount;
    }

    // Returns all possible melds in player's hand
    protected ArrayList<Meld> createMeldsFromHand(Hand hand) {
        // For each tile, add it to a new meld and add each meld to a new ArrayList
        ArrayList<ArrayList<Meld>> allMelds = new ArrayList<>();
        for (int i = 0; i < hand.getSize(); i++) {
            ArrayList<Meld> melds = new ArrayList<>();
            Meld meld = new Meld();
            meld.addTile(hand.getTile(i));
            melds.add(meld);
            allMelds.add(melds);
        }

        // For each tile, clone every meld and attempt to add the tile to it
        // If tile is added to the cloned meld, add this amended cloned meld to the ArrayList
        for (int i = 0; i < hand.getSize(); i++) {
            for (ArrayList<Meld> arrayList : allMelds) {
                for (int j = 0; j < arrayList.size(); j++) {
                    // Clone meld
                    Meld newMeld = new Meld();
                    for (int k = 0; k < arrayList.get(j).getSize(); k++) {
                        newMeld.addTile(arrayList.get(j).getTile(k));
                    }

                    // If tile is added to cloned meld, add this meld to ArrayList
                    if (newMeld.addTile(hand.getTile(i))) {
                        arrayList.add(newMeld);
                    }
                }
            }
        }

        // Filter out valid melds to a new ArrayList, ignoring duplicates
        ArrayList<Meld> validMelds = new ArrayList<>();
        for (ArrayList<Meld> tempArrayList : allMelds) {
            for (Meld meld : tempArrayList) {
                boolean duplicate = false;
                for (Meld validMeld : validMelds) {
                    if (meld.equals(validMeld)) {
                        duplicate = true;
                    }
                }
                if (meld.isValidMeld() && !duplicate) {
                    validMelds.add(meld);
                }
            }
        }
        return validMelds;
    }

    // Filters melds >= 30 points and returns the largest sized one
    protected Meld getLargestMeldOver30(ArrayList<Meld> melds) {
        if (melds.size() == 0) return null;
        ArrayList<Meld> filteredMelds = new ArrayList<>();
        for (Meld tempMeld : melds) {
            if (tempMeld.getValue() >= 30) {
                filteredMelds.add(tempMeld);
            }
        }

        if (filteredMelds.size() <= 0) return null;

        Meld largestMeld = filteredMelds.get(0);
        for (Meld tempMeld : filteredMelds) {
            if (tempMeld.getSize() > largestMeld.getSize()) {
                largestMeld = tempMeld;
            }
        }
        return largestMeld;
    }

    // Returns largest meld (by size)
    protected Meld getLargestMeld(ArrayList<Meld> melds) {
        if (melds.size() == 0) return null;
        Meld largestMeld = melds.get(0);
        for (Meld tempMeld : melds) {
            if (tempMeld.getSize() > largestMeld.getSize()) {
                largestMeld = tempMeld;
            }
        }
       return largestMeld;
    }

    // Returns greatest meld (by tile value)
    protected Meld getGreatestMeld(ArrayList<Meld> melds) {
        if (melds.size() == 0) return null;
        Meld greatestMeld = melds.get(0);
        for (Meld tempMeld : melds) {
            if (tempMeld.getValue() > greatestMeld.getValue()) {
                greatestMeld = tempMeld;
            }
        }
        return greatestMeld;
    }
    
    protected ArrayList<Meld> playMeldsWithoutExistingTiles(Hand hand) {
        ArrayList<Meld> workspace = new ArrayList<>(this.workspace);
        boolean workspaceChanged = false;
        // Plays all its melds to the table
        while (hand.getSize() > 0) {
            ArrayList<Meld> currentMelds = this.createMeldsFromHand(hand);
            Meld largestMeld = this.getLargestMeld(currentMelds);
            if (largestMeld != null) {
                workspace.add(largestMeld);
                for (int j = 0; j < largestMeld.getSize(); j++) {
                    hand.remove(largestMeld.getTile(j));
                }
                workspaceChanged = true;
            } else {
                break;
            }
        }
        if (workspaceChanged) {
            return workspace;
        }
        return null;
    }
    
    protected ArrayList<Meld> playTilesToExistingMelds(Hand hand) {
        ArrayList<Meld> workspace = new ArrayList<>(this.workspace);

        boolean workspaceChanged = false;
        // Then tries to add each tiles to every existing melds
        while (hand.getSize() > 0) {
            boolean tileAdded = false;
            for (int i = 0; i < hand.getSize(); i++) {
                ArrayList<Tile> tiles = new ArrayList<>();
                tiles.add(hand.getTile(i));
                for (Meld tempMeld : workspace) {
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
            return workspace;
        }
        return null;
    }
    
    // Tries to play all melds in player's hand using only tiles on the table
    protected ArrayList<Meld> playMeldsUsingExistingTiles(Hand hand) {
        ArrayList<Meld> workspace = new ArrayList<>(this.workspace); 
        boolean workspaceChanged = false;
        
        ArrayList<Meld> melds = this.createMeldsFromHand(hand);
        Meld handMeld = this.getLargestMeld(melds);

        while (true) {
            for (Meld meld : workspace) {
                // Clone meld
                Meld clone = new Meld();
                for (int k = 0; k < meld.getSize(); k++) {
                    clone.addTile(meld.getTile(k));
                }

                // Check if the clone can shrink will still remaining valid
                for (int l = 0; l < clone.getSize(); l++) {
                    Tile removedTile = clone.removeTile(l);
                    Boolean addedTile = handMeld.addTile(removedTile);    
                    
                    if (addedTile && clone.isValidMeld()) {
                        meld.removeTile(l);
                        workspaceChanged = true;
                        workspace.add(handMeld);

                        for (int m = 0; m < handMeld.getSize(); m++) {
                            hand.remove(handMeld.getTile(m));
                        }
                        break;
                    } else {
                        workspaceChanged = false;
                        if (addedTile) {
                            for (int t = 0; t < handMeld.getSize(); t++) {
                                if (handMeld.getTile(t).equals(removedTile)) {
                                    handMeld.removeTile(t);
                                }
                            }
                        }
                        clone.addTile(removedTile);
                    }
                }
                // Next meld
                if (workspaceChanged) {
                    melds = this.createMeldsFromHand(hand);
                    handMeld = this.getLargestMeld(melds);
                    break;
                }
            }
            if (melds.size() <= 1) {
                break;
            }
        }
        if (workspaceChanged) {
            return workspace;
        }
        return null;
    }
    
    // Tries to play all potential melds in player's hand using tiles on the table
    protected ArrayList<Meld> playPotentialMeldsUsingExistingTiles(Hand hand) {
        ArrayList<Meld> workspace = new ArrayList<>(this.workspace); 
        boolean workspaceChanged = false;

        // Create all possible pairs and check if they are potential runs/sets
        for (int i = 0; i < hand.getSize() - 1; i++) {
            for (int j = i + 1; j < hand.getSize(); j++) {
                
                ArrayList<Tile> pair = new ArrayList<>();
                Meld tempMeld = new Meld();
                pair.add(hand.getTile(i));
                pair.add(hand.getTile(j));
                tempMeld.addTile(pair);

                if (tempMeld.isPotentialMeld()) {
                    for (Meld meld : workspace) {
                        // Clone meld
                        Meld clone = new Meld();
                        for (int k = 0; k < meld.getSize(); k++) {
                            clone.addTile(meld.getTile(k));
                        }

                        // Check if the clone can shrink will still remaining valid
                        for (int l = 0; l < clone.getSize(); l++) {
                            Tile removedTile = clone.removeTile(l);
                            Boolean addedTile = tempMeld.addTile(removedTile);
                            if (tempMeld.isValidMeld() && clone.isValidMeld()) {
                                meld.removeTile(l);
                                workspaceChanged = true;
                                workspace.add(tempMeld);
                                for (Tile tile : pair) {
                                    hand.remove(tile);
                                }
                                break;
                            } else {
                                workspaceChanged = false;
                                if (addedTile) {
                                    // Remove the tile
                                    for (int t = 0; t < tempMeld.getSize(); t++) {
                                        if (tempMeld.getTile(t).equals(removedTile)) {
                                            tempMeld.removeTile(t);
                                        }
                                    }
                                }
                                // The removal of this tile does not create two valid melds
                                // Add it back to its parent meld
                                clone.addTile(removedTile);
                            }
                        }
                        if (workspaceChanged) {
                            break;
                        }
                    }
                }
                // Try next pair
            }
        }
        if (workspaceChanged) {
            return workspace;
        }
        return null;
    }
    
    // Tries to extract a tile from each meld on the table and add it to
    // the current tile in the player's hand until a meld is formed
    protected ArrayList<Meld> playTileUsingExistingTiles(Hand hand) {
        ArrayList<Meld> workspace = new ArrayList<>(this.workspace);
        
        boolean workspaceChanged = false;
        boolean removedFront = false;
        boolean removedBack = false;
        int offset = 0;
        ArrayList<Meld> addedMelds = new ArrayList<>();
        ArrayList<Meld> workspace2 = new ArrayList<>(workspace);
        ArrayList<Tile> removedTiles = new ArrayList<>(); 

        for (int i = 0; i < hand.getSize(); i++) {
            Tile currentHandTile = hand.getTile(i);

            Meld tempMeld = new Meld();
            tempMeld.addTile(currentHandTile);
            
            for (int j = 0; j < workspace.size(); j++) {
                Meld meld = workspace.get(j);

                // Clone meld
                Meld clone = new Meld();
                for (int k = 0; k < meld.getSize(); k++) {
                    clone.addTile(meld.getTile(k));
                }

                // Check if the clone can shrink will still remaining valid
                // for each tile in the clone
                for (int l = 0; l < clone.getSize(); l++) {
                    Tile currTile = clone.getTile(l);
                    Meld secondHalf = new Meld();
                    
                    if (tempMeld.addTile(currTile)) {
                        if (l == 0) {
                            removedFront = true;
                            secondHalf = clone.splitMeld(l + 1);
                        } else if (l == clone.getSize() - 1) {
                            removedBack = true;
                            //secondHalf = clone.splitMeld(l);
                        } else {
                            secondHalf = clone.splitMeld(l + 1);
                        }
                        
                        removedTiles.add(clone.removeTileObject(currTile));
                        
                        if (removedFront && secondHalf.isValidMeld() || removedBack && clone.isValidMeld()
                                || clone.isValidMeld() && secondHalf.isValidMeld()) {

                            workspace2.remove(j + offset);

                            if (!clone.isValidMeld()) {
                                workspace2.add(j + offset, secondHalf);
                            } else if (!secondHalf.isValidMeld()) {
                                workspace2.add(j + offset, clone);
                            } else {
                                workspace2.add(j + offset, clone);
                                workspace2.add(j + offset + 1, secondHalf);
                                offset += 1;
                            }

                            if (tempMeld.isValidMeld()) {
                                // add to table
                                addedMelds.add(tempMeld);
                                // move to next tile in hand
                                workspaceChanged = true;

                                workspace2.addAll(addedMelds);
                                workspace = workspace2;
                                
                                // remove tile from hand
                                hand.remove(currentHandTile);
                            } else {
                                // potential meld
                                // move to next meld
                                workspaceChanged = false;
                            }
                            break;
                        } else {
                            clone = new Meld();

                            for (int k = 0; k < meld.getSize(); k++) {
                                clone.addTile(meld.getTile(k));
                            }
                            workspace2.get(j + offset).addTile(removedTiles);
                            tempMeld.removeTileObject(currTile);
                        }
                    }
                }
                // try next tile in hand
                if (workspaceChanged) {
                    break;
                }
            }
        }
        if (workspaceChanged) {
            return workspace2;
        }
        return null;
    }
}