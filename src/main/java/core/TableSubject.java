package core;

public interface TableSubject {
    public void registerObserver(TableObserver player);
    public void removeObserver(TableObserver player);
    public void notifyObservers();
}
