package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Meld {
    public enum MeldType {
        INVALID, NONE, RUN, SET;
    }
    
    private ArrayList<Tile> meld;
    private MeldType meldType;
    
    public Meld() {
        this.meld = new ArrayList<>();
        this.meldType = MeldType.INVALID;
    }
    
    public boolean addTile(ArrayList<Tile> tiles) {
        MeldType tempMeldType;
        ArrayList<Tile> tempMeld;

        tempMeld = new ArrayList<>();
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
        } else {
            //this.meldType = tempMeldType;
            return false;
        }
    }
    
    private MeldType determineMeldType(ArrayList<Tile> tiles) {
        // Redundant melds
        if (tiles.size() <= 1) return MeldType.NONE;
        
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
        
        if ((sameValues && differentColours || consecutiveValues && sameColours) && tiles.size() < 3) { return MeldType.NONE;    } 
        else if (consecutiveValues && sameColours)                                                  { return MeldType.RUN;     } 
        else if (sameValues && differentColours)                                                    { return MeldType.SET;     }
        else                                                                                        { return MeldType.INVALID; }
    }
    
    public MeldType getMeldType() {
        return this.meldType;
    }
}