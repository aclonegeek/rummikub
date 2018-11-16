package core;

import java.util.ArrayList;

public class Table {
    private ArrayList<Meld> melds;

    public Table() {
        this.melds = new ArrayList<>();
    }

    public ArrayList<Meld> getState() {
        return this.melds;
    }

    public boolean setState(ArrayList<Meld> melds) {
        if (isValidState(melds)) {
            this.melds = melds;
            return true;
        }
        return false;
    }

    private boolean isValidState(ArrayList<Meld> melds) {
        // Discard empty melds before validating them
        melds.removeIf(m -> m.getSize() == 0);

        // Check if the melds are valid
        for (int i = 0; i < melds.size(); i++) {
            if (!melds.get(i).isValidMeld()) {
                return false;
            }
        }
        return true;
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
