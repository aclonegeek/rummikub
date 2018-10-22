package core;

public class Player3 extends Player implements PlayerObserver {
    private int lowestHandCount = -1;

    public Player3() {
        this.playBehaviour = new Strategy3();
    }

    public void update(int lowestHandCount) {
        if (this.lowestHandCount == -1 || lowestHandCount < this.lowestHandCount) {
            this.lowestHandCount = lowestHandCount;
        }
    }

    public int getLowestHandCount() {
        return this.lowestHandCount;
    }

    public String toString() {
        return "Player 3:\n# tiles: " + this.hand.getSize() + "\n\n";
    }
}
