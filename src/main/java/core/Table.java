package core;

import java.util.ArrayList;

public class Table implements TableSubject {
    private ArrayList<TableObserver> observers;
    private ArrayList<Meld> melds;

    public Table() {
        this.observers = new ArrayList<>();
        this.melds = new ArrayList<>();
    }

    public void registerObserver(TableObserver observer) {
        this.observers.add(observer);
    }

    public void removeObserver(TableObserver observer) {
        this.observers.removeIf(o -> observer.equals(o));
    }

    public void notifyObservers() {
        this.observers.forEach(o -> o.update(this.melds));
    }

    public ArrayList<Meld> getState() {
        return this.melds;
    }

    public boolean setState(ArrayList<Meld> melds) {
        if (isValidState(melds)) {
            this.melds = melds;
            this.notifyObservers();
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
