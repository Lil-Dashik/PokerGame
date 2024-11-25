package PokerGame;

import java.util.Objects;

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
        if (cardStr == null || cardStr.length() < 2 || cardStr.length() > 3) {
            throw new InvalidPokerBoardException ("Invalid card format: " + cardStr);
        }

        String rankSymbol;
        String suitSymbol;

        if (cardStr.length() == 3 && cardStr.startsWith("10")) {
            rankSymbol = cardStr.substring(0, 2);
            suitSymbol = cardStr.substring(2);
        } else {
            rankSymbol = cardStr.substring(0, 1);
            suitSymbol = cardStr.substring(1);
        }

        Rank rank = Rank.fromSymbol(rankSymbol);
        Suit suit = Suit.fromSymbol(suitSymbol);

        return new Card(rank, suit);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return rank == card.rank && suit == card.suit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, suit);
    }
}
