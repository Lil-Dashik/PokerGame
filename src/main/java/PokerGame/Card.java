package PokerGame;

public class Card {
    private final Rank rank;
    private final Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }


    public Suit getSuit() {
        return suit;
    }

    public int getRankValue() {
        return rank.getValue();
    }

    @Override
    public String toString() {
        return rank.getSymbol() + suit.getSymbol();
    }

    public static Card fromString(String cardStr) {
        if (cardStr.length() < 2 || cardStr.length() > 3) {
            throw new IllegalArgumentException("Invalid card format");
        }

        String rankSymbol;
        String suitSymbol;
        if (cardStr.length() == 3) {
            rankSymbol = cardStr.substring(0, 2);
            suitSymbol = cardStr.substring(2, 3);
        } else {
            rankSymbol = cardStr.substring(0, 1);
            suitSymbol = cardStr.substring(1, 2);
        }
        Rank rank = Rank.fromSymbol(rankSymbol);
        Suit suit = Suit.fromSymbol(suitSymbol);

        return new Card(rank, suit);
    }
}
