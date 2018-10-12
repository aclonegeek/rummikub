package core;

public interface TableSubject {
    public void registerObserver(TableObserver o);
    public void removeObserver(TableObserver o);
    public void notifyObservers();
}
