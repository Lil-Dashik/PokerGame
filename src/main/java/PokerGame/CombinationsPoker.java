package PokerGame;

import java.util.*;
import java.util.stream.Collectors;
import java.util.concurrent.ConcurrentHashMap;


public class CombinationsPoker {
    private static final Map<String, HandPlayer> combinationCache = new ConcurrentHashMap<>();

    public static HandPlayer evaluateCombination(List<Card> cards) {
        validateCards(cards);
        String key = generateCacheKey(cards);
        if (combinationCache.containsKey(key)) {
            return combinationCache.get(key);
        }
        List<Card> sortedCards = cards.stream()
                .sorted(Comparator.comparingInt(Card::getRankValue).reversed())
                .toList();


        HandPlayer bestCombination;

        // Проверяем комбинации последовательно по приоритету
        if (RoyalFlush(sortedCards)) {
            bestCombination = new HandPlayer(Priority.ROYAL_FLUSH, getRoyalFlushCards(sortedCards));
        } else if (StraightFlush(sortedCards)) {
            bestCombination = new HandPlayer(Priority.STRAIGHT_FLUSH, getStraightFlushCards(sortedCards));
        } else if (FourOfAKind(sortedCards)) {
            bestCombination = new HandPlayer(Priority.FOUR_OF_A_KIND, getFourOfAKindCards(sortedCards));
        } else if (FullHouse(sortedCards)) {
            List<Card> fullHouseCards = getFullHouseCards(sortedCards);
            bestCombination = !fullHouseCards.isEmpty()
                    ? new HandPlayer(Priority.FULL_HOUSE, fullHouseCards)
                    : null;
        } else if (Flush(sortedCards)) {
            bestCombination = new HandPlayer(Priority.FLUSH, getFlushCards(sortedCards));
        } else if (Straight(sortedCards)) {
            bestCombination = new HandPlayer(Priority.STRAIGHT, getStraightCards(sortedCards));
        } else if (ThreeOfAKind(sortedCards)) {
            bestCombination = new HandPlayer(Priority.THREE_OF_A_KIND, getThreeOfAKindCards(sortedCards));
        } else if (TwoPair(sortedCards)) {
            bestCombination = new HandPlayer(Priority.TWO_PAIR, getTwoPairCards(sortedCards));
        } else if (OnePair(sortedCards)) {
            bestCombination = new HandPlayer(Priority.ONE_PAIR, getOnePairCards(sortedCards));
        } else {
            bestCombination = new HandPlayer(Priority.HIGH_CARD, getHighCard(sortedCards));
        }

        // Сохраняем результат в кэш
        combinationCache.put(key, bestCombination);

        return bestCombination;
    }

    private static void validateCards(List<Card> cards) {
        if (cards == null || cards.isEmpty()) {
            throw new IllegalArgumentException("Card list cannot be null or empty.");
        }

        Set<Card> uniqueCards = new HashSet<>(cards);
        if (uniqueCards.size() != cards.size()) {
            throw new IllegalArgumentException("Duplicate cards detected in the list: " + cards);
        }

        for (Card card : cards) {
            if (card == null) {
                throw new IllegalArgumentException("Null card detected in the list.");
            }
            if (card.getRankValue() < 2 || card.getRankValue() > 14) {
                throw new IllegalArgumentException("Invalid card rank: " + card);
            }
        }
    }

    private static String generateCacheKey(List<Card> cards) {
        return cards.stream()
                .sorted(Comparator.comparingInt(Card::getRankValue).thenComparing(Card::getSuit))
                .map(card -> card.getRankValue() + "-" + card.getSuit().name())
                .collect(Collectors.joining("_"));
    }


    private static boolean RoyalFlush(List<Card> cards) {
        if (!Flush(cards)) {
            return false; // Royal Flush невозможен без Flush
        }

        // Проверяем наличие всех карт для Royal Flush
        Set<Integer> royalRanks = Set.of(10, 11, 12, 13, 14); // TEN, JACK, QUEEN, KING, ACE
        return cards.stream()
                .map(Card::getRankValue)
                .collect(Collectors.toSet())
                .containsAll(royalRanks);
    }

    private static List<Card> getRoyalFlushCards(List<Card> cards) {
        Set<Integer> royalFlushRanks = Set.of(10, 11, 12, 13, 14); // TEN, JACK, QUEEN, KING, ACE
        Map<Suit, List<Card>> suitGroups = cards.stream()
                .collect(Collectors.groupingBy(Card::getSuit));

        for (List<Card> suitedCards : suitGroups.values()) {
            if (suitedCards.size() >= 5) {
                Set<Integer> suitedRanks = suitedCards.stream()
                        .map(Card::getRankValue)
                        .collect(Collectors.toSet());
                if (suitedRanks.containsAll(royalFlushRanks)) {
                    return suitedCards.stream()
                            .filter(card -> royalFlushRanks.contains(card.getRankValue()))
                            .sorted(Comparator.comparingInt(Card::getRankValue).reversed())
                            .collect(Collectors.toList());
                }
            }
        }
        return Collections.emptyList();
    }

    private static boolean StraightFlush(List<Card> cards) {
        if (!Flush(cards)) {
            return false; // Straight Flush невозможен без Flush
        }


        // Оставляем только карты одной масти
        Map<Suit, List<Card>> suitGroups = cards.stream()
                .collect(Collectors.groupingBy(Card::getSuit));
        for (List<Card> suitedCards : suitGroups.values()) {
            if (suitedCards.size() >= 5 && Straight(suitedCards)) {
                return true;
            }
        }
        return false;
    }

    private static List<Card> getStraightFlushCards(List<Card> cards) {
        Map<Suit, List<Card>> suitGroups = cards.stream()
                .collect(Collectors.groupingBy(Card::getSuit));

        for (List<Card> suitedCards : suitGroups.values()) {
            if (suitedCards.size() >= 5) {
                List<Card> sortedSuitedCards = suitedCards.stream()
                        .sorted(Comparator.comparingInt(Card::getRankValue))
                        .collect(Collectors.toList());
                List<Card> straightFlush = findStraightSequence(sortedSuitedCards);
                if (straightFlush.size() == 5) {
                    return straightFlush;
                }
            }
        }
        return Collections.emptyList();
    }

    private static boolean FullHouse(List<Card> cards) {
        return ThreeOfAKind(cards) && OnePair(cards);
    }

    private static List<Card> getFullHouseCards(List<Card> cards) {
        Map<Integer, List<Card>> rankGroups = cards.stream()
                .collect(Collectors.groupingBy(Card::getRankValue));

        List<Card> threeOfAKind = rankGroups.values().stream()
                .filter(group -> group.size() >= 3)
                .max(Comparator.comparingInt(group -> group.get(0).getRankValue()))
                .orElse(Collections.emptyList());

        if (threeOfAKind.isEmpty()) {
            return Collections.emptyList();
        }

        List<Card> remainingCards = cards.stream()
                .filter(card -> !threeOfAKind.contains(card))
                .toList();

        List<Card> pair = remainingCards.stream()
                .collect(Collectors.groupingBy(Card::getRankValue))
                .values().stream()
                .filter(group -> group.size() >= 2)
                .max(Comparator.comparingInt(group -> group.get(0).getRankValue()))
                .orElse(Collections.emptyList());

        if (!pair.isEmpty()) {
            List<Card> fullHouse = new ArrayList<>(threeOfAKind.subList(0, 3));
            fullHouse.addAll(pair.subList(0, 2));
            return fullHouse;
        }
        return Collections.emptyList();
    }

    private static boolean Flush(List<Card> cards) {
        Map<Suit, List<Card>> suitGroups = cards.stream()
                .collect(Collectors.groupingBy(Card::getSuit));
        return suitGroups.values().stream()
                .anyMatch(group -> group.size() >= 5);
    }

    private static List<Card> getFlushCards(List<Card> cards) {
        Map<Suit, List<Card>> suitGroups = cards.stream()
                .collect(Collectors.groupingBy(Card::getSuit));

        for (List<Card> suitedCards : suitGroups.values()) {
            if (suitedCards.size() >= 5) {
                return suitedCards.stream()
                        .sorted(Comparator.comparingInt(Card::getRankValue).reversed())
                        .limit(5)
                        .collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

    private static boolean Straight(List<Card> cards) {
        List<Card> sortedCards = cards.stream()
                .distinct()
                .sorted(Comparator.comparingInt(Card::getRankValue))
                .toList();

        int consecutiveCount = 1;
        for (int i = 1; i < sortedCards.size(); i++) {
            if (sortedCards.get(i).getRankValue() == sortedCards.get(i - 1).getRankValue() + 1) {
                consecutiveCount++;
                if (consecutiveCount == 5) {
                    return true;
                }
            } else {
                consecutiveCount = 1;
            }
        }

        // Проверка на A-2-3-4-5 (low straight)
        return sortedCards.size() >= 5 &&
                sortedCards.get(0).getRankValue() == 2 &&
                sortedCards.get(sortedCards.size() - 1).getRankValue() == 14;
    }

    private static List<Card> getStraightCards(List<Card> cards) {
        List<Card> sortedCards = cards.stream()
                .distinct()
                .sorted(Comparator.comparingInt(Card::getRankValue))
                .collect(Collectors.toList());

        List<Card> straight = findStraightSequence(sortedCards);
        if (straight.size() == 5) {
            return straight;
        }

        if (sortedCards.size() >= 5 &&
                sortedCards.get(0).getRankValue() == 2 &&
                sortedCards.get(sortedCards.size() - 1).getRankValue() == 14) {
            return sortedCards.stream()
                    .filter(card -> card.getRankValue() == 14 || card.getRankValue() <= 5)
                    .sorted(Comparator.comparingInt(Card::getRankValue))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private static boolean FourOfAKind(List<Card> cards) {
        return cards.stream()
                .collect(Collectors.groupingBy(Card::getRankValue))
                .values().stream()
                .anyMatch(group -> group.size() == 4);
    }

    private static List<Card> getFourOfAKindCards(List<Card> cards) {
        Map<Integer, List<Card>> rankGroups = cards.stream()
                .collect(Collectors.groupingBy(Card::getRankValue));

        for (Map.Entry<Integer, List<Card>> entry : rankGroups.entrySet()) {
            if (entry.getValue().size() == 4) {
                List<Card> fourOfAKind = new ArrayList<>(entry.getValue());
                Card kicker = cards.stream()
                        .filter(card -> card.getRankValue() != entry.getKey())
                        .max(Comparator.comparingInt(Card::getRankValue))
                        .orElse(null);
                if (kicker != null) {
                    fourOfAKind.add(kicker);
                }
                return fourOfAKind;
            }
        }
        return Collections.emptyList();
    }

    private static boolean ThreeOfAKind(List<Card> cards) {
        return cards.stream()
                .collect(Collectors.groupingBy(Card::getRankValue))
                .values().stream()
                .anyMatch(group -> group.size() == 3);
    }

    private static List<Card> getThreeOfAKindCards(List<Card> cards) {
        Map<Integer, List<Card>> rankGroups = cards.stream()
                .collect(Collectors.groupingBy(Card::getRankValue));

        for (Map.Entry<Integer, List<Card>> entry : rankGroups.entrySet()) {
            if (entry.getValue().size() == 3) {
                List<Card> threeOfAKind = new ArrayList<>(entry.getValue());
                List<Card> kickers = cards.stream()
                        .filter(card -> card.getRankValue() != entry.getKey())
                        .sorted(Comparator.comparingInt(Card::getRankValue).reversed())
                        .limit(2)
                        .toList();
                threeOfAKind.addAll(kickers);
                return threeOfAKind;
            }
        }
        return Collections.emptyList();
    }

    private static boolean TwoPair(List<Card> cards) {
        long pairCount = cards.stream()
                .collect(Collectors.groupingBy(Card::getRankValue))
                .values().stream()
                .filter(group -> group.size() == 2)
                .count();
        return pairCount >= 2;
    }

    private static List<Card> getTwoPairCards(List<Card> cards) {
        Map<Integer, List<Card>> rankGroups = cards.stream()
                .collect(Collectors.groupingBy(Card::getRankValue));

        List<List<Card>> pairs = rankGroups.values().stream()
                .filter(group -> group.size() == 2)
                .sorted((g1, g2) -> Integer.compare(g2.get(0).getRankValue(), g1.get(0).getRankValue()))
                .toList();

        if (pairs.size() >= 2) {
            List<Card> twoPairs = new ArrayList<>();
            twoPairs.addAll(pairs.get(0));
            twoPairs.addAll(pairs.get(1));

            Card kicker = cards.stream()
                    .filter(card -> !twoPairs.contains(card))
                    .max(Comparator.comparingInt(Card::getRankValue))
                    .orElse(null);
            if (kicker != null) {
                twoPairs.add(kicker);
            }
            return twoPairs;
        }
        return Collections.emptyList();
    }

    private static boolean OnePair(List<Card> cards) {
        return cards.stream()
                .collect(Collectors.groupingBy(Card::getRankValue))
                .values().stream()
                .anyMatch(group -> group.size() == 2);
    }

    private static List<Card> getOnePairCards(List<Card> cards) {
        Map<Integer, List<Card>> rankGroups = cards.stream()
                .collect(Collectors.groupingBy(Card::getRankValue));

        for (Map.Entry<Integer, List<Card>> entry : rankGroups.entrySet()) {
            if (entry.getValue().size() == 2) {
                List<Card> onePair = new ArrayList<>(entry.getValue());
                List<Card> kickers = cards.stream()
                        .filter(card -> card.getRankValue() != entry.getKey())
                        .sorted(Comparator.comparingInt(Card::getRankValue).reversed())
                        .limit(3)
                        .toList();
                onePair.addAll(kickers);
                return onePair;
            }
        }
        return Collections.emptyList();
    }

    private static List<Card> getHighCard(List<Card> cards) {
        return cards.stream()
                .sorted(Comparator.comparingInt(Card::getRankValue).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    private static List<Card> findStraightSequence(List<Card> sortedCards) {
        List<Card> straight = new ArrayList<>();

        for (int i = 0; i < sortedCards.size(); i++) {
            if (straight.isEmpty() || sortedCards.get(i).getRankValue() == straight.get(straight.size() - 1).getRankValue() + 1) {
                straight.add(sortedCards.get(i));
                 if (straight.size() == 5) {
                    return straight;
                }
            } else if (sortedCards.get(i).getRankValue() != straight.get(straight.size() - 1).getRankValue()) {
                straight.clear();
                straight.add(sortedCards.get(i));
            }
        }

        // Проверка на случай A-2-3-4-5 (low straight)
        if (straight.size() == 4 && straight.get(0).getRankValue() == 2 &&
                sortedCards.get(sortedCards.size() - 1).getRankValue() == 14) {
            straight.add(sortedCards.get(sortedCards.size() - 1));
            return straight;
        }

        return Collections.emptyList();
    }
}

