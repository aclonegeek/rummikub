package core;

public class GameMemento {
    private Table savedTable;
    private Hand savedHand;
    
    public GameMemento(Table table, Hand hand) {
        this.savedTable = new Table(table);
        this.savedHand = new Hand(hand);
    }
    
    public void setTableState(Table table) {
        this.savedTable = new Table(table);
    }
    
    public void setHandState(Hand hand) {
        this.savedHand = new Hand(hand);
    }
    
    public Table getTableState() {
        return this.savedTable;
    }
    
    public Hand getHandState() {
        return this.savedHand;
    }
}
