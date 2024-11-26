package PokerGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    public static List<Card> parseCards(String cardsStr) {
        if (cardsStr == null || cardsStr.isBlank()) {
            throw new IllegalArgumentException("Input string for cards cannot be null or empty.");
        }
        // Удаляем лишние пробелы между картами
        cardsStr = cardsStr.trim().replaceAll("\\s+", "");

        List<Card> cards = new ArrayList<>();
        int i = 0;

        while (i < cardsStr.length()) {
            // Определяем длину текущей карты
            String cardStr;
            if (i + 2 <= cardsStr.length() && cardsStr.substring(i, i + 2).equals("10")) {
                cardStr = cardsStr.substring(i, i + 3); // Берем 3 символа для карты "10"
                i += 3;
            } else if (i + 1 <= cardsStr.length()) {
                cardStr = cardsStr.substring(i, i + 2); // Берем 2 символа для обычной карты
                i += 2;
            } else {
                throw new IllegalArgumentException("Invalid card format in string: " + cardsStr);
            }
            if (cardStr.isEmpty()) {
                throw new IllegalArgumentException("Encountered empty card while parsing: " + cardsStr);
            }
            //if (!cardStr.matches("^[2-9TJQKA][CDHS]$|^10[CDHS]$")) {
            //throw new IllegalArgumentException("Invalid card format: " + cardStr);
            //}
            cards.add(Card.fromString(cardStr));
        }

        return cards;
    }

    public static Card fromString(String cardStr) {
        if (cardStr == null || cardStr.length() < 2 || cardStr.length() > 3) {
            throw new InvalidPokerBoardException("Invalid card format: " + cardStr);
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

    public static List<String> parseCardsToString(String cardsStr) {
        if (cardsStr == null || cardsStr.isBlank()) {
            return Collections.emptyList();
        }
        return parseCards(cardsStr).stream()
                .map(Card::toString) // Используем стандартное строковое представление карты
                .toList();
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
