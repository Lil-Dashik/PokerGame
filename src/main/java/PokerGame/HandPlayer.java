package PokerGame;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        // Сравнение по приоритету комбинации
        int priorityComparison = this.priority.compareTo(other.priority);
        if (priorityComparison != 0) {
            return priorityComparison;
        }

        // Если приоритеты равны, сравниваем карты в комбинации
        return switch (this.priority) {
            case HIGH_CARD -> compareHighCards(this.cards, other.cards);
            case ONE_PAIR -> compareOnePair(this.cards, other.cards);
            case TWO_PAIR -> compareTwoPairs(this.cards, other.cards);
            case THREE_OF_A_KIND -> compareThreeOfAKind(this.cards, other.cards);
            case STRAIGHT, STRAIGHT_FLUSH -> compareStraight(this.cards, other.cards);
            case FLUSH -> compareFlush(this.cards, other.cards);
            case FULL_HOUSE -> compareFullHouse(this.cards, other.cards);
            case FOUR_OF_A_KIND -> compareFourOfAKind(this.cards, other.cards);
            case ROYAL_FLUSH -> 0; // Ничья при Роял-Флеше
            default -> throw new IllegalArgumentException("Unknown priority: " + this.priority);
            // Если комбинации полностью равны
        };
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
    private int compareHighCards(List<Card> cards1, List<Card> cards2) {
        for (int i = 0; i < Math.min(cards1.size(), cards2.size()); i++) {
            int cardComparison = Integer.compare(
                    cards1.get(i).getRankValue(),
                    cards2.get(i).getRankValue()
            );

            if (cardComparison != 0) {
                return cardComparison;
            }
        }
        return 0;
    }
    private int compareOnePair(List<Card> cards1, List<Card> cards2) {
        // Найти значение пары у каждого игрока
        int pair1 = findPairValue(cards1);
        int pair2 = findPairValue(cards2);

        // Сравниваем пары
        if (pair1 != pair2) {
            return Integer.compare(pair2, pair1); // Сначала сравниваем старшинство пар
        }

        // Если пары одинаковы, сравниваем кикеры
        List<Card> kickers1 = getKickers(cards1, pair1);
        List<Card> kickers2 = getKickers(cards2, pair2);

        return compareHighCards(kickers1, kickers2);
    }
    private int findPairValue(List<Card> cards) {
        // Группируем карты по рангу и находим старшую пару
        return cards.stream()
                .collect(Collectors.groupingBy(Card::getRankValue, Collectors.counting()))
                .entrySet().stream()
                .filter(entry -> entry.getValue() == 2) // Ищем только пары
                .mapToInt(Map.Entry::getKey)
                .max().orElse(0); // Возвращаем старший ранг пары
    }
    private List<Card> getKickers(List<Card> cards, int excludedValue) {
        // Исключаем карты пары и сортируем оставшиеся по старшинству
        return cards.stream()
                .filter(card -> card.getRankValue() != excludedValue)
                .sorted(Comparator.comparingInt(Card::getRankValue).reversed())
                .toList();
    }
    private int compareTwoPairs(List<Card> cards1, List<Card> cards2) {
        // Находим две старшие пары для обоих наборов карт
        List<Integer> pairs1 = findPairs(cards1);
        List<Integer> pairs2 = findPairs(cards2);

        // Сравниваем старшие пары
        if (!pairs1.get(0).equals(pairs2.get(0))) {
            return Integer.compare(pairs1.get(0), pairs2.get(0));
        }

        // Сравниваем младшие пары
        if (!pairs1.get(1).equals(pairs2.get(1))) {
            return Integer.compare(pairs1.get(1), pairs2.get(1));
        }

        // Если пары равны, сравниваем кикеры
        List<Card> remaining1 = cards1.stream()
                .filter(card -> card.getRankValue() != pairs1.get(0) && card.getRankValue() != pairs1.get(1))
                .sorted(Comparator.comparingInt(Card::getRankValue).reversed())
                .toList();

        List<Card> remaining2 = cards2.stream()
                .filter(card -> card.getRankValue() != pairs2.get(0) && card.getRankValue() != pairs2.get(1))
                .sorted(Comparator.comparingInt(Card::getRankValue).reversed())
                .toList();

        return compareHighCards(remaining1, remaining2);
    }
    private List<Integer> findPairs(List<Card> cards) {
        return cards.stream()
                .collect(Collectors.groupingBy(Card::getRankValue, Collectors.counting()))
                .entrySet().stream()
                .filter(entry -> entry.getValue() == 2)
                .map(Map.Entry::getKey)
                .sorted(Comparator.reverseOrder())
                .toList();
    }
    private int compareThreeOfAKind(List<Card> cards1, List<Card> cards2) {
        int triplet1 = findTripletValue(cards1);
        int triplet2 = findTripletValue(cards2);

        if (triplet1 != triplet2) {
            return Integer.compare(triplet1, triplet2);
        }

        return compareHighCards(getKickers(cards1, triplet1), getKickers(cards2, triplet2));
    }
    private int findTripletValue(List<Card> cards) {
        return cards.stream()
                .collect(Collectors.groupingBy(Card::getRankValue, Collectors.counting()))
                .entrySet().stream()
                .filter(entry -> entry.getValue() == 3)
                .mapToInt(Map.Entry::getKey)
                .max().orElse(0);
    }
    private int compareStraight(List<Card> cards1, List<Card> cards2) {
        return Integer.compare(
                cards1.get(cards1.size() - 1).getRankValue(),
                cards2.get(cards2.size() - 1).getRankValue()
        );
    }
    private int compareFlush(List<Card> cards1, List<Card> cards2) {
        return compareHighCards(cards1, cards2);
    }
    private int compareFullHouse(List<Card> cards1, List<Card> cards2) {
        int triplet1 = findTripletValue(cards1);
        int triplet2 = findTripletValue(cards2);

        if (triplet1 != triplet2) {
            return Integer.compare(triplet1, triplet2);
        }

        int pair1 = findPairValue(cards1);
        int pair2 = findPairValue(cards2);

        return Integer.compare(pair1, pair2);
    }
    private int compareFourOfAKind(List<Card> cards1, List<Card> cards2) {
        int four1 = findFourValue(cards1);
        int four2 = findFourValue(cards2);

        if (four1 != four2) {
            return Integer.compare(four1, four2);
        }

        return compareHighCards(getKickers(cards1, four1), getKickers(cards2, four2));
    }
    private int findFourValue(List<Card> cards) {
        return cards.stream()
                .collect(Collectors.groupingBy(Card::getRankValue, Collectors.counting()))
                .entrySet().stream()
                .filter(entry -> entry.getValue() == 4)
                .mapToInt(Map.Entry::getKey)
                .max().orElse(0);
    }
}
