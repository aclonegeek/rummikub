package core;

import java.util.ArrayList;

public class PlayerHuman extends Player implements PlayerSubject {
    private ArrayList<PlayerObserver> observers;

    public PlayerHuman() {
        this.observers = new ArrayList<>();
        this.playBehaviour = new StrategyHuman();
    }

	public void registerObserver(PlayerObserver observer) {
		this.observers.add(observer);
	}

	public void removeObserver(PlayerObserver observer) {
        this.observers.removeIf(o -> observer.equals(o));
	}

	public void notifyObservers() {
        this.observers.forEach(o -> o.update(this.getHandSize()));
	}

    public ArrayList<PlayerObserver> getObservers() {
        return this.observers;
    }

    public String toString() {
        return "You:\n# tiles: " + this.hand.getSize() + "\n" + this.hand.toString() + "\n\n";
    }
}
