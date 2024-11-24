package PokerGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PokerHandEvaluator {
    public static HandPlayer evaluateBestHand(List<Card> playerCards, List<Card> communityCards) {


        // Генерация всех валидных комбинаций (включающих хотя бы одну карту игрока)
        List<List<Card>> validCombinations = generateValidCombinations(playerCards, communityCards);

        HandPlayer bestHand = null;
        for (List<Card> combination : validCombinations) {
            HandPlayer evaluatedHand = CombinationsPoker.evaluateCombination(combination);
            if (evaluatedHand.getCards().size() == 5) { // Проверка, что комбинация состоит из 5 карт
                if (bestHand == null || evaluatedHand.compareTo(bestHand) > 0) {
                    bestHand = evaluatedHand;
                }
            }
        }

        if (bestHand == null) {
            throw new IllegalStateException("No valid hand found");
        }

        // Диагностический вывод для отладки

        return bestHand;
    }

    public static List<List<Card>> generateValidCombinations(List<Card> playerCards, List<Card> communityCards) {

        List<Card> allCards = new ArrayList<>(communityCards);
        allCards.addAll(playerCards);

        return generateSubCombinations(allCards);
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
