package core;

public class Globals {
    // Optional rules
    public static enum JokerRules {
        DEFAULT, LENIENT, NO_EXISTING_MELDS;
    }
    
    private static JokerRules jokerRules = JokerRules.DEFAULT;
    
    public static JokerRules getJokerRules() {
        return jokerRules;
    }
    
    public static void setJokerRules(JokerRules rule) {
        jokerRules = rule;
    }
    
    public static enum Colour {
        RED('R'), BLUE('B'), GREEN('G'), ORANGE('O');
        
        private final char symbol;
        
        private Colour (char symbol) {
            this.symbol = symbol;
        }
        
        public char getSymbol() {
            return this.symbol;
        }
        
        public int getValue() {
            return this.ordinal() + 1;
        }
        
        public static Colour getEnum(char symbol) {
            for (Colour colour : Colour.values()) {
                if (colour.symbol == symbol) { return colour; }
            }
            return null;
        }
    }
    
    public static enum PlayerType {
        PLAYERHUMAN("StrategyHuman"),
        PLAYER1("Strategy1"),
        PLAYER2("Strategy2"),
        PLAYER3("Strategy3"),
        PLAYER4("Strategy4");
        
        private final String playerType;
        
        private PlayerType(String playerType) {
            this.playerType = playerType;
        }
        
        public boolean equals(String otherPlayerType) {
            return this.playerType.equals(otherPlayerType);
        }
        
        public String toString() {
            return this.playerType;
        }
    }
}