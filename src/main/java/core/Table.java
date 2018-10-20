package core;

import java.util.ArrayList;

public class Table implements TableSubject {
    private ArrayList<TableObserver> players;
    private ArrayList<Meld> melds;

    public Table() {
        this.players = new ArrayList<>();
        this.melds = new ArrayList<>();
    }

    public void registerObserver(TableObserver player) {
        players.add(player);
    }

    public void removeObserver(TableObserver player) {
        int i = players.indexOf(player);
        if (i >= 0) {
            players.remove(i);
        }
    }

    public void notifyObservers() {
        for (int i = 0; i < players.size(); i++) {
            TableObserver player = (TableObserver) players.get(i);
            player.update(this.melds);
        }
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