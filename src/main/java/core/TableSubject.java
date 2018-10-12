package core;

import java.util.Observer;

public interface TableSubject {
    public void registerObserver(TableObserver o);
    public void removeObserver(TableObserver o);
    public void notifyObservers();
}
