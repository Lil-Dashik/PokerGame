package PokerGame;

import java.util.List;

public class HandPlayer implements Comparable<HandPlayer> {
    private final Priority priority;
    private final List<Card> cards;


    public Priority getPriority() {
        return priority;
    }

    public List<Card> getCards() {
        return cards;
    }

    @Override
    public int compareTo(HandPlayer other) {
        int priorityComparison = Integer.compare(this.priority.getPriority(), other.priority.getPriority());
        if (priorityComparison != 0) {
            return priorityComparison;
        }

        // Если приоритеты равны, сравниваем карты в комбинациях
        for (int i = 0; i < Math.min(this.cards.size(), other.cards.size()); i++) {
            int rankComparison = Integer.compare(this.cards.get(i).getRankValue(), other.cards.get(i).getRankValue());
            if (rankComparison != 0) {
                return rankComparison;
            }
        }

        // Если карты тоже равны, это ничья
        return 0;
    }

    public HandPlayer(Priority priority, List<Card> cards) {
        if (priority == null || cards == null || cards.isEmpty()) {
            throw new IllegalArgumentException("Priority and cards must not be null or empty");
        }
        this.priority = priority;
        this.cards = cards;
    }

    @Override
    public String toString() {
        return priority + " with cards: " + cards;
    }

}
