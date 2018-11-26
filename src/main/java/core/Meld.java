package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import core.Globals.Colour;

public class Meld {
    public enum MeldType {
        INVALID, POTENTIAL, RUN, SET;
    }

    private ArrayList<Tile> meld;
    private MeldType meldType;
    private boolean isLocked = false;
    boolean isInitialMeld = true;

    public Meld() {
        this.meld = new ArrayList<>();
        this.meldType = MeldType.INVALID;
    }

    public Meld(String meld) {
        this.meld = new ArrayList<>();
        this.meldType = MeldType.INVALID;

        if (meld.length() != 0) {
            for (String tile : meld.split(",")) {
                this.addTile(new Tile(tile));
            }
        }
    }
    
    public Tile addTile(Tile tile) {
        ArrayList<Tile> tempMeld = new ArrayList<>();
        tempMeld.add(tile);
        return this.addTile(tempMeld);
    }

    public Tile removeTile(int index) {
        if (this.isLocked) { return null; }
        if (index < 0 || index >= this.meld.size()) {
            return null;
        }

        Tile removedTile = this.meld.remove(index);        
        this.meldType = determineMeldType(this.meld);
        
        // If the removed tile is a joker or a meld with a joker only is left, reset it
        if (removedTile.isJoker() || (this.meld.size() == 1 && removedTile.isJoker())) { removedTile.releaseJoker(); }
        
        // If the meld has a single joker after splitting, reset it
        return removedTile;
    }

    public boolean isValidIfRemoveTile(int index) {
        // Copy meld
        Meld copyMeld = new Meld();
        for (Tile tile : this.meld) {
            copyMeld.addTile(tile);
        }

        // Remove from copy and check if valid
        copyMeld.removeTile(index);
        if (copyMeld.isValidMeld()) {
            return true;
        }
        return false;
    }

    public Tile removeTileObject(Tile tile) {
        for (int i = 0; i < this.meld.size(); i++) {
            if (this.meld.get(i).equals(tile)) {
                return removeTile(i);
            }
        }
        return null;
    }

    public Meld splitMeld(int index) {
        if (this.isLocked) { return null; }
        if (index <= 0 || index >= this.meld.size()) { return null; }

        Meld newMeld = new Meld();
        ArrayList<Tile> secondHalf = new ArrayList<>();
        int currMeldSize = this.meld.size();

        // Add the second chunk of the meld to a new ArrayList
        for (int i = index; i < currMeldSize; i++) {
            secondHalf.add(this.meld.remove(index));
        }

        // Re-determine the meld type since the current meld has changed
        this.meldType = determineMeldType(this.meld);

        // Check if the second half of the meld (which we split) is valid, return the
        // new Meld
        newMeld.addTile(secondHalf);
        
        // If the meld has one tile left which is a joker, reset it
        if (this.meld.size() == 1 && this.meld.get(0).isJoker()) { this.meld.get(0).releaseJoker(); }
        
        // If the second half has one tile left which is a joker, reset it
        if (secondHalf.size() == 1 && secondHalf.get(0).isJoker()) { secondHalf.get(0).releaseJoker(); }
        return newMeld;
    }

    public Tile addTile(ArrayList<Tile> tiles) {
        ArrayList<Tile> tempMeld = new ArrayList<>();
        ArrayList<Tile> jokers = new ArrayList<>();
        Tile releasedJoker = null;
        
        // Disallow adding multiple tiles containing a joker (affects console game only)
        if (tiles.size() > 1) { if (tiles.removeIf(t -> t.isJoker())) { return null; } }
        
        // Check if the tile being added is part of an initial meld (so the joker inside is not replaced by it right away)
        boolean meldIsFromHand = this.meld.stream().allMatch(t -> t.isOnTable());
        if (meldIsFromHand && !tiles.get(0).isOnTable()) { this.isInitialMeld = true; }
        
        // Temporarily remove jokers from meld
        for (int i = 0; i < this.meld.size(); i++) {
            if (this.meld.get(i).isJoker()) {
                jokers.add(this.meld.remove(i));
            }
        }

        // If meld has no jokers
        if (jokers.size() == 0) {
            // Disallow adding tiles to melds that are full
            if (this.meld.size() == 13 && this.meldType == MeldType.RUN) { return null; }
            if (this.meld.size() == 4 && this.meldType == MeldType.SET)  { return null; }
            
            if (tiles.get(0).isJoker()) {
                tempMeld.addAll(this.meld);
                Tile joker = determineJokerType(tiles.get(0), tempMeld);
                tempMeld.add(joker);
                return buildMeld(tempMeld, releasedJoker);
            } else {
                tempMeld.addAll(this.meld);
                tempMeld.addAll(tiles);   
                return buildMeld(tempMeld, releasedJoker);
            }
        }
        
        // If meld has 1 joker
        if (jokers.size() == 1) {
            Tile joker = jokers.get(0);
            Tile tile = tiles.get(0);
            
            // Adding tile to a meld with a joker only
            if (this.meld.size() == 0) {
                tempMeld.add(tile);
                tempMeld.addAll(this.meld);
                tempMeld.add(determineJokerType(joker, tempMeld));
                return buildMeld(tempMeld, releasedJoker);
            }
            
            // Adding tile to a meld with a joker and one or more tiles
            if (this.meld.size() > 0) {
                // Check if the meld is locked by a joker
                if (this.isLocked) {
                    // If the joker can be replaced (unless its part of an initial meld)
                    if (joker.jokerEquals(tile) && !this.isInitialMeld) {
                        this.isLocked = false;
                        releasedJoker = jokers.remove(0);
                        tempMeld.add(tile);
                        tempMeld.addAll(this.meld);
                        return buildMeld(tempMeld, releasedJoker);
                    }
                }

                // Otherwise if the joker can't be replaced, check if the tile can still be added to the meld
                tempMeld.addAll(this.meld);
                tempMeld.add(tile);
                joker = determineJokerType(joker, tempMeld);
                if (joker == null) { return null; }
                tempMeld.add(joker);
                return buildMeld(tempMeld, releasedJoker);
            }
        }
        return null;
    }
    
    private Tile buildMeld(ArrayList<Tile> tempMeld, Tile releasedJoker) {
        MeldType tempMeldType;
        
        // Test for a valid meld on a dummy list before changing the actual meld
        tempMeldType = determineMeldType(tempMeld);

        // Check if this is a meld or a potential meld
        if (tempMeldType != MeldType.INVALID && tempMeld.size() < 3 || tempMeldType == MeldType.RUN || tempMeldType == MeldType.SET) {
            Collections.sort(tempMeld, Comparator.comparingInt(Tile::getValue)); // Sort numerically
            // Lock the meld if a joker from the hand was added
            if (!this.isLocked) {
                this.isLocked = determineLockedMeld(tempMeld);
            }
            
            this.meld = tempMeld;
            this.meldType = tempMeldType;
            
            // Return the replaced joker to be placed to the table
            if (releasedJoker != null) {
                releasedJoker.releaseJoker();
                return releasedJoker;
            }
            
            // Return a null type tile to indicate that the addition was successful
            return new Tile(null, 0);
        }
        return null;
    }
    
    // Determines the tile the joker will replace when added to a meld
    private Tile determineJokerType(Tile joker, ArrayList<Tile> meld) {
        // If the meld is empty (adding joker to new meld)
        if (meld.size() == 0) {
            return joker;
        }
        
        // If the meld is a single tile
        if (meld.size() == 1) {
            // Set the joker's alternate attributes to make this meld a potential set
            // Get a list of all the available colours
            ArrayList<Colour> availableColours = new ArrayList<>();
            for (Colour c : Colour.values()) {
                availableColours.add(c);
            }

            // Remove the colours already in use from the available colours list
            for (Tile tile : meld) {
                availableColours.remove(tile.getColour());
            }

            // The joker could be any other available colour in the set
            joker.setColour(availableColours.get(0));
            joker.setValue(meld.get(0).value);
            for (Colour c : availableColours) {
                joker.addAlternateState(new Tile(c, meld.get(0).value));
            }

            // Set the joker's alternate attributes to make this meld a potential run
            if (meld.get(0).value == 1) {
                joker.addAlternateState(new Tile(meld.get(0).colour, meld.get(0).value + 1));
            } else if (meld.get(0).value == 13) {
                joker.addAlternateState(new Tile(meld.get(0).colour, meld.get(0).value + 1));
            } else {
                joker.addAlternateState(new Tile(meld.get(0).colour, meld.get(0).value - 1));
                joker.addAlternateState(new Tile(meld.get(0).colour, meld.get(0).value + 1));
            }
        } else {
            // Case when the meld is a valid or potential run
            if (determineMeldType(meld) == MeldType.RUN || determineMeldType(meld) != MeldType.INVALID && meld.get(0).colour == meld.get(1).colour) {
                // Adding to back
                if (meld.get(0).value == 1) {
                    joker.setColour(meld.get(meld.size() - 1).colour);
                    joker.setValue(meld.get(meld.size() - 1).value + 1);
                // Adding to front
                } else if (meld.get(meld.size() - 1).value == 13) {
                    joker.setColour(meld.get(0).colour);
                    joker.setValue(meld.get(0).value - 1);
                // Add to back by default
                } else {
                    joker.setColour(meld.get(meld.size() - 1).colour);
                    joker.setValue(meld.get(meld.size() - 1).value + 1);
                }
            // Case when the meld is a valid or potential set
            } else if (determineMeldType(meld) == MeldType.SET || determineMeldType(meld) != MeldType.INVALID && meld.get(0).colour != meld.get(1).colour) {
                // Get a list of all the available colours
                ArrayList<Colour> availableColours = new ArrayList<>();
                for (Colour c : Colour.values()) {
                    availableColours.add(c);
                }

                // Remove the colours already in use from the available colours list
                for (Tile tile : meld) {
                    availableColours.remove(tile.getColour());
                }

                // Set the joker colour as the first colour in the available colours list
                joker.setColour(availableColours.get(0));
                joker.setValue(meld.get(0).value);
                
                // The joker could be any other available colour in the set
                for (Colour c : availableColours) {
                    joker.addAlternateState(new Tile(c, meld.get(0).value));
                }
            // Case when the meld is invalid (adding back a joker to make it valid)
            } else {                
                // Check if the joker can be added back to the potential meld without making it invalid
                if (meld.size() == 2) {
                    // In potential melds, jokers can have multiple forms. Check each one
                    for (Tile tile : joker.getAlternateState()) {
                        for (int i = 0; i < meld.size(); i++) {
                            meld.add(i, tile);
                            if (determineMeldType(meld) == MeldType.POTENTIAL || determineMeldType(meld) == MeldType.RUN || determineMeldType(meld) == MeldType.SET) {
                                meld.remove(i);
                                joker.setColour(tile.getColour());
                                joker.setValue(tile.getValue());
                                return joker;
                            }
                            meld.remove(i);
                        }
                    }
                    return null;
                }
                
                // Check if the joker can be added back to the meld without making it invalid
                for (int i = 0; i < meld.size(); i++) {
                    meld.add(i, joker);
                    if (determineMeldType(meld) == MeldType.POTENTIAL || determineMeldType(meld) == MeldType.RUN || determineMeldType(meld) == MeldType.SET) {
                        meld.remove(i);
                        return joker;
                    }
                    meld.remove(i);
                }
                return null;
            }
        }
        return joker;
    }

    private MeldType determineMeldType(ArrayList<Tile> tiles) {
        // Redundant melds
        if (tiles.size() <= 1) { return MeldType.POTENTIAL; }

        // Map each tile to its corresponding integer value and use this array to determine if it satisfies any Meld properties
        List<Integer> tilesByValue = tiles.stream()
                .map(tile -> tile.getValue())
                .sorted()
                .collect(Collectors.toList());

        // Calculate the differences of each consecutive pair of elements in the sorted list and if each pair
        // yields a difference of 1 then the list contains consecutive values, which makes it a potential RUN
        boolean consecutiveValues = IntStream
                .range(0, tilesByValue.size() - 1)
                .map(i -> tilesByValue.get(i + 1) - tilesByValue.get(i))
                .reduce((t1, t2) -> t1 * t2)
                .getAsInt() == 1;

        // Determine if the values in the potential SET are of the same value
        boolean sameValues = tiles.stream()
                .allMatch(tile -> tile.getValue() == tiles.get(0).getValue());

        // Count the number of distinct colours, N, in this list. N = list.size() is true distinctness, which makes it a potential RUN
        boolean differentColours = tiles.stream()
                .map(tile -> tile.getColour().getSymbol())
                .distinct()
                .collect(Collectors.counting())
                .intValue() == tiles.size();

        // Determine if the values in the potential RUN are of the same colour
        boolean sameColours = tiles.stream()
                .allMatch(tile -> tile.getColour() == tiles.get(0).getColour());

        if ((sameValues && differentColours || consecutiveValues && sameColours) && tiles.size() < 3) { return MeldType.POTENTIAL;    }
        else if (consecutiveValues && sameColours)                                                    { return MeldType.RUN;          }
        else if (sameValues && differentColours)                                                      { return MeldType.SET;          }
        else                                                                                          { return MeldType.INVALID;      }
    }
    
    // Locked meld = meld with joker that is from the hand
    private boolean determineLockedMeld(ArrayList<Tile> meld) {
        for (Tile tile : meld) {
            if (tile.isJoker() && !tile.isOnTable()) { this.isLocked = true; }
        }
        return this.isLocked;
    }
    
    public boolean isLocked() {
        return this.isLocked;
    }
    
    public void setIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }
    
    public boolean isInitialMeld() {
        return this.isInitialMeld;
    }
    
    public void setIsInitialMeld(boolean isInitialMeld) {
        this.isInitialMeld = isInitialMeld;
    }

    public int getSize() {
        return this.meld.size();
    }

    public Tile getTile(int index) {
        return this.meld.get(index);
    }

    public MeldType getMeldType() {
        return this.meldType;
    }

    public boolean isValidMeld() {
        return this.meldType == MeldType.RUN || this.meldType == MeldType.SET;
    }

    public boolean isPotentialMeld() {
        return this.meldType == MeldType.POTENTIAL;
    }

    public int getValue() {
        int total = 0;
        for (Tile tile : this.meld) {
            total += tile.getValue();
        }
        return total;
    }

    public boolean containsTile(Tile tile) {
        for (Tile tempTile : this.meld) {
            if (tempTile.toString().equals(tile.toString())) {
                return true;
            }
        }
        return false;
    }

    public boolean containsTileValue(Tile tile) {
        for (Tile tempTile : this.meld) {
            if (tempTile.getValue() == tile.getValue()) {
                return true;
            }
        }
        return false;
    }

    public boolean equals(Meld meld) {
        if (this.getSize() != meld.getSize()) {
            return false;
        }
        for (int i = 0; i < meld.getSize(); i++) {
            if (!this.containsTile(meld.getTile(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return this.meld.stream().map(Object::toString).collect(Collectors.joining(" ", "{", "}"));
    }
}