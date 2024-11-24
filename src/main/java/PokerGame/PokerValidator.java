package PokerGame;

import java.util.*;

import static PokerGame.DealerDeal.parseCards;

public class PokerValidator {
    private static boolean validationPerformed = false;
    public static void validateBoard(Board board) {
        if (board == null) {
            throw new InvalidPokerBoardException("Board is null! Validation cannot proceed.");
        }

        List<String> allCards = new ArrayList<>();

        if (board.getPlayerOne() != null) {
            allCards.addAll(parseCardsToString(board.getPlayerOne()));
        }
        if (board.getPlayerTwo() != null) {
            allCards.addAll(parseCardsToString(board.getPlayerTwo()));
        }
        if (board.getFlop() != null) {
            allCards.addAll(parseCardsToString(board.getFlop()));
        }
        if (board.getTurn() != null) {
            allCards.addAll(parseCardsToString(board.getTurn()));
        }
        if (board.getRiver() != null) {
            allCards.addAll(parseCardsToString(board.getRiver()));
        }
        // Проверяем дубликаты
        Set<String> uniqueCards = new HashSet<>(allCards);
        if (uniqueCards.size() != allCards.size()) {
            throw new InvalidPokerBoardException("Duplicate cards detected! Possible cheating.");
        }

        System.out.println("Board validation passed. All cards are unique.");
    }
    public static void printValidationMessageIfNeeded() {
        if (validationPerformed) {
            System.out.println("Board validation passed. All cards are unique.");
            validationPerformed = false;
        }
    }
    private static List<String> parseCardsToString(String cardsStr) {
        if (cardsStr == null || cardsStr.isBlank()) {
            return Collections.emptyList();
        }
        return DealerDeal.parseCards(cardsStr).stream()
                .map(Card::toString) // Используем стандартное строковое представление карты
                .toList();
    }
}
