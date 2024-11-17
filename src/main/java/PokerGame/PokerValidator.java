package PokerGame;

import java.util.*;

public class PokerValidator {
    private static boolean validationPerformed = false;
    public static void validateBoard(Board board) {
        // Проверяем на null, чтобы избежать ошибок при вызове методов
        if (board == null) {
            throw new InvalidPokerBoardException("Board is null! Validation cannot proceed.");
        }

        // Собираем все карты из доски
        List<String> allCards = new ArrayList<>();
        if (board.getPlayerOne() != null) {
            allCards.addAll(Arrays.asList(board.getPlayerOne().split(" ")));
        }
        if (board.getPlayerTwo() != null) {
            allCards.addAll(Arrays.asList(board.getPlayerTwo().split(" ")));
        }
        if (board.getFlop() != null) {
            allCards.addAll(Arrays.asList(board.getFlop().split(" ")));
        }
        if (board.getTurn() != null) {
            allCards.add(board.getTurn());
        }
        if (board.getRiver() != null) {
            allCards.add(board.getRiver());
        }

        // Проверяем дубликаты
        Set<String> uniqueCards = new HashSet<>(allCards);
        if (uniqueCards.size() != allCards.size()) {
            throw new InvalidPokerBoardException("Duplicate cards detected! Possible cheating.");
        }
        validationPerformed = true;
    }
    public static void printValidationMessageIfNeeded() {
        if (validationPerformed) {
            System.out.println("Board validation passed. All cards are unique.");
            validationPerformed = false; // Сбрасываем флаг для следующей проверки
        }
    }
}
