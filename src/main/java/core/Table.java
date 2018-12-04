package core;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Table {
    private ArrayList<Meld> melds;
    private ObservableList<Meld> tableList;

    public Table() {
        this.melds = new ArrayList<>();
        this.tableList = FXCollections.observableArrayList();
    }
    
    public Table(Table table) {
        this.melds = new ArrayList<>();
        for (Meld meld : table.getState()) {
            Meld newMeld = new Meld(meld);
            melds.add(newMeld);
        }
        this.tableList = FXCollections.observableArrayList();
    }

    public ObservableList<Meld> getTableList() {
        return this.tableList;
    }

    public void updateTableList() {
        this.tableList.clear();
        for (Meld meld : this.melds) {
            this.tableList.add(meld);
        }
    }

    public ArrayList<Meld> getState() {
        return this.melds;
    }

    public void setState(ArrayList<Meld> melds) {
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
