package PokerGame;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

class CombinationsPokerTest {
    @Test
    void testRoyalFlush() {
        List<Card> cards = List.of(
                new Card(Rank.TEN, Suit.H),
                new Card(Rank.JACK, Suit.H),
                new Card(Rank.QUEEN, Suit.H),
                new Card(Rank.KING, Suit.H),
                new Card(Rank.ACE, Suit.H)
        );
        HandPlayer hand = CombinationsPoker.evaluateCombination(cards);
        assertEquals(Priority.ROYAL_FLUSH, hand.getPriority());
    }

    @Test
    void testStraightFlush() {
        List<Card> cards = List.of(
                new Card(Rank.NINE, Suit.S),
                new Card(Rank.TEN, Suit.S),
                new Card(Rank.JACK, Suit.S),
                new Card(Rank.QUEEN, Suit.S),
                new Card(Rank.KING, Suit.S)
        );
        HandPlayer hand = CombinationsPoker.evaluateCombination(cards);
        assertEquals(Priority.STRAIGHT_FLUSH, hand.getPriority());
    }

    @Test
    void testFourOfAKind() {
        List<Card> cards = List.of(
                new Card(Rank.NINE, Suit.S),
                new Card(Rank.NINE, Suit.H),
                new Card(Rank.NINE, Suit.D),
                new Card(Rank.NINE, Suit.C),
                new Card(Rank.KING, Suit.S)
        );
        HandPlayer hand = CombinationsPoker.evaluateCombination(cards);
        assertEquals(Priority.FOUR_OF_A_KIND, hand.getPriority());
    }

    @Test
    void testFullHouse() {
        List<Card> cards = List.of(
                new Card(Rank.THREE, Suit.S),
                new Card(Rank.THREE, Suit.H),
                new Card(Rank.THREE, Suit.D),
                new Card(Rank.KING, Suit.S),
                new Card(Rank.KING, Suit.H)
        );
        HandPlayer hand = CombinationsPoker.evaluateCombination(cards);
        assertEquals(Priority.FULL_HOUSE, hand.getPriority());
    }

    @Test
    void testFlush() {
        List<Card> cards = List.of(
                new Card(Rank.TWO, Suit.D),
                new Card(Rank.FIVE, Suit.D),
                new Card(Rank.NINE, Suit.D),
                new Card(Rank.JACK, Suit.D),
                new Card(Rank.KING, Suit.D)
        );
        HandPlayer hand = CombinationsPoker.evaluateCombination(cards);
        assertEquals(Priority.FLUSH, hand.getPriority());
    }

    @Test
    void testStraight() {
        List<Card> cards = List.of(
                new Card(Rank.FIVE, Suit.H),
                new Card(Rank.SIX, Suit.S),
                new Card(Rank.SEVEN, Suit.D),
                new Card(Rank.EIGHT, Suit.C),
                new Card(Rank.NINE, Suit.H)
        );
        HandPlayer hand = CombinationsPoker.evaluateCombination(cards);
        assertEquals(Priority.STRAIGHT, hand.getPriority());
    }

    @Test
    void testThreeOfAKind() {
        List<Card> cards = List.of(
                new Card(Rank.QUEEN, Suit.S),
                new Card(Rank.QUEEN, Suit.H),
                new Card(Rank.QUEEN, Suit.D),
                new Card(Rank.SEVEN, Suit.S),
                new Card(Rank.TWO, Suit.H)
        );
        HandPlayer hand = CombinationsPoker.evaluateCombination(cards);
        assertEquals(Priority.THREE_OF_A_KIND, hand.getPriority());
    }

    @Test
    void testTwoPair() {
        List<Card> cards = List.of(
                new Card(Rank.JACK, Suit.S),
                new Card(Rank.JACK, Suit.H),
                new Card(Rank.FOUR, Suit.D),
                new Card(Rank.FOUR, Suit.S),
                new Card(Rank.TWO, Suit.H)
        );
        HandPlayer hand = CombinationsPoker.evaluateCombination(cards);
        assertEquals(Priority.TWO_PAIR, hand.getPriority());
    }

    @Test
    void testOnePair() {
        List<Card> cards = List.of(
                new Card(Rank.EIGHT, Suit.S),
                new Card(Rank.EIGHT, Suit.H),
                new Card(Rank.FIVE, Suit.D),
                new Card(Rank.THREE, Suit.S),
                new Card(Rank.TWO, Suit.C)
        );
        HandPlayer hand = CombinationsPoker.evaluateCombination(cards);
        assertEquals(Priority.ONE_PAIR, hand.getPriority());
    }

    @Test
    void testHighCard() {
        List<Card> cards = Arrays.asList(
                new Card(Rank.ACE, Suit.C),
                new Card(Rank.KING, Suit.D),
                new Card(Rank.NINE, Suit.S),
                new Card(Rank.FIVE, Suit.H),
                new Card(Rank.THREE, Suit.C)
        );
        HandPlayer hand = CombinationsPoker.evaluateCombination(cards);
        assertEquals(Priority.HIGH_CARD, hand.getPriority());
    }
}

