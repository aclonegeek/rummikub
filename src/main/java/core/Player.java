package core;

import java.util.ArrayList;

public abstract class Player implements PlayerSubject, PlayerObserver {
    protected Hand hand;
    protected PlayBehaviour playBehaviour;
    protected boolean initialMove = true;
    protected String name;
    protected int lowestHandCount = 100;
    protected boolean drawing = false;

    private ArrayList<PlayerObserver> observers;

    public Player() {
        this.observers = new ArrayList<>();
        this.hand = new Hand();
    }

    abstract ArrayList<Meld> play();

    public void add(Tile tile) {
        System.out.println(this.name + " drew " + tile.toString());
        this.hand.add(tile);
    }

    public String getName() {
        return this.name;
    }

    public Hand getHand() {
        return this.hand;
    }

    public int getHandSize() {
        return this.hand.getSize();
    }

    public ArrayList<Meld> getWorkspace() {
        return this.playBehaviour.getWorkspace();
    }

    public int getLowestHandCount() {
        return this.lowestHandCount;
    }

    // Required for testing purposes
    public void setLowestHandCount(int count) {
        this.lowestHandCount = count;
    }

    protected PlayBehaviour getPlayBehaviour() {
        return this.playBehaviour;
    }

    public boolean getInitialMove() {
        return this.initialMove;
    }

    // Removes tiles from Player's hand that appear in the given ArrayList for which isOnTable is false (ie. they were just added)
    public void removeTilesFromHand(ArrayList<Meld> newTableState) {
        for (Meld meld : newTableState) {
            for (int j = 0; j < meld.getSize(); j++) {
                if (!meld.getTile(j).isOnTable()) {
                    this.hand.remove(meld.getTile(j));
                }
            }
        }
    }

    public void registerObserver(PlayerObserver observer) {
        this.observers.add(observer);
    }

    public void removeObserver(PlayerObserver observer) {
        this.observers.removeIf(o -> observer.equals(o));
    }

    public void notifyObservers() {
        this.observers.forEach(o -> o.update(drawing ? this.hand.getSize() + 1 : this.hand.getSize()));
    }

    public ArrayList<PlayerObserver> getObservers() {
        return this.observers;
    }

    public void update(int lowestHandCount) {
        if (lowestHandCount < this.lowestHandCount) {
            this.lowestHandCount = lowestHandCount;
        }
    }

    // Required for testing purposes
    public void setInitialMove(boolean initialMove) {
        this.initialMove = initialMove;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public void setWorkspace(ArrayList<Meld> workspace) {
        this.playBehaviour.workspace = workspace;
    }

    @Override
    public String toString() {
        return this.name + ": " + this.hand.getSize() + " tiles" + "\n" + this.hand.toString() + "\n";
    }
}
