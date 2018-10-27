package core;

import java.util.ArrayList;

public abstract class PlayBehaviour {
    protected boolean initialMove;
    protected ArrayList<Meld> workspace;
    
    public PlayBehaviour() {
        this.workspace = new ArrayList<>();
        this.initialMove = true;
    }
    
    public ArrayList<Meld> determineMove(Hand hand) {
        if (initialMove) {
            ArrayList<Meld> newTableState = determineInitialMove(hand);
            if (newTableState != null) initialMove = false;
            return newTableState;
        }
        return determineRegularMove(hand);
    }
    
    public void setWorkspace(ArrayList<Meld> workspace) {
        this.workspace = workspace;
    }
    
    public ArrayList<Meld> getWorkspace() {
        return this.workspace;
    }
    
    public void setInitialMove(boolean initialMove) {
        this.initialMove = initialMove;
    }
    
    public boolean getInitialMove() {
        return this.initialMove;
    }
    
    protected ArrayList<Meld> determineInitialMove(Hand hand) {
        System.out.println("determineInitialMove should have been overridden.");
        return null;
    }
    
    protected ArrayList<Meld> determineRegularMove(Hand hand) {
        System.out.println("determineRegularMove should have been overridden.");
        return null;
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
        if (filteredMelds.size() > 0) {
            Meld largestMeld = filteredMelds.get(0);
            for (Meld tempMeld : filteredMelds) {
                if (tempMeld.getSize() > largestMeld.getSize()) {
                    largestMeld = tempMeld;
                }
            }
            return largestMeld;
        }  
        return null;
    }
    
    // Returns largest meld (by size)
    protected Meld getLargestMeld(ArrayList<Meld> melds) {
        if (melds.size() == 0) return null;
        Meld largestMeld = melds.get(0);
        for (Meld tempMeld : melds) {
            if ((tempMeld.isValidMeld() && !largestMeld.isValidMeld()) ||
                (tempMeld.isValidMeld() && tempMeld.getSize() > largestMeld.getSize())) {
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
            if ((tempMeld.isValidMeld() && !greatestMeld.isValidMeld()) || 
                (tempMeld.isValidMeld() && tempMeld.getValue() > greatestMeld.getValue())) {
                greatestMeld = tempMeld;
            }
        }
        return greatestMeld;
    }
}