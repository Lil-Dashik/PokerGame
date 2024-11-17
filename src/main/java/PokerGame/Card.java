package PokerGame;

public class Card {
    private final Rank rank;
    private final Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
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

        String rankSymbol = cardStr.substring(0, cardStr.length() - 1);
        String suitSymbol = cardStr.substring(cardStr.length() - 1);

        Rank rank = Rank.fromSymbol(rankSymbol);
        Suit suit = Suit.fromSymbol(suitSymbol);

        return new Card(rank, suit);
    }
}
