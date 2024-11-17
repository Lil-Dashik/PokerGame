package PokerGame;

import java.util.HashMap;
import java.util.Map;
public enum Rank {
    TWO("2", 2), THREE("3", 3), FOUR("4", 4), FIVE("5", 5),
    SIX("6", 6), SEVEN("7", 7), EIGHT("8", 8), NINE("9", 9),
    TEN("T", 10), JACK("J", 11), QUEEN("Q", 12), KING("K", 13), ACE("A", 14);
    private final String symbol;
    private final int value;
    private static final Map<String, Rank> SYMBOL_TO_RANK_MAP = new HashMap<>();

    static {
        for (Rank rank : Rank.values()) {
            SYMBOL_TO_RANK_MAP.put(rank.symbol, rank);
        }
    }
    Rank(String symbol, int value) {
        this.symbol = symbol;
        this.value = value;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return symbol;
    }

    public static Rank fromSymbol(String symbol) {
        Rank rank = SYMBOL_TO_RANK_MAP.get(symbol);
        if (rank == null) {
            throw new IllegalArgumentException("Invalid rank symbol: " + symbol);
        }
        return rank;
    }
}
