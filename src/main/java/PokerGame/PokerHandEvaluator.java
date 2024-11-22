package PokerGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PokerHandEvaluator {
    public static HandPlayer evaluateBestHand(List<Card> playerCards, List<Card> communityCards) {
        List<List<Card>> validCombinations = generateValidCombinations(playerCards, communityCards);

        if (validCombinations.isEmpty()) {
            return getHighCardHand(playerCards, communityCards);
        }

        HandPlayer bestHand = null;
        for (List<Card> combination : validCombinations) {


            HandPlayer evaluatedHand = CombinationsPoker.evaluateCombination(combination);

            if (evaluatedHand.getPriority() == null || evaluatedHand.getCards().isEmpty()) {
                continue;
            }

            if (bestHand == null || evaluatedHand.compareTo(bestHand) > 0) {
                bestHand = evaluatedHand;
            }
        }
        if (bestHand == null) {
            return getHighCardHand(playerCards, communityCards);
        }
        return bestHand;
        }

    private static HandPlayer getHighCardHand(List<Card> playerCards, List<Card> communityCards) {
        List<Card> allCards = new ArrayList<>(communityCards);
        allCards.addAll(playerCards);

        List<Card> sortedCards = allCards.stream()
                .sorted(Comparator.comparingInt(Card::getRankValue).reversed())
                .limit(5)
                .collect(Collectors.toList());

        return new HandPlayer(Priority.HIGH_CARD, sortedCards);
    }
    private static List<List<Card>> generateValidCombinations(List<Card> playerCards, List<Card> communityCards) {
        List<Card> allCards = new ArrayList<>(playerCards);
        allCards.addAll(communityCards);

        List<List<Card>> combinations = new ArrayList<>();
        for (int i = 0; i < allCards.size() - 4; i++) {
            for (int j = i + 1; j < allCards.size() - 3; j++) {
                for (int k = j + 1; k < allCards.size() - 2; k++) {
                    for (int l = k + 1; l < allCards.size() - 1; l++) {
                        for (int m = l + 1; m < allCards.size(); m++) {
                            List<Card> combination = Arrays.asList(allCards.get(i), allCards.get(j), allCards.get(k), allCards.get(l), allCards.get(m));
                            if (combination.stream().anyMatch(playerCards::contains)) {
                                combinations.add(combination);
                            }
                        }
                    }
                }
            }
        }
        return combinations;
    }


    private static void generateSubCombinationsHelper(List<Card> cards, int start, List<Card> current, List<List<Card>> result) {
        if (current.size() == 5) {
            result.add(new ArrayList<>(current));
            return;
        }

        for (int i = start; i < cards.size(); i++) {
            current.add(cards.get(i));
            generateSubCombinationsHelper(cards, i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }

    private static List<List<Card>> generateSubCombinations(List<Card> cards) {
        List<List<Card>> combinations = new ArrayList<>();
        generateSubCombinationsHelper(cards, 0, new ArrayList<>(), combinations);
        return combinations;
    }
}

