package PokerGame;

import java.util.List;

public class HandPlayer implements Comparable<HandPlayer> {
    private final Priority priority;
    private final List<Card> cards;

    public HandPlayer(Priority priority, List<Card> cards) {
        if (priority == null || cards == null || cards.isEmpty()) {
            throw new IllegalArgumentException("Priority and cards must not be null or empty");
        }
        this.priority = priority;
        this.cards = cards;
    }

    public Priority getPriority() {
        return priority;
    }

    public List<Card> getCards() {
        return cards;
    }

    public static int compareHands(HandPlayer hand1, HandPlayer hand2) {
        // Сравнение приоритетов комбинаций
        int priorityComparison = Integer.compare(hand1.getPriority().getPriority(), hand2.getPriority().getPriority());
        if (priorityComparison != 0) {
            return priorityComparison; // Возвращаем разницу приоритетов
        }

        // Сортируем карты комбинаций по убыванию ранга
        List<Card> sortedHand1 = hand1.getCards().stream()
                .sorted((a, b) -> Integer.compare(b.getRankValue(), a.getRankValue()))
                .toList();
        List<Card> sortedHand2 = hand2.getCards().stream()
                .sorted((a, b) -> Integer.compare(b.getRankValue(), a.getRankValue()))
                .toList();

        // Сравниваем карты комбинации
        for (int i = 0; i < Math.min(sortedHand1.size(), sortedHand2.size()); i++) {
            int rankComparison = Integer.compare(sortedHand1.get(i).getRankValue(), sortedHand2.get(i).getRankValue());
            if (rankComparison != 0) {
                return rankComparison;
            }
        }
        return 0;
    }

    @Override
    public int compareTo(HandPlayer other) {
        return compareHands(this, other);
    }

    @Override
    public String toString() {
        return priority + " with cards: " + cards;
    }
}