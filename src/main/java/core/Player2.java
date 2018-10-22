package core;

import java.util.ArrayList;

public class Player2 extends Player implements PlayerSubject {
    ArrayList<PlayerObserver> observers;

    public Player2() {
        this.observers = new ArrayList<>();
        this.playBehaviour = new Strategy2();
    }

	public void registerObserver(PlayerObserver observer) {
		this.observers.add(observer);
	}

	public void removeObserver(PlayerObserver observer) {
        this.observers.removeIf(o -> observer.equals(o));
	}

	public void notifyObservers() {
        this.observers.forEach(o -> o.update(this.hand.getSize()));
	}

    public ArrayList<PlayerObserver> getObservers() {
        return this.observers;
    }

    public String toString() {
        return "Player 2:\n# tiles: " + this.hand.getSize() + "\n\n";
    }
}
