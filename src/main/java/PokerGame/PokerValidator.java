package PokerGame;

import java.util.*;


public class PokerValidator {
    public static void validateBoard(Board board, Stage stage) {
        if (board == null) {
            throw new InvalidPokerBoardException("Board is null! Validation cannot proceed.");
        }

        List<String> allCards = new ArrayList<>();
        switch (stage) {
            case RIVER -> {
                if (board.getRiver() == null || Card.parseCards(board.getRiver()).size() != 1) {
                    throw new InvalidPokerBoardException("Invalid board state: river must consist of exactly 1 card.");
                }
                allCards.addAll(Card.parseCardsToString(board.getRiver()));
            }
            case TURN -> {
                if (board.getTurn() == null || Card.parseCards(board.getTurn()).size() != 1) {
                    throw new InvalidPokerBoardException("Invalid board state: turn must consist of exactly 1 card.");
                }
                allCards.addAll(Card.parseCardsToString(board.getTurn()));
            }
            case FLOP -> {
                if (board.getFlop() == null || Card.parseCards(board.getFlop()).size() != 3) {
                    throw new InvalidPokerBoardException("Invalid board state: flop must consist of exactly 3 cards.");
                }
                allCards.addAll(Card.parseCardsToString(board.getFlop()));
            }
            case PLAYERS_CARDS -> {
                if (board.getPlayerOne() == null || Card.parseCards(board.getPlayerOne()).size() != 2) {
                    throw new InvalidPokerBoardException("Invalid board state: playerOne must have exactly 2 cards.");
                }
                allCards.addAll(Card.parseCardsToString(board.getPlayerOne()));
                if (board.getPlayerTwo() == null || Card.parseCards(board.getPlayerTwo()).size() != 2) {
                    throw new InvalidPokerBoardException("Invalid board state: playerTwo must have exactly 2 cards.");
                }
                allCards.addAll(Card.parseCardsToString(board.getPlayerTwo()));
            }
        }

        // Проверяем дубликаты
        Set<String> uniqueCards = new HashSet<>(allCards);
        if (uniqueCards.size() != allCards.size()) {
            throw new InvalidPokerBoardException("Duplicate cards detected! Possible cheating.");
        }
    }

    public static void validateCards(List<Card> cards) {
        if (cards == null || cards.isEmpty()) {
            throw new InvalidPokerBoardException("Card list cannot be null or empty.");
        }

        Set<Card> uniqueCards = new HashSet<>(cards);
        if (uniqueCards.size() != cards.size()) {
            throw new InvalidPokerBoardException("Duplicate cards detected in the list: " + cards);
        }

        for (Card card : cards) {
            if (card == null) {
                throw new InvalidPokerBoardException("Null card detected in the list.");
            }
            if (card.getRankValue() < 2 || card.getRankValue() > 14) {
                throw new InvalidPokerBoardException("Invalid card rank: " + card);
            }
        }
    }
}
