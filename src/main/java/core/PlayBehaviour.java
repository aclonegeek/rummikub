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
                    if (newMeld.addTile(hand.getTile(i)) != null) {
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
            Meld newMeld = new Meld();
            for (int i = 0; i < meld.getSize(); i++) {
                newMeld.addTile(new Tile(meld.getTile(i).toString()));
            }
            workspaceCopy.add(newMeld);
        }

        workspaceCopy = this.playUsingHand(hand, workspaceCopy);
        workspaceCopy = this.playUsingHandAndTable(hand, workspaceCopy);
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

    // Plays using both tiles in hand and on table (but not only from hand)
    protected ArrayList<Meld> playUsingHandAndTable(Hand hand, ArrayList<Meld> workspace) {
        // Add tiles on table to potential melds in hand, removing from the first and last index of the meld
        ArrayList<Meld> potentialMelds = this.createPotentialMeldsFromHand(hand);
        for (Meld potentialMeld : potentialMelds) {
            for (int i = 0; i < workspace.size(); i++) {
                Meld tempMeld = workspace.get(i);
                if (tempMeld.isValidIfRemoveTile(0) && potentialMeld.addTile(tempMeld.getTile(0)) != null) {
                    tempMeld.removeTile(0);
                    hand.remove(potentialMeld);
                    workspace.add(potentialMeld);
                    break;
                }
                int n = tempMeld.getSize() - 1;
                if (tempMeld.isValidIfRemoveTile(n) && potentialMeld.addTile(tempMeld.getTile(n)) != null) {
                    tempMeld.removeTile(n);
                    hand.remove(potentialMeld);
                    workspace.add(potentialMeld);
                    break;
                }
            }
        }

        // Add tiles on table to tiles in hand, removing from the first and last index of the meld
        for (int i = 0; i < hand.getSize(); i++) {
            Meld newMeld = new Meld();
            newMeld.addTile(hand.getTile(i));

            Meld meldRemovedFrom = new Meld();
            Tile tileRemoved = new Tile("R1");

            for (int j = 0; j < workspace.size(); j++) {
                Meld tempMeld = workspace.get(j);
                if (tempMeld.isValidIfRemoveTile(0) && newMeld.addTile(tempMeld.getTile(0)) != null) {
                    tileRemoved = tempMeld.getTile(0);
                    meldRemovedFrom = tempMeld;
                    tempMeld.removeTile(0);
                    hand.remove(tileRemoved);
                }
                int n = tempMeld.getSize() - 1;
                if (tempMeld.isValidIfRemoveTile(n) && newMeld.addTile(tempMeld.getTile(n)) != null) {
                    tileRemoved = tempMeld.getTile(n);
                    meldRemovedFrom = tempMeld;
                    tempMeld.removeTile(n);
                    hand.remove(tileRemoved);
                }
            }

            if (newMeld.isValidMeld()) {
                workspace.add(newMeld);
                hand.remove(i);
            } else if (meldRemovedFrom != null) {
                meldRemovedFrom.addTile(tileRemoved);
            }
        }

        // Add remaining tiles in hand to each meld on table
        while (hand.getSize() > 0) {
            boolean tileAdded = false;
            for (int i = 0; i < hand.getSize(); i++) {
                for (Meld tempMeld : workspace) {
                    if (tempMeld.addTile(hand.getTile(i)) != null) {
                        tileAdded = true;
                        hand.remove(i);
                        break;
                    }
                }
            }
            if (!tileAdded) {
                break;
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