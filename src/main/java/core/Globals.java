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
    }
}