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
}