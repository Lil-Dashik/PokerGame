package PokerGame;

import java.util.LinkedList;

import java.util.Collections;
import java.util.stream.Collectors;

public class Deck {
    private final LinkedList<Card> cards;

    public Deck() {
        cards = new LinkedList<>();

        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(rank, suit));
            }
        }
        shuffle();

    }

    public void shuffle() {
        Collections.shuffle(cards);

    }

    public Card dealCard() {
        if (cards.isEmpty()) {

            throw new IllegalStateException("Deck is empty");
        }
        Card dealtCard = cards.removeLast();
        cards.addFirst(dealtCard);
        return dealtCard;
    }

    @Override
    public String toString() {
        return cards.stream()
                .map(Card::toString)
                .collect(Collectors.joining( " "));
    }

    public int size() {
        return cards.size();
    }

}
