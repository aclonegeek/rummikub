package core;

import java.util.ArrayList;

public abstract class PlayBehaviour implements TableObserver {
    protected ArrayList<Meld> workspace;

    public PlayBehaviour() {
        this.workspace = new ArrayList<>();
    }

    abstract ArrayList<Meld> determineInitialMove(Hand hand);
    abstract ArrayList<Meld> determineRegularMove(Hand hand);

    public void update(ArrayList<Meld> melds) {
        this.workspace = new ArrayList<>(melds);
    }

    public ArrayList<Meld> getWorkspace() {
        return this.workspace;
    }

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
    protected ArrayList<Meld> hasWinningHand(Hand hand) {
        // Make deep copy of workspace
        ArrayList<Meld> workspaceCopy = new ArrayList<Meld>();
        for (Meld meld : this.workspace) {
            Meld newMeld = new Meld();
            for (int i = 0; i < meld.getSize(); i++) {
                newMeld.addTile(new Tile(meld.getTile(i).toString()));
            }
            workspaceCopy.add(newMeld);
        }

        this.workspace = playUsingHand(hand);
        this.workspace = playUsingHandAndTable(hand);
        if (hand.getSize() == 0) {
            return this.workspace;
        }
        else {
            this.workspace = workspaceCopy;
            return null;
        }
    }

    // Plays using only tiles in hand
    protected ArrayList<Meld> playUsingHand(Hand hand) {
        while (hand.getSize() > 0) {
            ArrayList<Meld> currentMelds = this.createMeldsFromHand(hand);
            Meld largestMeld = this.getLargestMeld(currentMelds);
            if (largestMeld != null) {
                this.workspace.add(largestMeld);
                hand.remove(largestMeld);
            } else {
                break;
            }
        }

        return this.workspace;
    }

    // Plays using both tiles in hand and on table (but not only from hand)
    protected ArrayList<Meld> playUsingHandAndTable(Hand hand) {
        // Add tiles on table to valid melds in hand, removing from the first and last index of the meld
        ArrayList<Meld> validMelds = this.createMeldsFromHand(hand);
        for (Meld validMeld : validMelds) {
            for (int i = 0; i < this.workspace.size(); i++) {
                Meld tempMeld = this.workspace.get(i);
                if (tempMeld.isValidIfRemoveTile(0) && validMeld.addTile(tempMeld.getTile(0))) {
                    tempMeld.removeTile(0);
                    hand.remove(validMeld);
                    this.workspace.add(validMeld);
                }
                int n = tempMeld.getSize() - 1;
                if (tempMeld.isValidIfRemoveTile(n) && validMeld.addTile(tempMeld.getTile(n))) {
                    hand.remove(tempMeld.removeTile(n));
                    hand.remove(validMeld);
                    this.workspace.add(validMeld);
                }
            }
        }

        // Add tiles on table to potential melds in hand, removing from the first and last index of the meld
        ArrayList<Meld> potentialMelds = this.createPotentialMeldsFromHand(hand);
        for (Meld potentialMeld : potentialMelds) {
            for (int i = 0; i < this.workspace.size(); i++) {
                Meld tempMeld = this.workspace.get(i);
                if (tempMeld.isValidIfRemoveTile(0) && potentialMeld.addTile(tempMeld.getTile(0))) {
                    tempMeld.removeTile(0);
                    hand.remove(potentialMeld);
                    this.workspace.add(potentialMeld);
                }
                int n = tempMeld.getSize() - 1;
                if (tempMeld.isValidIfRemoveTile(n) && potentialMeld.addTile(tempMeld.getTile(n))) {
                    tempMeld.removeTile(n);
                    hand.remove(potentialMeld);
                    this.workspace.add(potentialMeld);
                }
            }
        }

        // Add tiles on table to tiles in hand, removing from the first and last index of the meld
        for (int i = 0; i < hand.getSize(); i++) {
            Meld newMeld = new Meld();
            newMeld.addTile(hand.getTile(i));

            Meld meldRemovedFrom = new Meld();
            Tile tileRemoved = new Tile("R1");

            for (int j = 0; j < this.workspace.size(); j++) {
                Meld tempMeld = this.workspace.get(j);
                if (tempMeld.isValidIfRemoveTile(0) && newMeld.addTile(tempMeld.getTile(0))) {
                    tileRemoved = tempMeld.getTile(0);
                    meldRemovedFrom = tempMeld;
                    tempMeld.removeTile(0);
                    hand.remove(tileRemoved);
                }
                int n = tempMeld.getSize() - 1;
                if (tempMeld.isValidIfRemoveTile(n) && newMeld.addTile(tempMeld.getTile(n))) {
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
                    if (tempMeld.addTile(hand.getTile(i))) {
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

        return this.workspace;
    }

    public void setWorkspace(ArrayList<Meld> workspace) {
        this.workspace = workspace;
    }
}
