package core;

import java.util.ArrayList;

public class Table implements TableSubject {
    private ArrayList<TableObserver> observers;
    private ArrayList<Meld> melds;

    public Table() {
        this.observers = new ArrayList<>();
        this.melds = new ArrayList<>();
    }

    public void registerObserver(TableObserver player) {
        this.observers.add(player);
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
        for (int i = 0; i < melds.size(); i++) {
            // Discard empty melds before validating them
            if (melds.get(i).getSize() == 0) {
                melds.remove(i);
            } else if (!melds.get(i).isValidMeld()) {
                return false;
            } else {
                continue;
            }
        }
        return true;
    }
}