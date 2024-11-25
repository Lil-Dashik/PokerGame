package PokerGame;

import java.util.*;

import static PokerGame.DealerDeal.parseCards;

public class PokerValidator {
    public static void validateBoard(Board board, Stage stage) {
        if (board == null) {
            throw new InvalidPokerBoardException("Board is null! Validation cannot proceed.");
        }

        List<String> allCards = new ArrayList<>();
        switch (stage) {
            case RIVER -> {
                if (board.getRiver() == null || parseCards(board.getRiver()).size() != 1) {
                    throw new InvalidPokerBoardException("Invalid board state: river must consist of exactly 1 card.");
                }
                allCards.addAll(parseCardsToString(board.getRiver()));
            }
            case TURN -> {
                if (board.getTurn() == null || parseCards(board.getTurn()).size() != 1) {
                    throw new InvalidPokerBoardException("Invalid board state: turn must consist of exactly 1 card.");
                }
                allCards.addAll(parseCardsToString(board.getTurn()));
            }
            case FLOP -> {
                if (board.getFlop() == null || parseCards(board.getFlop()).size() != 3) {
                    throw new InvalidPokerBoardException("Invalid board state: flop must consist of exactly 3 cards.");
                }
                allCards.addAll(parseCardsToString(board.getFlop()));
            }
            case PLAYERS_CARDS -> {
                if (board.getPlayerOne() == null || parseCards(board.getPlayerOne()).size() != 2) {
                    throw new InvalidPokerBoardException("Invalid board state: playerOne must have exactly 2 cards.");
                }
                allCards.addAll(parseCardsToString(board.getPlayerOne()));
                if (board.getPlayerTwo() == null || parseCards(board.getPlayerTwo()).size() != 2) {
                    throw new InvalidPokerBoardException("Invalid board state: playerTwo must have exactly 2 cards.");
                }
                allCards.addAll(parseCardsToString(board.getPlayerTwo()));
            }
        }

        // Проверяем дубликаты
        Set<String> uniqueCards = new HashSet<>(allCards);
        if (uniqueCards.size() != allCards.size()) {
            throw new InvalidPokerBoardException("Duplicate cards detected! Possible cheating.");
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
