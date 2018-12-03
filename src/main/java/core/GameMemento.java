package core;

public class GameMemento {
    private Table savedTable;
    private Hand savedHand;
    
    void setSavedTable(Table table) {
        this.savedTable = new Table(table);
    }
    
    void setSavedHand(Hand hand) {
        this.savedHand = new Hand(hand);
    }
    
    Table restoreSavedTable() {
        return this.savedTable;
    }
    
    Hand restoreSavedHand() {
        return this.savedHand;
    }
}
