package core;

import java.util.ArrayList;

public class Table {
    private ArrayList<Meld> melds;

    public Table() {
        this.melds = new ArrayList<>();
    }
    
    public Table(Table table) {
        this.melds = new ArrayList<>();
        for (Meld meld : table.getState()) {
            Meld newMeld = new Meld(meld);
            melds.add(newMeld);
        }
    }

    public ArrayList<Meld> getState() {
        return this.melds;
    }

    public void setState(ArrayList<Meld> melds) {
        System.out.println("Table before Set: " + this.toHighlightedString(melds));
        for (Meld meld : melds) {
            meld.setIsLocked(true);
            for (int i = 0; i < meld.getSize(); i++) {
                meld.getTile(i).setOnTable(true);
            }
        }
        this.melds = melds;
    }

    public boolean isValidState() {
        // Discard empty melds before validating them
        this.melds.removeIf(m -> m.getSize() == 0);

        // Check if the melds are valid
        for (int i = 0; i < this.melds.size(); i++) {
            if (!this.melds.get(i).isValidMeld()) {
                return false;
            }
        }
        return true;
    }
    
    public String toHighlightedString(ArrayList<Meld> workspace) {
        String table = new String();
        int counter = 1;
        for (Meld meld : workspace) {
            table += counter++ + ": " + meld.toHighlightedString() +"\n";
        }
        return table;
    }

    @Override
    public String toString() {
        String table = new String();
        int counter = 1;
        for (Meld meld : this.melds) {
            table += counter++ + ": " + meld.toString() +"\n";
        }
        return table;
    }
}
