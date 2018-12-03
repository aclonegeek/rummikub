package core;

import java.util.ArrayList;

public abstract class PlayBehaviour {

    abstract ArrayList<Meld> determineInitialMove(Hand hand, ArrayList<Meld> workspace);
    abstract ArrayList<Meld> determineRegularMove(Hand hand, ArrayList<Meld> workspace);

    // Returns all possible valid melds in player's hand
    protected ArrayList<Meld> createMeldsFromHand(Hand hand) {
        // For each tile, add it to a new meld and add each meld to a new ArrayList
        ArrayList<ArrayList<Meld>> allMelds = new ArrayList<>();
        for (int i = 0; i < hand.getSize(); i++) {
            ArrayList<Meld> melds = new ArrayList<>();
            Meld meld = new Meld();
            meld.addTile(new Tile(hand.getTile(i)));
            melds.add(meld);
            allMelds.add(melds);
        }

        // For each tile, clone every meld and attempt to add the tile to it
        // If tile is added to the cloned meld, add this amended cloned meld to the ArrayList
        for (int i = 0; i < hand.getSize(); i++) {
            for (ArrayList<Meld> arrayList : allMelds) {
                for (int j = 0; j < arrayList.size(); j++) {
                    //Deep copy meld
                    Meld newMeld = new Meld(arrayList.get(j));

                    // If tile is added to cloned meld, add this meld to ArrayList
                    if (newMeld.addTile(new Tile(hand.getTile(i))) != null) {
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

    // Returns all possible potential melds in player's hand
    protected ArrayList<Meld> createPotentialMeldsFromHand(Hand hand) {
        ArrayList<Meld> potentialMelds = new ArrayList<>();
        // Check each pair
        for (int i = 0; i < hand.getSize() - 1; i++) {
            for (int j = i + 1; j < hand.getSize(); j++) {
               // Add the pair to a new meld and check if it's a valid potential meld (of size 2)
                Meld pair = new Meld();
                pair.addTile(hand.getTile(i));
                pair.addTile(hand.getTile(j));
                if (pair.isPotentialMeld() && pair.getSize() == 2) { potentialMelds.add(pair); }
            }
        }
        return potentialMelds;
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

    // Determines if player can win; returns updated workspace if won, otherwise returns null
    protected ArrayList<Meld> hasWinningHand(Hand hand, ArrayList<Meld> workspace) {
        // Make deep copy of workspace
        ArrayList<Meld> workspaceCopy = new ArrayList<Meld>();
        for (Meld meld : workspace) {
            workspaceCopy.add(new Meld(meld));
        }

        workspaceCopy = this.playUsingHand(hand, workspaceCopy);
        workspaceCopy = this.playUsingHandAndTable_AddToPotentialMeldsInHand(hand, workspaceCopy);
        workspaceCopy = this.playUsingHandAndTable_AddToTilesInHand(hand, workspaceCopy);
        workspaceCopy = this.playUsingHandAndTable_AddToMeldsOnTable(hand, workspaceCopy);

        if (hand.getSize() == 0) {
            return workspaceCopy;
        } else {
            return null;
        }
    }

    // Plays using only tiles in hand
    protected ArrayList<Meld> playUsingHand(Hand hand, ArrayList<Meld> workspace) {
        while (hand.getSize() > 0) {
            ArrayList<Meld> currentMelds = this.createMeldsFromHand(hand);
            Meld largestMeld = this.getLargestMeld(currentMelds);
            if (largestMeld != null) {
                workspace.add(largestMeld);
                hand.remove(largestMeld);
            } else {
                break;
            }
        }

        return workspace;
    }

    // Add tiles on table to potential melds in hand, removing from the first and last index of the meld
    protected ArrayList<Meld> playUsingHandAndTable_AddToPotentialMeldsInHand(Hand hand, ArrayList<Meld> workspace) {
        ArrayList<Meld> potentialMelds = this.createPotentialMeldsFromHand(hand);
        for (Meld potentialMeld : potentialMelds) {
            // Remove tiles from potential meld that are no longer in the player's hand (ie. that have already been played)
            for (int i = 0; i < potentialMeld.getSize(); i++) {
                if (!hand.containsTile(potentialMeld.getTile(i))) {
                    potentialMeld.setIsLocked(false);
                    potentialMeld.removeTile(i);
                    if (potentialMeld.containsJoker()) { potentialMeld.setIsLocked(true); }
                }
            }
            
            // If it's no longer a potential meld of size 2, we don't worry about it here
            if (potentialMeld.getSize() < 2) { continue; }
            
            for (int i = 0; i < workspace.size(); i++) {
                Meld tempMeld = workspace.get(i);
                
                // If meld contains a joker, don't do anything to this meld
                if (tempMeld.containsJoker()) { continue; }
                
                // Attempt to split meld and extract a tile to add to the potential meld
                if (tempMeld.getSize() >= 7) {
                    for (int j = 3; j < tempMeld.getSize() - 3; j++) {
                        if (potentialMeld.addTile(tempMeld.getTile(j)) != null) {
                            Meld splitMeld = tempMeld.splitMeld(j);
                            splitMeld.removeTile(0);
                            workspace.add(splitMeld);
                            workspace.add(potentialMeld);
                        }
                    }
                }
                
                //Otherwise, just work from the front and back of the meld
                int n = tempMeld.getSize() - 1;
                Tile firstTile = tempMeld.getTile(0);
                Tile lastTile = tempMeld.getTile(n);
                
                boolean firstTilewasInHand = false;
                boolean lastTilewasInHand = false;
                if (firstTile.isOnTable() == false) { firstTilewasInHand = true; }
                if (lastTile.isOnTable() == false) { lastTilewasInHand = true; }
                
                // If tempMeld is valid if first tile is removed, and that tile can be added to the potentialMeld: do it
                // break because we now have a valid meld that can be added to the workspace
                    
                firstTile.setOnTable(false);
                if (tempMeld.isValidIfRemoveTile(0) && potentialMeld.addTile(firstTile) != null) {
                    tempMeld.removeTile(0);
                    hand.remove(potentialMeld);
                    workspace.add(potentialMeld);
                    
                    if (firstTilewasInHand) {
                        firstTile.setOnTable(false);
                    } else {
                        firstTile.setOnTable(true);
                    }
                    break;
                }
                
                // If tempMeld is valid if last tile is removed, and that tile can be added to the potentialMeld: do it
                // break because we now have a valid meld that can be added to the workspace
                lastTile.setOnTable(false);
                if (tempMeld.isValidIfRemoveTile(n) && potentialMeld.addTile(lastTile) != null) {
                    tempMeld.removeTile(n);
                    hand.remove(potentialMeld);
                    workspace.add(potentialMeld);
                    
                    if (lastTilewasInHand) {
                        lastTile.setOnTable(false);
                    } else {
                        lastTile.setOnTable(true);
                    }
                    
                    break;
                }
                
                if (firstTilewasInHand) {
                    firstTile.setOnTable(false);
                } else {
                    firstTile.setOnTable(true);
                }
                
                if (lastTilewasInHand) {
                    lastTile.setOnTable(false);
                } else {
                    lastTile.setOnTable(true);
                }
            }
        }

        return workspace;
    }
    
    // Add tiles on table to tiles in hand, removing from the first and last index of the meld
    protected ArrayList<Meld> playUsingHandAndTable_AddToTilesInHand(Hand hand, ArrayList<Meld> workspace) {
        for (int i = 0; i < hand.getSize(); i++) {
            Meld newMeld = new Meld();
            newMeld.addTile(hand.getTile(i));

            Meld meldRemovedFrom = new Meld();
            Tile tileRemoved = new Tile("R0");

            for (int j = 0; j < workspace.size(); j++) {
                Meld tempMeld = workspace.get(j);
                
                // If meld contains a joker, don't do anything to this meld
                if (tempMeld.containsJoker()) { continue; }
                
                int n = tempMeld.getSize() - 1;
                Tile firstTile = tempMeld.getTile(0);
                Tile lastTile = tempMeld.getTile(n);
                
                boolean firstTilewasInHand = false;
                boolean lastTilewasInHand = false;
                if (firstTile.isOnTable() == false) { firstTilewasInHand = true; }
                if (lastTile.isOnTable() == false) { lastTilewasInHand = true; }
                
                // If tempMeld is valid if first tile is removed, and that tile can be added to the potentialMeld: do it
                firstTile.setOnTable(false);
                if (tempMeld.isValidIfRemoveTile(0) && newMeld.addTile(firstTile) != null) {
                    tileRemoved = tempMeld.getTile(0);
                    meldRemovedFrom = tempMeld;
                    tempMeld.removeTile(0);
                    hand.remove(tileRemoved);
                }
                
                // If tempMeld is valid if last tile is removed, and that tile can be added to the potentialMeld: do it
                lastTile.setOnTable(false);
                if (tempMeld.isValidIfRemoveTile(n) && newMeld.addTile(lastTile) != null) {
                    tileRemoved = tempMeld.getTile(n);
                    meldRemovedFrom = tempMeld;
                    tempMeld.removeTile(n);
                    hand.remove(tileRemoved);
                }
                
                if (firstTilewasInHand) {
                    firstTile.setOnTable(false);
                } else {
                    firstTile.setOnTable(true);
                }
                
                if (lastTilewasInHand) {
                    lastTile.setOnTable(false);
                } else {
                    lastTile.setOnTable(true);
                }
            }

            // If newMeld is valid, add it to the table; otherwise, disregard it
            if (newMeld.isValidMeld()) {
                workspace.add(newMeld);
                hand.remove(i);
            } else if (meldRemovedFrom != null) {
                meldRemovedFrom.addTile(tileRemoved);
            }
        }

        return workspace;
    }
    
    // Attempt to add remaining tiles to each meld on table
    protected ArrayList<Meld> playUsingHandAndTable_AddToMeldsOnTable(Hand hand, ArrayList<Meld> workspace) {
        while (hand.getSize() > 0) {
            boolean tileAdded = false;
            for (int i = 0; i < hand.getSize(); i++) {
                for (Meld tempMeld : workspace) {
                    // If meld contains a joker, don't do anything to this meld
                    if (tempMeld.containsJoker()) { continue; }
                    
                    // Attempt to add tile to each meld; if added, remove it from the hand
                    if (tempMeld.addTile(hand.getTile(i)) != null) {
                        tileAdded = true;
                        hand.remove(i);
                        break;
                    }
                }
            }
            // If no tiles were added during this iteration, we're done
            if (!tileAdded) {
                break;
            }
        }

        return workspace;
    }
    
    // Tries to add first and last tile in each meld to all proceding melds
    protected ArrayList<Meld> rearrangeWorkspace(ArrayList<Meld> workspace) {
        for (int i = 0; i < workspace.size(); i++) {
            Meld tempMeld = workspace.get(i);
            
            if (tempMeld.containsJoker()) { continue; }
            int n = tempMeld.getSize() - 1;
            Tile firstTile = tempMeld.getTile(0);
            Tile lastTile = tempMeld.getTile(n);

            if (tempMeld.isValidIfRemoveTile(0)) {
                for (int j = i; j < workspace.size(); j++) {
                    if (workspace.get(j).containsJoker()) { continue; }
                    if (workspace.get(j).addTile(firstTile) != null) {
                        tempMeld.removeTile(0);
                    }
                }
            }
            
            if (tempMeld.isValidIfRemoveTile(n)) {
                for (int j = i; j < workspace.size(); j++) {
                    if (workspace.get(j).containsJoker()) { continue; }
                    if (workspace.get(j).addTile(lastTile) != null) {
                        tempMeld.removeTile(n);
                    }
                }
            }
        }
        
        return workspace;
    }
    
    // Returns true if number of tiles in newWorkspace > number of tiles in oldWorkspace, otherwise returns false
    protected boolean tilesAddedToWorkspace(ArrayList<Meld> newWorkspace, ArrayList<Meld> oldWorkspace) {
        int newWorkspaceTileCount = 0;
        int oldWorkspaceTileCount = 0;
        for (Meld meld : newWorkspace) {
            newWorkspaceTileCount += meld.getSize();
        }
        
        for (Meld meld : oldWorkspace) {
            oldWorkspaceTileCount += meld.getSize();
        }
        
        if (newWorkspaceTileCount > oldWorkspaceTileCount) { return true; }
        return false;
    }

    protected ArrayList<Meld> parseInput(Hand hand, String input, ArrayList<Meld> workspace) { return null; }
}