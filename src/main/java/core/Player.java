package core;

import java.util.ArrayList;

import core.Globals.PlayerType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class Player implements PlayerSubject, PlayerObserver {
    protected Hand hand;
    protected PlayBehaviour playBehaviour;
    protected PlayerType playerType;
    protected boolean initialMove = true;
    protected String name;
    protected int lowestHandCount = 100;
    protected boolean drawing = false;

    private ObservableList<Tile> handList;

    private ArrayList<PlayerObserver> observers;

    public Player() {
        this.observers = new ArrayList<>();
        this.hand = new Hand();
        this.handList = FXCollections.observableArrayList();
    }

    abstract ArrayList<Meld> play(ArrayList<Meld> tableState);

    public void add(Tile tile) {
        // System.out.println(this.name + " drew " + tile.toString());
        this.hand.add(tile);
    }

    public String getName() {
        return this.name;
    }

    public ObservableList<Tile> getHandList() {
        return this.handList;
    }

    public void updateHandList() {
        this.handList.clear();
        for (int i = 0; i < this.hand.getSize(); i++) {
            this.handList.add(this.hand.getTile(i));
        }
    }

    public Hand getHand() {
        return this.hand;
    }

    public int getHandSize() {
        return this.hand.getSize();
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
    
    public PlayerType getPlayerType() {
        return this.playerType;
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
    
    public int getScore() {
        int score = 0;
        for (int i = 0; i < this.hand.getSize(); i++) {
            Tile tile = this.hand.getTile(i);
            if (tile.isJoker()) {
                score += 30;
            } else if (tile.getValue() >= 2 && tile.getValue() <= 9) {
                score += tile.getValue();
            } else {
                score += 10;
            }
        }
        return score;
    }

    // Required for testing purposes
    public void setInitialMove(boolean initialMove) {
        this.initialMove = initialMove;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    @Override
    public String toString() {
        return this.name + ": " + this.hand.getSize() + " tiles" + "\n" + this.hand.toString() + "\n";
    }
}
