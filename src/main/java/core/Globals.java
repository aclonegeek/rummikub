package core;

public class Globals {
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
}