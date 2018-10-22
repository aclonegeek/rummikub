package core;

public interface PlayerSubject {
    public void registerObserver(PlayerObserver observer);
    public void removeObserver(PlayerObserver observer);
    public void notifyObservers();
}
