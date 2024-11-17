package PokerGame;

import java.util.Arrays;
import java.util.List;

public class PokerHandEvaluatorTest {
    public static void main(String[] args) {
        // Карты игрока
        List<Card> playerCards = List.of(
                new Card(Rank.ACE, Suit.H),
                new Card(Rank.KING, Suit.D)
        );

        List<Card> communityCards = List.of(
                new Card(Rank.QUEEN, Suit.C),
                new Card(Rank.JACK, Suit.S),
                new Card(Rank.TEN, Suit.H),
                new Card(Rank.NINE, Suit.H),
                new Card(Rank.EIGHT, Suit.C)
        );

        // Вызов метода
        List<List<Card>> validCombinations = PokerHandEvaluator.generateValidCombinations(playerCards, communityCards);

        // Проверка логики фильтрации
        System.out.println("Общее количество валидных комбинаций: " + validCombinations.size());
        for (List<Card> combination : validCombinations) {
            System.out.println("Валидная комбинация: " + combination);
            boolean containsPlayerCard = combination.stream().anyMatch(playerCards::contains);
            System.out.println("Содержит карту игрока: " + containsPlayerCard);
            if (!containsPlayerCard) {
                throw new IllegalStateException("Ошибка! Комбинация не содержит карту игрока.");
            }
        }
    }
}

