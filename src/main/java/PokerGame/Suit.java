package PokerGame;

import java.util.HashMap;
import java.util.Map;

public enum Suit {
    C("C"), D("D"), H("H"), S("S");
    private final String symbol;
    private static final Map<String, Suit> SYMBOL_TO_SUIT_MAP = new HashMap<>();

    static {
        for (Suit suit : Suit.values()) {
            SYMBOL_TO_SUIT_MAP.put(suit.symbol, suit);
        }
    }

    Suit(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public String toString() {
        return symbol;
    }

    public static Suit fromSymbol(String symbol) {
        Suit suit = SYMBOL_TO_SUIT_MAP.get(symbol);
        if (suit == null) {
            throw new IllegalArgumentException("Invalid suit symbol: " + symbol);
        }
        return suit;
    }
}
