package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Meld {
    public enum MeldType {
        INVALID, POTENTIAL, RUN, SET;
    }

    private ArrayList<Tile> meld;
    private MeldType meldType;

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

    public boolean addTile(ArrayList<Tile> tiles) {
        MeldType tempMeldType;
        ArrayList<Tile> tempMeld = new ArrayList<>();

        tempMeld.addAll(this.meld);
        tempMeld.addAll(tiles);

        // Test for a valid meld on a dummy list before changing the actual meld
        tempMeldType = determineMeldType(tempMeld);

        // Check if this is a meld or a potential meld
        if (tempMeldType != MeldType.INVALID && tempMeld.size() < 3 || tempMeldType == MeldType.RUN || tempMeldType == MeldType.SET) {
            Collections.sort(tempMeld, Comparator.comparingInt(Tile::getValue)); // Sort numerically
            this.meld = tempMeld;
            this.meldType = tempMeldType;
            return true;
        }

        return false;
    }

    public boolean addTile(Tile tile) {
        ArrayList<Tile> tempMeld = new ArrayList<>();
        tempMeld.add(tile);
        return this.addTile(tempMeld);
    }

    public Tile removeTile(int index) {
        if (index < 0 || index >= this.meld.size()) {
            return null;
         }

        Tile removedTile = this.meld.remove(index);
        this.meldType = determineMeldType(this.meld);
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
        if (copyMeld.isValidMeld()) { return true; }
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
        if (index <= 0 || index >= this.meld.size()) {
           return null;
        }

        Meld newMeld = new Meld();
        ArrayList<Tile> secondHalf = new ArrayList<>();
        int currMeldSize = this.meld.size();

        // Add the second chunk of the meld to a new ArrayList
        for (int i = index; i < currMeldSize; i++) {
            secondHalf.add(this.meld.remove(index));
        }

        // Re-determine the meld type since the current meld has changed
        this.meldType = determineMeldType(this.meld);

        // Check if the second half of the meld (which we split) is valid, return the new Meld
        newMeld.addTile(secondHalf);
        return newMeld;
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
        else if (consecutiveValues && sameColours)                                                    { return MeldType.RUN;     }
        else if (sameValues && differentColours)                                                      { return MeldType.SET;     }
        else                                                                                          { return MeldType.INVALID; }
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
        return this.meld.stream()
            .map(Object::toString)
            .collect(Collectors.joining(" ", "{", "}"));
    }
}
